package com.bb.stardium.bench.controller;

import com.bb.stardium.bench.domain.Address;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.player.domain.repository.PlayerRepository;
import com.bb.stardium.player.dto.PlayerRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class RoomControllerTest {

    private Address address;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private RoomRequestDto roomRequest;
    private String loginCookie;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        address = new Address("서울시", "송파구", "루터회관 앞");
        startTime = LocalDateTime.of(2020, 11, 30, 10, 0);
        endTime = LocalDateTime.of(2020, 11, 30, 13, 0);
        roomRequest = new RoomRequestDto("title", "intro", address, startTime, endTime, 10);
        signUp();
        loginCookie = getLoginCookie("master1@master.net", "master1");
    }

    @DisplayName("방 만들기 성공 테스트")
    @Test
    void createRoomTest() {
        webTestClient.post().uri("/rooms")
                .header("Cookie", loginCookie)
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

        webTestClient.put().uri("/rooms/" + roomId)
                .header("Cookie", loginCookie)
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
                .header("Cookie", loginCookie)
                .body(Mono.just(roomRequest), RoomRequestDto.class)
                .exchange()
                .expectBody(Long.class)
                .returnResult()
                .getResponseBody();
    }

    private void signUp() {
        webTestClient.post().uri("/player/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("nickname", "master1")
                        .with("email", "master1@master.net")
                        .with("password", "master1"))
                .exchange();
    }

    private String getLoginCookie(String email, String password) {
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }


}
