package com.bb.stardium;

import com.bb.stardium.bench.domain.Address;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.dto.PlayerRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Disabled
class RoomTest extends BaseAcceptanceTest {
    private RoomRequestDto roomRequestDto;
    private Player masterPlayer;

    @BeforeEach
    void setUp() {
        masterPlayer = Player.builder()
                .nickname("master")
                .nickname("master@mail.com")
                .password("password")
                .build();

        roomRequestDto = new RoomRequestDto("title", "intro",
                Address.builder()
                        .city("서울시").section("송파구").detail("루터회관 앞")
                        .build(),
                LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L), 3, masterPlayer);
    }

    @Test
    @DisplayName("사용자가 방을 만들고 들어가고 나올 수 있다")
    void joinRoom() {
        PlayerRequestDto master = new PlayerRequestDto("test", "create@room", "password", "");
        Long roomNumber = newSessionRoomPost(master, roomRequestDto);


        PlayerRequestDto joinPlayer = new PlayerRequestDto("join", "join@room.com", "A!1bcdefg", "dd");

        newSessionRoomJoinPost(roomNumber, joinPlayer)
                .expectStatus()
                .isOk();

        loginSessionRoomQuitPost(roomNumber, joinPlayer)
                .expectStatus()
                .isOk();
    }

    @Test
    @DisplayName("사용자가 방 들어간 후 방이 ready가 되면(인원이 꽉 차면) 방장을 제외한 그 누구도 나갈 수 없다")
    void 방의_상태가_레디가_됨() {
        RoomRequestDto roomWith2Players = new RoomRequestDto("title", "intro",
                Address.builder()
                        .city("서울시").section("송파구").detail("루터회관 앞")
                        .build(),
                LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L), 2, masterPlayer);

        PlayerRequestDto master = new PlayerRequestDto("test", "create@room", "password", "");
        Long roomNumber = newSessionRoomPost(master, roomWith2Players);

        PlayerRequestDto joinPlayer = new PlayerRequestDto("join", "join@room.com", "A!1bcdefg", "dd");

        newSessionRoomJoinPost(roomNumber, joinPlayer)
                .expectStatus()
                .isOk();

        loginSessionRoomQuitPost(roomNumber, joinPlayer)
                .expectStatus()
                .isBadRequest();
    }

    @Test
    @DisplayName("방 주인이 방을 나가면 방이 사라진다")
    void quitRoom() {
        PlayerRequestDto dto = new PlayerRequestDto("test", "master@room.com", "A!1bcdefg", "Dd");

        String roomUri = newSessionPost(dto, "/rooms")
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

    @Test
    @DisplayName("방 주인만이 방 정보를 수정할 수 있다")
    void updateRoom() {
        PlayerRequestDto masterPlayer = new PlayerRequestDto("test", "master@room.com", "A!1bcdefg", "Dd");

        String roomUri = newSessionPost(masterPlayer, "/rooms")
                .body(Mono.just(roomRequestDto), RoomRequestDto.class)
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .returnResult(String.class).getRequestHeaders().getFirst("Location");

        PlayerRequestDto joinPlayer = new PlayerRequestDto("join", "join@room.com", "A!1bcdefg", "Dd");
        newSessionPost(joinPlayer, roomUri)
                .body(Mono.just(joinPlayer), PlayerRequestDto.class)
                .exchange()
                .expectStatus()
                .isOk();

        loginSessionPut(masterPlayer, roomUri)
                .body(Mono.just(roomRequestDto), RoomRequestDto.class)
                .exchange()
                .expectStatus()
                .is3xxRedirection();

        loginSessionPut(joinPlayer, roomUri)
                .body(Mono.just(roomRequestDto), RoomRequestDto.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    private Long newSessionRoomPost(PlayerRequestDto playerRequestDto, RoomRequestDto roomRequestDto) {
        return newSessionPost(playerRequestDto, "/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(roomRequestDto), RoomRequestDto.class)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Long.class)
                .returnResult()
                .getResponseBody();
    }

    private WebTestClient.ResponseSpec newSessionRoomJoinPost(Long roomNumber, PlayerRequestDto joinPlayer) {
        return newSessionPost(joinPlayer, "rooms/join/" + roomNumber)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(joinPlayer), PlayerRequestDto.class)
                .exchange();
    }

    private WebTestClient.ResponseSpec loginSessionRoomQuitPost(Long roomNumber, PlayerRequestDto joinPlayer) {
        return loginSessionPost(joinPlayer, "rooms/quit/" + roomNumber)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();
    }
}
