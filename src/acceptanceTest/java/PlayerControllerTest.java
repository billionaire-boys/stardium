import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.domain.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Disabled
class PlayerControllerTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private PlayerRepository playerRepository;

    @BeforeEach
    void setUp() {
        Player player = Player.builder()
                .nickname("nickname")
                .email("email@email.com")
                .password("password")
                .build();
        playerRepository.deleteAll();
        playerRepository.save(player);
    }

    @Test
    @DisplayName("회원가입 페이지 접속")
    void signUpPage() {
        client.get().uri("/players/new").exchange().expectStatus().isOk();
    }

    @Test
    @DisplayName("회원가입")
    void register() {
        client.post().uri("/players/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("nickname", "noname01")
                        .with("email", "asdf@mail.net")
                        .with("password", "1q2w3e4r!"))
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    @DisplayName("로그인 상태에서 회원정보 수정 페이지 접속")
    void userEditPageLoggedIn() {
        final String cookie = getLoginCookie();

        client.get().uri("/players/edit")
                .header("Cookie", cookie)
                .exchange().expectStatus().isOk();
    }

    private String getLoginCookie() {
        return client.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "email")
                        .with("password", "password"))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }

    @Test
    @DisplayName("로그인하지 않은 상태에서 회원정보 수정 페이지 접속")
    void userEditPageNotLoggedIn() {
        client.get()
                .uri("/players/edit")
                .exchange().expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*\\/login.*");
    }

    @Test
    @DisplayName("로그인한 상태에서 회원 정보 수정")
    void userEdit() {
        final String cookie = getLoginCookie();

        client.post().uri("/players/edit")
                .header("Cookie", cookie)
                .body(BodyInserters
                        .fromFormData("nickname", "noname01")
                        .with("email", "email")
                        .with("password", "1q2w3e4r!")
                        .with("statusMessage", "야호!!"))
                .exchange().expectStatus().is3xxRedirection();

        final Player player = playerRepository.findByEmail("email")
                .orElseThrow(() -> new IllegalArgumentException());
        assertThat(player.getNickname()).isEqualTo("noname01");
        assertThat(player.getEmail()).isEqualTo("email");
        assertThat(player.getPassword()).isEqualTo("1q2w3e4r!");
        assertThat(player.getStatusMessage()).isEqualTo("야호!!");
    }

    @Test
    @DisplayName("로그인하지 않은 상태에서 회원 정보 수정")
    void userEditNotLoggedIn() {
        client.post().uri("/players/edit")
                .body(BodyInserters
                        .fromFormData("nickname", "noname01")
                        .with("email", "email")
                        .with("password", "1q2w3e4r!")
                        .with("statusMessage", "야호!!"))
                .exchange().expectStatus().is3xxRedirection();

        final Player player = playerRepository.findByEmail("email")
                .orElseThrow(() -> new IllegalArgumentException());
        assertThat(player.getNickname()).isEqualTo("nickname");
        assertThat(player.getEmail()).isEqualTo("email");
        assertThat(player.getPassword()).isEqualTo("password");
        assertThat(player.getStatusMessage()).isEqualTo("");
    }
}