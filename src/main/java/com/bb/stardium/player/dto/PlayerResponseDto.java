package com.bb.stardium.player.dto;

import com.bb.stardium.player.domain.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PlayerResponseDto {
    private String nickname;
    private String email;
    private String statusMessage;

    public PlayerResponseDto(final Player player) {
        this.nickname = player.getNickname();
        this.email = player.getEmail();
        this.statusMessage = player.getStatusMessage();
    }
}
