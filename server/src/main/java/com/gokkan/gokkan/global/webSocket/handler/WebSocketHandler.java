package com.gokkan.gokkan.global.webSocket.handler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

	private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message)
		throws Exception {
		String payload = message.getPayload();
		JSONObject jsonObject = new JSONObject(payload);
		for (WebSocketSession s : sessions) {
			s.sendMessage(new TextMessage("Hi " + jsonObject.getString("user") + "!"));
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
		throws Exception {
		sessions.remove(session);
	}
}
