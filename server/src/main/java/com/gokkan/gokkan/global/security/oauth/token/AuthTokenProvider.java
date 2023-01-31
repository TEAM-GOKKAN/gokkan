package com.gokkan.gokkan.global.security.oauth.token;

import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.domain.MemberAdapter;
import com.gokkan.gokkan.global.security.oauth.exception.TokenValidFailedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Slf4j
public class AuthTokenProvider {

	private static final String AUTHORITIES_KEY = "role";
	private final Key key;

	public AuthTokenProvider(String secret) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public AuthToken createAuthToken(String id, Date expiry) {
		return new AuthToken(id, expiry, key);
	}

	public AuthToken createAuthToken(String id, String role, Date expiry) {
		return new AuthToken(id, role, expiry, key);
	}

	public AuthToken convertAuthToken(String token) {
		return new AuthToken(token, key);
	}

	public Authentication getAuthentication(AuthToken authToken, Member member) {

		if (authToken.validate()) {
			Claims claims = authToken.getTokenClaims();
			Collection<? extends GrantedAuthority> authorities =
				Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());

			MemberAdapter memberAdapter = new MemberAdapter(member);

			return new UsernamePasswordAuthenticationToken(memberAdapter, authToken, authorities);
		} else {
			throw new TokenValidFailedException();
		}
	}
}

