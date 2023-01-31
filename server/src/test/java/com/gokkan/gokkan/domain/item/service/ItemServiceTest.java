package com.gokkan.gokkan.domain.item.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gokkan.gokkan.domain.category.domain.Category;
import com.gokkan.gokkan.domain.category.service.CategoryService;
import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import com.gokkan.gokkan.domain.image.domain.ImageItem;
import com.gokkan.gokkan.domain.image.dto.ImageDto;
import com.gokkan.gokkan.domain.image.repository.ImageCheckRepository;
import com.gokkan.gokkan.domain.image.repository.ImageItemRepository;
import com.gokkan.gokkan.domain.image.service.AwsS3Service;
import com.gokkan.gokkan.domain.image.service.ImageCheckService;
import com.gokkan.gokkan.domain.image.service.ImageItemService;
import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.dto.ItemDto.Response;
import com.gokkan.gokkan.domain.item.dto.ItemDto.ResponseForAuction;
import com.gokkan.gokkan.domain.item.dto.ItemDto.UpdateRequest;
import com.gokkan.gokkan.domain.item.exception.ItemErrorCode;
import com.gokkan.gokkan.domain.item.repository.ItemRepository;
import com.gokkan.gokkan.domain.item.type.State;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.exception.MemberErrorCode;
import com.gokkan.gokkan.domain.style.domain.Style;
import com.gokkan.gokkan.domain.style.domain.StyleItem;
import com.gokkan.gokkan.domain.style.repository.StyleItemRepository;
import com.gokkan.gokkan.domain.style.service.StyleItemService;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import com.gokkan.gokkan.global.security.oauth.entity.ProviderType;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

	ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
	Category root = Category.builder()
		.id(0L)
		.parent(null)
		.level(0)
		.name("root")
		.children(new ArrayList<>())
		.build();
	String png = "png";
	String url1 = "url1";
	String url2 = "url2";
	String url3 = "url3";
	String url4 = "url4";
	String style1 = "style1";
	String style2 = "style2";
	String categoryName1 = "test category1";
	Member member = Member.builder()
		.userId("userId")
		.email("member@email.com")
		.name("name")
		.providerType(ProviderType.KAKAO)
		.build();
	@Mock
	private ItemRepository itemRepository;
	@Mock
	private ImageItemRepository imageItemRepository;
	@Mock
	private ImageCheckRepository imageCheckRepository;
	@Mock
	private StyleItemRepository styleItemRepository;
	@Mock
	private CategoryService categoryService;
	@Mock
	private StyleItemService styleItemService;
	@Mock
	private ImageItemService imageItemService;
	@Mock
	private ImageCheckService imageCheckService;
	@Mock
	private AwsS3Service awsS3Service;
	@InjectMocks
	private ItemService itemService;

	@DisplayName("01_00. create success from empty image saved")
	@Test
	public void test_01_00() throws IOException {
		//given
		UpdateRequest updateRequest = getUpdateRequest(
			new ArrayList<>(),
			new ArrayList<>(),
			new ArrayList<>());
		given(itemRepository.findById(anyLong()))
			.willReturn(Optional.of(getEmptyItem()));
		given(categoryService.getCategory(anyString()))
			.willReturn(getCategory(updateRequest.getCategory(), root));

		given(styleItemService.createNotDuplicate(anyList(), anyList()))
			.willReturn(new ArrayList<>(List.of(StyleItem.builder().build())));
		given(awsS3Service.save(anyList()))
			.willReturn(new ArrayList<>(Arrays.asList(url1, url2)));
		given(imageItemService.create(anyList()))
			.willReturn(
				new ArrayList<>(Arrays.asList(getImageItem(1L, url1), getImageItem(2L, url2))));
		given(imageCheckService.create(anyList()))
			.willReturn(
				new ArrayList<>(Arrays.asList(getImageCheck(1L, url1), getImageCheck(2L, url2))));

		given(imageItemService.checkImageItemDeleted(new ArrayList<>(), new ArrayList<>()))
			.willReturn(new ArrayList<>());
		given(imageCheckService.checkImageCheckDeleted(new ArrayList<>(), new ArrayList<>()))
			.willReturn(new ArrayList<>());

		given(styleItemRepository.saveAll(any()))
			.willReturn(new ArrayList<>());
		given((imageItemRepository.saveAll(any())))
			.willReturn(
				new ArrayList<>(Arrays.asList(getImageItem(1L, url1), getImageItem(2L, url2))));
		given((imageCheckRepository.saveAll(any())))
			.willReturn(
				new ArrayList<>(Arrays.asList(getImageCheck(1L, url1), getImageCheck(2L, url2))));

		given(itemRepository.save(any()))
			.willReturn(getItem(
				new ArrayList<>(Arrays.asList(getImageItem(1L, url1), getImageItem(2L, url2))),
				new ArrayList<>(Arrays.asList(getImageCheck(1L, url1), getImageCheck(2L, url2))),
				new ArrayList<>()));
		//when

		itemService.create(updateRequest, getMultipartFiles(png), getMultipartFiles(png), member);
		verify(itemRepository, times(1)).save(itemCaptor.capture());

		//then
		Item item = itemCaptor.getValue();
		assertEquals(item.getName(), updateRequest.getName());
		assertEquals(item.getStartPrice(), updateRequest.getStartPrice());
		assertEquals(item.getState(), State.ASSESSING);

		assertEquals(item.getCategory().getName(), updateRequest.getCategory());
		assertEquals(item.getStyleItems().size(), 1);

		assertEquals(item.getWidth(), updateRequest.getWidth());
		assertEquals(item.getDepth(), updateRequest.getDepth());
		assertEquals(item.getHeight(), updateRequest.getHeight());
		assertEquals(item.getMaterial(), updateRequest.getMaterial());
		assertEquals(item.getConditionGrade(), updateRequest.getConditionGrade());
		assertEquals(item.getConditionDescription(), updateRequest.getConditionDescription());
		assertEquals(item.getText(), updateRequest.getText());
		assertEquals(item.getDesigner(), updateRequest.getDesigner());
		assertEquals(item.getBrand(), updateRequest.getBrand());
		assertEquals(item.getProductionYear(), updateRequest.getProductionYear());

		assertEquals(item.getImageChecks().size(), 2);
		assertEquals(item.getImageChecks().get(0).getId(), 1L);
		assertEquals(item.getImageChecks().get(1).getId(), 2L);
		assertEquals(item.getImageItems().size(), 2);
		assertEquals(item.getImageItems().get(0).getId(), 1L);
		assertEquals(item.getImageItems().get(1).getId(), 2L);

		assertEquals(item.getMember().getName(), member.getName());
		assertEquals(item.getMember().getEmail(), member.getEmail());
	}

	@DisplayName("01_01. create success from not empty item not change image, style")
	@Test
	public void test_01_01() {
		//given
		UpdateRequest updateRequest = getUpdateRequest(
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(style1)));

		List<ImageItem> savedImageItems =
			new ArrayList<>(Arrays.asList(getImageItem(1L, url1), getImageItem(2L, url2)));
		List<ImageCheck> savedImageChecks =
			new ArrayList<>(Arrays.asList(getImageCheck(1L, url1), getImageCheck(2L, url2)));
		List<StyleItem> savedStyleItems =
			new ArrayList<>(Arrays.asList(getStyleItem(style1)));

		given(itemRepository.findById(anyLong()))
			.willReturn(Optional.of(getItem(savedImageItems, savedImageChecks, savedStyleItems)));

		given(categoryService.getCategory(anyString()))
			.willReturn(getCategory(updateRequest.getCategory(), root));
		given(styleItemService.createNotDuplicate(anyList(), anyList()))
			.willReturn(savedStyleItems);

		given(awsS3Service.save(anyList()))
			.willReturn(new ArrayList<>());
		given(imageItemService.create(anyList()))
			.willReturn(new ArrayList<>());
		given(imageCheckService.create(anyList()))
			.willReturn(new ArrayList<>());

		given(imageItemService.checkImageItemDeleted(anyList(), anyList()))
			.willReturn(savedImageItems);
		given(imageCheckService.checkImageCheckDeleted(anyList(), anyList()))
			.willReturn(savedImageChecks);

		given(styleItemRepository.saveAll(any())).willReturn(savedStyleItems);
		given((imageItemRepository.saveAll(any()))).willReturn(savedImageItems);
		given((imageCheckRepository.saveAll(any()))).willReturn(savedImageChecks);

		given(itemRepository.save(any()))
			.willReturn(getItem(savedImageItems, savedImageChecks, savedStyleItems));
		//when

		itemService.create(updateRequest, new ArrayList<>(), new ArrayList<>(), member);
		verify(itemRepository, times(1)).save(itemCaptor.capture());

		//then
		Item item = itemCaptor.getValue();
		assertEquals(item.getName(), updateRequest.getName());
		assertEquals(item.getStartPrice(), updateRequest.getStartPrice());
		assertEquals(item.getState(), State.ASSESSING);

		assertEquals(item.getCategory().getName(), updateRequest.getCategory());
		assertEquals(item.getStyleItems().size(), 1);

		assertEquals(
			item.getStyleItems().get(0).getStyle().getName(),
			updateRequest.getStyles().get(0));

		assertEquals(item.getWidth(), updateRequest.getWidth());
		assertEquals(item.getDepth(), updateRequest.getDepth());
		assertEquals(item.getHeight(), updateRequest.getHeight());
		assertEquals(item.getMaterial(), updateRequest.getMaterial());
		assertEquals(item.getConditionGrade(), updateRequest.getConditionGrade());
		assertEquals(item.getConditionDescription(), updateRequest.getConditionDescription());
		assertEquals(item.getText(), updateRequest.getText());
		assertEquals(item.getDesigner(), updateRequest.getDesigner());
		assertEquals(item.getBrand(), updateRequest.getBrand());
		assertEquals(item.getProductionYear(), updateRequest.getProductionYear());

		assertEquals(item.getImageItems().size(), 2);
		assertEquals(
			item.getImageItems().get(0).getId(),
			updateRequest.getImageItemUrls().get(0).getImageId());
		assertEquals(
			item.getImageItems().get(1).getId(),
			updateRequest.getImageItemUrls().get(1).getImageId());

		assertEquals(item.getImageChecks().size(), 2);
		assertEquals(
			item.getImageChecks().get(0).getId(),
			updateRequest.getImageCheckUrls().get(0).getImageId());
		assertEquals(
			item.getImageChecks().get(1).getId(),
			updateRequest.getImageCheckUrls().get(1).getImageId());

		assertEquals(item.getMember().getName(), member.getName());
		assertEquals(item.getMember().getEmail(), member.getEmail());
	}

	@DisplayName("01_02. create success from not empty item change image, style")
	@Test
	public void test_01_02() throws IOException {
		//given
		UpdateRequest updateRequest = getUpdateRequest(
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(style2)));

		List<ImageItem> savedImageItems =
			new ArrayList<>(Arrays.asList(getImageItem(1L, url1), getImageItem(2L, url2)));
		List<ImageCheck> savedImageChecks =
			new ArrayList<>(Arrays.asList(getImageCheck(1L, url1), getImageCheck(2L, url2)));
		List<StyleItem> savedStyleItems =
			new ArrayList<>(Arrays.asList(getStyleItem(style1)));

		given(itemRepository.findById(anyLong()))
			.willReturn(Optional.of(getItem(savedImageItems, savedImageChecks, savedStyleItems)));

		given(categoryService.getCategory(anyString()))
			.willReturn(getCategory(categoryName1, root));
		given(styleItemService.createNotDuplicate(anyList(), anyList()))
			.willReturn(new ArrayList<>(Arrays.asList(getStyleItem(style2))));

		given(awsS3Service.save(anyList()))
			.willReturn(new ArrayList<>(Arrays.asList(url3, url4)));
		List<ImageItem> newImageItems =
			new ArrayList<>(Arrays.asList(getImageItem(3L, url3), getImageItem(4L, url4)));
		given(imageItemService.create(anyList()))
			.willReturn(newImageItems);
		List<ImageCheck> newImageChecks =
			new ArrayList<>(Arrays.asList(getImageCheck(null, url3), getImageCheck(null, url4)));
		given(imageCheckService.create(anyList()))
			.willReturn(newImageChecks);

		given(imageItemService.checkImageItemDeleted(anyList(), anyList()))
			.willReturn(savedImageItems);
		given(imageCheckService.checkImageCheckDeleted(anyList(), anyList()))
			.willReturn(savedImageChecks);

		given(styleItemRepository.saveAll(any())).willReturn(savedStyleItems);
		ArrayList<ImageItem> finalImageItems = new ArrayList<>(savedImageItems);
		finalImageItems.addAll(newImageItems);
		given((imageItemRepository.saveAll(any()))).willReturn(finalImageItems);
		ArrayList<ImageCheck> finalImageChecks = new ArrayList<>(savedImageChecks);
		finalImageChecks.addAll(newImageChecks);
		given((imageCheckRepository.saveAll(any()))).willReturn(finalImageChecks);

		given(itemRepository.save(any()))
			.willReturn(getItem(savedImageItems, savedImageChecks, savedStyleItems));
		//when

		itemService.create(updateRequest, getMultipartFiles(png), getMultipartFiles(png), member);
		verify(itemRepository, times(1)).save(itemCaptor.capture());

		//then
		Item item = itemCaptor.getValue();
		assertEquals(item.getName(), updateRequest.getName());
		assertEquals(item.getStartPrice(), updateRequest.getStartPrice());
		assertEquals(item.getState(), State.ASSESSING);

		assertEquals(item.getCategory().getName(), updateRequest.getCategory());
		assertEquals(item.getStyleItems().size(), 1);

		assertEquals(
			item.getStyleItems().get(0).getStyle().getName(),
			updateRequest.getStyles().get(0));

		assertEquals(item.getWidth(), updateRequest.getWidth());
		assertEquals(item.getDepth(), updateRequest.getDepth());
		assertEquals(item.getHeight(), updateRequest.getHeight());
		assertEquals(item.getMaterial(), updateRequest.getMaterial());
		assertEquals(item.getConditionGrade(), updateRequest.getConditionGrade());
		assertEquals(item.getConditionDescription(), updateRequest.getConditionDescription());
		assertEquals(item.getText(), updateRequest.getText());
