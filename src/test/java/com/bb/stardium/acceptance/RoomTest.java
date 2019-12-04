package com.bb.stardium.acceptance;

import com.bb.stardium.bench.domain.Address;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.player.dto.PlayerRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


public class RoomTest extends BaseAcceptanceTest {
    private RoomRequestDto roomRequestDto;

    @BeforeEach
    void setUp() {
        roomRequestDto = new RoomRequestDto("title", "intro",
                new Address("서울시", "송파구", "루터회관"),
                LocalDateTime.now(), LocalDateTime.now().plusHours(1L), 6);
    }

    @Test
    @DisplayName("사용자가 방을 만들고 들어가고 나올 수 있다")
    void joinRoom() {
        PlayerRequestDto dto = new PlayerRequestDto("join", "join@room.com", "A!1bcdefg");

        String roomUri = createRoom();

        newSessionPost(dto, roomUri)
                .body(Mono.just(dto), PlayerRequestDto.class)
                .exchange()
                .expectStatus()
                .isOk();

        loginSessionPost(dto, roomUri + "/quit")
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    @DisplayName("방 주인이 방을 나가면 방이 사라진다")
    void quitRoom() {
        PlayerRequestDto dto = new PlayerRequestDto("test", "master@room.com", "A!1bcdefg");

        String roomUri = newSessionPost(dto, "/rooms/new")
                .body(Mono.just(roomRequestDto), RoomRequestDto.class)
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .returnResult(String.class).getRequestHeaders().getFirst("Location");

        loginSessionPost(dto, roomUri + "/quit")
                .exchange();

        loginSessionPost(dto, roomUri)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    private String createRoom() {
        PlayerRequestDto dto = new PlayerRequestDto("test", "create@room.com", "A!1bcdefg");
        return newSessionPost(dto, "/rooms/new")
                .body(Mono.just(roomRequestDto), RoomRequestDto.class)
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .returnResult(String.class).getRequestHeaders().getFirst("Location");
    }
}
