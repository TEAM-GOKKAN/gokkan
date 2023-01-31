package com.gokkan.gokkan.domain.expertComment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.gokkan.gokkan.domain.auction.domain.Auction;
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
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExpertCommentServiceTest {

	ArgumentCaptor<ExpertComment> expertCommentArgumentCaptor = ArgumentCaptor.forClass(
		ExpertComment.class);
	ArgumentCaptor<Auction> auctionArgumentCaptor = ArgumentCaptor.forClass(Auction.class);
	@Mock
	private ExpertCommentRepository expertCommentRepository;
	@Mock
	private ExpertInfoRepository expertInfoRepository;
	@Mock
	private ItemRepository itemRepository;
	@Mock
	private AuctionRepository auctionRepository;
	@InjectMocks
	private ExpertCommentService expertCommentService;

	@Test
	@DisplayName("전문가 의견 생성 완료")
	void createExpertComment() {
		//given
		given(expertInfoRepository.findByMember(any())).willReturn(
			Optional.of(getExpertInfo()));
		given(itemRepository.findById(any())).willReturn(Optional.of(getItem(State.ASSESSING)));
		given(expertCommentRepository.existsByExpertInfoAndItem(any(), any())).willReturn(false);
		given(expertCommentRepository.save(any())).willReturn(getExpertComment());
		given(auctionRepository.save(any())).willReturn(getAuction());

		//when
		expertCommentService.createExpertComment(getMember(),
			getRequestCreateExpertComment(State.COMPLETE));
		verify(expertCommentRepository).save(expertCommentArgumentCaptor.capture());
		verify(auctionRepository).save(auctionArgumentCaptor.capture());

		//then
		ExpertComment expertComment = expertCommentArgumentCaptor.getValue();
		Auction auction = auctionArgumentCaptor.getValue();
		assertThat(expertComment.getComment()).isEqualTo("comment");
		assertThat(expertComment.getMinPrice()).isEqualTo(1000L);
		assertThat(expertComment.getMaxPrice()).isEqualTo(2000L);
		assertThat(auction.getStartPrice()).isEqualTo(1000L);
		assertThat(auction.getCurrentPrice()).isEqualTo(1000L);
		assertThat(auction.getExpertComment().getComment()).isEqualTo(expertComment.getComment());
	}

	@Test
	@DisplayName("전문가 의견 생성 완료 (반려된 의견 업데이트)")
	void createExpertComment_update() {
		//given
		given(expertInfoRepository.findByMember(any())).willReturn(
			Optional.of(getExpertInfo()));
		given(itemRepository.findById(any())).willReturn(Optional.of(getItem(State.ASSESSING)));
		given(expertCommentRepository.existsByExpertInfoAndItem(any(), any())).willReturn(true);
		given(expertCommentRepository.findByExpertInfoAndItem(any(), any())).willReturn(
			getExpertComment());
		given(expertCommentRepository.save(any())).willReturn(getExpertComment());
		given(auctionRepository.save(any())).willReturn(getAuction());

		//when
		expertCommentService.createExpertComment(getMember(),
			getRequestCreateExpertComment(State.COMPLETE));
		verify(expertCommentRepository).save(expertCommentArgumentCaptor.capture());
		verify(auctionRepository).save(auctionArgumentCaptor.capture());

		//then
		ExpertComment expertComment = expertCommentArgumentCaptor.getValue();
		Auction auction = auctionArgumentCaptor.getValue();
		assertThat(expertComment.getComment()).isEqualTo("comment");
		assertThat(expertComment.getMinPrice()).isEqualTo(1000L);
		assertThat(expertComment.getMaxPrice()).isEqualTo(2000L);
		assertThat(auction.getStartPrice()).isEqualTo(1000L);
		assertThat(auction.getCurrentPrice()).isEqualTo(1000L);
		assertThat(auction.getExpertComment().getComment()).isEqualTo(expertComment.getComment());
	}

	@Test
	@DisplayName("전문가 의견 생성 실패 - 멤버 정보 없음")
	void createExpertComment_error_memberNotFound() {
		//given

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertCommentService.createExpertComment(null,
				getRequestCreateExpertComment(State.COMPLETE));
		});
		//then
		assertThat(restApiException.getErrorCode()).isEqualTo(MemberErrorCode.MEMBER_NOT_FOUND);
	}

	@Test
	@DisplayName("전문가 의견 생성 실패 - 전문가 정보 없음")
	void createExpertComment_error_expertInfoNotFound() {
		//given
		given(expertInfoRepository.findByMember(any())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertCommentService.createExpertComment(getMember(),
				getRequestCreateExpertComment(State.COMPLETE));
		});

		//then
		assertThat(restApiException.getErrorCode()).isEqualTo(
			ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND);
	}

	@Test
	@DisplayName("전문가 의견 생성 실패 - 상품 정보 없음")
	void createExpertComment_error_notFoundItem() {
		//given
		given(expertInfoRepository.findByMember(any())).willReturn(Optional.of(getExpertInfo()));
		given(itemRepository.findById(any())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertCommentService.createExpertComment(getMember(),
				getRequestCreateExpertComment(State.COMPLETE));
		});

		//then
		assertThat(restApiException.getErrorCode()).isEqualTo(ItemErrorCode.NOT_FOUND_ITEM);
	}

	@Test
	@DisplayName("전문가 의견 생성 실패 - 상품 상태가 검수중이 아님")
	void createExpertComment_error_itemStateNotAssessing() {
		//given
		given(expertInfoRepository.findByMember(any())).willReturn(Optional.of(getExpertInfo()));
		given(itemRepository.findById(any())).willReturn(Optional.of(getItem(State.TEMPORARY)));

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertCommentService.createExpertComment(getMember(),
				getRequestCreateExpertComment(State.COMPLETE));
		});

		//then
		assertThat(restApiException.getErrorCode()).isEqualTo(
			ExpertCommentErrorCode.ITEM_STATE_NOT_ASSESSING);
	}

	@Test
	@DisplayName("전문가 의견 조회 성공")
	void getExpertComment_success() {
		//given
		given(itemRepository.findById(any())).willReturn(Optional.of(getItem(State.COMPLETE)));
		given(expertCommentRepository.findByItem(any())).willReturn(
			Optional.of(getExpertComment()));

		//when
		ResponseExpertComment responseExpertComment = expertCommentService.getExpertComment(1L);

		//then
		assertThat(responseExpertComment.getName()).isEqualTo("name");
		assertThat(responseExpertComment.getComment()).isEqualTo("comment");
		assertThat(responseExpertComment.getMinPrice()).isEqualTo(1000L);
		assertThat(responseExpertComment.getMaxPrice()).isEqualTo(2000L);
	}

	@Test
	@DisplayName("전문가 의견 조회 실패 - 상품 정보 없음")
	void getExpertComment_error_notFoundItem() {
		//given
		given(itemRepository.findById(any())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertCommentService.getExpertComment(1L);
		});

		//then
		assertThat(restApiException.getErrorCode()).isEqualTo(ItemErrorCode.NOT_FOUND_ITEM);
	}

	@Test
	@DisplayName("전문가 의견 조회 실패 - 전문가 의견 정보 없음")
	void getExpertComment_error_notFoundExpertComment() {
		//given
		given(itemRepository.findById(any())).willReturn(Optional.of(getItem(State.COMPLETE)));
		given(expertCommentRepository.findByItem(any())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertCommentService.getExpertComment(1L);
		});

		//then
		assertThat(restApiException.getErrorCode()).isEqualTo(
			ExpertCommentErrorCode.NOT_FOUND_EXPERT_COMMENT);
	}


	private Auction getAuction() {
		return Auction.builder()
			.startPrice(1000L)
			.currentPrice(1000L)
			.build();
	}

	private RequestCreateExpertComment getRequestCreateExpertComment(State state) {
		return RequestCreateExpertComment.builder()
			.itemId(1L)
			.comment("comment")
			.minPrice(1000L)
			.maxPrice(2000L)
			.status(state)
			.build();
	}

	private Member getMember() {
		return Member.builder()
			.name("name")
			.build();
	}

	private ExpertInfo getExpertInfo() {
		return ExpertInfo.builder()
			.member(getMember())
			.name("name")
			.info("info")
			.build();
	}

	private Item getItem(State state) {
		return Item.builder()
			.name("name")
			.thumbnail("thumbnail")
			.startPrice(1000L)
			.state(state)
			.build();
	}

	private ExpertComment getExpertComment() {
		return ExpertComment.builder()
			.expertInfo(getExpertInfo())
			.item(getItem(State.ASSESSING))
			.comment("comment")
			.minPrice(1000L)
			.maxPrice(2000L)
			.build();
	}
}