package com.bb.stardium.bench.controller;

import com.bb.stardium.bench.domain.Address;
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
    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        address = new Address("서울시", "송파구", "루터회관 앞");
        startTime = LocalDateTime.of(2020, 11, 30, 10, 0);
        endTime = LocalDateTime.of(2020, 11, 30, 13, 0);
        roomRequest = new RoomRequestDto("title", "intro", address, startTime, endTime, 10);
    }

    @DisplayName("방 만들기 성공 테스트")
    @Test
    void createRoomTest() {
        webTestClient.post().uri("/api/rooms")
                .body(Mono.just(roomRequest), RoomRequestDto.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @DisplayName("방 정보 수정 성공 테스트")
    @Test
    void updateRoom() {
        RoomRequestDto updateRequest = new RoomRequestDto("updatedTitle", "updatedIntro", address, startTime, endTime, 5);
        Long roomId = createRoom(roomRequest);

        webTestClient.put().uri("/api/rooms/" + roomId)
                .body(Mono.just(updateRequest), RoomRequestDto.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @DisplayName("방 삭제 성공 테스트")
    @Test
    void deleteRoomTest() {
        Long roomId = createRoom(roomRequest);

        webTestClient.delete().uri("/api/rooms/" + roomId)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @DisplayName("방 조회 성공 테스트")
    @Test
    void readRoomTest() {
        Long roomId = createRoom(roomRequest);

        webTestClient.get().uri("/api/rooms/" + roomId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.playersLimit")
                .isEqualTo(roomRequest.getPlayersLimit())
                .jsonPath("$.address.city")
                .isEqualTo(roomRequest.getAddress().getCity());
    }

    private Long createRoom(RoomRequestDto roomRequest) {
        return webTestClient.post().uri("/api/rooms")
                .body(Mono.just(roomRequest), RoomRequestDto.class)
                .exchange()
                .expectBody(Long.class)
                .returnResult()
                .getResponseBody();
    }
}
