package com.bb.stardium.chat.dto;

import com.bb.stardium.chat.domain.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessageResponseDto {
    private String roomId;
    private String sender;
    private String message;
    private OffsetDateTime dateTime;

    public ChatMessageResponseDto(final ChatMessage message) {
        this.roomId = message.getRoomId();
        this.sender = message.getSender();
        this.message = message.getMessage();
        this.dateTime = message.getDateTime();
    }
}
