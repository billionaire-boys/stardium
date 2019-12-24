import com.bb.stardium.player.dto.PlayerRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InterceptorTest extends BaseAcceptanceTest {

    private PlayerRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = PlayerRequestDto.builder()
                .nickname("nickname")
                .email("email@email.com")
                .password("password")
                .statusMessage("status message")
                .build();
        requestDto.setConfirmPassword("password");
    }

    @DisplayName("로그인 한 유저가 접근 가능한 페이지 접근 성공")
    @Test
    void loggedInUserPageAccessSuccess() {
        newSessionGet(requestDto, "/rooms")
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName("로그인 한 유저가 접근 불가능한 페이지 접근 실패")
    @Test
    void loggedInUserPageAccessFail() {
        newSessionGet(requestDto, "/login")
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @DisplayName("로그인 안 한 유저가 접근 가능한 페이지 접근 성공")
    @Test
    void notLoggedInUserPageAccessSuccess() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName("로그인 안 한 유저가 접근 불가능한 페이지 접근 실패")
    @Test
    void notLoggedInUserPageAccessFail() {
        webTestClient.get().uri("/rooms")
                .exchange()
                .expectStatus().is3xxRedirection();
    }

}
