import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.domain.repository.PlayerRepository;
import com.bb.stardium.player.dto.PlayerRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerControllerTest extends BaseAcceptanceTest {

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
        webTestClient.get().uri("/players/new")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @DisplayName("회원가입")
    void register() {
        webTestClient.post().uri("/players/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("nickname", "newplayer")
                        .with("email", "newplayer")
                        .with("password", "1q2w3e4r"))
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    @DisplayName("로그인 상태에서 회원정보 수정 페이지 접속")
    void userEditPageLoggedIn() {
        PlayerRequestDto player = new PlayerRequestDto("nick", "email@mail.com", "password", "");
        loginSessionGet(player, "/players/edit")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @DisplayName("로그인하지 않은 상태에서 회원정보 수정 페이지 접속")
    void userEditPageNotLoggedIn() {
        webTestClient.get().uri("/players/edit")
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*\\/login.*");
    }

    @Test
    @DisplayName("로그인한 상태에서 회원 정보 수정")
    void userEdit() {
        PlayerRequestDto player = new PlayerRequestDto("" +
                "nick", "email@email.com", "password", "");
        loginSessionPost(player, "/players/edit")
                .body(BodyInserters
                        .fromFormData("nickname", "update")
                        .with("email", "update@email")
                        .with("password", "password")
                        .with("statusMessage", "야호!!"))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
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