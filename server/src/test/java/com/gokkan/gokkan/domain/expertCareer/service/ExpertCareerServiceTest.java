package com.gokkan.gokkan.domain.expertCareer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gokkan.gokkan.domain.expertCareer.domain.ExpertCareer;
import com.gokkan.gokkan.domain.expertCareer.domain.dto.ExpertCareerDto.RequestCreateExpertCareer;
import com.gokkan.gokkan.domain.expertCareer.domain.dto.ExpertCareerDto.RequestUpdateExpertCareer;
import com.gokkan.gokkan.domain.expertCareer.domain.dto.ExpertCareerDto.ResponseGetExpertCareer;
import com.gokkan.gokkan.domain.expertCareer.exception.ExpertCareerErrorCode;
import com.gokkan.gokkan.domain.expertCareer.repository.ExpertCareerRepository;
import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import com.gokkan.gokkan.domain.expertInfo.exception.ExpertInfoErrorCode;
import com.gokkan.gokkan.domain.expertInfo.repository.ExpertInfoRepository;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.exception.MemberErrorCode;
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
class ExpertCareerServiceTest {

	ArgumentCaptor<ExpertCareer> expertCareerArgumentCaptor = ArgumentCaptor.forClass(
		ExpertCareer.class);
	@Mock
	private ExpertCareerRepository expertCareerRepository;
	@Mock
	private ExpertInfoRepository expertInfoRepository;
	@InjectMocks
	private ExpertCareerService expertCareerService;

	@Test
	@DisplayName("전문가 경력 생성 성공")
	void createExpertCareer() {
		//given
		given(expertInfoRepository.findByMember(any())).willReturn(Optional.of(getExpertInfo()));

		//when
		expertCareerService.createExpertCareer(getMember(), getRequestCreateExpertCareer());
		verify(expertCareerRepository, times(3)).save(expertCareerArgumentCaptor.capture());

		//then
		List<ExpertCareer> expertCareers = expertCareerArgumentCaptor.getAllValues();
		for (int i = 1; i <= 3; i++) {
			ExpertCareer expertCareer = expertCareers.get(i - 1);
			assertThat(expertCareer.getExpertInfo().getName()).isEqualTo(getExpertInfo().getName());
			assertThat(expertCareer.getCompanyName()).isEqualTo("companyName" + i);
			assertThat(expertCareer.getPosition()).isEqualTo("position" + i);
		}
	}

