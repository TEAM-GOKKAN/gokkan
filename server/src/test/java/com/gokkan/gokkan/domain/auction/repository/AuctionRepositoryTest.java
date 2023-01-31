package com.gokkan.gokkan.domain.auction.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gokkan.gokkan.domain.auction.domain.Auction;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.FilterListRequest;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.ListResponse;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.SimilarListRequest;
import com.gokkan.gokkan.domain.auction.domain.type.AuctionStatus;
import com.gokkan.gokkan.domain.auction.domain.type.SortType;
import com.gokkan.gokkan.domain.category.domain.Category;
import com.gokkan.gokkan.domain.category.repository.CategoryRepository;
import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.repository.ItemRepository;
import com.gokkan.gokkan.domain.item.type.State;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.repository.MemberRepository;
import com.gokkan.gokkan.domain.style.domain.Style;
import com.gokkan.gokkan.domain.style.domain.StyleItem;
import com.gokkan.gokkan.domain.style.repository.StyleItemRepository;
import com.gokkan.gokkan.domain.style.repository.StyleRepository;
import com.gokkan.gokkan.global.querydsl.config.QueryDslConfig;
import com.gokkan.gokkan.global.security.oauth.entity.ProviderType;
import com.gokkan.gokkan.global.security.oauth.entity.Role;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@Import(QueryDslConfig.class)
class AuctionRepositoryTest {

	String styleName1 = "style1";
	String styleName2 = "style2";
	String categoryName1 = "test category1";
	String categoryName2 = "test category2";

	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private StyleItemRepository styleItemRepository;
	@Autowired
	private StyleRepository styleRepository;
	@Autowired
	private MemberRepository memberRepository;


