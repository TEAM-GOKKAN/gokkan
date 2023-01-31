package com.gokkan.gokkan.domain.category.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gokkan.gokkan.domain.category.domain.Category;
import com.gokkan.gokkan.domain.category.dto.CategoryDto.CreateRequest;
import com.gokkan.gokkan.domain.category.dto.CategoryDto.Response;
import com.gokkan.gokkan.domain.category.dto.CategoryDto.UpdateRequest;
import com.gokkan.gokkan.domain.category.exception.CategoryErrorCode;
import com.gokkan.gokkan.domain.category.repository.CategoryRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@Transactional
class CategoryServiceTest {

	String categoryName1 = "c1";
	String categoryName11 = "c11";
	String categoryName12 = "c12";
	String categoryName2 = "c2";
	String categoryName21 = "c21";
	Category root = Category.builder()
		.id(0L)
		.parent(null)
		.level(0)
		.name("root")
		.children(new ArrayList<>())
		.build();
	ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
	@Mock
	private CategoryRepository categoryRepository;
	@InjectMocks
	private CategoryService categoryService;

	@DisplayName("01_01. create root category success already exist root")
	@Test
	public void test_01_01() {
		//given
		given(categoryRepository.existsByName(categoryName1)).willReturn(false);
		given(categoryRepository.findByName("root")).willReturn(Optional.of(root));
		given(categoryRepository.save(any())).willReturn(getCategory(categoryName1, root));

		//when
		CreateRequest request = getCreateRequest(categoryName1, null);
		categoryService.create(request);

		verify(categoryRepository, times(1)).save(categoryCaptor.capture());

		//then
		Category category = categoryCaptor.getValue();

		assertEquals(category.getName(), categoryName1);
		assertEquals(category.getParent().getName(), "root");
		assertEquals(category.getParent().getLevel(), 0);
		assertEquals(category.getLevel(), 1);
	}

	@DisplayName("01_02. create not root category success")
	@Test
	public void test_01_02() {
		//given
		Category parent = getCategory(categoryName1, root);
		given(categoryRepository.existsByName(categoryName11)).willReturn(false);
		given(categoryRepository.findByName(categoryName1))
			.willReturn(Optional.of(parent));
		given(categoryRepository.save(any()))
			.willReturn(getCategory(categoryName11, parent));

		//when
		CreateRequest request = getCreateRequest(categoryName11, parent.getName());
		categoryService.create(request);

		verify(categoryRepository, times(1)).save(categoryCaptor.capture());

		//then
		Category category = categoryCaptor.getValue();

		assertEquals(category.getName(), categoryName11);
		assertEquals(category.getParent().getName(), categoryName1);
		assertEquals(category.getParent().getLevel(), 1);
		assertEquals(category.getLevel(), 2);
	}

	@DisplayName("01_03. create not root category fail not found parent")
	@Test
	public void test_01_03() {
		//given
		Category parent = getCategory(categoryName1, root);
		given(categoryRepository.existsByName(categoryName11)).willReturn(false);
		given(categoryRepository.findByName(categoryName1))
			.willReturn(Optional.empty());

		//when
		CreateRequest request = getCreateRequest(categoryName11, parent.getName());
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> categoryService.create(request));

