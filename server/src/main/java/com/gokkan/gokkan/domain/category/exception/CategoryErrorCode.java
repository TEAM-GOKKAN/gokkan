package com.gokkan.gokkan.domain.category.exception;

import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CategoryErrorCode implements ErrorCode {
	NOT_FOUND_PARENT_CATEGORY(HttpStatus.BAD_REQUEST, "상위 카테고리가 존재하지 않습니다."),
	NOT_FOUND_CATEGORY(HttpStatus.BAD_REQUEST, "존재하지 않는 카테고리 입니다."),
	DUPLICATED_CATEGORY(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리 입니다."),
	CAN_NOT_SAME_PARENT_NAME(HttpStatus.BAD_REQUEST, "부모카테고리와 같은 이름을 사용 할 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
