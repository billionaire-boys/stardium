package com.bb.stardium.player.domain;

import java.util.Objects;

public class Player {
    private String nickname;
    private String email;
    private String password;

    public Player(final String nickname, final String email, final String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Player update(final Player newPlayer) {
        this.nickname = newPlayer.nickname;
        this.email = newPlayer.email;
        this.password = newPlayer.password;
        return this;
    }

    @Override
    public boolean equals(final Object another) {
        if (this == another) return true;
        if (another == null || getClass() != another.getClass()) return false;
        final Player player = (Player) another;
        return Objects.equals(email, player.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "Player {" +
                "nickname: \"" + nickname + "\"" +
                ", email: \"" + email + "\"" +
                "}";
    }
}
