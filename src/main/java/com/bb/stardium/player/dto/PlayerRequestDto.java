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
public class PlayerRequestDto {
    private String nickname;
    private String email;
    private String password;

    public Player toEntity() {
        return new Player(nickname, email, password);
    }
}
