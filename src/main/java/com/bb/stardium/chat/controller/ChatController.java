package com.bb.stardium.chat.controller;

import com.bb.stardium.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

    @MessageMapping("/chat/{roomId}")
    @SendTo("/subscribe/chat/{roomId}")
    public ChatMessage message(final ChatMessage message) {
        return message;
    }
}
