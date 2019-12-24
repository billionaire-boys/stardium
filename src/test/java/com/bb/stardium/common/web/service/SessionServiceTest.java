package com.bb.stardium.common.web.service;

import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.dto.PlayerResponseDto;
import com.bb.stardium.player.service.PlayerService;
import com.bb.stardium.player.service.exception.EmailNotExistException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SessionServiceTest {
    private static final Player PLAYER = Player.builder()
            .id(1L).email("session@Session.com")
            .password("password").nickname("nickname")
            .statusMessage("status Message")
            .build();
    private static final Player WRONG_PLAYER = Player.builder()
            .id(1L).email("wrongsession@Session.com")
            .password("password").nickname("nickname")
            .statusMessage("status Message")
            .build();
    private static final PlayerResponseDto RESPONSE_PLAYER_DTO = new PlayerResponseDto(PLAYER);

    private MockHttpSession session;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private SessionService sessionService;

    @Test
    @DisplayName("로그인이 되어있는지 확인")
    void isLoginTest() {
        given(playerService.findByPlayerEmail(RESPONSE_PLAYER_DTO.getEmail())).willReturn(PLAYER);

        session = new MockHttpSession();
        session.setAttribute("login", RESPONSE_PLAYER_DTO);
        assertThat(sessionService.isLoggedIn(session)).isTrue();
    }

    @Test
    @DisplayName("로그인이 안되어있는지 확인")
    void isNotLoginTest() {
        session = new MockHttpSession();
        assertThat(sessionService.isLoggedIn(session)).isFalse();
    }

    @Test
    @DisplayName("로그인한 사람의 이메일이 같은지 확인하는 테스트")
    void isSameLoginTest() {
        given(playerService.findByPlayerEmail(RESPONSE_PLAYER_DTO.getEmail())).willReturn(WRONG_PLAYER);

        session = new MockHttpSession();
        session.setAttribute("login", RESPONSE_PLAYER_DTO);
        assertThat(sessionService.isLoggedIn(session)).isFalse();
    }

    @Test
    @DisplayName("로그인한 사람의 이메일이 없을경우 예외처리 테스트")
    void isSameLoginExceptionTest() {
        given(playerService.findByPlayerEmail(RESPONSE_PLAYER_DTO.getEmail())).willThrow(EmailNotExistException.class);

        session = new MockHttpSession();
        session.setAttribute("login", RESPONSE_PLAYER_DTO);
        assertThat(sessionService.isLoggedIn(session)).isFalse();
    }

}