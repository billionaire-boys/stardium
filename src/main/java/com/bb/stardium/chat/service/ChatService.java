package com.bb.stardium.chat.service;

import com.bb.stardium.chat.domain.ChatMessage;
import com.bb.stardium.chat.dto.ChatMessageRequestDto;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class ChatService {

    public ChatMessage saveMessage(final ChatMessageRequestDto requestDto) {
        final ChatMessage message = new ChatMessage(
                requestDto.getRoomId(),
                requestDto.getSender(),
                requestDto.getMessage(),
                OffsetDateTime.now());
        // TODO: 데이터 sanitize
        // TODO: 여기서 실제 저장
        return message;
    }
}
