package com.gokkan.gokkan.domain.auction.service;

import com.gokkan.gokkan.domain.auction.domain.Auction;
import com.gokkan.gokkan.domain.auction.domain.AuctionHistory;
import com.gokkan.gokkan.domain.auction.domain.History;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.AuctionOrderDetailAddress;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.AuctionOrderDetailItem;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.AuctionOrderDetailPaymentAmount;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.FilterListRequest;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.ListResponse;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.ResponseAuctionHistory;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.ResponseAuctionInfo;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.SimilarListRequest;
import com.gokkan.gokkan.domain.auction.domain.type.AuctionStatus;
import com.gokkan.gokkan.domain.auction.exception.AuctionErrorCode;
import com.gokkan.gokkan.domain.auction.repository.AuctionHistoryRepository;
import com.gokkan.gokkan.domain.auction.repository.AuctionRepository;
import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.exception.ItemErrorCode;
import com.gokkan.gokkan.domain.item.repository.ItemRepository;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionService {

	private final AuctionRepository auctionRepository;
	private final AuctionHistoryRepository auctionHistoryRepository;
	private final ItemRepository itemRepository;
	private final RedisTemplate<String, String> redisTemplate;

	@Transactional(readOnly = true)
	public ResponseAuctionInfo getAuctionInfo(Long auctionId) {
		log.info("현재가, 마감시간 조회");
		Auction auction = auctionRepository.findById(auctionId)
			.orElseThrow(() -> new RestApiException(AuctionErrorCode.AUCTION_NOT_FOUND));
		return ResponseAuctionInfo.builder()
			.auctionEndDateTime(auction.getEndDateTime())
			.currentPrice(auction.getCurrentPrice())
			.build();
	}

	@Transactional(readOnly = true)
	public List<ResponseAuctionHistory> getAuctionHistory(Long auctionId) {
		Auction auction = auctionRepository.findById(auctionId)
			.orElseThrow(() -> new RestApiException(AuctionErrorCode.AUCTION_NOT_FOUND));
		if (!auction.getAuctionStatus().equals(AuctionStatus.STARTED)) {
			List<AuctionHistory> auctionHistories = auctionHistoryRepository.findAllByAuctionIdOrderByBidDateTimeDesc(
				auctionId);
			return auctionHistories.stream()
				.map(auctionHistory -> ResponseAuctionHistory.builder()
					.bidTime(auctionHistory.getBidDateTime())
					.memberId(makeSecretId(auction, auction.getMember().getId()))
					.price(auctionHistory.getPrice())
					.build())
				.collect(Collectors.toList());
		}
		List<History> auctionHistories = getHistory(auction.getId());
		if (auctionHistories.isEmpty()) {
			return new ArrayList<>();
		}
		List<ResponseAuctionHistory> responseAuctionHistories = new ArrayList<>();
		for (History h : auctionHistories) {
			responseAuctionHistories.add(
				ResponseAuctionHistory.of(makeSecretId(auction, h.getMemberId()), h.getPrice(),
					h.getBidTime()));
		}
		return responseAuctionHistories;
	}

	@Transactional(readOnly = true)
	public Page<ListResponse> readList(FilterListRequest filterListRequest, Pageable pageable) {
		log.info("readList : " + filterListRequest);

		Page<ListResponse> result = auctionRepository.searchAllFilter(filterListRequest, pageable);
		log.info("readList : getTotalElements : " + result.getTotalElements());
		log.info("readList : getTotalPages : " + result.getTotalPages());
		log.info("readList : getContent.size : " + result.getContent().size());
		return result;
	}

	@Transactional(readOnly = true)
	public List<ListResponse> similarList(SimilarListRequest similarListRequest) {
		log.info("similarList : " + similarListRequest);
		List<ListResponse> result = auctionRepository.searchAllSimilar(similarListRequest);
		log.info("similarList : size : " + result.size());
		return result;
	}

	@Transactional
	public AuctionOrderDetailAddress getAddressInfo(Member member) {
		return AuctionOrderDetailAddress.builder()
			.name(member.getName())
			.phoneNumber(member.getPhoneNumber())
			.address(member.getAddress())
			.addressDetail(member.getAddressDetail())
			.build();
	}

	@Transactional(readOnly = true)
	public AuctionOrderDetailItem getItemInfo(Long auctionId, Long itemId) {
		Auction auction = auctionRepository.findById(auctionId)
			.orElseThrow(() -> new RestApiException(AuctionErrorCode.AUCTION_NOT_FOUND));
		isWaitPayment(auction);
		Item item = itemRepository.findById(itemId).orElseThrow(() -> new RestApiException(
			ItemErrorCode.NOT_FOUND_ITEM));
		return AuctionOrderDetailItem.builder()
			.id(auction.getId())
			.itemId(item.getId())
			.itemName(item.getName())
			.thumbnail(item.getThumbnail())
			.price(auction.getCurrentPrice())
			.build();
	}

	@Transactional(readOnly = true)
	public AuctionOrderDetailPaymentAmount getPaymentAmount(Long auctionId) {
		Auction auction = auctionRepository.findById(auctionId)
			.orElseThrow(() -> new RestApiException(AuctionErrorCode.AUCTION_NOT_FOUND));
		isWaitPayment(auction);
		Long hammerPrice = auction.getCurrentPrice();
		Long fee = hammerPrice / 10;
		Long paymentAmount = hammerPrice + fee;
		return AuctionOrderDetailPaymentAmount.builder()
			.hammerPrice(hammerPrice)
			.fee(fee)
			.paymentAmount(paymentAmount)
			.build();
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

	private void isWaitPayment(Auction auction) {
		if (!auction.getAuctionStatus().equals(AuctionStatus.WAIT_PAYMENT)) {
			throw new RestApiException(AuctionErrorCode.AUCTION_STATUS_IS_NOT_WAIT_PAYMENT);
		}
	}

	private String makeSecretId(Auction auction, Long memberId) {
		LocalDateTime bidDateTime = auction.getStartDateTime();
		int secretValue = (int) (
			(memberId + auction.getId()) +
				(bidDateTime.getYear() * bidDateTime.getMonth().getValue()) +
				(bidDateTime.getSecond() + bidDateTime.getMinute() + bidDateTime.getHour()));
		return String.format("%05d",
			secretValue).substring(0, 5);
	}
}
