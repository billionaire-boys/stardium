package com.bb.stardium.player.dto;

import com.bb.stardium.player.domain.Player2;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Player2ResponseDto {
    private String nickname;
    private String email;
    private String statusMessage;

    public Player2ResponseDto(final Player2 player) {
        this.nickname = player.getNickname();
        this.email = player.getEmail();
        this.statusMessage = player.getStatusMessage();
    }
}
