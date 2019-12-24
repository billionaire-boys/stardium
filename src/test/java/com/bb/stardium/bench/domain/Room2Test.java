package com.bb.stardium.bench.domain;

import com.bb.stardium.bench.domain.exception.PlayerAlreadyExistException;
import com.bb.stardium.player.domain.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
class Room2Test {
    @Autowired
    private TestEntityManager tm;

    private Room2 room2;
    private Player player;

    @BeforeEach
    void setUp() {
        player = Player.builder()
                .nickname("노수진")
                .password("비밀번호")
                .email("soojinroh@naver.com")
                .build();

        room2 = Room2.builder()
                .title("타이틀")
                .intro("인트로")
                .playersLimit(3)
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .master(player)
                .build();
    }

    @Test
    void 생성() {
        assertDoesNotThrow(() -> tm.persist(room2));
    }

    @Test
    void 방장확인() {
        assertThat(room2.isNotMaster(player)).isFalse();

        Player master = Player.builder()
                .email("master@email.com")
                .nickname("nn")
                .password("password")
                .build();

        room2 = Room2.builder()
                .title("타이틀")
                .intro("인트로")
                .playersLimit(3)
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .master(master)
                .build();

        assertThat(room2.isNotMaster(master)).isFalse();
        assertThat(room2.isNotMaster(player)).isTrue();
    }

    @Test
    void addPlayer() {
        room2.addPlayer(player);
        assertThat(room2.getPlayers().size()).isEqualTo(1);
        assertThatThrownBy(() -> room2.addPlayer(player)).isInstanceOf(PlayerAlreadyExistException.class);
    }

    @Test
    void hasPlayer() {
        room2.addPlayer(player);
        assertThat(room2.hasPlayer(player)).isTrue();
    }

    @Test
    void removePlayer() {
        room2.addPlayer(player);
        room2.removePlayer(player);
        assertThat(room2.hasPlayer(player)).isFalse();
        assertThat(room2.getPlayers().size()).isEqualTo(0);
    }

    @Test
    void isUnexpiredRoom() {
    }

    @Test
    void hasRemainingSeat() {
        room2.addPlayer(player);
        assertThat(room2.hasRemainingSeat()).isTrue();
    }

    @Test
    void isReady() {
        room2.addPlayer(player);
        assertThat(room2.isReady()).isFalse();
    }
}