package com.bb.stardium.bench.controller;

import com.bb.stardium.bench.domain.Address;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.bench.service.RoomService;
import com.bb.stardium.player.domain.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomControllerTest {

    private Player masterPlayer1;
    private Address address;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private RoomRequestDto roomRequest;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        masterPlayer1 = new Player("master1", "master1@mail.net", "password");
        address = new Address("서울시", "송파구", "루터회관 앞");
        startTime = LocalDateTime.of(2020, 11, 30, 10, 0);
        endTime = LocalDateTime.of(2020, 11, 30, 13, 0);
        roomRequest = new RoomRequestDto("title", "intro", address, startTime, endTime, 10, masterPlayer1);
    }

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
        RoomRequestDto updateRequest = new RoomRequestDto("updatedTitle", "updatedIntro", address, startTime, endTime, 5, masterPlayer1);
        Long roomId = createRoom(roomRequest);

        webTestClient.put().uri("/rooms/" + roomId)
                .body(Mono.just(updateRequest), RoomRequestDto.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @DisplayName("방 삭제 성공 테스트")
    @Test
    void deleteRoomTest() {
        Long roomId = createRoom(roomRequest);

        webTestClient.delete().uri("/rooms/" + roomId)
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @DisplayName("방 조회 성공 테스트")
    @Test
    void readRoomTest() {
        Long roomId = createRoom(roomRequest);

        webTestClient.get().uri("/rooms/" + roomId)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @DisplayName("전체 방 조회 성공 테스트")
    @Test
    public void findAllRooms() throws Exception {
        createRoom(roomRequest);

        webTestClient.get().uri("/rooms")
                .exchange()
                .expectStatus()
                .isOk();
    }


    // TODO : 리팩토링 필요
    private Long createRoom(RoomRequestDto roomRequest) {

        return webTestClient.post().uri("/rooms")
                .body(Mono.just(roomRequest), RoomRequestDto.class)
                .exchange()
                .expectBody(Long.class)
                .returnResult()
                .getResponseBody();

    }
}
