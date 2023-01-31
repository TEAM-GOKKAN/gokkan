package com.gokkan.gokkan.global.security.oauth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;


public class JwtExceptionFilter extends OncePerRequestFilter {

	/*
	인증 오류가 아닌, JWT 관련 오류는 이 필터에서 따로 잡아낸다.
	이를 통해 JWT 만료 에러와 인증 에러를 따로 잡아낼 수 있다.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain chain) throws ServletException, IOException {
		try {
			chain.doFilter(request, response); // JwtAuthenticationFilter로 이동
		} catch (RestApiException ex) {
			// JwtAuthenticationFilter에서 예외 발생하면 바로 setErrorResponse 호출
			setErrorResponse(request, response, ex);
		}
	}

	public void setErrorResponse(HttpServletRequest req, HttpServletResponse res,
		RestApiException ex) throws IOException {

		res.setContentType(MediaType.APPLICATION_JSON_VALUE);

		final Map<String, Object> body = new HashMap<>();
		body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
		body.put("error", "Unauthorized");
		body.put("message", ex.getErrorCode().getMessage());
		body.put("path", req.getServletPath());
		final ObjectMapper mapper = new ObjectMapper();
//		mapper.writeValue(res.getOutputStream(), body);
//		res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		res.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getErrorCode().getMessage());
	}
}
