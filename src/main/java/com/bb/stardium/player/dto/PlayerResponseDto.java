package com.bb.stardium.player.dto;

import com.bb.stardium.player.domain.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class PlayerResponseDto {
    private String nickname;
    private String email;

    public PlayerResponseDto(final Player player) {
        this(player.getNickname(), player.getEmail());
    }
}
