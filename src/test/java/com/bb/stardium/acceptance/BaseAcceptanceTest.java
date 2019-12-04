package com.bb.stardium.acceptance;

import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.dto.PlayerRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public abstract class BaseAcceptanceTest {
    @Autowired
    protected WebTestClient webTestClient;

    private Player player;
    private String sessionId;

    protected BaseAcceptanceTest() {
        player = createPlayer();
        sessionId = getCookie();
    }


    private Player createPlayer() {
        PlayerRequestDto playerRequestDto = new PlayerRequestDto("test", "test@test.com", "1234");
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(Mono.just(playerRequestDto), PlayerRequestDto.class)
                .exchange();
        return playerRequestDto.toEntity();
    }

    private String getCookie() {
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("email", "test@test.com")
                        .with("password", "A!1bcdefg"))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }

    protected WebTestClient.RequestBodySpec post(String uri) {
        String cookie = getCookie();
        return webTestClient.post()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    protected WebTestClient.RequestBodySpec put(String uri) {
        String cookie = getCookie();
        return webTestClient.put()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    protected WebTestClient.RequestHeadersSpec get(String uri) {
        String cookie = getCookie();
        return webTestClient.get().uri(uri).header("Cookie", cookie);
    }

    protected WebTestClient.RequestHeadersSpec delete(String uri) {
        String cookie = getCookie();
        return webTestClient.delete().uri(uri).header("Cookie", cookie);
    }

    protected BodyInserters.FormInserter<String> params(List<String> keyNames, String... parameters) {
        BodyInserters.FormInserter<String> body = BodyInserters.fromFormData("", "");
        for (int i = 0; i < keyNames.size(); i++) {
            body.with(keyNames.get(i), parameters[i]);
        }
        return body;
    }

    protected Player getPlayer() {
        return this.player;
    }
}
