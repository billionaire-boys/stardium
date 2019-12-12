package com.bb.stardium.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Getter
@ToString
@AllArgsConstructor
public class ChatMessage {
    private String roomId;
    private String sender;
    private String message;
    private OffsetDateTime dateTime;
}
