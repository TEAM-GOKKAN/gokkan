package com.gokkan.gokkan.global.security.oauth.exception;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException authException) throws IOException {

		authException.printStackTrace();
		log.info("Responding with unauthorized error. Message := {}",
			authException.getMessage());
		response.sendError(
			HttpServletResponse.SC_UNAUTHORIZED,
			authException.getLocalizedMessage()
		);

	}
}

