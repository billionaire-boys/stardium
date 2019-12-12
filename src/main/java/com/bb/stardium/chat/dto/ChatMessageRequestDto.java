package com.bb.stardium.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDto {
    private Long roomId;
    private Long playerId;
    private String contents;
}
