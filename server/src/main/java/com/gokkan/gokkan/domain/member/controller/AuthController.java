package com.gokkan.gokkan.domain.member.controller;

import com.gokkan.gokkan.domain.member.domain.dto.TokenDto;
import com.gokkan.gokkan.domain.member.service.AuthService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "토큰 컨트롤러", description = "토큰 컨트롤러")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;


	@GetMapping("/refresh")
	@ApiResponse(description = "토큰 정보", content = @Content(schema = @Schema(implementation = TokenDto.class)))
	public ResponseEntity<TokenDto> newAccessToken(
		@Parameter(description = "리프레시 토큰")
		@RequestParam String refreshToken) {

		return ResponseEntity.ok(authService.newAccessToken(refreshToken));
	}
}

