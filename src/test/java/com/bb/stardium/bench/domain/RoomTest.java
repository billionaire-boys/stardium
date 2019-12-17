package com.bb.stardium.bench.domain;

import com.bb.stardium.player.domain.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoomTest {
    private static final Logger log = LoggerFactory.getLogger(RoomTest.class);

    private LocalDateTime startTime = LocalDateTime.now().plusDays(1);
    private LocalDateTime endTime = LocalDateTime.now().plusDays(1).plusHours(1);
    private Address address = Address.builder()
            .city("서울시").section("송파구")
            .detail("루터회관 앞")
            .build();
    private Player player = Player.builder()
            .id(1L)
            .nickname("nick")
            .email("email@email.com")
            .password("password")
            .rooms(new ArrayList<>())
            .build();
    private Player master = Player.builder()
            .id(2L)
            .nickname("master")
            .email("master@email.com")
            .password("password")
            .rooms(new ArrayList<>())
            .build();
    private Room room = Room.builder()
            .id(1L)
            .title("title")
            .intro("intro")
            .address(address)
            .startTime(startTime)
            .endTime(endTime)
            .playersLimit(2)
            .master(master)
            .players(new ArrayList<>())
            .build();

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

    @Test
    @DisplayName("방의 마스터가 아닌 경우 확인")
    void isNotMaster() {
        boolean result = room.isNotMaster(player);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("방의 마스터가 아닌 경우 확인")
    void isMaster() {
        boolean result = room.isNotMaster(master);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("게임 시간이 현재 시간 이전인 경우")
    void isUnexpiredRoomTest() {
        Room expiredRoom = Room.builder()
                .id(1L)
                .title("title")
                .intro("intro")
                .address(address)
                .startTime(LocalDateTime.now().minusDays(1L))
                .endTime(LocalDateTime.now().minusDays(1L).plusHours(2L))
                .playersLimit(10)
                .master(master)
                .players(new ArrayList<>())
                .build();

        boolean result = expiredRoom.isUnexpiredRoom();
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("방에 남은 자리가 있는지 확인")
    void hasRemainingSeatTest() {
        room.addPlayer(player);
        assertThat(room.hasRemainingSeat()).isTrue();
    }

    @Test
    @DisplayName("방이 레디 상태인지 확인")
    void roomIsReady() {
        room.addPlayer(player);
        room.addPlayer(master);
        assertThat(room.isReady()).isTrue();
    }

}