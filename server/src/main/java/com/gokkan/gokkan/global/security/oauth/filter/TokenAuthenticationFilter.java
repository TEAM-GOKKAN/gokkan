package com.gokkan.gokkan.global.security.oauth.filter;

import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.repository.MemberRepository;
import com.gokkan.gokkan.global.security.oauth.token.AuthToken;
import com.gokkan.gokkan.global.security.oauth.token.AuthTokenProvider;
import com.gokkan.gokkan.infra.utils.HeaderUtil;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private final AuthTokenProvider tokenProvider;
	private final MemberRepository memberRepository;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String tokenStr = HeaderUtil.getAccessToken(request);
		AuthToken token = tokenProvider.convertAuthToken(tokenStr);
		if (token.validate()) {
			log.info("토큰 유효 멤버 조회");
			Member member = memberRepository.findByUserId(token.getTokenClaims().getSubject());
			Authentication authentication = tokenProvider.getAuthentication(token, member);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}
}

