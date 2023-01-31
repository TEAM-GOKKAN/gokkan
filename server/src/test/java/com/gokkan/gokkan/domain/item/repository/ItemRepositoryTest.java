package com.gokkan.gokkan.domain.item.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gokkan.gokkan.domain.category.domain.Category;
import com.gokkan.gokkan.domain.category.repository.CategoryRepository;
import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import com.gokkan.gokkan.domain.expertInfo.repository.ExpertInfoRepository;
import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.dto.ItemDto.ListResponse;
import com.gokkan.gokkan.domain.item.type.State;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.repository.MemberRepository;
import com.gokkan.gokkan.domain.style.domain.ExpertStyle;
import com.gokkan.gokkan.domain.style.domain.Style;
import com.gokkan.gokkan.domain.style.domain.StyleItem;
import com.gokkan.gokkan.domain.style.repository.ExpertStyleRepository;
import com.gokkan.gokkan.domain.style.repository.StyleItemRepository;
import com.gokkan.gokkan.domain.style.repository.StyleRepository;
import com.gokkan.gokkan.global.querydsl.config.QueryDslConfig;
import com.gokkan.gokkan.global.security.oauth.entity.ProviderType;
import com.gokkan.gokkan.global.security.oauth.entity.Role;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@Import(QueryDslConfig.class)
class ItemRepositoryTest {

	String style1 = "style1";
	String style2 = "style2";
	String style3 = "style3";
	String style4 = "style4";
	String categoryName1 = "category1";
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private StyleItemRepository styleItemRepository;
	@Autowired
	private StyleRepository styleRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private ExpertInfoRepository expertInfoRepository;
	@Autowired
	private ExpertStyleRepository expertStyleRepository;

	@DisplayName("01_00. searchAllMyItem success size 2")
	@Test
	public void test_01_00() {
		//given
		// member(사용자) 생성
		Member member = getMember("test id", "test1@test.com");

		// 스타일 두개 생성
		Style styleSaved1 = styleRepository.save(Style.builder().name(style1).build());
		Style styleSaved2 = styleRepository.save(Style.builder().name(style2).build());

		// 카테고리 생성
		Category category = getCategory(categoryName1);

		// 상품1 생성, 상품 이미지, 검수용 이미지, 스타일 등록
		Item item1 = itemRepository.save(
			getItem(category, member, State.TEMPORARY));

		// 상품2 생성, 상품 이미지, 검수용 이미지, 스타일 등록
		Item item2 = itemRepository.save(
			getItem(category, member, State.TEMPORARY));

		//when
		// 임시 저장상태 상품 member(작성자) 가 만든 것 list 찾기
		Page<ListResponse> items11 = itemRepository.searchAllMyItem(
			new ArrayList<>(List.of(State.TEMPORARY)), member, PageRequest.of(0, 1));
		Page<ListResponse> items12 = itemRepository.searchAllMyItem(
			new ArrayList<>(List.of(State.TEMPORARY)), member, PageRequest.of(1, 1));

		// 임시 저장상태 상품 상품 등록 안한 사람 아이디로 상품 list 찾기
		Page<ListResponse> items2 = itemRepository.searchAllMyItem(
			new ArrayList<>(List.of(State.TEMPORARY)), getMember("another", "test2@test.com"),
			PageRequest.of(0, 2));

		// 검수 대기 상품 member(작성자) 가 만든 것 list 찾기
		Page<ListResponse> items3 = itemRepository.searchAllMyItem(
			new ArrayList<>(List.of(State.ASSESSING)), member, PageRequest.of(0, 2));

		//then
		assertEquals(items11.getContent().size(), 1);
		assertEquals(items11.getSize(), 1);
		assertEquals(items11.getTotalElements(), 2);
		assertEquals(items12.getContent().size(), 1);
		assertEquals(items12.getSize(), 1);
		assertEquals(items12.getTotalElements(), 2);
		assertEquals(items2.getContent().size(), 0);
		assertEquals(items3.getContent().size(), 0);
	}

