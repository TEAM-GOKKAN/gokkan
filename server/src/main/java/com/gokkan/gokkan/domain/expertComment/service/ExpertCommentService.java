package com.gokkan.gokkan.domain.expertComment.service;

import com.gokkan.gokkan.domain.auction.domain.Auction;
import com.gokkan.gokkan.domain.auction.domain.type.AuctionStatus;
import com.gokkan.gokkan.domain.auction.repository.AuctionRepository;
import com.gokkan.gokkan.domain.expertComment.domain.ExpertComment;
import com.gokkan.gokkan.domain.expertComment.domain.dto.ExpertCommentDto.RequestCreateExpertComment;
import com.gokkan.gokkan.domain.expertComment.domain.dto.ExpertCommentDto.ResponseExpertComment;
import com.gokkan.gokkan.domain.expertComment.exception.ExpertCommentErrorCode;
import com.gokkan.gokkan.domain.expertComment.repository.ExpertCommentRepository;
import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import com.gokkan.gokkan.domain.expertInfo.exception.ExpertInfoErrorCode;
import com.gokkan.gokkan.domain.expertInfo.repository.ExpertInfoRepository;
import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.exception.ItemErrorCode;
import com.gokkan.gokkan.domain.item.repository.ItemRepository;
import com.gokkan.gokkan.domain.item.type.State;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.exception.MemberErrorCode;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExpertCommentService {

	private final ExpertCommentRepository expertCommentRepository;
	private final ExpertInfoRepository expertInfoRepository;
	private final ItemRepository itemRepository;
	private final AuctionRepository auctionRepository;

	@Transactional
	public void createExpertComment(Member member,
		RequestCreateExpertComment requestCreateExpertComment) {
		log.info("전문가 코멘트 생성");
		if (member == null) {
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		ExpertInfo expertInfo = expertInfoRepository.findByMember(member)
			.orElseThrow(() -> new RestApiException(ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND));
		log.info("전문가 정보 아이디 : " + expertInfo.getId() + " 아이템 아이디 : "
			+ requestCreateExpertComment.getItemId());
		Item item = itemRepository.findById(requestCreateExpertComment.getItemId())
			.orElseThrow(() -> new RestApiException(ItemErrorCode.NOT_FOUND_ITEM));
		State requestState = requestCreateExpertComment.getStatus();
		if (!isStateAssessing(item.getState())) {
			throw new RestApiException(ExpertCommentErrorCode.ITEM_STATE_NOT_ASSESSING);
		}
		ExpertComment expertComment;
		if (expertCommentRepository.existsByExpertInfoAndItem(expertInfo, item)) {
			log.info("반려된 의견 있음");
			expertComment = expertCommentRepository.findByExpertInfoAndItem(expertInfo, item);
			expertComment.update(requestCreateExpertComment.getComment(),
				requestCreateExpertComment.getMinPrice(), requestCreateExpertComment.getMaxPrice());
			expertCommentRepository.save(expertComment);
		} else {
			expertComment = expertCommentRepository.save(
				RequestCreateExpertComment.toEntity(requestCreateExpertComment, expertInfo, item));
		}
		if (isStateReturn(requestState)) {
			item.setState(State.RETURN);
			log.info("반려");
		} else if (isStateComplete(requestState)) {
			item.setState(State.COMPLETE);
			Auction auction = auctionRepository.save(createAuction(item, expertComment));
			log.info("감정완료, 경매 등록 완료 경매 아이디 : " + auction.getId());
		} else {
			throw new RestApiException(ExpertCommentErrorCode.NOT_FOUND_STATE);
		}
	}

	@Transactional(readOnly = true)
	public ResponseExpertComment getExpertComment(Long itemId) {
		log.info("전문가 코멘트 조회 itemId : " + itemId);
		Item item = itemRepository.findById(itemId).orElseThrow(() -> new RestApiException(
			ItemErrorCode.NOT_FOUND_ITEM));
		ExpertComment expertComment = expertCommentRepository.findByItem(item)
			.orElseThrow(() -> new RestApiException(
				ExpertCommentErrorCode.NOT_FOUND_EXPERT_COMMENT));
		ExpertInfo expertInfo = expertComment.getExpertInfo();
		ResponseExpertComment responseExpertComment = ResponseExpertComment.builder()
			.name(expertInfo.getName())
			.profileImageUrl(expertInfo.getMember().getProfileImageUrl())
			.comment(expertComment.getComment())
			.minPrice(expertComment.getMinPrice())
			.maxPrice(expertComment.getMaxPrice())
			.build();
		responseExpertComment.setStyles(expertInfo.getExpertStyles());
		log.info("전문가 코멘트 조회 완료");
		return responseExpertComment;
	}


	private boolean isStateAssessing(State state) {
		return state.equals(State.ASSESSING);
	}

	private boolean isStateReturn(State state) {
		return state.equals(State.RETURN);
	}

	private boolean isStateComplete(State state) {
		return state.equals(State.COMPLETE);
	}

	private Auction createAuction(Item item, ExpertComment expertComment) {
		return Auction.builder()
			.startDateTime(LocalDateTime.now())
			.endDateTime(LocalDateTime.now().plusDays(7))
			.startPrice(item.getStartPrice())
			.currentPrice(item.getStartPrice())
			.auctionStatus(AuctionStatus.STARTED)
			.expertComment(expertComment)
			.item(item)
			.build();
	}

	private ExpertComment updateExpertComment(ExpertComment expertComment,
		RequestCreateExpertComment requestCreateExpertComment) {
		expertComment.update(requestCreateExpertComment.getComment(),
			requestCreateExpertComment.getMinPrice(), requestCreateExpertComment.getMaxPrice());
		return expertComment;
	}
}
