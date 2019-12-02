package com.bb.stardium.player.dto;

public class PlayerRequestDto {
    private String nickname;
    private String email;
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "PlayerRequestDto {" +
                "nickname: \"" + nickname + "\"" +
                ", email: \"" + email + "\"" +
                ", password: \"" + password + "\"" +
                "}";
    }
}
