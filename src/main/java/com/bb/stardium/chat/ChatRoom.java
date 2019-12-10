package com.bb.stardium.chat;

import com.bb.stardium.chat.dto.ChatMessageDto;
import com.bb.stardium.chat.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoom {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(final String roomId, final String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handleActions(final WebSocketSession session, final ChatMessageDto message, final ChatService chatService) {
        if (message.getMessageType() == ChatMessageDto.MessageType.ENTER) {
            sessions.add(session);
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        sendMessage(message, chatService);
    }

    public <T> void sendMessage(final T message, final ChatService chatService) {
        this.sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
    }
}
