package com.bb.stardium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;


class MainPageControllerTest extends BaseAcceptanceTest {

    @Test
    @DisplayName("메인 페이지 접속")
    void homepage() throws Exception {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isFound()
                .expectBody(String.class)
                .consumeWith(document("common/main",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("로그인되지 않은 채 마이룸 페이지 접속")
    void myRoomPage() throws Exception {
        webTestClient.get().uri("/my-room")
                .exchange()
                .expectStatus().isFound()
                .expectBody(String.class)
                .consumeWith(document("room/my-room",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}