package com.gokkan.gokkan.domain.member.exception;

import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Member not found"),
	MEMBER_ALREADY_EXISTS(HttpStatus.ALREADY_REPORTED, "Member already exists"),
	MEMBER_CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "Member card not found"),
	MEMBER_ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "Member address not found"),
	MEMBER_NOT_LOGIN(HttpStatus.BAD_REQUEST, "member not login"),
	MEMBER_MISMATCH(HttpStatus.BAD_REQUEST, "member mismatch"),
	MEMBER_FORBIDDEN(HttpStatus.FORBIDDEN, "member not admin");

	private final HttpStatus httpStatus;
	private final String message;
}