	@DisplayName("01_00. searchAllFilter")
	@Test
	public void test_01_00() {
		//given
		Category category1 = getCategory(categoryName1);
		Category category11 = getCategory(categoryName1 + "1");
		category11.setParent(category1);
		category1.setChildren(new ArrayList<>(List.of(category11)));
		Category category2 = getCategory(categoryName2);
		Category category21 = getCategory(categoryName2 + "1");
		category21.setParent(category2);
		category2.setChildren(new ArrayList<>(List.of(category21)));

		Style style1 = getStyle(this.styleName1);
		Style style2 = getStyle(this.styleName2);
		Member member = getMember("member", "member@test.com");
		member.setNickName("nickName");

		Item item1 = getItem(category1, member, State.COMPLETE);
		item1.setStyleItems(
			new ArrayList<>(List.of(getStyleItem(style1, item1))));
		Item item2 = getItem(category2, member, State.COMPLETE);
		item2.setStyleItems(
			new ArrayList<>(List.of(getStyleItem(style2, item2))));
		Item item3 = getItem(category11, member, State.COMPLETE);
		item3.setStyleItems(
			new ArrayList<>(List.of(getStyleItem(style1, item3), getStyleItem(style2, item3))));
		Item item4 = getItem(category21, member, State.COMPLETE);
		item4.setStyleItems(
			new ArrayList<>(List.of(getStyleItem(style1, item4), getStyleItem(style2, item4))));

		getAuction(item1, member, AuctionStatus.STARTED, 100L);
		getAuction(item2, member, AuctionStatus.STARTED, 100L);
		getAuction(item3, member, AuctionStatus.STARTED, 100L);
		getAuction(item4, member, AuctionStatus.STARTED, 100L);
		getAuction(item1, member, AuctionStatus.STARTED, 200L);
		getAuction(item2, member, AuctionStatus.STARTED, 200L);
		getAuction(item3, member, AuctionStatus.STARTED, 200L);
		getAuction(item4, member, AuctionStatus.STARTED, 200L);
		getAuction(item4, member, AuctionStatus.ENDED, 100L);
		getAuction(item4, member, AuctionStatus.ENDED, 100L);
		getAuction(item4, member, AuctionStatus.ENDED, 100L);
		getAuction(item4, member, AuctionStatus.ENDED, 100L);

		//when
		Page<ListResponse> listResponses1 = auctionRepository.searchAllFilter(
			getFilterListRequest(category1, List.of(styleName1), null, null), PageRequest.of(0, 1));
		Page<ListResponse> listResponses2 = auctionRepository.searchAllFilter(
			getFilterListRequest(category2, List.of(styleName2), null, null), PageRequest.of(0, 1));
		Page<ListResponse> listResponses3 = auctionRepository.searchAllFilter(
			getFilterListRequest(category1, null, null, null), PageRequest.of(0, 1));
		Page<ListResponse> listResponses4 = auctionRepository.searchAllFilter(
			getFilterListRequest(category2, null, null, null), PageRequest.of(0, 1));

		Page<ListResponse> listResponses11 = auctionRepository.searchAllFilter(
			getFilterListRequest(category1, List.of(styleName1), 0L, 100L), PageRequest.of(0, 1));
		Page<ListResponse> listResponses22 = auctionRepository.searchAllFilter(
			getFilterListRequest(category2, List.of(styleName2), 101L, 200L), PageRequest.of(0, 1));
		Page<ListResponse> listResponses33 = auctionRepository.searchAllFilter(
			getFilterListRequest(category1, null, null, 100L), PageRequest.of(0, 1));
		Page<ListResponse> listResponses44 = auctionRepository.searchAllFilter(
			getFilterListRequest(category2, null, 150L, null), PageRequest.of(0, 1));

		FilterListRequest asc = getFilterListRequest(Category.builder().build(), null,
			null, null);
		asc.setSort(SortType.DESC);
		Page<ListResponse> listResponsesAllASC = auctionRepository.searchAllFilter(asc,
			PageRequest.of(0, 10));

		FilterListRequest desc = getFilterListRequest(Category.builder().build(), null,
			null, null);
		desc.setSort(SortType.ASC);
		Page<ListResponse> listResponsesAllDESC = auctionRepository.searchAllFilter(desc,
			PageRequest.of(0, 10));

		FilterListRequest findNickName = getFilterListRequest(Category.builder().build(), null,
			null, null);
		findNickName.setMemberNickName("nickName");
		Page<ListResponse> listResponsesNickName = auctionRepository.searchAllFilter(findNickName,
			PageRequest.of(0, 3));

		FilterListRequest findNickName2 = getFilterListRequest(Category.builder().build(), null,
			null, null);

		findNickName2.setMemberNickName("nickName2");
		Page<ListResponse> listResponsesNickName2 = auctionRepository.searchAllFilter(findNickName2,
			PageRequest.of(0, 3));

		//then
		//카테고리, 스타일 필터링 테스트1
		assertEquals(listResponses1.getContent().size(), 1);
		assertEquals(listResponses1.getTotalElements(), 4);
		assertEquals(listResponses1.getTotalPages(), 4);
		//최저가, 최대가 필터링 테스트1
		assertEquals(listResponses11.getContent().size(), 1);
		assertEquals(listResponses11.getTotalElements(), 2);
		assertEquals(listResponses11.getTotalPages(), 2);
		//카테고리, 스타일  필터링 테스트2
		assertEquals(listResponses2.getContent().size(), 1);
		assertEquals(listResponses2.getTotalElements(), 4);
		assertEquals(listResponses2.getTotalPages(), 4);
		//최저가, 최대가 필터링 테스트2
		assertEquals(listResponses22.getContent().size(), 1);
		assertEquals(listResponses22.getTotalElements(), 2);
		assertEquals(listResponses22.getTotalPages(), 2);
		//카테고리, 스타일  필터링 테스트3
		assertEquals(listResponses3.getContent().size(), 1);
		assertEquals(listResponses3.getTotalElements(), 4);
		assertEquals(listResponses3.getTotalPages(), 4);
		//최저가, 최대가 필터링 테스트2
		assertEquals(listResponses33.getContent().size(), 1);
		assertEquals(listResponses33.getTotalElements(), 2);
		assertEquals(listResponses33.getTotalPages(), 2);
		//카테고리, 스타일  필터링 테스트4
		assertEquals(listResponses4.getContent().size(), 1);
		assertEquals(listResponses4.getTotalElements(), 4);
		assertEquals(listResponses4.getTotalPages(), 4);
		//최저가, 최대가 필터링 테스트2
		assertEquals(listResponses44.getContent().size(), 1);
		assertEquals(listResponses44.getTotalElements(), 2);
		assertEquals(listResponses44.getTotalPages(), 2);
		//정렬 신규등록순 테스트
		for (int i = 0; i < listResponsesAllASC.getContent().size() - 1; i++) {
			assertTrue(listResponsesAllASC.getContent().get(i).getAuctionEndDateTime()
				.isAfter(listResponsesAllASC.getContent().get(i + 1).getAuctionEndDateTime()));
		}
		//정렬 마감임박순 테스트
		for (int i = 0; i < listResponsesAllDESC.getContent().size() - 1; i++) {
			assertTrue(listResponsesAllDESC.getContent().get(i).getAuctionEndDateTime()
				.isBefore(listResponsesAllDESC.getContent().get(i + 1).getAuctionEndDateTime()));
		}
		// 특정 맴버 경매 테스트
		assertEquals(listResponsesNickName.getContent().size(), 3);
		assertEquals(listResponsesNickName.getTotalElements(), 8);
		assertEquals(listResponsesNickName.getTotalPages(), 3);

		assertEquals(listResponsesNickName2.getContent().size(), 0);
		assertEquals(listResponsesNickName2.getTotalElements(), 0);
		assertEquals(listResponsesNickName2.getTotalPages(), 0);
	}