//		assertEquals(item.getMadeIn(), updateRequest.getMadeIn());
		assertEquals(item.getDesigner(), updateRequest.getDesigner());
		assertEquals(item.getBrand(), updateRequest.getBrand());
		assertEquals(item.getProductionYear(), updateRequest.getProductionYear());

		assertEquals(item.getImageItems().size(), 4);
		assertEquals(
			item.getImageItems().get(0).getId(),
			updateRequest.getImageItemUrls().get(0).getImageId());
		assertEquals(
			item.getImageItems().get(1).getId(),
			updateRequest.getImageItemUrls().get(1).getImageId());
		assertEquals(
			item.getImageItems().get(2).getUrl(), url3);
		assertEquals(
			item.getImageItems().get(3).getUrl(), url4);

		assertEquals(item.getImageChecks().size(), 4);
		assertEquals(
			item.getImageChecks().get(0).getId(),
			updateRequest.getImageCheckUrls().get(0).getImageId());
		assertEquals(
			item.getImageChecks().get(1).getId(),
			updateRequest.getImageCheckUrls().get(1).getImageId());
		assertEquals(
			item.getImageChecks().get(2).getUrl(), url3);
		assertEquals(
			item.getImageChecks().get(3).getUrl(), url4);

		assertEquals(item.getMember().getName(), member.getName());
		assertEquals(item.getMember().getEmail(), member.getEmail());
	}

	@DisplayName("01_03. create fail MEMBER_NOT_LOGIN")
	@Test
	public void test_01_03() {
		//given
		UpdateRequest updateRequest = getUpdateRequest(
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(style2)));

		//when

		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> itemService.create(updateRequest, getMultipartFiles(png), getMultipartFiles(png),
				null));

		//then
		assertEquals(restApiException.getErrorCode(), MemberErrorCode.MEMBER_NOT_LOGIN);
	}

	@DisplayName("01_04. create fail MEMBER_MISMATCH")
	@Test
	public void test_01_04() {
		//given
		UpdateRequest updateRequest = getUpdateRequest(
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(style2)));

		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(Item.builder()
				.member(Member.builder()
					.build())
				.build()));

		//when

		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> itemService.create(updateRequest, getMultipartFiles(png), getMultipartFiles(png),
				member));

		//then
		assertEquals(restApiException.getErrorCode(), MemberErrorCode.MEMBER_MISMATCH);
	}

	@DisplayName("01_05_1. create fail CAN_NOT_UPDATE_STATE")
	@Test
	public void test_01_05_1() {
		//given
		UpdateRequest updateRequest = getUpdateRequest(
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(style2)));

		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(Item.builder()
				.member(member)
				.state(State.ASSESSING)
				.build()));

		//when

		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> itemService.create(updateRequest, getMultipartFiles(png), getMultipartFiles(png),
				member));

		//then
		assertEquals(restApiException.getErrorCode(), ItemErrorCode.CAN_NOT_FIX_STATE);
	}

	@DisplayName("01_05_2. create fail CAN_NOT_UPDATE_STATE")
	@Test
	public void test_01_05_2() {
		//given
		UpdateRequest updateRequest = getUpdateRequest(
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(style2)));

		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(Item.builder()
				.member(member)
				.state(State.COMPLETE)
				.build()));

		//when

		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> itemService.create(updateRequest, getMultipartFiles(png), getMultipartFiles(png),
				member));

		//then
		assertEquals(restApiException.getErrorCode(), ItemErrorCode.CAN_NOT_FIX_STATE);
	}

	@DisplayName("01_06. create fail CATEGORY_NOT_NUL")
	@Test
	public void test_01_06() throws IOException {

		//given
		UpdateRequest updateRequest = getUpdateRequest(
			new ArrayList<>(),
			new ArrayList<>(),
			new ArrayList<>());
		updateRequest.setCategory("");
		given(itemRepository.findById(anyLong()))
			.willReturn(Optional.of(getEmptyItem()));
//		given(categoryService.getCategory(anyString()))
//			.willReturn(Category.builder().name("").build());
//
//		given(styleItemService.createNotDuplicate(anyList(), anyList()))
//			.willReturn(new ArrayList<>());

		//when

		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> itemService.create(updateRequest, getMultipartFiles(png), getMultipartFiles(png),
				member));

		//then
		assertEquals(restApiException.getErrorCode(), ItemErrorCode.CATEGORY_NOT_NUL);
	}

	@DisplayName("01_07. create fail STYLE_NOT_NUL")
	@Test
	public void test_01_07() throws IOException {

		//given
		UpdateRequest updateRequest = getUpdateRequest(
			new ArrayList<>(),
			new ArrayList<>(),
			new ArrayList<>());
		given(itemRepository.findById(anyLong()))
			.willReturn(Optional.of(getEmptyItem()));
		given(categoryService.getCategory(anyString()))
			.willReturn(root);

		given(styleItemService.createNotDuplicate(anyList(), anyList()))
			.willReturn(new ArrayList<>());

		//when

		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> itemService.create(updateRequest, getMultipartFiles(png), getMultipartFiles(png),
				member));

		//then
		assertEquals(restApiException.getErrorCode(), ItemErrorCode.STYLE_NOT_NULL);
	}

	@DisplayName("02_00_1. readDetail success, state ASSESSING ")
	@Test
	public void test_02_00_1() {
		//given
		Item item = getItem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		item.setState(State.ASSESSING);
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(item));

		//when

		Response response = itemService.readDetail(1L);

		//then
		assertEquals(response.getName(), item.getName());
	}

	@DisplayName("02_00_2. readDetail success, state COMPLETE ")
	@Test
	public void test_02_00_2() {
		//given
		Item item = getItem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		item.setState(State.COMPLETE);
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(item));

		//when

		Response response = itemService.readDetail(1L);

		//then
		assertEquals(response.getName(), item.getName());
	}

	@DisplayName("02_01. readDetail fail NOT_FOUND_ITEM")
	@Test
	public void test_02_01() {
		//given
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.empty());

		//when
		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.readDetail(1L));

		//then
		assertEquals(itemException.getErrorCode(), ItemErrorCode.NOT_FOUND_ITEM);
	}

	@DisplayName("02_02_1. readDetail fail, state TEMPORARY ")
	@Test
	public void test_02_02_1() {
		//given
		Item item = getItem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		item.setState(State.TEMPORARY);
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(item));

		//when

		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.readDetail(1L));

		//then
		assertEquals(itemException.getErrorCode(), ItemErrorCode.CAN_NOT_READ_STATE);
	}

	@DisplayName("02_02_2. readDetail fail, state RETURN ")
	@Test
	public void test_02_02_2() {
		//given
		Item item = getItem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		item.setState(State.RETURN);
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(item));

		//when

		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.readDetail(1L));

		//then
		assertEquals(itemException.getErrorCode(), ItemErrorCode.CAN_NOT_READ_STATE);
	}

	@DisplayName("03_00. delete success")
	@Test
	public void test_03_00() {
		//given
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(getItem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>())));

		//when
		boolean deleted = itemService.delete(1L, member);
		verify(itemRepository, times(1)).delete(itemCaptor.capture());

		//then
		assertTrue(deleted);
	}

	@DisplayName("03_01. delete fail NOT_FOUND_ITEM")
	@Test
	public void test_03_01() {
		//given
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.empty());

		//when
		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.delete(1L, member));
		verify(itemRepository, times(0)).delete(itemCaptor.capture());

		//then
		assertEquals(itemException.getErrorCode(), ItemErrorCode.NOT_FOUND_ITEM);
	}

	@DisplayName("03_02. delete fail MEMBER_NOT_LOGIN")
	@Test
	public void test_03_02() {
		//given

		//when
		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.delete(1L, null));
		verify(itemRepository, times(0)).delete(itemCaptor.capture());

		//then
		assertEquals(itemException.getErrorCode(), MemberErrorCode.MEMBER_NOT_LOGIN);
	}

	@DisplayName("03_03. delete fail MEMBER_MISMATCH")
	@Test
	public void test_03_03() {
		//given
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(getItem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>())));

		//when
		Member loginMember = Member.builder()
			.userId("mismatch")
			.email("member@email.com")
			.name("name")
			.providerType(ProviderType.KAKAO)
			.build();

		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.delete(1L, loginMember));
		verify(itemRepository, times(0)).delete(itemCaptor.capture());

		//then
		assertEquals(itemException.getErrorCode(), MemberErrorCode.MEMBER_MISMATCH);
	}

	@DisplayName("03_04_1. delete fail CAN_NOT_FIX_STATE")
	@Test
	public void test_03_04_1() {
		//given
		Item item = getItem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		item.setState(State.ASSESSING);
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(item));

		//when

		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.delete(1L, member));
		verify(itemRepository, times(0)).delete(itemCaptor.capture());

		//then
		assertEquals(itemException.getErrorCode(), ItemErrorCode.CAN_NOT_FIX_STATE);
	}

	@DisplayName("03_04_2. delete fail CAN_NOT_FIX_STATE")
	@Test
	public void test_03_04_2() {
		//given
		Item item = getItem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		item.setState(State.COMPLETE);
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(item));

		//when

		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.delete(1L, member));
		verify(itemRepository, times(0)).delete(itemCaptor.capture());

		//then
		assertEquals(itemException.getErrorCode(), ItemErrorCode.CAN_NOT_FIX_STATE);
	}

	@DisplayName("04_00. update success from empty image saved")
	@Test
	public void test_04_00() throws IOException {
		//given
		UpdateRequest updateRequest = getUpdateRequest(
			new ArrayList<>(),
			new ArrayList<>(),
			new ArrayList<>());
		given(itemRepository.findById(anyLong()))
			.willReturn(Optional.of(getEmptyItem()));
		given(categoryService.getCategory(anyString()))
			.willReturn(getCategory(updateRequest.getCategory(), root));

		given(styleItemService.createNotDuplicate(anyList(), anyList()))
			.willReturn(new ArrayList<>());
		given(awsS3Service.save(anyList()))
			.willReturn(new ArrayList<>(Arrays.asList(url1, url2)));
		given(imageItemService.create(anyList()))
			.willReturn(
				new ArrayList<>(Arrays.asList(getImageItem(1L, url1), getImageItem(2L, url2))));
		given(imageCheckService.create(anyList()))
			.willReturn(
				new ArrayList<>(Arrays.asList(getImageCheck(1L, url1), getImageCheck(2L, url2))));

		given(imageItemService.checkImageItemDeleted(new ArrayList<>(), new ArrayList<>()))
			.willReturn(new ArrayList<>());
		given(imageCheckService.checkImageCheckDeleted(new ArrayList<>(), new ArrayList<>()))
			.willReturn(new ArrayList<>());

		given(styleItemRepository.saveAll(any()))
			.willReturn(new ArrayList<>());
		given((imageItemRepository.saveAll(any())))
			.willReturn(
				new ArrayList<>(Arrays.asList(getImageItem(1L, url1), getImageItem(2L, url2))));
		given((imageCheckRepository.saveAll(any())))
			.willReturn(
				new ArrayList<>(Arrays.asList(getImageCheck(1L, url1), getImageCheck(2L, url2))));

		given(itemRepository.save(any()))
			.willReturn(getItem(
				new ArrayList<>(Arrays.asList(getImageItem(1L, url1), getImageItem(2L, url2))),
				new ArrayList<>(Arrays.asList(getImageCheck(1L, url1), getImageCheck(2L, url2))),
				new ArrayList<>()));
		//when

		itemService.update(updateRequest, getMultipartFiles(png), getMultipartFiles(png), member);
		verify(itemRepository, times(1)).save(itemCaptor.capture());

		//then
		Item item = itemCaptor.getValue();
		assertEquals(item.getName(), updateRequest.getName());
		assertEquals(item.getStartPrice(), updateRequest.getStartPrice());
		assertEquals(item.getState(), State.TEMPORARY);

		assertEquals(item.getCategory().getName(), updateRequest.getCategory());
		assertEquals(item.getStyleItems().size(), 0);

		assertEquals(item.getWidth(), updateRequest.getWidth());
		assertEquals(item.getDepth(), updateRequest.getDepth());
		assertEquals(item.getHeight(), updateRequest.getHeight());
		assertEquals(item.getMaterial(), updateRequest.getMaterial());
		assertEquals(item.getConditionGrade(), updateRequest.getConditionGrade());
		assertEquals(item.getConditionDescription(), updateRequest.getConditionDescription());
		assertEquals(item.getText(), updateRequest.getText());
//		assertEquals(item.getMadeIn(), updateRequest.getMadeIn());
		assertEquals(item.getDesigner(), updateRequest.getDesigner());
		assertEquals(item.getBrand(), updateRequest.getBrand());
		assertEquals(item.getProductionYear(), updateRequest.getProductionYear());

		assertEquals(item.getImageChecks().size(), 2);
		assertEquals(item.getImageChecks().get(0).getId(), 1L);
		assertEquals(item.getImageChecks().get(1).getId(), 2L);
		assertEquals(item.getImageItems().size(), 2);
		assertEquals(item.getImageItems().get(0).getId(), 1L);
		assertEquals(item.getImageItems().get(1).getId(), 2L);

		assertEquals(item.getMember().getName(), member.getName());
		assertEquals(item.getMember().getEmail(), member.getEmail());
	}

	@DisplayName("04_01. update success from not empty item not change image, style")
	@Test
	public void test_04_01() {
		//given
		UpdateRequest updateRequest = getUpdateRequest(
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(style1)));

		List<ImageItem> savedImageItems =
			new ArrayList<>(Arrays.asList(getImageItem(1L, url1), getImageItem(2L, url2)));
		List<ImageCheck> savedImageChecks =
			new ArrayList<>(Arrays.asList(getImageCheck(1L, url1), getImageCheck(2L, url2)));
		List<StyleItem> savedStyleItems =
			new ArrayList<>(Arrays.asList(getStyleItem(style1)));

		given(itemRepository.findById(anyLong()))
			.willReturn(Optional.of(getItem(savedImageItems, savedImageChecks, savedStyleItems)));

		given(categoryService.getCategory(anyString()))
			.willReturn(getCategory(updateRequest.getCategory(), root));
		given(styleItemService.createNotDuplicate(anyList(), anyList()))
			.willReturn(savedStyleItems);

		given(awsS3Service.save(anyList()))
			.willReturn(new ArrayList<>());
		given(imageItemService.create(anyList()))
			.willReturn(new ArrayList<>());
		given(imageCheckService.create(anyList()))
			.willReturn(new ArrayList<>());

		given(imageItemService.checkImageItemDeleted(anyList(), anyList()))
			.willReturn(savedImageItems);
		given(imageCheckService.checkImageCheckDeleted(anyList(), anyList()))
			.willReturn(savedImageChecks);

		given(styleItemRepository.saveAll(any())).willReturn(savedStyleItems);
		given((imageItemRepository.saveAll(any()))).willReturn(savedImageItems);
		given((imageCheckRepository.saveAll(any()))).willReturn(savedImageChecks);

		given(itemRepository.save(any()))
			.willReturn(getItem(savedImageItems, savedImageChecks, savedStyleItems));
		//when

		itemService.update(updateRequest, new ArrayList<>(), new ArrayList<>(), member);
		verify(itemRepository, times(1)).save(itemCaptor.capture());

		//then
		Item item = itemCaptor.getValue();
		assertEquals(item.getName(), updateRequest.getName());
		assertEquals(item.getStartPrice(), updateRequest.getStartPrice());
		assertEquals(item.getState(), State.TEMPORARY);

		assertEquals(item.getCategory().getName(), updateRequest.getCategory());
		assertEquals(item.getStyleItems().size(), 1);

		assertEquals(
			item.getStyleItems().get(0).getStyle().getName(),
			updateRequest.getStyles().get(0));

		assertEquals(item.getWidth(), updateRequest.getWidth());
		assertEquals(item.getDepth(), updateRequest.getDepth());
		assertEquals(item.getHeight(), updateRequest.getHeight());
		assertEquals(item.getMaterial(), updateRequest.getMaterial());
		assertEquals(item.getConditionGrade(), updateRequest.getConditionGrade());
		assertEquals(item.getConditionDescription(), updateRequest.getConditionDescription());
		assertEquals(item.getText(), updateRequest.getText());
