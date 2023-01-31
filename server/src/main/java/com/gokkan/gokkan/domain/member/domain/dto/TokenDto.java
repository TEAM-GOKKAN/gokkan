package com.gokkan.gokkan.domain.member.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "토큰 정보")
public class TokenDto {

	private final String accessToken;
	private final String refreshToken;

	@Builder
	public TokenDto(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