	@DisplayName("02_00. searchAllSimilar")
	@Test
	public void test_02_00() {
		//given
		Category category1 = getCategory(categoryName1);
		Category category2 = getCategory(categoryName2);
		Member member = getMember("member", "member@test.com");

		Item item1 = getItem(category1, member, State.COMPLETE);
		Item item2 = getItem(category1, member, State.COMPLETE);
		Item item3 = getItem(category1, member, State.COMPLETE);
		Item item4 = getItem(category1, member, State.COMPLETE);
		Item item5 = getItem(category1, member, State.COMPLETE);
		Item item6 = getItem(category1, member, State.COMPLETE);
		Item item7 = getItem(category2, member, State.COMPLETE);
		Item item8 = getItem(category2, member, State.COMPLETE);
		Item item9 = getItem(category2, member, State.COMPLETE);
		Item item10 = getItem(category2, member, State.COMPLETE);
		Item item11 = getItem(category2, member, State.COMPLETE);
		Item item12 = getItem(category2, member, State.COMPLETE);

		getAuction(item1, member, AuctionStatus.STARTED, 100L);
		getAuction(item2, member, AuctionStatus.STARTED, 100L);
		getAuction(item3, member, AuctionStatus.STARTED, 100L);
		getAuction(item4, member, AuctionStatus.STARTED, 100L);
		getAuction(item5, member, AuctionStatus.STARTED, 100L);
		getAuction(item6, member, AuctionStatus.STARTED, 200L);
		getAuction(item7, member, AuctionStatus.STARTED, 200L);
		getAuction(item8, member, AuctionStatus.STARTED, 200L);
		getAuction(item9, member, AuctionStatus.STARTED, 200L);
		getAuction(item10, member, AuctionStatus.STARTED, 300L);
		getAuction(item11, member, AuctionStatus.STARTED, 300L);
		getAuction(item12, member, AuctionStatus.STARTED, 300L);
		getAuction(item4, member, AuctionStatus.ENDED, 100L);
		getAuction(item4, member, AuctionStatus.ENDED, 100L);
		getAuction(item4, member, AuctionStatus.ENDED, 100L);
		getAuction(item4, member, AuctionStatus.ENDED, 100L);

		//when
		List<ListResponse> listResponses1 = auctionRepository.searchAllSimilar(
			getSimilarList(categoryName1, 5L));
		List<ListResponse> listResponses2 = auctionRepository.searchAllSimilar(
			getSimilarList(categoryName1, 4L));
		List<ListResponse> listResponses3 = auctionRepository.searchAllSimilar(
			getSimilarList(categoryName2, 12L));
		List<ListResponse> listResponses4 = auctionRepository.searchAllSimilar(
			getSimilarList(categoryName2, 10L));

		//then
		assertEquals(listResponses1.size(), 5);
		assertFalse(listResponses1.stream().map(ListResponse::getId).collect(Collectors.toSet())
			.contains(5L));
		assertEquals(listResponses2.size(), 5);
		assertFalse(listResponses2.stream().map(ListResponse::getId).collect(Collectors.toSet())
			.contains(4L));
		assertEquals(listResponses3.size(), 5);
		assertFalse(listResponses3.stream().map(ListResponse::getId).collect(Collectors.toSet())
			.contains(12L));
		assertEquals(listResponses4.size(), 5);
		assertFalse(listResponses4.stream().map(ListResponse::getId).collect(Collectors.toSet())
			.contains(10L));
	}

//	@DisplayName("03_00. searchMyBidAuction")
//	@Test
//	public void test_03_00() {
//		//given
//		Category category1 = getCategory(categoryName1);
//		Category category2 = getCategory(categoryName2);
//		Style style1 = getStyle(this.styleName1);
//		Style style2 = getStyle(this.styleName2);
//		Member member = getMember("member", "member@test.com");
//		member.setNickName("nickName");
//		Member member1 = getMember("member1", "member1@test.com");
//		member1.setNickName("nickName1");
//		Member member2 = getMember("member2", "member2@test.com");
//		member2.setNickName("nickName2");
//		Member member3 = getMember("member3", "member3@test.com");
//		member3.setNickName("nickName3");
//
//		Item item1 = getItem(category1, member, State.COMPLETE);
//		item1.setStyleItems(
//			new ArrayList<>(List.of(getStyleItem(style1, item1))));
//		Item item2 = getItem(category2, member, State.COMPLETE);
//		item2.setStyleItems(
//			new ArrayList<>(List.of(getStyleItem(style2, item2))));
//		Item item3 = getItem(category1, member, State.COMPLETE);
//		item3.setStyleItems(
//			new ArrayList<>(List.of(getStyleItem(style1, item3), getStyleItem(style2, item3))));
//		Item item4 = getItem(category2, member, State.COMPLETE);
//		item4.setStyleItems(
//			new ArrayList<>(List.of(getStyleItem(style1, item4), getStyleItem(style2, item4))));
//
//		getAuction(item1, member1, AuctionStatus.WAIT_PAYMENT, 100L);
//		getAuction(item2, member1, AuctionStatus.WAIT_PAYMENT, 100L);
//		getAuction(item3, member1, AuctionStatus.WAIT_PAYMENT, 100L);
//		getAuction(item4, member2, AuctionStatus.WAIT_PAYMENT, 100L);
//		getAuction(item1, member2, AuctionStatus.WAIT_PAYMENT, 200L);
//		getAuction(item2, member3, AuctionStatus.STARTED, 200L);
//		getAuction(item3, member3, AuctionStatus.STARTED, 200L);
//		getAuction(item4, member3, AuctionStatus.STARTED, 200L);
//
//		//when
//		Page<ListResponse> myBidAuctionList1 = auctionRepository
//			.searchMyBidAuction(member1.getNickName(), "결제대기", PageRequest.of(0, 3));
//
//		Page<ListResponse> myBidAuctionList2 = auctionRepository
//			.searchMyBidAuction(member2.getNickName(), "결제대기", PageRequest.of(0, 3));
//
//		Page<ListResponse> myBidAuctionList3 = auctionRepository
//			.searchMyBidAuction(member3.getNickName(), "결제대기", PageRequest.of(0, 3));
//
//		//then
//		assertEquals(myBidAuctionList1.getContent().size(), 3);
//		assertEquals(myBidAuctionList1.getTotalElements(), 3);
//		assertEquals(myBidAuctionList1.getTotalPages(), 1);
//
//		assertEquals(myBidAuctionList2.getContent().size(), 2);
//		assertEquals(myBidAuctionList2.getTotalElements(), 2);
//		assertEquals(myBidAuctionList2.getTotalPages(), 1);
//
//		assertEquals(myBidAuctionList3.getContent().size(), 0);
//		assertEquals(myBidAuctionList3.getTotalElements(), 0);
//		assertEquals(myBidAuctionList3.getTotalPages(), 0);
//	}

