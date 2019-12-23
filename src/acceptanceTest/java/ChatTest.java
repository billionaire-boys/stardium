import com.bb.stardium.bench.domain.Address;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.chat.dto.ChatMessageRequestDto;
import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.dto.PlayerRequestDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.bb.stardium.StardiumApplication.class)
@AutoConfigureWebTestClient
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ChatTest extends BaseAcceptanceTest {

    private static final int TIME_LIMIT = 5;
    private static final String SOCKET_SEND_URL = "/chat/";
    private static final String SOCKET_SUBSCRIBE_URL = "/subscribe/chat/";

    @LocalServerPort
    int port;

    private WebSocketStompClient webSocketStompClient;
    private String socketConnectUrl;
    private CompletableFuture<TestChatResponseDto> completableFuture;
    private Long roomId;

    @BeforeEach
    void setup() {
        setSocketConfig();

        roomId = createRoom();

        completableFuture = new CompletableFuture<>();
    }

    @Test
    @DisplayName("소켓에 메세지 수신되는지 확인")
    public void 소켓에_메시지_수신() throws ExecutionException, InterruptedException, TimeoutException {
        StompSession stompSession =
                webSocketStompClient.connect(socketConnectUrl,
                        new StompSessionHandlerAdapter() {
                        }).get(TIME_LIMIT, TimeUnit.SECONDS);

        stompSession.subscribe(SOCKET_SUBSCRIBE_URL + roomId, getStompFrameHandler());

        ChatMessageRequestDto chatMessageRequestDto =
                new ChatMessageRequestDto(roomId, 1L, "contents");

        stompSession.send(SOCKET_SEND_URL + roomId, chatMessageRequestDto);

        TestChatResponseDto testChatResponseDto
                = completableFuture.get(TIME_LIMIT, SECONDS);

        assertThat(testChatResponseDto.getContents()).isEqualTo(chatMessageRequestDto.getContents());

    }

    private void setSocketConfig() {
        socketConnectUrl = "ws://localhost:" + port + "/stomp-connect";

        webSocketStompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))
        ));

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    private long createRoom() {
        Player mockPlayer = Player.builder()
                .nickname("master")
                .nickname("master@mail.com")
                .password("password")
                .build();
        PlayerRequestDto playerRequestDto = new PlayerRequestDto("test", "create@room", "password", "");
        playerRequestDto.setConfirmPassword("password");
        RoomRequestDto roomRequestDto = new RoomRequestDto("title", "intro",
                Address.builder()
                        .city("서울시").section("송파구").detail("루터회관 앞")
                        .build(),
                LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L), 3, mockPlayer);

        return newSessionRoomPost(playerRequestDto, roomRequestDto);
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

    private StompFrameHandler getStompFrameHandler() {
        return new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatTest.TestChatResponseDto.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                completableFuture.complete((TestChatResponseDto) payload);
            }
        };
    }

    static private class TestChatResponseDto {
        private Long roomId;
        private String nickname;

        @JsonProperty("message")
        private String contents;
        private String timestamp;

        public TestChatResponseDto() {
        }

        public String getContents() {
            return contents;
        }
    }
}
