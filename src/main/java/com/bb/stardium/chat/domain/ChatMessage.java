package com.bb.stardium.chat.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatMessage {
    private String roomId;
    private String sender;
    private String message;
}
