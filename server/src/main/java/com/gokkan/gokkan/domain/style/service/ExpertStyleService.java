package com.gokkan.gokkan.domain.style.service;

import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import com.gokkan.gokkan.domain.expertInfo.exception.ExpertInfoErrorCode;
import com.gokkan.gokkan.domain.expertInfo.repository.ExpertInfoRepository;
import com.gokkan.gokkan.domain.style.domain.ExpertStyle;
import com.gokkan.gokkan.domain.style.domain.Style;
import com.gokkan.gokkan.domain.style.exception.StyleErrorCode;
import com.gokkan.gokkan.domain.style.repository.ExpertStyleRepository;
import com.gokkan.gokkan.domain.style.repository.StyleRepository;
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
public class ExpertStyleService {

	private final ExpertStyleRepository expertStyleRepository;
	private final ExpertInfoRepository expertInfoRepository;
	private final StyleRepository styleRepository;

	@Transactional
	public void createStyleByStyleId(Long expertInfoId, Long styleId) {
		log.info("전문가 스타일 생성 전문가 정보 Id : " + expertInfoId + " 스타일 Id : " + styleId);
		ExpertInfo expertInfo = expertInfoRepository.findById(expertInfoId)
			.orElseThrow(() -> new RestApiException(
				ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND));
		Style style = styleRepository.findById(styleId).orElseThrow(() -> new RestApiException(
			StyleErrorCode.NOT_FOUND_STYLE));
		ExpertStyle expertStyle = expertStyleRepository.save(ExpertStyle.builder()
			.expertInfo(expertInfo)
			.style(style)
			.build());
		expertInfo.addExpertStyle(expertStyle);
		log.info("전문가 스타일 생성 완료");
	}

	@Transactional
	public void createStyleByStyleName(Long expertInfoId, String styleName) {
		log.info("전문가 스타일 생성 전문가 정보 Id : " + expertInfoId + " 스타일 이름 : " + styleName);
		ExpertInfo expertInfo = expertInfoRepository.findById(expertInfoId)
			.orElseThrow(() -> new RestApiException(
				ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND));
		Style style = styleRepository.findByName(styleName).orElseThrow(() -> new RestApiException(
			StyleErrorCode.NOT_FOUND_STYLE));
		ExpertStyle expertStyle = expertStyleRepository.save(ExpertStyle.builder()
			.expertInfo(expertInfo)
			.style(style)
			.build());
		expertInfo.addExpertStyle(expertStyle);
		log.info("전문가 스타일 생성 완료");
	}

	@Transactional
	public void deleteExpertStyle(Long expertStyleId) {
		log.info("전문가 스타일 삭제 전문가 스타일 Id : " + expertStyleId);
		ExpertStyle expertStyle = expertStyleRepository.findById(expertStyleId)
			.orElseThrow(() -> new RestApiException(
				StyleErrorCode.NOT_FOUND_STYLE));
		expertStyleRepository.delete(expertStyle);
		log.info("전문가 스타일 삭제 완료");
	}

	@Transactional
	public void deleteStyleByStyleName(Long expertInfoId, String styleName) {
		log.info("전문가 스타일 삭제 전문가 정보 Id : " + expertInfoId + " 스타일 이름 : " + styleName);
		ExpertInfo expertInfo = expertInfoRepository.findById(expertInfoId)
			.orElseThrow(() -> new RestApiException(
				ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND));
		Style style = styleRepository.findByName(styleName).orElseThrow(() -> new RestApiException(
			StyleErrorCode.NOT_FOUND_STYLE));
		ExpertStyle expertStyle = expertStyleRepository.findByExpertInfoAndStyle(expertInfo, style)
			.orElseThrow(() -> new RestApiException(
				StyleErrorCode.NOT_FOUND_STYLE));
		expertStyleRepository.delete(expertStyle);
		log.info("전문가 스타일 삭제 완료");
	}

	@Transactional(readOnly = true)
	public List<String> getExpertStyles(Long expertInfoId) {
		log.info("전문가 스타일 조회 전문가 정보 Id : " + expertInfoId);
		ExpertInfo expertInfo = expertInfoRepository.findById(expertInfoId)
			.orElseThrow(() -> new RestApiException(
				ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND));
//		List<String> allByExpertInfo = expertStyleRepository.findAllByExpertInfo(expertInfo)
//			.stream().map(
//				ExpertStyle::getStyleName).collect(Collectors.toList());
		List<String> allByExpertInfo = expertInfo.getExpertStyles().stream().map(
			ExpertStyle::getStyleName).collect(Collectors.toList());
		if (allByExpertInfo.isEmpty()) {
			throw new RestApiException(StyleErrorCode.NOT_FOUND_STYLE);
		}
		log.info("전문가 스타일 조회 완료");
		return allByExpertInfo;
	}
}
