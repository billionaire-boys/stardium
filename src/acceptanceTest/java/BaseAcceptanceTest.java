import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.dto.PlayerRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;


@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.bb.stardium.StardiumApplication.class)
@ExtendWith(RestDocumentationExtension.class)
public abstract class BaseAcceptanceTest {

    protected WebTestClient webTestClient;
    @LocalServerPort
    private int port;

    protected BaseAcceptanceTest() {
    }

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .filter(documentationConfiguration(provider))
                .build();
    }

    private Player createPlayer(PlayerRequestDto playerRequestDto) {
        webTestClient.post().uri("/players/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("nickname", playerRequestDto.getNickname())
                        .with("email", playerRequestDto.getEmail())
                        .with("password", playerRequestDto.getPassword())
                        .with("confirmPassword", playerRequestDto.getConfirmPassword())
                        .with("status", playerRequestDto.getStatusMessage()))
                .exchange();
        return playerRequestDto.toEntity();
    }

    protected WebTestClient.RequestBodySpec newSessionPost(PlayerRequestDto dto, String uri) {
        String cookie = getNewCookie(dto);
        return webTestClient.post()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    protected WebTestClient.RequestBodySpec newSessionPut(PlayerRequestDto dto, String uri) {
        String cookie = getNewCookie(dto);
        return webTestClient.put()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    protected WebTestClient.RequestHeadersSpec newSessionGet(PlayerRequestDto dto, String uri) {
        String cookie = getNewCookie(dto);
        return webTestClient.get().uri(uri).header("Cookie", cookie);
    }

    protected WebTestClient.RequestHeadersSpec newSessionDelete(PlayerRequestDto dto, String uri) {
        String cookie = getNewCookie(dto);
        return webTestClient.delete().uri(uri).header("Cookie", cookie);
    }

    protected WebTestClient.RequestBodySpec loginSessionPost(PlayerRequestDto dto, String uri) {
        String cookie = getLoginCookie(dto);
        return webTestClient.post()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    protected WebTestClient.RequestBodySpec loginSessionPut(PlayerRequestDto dto, String uri) {
        String cookie = getLoginCookie(dto);
        return webTestClient.put()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    protected WebTestClient.RequestHeadersSpec loginSessionGet(PlayerRequestDto dto, String uri) {
        String cookie = getLoginCookie(dto);
        return webTestClient.get().uri(uri).header("Cookie", cookie);
    }

    protected WebTestClient.RequestHeadersSpec loginSessionDelete(PlayerRequestDto dto, String uri) {
        String cookie = getLoginCookie(dto);
        return webTestClient.delete().uri(uri).header("Cookie", cookie);
    }

    protected BodyInserters.FormInserter<String> params(List<String> keyNames, String... parameters) {
        BodyInserters.FormInserter<String> body = BodyInserters.fromFormData("", "");
        for (int i = 0; i < keyNames.size(); i++) {
            body.with(keyNames.get(i), parameters[i]);
        }
        return body;
    }

    private String getLoginCookie(PlayerRequestDto playerRequestDto) {
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("email", playerRequestDto.getEmail())
                        .with("password", playerRequestDto.getPassword()))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }

    private String getNewCookie(PlayerRequestDto playerRequestDto) {
        createPlayer(playerRequestDto);
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("email", playerRequestDto.getEmail())
                        .with("password", playerRequestDto.getPassword()))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }
}