//		assertEquals(item.getMadeIn(), updateRequest.getMadeIn());
		assertEquals(item.getDesigner(), updateRequest.getDesigner());
		assertEquals(item.getBrand(), updateRequest.getBrand());
		assertEquals(item.getProductionYear(), updateRequest.getProductionYear());

		assertEquals(item.getImageItems().size(), 2);
		assertEquals(
			item.getImageItems().get(0).getId(),
			updateRequest.getImageItemUrls().get(0).getImageId());
		assertEquals(
			item.getImageItems().get(1).getId(),
			updateRequest.getImageItemUrls().get(1).getImageId());

		assertEquals(item.getImageChecks().size(), 2);
		assertEquals(
			item.getImageChecks().get(0).getId(),
			updateRequest.getImageCheckUrls().get(0).getImageId());
		assertEquals(
			item.getImageChecks().get(1).getId(),
			updateRequest.getImageCheckUrls().get(1).getImageId());

		assertEquals(item.getMember().getName(), member.getName());
		assertEquals(item.getMember().getEmail(), member.getEmail());
	}

	@DisplayName("04_02. update success from not empty item change image, style")
	@Test
	public void test_04_02() throws IOException {
		//given
		UpdateRequest updateRequest = getUpdateRequest(
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(style2)));

		List<ImageItem> savedImageItems =
			new ArrayList<>(Arrays.asList(getImageItem(1L, url1), getImageItem(2L, url2)));
		List<ImageCheck> savedImageChecks =
			new ArrayList<>(Arrays.asList(getImageCheck(1L, url1), getImageCheck(2L, url2)));
		List<StyleItem> savedStyleItems =
			new ArrayList<>(Arrays.asList(getStyleItem(style1)));

		given(itemRepository.findById(anyLong()))
			.willReturn(Optional.of(getItem(savedImageItems, savedImageChecks, savedStyleItems)));

		given(categoryService.getCategory(anyString()))
			.willReturn(getCategory(categoryName1, root));
		given(styleItemService.createNotDuplicate(anyList(), anyList()))
			.willReturn(new ArrayList<>(Arrays.asList(getStyleItem(style2))));

		given(awsS3Service.save(anyList()))
			.willReturn(new ArrayList<>(Arrays.asList(url3, url4)));
		List<ImageItem> newImageItems =
			new ArrayList<>(Arrays.asList(getImageItem(3L, url3), getImageItem(4L, url4)));
		given(imageItemService.create(anyList()))
			.willReturn(newImageItems);
		List<ImageCheck> newImageChecks =
			new ArrayList<>(Arrays.asList(getImageCheck(null, url3), getImageCheck(null, url4)));
		given(imageCheckService.create(anyList()))
			.willReturn(newImageChecks);

		given(imageItemService.checkImageItemDeleted(anyList(), anyList()))
			.willReturn(savedImageItems);
		given(imageCheckService.checkImageCheckDeleted(anyList(), anyList()))
			.willReturn(savedImageChecks);

		given(styleItemRepository.saveAll(any())).willReturn(savedStyleItems);
		ArrayList<ImageItem> finalImageItems = new ArrayList<>(savedImageItems);
		finalImageItems.addAll(newImageItems);
		given((imageItemRepository.saveAll(any()))).willReturn(finalImageItems);
		ArrayList<ImageCheck> finalImageChecks = new ArrayList<>(savedImageChecks);
		finalImageChecks.addAll(newImageChecks);
		given((imageCheckRepository.saveAll(any()))).willReturn(finalImageChecks);

		given(itemRepository.save(any()))
			.willReturn(getItem(savedImageItems, savedImageChecks, savedStyleItems));
		//when

		itemService.update(updateRequest, getMultipartFiles(png), getMultipartFiles(png), member);
		verify(itemRepository, times(1)).save(itemCaptor.capture());

		//then
		Item item = itemCaptor.getValue();
		assertEquals(item.getName(), updateRequest.getName());
		assertEquals(item.getStartPrice(), updateRequest.getStartPrice());
		assertEquals(item.getState(), State.TEMPORARY);

		assertEquals(item.getCategory().getName(), updateRequest.getCategory());
		assertEquals(item.getStyleItems().size(), 1);

		assertEquals(
			item.getStyleItems().get(0).getStyle().getName(),
			updateRequest.getStyles().get(0));

		assertEquals(item.getWidth(), updateRequest.getWidth());
		assertEquals(item.getDepth(), updateRequest.getDepth());
		assertEquals(item.getHeight(), updateRequest.getHeight());
		assertEquals(item.getMaterial(), updateRequest.getMaterial());
		assertEquals(item.getConditionGrade(), updateRequest.getConditionGrade());
		assertEquals(item.getConditionDescription(), updateRequest.getConditionDescription());
		assertEquals(item.getText(), updateRequest.getText());
