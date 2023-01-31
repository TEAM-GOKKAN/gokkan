package com.gokkan.gokkan.domain.expertCareer.exception;

import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExpertCareerErrorCode implements ErrorCode {

	EMPTY_EXPERT_CAREER(HttpStatus.BAD_REQUEST, "전문가 경력이 존재하지 않습니다."),
	EMPTY_EXPERT_CAREER_ID(HttpStatus.BAD_REQUEST, "전문가 경력 ID가 존재하지 않습니다."),
	EXPERT_CAREER_NOT_FOUND(HttpStatus.NOT_FOUND, "전문가 경력을 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
