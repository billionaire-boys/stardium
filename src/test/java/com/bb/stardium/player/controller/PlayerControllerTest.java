package com.bb.stardium.player.controller;

import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.domain.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
        playerRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 페이지 접속")
    void signUpPage() {
        client.get().uri("/player/new").exchange().expectStatus().isOk();
    }

    @Test
    @DisplayName("회원정보 수정 페이지 접속")
    void userEditPage() {
        client.get().uri("/player/edit").exchange().expectStatus().isOk();
    }

    @Test
    @DisplayName("회원가입")
    void register() {
        client.post()
                .uri("/player")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("nickname", "noname01")
                        .with("email", "asdf@mail.net")
                        .with("password", "1q2w3e4r!"))
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    @DisplayName("로그인")
    void login() {
        playerRepository.save(new Player("nickname", "nick@name.com", "1q2w3e4r!"));

        client.post()
                .uri("/player/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "nick@name.com")
                        .with("password", "1q2w3e4r!"))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void loginFailure() {
        playerRepository.save(new Player("nickname", "nick@name.com", "1q2w3e4r!"));

        client.post()
                .uri("/player/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "nick@name.com")
                        .with("password", "wrong!"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*\\/player\\/login.*");
    }

    @Test
    @DisplayName("로그아웃")
    void logout() {
        client.get()
                .uri("/player/logout")
                .exchange()
                .expectStatus().is3xxRedirection();
    }
}