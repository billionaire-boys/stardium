package com.bb.stardium.bench.domain;

import com.bb.stardium.player.domain.Player;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class RoomTest {

    private Player player = new Player("nick", "email", "pass");

    private LocalDateTime endTime = LocalDateTime.of(2020, 11, 30, 13, 0);

    private LocalDateTime startTime = LocalDateTime.of(2020, 11, 30, 10, 0);

    private Address address = Address.builder()
            .city("서울시").section("송파구")
            .detail("루터회관 앞")
            .build();

    Room room = new Room(1L, "title", "intro", address, startTime, endTime, 10, player, new ArrayList<>());

    @Test
    void 방_입장() {

        room.addPlayer(player);
        assertThat(room.hasPlayer(player)).isTrue();

    }

    @Test
    void 방_퇴장() {

        room.removePlayer(player);
        assertThat(room.hasPlayer(player)).isFalse();

    }

}