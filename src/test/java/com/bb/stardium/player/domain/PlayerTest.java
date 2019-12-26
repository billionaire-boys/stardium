package com.bb.stardium.player.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTest {
    private final String nickname = "noname";
    private final String email = "asdf@mail.com";
    private final String password = "weak password";

    @Test
    @DisplayName("새로운 플레이어 객체를 생성")
    void makeNewPlayer() {
        final Player player = new Player(nickname, email, password);
        assertThat(player.getNickname()).isEqualTo(nickname);
        assertThat(player.getEmail()).isEqualTo(email);
        assertThat(player.getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("기존 객체의 정보를 새로운 객체로 갱신")
    void update() {
        final String newNickname = "nickname";
        final Player originalPlayer = new Player(nickname, email, password);
        final Player newPlayer = new Player(newNickname, email, password);
        originalPlayer.update(newPlayer);
        assertThat(originalPlayer).isEqualTo(newPlayer);
    }
}