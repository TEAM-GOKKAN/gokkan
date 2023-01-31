package com.gokkan.gokkan.global.iamport.controller;

import com.gokkan.gokkan.global.iamport.service.IamportService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "아임포트 통합인증 컨트롤러", description = "아임포트 통합인증 컨트롤러")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/certification")
public class CertificationController {

	private final IamportService iamportService;

	@GetMapping
	public ResponseEntity<String> getCertification(
		@Parameter(description = "아임포트 imp_uid")
		@RequestParam String imp_uid) {
		String accessToken = iamportService.getAccessToken();
		return ResponseEntity.ok(iamportService.getPhoneNumber(imp_uid, accessToken));
	}
}
