package com.bb.stardium.bench.controller;

import com.bb.stardium.bench.dto.Address;
import com.bb.stardium.bench.dto.RoomRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class RoomControllerTest {

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
                .is3xxRedirection();
    }

    @DisplayName("방 정보 수정 성공 테스트")
    @Test
    void updateRoom() {
        RoomRequestDto updateRequest = new RoomRequestDto("updatedTitle", "updatedIntro", address, startTime, endTime, 5);
        Long roomId = createRoom(roomRequest);

        webTestClient.put().uri("/rooms/" + roomId)
                .body(Mono.just(updateRequest), RoomRequestDto.class)
                .exchange()
                .expectStatus()
                .is3xxRedirection();
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

    // TODO : 리팩토링 필요
    private Long createRoom(RoomRequestDto roomRequest) {
        List<Long> roomIdList = new ArrayList<>();

        webTestClient.post().uri("/rooms")
                .body(Mono.just(roomRequest), RoomRequestDto.class)
                .exchange()
                .expectBody()
                .consumeWith(response -> {
                    String url = Objects.requireNonNull(response.getResponseHeaders().get("Location")).get(0);
                    String roomId = url.substring(url.lastIndexOf("/") + 1);
                    Long roomIdLong = Long.valueOf(roomId);
                    roomIdList.add(roomIdLong);
                });

        return roomIdList.get(0);
    }
}