		//then
		assertEquals(restApiException.getErrorCode(), CategoryErrorCode.NOT_FOUND_PARENT_CATEGORY);
	}

	@DisplayName("01_04. create not root category fail duplicate category")
	@Test
	public void test_01_04() {
		//given
		Category parent = getCategory(categoryName1, root);
		given(categoryRepository.existsByName(categoryName11)).willReturn(true);

		//when
		CreateRequest request = getCreateRequest(categoryName11, parent.getName());
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> categoryService.create(request));

		//then
		assertEquals(restApiException.getErrorCode(), CategoryErrorCode.DUPLICATED_CATEGORY);
	}

	@DisplayName("01_05. create fail fail same parent and child")
	@Test
	public void test_01_05() {
		//given

		//when
		CreateRequest request = getCreateRequest(categoryName1, categoryName1);
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> categoryService.create(request));

		//then
		assertEquals(restApiException.getErrorCode(), CategoryErrorCode.CAN_NOT_SAME_PARENT_NAME);
	}

	@DisplayName("02_00. read success root")
	@Test
	public void test_02_00() {
		//given
		Category category = getCategory(categoryName1, root);
		List<Category> children = category.getChildren();
		children.add(getCategory(categoryName11, category));
		children.add(getCategory(categoryName12, category));

		given(categoryRepository.findByName(any()))
			.willReturn(Optional.of(category));

		//when
		Response response = categoryService.read(categoryName1);

		//then
		assertEquals(response.getName(), categoryName1);
		assertEquals(response.getParent(), "root");
		assertEquals(response.getChildren().size(), 2);
	}

	@DisplayName("02_01. read success no children")
	@Test
	public void test_02_01() {
		//given
		Category parent = getCategory(categoryName1, root);
		Category category = getCategory(categoryName11, parent);
		given(categoryRepository.findByName(any()))
			.willReturn(Optional.of(category));

		//when
		Response response = categoryService.read(categoryName11);

		//then
		assertEquals(response.getName(), categoryName11);
		assertEquals(response.getParent(), categoryName1);
		assertEquals(response.getChildren().size(), 0);
	}

	@DisplayName("02_02. read fail not found category")
	@Test
	public void test_02_02() {
		//given
		given(categoryRepository.findByName(any())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> categoryService.read(categoryName11));

		//then
		assertEquals(restApiException.getErrorCode(), CategoryErrorCode.NOT_FOUND_CATEGORY);
	}

	@DisplayName("03_00. delete success")
	@Test
	public void test_03_00() {
		//given
		given(categoryRepository.findByName(any()))
			.willReturn(Optional.of(getCategory(categoryName1, root)));

		//when
		boolean deleted = categoryService.delete(categoryName1);
		verify(categoryRepository, times(1)).delete(categoryCaptor.capture());

		//then
		Category category = categoryCaptor.getValue();
		assertTrue(deleted);
		assertEquals(category.getName(), categoryName1);
	}

	@DisplayName("03_01. delete fail not found category")
	@Test
	public void test_03_01() {
		//given
		given(categoryRepository.findByName(any())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> categoryService.delete(categoryName1));

		//then
		assertEquals(restApiException.getErrorCode(), CategoryErrorCode.NOT_FOUND_CATEGORY);
	}

	@DisplayName("04_00. update success same parent")
	@Test
	public void test_04_00() {
		//given
		given(categoryRepository.existsByName(categoryName2)).willReturn(false);
		given(categoryRepository.findById(any()))
			.willReturn(Optional.of(getCategory(categoryName1, root)));
		given(categoryRepository.save(any())).willReturn(getCategory(categoryName2, root));

		//when
		UpdateRequest request = getUpdateRequest(categoryName2, "root", 1L);
		categoryService.update(request);

		verify(categoryRepository, times(1)).save(categoryCaptor.capture());

		//then
		Category category = categoryCaptor.getValue();

		assertEquals(category.getName(), categoryName2);
		assertEquals(category.getParent().getName(), "root");
	}

	@DisplayName("04_01. update success different parent")
	@Test
	public void test_04_01() {
		//given
		given(categoryRepository.existsByName(categoryName21)).willReturn(false);
		given(categoryRepository.findById(any()))
			.willReturn(Optional.of(getCategory(categoryName12, getCategory(categoryName1, root))));
		given(categoryRepository.findByName(categoryName2))
			.willReturn(Optional.of(getCategory(categoryName2, root)));
		given(categoryRepository.save(any()))
			.willReturn(getCategory(categoryName21, getCategory(categoryName2, root)));

		//when
		UpdateRequest request = getUpdateRequest(categoryName21, categoryName2, 1L);
		categoryService.update(request);

		verify(categoryRepository, times(1)).save(categoryCaptor.capture());

		//then
		Category category = categoryCaptor.getValue();

		assertEquals(category.getName(), categoryName21);
		assertEquals(category.getParent().getName(), categoryName2);
	}

	@DisplayName("04_02. update fail duplicate category")
	@Test
	public void test_04_02() {
		//given
		given(categoryRepository.findById(any())).willReturn(
			Optional.of(getCategory(categoryName1, root)));
		given(categoryRepository.existsByName(categoryName2)).willReturn(true);

		//when
		UpdateRequest request = getUpdateRequest(categoryName2, "root", 1L);
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> categoryService.update(request));

		//then
		assertEquals(restApiException.getErrorCode(), CategoryErrorCode.DUPLICATED_CATEGORY);
	}

	@DisplayName("04_03. update fail not found category")
	@Test
	public void test_04_03() {
		//given
		given(categoryRepository.findById(any())).willReturn(Optional.empty());

		//when
		UpdateRequest request = getUpdateRequest(categoryName2, "root", 1L);
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> categoryService.update(request));

		//then
		assertEquals(restApiException.getErrorCode(), CategoryErrorCode.NOT_FOUND_CATEGORY);
	}

	@DisplayName("04_04. update fail not found parent category")
	@Test
	public void test_04_04() {
		//given
		given(categoryRepository.existsByName(categoryName21)).willReturn(false);
		given(categoryRepository.findById(any()))
			.willReturn(Optional.of(getCategory(categoryName12, getCategory(categoryName1, root))));
		given(categoryRepository.findByName(categoryName2))
			.willReturn(Optional.empty());

		//when
		UpdateRequest request = getUpdateRequest(categoryName21, categoryName2, 1L);
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> categoryService.update(request));

		//then
		assertEquals(restApiException.getErrorCode(), CategoryErrorCode.NOT_FOUND_PARENT_CATEGORY);
	}

	@DisplayName("04_05. update fail same parent and child")
	@Test
	public void test_04_05() {
		//given

		//when
		UpdateRequest request = getUpdateRequest(categoryName2, categoryName2, 1L);
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> categoryService.update(request));

		//then
		assertEquals(restApiException.getErrorCode(), CategoryErrorCode.CAN_NOT_SAME_PARENT_NAME);
	}

	@DisplayName("05_00. readAll success")
	@Test
	public void test_05_00() {
		//given
		Category root1 = Category.builder()
			.name("root")
			.level(0)
			.parent(null)
			.build();
		Category category1 = getCategory(categoryName1, root1);
		Category category11 = getCategory(categoryName11, category1);
		Category category12 = getCategory(categoryName12, category1);
		category1.setChildren(new ArrayList<>(List.of(category11, category12)));

		Category category2 = getCategory(categoryName2, root1);
		Category category21 = getCategory(categoryName21, category2);
		category2.setChildren(new ArrayList<>(List.of(category21)));
		root1.setChildren(new ArrayList<>(List.of(category1, category2)));

		given(categoryRepository.findByName("root")).willReturn(
			Optional.of(root1));

		//when
		Response response = categoryService.readAll();

		//then
		assertEquals(response.getChildren().size(), 2);
		assertEquals(response.getChildren().get(0).getChildren().size(), 2);
		assertEquals(response.getChildren().get(1).getChildren().size(), 1);
	}

	@DisplayName("05_01. readAll fail ")
	@Test
	public void test_05_01() {
		//given
		given(categoryRepository.findByName("root")).willReturn(
			Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> categoryService.readAll());

		//then
		assertEquals(restApiException.getErrorCode(), CategoryErrorCode.NOT_FOUND_CATEGORY);
	}

	private Category getCategory(String name, Category parent) {
		return Category.builder()
			.name(name)
			.parent(parent)
			.children(new ArrayList<>())
			.level(parent.getLevel() + 1)
			.build();
	}

	private CreateRequest getCreateRequest(String name, String parent) {
		return CreateRequest.builder()
			.parent(parent)
			.name(name)
			.build();
	}

	private UpdateRequest getUpdateRequest(String name, String parent, Long id) {
		return UpdateRequest.builder()
			.id(id)
			.parent(parent)
			.name(name)
			.build();
	}
}