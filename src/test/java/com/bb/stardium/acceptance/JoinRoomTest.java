package com.bb.stardium.acceptance;

import com.bb.stardium.player.domain.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public class JoinRoomTest extends BaseAcceptanceTest {
    @Test
    @DisplayName("만들어진 방에 사용자가 들어갈 수 있다")
    void joinRoom() {
        post("/rooms/1/join")
                .body(Mono.just(getPlayer()), Player.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}
