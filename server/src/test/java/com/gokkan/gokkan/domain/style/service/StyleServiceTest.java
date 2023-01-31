package com.gokkan.gokkan.domain.style.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gokkan.gokkan.domain.style.domain.Style;
import com.gokkan.gokkan.domain.style.dto.StyleDto;
import com.gokkan.gokkan.domain.style.dto.StyleDto.CreateRequest;
import com.gokkan.gokkan.domain.style.dto.StyleDto.Response;
import com.gokkan.gokkan.domain.style.dto.StyleDto.UpdateRequest;
import com.gokkan.gokkan.domain.style.exception.StyleErrorCode;
import com.gokkan.gokkan.domain.style.repository.StyleRepository;
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
class StyleServiceTest {

	static String name = "test";
	static Long id = 1L;
	ArgumentCaptor<Style> styleCaptor = ArgumentCaptor.forClass(Style.class);
	@Mock
	private StyleRepository styleRepository;
	@InjectMocks
	private StyleService styleService;

	@DisplayName("01_00. create success")
	@Test
	public void test_01_00() {
		//given
		given(styleRepository.existsByName(any())).willReturn(false);
		given(styleRepository.save(any())).willReturn(getStyle(name, 1L));

		//when
		CreateRequest createRequest = getCreateRequest(name);
		styleService.create(createRequest);

		verify(styleRepository, times(1)).save(styleCaptor.capture());

		//then
		Style style = styleCaptor.getValue();
		assertEquals(createRequest.getName(), style.getName());
	}

	@DisplayName("01_01. create fail duplicate name")
	@Test
	public void test_01_01() {
		//given
		given(styleRepository.existsByName(any())).willReturn(true);

		//when
		CreateRequest createRequest = getCreateRequest(name);
		RestApiException styleException = assertThrows(RestApiException.class,
			() -> styleService.create(createRequest));

		//then
		assertEquals(styleException.getErrorCode(), StyleErrorCode.DUPLICATE_STYLE);
	}

	@DisplayName("02_00. read success")
	@Test
	public void test_02_00() {
		//given
		given(styleRepository.findByName(any())).willReturn(Optional.of(getStyle(name, 1L)));

		//when
		Response response = styleService.read(name);

		//then
		assertEquals(response.getName(), name);
		assertEquals(response.getId(), 1L);
	}

	@DisplayName("02_01. read fail not found style")
	@Test
	public void test_02_01() {
		//given
		given(styleRepository.findByName(any())).willReturn(Optional.empty());

		//when
		RestApiException styleException = assertThrows(RestApiException.class,
			() -> styleService.read(name));

		//then
		assertEquals(styleException.getErrorCode(), StyleErrorCode.NOT_FOUND_STYLE);
	}

	@DisplayName("03_00. delete success")
	@Test
	public void test_03_00() {
		//given
		given(styleRepository.findByName(any())).willReturn(Optional.of(getStyle(name, id)));

		//when
		boolean deleted = styleService.delete(name);
		verify(styleRepository, times(1)).delete(any());

		//then
		assertTrue(deleted);
	}

	@DisplayName("03_01. delete fail not found style")
	@Test
	public void test_03_01() {
		//given
		given(styleRepository.findByName(any())).willReturn(Optional.empty());

		//when
		RestApiException styleException = assertThrows(RestApiException.class,
			() -> styleService.delete(name));

		//then
		assertEquals(styleException.getErrorCode(), StyleErrorCode.NOT_FOUND_STYLE);
	}

	@DisplayName("04_00. update success")
	@Test
	public void test_04_00() {
		//given
		String updatedName = "update";
		given(styleRepository.findById(any())).willReturn(Optional.of(getStyle(name, id)));
		given(styleRepository.save(any())).willReturn(getStyle(updatedName, id));

		//when
		styleService.update(getUpdateRequest(updatedName, id));
		verify(styleRepository, times(1)).save(styleCaptor.capture());

		//then
		Style style = styleCaptor.getValue();
		assertEquals(style.getName(), updatedName);
		assertEquals(style.getId(), 1L);
	}

	@DisplayName("04_01. update fail not found style")
	@Test
	public void test_04_01() {
		//given
		String updatedName = "update";
		given(styleRepository.findById(any())).willReturn(Optional.empty());

		//when
		RestApiException styleException = assertThrows(RestApiException.class,
			() -> styleService.update(getUpdateRequest(updatedName, id)));

		//then
		assertEquals(styleException.getErrorCode(), StyleErrorCode.NOT_FOUND_STYLE);
	}

	private static UpdateRequest getUpdateRequest(String name, Long id) {
		return StyleDto.UpdateRequest.builder()
			.id(id)
			.name(name)
			.build();
	}

	private static CreateRequest getCreateRequest(String name) {
		return StyleDto.CreateRequest.builder()
			.name(name)
			.build();
	}

	private static Style getStyle(String name, Long id) {
		return Style.builder()
			.id(id)
			.name(name)
			.build();
	}

}