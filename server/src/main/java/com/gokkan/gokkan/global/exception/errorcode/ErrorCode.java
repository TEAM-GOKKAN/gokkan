package com.gokkan.gokkan.global.exception.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

	String name();

	HttpStatus getHttpStatus();

	String getMessage();

}
