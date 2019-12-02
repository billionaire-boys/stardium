package com.bb.stardium.bench.controller;

import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.dto.Address;
import com.bb.stardium.bench.dto.RoomRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class RoomApiControllerTest {

    private Address address;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private RoomRequestDto roomRequest;

    @BeforeEach
    void setUp() {
        address = new Address("서울시", "송파구", "루터회관 앞");
        startTime = LocalDateTime.of(2020, 11, 30, 10, 0);
        endTime = LocalDateTime.of(2020, 11, 30, 13, 0);
        roomRequest = new RoomRequestDto("title", "intro", address, startTime, endTime, 10);
    }

    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("방 만들기 성공 테스트")
    @Test
    void createRoomTest() {
        webTestClient.post().uri("/rooms")
                .body(Mono.just(roomRequest), RoomRequestDto.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @DisplayName("방 정보 수정 성공 테스트")
    @Test
    void updateRoom() {
        RoomRequestDto updateRequest = new RoomRequestDto("updatedTitle", "updatedIntro", address, startTime, endTime, 5);
        createRoom(roomRequest);

        webTestClient.put().uri("/rooms/1")
                .body(Mono.just(updateRequest), RoomRequestDto.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @DisplayName("방 삭제 성공 테스트")
    @Test
    void deleteRoomTest() {
        createRoom(roomRequest);

        webTestClient.delete().uri("/rooms/1")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @DisplayName("방 조회 성공 테스트")
    @Test
    void readRoomTest() {
        createRoom(roomRequest);

        webTestClient.get().uri("/rooms/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.playersLimit")
                .isEqualTo(roomRequest.getPlayersLimit())
                .jsonPath("$.address.city")
                .isEqualTo(roomRequest.getAddress().getCity());
    }

    private Room toRoomEntity(RoomRequestDto roomRequest) {
        return Room.builder()
                .id(1L)
                .title(roomRequest.getTitle())
                .intro(roomRequest.getIntro())
                .address(roomRequest.getAddress())
                .startTime(roomRequest.getStartTime())
                .endTime(roomRequest.getEndTime())
                .playersLimit(roomRequest.getPlayersLimit())
                .build();
    }

    private void createRoom(RoomRequestDto roomRequest) {
        webTestClient.post().uri("/rooms")
                .body(Mono.just(roomRequest), RoomRequestDto.class)
                .exchange();
    }
}
