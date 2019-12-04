package com.bb.stardium.common.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MainPageControllerTest {

    @Autowired
    private WebTestClient client;

    @Test
    @DisplayName("메인 페이지 접속")
    void homepage() {
        client.get().uri("/").exchange().expectStatus().isOk();
    }
}