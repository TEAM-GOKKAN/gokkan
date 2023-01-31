package com.gokkan.gokkan.global.security.oauth.exception;

import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SecurityErrorCode implements ErrorCode {

	INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "Invalid JWT signature."),
	INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid JWT token."),
	EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "Expired JWT token."),
	UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "Unsupported JWT token."),
	ILLEGAL_ARGUMENT_JWT_TOKEN(HttpStatus.UNAUTHORIZED,
		"JWT token compact of handler are invalid.");

	private final HttpStatus httpStatus;
	private final String message;
}
