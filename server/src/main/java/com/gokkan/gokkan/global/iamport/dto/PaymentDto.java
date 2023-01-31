package com.gokkan.gokkan.global.iamport.dto;

import lombok.Builder;
import lombok.Getter;

public class PaymentDto {

	@Getter
	@Builder
	public static class PaymentVerifyResponse {

		String name;
		String phoneNumber;
		String address;
		String pay_method;
	}
}
