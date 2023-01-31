package com.gokkan.gokkan.domain.style.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import com.gokkan.gokkan.domain.expertInfo.exception.ExpertInfoErrorCode;
import com.gokkan.gokkan.domain.expertInfo.repository.ExpertInfoRepository;
import com.gokkan.gokkan.domain.style.domain.ExpertStyle;
import com.gokkan.gokkan.domain.style.domain.Style;
import com.gokkan.gokkan.domain.style.exception.StyleErrorCode;
import com.gokkan.gokkan.domain.style.repository.ExpertStyleRepository;
import com.gokkan.gokkan.domain.style.repository.StyleRepository;
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

@ExtendWith(MockitoExtension.class)
class ExpertStyleServiceTest {

	ArgumentCaptor<ExpertStyle> expertStyleArgumentCaptor = ArgumentCaptor.forClass(
		ExpertStyle.class);
	@Mock
	private ExpertStyleRepository expertStyleRepository;
	@Mock
	private StyleRepository styleRepository;
	@Mock
	private ExpertInfoRepository expertInfoRepository;
	@InjectMocks
	private ExpertStyleService expertStyleService;

	@Test
	@DisplayName("전문가 스타일 생성(스타일 id) 성공")
	void createStyleByStyleId_success() {
		//given
		ExpertInfo expertInfo = getExpertInfo();
		Style style = getStyle();
		given(expertInfoRepository.findById(anyLong())).willReturn(Optional.of(expertInfo));
		given(styleRepository.findById(anyLong())).willReturn(Optional.of(style));
		//when
		expertStyleService.createStyleByStyleId(1L, 1L);
		//then
		verify(expertStyleRepository).save(expertStyleArgumentCaptor.capture());
		ExpertStyle expertStyle = expertStyleArgumentCaptor.getValue();
		assertEquals(expertStyle.getExpertInfo(), expertInfo);
		assertEquals(expertStyle.getStyle(), style);
	}

	@Test
	@DisplayName("전문가 스타일 생성(스타일 id) 실패 - 전문가 정보 없음")
	void createStyleByStyleId_error_notFoundExpertInfo() {
		//given
		given(expertInfoRepository.findById(anyLong())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertStyleService.createStyleByStyleId(1L, 1L);
		});

