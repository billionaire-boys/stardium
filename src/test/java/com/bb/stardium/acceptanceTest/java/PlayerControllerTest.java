import com.bb.stardium.player.dto.PlayerRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

class PlayerControllerTest extends BaseAcceptanceTest {

    @Test
    @DisplayName("회원가입 페이지 접속")
    void signUpPage() {
        webTestClient.get().uri("/players/new")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .consumeWith(document("user/player-new",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
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
                .expectStatus().isFound()
                .expectBody(String.class)
                .consumeWith(document("user/player-post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("로그인 상태에서 회원정보 수정 페이지 접속")
    void userEditPageLoggedIn() {
        PlayerRequestDto player = new PlayerRequestDto("nick", "email@mail.com", "password", "password", "");
        loginSessionGet(player, "/players/edit")
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody(String.class)
                .consumeWith(document("user/player-edit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("로그인하지 않은 상태에서 회원정보 수정 페이지 접속")
    void userEditPageNotLoggedIn() {
        webTestClient.get().uri("/players/edit")
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*\\/login.*")
                .expectBody(String.class)
                .consumeWith(document("user/player-edit-no-session",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("로그인한 상태에서 회원 정보 수정")
    void userEdit() {
        PlayerRequestDto player = new PlayerRequestDto("nick", "email@email.com",
                "password", "password", "");

        loginSessionPost(player, "/players/edit")
                .body(BodyInserters
                        .fromMultipartData("profile", "profile.png".getBytes())
                        .with("email", "update@email")
                        .with("password", "password")
                        .with("statusMessage", "야호!!"))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectBody(String.class)
                .consumeWith(document("user/player-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("로그인하지 않은 상태에서 회원 정보 수정")
    void userEditNotLoggedIn() {
        webTestClient.post().uri("/players/edit")
                .body(BodyInserters
                        .fromFormData("nickname", "noname01")
                        .with("email", "email")
                        .with("password", "1q2w3e4r!")
                        .with("statusMessage", "야호!!"))
                .exchange().expectStatus().is3xxRedirection()
                .expectBody(String.class)
                .consumeWith(document("user/player-update-no-session",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}