package com.bb.stardium.acceptance;

import com.bb.stardium.bench.domain.Address;
import com.bb.stardium.bench.dto.RoomRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


public class CreateRoomTest extends BaseAcceptanceTest {
    private RoomRequestDto roomRequestDto;

    @BeforeEach
    void setUp() {
        roomRequestDto = new RoomRequestDto("title", "intro",
                new Address("서울시", "송파구", "루터회관"),
                LocalDateTime.now(), LocalDateTime.now().plusHours(1L), 6);
    }

    @Test
    @DisplayName("사용자가 방을 만들 수 있다.")
    void createRoom() {
        post("/rooms/new")
                .body(Mono.just(roomRequestDto), RoomRequestDto.class)
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }
}
