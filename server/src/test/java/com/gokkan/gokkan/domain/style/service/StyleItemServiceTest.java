package com.gokkan.gokkan.domain.style.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.type.State;
import com.gokkan.gokkan.domain.style.domain.Style;
import com.gokkan.gokkan.domain.style.domain.StyleItem;
import com.gokkan.gokkan.domain.style.exception.StyleErrorCode;
import com.gokkan.gokkan.domain.style.repository.StyleItemRepository;
import com.gokkan.gokkan.domain.style.repository.StyleRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StyleItemServiceTest {

	private final String name1 = "test style1";
	private final String name2 = "test style2";
	private final List<String> names = List.of(name1, name2);
	ArgumentCaptor<StyleItem> styleItemCaptor = ArgumentCaptor.forClass(StyleItem.class);
	@Mock
	private StyleItemRepository styleItemRepository;
	@Mock
	private StyleRepository styleRepository;
	@InjectMocks
	private StyleItemService styleItemService;

	@DisplayName("01_00. createNotDuplicate success not delete all duplicate")
	@Test
	public void test_01_00() {
		//given
		Style style1 = getStyle(name1);
		Style style2 = getStyle(name2);
		given(styleRepository.existsByName(name1)).willReturn(true);
		given(styleRepository.existsByName(name2)).willReturn(true);

		//when
		List<StyleItem> styleItems = styleItemService.createNotDuplicate(
			List.of(name1, name2),
			List.of(getStyleItem(style1), getStyleItem(style2)));

		//then
		assertEquals(styleItems.get(0).getStyle().getName(), names.get(0));
		assertEquals(styleItems.get(0).getName(), names.get(0));
		assertEquals(styleItems.get(1).getStyle().getName(), names.get(1));
		assertEquals(styleItems.get(1).getName(), names.get(1));
	}

	@DisplayName("01_01. createNotDuplicate success do delete not duplicate")
	@Test
	public void test_01_01() {
		//given
		Style style1 = getStyle(name1);
		Style style2 = getStyle(name2);
		given(styleRepository.existsByName(name1)).willReturn(true);
		given(styleRepository.findByName(name1)).willReturn(Optional.of(style1));

		//when

		List<StyleItem> styleItems = styleItemService.createNotDuplicate(
			List.of(name1),
			List.of(getStyleItem(style2)));

		//then
		assertEquals(styleItems.size(), 1);
		assertEquals(styleItems.get(0).getStyle().getName(), style1.getName());
		assertEquals(styleItems.get(0).getName(), style1.getName());
	}

	@DisplayName("01_02. createNotDuplicate success some delete some duplicate")
	@Test
	public void test_01_02() {
		//given
		Style style1 = getStyle(name1);
		Style style2 = getStyle(name2);
		String name3 = "test style3";
		Style style3 = getStyle(name3);
		String name4 = "test style4";
		Style style4 = getStyle(name4);
		given(styleRepository.existsByName(name1)).willReturn(true);
		given(styleRepository.existsByName(name2)).willReturn(true);
		given(styleRepository.existsByName(name3)).willReturn(true);

		given(styleRepository.findByName(name1)).willReturn(Optional.of(style1));

		//when

		List<StyleItem> styleItems = styleItemService.createNotDuplicate(
			List.of(name1, name2, name3),
			List.of(getStyleItem(style2), getStyleItem(style3), getStyleItem(style4)));
		styleItems.sort(Comparator.comparing(o -> o.getStyle().getName()));

		//then
		assertEquals(styleItems.size(), 3);
		assertEquals(styleItems.get(0).getStyle().getName(), style1.getName());
		assertEquals(styleItems.get(0).getName(), style1.getName());
		assertEquals(styleItems.get(1).getStyle().getName(), style2.getName());
		assertEquals(styleItems.get(1).getName(), style2.getName());
		assertEquals(styleItems.get(2).getStyle().getName(), style3.getName());
		assertEquals(styleItems.get(2).getName(), style3.getName());
	}

	@DisplayName("01_03. createNotDuplicate success not delete not duplicate")
	@Test
	public void test_01_03() {
		//given
		Style style1 = getStyle(name1);
		Style style2 = getStyle(name2);
		given(styleRepository.existsByName(name1)).willReturn(true);
		given(styleRepository.existsByName(name2)).willReturn(true);

		given(styleRepository.findByName(name1)).willReturn(Optional.of(style1));
		given(styleRepository.findByName(name2)).willReturn(Optional.of(style2));

		//when

		List<StyleItem> styleItems = styleItemService.createNotDuplicate(
			List.of(name1, name2),
			new ArrayList<>());

		//then
		assertEquals(styleItems.size(), 2);
		assertEquals(styleItems.get(0).getStyle().getName(), style1.getName());
		assertEquals(styleItems.get(0).getName(), style1.getName());
		assertEquals(styleItems.get(1).getStyle().getName(), style2.getName());
		assertEquals(styleItems.get(1).getName(), style2.getName());
	}

	@DisplayName("01_04. create fail not found style")
	@Test
	public void test_01_04() {
		//given
		given(styleRepository.existsByName(any())).willReturn(false);

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> styleItemService.createNotDuplicate(names, new ArrayList<>()));

		//then
		assertEquals(restApiException.getErrorCode(), StyleErrorCode.NOT_FOUND_STYLE);
	}

	@DisplayName("02_00. update success")
	@Test
	public void test_02_00() {
		//given
		Style style = getStyle(name1);
		given(styleItemRepository.findById(any())).willReturn(
			Optional.of(getStyleItem(style)));
		given(styleRepository.findByName(any())).willReturn(Optional.of(getStyle("update")));
		given(styleItemRepository.save(any())).willReturn(getStyleItem(style));

		//when
		styleItemService.update(1L, "update");
		verify(styleItemRepository, times(1)).save(
			styleItemCaptor.capture());

		//then
		StyleItem updateStyleItem = styleItemCaptor.getValue();
		assertEquals(updateStyleItem.getStyle().getName(), "update");
		assertEquals(updateStyleItem.getName(), "update");
	}

	@DisplayName("02_01. update fail not found style")
	@Test
	public void test_02_01() {
		//given
		Style style = getStyle(name1);
		given(styleItemRepository.findById(any())).willReturn(
			Optional.of(getStyleItem(style)));
		given(styleRepository.findByName(any())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> styleItemService.update(1L, "update"));

		//then

		assertEquals(restApiException.getErrorCode(), StyleErrorCode.NOT_FOUND_STYLE);
	}

	@DisplayName("02_02. update fail not found style item")
	@Test
	public void test_02_02() {
		//given
		given(styleItemRepository.findById(any())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> styleItemService.update(1L, "update"));

		//then

		assertEquals(restApiException.getErrorCode(), StyleErrorCode.NOT_FOUND_STYLE_ITEM);
	}

	private Style getStyle(String styleName) {
		return Style.builder()
			.name(styleName)
			.build();
	}

	private StyleItem getStyleItem(Style style) {
		return StyleItem.builder()
			.style(style)
			.name(style.getName())
			.item(getItem())
			.build();
	}

	private static Item getItem() {
		return Item.builder()
			.name("test name")
			.startPrice(100L)
			.width(100L)
			.depth(100L)
			.height(100L)
			.material("나무")
			.conditionGrade("test conditionGrade")
			.conditionDescription("test conditionDescription")
			.text("test text")
			.designer("test designer")
			.brand("test brand")
			.productionYear(2023)
			.state(State.ASSESSING)
			.created(LocalDateTime.now())
			.updated(LocalDateTime.now())
			.imageItems(new ArrayList<>())
			.imageChecks(new ArrayList<>())
			.build();
	}
}