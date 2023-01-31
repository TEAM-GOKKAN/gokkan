package com.gokkan.gokkan.global.webSocket.interceptor;

import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.exception.AuthErrorCode;
import com.gokkan.gokkan.domain.member.repository.MemberRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import com.gokkan.gokkan.global.security.oauth.token.AuthToken;
import com.gokkan.gokkan.global.security.oauth.token.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompChannelInterceptor implements ChannelInterceptor {

	private static final String BEARER_PREFIX = "Bearer ";
	private final AuthTokenProvider tokenProvider;
	private final MemberRepository memberRepository;

	private Member member;

	public Member getMember() {
		return this.member;
	}

	private void setMember(Member tokenMember) {
		this.member = tokenMember;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
		if (headerAccessor.getCommand() != StompCommand.SEND) {
			return message;
		}

		String authorizationHeader = String.valueOf(
			headerAccessor.getNativeHeader("Authorization"));
		if (authorizationHeader == null || authorizationHeader.equals("null")) {
			throw new RestApiException(AuthErrorCode.AUTHORIZATION_HEADER_NOT_FOUND);
		}
		String tokenStr = authorizationHeader.substring(BEARER_PREFIX.length());
		AuthToken token = tokenProvider.convertAuthToken(tokenStr);
		if (token.validate()) {
			log.info("토큰 유효 멤버 조회");
			Member member = memberRepository.findByUserId(token.getTokenClaims().getSubject());
			if (member == null) {
				throw new RestApiException(AuthErrorCode.TOKEN_EXPIRED);
			}
			setMember(member);
		} else {
			throw new RestApiException(AuthErrorCode.ACCESS_TOKEN_INVALID);
		}

		return message;
	}
}
