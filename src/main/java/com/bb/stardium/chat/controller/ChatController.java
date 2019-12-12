package com.bb.stardium.chat.controller;

import com.bb.stardium.chat.domain.ChatMessage;
import com.bb.stardium.chat.dto.ChatMessageRequestDto;
import com.bb.stardium.chat.dto.ChatMessageResponseDto;
import com.bb.stardium.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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

    @ResponseBody
    @GetMapping("/chat/rooms/{roomId}")
    public ResponseEntity<List<ChatMessageResponseDto>> getPreviousChatMessages(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatService.getPreviousChatMessages(roomId));
    }

}
