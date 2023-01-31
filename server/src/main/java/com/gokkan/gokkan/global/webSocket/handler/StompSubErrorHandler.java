package com.gokkan.gokkan.global.webSocket.handler;

import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.nio.charset.StandardCharsets;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Component
public class StompSubErrorHandler extends StompSubProtocolErrorHandler {

	@Override
	public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage,
		Throwable ex) {

		if (ex.getCause() instanceof RestApiException) {
			RestApiException exception = (RestApiException) ex.getCause();
			String message = exception.getErrorCode().getMessage();
			StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

			accessor.setMessage(String.valueOf(exception.getErrorCode().getHttpStatus().value()));
			accessor.setLeaveMutable(true);

			return MessageBuilder.createMessage(message.getBytes(StandardCharsets.UTF_8),
				accessor.getMessageHeaders());
		}

		return super.handleClientMessageProcessingError(clientMessage, ex);

	}

}
