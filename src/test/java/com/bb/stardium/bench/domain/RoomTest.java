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
class RoomTest {
    @Autowired
    private TestEntityManager tm;

    private Room room;
    private Player player;

    @BeforeEach
    void setUp() {
        player = Player.builder()
                .nickname("노수진")
                .password("비밀번호")
                .email("soojinroh@naver.com")
                .build();

        room = Room.builder()
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
        assertDoesNotThrow(() -> tm.persist(room));
    }

    @Test
    void 방장확인() {
        assertThat(room.isNotMaster(player)).isFalse();

        Player master = Player.builder()
                .email("master@email.com")
                .nickname("nn")
                .password("password")
                .build();

        room = Room.builder()
                .title("타이틀")
                .intro("인트로")
                .playersLimit(3)
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .master(master)
                .build();

        assertThat(room.isNotMaster(master)).isFalse();
        assertThat(room.isNotMaster(player)).isTrue();
    }

    @Test
    void addPlayer() {
        room.addPlayer(player);
        assertThat(room.getPlayers().size()).isEqualTo(1);
        assertThatThrownBy(() -> room.addPlayer(player)).isInstanceOf(PlayerAlreadyExistException.class);
    }

    @Test
    void hasPlayer() {
        room.addPlayer(player);
        assertThat(room.hasPlayer(player)).isTrue();
    }

    @Test
    void removePlayer() {
        room.addPlayer(player);
        room.removePlayer(player);
        assertThat(room.hasPlayer(player)).isFalse();
        assertThat(room.getPlayers().size()).isEqualTo(0);
    }

    @Test
    void isUnexpiredRoom() {
        Room expiredRoom = Room.builder()
                .title("title")
                .intro("intro")
                .startTime(LocalDateTime.now().minusDays(1L))
                .endTime(LocalDateTime.now().minusDays(1L).plusHours(2L))
                .playersLimit(10)
                .master(player)
                .build();

        boolean result = expiredRoom.isUnexpiredRoom();
        assertThat(result).isFalse();
    }

    @Test
    void hasRemainingSeat() {
        room.addPlayer(player);
        assertThat(room.hasRemainingSeat()).isTrue();
    }

    @Test
    void isReady() {
        room.addPlayer(player);
        assertThat(room.isReady()).isFalse();
    }
}