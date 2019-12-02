package com.bb.stardium.player.service;

import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.dto.PlayerRequestDto;
import com.bb.stardium.player.exception.NotExistPlayerException;
import com.bb.stardium.player.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class PlayerServiceTest {
    private Player player;
    private PlayerRequestDto requestDto;

    @Mock
    PlayerRepository playerRepository;

    @InjectMocks
    PlayerService playerService;

    @BeforeEach
    void setUp() {
        requestDto = new PlayerRequestDto("nickname", "email", "password");
        player = new Player(requestDto);
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

        Player logindPlayer = playerService.login(requestDto);
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