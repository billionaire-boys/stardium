package com.bb.stardium.player.service;

import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.domain.repository.PlayerRepository;
import com.bb.stardium.player.dto.PlayerRequestDto;
import com.bb.stardium.player.dto.PlayerResponseDto;
import com.bb.stardium.player.service.exception.NotExistPlayerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class PlayerServiceTest {
    @Mock
    PlayerRepository playerRepository;
    @InjectMocks
    PlayerService playerService;
    private Player player;
    private PlayerRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = new PlayerRequestDto("nickname", "email", "password");
        player = requestDto.toEntity();
    }

    @Test
    @DisplayName("회원 가입")
    void register() {
        given(playerRepository.findByEmail(anyString())).willReturn(Optional.empty());
        given(playerRepository.save(player)).willReturn(player);

        Player savedPlayer = playerService.register(requestDto);

        verify(playerRepository, times(1)).save(savedPlayer);
    }

    @Test
    @DisplayName("로그인 성공")
    void login() {
        given(playerRepository.findByEmail("email")).willReturn(Optional.of(player));

        PlayerResponseDto responseDto = playerService.login(requestDto);

        verify(playerRepository).findByEmail("email");
    }

    @Test
    @DisplayName("없는 이메일로 로그인 시도")
    void loginFailureByEmail() {
        given(playerRepository.findByEmail(anyString())).willReturn(Optional.empty());

        assertThatThrownBy(() -> playerService.login(requestDto))
                .isInstanceOf(NotExistPlayerException.class);
    }
}