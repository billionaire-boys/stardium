package com.bb.stardium.player.dto;

import com.bb.stardium.player.domain.Player;

public class PlayerResponseDto {
    private String nickname;
    private String email;

    public PlayerResponseDto(final String nickname, final String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public PlayerResponseDto(final Player player) {
        this(player.getNickname(), player.getEmail());
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(final String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "PlayerResponseDto {" +
                "nickname: \"" + nickname + "\"" +
                ", email: \"" + email + "\"" +
                "}";
    }
}
