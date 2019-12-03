package com.bb.stardium.player.controller;

import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.repository.PlayerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayerControllerTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    @DisplayName("회원 가입")
    void register() {
        client.post()
                .uri("/players")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("nickname", "noname01")
                        .with("email", "asdf@mail.net")
                        .with("password", "1q2w3e4r!"))
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    @DisplayName("로그인")
    void login() {
        playerRepository.save(new Player("nickname", "nick@name.com", "1q2w3e4r!"));

        client.post()
                .uri("/players/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "nick@name.com")
                        .with("password", "1q2w3e4r!"))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    @DisplayName("로그아웃")
    void logout() {
        client.get()
                .uri("/players/logout")
                .exchange()
                .expectStatus().is3xxRedirection();
    }
}