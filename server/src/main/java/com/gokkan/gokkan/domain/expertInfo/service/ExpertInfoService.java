package com.gokkan.gokkan.domain.expertInfo.service;

import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import com.gokkan.gokkan.domain.expertInfo.domain.dto.ExpertInfoDto.RequestCreateExpertInfoByMemberId;
import com.gokkan.gokkan.domain.expertInfo.domain.dto.ExpertInfoDto.RequestCreateExpertInfoByNickName;
import com.gokkan.gokkan.domain.expertInfo.domain.dto.ExpertInfoDto.ResponseGetExpertInfo;
import com.gokkan.gokkan.domain.expertInfo.exception.ExpertInfoErrorCode;
import com.gokkan.gokkan.domain.expertInfo.repository.ExpertInfoRepository;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.exception.MemberErrorCode;
import com.gokkan.gokkan.domain.member.repository.MemberRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExpertInfoService {

	private final ExpertInfoRepository expertInfoRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public void createExpertInfoByMemberId(
		RequestCreateExpertInfoByMemberId requestCreateExpertInfoByMemberId) {
		log.info("전문가 정보 추가 멤버 아이디 : " + requestCreateExpertInfoByMemberId.getMemberId());
		Member member = memberRepository.findById(requestCreateExpertInfoByMemberId.getMemberId())
			.orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
		if (existByMemberId(member.getId())) {
			throw new RestApiException(ExpertInfoErrorCode.ALREADY_EXIST_MEMBER);
		}
		expertInfoRepository.save(ExpertInfo.builder()
			.member(member)
			.name(member.getName())
			.info(requestCreateExpertInfoByMemberId.getInfo())
			.build());
		log.info("전문가 정보 추가 완료");
	}

	@Transactional
	public void createExpertInfoByNickName(
		RequestCreateExpertInfoByNickName requestCreateExpertInfoByNickName) {
		log.info("전문가 정보 추가 닉네임 : " + requestCreateExpertInfoByNickName.getNickName());
		Member member = memberRepository.findByNickName(
				requestCreateExpertInfoByNickName.getNickName())
			.orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
		if (existByMemberId(member.getId())) {
			throw new RestApiException(ExpertInfoErrorCode.ALREADY_EXIST_MEMBER);
		}
		expertInfoRepository.save(ExpertInfo.builder()
			.member(member)
			.name(member.getName())
			.info(requestCreateExpertInfoByNickName.getInfo())
			.build());
		log.info("전문가 정보 추가 완료");
	}

	@Transactional
	public void updateExpertInfo(Member member, String info) {
		log.info("전문가 정보 수정 멤버 아이디 : " + member.getId());
		ExpertInfo expertInfo = findByMemberId(member.getId());
		expertInfo.updateInfo(info);
		expertInfoRepository.save(expertInfo);
		log.info("전문가 정보 수정 완료");
	}

	@Transactional(readOnly = true)
	public ResponseGetExpertInfo getExpertInfo(Member member) {
		log.info("전문가 정보 조회 멤버 아이디 : " + member.getId());
		ExpertInfo expertInfo = findByMemberId(member.getId());
		log.info("전문가 정보 조회 완료");
		return ResponseGetExpertInfo.fromEntity(expertInfo);
	}

	private boolean existByMemberId(Long memberId) {
		return expertInfoRepository.existsByMemberId(memberId);
	}

	private ExpertInfo findByMemberId(Long memberId) {
		return expertInfoRepository.findByMemberId(memberId)
			.orElseThrow(() -> new RestApiException(ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND));
	}

	public boolean isExpert(Member member) {
		return expertInfoRepository.existsByMemberId(member.getId());
	}
}
