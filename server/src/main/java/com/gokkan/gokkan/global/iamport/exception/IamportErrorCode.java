package com.gokkan.gokkan.global.iamport.exception;

import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum IamportErrorCode implements ErrorCode {

	IAMPORT_FAILED(HttpStatus.UNAUTHORIZED, "Certification failed"),
	IAMPORT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "정보가 없습니다."),
	IAMPORT_NOT_MATCH_AMOUNT(HttpStatus.BAD_REQUEST, "결제 금액이 일치하지 않습니다."),
	IAMPORT_ALREADY_PAID(HttpStatus.BAD_REQUEST, "이미 결제가 완료되었습니다."),
	IAMPORT_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "이미 취소되었습니다."),
	IAMPORT_PAYMENT_FAILED(HttpStatus.BAD_REQUEST, "요청이 실패하였습니다."),
	IAMPORT_PAYMENT_STATUS_IS_READY(HttpStatus.BAD_REQUEST, "결제 대기중인 주문입니다."),

	IAMPORT_NOT_PAID(HttpStatus.UNAUTHORIZED, "결제가 완료되지 않았습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
