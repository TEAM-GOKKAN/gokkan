package com.gokkan.gokkan.domain.member.service;

import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.domain.MemberRefreshToken;
import com.gokkan.gokkan.domain.member.domain.dto.TokenDto;
import com.gokkan.gokkan.domain.member.exception.AuthErrorCode;
import com.gokkan.gokkan.domain.member.exception.MemberErrorCode;
import com.gokkan.gokkan.domain.member.repository.MemberRefreshTokenRepository;
import com.gokkan.gokkan.domain.member.repository.MemberRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import com.gokkan.gokkan.global.security.config.properties.AppProperties;
import com.gokkan.gokkan.global.security.oauth.entity.Role;
import com.gokkan.gokkan.global.security.oauth.token.AuthToken;
import com.gokkan.gokkan.global.security.oauth.token.AuthTokenProvider;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

	private static final long THREE_DAYS_MSEC = 259200000;
	private static final String REFRESH_TOKEN = "refresh_token";
	private final AppProperties appProperties;
	private final AuthTokenProvider tokenProvider;
	private final MemberRefreshTokenRepository memberRefreshTokenRepository;
	private final MemberRepository memberRepository;

	public TokenDto newAccessToken(String refreshToken) {
		log.info("엑세스 토큰 재발급 시작");
		// refresh token 으로 DB 확인
		MemberRefreshToken memberRefreshToken = memberRefreshTokenRepository.findByRefreshToken(
			refreshToken).orElseThrow(() -> new RestApiException(
			AuthErrorCode.REFRESH_TOKEN_NOT_FOUND));

		log.info("리프레시 토큰 유효");
		AuthToken authRefreshToken = tokenProvider.convertAuthToken(
			memberRefreshToken.getRefreshToken());

		Member member = memberRepository.findByUserId(memberRefreshToken.getUserId());
		if (member == null) {
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		String userId = member.getUserId();
		Role role = member.getRole();

		Date now = new Date();
		AuthToken newAccessToken = tokenProvider.createAuthToken(
			userId,
			role.getCode(),
			new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
		);

		long validTime =
			authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

		// refresh 토큰 기간이 3일 이하로 남은 경우, refresh 토큰 갱신
		if (validTime <= THREE_DAYS_MSEC) {
			log.info("리프레시 토큰 재발급 시작");
			// refresh 토큰 설정
			long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

			authRefreshToken = tokenProvider.createAuthToken(
				appProperties.getAuth().getTokenSecret(),
				new Date(now.getTime() + refreshTokenExpiry)
			);

			// DB에 refresh 토큰 업데이트
			memberRefreshToken.setRefreshToken(authRefreshToken.getToken());
			memberRefreshTokenRepository.save(memberRefreshToken);
			log.info("리프레시 토큰 재발급 완료");
		}
		log.info("엑세스 토큰 재발급 완료");
		return TokenDto.builder()
			.accessToken(newAccessToken.getToken())
			.refreshToken(authRefreshToken.getToken())
			.build();
	}
}
