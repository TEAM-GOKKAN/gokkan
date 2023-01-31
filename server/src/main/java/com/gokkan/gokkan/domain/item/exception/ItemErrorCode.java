package com.gokkan.gokkan.domain.item.exception;

import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ItemErrorCode implements ErrorCode {


	NOT_FOUND_ITEM(HttpStatus.BAD_REQUEST, "해당 상품이 존재하지 않습니다."),
	CAN_NOT_FIX_STATE(HttpStatus.FORBIDDEN, "해당 상품은 수정 또는 삭제할 수 없는 상태 입니다."),
	CAN_NOT_READ_STATE(HttpStatus.FORBIDDEN, "해당 상품은 조회할 수 없는 상태 입니다."),
	CATEGORY_NOT_NUL(HttpStatus.BAD_REQUEST, "저장되는 카테고리는 null 일 수 없습니다."),
	STYLE_NOT_NULL(HttpStatus.BAD_REQUEST, "저장되는 스타일은 null 일 수 없습니다."),
	;
	private final HttpStatus httpStatus;
	private final String message;
}
