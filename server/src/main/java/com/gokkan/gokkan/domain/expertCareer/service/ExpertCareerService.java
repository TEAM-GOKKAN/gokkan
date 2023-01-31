package com.gokkan.gokkan.domain.expertCareer.service;

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
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExpertCareerService {

	private final ExpertCareerRepository expertCareerRepository;
	private final ExpertInfoRepository expertInfoRepository;

	@Transactional
	public void createExpertCareer(Member member,
		List<RequestCreateExpertCareer> requestCreateExpertCareer) {
		log.info("전문가 커리어 생성");
		if (member == null) {
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		if (requestCreateExpertCareer == null || requestCreateExpertCareer.isEmpty()) {
			throw new RestApiException(ExpertCareerErrorCode.EMPTY_EXPERT_CAREER);
		}
		ExpertInfo expertInfo = expertInfoRepository.findByMember(member).orElseThrow(
			() -> new RestApiException(ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND));
		log.info("전문가 아이디 : " + expertInfo.getId());
		for (RequestCreateExpertCareer request : requestCreateExpertCareer) {
			ExpertCareer expertCareer = expertCareerRepository.save(ExpertCareer.builder()
				.expertInfo(expertInfo)
				.startDate(request.getStartDate())
				.endDate(request.getEndDate())
				.companyName(request.getCompanyName())
				.position(request.getPosition())
				.build());
			expertInfo.addExpertCareer(expertCareer);
		}
		log.info("전문가 커리어 생성 완료");
	}

	@Transactional
	public void updateExpertCareer(Member member,
		List<RequestUpdateExpertCareer> requestUpdateExpertCareer) {
		log.info("전문가 커리어 수정");
		if (member == null) {
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		if (requestUpdateExpertCareer == null || requestUpdateExpertCareer.isEmpty()) {
			throw new RestApiException(ExpertCareerErrorCode.EMPTY_EXPERT_CAREER);
		}
		ExpertInfo expertInfo = expertInfoRepository.findByMember(member).orElseThrow(
			() -> new RestApiException(ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND));
		log.info("전문가 아이디 : " + expertInfo.getId());
		expertInfo.getExpertCareers().clear();
		for (RequestUpdateExpertCareer request : requestUpdateExpertCareer) {
			ExpertCareer expertCareer = expertCareerRepository.findById(
				request.getExpertCareerId()).orElseThrow(
				() -> new RestApiException(ExpertCareerErrorCode.EXPERT_CAREER_NOT_FOUND));
			expertCareer.update(request.getStartDate(), request.getEndDate(),
				request.getCompanyName(), request.getPosition());
			expertCareerRepository.save(expertCareer);
			expertInfo.addExpertCareer(expertCareer);
		}

		log.info("전문가 커리어 수정 완료");
	}

	@Transactional(readOnly = true)
	public List<ResponseGetExpertCareer> getMyExpertCareer(Member member) {
		log.info("전문가 커리어 조회");
		if (member == null) {
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		ExpertInfo expertInfo = expertInfoRepository.findByMember(member).orElseThrow(
			() -> new RestApiException(ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND));
		log.info("전문가 아이디 : " + expertInfo.getId());
		List<ResponseGetExpertCareer> expertCareerList = expertCareerRepository.findByExpertInfo(
				expertInfo).stream().map(ResponseGetExpertCareer::fromEntity)
			.collect(Collectors.toList());
		log.info("전문가 커리어 조회 완료");
		return expertCareerList;
	}

	@Transactional
	public void deleteExpertCareer(Member member, Long expertCareerId) {
		log.info("전문가 커리어 삭제");
		if (member == null) {
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		if (expertCareerId == null) {
			throw new RestApiException(ExpertCareerErrorCode.EMPTY_EXPERT_CAREER);
		}
		ExpertCareer expertCareer = expertCareerRepository.findById(expertCareerId).orElseThrow(
			() -> new RestApiException(ExpertCareerErrorCode.EXPERT_CAREER_NOT_FOUND));
		expertCareerRepository.delete(expertCareer);
		log.info("전문가 커리어 삭제 완료");
	}
}
