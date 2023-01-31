package com.gokkan.gokkan.global.webSocket.interceptor;

import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Slf4j
@Component
public class StompHandshakeInterceptor implements HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
		WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
		log.info("stomp handshake start");
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest
				= (ServletServerHttpRequest) request;
			HttpSession session = servletRequest
				.getServletRequest().getSession();
			attributes.put("sessionId", session.getId());
		}
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
		WebSocketHandler wsHandler, Exception exception) {
		log.info("stomp hadnshake success!");
	}
}