	@Test
	@DisplayName("전문가 경력 생성 실패 - 멤버 정보 없음")
	void createExpertCareer_error_memberNotFound() {
		//given

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertCareerService.createExpertCareer(null, getRequestCreateExpertCareer());
		});

		//then
		assertThat(restApiException.getErrorCode()).isEqualTo(MemberErrorCode.MEMBER_NOT_FOUND);
	}

	@Test
	@DisplayName("전문가 경력 생성 실패 - 요청이 없음")
	void createExpertCareer_error_emptyExpertCareer() {
		//given

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertCareerService.createExpertCareer(getMember(), new ArrayList<>());
		});

		//then
		assertThat(restApiException.getErrorCode()).isEqualTo(
			ExpertCareerErrorCode.EMPTY_EXPERT_CAREER);
	}

	@Test
	@DisplayName("전문가 경력 생성 실패 - 전문가 정보 없음")
	void createExpertCareer_error_expertInfoNotFound() {
		//given
		given(expertInfoRepository.findByMember(any())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertCareerService.createExpertCareer(getMember(), getRequestCreateExpertCareer());
		});

		//then
		assertThat(restApiException.getErrorCode()).isEqualTo(
			ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND);
	}

	@Test
	@DisplayName("전문가 경력 수정 성공")
	void updateExpertCareer() {
		//given
		given(expertInfoRepository.findByMember(any())).willReturn(Optional.of(getExpertInfo()));
		given(expertCareerRepository.findById(1L)).willReturn(Optional.of(getExpertCareer(1)));
		given(expertCareerRepository.findById(2L)).willReturn(Optional.of(getExpertCareer(2)));
		given(expertCareerRepository.findById(3L)).willReturn(Optional.of(getExpertCareer(3)));

		//when
		expertCareerService.updateExpertCareer(getMember(), getRequestUpdateExpertCareer());
		verify(expertCareerRepository, times(3)).save(expertCareerArgumentCaptor.capture());

		//then
		List<ExpertCareer> expertCareers = expertCareerArgumentCaptor.getAllValues();
		for (int i = 1; i <= 3; i++) {
			ExpertCareer expertCareer = expertCareers.get(i - 1);
			assertThat(expertCareer.getCompanyName()).isEqualTo("CompanyName" + i);
			assertThat(expertCareer.getPosition()).isEqualTo("Position" + i);
		}
	}

	@Test
	@DisplayName("전문가 경력 수정 실패 - 멤버 정보 없음")
	void updateExpertCareer_error_memberNotFound() {
		//given

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertCareerService.updateExpertCareer(null, getRequestUpdateExpertCareer());
		});

		//then
		assertThat(restApiException.getErrorCode()).isEqualTo(MemberErrorCode.MEMBER_NOT_FOUND);
	}

	@Test
	@DisplayName("전문가 경력 수정 실패 - 요청이 없음")
	void updateExpertCareer_error_emptyExpertCareer() {
		//given

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertCareerService.updateExpertCareer(getMember(), new ArrayList<>());
		});

		//then
		assertThat(restApiException.getErrorCode()).isEqualTo(
			ExpertCareerErrorCode.EMPTY_EXPERT_CAREER);
	}

	@Test
	@DisplayName("전문가 경력 수정 실패 - 전문가 정보 없음")
	void updateExpertCareer_error_expertInfoNotFound() {
		//given
		given(expertInfoRepository.findByMember(any())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertCareerService.updateExpertCareer(getMember(), getRequestUpdateExpertCareer());
		});

		//then
		assertThat(restApiException.getErrorCode()).isEqualTo(
			ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND);
	}

	@Test
	@DisplayName("전문가 경력 수정 실패 - 전문가 경력 없음")
	void updateExpertCareer_error_expertCareerNotFound() {
		//given
		given(expertInfoRepository.findByMember(any())).willReturn(Optional.of(getExpertInfo()));
		given(expertCareerRepository.findById(1L)).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertCareerService.updateExpertCareer(getMember(), getRequestUpdateExpertCareer());
		});

		//then
		assertThat(restApiException.getErrorCode()).isEqualTo(
			ExpertCareerErrorCode.EXPERT_CAREER_NOT_FOUND);
	}

	@Test
	@DisplayName("전문가 경력 조회 성공")
	void getExpertCareer() {
		//given
		given(expertInfoRepository.findByMember(any())).willReturn(Optional.of(getExpertInfo()));
		given(expertCareerRepository.findByExpertInfo(any())).willReturn(getExpertCareerList());

		//when
		List<ResponseGetExpertCareer> expertCareerResponses = expertCareerService
			.getMyExpertCareer(getMember());

		//then
		for (int i = 1; i <= 3; i++) {
			ResponseGetExpertCareer expertCareerResponse = expertCareerResponses.get(i - 1);
			assertThat(expertCareerResponse.getCompanyName()).isEqualTo("companyName" + i);
			assertThat(expertCareerResponse.getPosition()).isEqualTo("position" + i);
		}
	}

	@Test
	@DisplayName("전문가 경력 조회 실패 - 멤버 정보 없음")
	void getExpertCareer_error_memberNotFound() {
		//given

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertCareerService.getMyExpertCareer(null);
		});

		//then
		assertThat(restApiException.getErrorCode()).isEqualTo(MemberErrorCode.MEMBER_NOT_FOUND);
	}

	@Test
	@DisplayName("전문가 경력 조회 실패 - 전문가 정보 없음")
	void getExpertCareer_error_expertInfoNotFound() {
		//given
		given(expertInfoRepository.findByMember(any())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertCareerService.getMyExpertCareer(getMember());
		});

		//then
		assertThat(restApiException.getErrorCode()).isEqualTo(
			ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND);
	}

	@Test
	@DisplayName("전문가 경력 삭제")
	void deleteExpertCareer() {
		//given
		given(expertCareerRepository.findById(anyLong())).willReturn(
			Optional.of(getExpertCareer(1)));

		//when
		expertCareerService.deleteExpertCareer(getMember(), 1L);

		//then
		verify(expertCareerRepository, times(1)).delete(any());
	}

	@Test
	@DisplayName("전문가 경력 삭제 실패 - 멤버 정보 없음")
	void deleteExpertCareer_error_memberNotFound() {
		//given

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertCareerService.deleteExpertCareer(null, 1L);
		});

		//then
		assertThat(restApiException.getErrorCode()).isEqualTo(MemberErrorCode.MEMBER_NOT_FOUND);
	}

	@Test
	@DisplayName("전문가 경력 삭제 실패 - 전문가 경력 아이디 null")
	void deleteExpertCareer_error_emptyExpertCareerId() {
		//given

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertCareerService.deleteExpertCareer(getMember(), null);
		});

		//then
		assertThat(restApiException.getErrorCode()).isEqualTo(
			ExpertCareerErrorCode.EMPTY_EXPERT_CAREER);
	}

	@Test
	@DisplayName("전문가 경력 삭제 실패 - 전문가 경력 없음")
	void deleteExpertCareer_error_expertCareerNotFound() {
		//given
		given(expertCareerRepository.findById(anyLong())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {
			expertCareerService.deleteExpertCareer(getMember(), 1L);
		});

		//then
		assertThat(restApiException.getErrorCode()).isEqualTo(
			ExpertCareerErrorCode.EXPERT_CAREER_NOT_FOUND);
	}


	private ExpertCareer getExpertCareer(int i) {
		return ExpertCareer.builder()
			.companyName("companyName" + i)
			.position("position" + i)
			.build();
	}


	private Member getMember() {
		return Member.builder()
			.name("memberName")
			.build();
	}

	private ExpertInfo getExpertInfo() {
		return ExpertInfo.builder()
			.member(getMember())
			.name("expertName")
			.build();
	}

	private List<RequestCreateExpertCareer> getRequestCreateExpertCareer() {
		return List.of(
			RequestCreateExpertCareer.builder()
				.companyName("companyName1")
				.position("position1")
				.build(),
			RequestCreateExpertCareer.builder()
				.companyName("companyName2")
				.position("position2")
				.build(),
			RequestCreateExpertCareer.builder()
				.companyName("companyName3")
				.position("position3")
				.build()
		);
	}

	private List<RequestUpdateExpertCareer> getRequestUpdateExpertCareer() {
		return List.of(
			RequestUpdateExpertCareer.builder()
				.expertCareerId(1L)
				.companyName("CompanyName1")
				.position("Position1")
				.build(),
			RequestUpdateExpertCareer.builder()
				.expertCareerId(2L)
				.companyName("CompanyName2")
				.position("Position2")
				.build(),
			RequestUpdateExpertCareer.builder()
				.expertCareerId(3L)
				.companyName("CompanyName3")
				.position("Position3")
				.build()
		);
	}

	private List<ExpertCareer> getExpertCareerList() {
		return List.of(
			ExpertCareer.builder()
				.companyName("companyName1")
				.position("position1")
				.build(),
			ExpertCareer.builder()
				.companyName("companyName2")
				.position("position2")
				.build(),
			ExpertCareer.builder()
				.companyName("companyName3")
				.position("position3")
				.build()
		);
	}
}