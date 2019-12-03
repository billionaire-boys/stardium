package com.bb.stardium.player.dto;

import com.bb.stardium.player.domain.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PlayerRequestDto {
    private String nickname;
    private String email;
    private String password;

    public PlayerRequestDto(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public Player toEntity() {
        return new Player(nickname, email, password);
    }
}
