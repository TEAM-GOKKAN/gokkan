package com.gokkan.gokkan.domain.expertComment.exception;

import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExpertCommentErrorCode implements ErrorCode {

	ITEM_STATE_NOT_ASSESSING(HttpStatus.BAD_REQUEST, "아이템 상태가 감정중이 아닙니다."),
	NOT_FOUND_STATE(HttpStatus.BAD_REQUEST, "상태가 존재하지 않습니다."),
	NOT_FOUND_EXPERT_COMMENT(HttpStatus.NOT_FOUND, "전문가 코멘트가 존재하지 않습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
