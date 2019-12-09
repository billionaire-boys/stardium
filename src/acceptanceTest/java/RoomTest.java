import com.bb.stardium.bench.domain.Address;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.player.domain.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Disabled
class RoomTest extends BaseAcceptanceTest {
    private RoomRequestDto roomRequestDto;
    private Player masterPlayer1;

    @BeforeEach
    void setUp() {
        masterPlayer1 = new Player("master1", "master1@mail.net", "password");
        roomRequestDto = new RoomRequestDto("title", "intro",
                new Address("서울시", "송파구", "루터회관"),
                LocalDateTime.now(), LocalDateTime.now().plusHours(1L), 3, masterPlayer1);
    }

    @Test
    @DisplayName("사용자가 방을 만들고 들어가고 나올 수 있다")
    void joinRoom() {
        PlayerRequestDto createPlayer = new PlayerRequestDto("test", "create@room.com", "A!1bcdefg", "");
        Long roomNumber = newSessionPost(createPlayer, "/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(roomRequestDto), RoomRequestDto.class)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Long.class)
                .returnResult()
                .getResponseBody();


        PlayerRequestDto joinPlayer = new PlayerRequestDto("join", "join@room.com", "A!1bcdefg", "dd");
        newSessionPost(joinPlayer, "rooms/join/" + roomNumber)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(joinPlayer), PlayerRequestDto.class)
                .exchange()
                .expectStatus()
                .isOk();

        loginSessionPost(joinPlayer, "rooms/quit/" + roomNumber)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @DisplayName("방 주인이 방을 나가면 방이 사라진다")
    void quitRoom() {
        PlayerRequestDto dto = new PlayerRequestDto("test", "master@room.com", "A!1bcdefg", "Dd");

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

    @Test
    @DisplayName("방 주인만이 방 정보를 수정할 수 있다")
    void updateRoom() {
        PlayerRequestDto masterPlayer = new PlayerRequestDto("test", "master@room.com", "A!1bcdefg", "Dd");

        String roomUri = newSessionPost(masterPlayer, "/rooms/new")
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
}
