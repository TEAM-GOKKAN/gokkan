package com.gokkan.gokkan.domain.style.exception;

import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StyleErrorCode implements ErrorCode {

	NOT_FOUND_STYLE(HttpStatus.BAD_REQUEST, "존재하지 않는 스타일 입니다."),
	DUPLICATE_STYLE(HttpStatus.BAD_REQUEST, "중복된 스타일 입니다."),
	NOT_FOUND_STYLE_ITEM(HttpStatus.BAD_REQUEST, "존재하지 않는 아이템 스타일입니다."),
	;


	private final HttpStatus httpStatus;
	private final String message;
}
