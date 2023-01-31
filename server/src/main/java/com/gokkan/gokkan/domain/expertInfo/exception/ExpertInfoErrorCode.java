package com.gokkan.gokkan.domain.expertInfo.exception;

import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExpertInfoErrorCode implements ErrorCode {
	ALREADY_EXIST_MEMBER(HttpStatus.BAD_REQUEST, "이미 존재하는 멤버입니다."),
	EXPERT_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 전문가 정보입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}