package com.bb.stardium.chat.controller;

import com.bb.stardium.chat.domain.ChatMessage;
import com.bb.stardium.chat.dto.ChatMessageRequestDto;
import com.bb.stardium.chat.dto.ChatMessageResponseDto;
import com.bb.stardium.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/subscribe/chat/{roomId}")
    public ChatMessageResponseDto message(final ChatMessageRequestDto requestDto) {
        final ChatMessage message = chatService.saveMessage(requestDto);
        return new ChatMessageResponseDto(message);
    }
}