	@DisplayName("02_00. searchAllItemForExport success")
	@Test
	public void test_02_00() {
		//given
		// member(전문가) 생성
		Member member1 = getMember("test id1", "test1@test.com");
		Member member2 = getMember("test id2", "test2@test.com");
		Member member123 = getMember("test id123", "test123@test.com");
		// 전문가 정보 생성
		ExpertInfo expertInfo1 = getExpertInfo(member1);
		ExpertInfo expertInfo2 = getExpertInfo(member2);
		ExpertInfo expertInfo123 = getExpertInfo(member123);

		// 스타일 1, 2, 3, 4 생성
		Style styleSaved1 = styleRepository.save(Style.builder().name(style1).build());
		Style styleSaved2 = styleRepository.save(Style.builder().name(style2).build());
		Style styleSaved3 = styleRepository.save(Style.builder().name(style3).build());
		Style styleSaved4 = styleRepository.save(Style.builder().name(style4).build());

		// 전문가 스타일 등록
		List<ExpertStyle> expertStyles1 = expertStyleRepository.saveAll(new ArrayList<>(List.of(
			getExpertStyle(styleSaved1, expertInfo1))));
		expertInfo1.setExpertStyles(expertStyles1);

		List<ExpertStyle> expertStyles2 = expertStyleRepository.saveAll(new ArrayList<>(List.of(
			getExpertStyle(styleSaved2, expertInfo2))));
		expertInfo2.setExpertStyles(expertStyles2);

		List<ExpertStyle> expertStyles123 = expertStyleRepository.saveAll(new ArrayList<>(List.of(
			getExpertStyle(styleSaved1, expertInfo123),
			getExpertStyle(styleSaved2, expertInfo123),
			getExpertStyle(styleSaved3, expertInfo123))));
		expertInfo123.setExpertStyles(expertStyles123);

		// 카테고링 생성
		Category category = getCategory(categoryName1);

		// 상품1 생성, 상품 스타일 등록
		Item item1 = itemRepository.save(
			getItem(category, member1, State.ASSESSING));
		item1.setStyleItems(styleItemRepository.saveAll(new ArrayList<>(List.of(
			getStyleItem(styleSaved1, item1)))));

		// 상품2 생성, 상품 스타일 등록
		Item item2 = itemRepository.save(
			getItem(category, member1, State.ASSESSING));
		item2.setStyleItems(styleItemRepository.saveAll(new ArrayList<>(List.of(
			getStyleItem(styleSaved2, item2)))));

		// 상품3
		Item item3 = itemRepository.save(
			getItem(category, member1, State.ASSESSING));
		item3.setStyleItems(styleItemRepository.saveAll(new ArrayList<>(List.of(
			getStyleItem(styleSaved3, item3)))));

		// 상품4
		Item item4 = itemRepository.save(
			getItem(category, member1, State.ASSESSING));
		item4.setStyleItems(styleItemRepository.saveAll(new ArrayList<>(List.of(
			getStyleItem(styleSaved4, item4)))));

		// 상품5
		Item item12 = itemRepository.save(
			getItem(category, member1, State.ASSESSING));
		item12.setStyleItems(styleItemRepository.saveAll(new ArrayList<>(List.of(
			getStyleItem(styleSaved1, item12),
			getStyleItem(styleSaved2, item12)))));

		// 상품6
		Item item123 = itemRepository.save(
			getItem(category, member1, State.TEMPORARY));
		item12.setStyleItems(styleItemRepository.saveAll(new ArrayList<>(List.of(
			getStyleItem(styleSaved1, item123),
			getStyleItem(styleSaved2, item123),
			getStyleItem(styleSaved3, item123)))));

		//when
		Page<ListResponse> items1 = itemRepository.searchAllItemForExport(member1,
			PageRequest.of(0, 3));
		Page<ListResponse> items2 = itemRepository.searchAllItemForExport(member2,
			PageRequest.of(0, 3));
		Page<ListResponse> items123 = itemRepository.searchAllItemForExport(member123,
			PageRequest.of(0, 3));
		Page<ListResponse> items1232 = itemRepository.searchAllItemForExport(member123,
			PageRequest.of(1, 3));

		//then
		assertEquals(items1.getContent().size(), 2);
		assertEquals(items1.getTotalPages(), 1);
		assertEquals(items1.getTotalElements(), 2);

		assertEquals(items2.getContent().size(), 2);
		assertEquals(items2.getTotalPages(), 1);
		assertEquals(items2.getTotalElements(), 2);

		for (ListResponse listResponse : items123.getContent()) {
			System.out.println(listResponse);
		}
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		for (ListResponse listResponse : items1232.getContent()) {
			System.out.println(listResponse);
		}

		assertEquals(items123.getContent().size(), 3);
		assertEquals(items123.getTotalPages(), 2);
		assertEquals(items123.getTotalElements(), 4);

		assertEquals(items1232.getContent().size(), 1);
		assertEquals(items1232.getTotalPages(), 2);
		assertEquals(items1232.getTotalElements(), 4);
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
			.level(0)
			.children(new ArrayList<>())
			.build());
	}

	private Item getItem(Category category, Member member, State state) {
		return Item.builder()
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
			.build();
	}

	private StyleItem getStyleItem(Style style, Item item) {
		return styleItemRepository.save(StyleItem.builder()
			.style(style)
			.name(style.getName())
			.item(item)
			.build());
	}

	private ExpertInfo getExpertInfo(Member member) {
		return expertInfoRepository.save(ExpertInfo.builder()
			.member(member)
			.name("test name")
			.info("test info")
			.build());
	}

	private static ExpertStyle getExpertStyle(Style styleSaved1, ExpertInfo expertInfo) {
		return ExpertStyle.builder()
			.expertInfo(expertInfo)
			.style(styleSaved1)
			.build();
	}
}