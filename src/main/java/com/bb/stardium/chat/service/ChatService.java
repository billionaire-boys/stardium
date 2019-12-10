package com.bb.stardium.chat.service;

import com.bb.stardium.chat.ChatRoom;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

@RequiredArgsConstructor
@Service
public class ChatService {
    private static final Logger LOG = getLogger(ChatService.class);

    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRooms() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(final String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoom createRoom(final String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }

    public <T> void sendMessage(final WebSocketSession session, final T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}

