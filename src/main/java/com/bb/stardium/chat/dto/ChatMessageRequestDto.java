package com.bb.stardium.chat.dto;

import com.bb.stardium.chat.domain.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDto {
    private Long roomId;
    private Long playerId;
    private String contents;

    public ChatMessage toEntity(String nickname, OffsetDateTime now) {
        return ChatMessage.builder()
                .roomId(roomId)
                .playerId(playerId)
                .playerNickname(nickname)
                .contents(contents)
                .timestamp(now)
                .build();
    }
}
