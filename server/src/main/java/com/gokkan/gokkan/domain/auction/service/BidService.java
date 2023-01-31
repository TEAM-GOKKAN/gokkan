package com.gokkan.gokkan.domain.auction.service;

import com.gokkan.gokkan.domain.auction.domain.Auction;
import com.gokkan.gokkan.domain.auction.domain.AuctionHistory;
import com.gokkan.gokkan.domain.auction.domain.AutoBidding;
import com.gokkan.gokkan.domain.auction.domain.History;
import com.gokkan.gokkan.domain.auction.domain.type.AuctionStatus;
import com.gokkan.gokkan.domain.auction.exception.AuctionErrorCode;
import com.gokkan.gokkan.domain.auction.repository.AuctionHistoryRepository;
import com.gokkan.gokkan.domain.auction.repository.AuctionRepository;
import com.gokkan.gokkan.domain.auction.repository.AutoBiddingRepository;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BidService {

	private final SimpMessageSendingOperations simpMessageSendingOperations;
	private final AuctionRepository auctionRepository;
	private final RedissonClient redissonClient;
	private final RedisTemplate<String, String> redisTemplate;
	private final AuctionHistoryRepository auctionHistoryRepository;
	private final AutoBiddingRepository autoBiddingRepository;


	@Transactional
	public void bidding(Member member, Long auctionId, Long bidPrice) {
		log.info("멤버 아이디 : " + member.getId() + "가 입찰을 시작합니다.");
		log.info("경매 아이디 : " + auctionId);
		log.info("입찰 가격 : " + bidPrice);

		Auction auction = auctionFindById(auctionId);

		final String lockName = auctionId + ":lock";
		final RLock lock = redissonClient.getLock(lockName);
		tryLock(lock);

		List<History> history = getHistory(auctionId);
		Long currentPrice;
		if (history.isEmpty()) {
			log.info("history is empty");
			currentPrice = auction.getCurrentPrice();
		} else {
			History lastHistory = history.get(0);
			if (lastHistory.getMemberId().equals(member.getId())) {
				throw new RestApiException(AuctionErrorCode.AUCTION_ALREADY_BID);
			}
			currentPrice = lastHistory.getPrice();
		}
		checkBidPrice(bidPrice, auction);

		LocalDateTime currentEndDateTime = auction.getEndDateTime();
		if (Duration.between(LocalDateTime.now(), currentEndDateTime).getSeconds() < 60) {
			auction.setEndDateTime(currentEndDateTime.plusSeconds(60));
		}

		log.info("현재 진행중인 사람 : {} & 입찰가 : {}원", member.getId(), bidPrice);
		History currentHistory = History.builder()
			.memberId(member.getId())
			.price(bidPrice)
			.bidTime(LocalDateTime.now())
			.build();
		history.add(0, currentHistory);
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObject2 = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		LocalDateTime bidDateTime = auction.getStartDateTime();
		for (History h : history) {
			JSONObject object = new JSONObject();
			int secretValue = (int) (
				(h.getMemberId() + auction.getId()) +
					(bidDateTime.getYear() * bidDateTime.getMonth().getValue()) +
					(bidDateTime.getSecond() + bidDateTime.getMinute() + bidDateTime.getHour()));
			String secretId = String.format("%05d",
				secretValue).substring(0, 5);
			object.put("memberId", secretId);
			object.put("price", h.getPrice());
			object.put("bidTime", h.getBidTime().toString());
			jsonArray.add(object);
		}
		jsonObject.put("history", jsonArray);
		jsonObject.put("currentPrice", bidPrice);
		jsonObject2.put("endDateTime", auction.getEndDateTime().toString());

		auction.setCurrentPrice(bidPrice);
		auction.setMember(member);
		auctionRepository.save(auction);
		auctionHistoryRepository.save(
			AuctionHistory.builder()
				.member(member)
				.auction(auction)
				.price(bidPrice)
				.bidDateTime(LocalDateTime.now())
				.build());
		saveRedisHistory(auction.getId(), currentHistory);
		simpMessageSendingOperations.convertAndSend("/topic/" + auctionId,
			jsonObject.toString());
		if (currentEndDateTime != auction.getEndDateTime()) {
			simpMessageSendingOperations.convertAndSend("/topic/endDateTime/" + auctionId,
				jsonObject2.toString());
		}

		unLock(lock);

		log.info("입찰 성공");

		AutoBidding autoBidding = autoBiddingRepository.findFirstByAuctionAndMemberNotAndPriceGreaterThanEqualOrderByCreatedDateAsc(
			auction, member, auction.getCurrentPrice() + 10000);
		if (autoBidding != null) {
			log.info("자동 입찰 진행");
			bidding(autoBidding.getMember(), auctionId, auction.getCurrentPrice() + 10000);
		}
	}

	@Transactional
	public void registrationAutoBid(Member member, Long auctionId, Long price) {
		log.info("멤버 아이디 : " + member.getId());
		log.info("경매 아이디 : " + auctionId);
		log.info("최대 입찰가 : " + price);
		log.info("자동 입찰 등록");
		Auction auction = auctionFindById(auctionId);
		checkBidPrice(price, auction);

		AutoBidding autoBidding = autoBiddingRepository.findByAuctionAndMember(auction, member);
		if (autoBidding == null) {
			autoBiddingRepository.save(AutoBidding.builder()
				.auction(auction)
				.member(member)
				.price(price)
				.build());
			log.info("자동 입찰 등록 성공");
		} else {
			autoBidding.setPrice(price);
			autoBiddingRepository.save(autoBidding);
			log.info("자동 입찰 수정 성공");
		}

		if (!auction.getMember().getId().equals(member.getId())) {
			bidding(member, auctionId, auction.getCurrentPrice() + 10000L);
		}
	}

	private void saveRedisHistory(Long auctionId, History currentHistory) {
		redisTemplate.opsForList().leftPush(auctionId.toString(), currentHistory.toString());
	}


	private List<History> getHistory(Long auctionId) {
		List<String> StringHistory = redisTemplate.opsForList()
			.range(String.valueOf(auctionId), 0, -1);
		if (StringHistory == null || StringHistory.isEmpty()) {
			return new ArrayList<>();
		}
		return StringHistory.stream()
			.map(History::toHistory)
			.collect(Collectors.toList());
	}

	private Auction auctionFindById(Long auctionId) {
		Auction auction = auctionRepository.findById(auctionId)
			.orElseThrow(() -> new RestApiException(
				AuctionErrorCode.AUCTION_NOT_FOUND));
		if (auction.getAuctionStatus() == AuctionStatus.ENDED || auction.getEndDateTime()
			.isBefore(LocalDateTime.now())) {
			throw new RestApiException(AuctionErrorCode.AUCTION_ALREADY_ENDED);
		}
		return auction;
	}

	private void tryLock(RLock lock) {
		try {
			if (!lock.tryLock(1, 3, TimeUnit.SECONDS)) {
				throw new RestApiException(AuctionErrorCode.AUCTION_ANOTHER_USER_IS_BIDDING);
			}
		} catch (InterruptedException e) {
			throw new RestApiException(AuctionErrorCode.AUCTION_FAILED_TO_GET_LOCK);
		}
		log.info("lock acquired");
	}

	private void unLock(RLock lock) {
		if (lock.isLocked()) {
			lock.unlock();
			log.info("lock released");
		}
	}

	private void checkBidPrice(Long bidPrice, Auction auction) {
		Long currentPrice = auction.getCurrentPrice();
		Long startPrice = auction.getStartPrice();
		if (!Objects.equals(currentPrice, startPrice)) {
			if (currentPrice >= bidPrice) {
				throw new RestApiException(
					AuctionErrorCode.AUCTION_PRICE_IS_LOWER_THAN_CURRENT_PRICE);
			} else if (currentPrice + 10000L > bidPrice) {
				throw new RestApiException(
					AuctionErrorCode.AUCTION_PRICE_IS_LOWER_THAN_BID_INCREMENT);
			}
		} else {
			if (currentPrice > bidPrice) {
				throw new RestApiException(
					AuctionErrorCode.AUCTION_PRICE_IS_LOWER_THAN_CURRENT_PRICE);
			}
		}
	}
}
