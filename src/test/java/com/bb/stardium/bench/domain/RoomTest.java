package com.bb.stardium.bench.domain;

import com.bb.stardium.player.domain.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoomTest {

    @Autowired
    private TestEntityManager tm;

    private LocalDateTime startTime = LocalDateTime.now().plusDays(1);
    private LocalDateTime endTime = LocalDateTime.now().plusDays(1).plusHours(1);
    private Address address = new Address("서울시", "송파구", "루터회관 앞");
    private Player player = Player.builder()
            .nickname("nick")
            .email("email@email.com")
            .password("password")
            .rooms(new ArrayList<>())
            .build();
    private Room room = new Room(1L, "title", "intro", address, startTime, endTime, 10, player, new ArrayList<>());

    @Test
    @DisplayName("방 입장")
    void roomEnter() {
        room.addPlayer(player);
        assertThat(room.hasPlayer(player)).isTrue();
    }

    @Test
    @DisplayName("방 퇴장")
    void roomQuit() {
        room.removePlayer(player);
        assertThat(room.hasPlayer(player)).isFalse();
    }

}