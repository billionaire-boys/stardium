package com.bb.stardium;

import com.bb.stardium.bench.dto.Address;
import com.bb.stardium.bench.dto.RoomRequestDto;
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

    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("방 만들기 성공 테스트")
    @Test
    void createRoom() {
        Address address = new Address("서울시", "송파구", "루터회관 앞");
        LocalDateTime startTime = LocalDateTime.of(2020, 11, 30, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2020, 11, 30, 13, 0);
        RoomRequestDto roomRequest = new RoomRequestDto("title", "intro", address, startTime, endTime, 10);

        webTestClient.post().uri("/rooms")
                .body(Mono.just(roomRequest), RoomRequestDto.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}