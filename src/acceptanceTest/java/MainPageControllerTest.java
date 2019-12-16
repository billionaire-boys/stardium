import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class MainPageControllerTest extends BaseAcceptanceTest {

    @Test
    @DisplayName("메인 페이지 접속")
    void homepage() throws Exception {
        webTestClient.get().uri("/")
                .exchange()
                .expectHeader().contentType("text/html;charset=UTF-8")
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("로그인되지 않은 채 마이룸 페이지 접속")
    void myRoomPage() throws Exception {
        webTestClient.get().uri("/my-room")
                .exchange()
                .expectHeader().contentType("text/html;charset=UTF-8")
                .expectStatus().isOk();
    }
}