//		assertEquals(item.getMadeIn(), updateRequest.getMadeIn());
		assertEquals(item.getDesigner(), updateRequest.getDesigner());
		assertEquals(item.getBrand(), updateRequest.getBrand());
		assertEquals(item.getProductionYear(), updateRequest.getProductionYear());

		assertEquals(item.getImageItems().size(), 4);
		assertEquals(
			item.getImageItems().get(0).getId(),
			updateRequest.getImageItemUrls().get(0).getImageId());
		assertEquals(
			item.getImageItems().get(1).getId(),
			updateRequest.getImageItemUrls().get(1).getImageId());
		assertEquals(
			item.getImageItems().get(2).getUrl(), url3);
		assertEquals(
			item.getImageItems().get(3).getUrl(), url4);

		assertEquals(item.getImageChecks().size(), 4);
		assertEquals(
			item.getImageChecks().get(0).getId(),
			updateRequest.getImageCheckUrls().get(0).getImageId());
		assertEquals(
			item.getImageChecks().get(1).getId(),
			updateRequest.getImageCheckUrls().get(1).getImageId());
		assertEquals(
			item.getImageChecks().get(2).getUrl(), url3);
		assertEquals(
			item.getImageChecks().get(3).getUrl(), url4);

		assertEquals(item.getMember().getName(), member.getName());
		assertEquals(item.getMember().getEmail(), member.getEmail());
	}

	@DisplayName("04_03. update fail MEMBER_NOT_LOGIN")
	@Test
	public void test_04_03() {
		//given
		UpdateRequest updateRequest = getUpdateRequest(
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(style2)));

		//when

		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> itemService.update(updateRequest, getMultipartFiles(png), getMultipartFiles(png),
				null));

		//then
		assertEquals(restApiException.getErrorCode(), MemberErrorCode.MEMBER_NOT_LOGIN);
	}

	@DisplayName("04_04. update fail MEMBER_MISMATCH")
	@Test
	public void test_04_04() {
		//given
		UpdateRequest updateRequest = getUpdateRequest(
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(style2)));

		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(Item.builder()
				.member(Member.builder()
					.build())
				.build()));

		//when

		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> itemService.update(updateRequest, getMultipartFiles(png), getMultipartFiles(png),
				member));

		//then
		assertEquals(restApiException.getErrorCode(), MemberErrorCode.MEMBER_MISMATCH);
	}

	@DisplayName("04_05_1. create fail CAN_NOT_UPDATE_STATE")
	@Test
	public void test_04_05_1() {
		//given
		UpdateRequest updateRequest = getUpdateRequest(
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(style2)));

		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(Item.builder()
				.member(member)
				.state(State.ASSESSING)
				.build()));

		//when

		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> itemService.update(updateRequest, getMultipartFiles(png), getMultipartFiles(png),
				member));

		//then
		assertEquals(restApiException.getErrorCode(), ItemErrorCode.CAN_NOT_FIX_STATE);
	}

	@DisplayName("04_05_2. create fail CAN_NOT_UPDATE_STATE")
	@Test
	public void test_04_05_2() {
		//given
		UpdateRequest updateRequest = getUpdateRequest(
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(getImageUpdateRequest(1L), getImageUpdateRequest(2L))),
			new ArrayList<>(Arrays.asList(style2)));

		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(Item.builder()
				.member(member)
				.state(State.COMPLETE)
				.build()));

		//when

		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> itemService.update(updateRequest, getMultipartFiles(png), getMultipartFiles(png),
				member));

		//then
		assertEquals(restApiException.getErrorCode(), ItemErrorCode.CAN_NOT_FIX_STATE);
	}

	@DisplayName("05_00_1. readDetailTemp success, state TEMPORARY ")
	@Test
	public void test_05_00_1() {
		//given
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(Item.builder()
				.member(member)
				.state(State.TEMPORARY)
				.created(LocalDateTime.now())
				.updated(LocalDateTime.now())
				.build()));

		//when
		Response response = itemService.readDetailTemp(1L, member);

		//then
		assertEquals(response.getName(), "");
	}

	@DisplayName("05_00_2. readDetailTemp success, state RETURN ")
	@Test
	public void test_05_00_2() {
		//given
		Item item = getItem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		item.setState(State.RETURN);
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(item));

		//when

		Response response = itemService.readDetailTemp(1L, member);

		//then
		assertEquals(response.getName(), item.getName());
	}

	@DisplayName("05_01. readDetailTemp fail NOT_FOUND_ITEM")
	@Test
	public void test_05_01() {
		//given
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.empty());

		//when
		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.readDetailTemp(1L, member));

		//then
		assertEquals(itemException.getErrorCode(), ItemErrorCode.NOT_FOUND_ITEM);
	}

	@DisplayName("05_02_1. readDetailTemp fail, state COMPLETE ")
	@Test
	public void test_05_02_1() {
		//given
		Item item = getItem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		item.setState(State.COMPLETE);
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(item));

		//when

		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.readDetailTemp(1L, member));

		//then
		assertEquals(itemException.getErrorCode(), ItemErrorCode.CAN_NOT_READ_STATE);
	}

	@DisplayName("05_02_2. readDetailTemp fail, state ASSESSING ")
	@Test
	public void test_05_02_2() {
		//given
		Item item = getItem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		item.setState(State.ASSESSING);
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(item));

		//when

		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.readDetailTemp(1L, member));

		//then
		assertEquals(itemException.getErrorCode(), ItemErrorCode.CAN_NOT_READ_STATE);
	}

	@DisplayName("05_03. readDetailTemp fail, MEMBER_NOT_LOGIN")
	@Test
	public void test_05_03() {
		//given
		Item item = getItem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		item.setState(State.ASSESSING);
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(item));

		//when

		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.readDetailTemp(1L, null));

		//then
		assertEquals(itemException.getErrorCode(), MemberErrorCode.MEMBER_NOT_LOGIN);
	}

	@DisplayName("05_04. readDetailTemp fail, MEMBER_NOT_LOGIN")
	@Test
	public void test_05_04() {
		//given
		Item item = getItem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		item.setState(State.ASSESSING);
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(item));

		//when

		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.readDetailTemp(1L, Member.builder().userId("").build()));

		//then
		assertEquals(itemException.getErrorCode(), MemberErrorCode.MEMBER_MISMATCH);
	}

	@DisplayName("06_00. readDetailAuction success")
	@Test
	public void test_06_00() {
		//given
		Item item = getItem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		item.setState(State.COMPLETE);
		given(itemRepository.findById(anyLong())).willReturn(Optional.of(item));

		//when
		ResponseForAuction responseForAuction = itemService.readDetailAuction(1L);

		//then
		assertEquals(responseForAuction.getName(), item.getName());
	}

	@DisplayName("06_01. readDetailAuction fail NOT_FOUND_ITEM")
	@Test
	public void test_06_01() {
		//given
		given(itemRepository.findById(anyLong())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> itemService.readDetailAuction(1L));

		//then
		assertEquals(restApiException.getErrorCode(), ItemErrorCode.NOT_FOUND_ITEM);
	}

	@DisplayName("06_02. readDetailAuction fail CAN_NOT_READ_STATE")
	@Test
	public void test_06_02() {
		//given
		Item item = getItem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		item.setState(State.TEMPORARY);
		given(itemRepository.findById(1L)).willReturn(Optional.of(item));
		item = getItem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		item.setId(2L);
		item.setState(State.RETURN);
		given(itemRepository.findById(2L)).willReturn(Optional.of(item));
		item = getItem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		item.setId(3L);
		item.setState(State.ASSESSING);
		given(itemRepository.findById(3L)).willReturn(Optional.of(item));

		//when
		RestApiException restApiException1 = assertThrows(RestApiException.class,
			() -> itemService.readDetailAuction(1L));
		RestApiException restApiException2 = assertThrows(RestApiException.class,
			() -> itemService.readDetailAuction(2L));
		RestApiException restApiException3 = assertThrows(RestApiException.class,
			() -> itemService.readDetailAuction(3L));

		//then
		assertEquals(restApiException1.getErrorCode(), ItemErrorCode.CAN_NOT_READ_STATE);
		assertEquals(restApiException2.getErrorCode(), ItemErrorCode.CAN_NOT_READ_STATE);
		assertEquals(restApiException3.getErrorCode(), ItemErrorCode.CAN_NOT_READ_STATE);
	}

	private Item getEmptyItem() {
		return Item.builder()
			.id(1L)
			.member(member)
			.state(State.TEMPORARY)
			.imageChecks(new ArrayList<>())
			.imageItems(new ArrayList<>())
			.styleItems(new ArrayList<>())
			.created(LocalDateTime.now())
			.updated(LocalDateTime.now())
			.build();

	}

	private Item getItem(List<ImageItem> imageItems, List<ImageCheck> imageChecks,
		List<StyleItem> styleItems) {
		return Item.builder()
			.id(1L)
			.name("test name")
			.member(member)
			.category(getCategory(categoryName1, root))
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
			.state(State.TEMPORARY)
			.created(LocalDateTime.now())
			.updated(LocalDateTime.now())
			.imageItems(new ArrayList<>(imageItems))
			.imageChecks(new ArrayList<>(imageChecks))
			.styleItems(new ArrayList<>(styleItems))
			.build();
	}

	private StyleItem getStyleItem(String styleName) {
		return StyleItem.builder()
			.style(Style.builder()
				.name(styleName)
				.build())
			.build();
	}

	private List<MultipartFile> getMultipartFiles(String extension) throws IOException {
		List<MultipartFile> multipartFile = new ArrayList<>();
		for (int i = 1; i <= 2; i++) {
			String file = String.format("%d.%s", i, extension);
			FileInputStream fis = new FileInputStream("src/main/resources/testImages/" + file);
			multipartFile.add(new MockMultipartFile(String.format("%d", i), file, extension, fis));
		}
		return multipartFile;
	}

	private UpdateRequest getUpdateRequest(
		List<ImageDto.UpdateRequest> imageItems,
		List<ImageDto.UpdateRequest> imageChecks,
		List<String> styleItems) {

		return UpdateRequest.builder()
			.itemId(1L)
			.name("update name")
			.category(categoryName1)
			.startPrice(200)
			.width(200L)
			.depth(200L)
			.height(200L)
			.material("철제")
			.conditionGrade("update conditionGrade")
			.conditionDescription("update conditionDescription")
			.text("update text")
			.designer("update designer")
			.brand("update brand")
			.productionYear(1023)
			.imageItemUrls(imageItems)
			.imageCheckUrls(imageChecks)
			.styles(styleItems)
			.build();
	}

	private static Category getCategory(String name, Category parent) {
		return Category.builder()
			.name(name)
			.parent(parent)
			.children(new ArrayList<>())
			.level(parent.getLevel() + 1)
			.build();
	}

	private static ImageItem getImageItem(Long id, String url) {
		return ImageItem.builder()
			.id(id)
			.url(url)
			.build();
	}

	private static ImageCheck getImageCheck(Long id, String url) {
		return ImageCheck.builder()
			.id(id)
			.url(url)
			.build();
	}

	private static ImageDto.UpdateRequest getImageUpdateRequest(Long id) {
		return ImageDto.UpdateRequest.builder()
			.imageId(id)
			.build();
	}
}