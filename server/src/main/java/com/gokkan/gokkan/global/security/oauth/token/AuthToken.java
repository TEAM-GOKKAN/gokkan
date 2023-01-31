package com.gokkan.gokkan.global.security.oauth.token;

import com.gokkan.gokkan.global.exception.exception.RestApiException;
import com.gokkan.gokkan.global.security.oauth.exception.SecurityErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.security.Key;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthToken {

	private static final String AUTHORITIES_KEY = "role";
	@Getter
	private final String token;
	private final Key key;

	AuthToken(String id, Date expiry, Key key) {
		this.key = key;
		this.token = createAuthToken(id, expiry);
	}

	AuthToken(String id, String role, Date expiry, Key key) {
		this.key = key;
		this.token = createAuthToken(id, role, expiry);
	}

	private String createAuthToken(String id, Date expiry) {
		return Jwts.builder()
			.setSubject(id)
			.signWith(key, SignatureAlgorithm.HS256)
			.setExpiration(expiry)
			.compact();
	}

	private String createAuthToken(String id, String role, Date expiry) {
		return Jwts.builder()
			.setSubject(id)
			.claim(AUTHORITIES_KEY, role)
			.signWith(key, SignatureAlgorithm.HS256)
			.setExpiration(expiry)
			.compact();
	}

	public boolean validate() {
		return this.getTokenClaims() != null;
	}

	public Claims getTokenClaims() {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (SecurityException e) {
			log.error("Invalid JWT signature.");
			throw new RestApiException(SecurityErrorCode.INVALID_JWT_SIGNATURE);
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token.");
			throw new RestApiException(SecurityErrorCode.INVALID_JWT_TOKEN);
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token.");
			throw new RestApiException(SecurityErrorCode.EXPIRED_JWT_TOKEN);
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token.");
			throw new RestApiException(SecurityErrorCode.UNSUPPORTED_JWT_TOKEN);
		} catch (IllegalArgumentException e) {
			log.error("JWT token compact of handler are invalid.");
			//throw new RestApiException(SecurityErrorCode.ILLEGAL_ARGUMENT_JWT_TOKEN);
		}
		return null;
	}

	public Claims getExpiredTokenClaims() {
		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token.");
			return e.getClaims();
		}
		return null;
	}
}
