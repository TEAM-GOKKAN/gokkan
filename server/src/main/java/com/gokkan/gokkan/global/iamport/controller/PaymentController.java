package com.gokkan.gokkan.global.iamport.controller;

import com.gokkan.gokkan.global.iamport.dto.PaymentDto.PaymentVerifyResponse;
import com.gokkan.gokkan.global.iamport.service.IamportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payment")
@Tag(name = "결제 검증 컨트롤러", description = "결제 검증 컨트롤러")
public class PaymentController {

	private final IamportService iamportService;

	@GetMapping
	@Operation(summary = "결제 검증", description = "결제 검증")
	public ResponseEntity<PaymentVerifyResponse> paymentVerify(
		@Parameter(description = "경매 아이디") @RequestParam Long auctionId,
		@Parameter(description = "결제 후 받아온 imp-uid") @RequestParam String impUid) {
		String accessToken = iamportService.getAccessToken();
		return ResponseEntity.ok(iamportService.paymentVerify(auctionId, impUid, accessToken));
	}
}
