package com.bb.stardium.chat.handler;

import com.bb.stardium.chat.ChatRoom;
import com.bb.stardium.chat.dto.ChatMessageDto;
import com.bb.stardium.chat.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import static org.slf4j.LoggerFactory.getLogger;

@Component
@RequiredArgsConstructor
public class WebSockChatHandler extends TextWebSocketHandler {
    private static final Logger LOG = getLogger(WebSockChatHandler.class);
    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage message) throws Exception {
        final String payload = message.getPayload();
        LOG.info("payload: {}", payload);

        ChatMessageDto chatMessage = objectMapper.readValue(payload, ChatMessageDto.class);
        ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
        room.handleActions(session, chatMessage, chatService);
    }
}