	private SimilarListRequest getSimilarList(String category, Long id) {
		return SimilarListRequest.builder()
			.category(category)
			.auctionId(id)
			.build();
	}

	private FilterListRequest getFilterListRequest(Category category1, List<String> styleNames,
		Long min, Long max) {
		return FilterListRequest.builder()
			.styles(styleNames)
			.category(category1.getName())
			.minPrice(min)
			.maxPrice(max)
			.sort(SortType.ASC)
			.auctionStatus(AuctionStatus.STARTED)
			.build();
	}


	private void getAuction(Item item, Member member,
		AuctionStatus auctionStatus, Long price) {
		auctionRepository.save(
			Auction.builder()
				.startDateTime(LocalDateTime.now())
				.endDateTime(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
				.startPrice(100L)
				.currentPrice(price)
				.auctionStatus(auctionStatus)
				.item(item)
				.member(member)
				.build());
	}

	private Member getMember(String userId, String email) {
		return memberRepository.save(Member.builder()
			.userId(userId)
			.email(email)
			.name("name")
			.providerType(ProviderType.KAKAO)
			.role(Role.ADMIN)
			.build());
	}

	private Category getCategory(String name) {
		return categoryRepository.save(Category.builder()
			.name(name)
			.children(new ArrayList<>())
			.build());
	}

	private Item getItem(Category category, Member member, State state) {
		return itemRepository.save(Item.builder()
			.name("test name")
			.member(member)
			.category(category)
			.startPrice(100L)
			.width(100L)
			.depth(100L)
			.height(100L)
			.material("나무")
			.conditionGrade("test CG")
			.conditionDescription("test CD")
			.text("test text")
			.designer("test designer")
			.brand("test brand")
			.productionYear(2023)
			.state(state)
			.created(LocalDateTime.now())
			.updated(LocalDateTime.now())
			.imageItems(new ArrayList<>())
			.imageChecks(new ArrayList<>())
			.styleItems(new ArrayList<>())
			.build());
	}

	private StyleItem getStyleItem(Style style, Item item) {
		return styleItemRepository.save(StyleItem.builder()
			.style(style)
			.name(style.getName())
			.item(item)
			.build());
	}

	private Style getStyle(String name) {
		return styleRepository.save(
			Style.builder()
				.name(name)
				.build());
	}
}