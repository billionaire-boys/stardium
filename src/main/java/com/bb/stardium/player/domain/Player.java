package com.bb.stardium.player.domain;

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
}
