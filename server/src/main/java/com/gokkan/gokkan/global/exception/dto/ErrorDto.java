package com.gokkan.gokkan.global.exception.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class ErrorDto {

	@Getter
	@Builder
	@Setter
	public static class MessageError {

		private int status;
		private String code;
		private String message;

	}

}