		//then
		assertEquals(restApiException.getErrorCode(), ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND);
	}

	@Test
	@DisplayName("전문가 스타일 생성(스타일 id) 실패 - 스타일 정보 없음")
	void createStyleByStyleId_error_notFoundStyle() {
		//given
		given(expertInfoRepository.findById(anyLong())).willReturn(Optional.of(getExpertInfo()));
		given(styleRepository.findById(anyLong())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertStyleService.createStyleByStyleId(1L, 1L);
		});

		//then
		assertEquals(restApiException.getErrorCode(), StyleErrorCode.NOT_FOUND_STYLE);
	}

	@Test
	@DisplayName("전문가 스타일 생성(스타일 이름) 성공")
	void createStyleByStyleName() {
		//given
		ExpertInfo expertInfo = getExpertInfo();
		Style style = getStyle();
		given(expertInfoRepository.findById(anyLong())).willReturn(Optional.of(expertInfo));
		given(styleRepository.findByName(any())).willReturn(Optional.of(style));
		//when
		expertStyleService.createStyleByStyleName(1L, "test");

		//then
		verify(expertStyleRepository).save(expertStyleArgumentCaptor.capture());
		ExpertStyle expertStyle = expertStyleArgumentCaptor.getValue();
		assertEquals(expertStyle.getExpertInfo(), expertInfo);
		assertEquals(expertStyle.getStyle(), style);
	}

	@Test
	@DisplayName("전문가 스타일 생성(스타일 이름) 실패 - 전문가 정보 없음")
	void createStyleByStyleName_error_notFoundExpertInfo() {
		//given
		given(expertInfoRepository.findById(anyLong())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertStyleService.createStyleByStyleId(1L, 1L);
		});

		//then
		assertEquals(restApiException.getErrorCode(), ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND);
	}

	@Test
	@DisplayName("전문가 스타일 생성(스타일 이름) 실패 - 스타일 정보 없음")
	void createStyleByStyleName_error_notFoundStyle() {
		//given
		given(expertInfoRepository.findById(anyLong())).willReturn(Optional.of(getExpertInfo()));
		given(styleRepository.findByName(any())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertStyleService.createStyleByStyleName(1L, "any");
		});

		//then
		assertEquals(restApiException.getErrorCode(), StyleErrorCode.NOT_FOUND_STYLE);
	}

	@Test
	@DisplayName("전문가 스타일 삭제 성공")
	void deleteExpertStyle_success() {
		//given
		ExpertStyle expertStyle = getExpertStyle();
		given(expertStyleRepository.findById(anyLong())).willReturn(Optional.of(expertStyle));

		//when
		expertStyleService.deleteExpertStyle(1L);

		//then
		verify(expertStyleRepository, times(1)).delete(expertStyle);
	}

	@Test
	@DisplayName("전문가 스타일 삭제 실패 - 전문가 정보 없음")
	void deleteExpertStyle_error_notFoundExpert() {
		//given
		given(expertStyleRepository.findById(anyLong())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertStyleService.deleteExpertStyle(1L);
		});

		//then
		assertEquals(restApiException.getErrorCode(), StyleErrorCode.NOT_FOUND_STYLE);
	}

	@Test
	@DisplayName("전문가 스타일 삭제(스타일 이름) 성공")
	void deleteStyleByStyleName_success() {
		//given
		ExpertInfo expertInfo = getExpertInfo();
		given(expertInfoRepository.findById(anyLong())).willReturn(Optional.of(expertInfo));
		Style style = getStyle();
		given(styleRepository.findByName(any())).willReturn(Optional.of(style));
		ExpertStyle expertStyle = getExpertStyle();
		given(expertStyleRepository.findByExpertInfoAndStyle(any(), any())).willReturn(
			Optional.of(expertStyle));

		//when
		expertStyleService.deleteStyleByStyleName(1L, "test");

		//then
		verify(expertStyleRepository, times(1)).delete(expertStyle);
	}

	@Test
	@DisplayName("전문가 스타일 삭제(스타일 이름) 실패 - 전문가 정보 없음")
	void deleteStyleByStyleName_error_notFoundExpert() {
		//given
		given(expertInfoRepository.findById(anyLong())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertStyleService.deleteStyleByStyleName(1L, "test");
		});

		//then
		assertEquals(restApiException.getErrorCode(), ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND);
	}

	@Test
	@DisplayName("전문가 스타일 삭제(스타일 이름) 실패 - 스타일 정보 없음")
	void deleteStyleByStyleName_error_notFoundStyle() {
		//given
		given(expertInfoRepository.findById(anyLong())).willReturn(Optional.of(getExpertInfo()));
		given(styleRepository.findByName(any())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertStyleService.deleteStyleByStyleName(1L, "test");
		});

		//then
		assertEquals(restApiException.getErrorCode(), StyleErrorCode.NOT_FOUND_STYLE);
	}

	@Test
	@DisplayName("전문가 스타일 삭제(스타일 이름) 실패 - 전문가 스타일 정보 없음")
	void deleteStyleByStyleName_error_notFoundExpertStyle() {
		//given
		given(expertInfoRepository.findById(anyLong())).willReturn(Optional.of(getExpertInfo()));
		given(styleRepository.findByName(any())).willReturn(Optional.of(getStyle()));
		given(expertStyleRepository.findByExpertInfoAndStyle(any(), any())).willReturn(
			Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertStyleService.deleteStyleByStyleName(1L, "test");
		});

		//then
		assertEquals(restApiException.getErrorCode(), StyleErrorCode.NOT_FOUND_STYLE);
	}

	@Test
	@DisplayName("전문가 스타일 조회 성공")
	void getExpertStyles_success() {
		//given
		ExpertInfo expertInfo = getExpertInfo();
		List<ExpertStyle> expertStyles = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			expertStyles.add(getExpertStyle());
		}
		expertInfo.setExpertStyles(expertStyles);
		given(expertInfoRepository.findById(anyLong())).willReturn(Optional.of(expertInfo));

		//when
		List<String> allByExpertInfo = expertStyleService.getExpertStyles(1L);

		//then
		assertEquals(allByExpertInfo.size(), 3);
	}

	@Test
	@DisplayName("전문가 스타일 조회 실패 - 전문가 정보 없음")
	void getExpertStyles_error_notFoundExpertInfo() {
		//given
		given(expertInfoRepository.findById(anyLong())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertStyleService.getExpertStyles(1L);
		});

		//then
		assertEquals(restApiException.getErrorCode(), ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND);
	}

	@Test
	@DisplayName("전문가 스타일 조회 실패 - 전문가 스타일 정보 없음")
	void getExpertStyles_error_notFoundExpertStyle() {
		//given
		given(expertInfoRepository.findById(anyLong())).willReturn(Optional.of(getExpertInfo()));
		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertStyleService.getExpertStyles(1L);
		});

		//then
		assertEquals(restApiException.getErrorCode(), StyleErrorCode.NOT_FOUND_STYLE);
	}

	private ExpertInfo getExpertInfo() {
		return ExpertInfo.builder()
			.info("info")
			.build();
	}

	private Style getStyle() {
		return Style.builder()
			.id(1L)
			.name("test")
			.build();
	}

	private ExpertStyle getExpertStyle() {
		return ExpertStyle.builder()
			.expertInfo(getExpertInfo())
			.style(getStyle())
			.build();
	}
}