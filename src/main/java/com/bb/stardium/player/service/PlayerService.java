package com.bb.stardium.player.service;

import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.dto.PlayerRequestDto;
import com.bb.stardium.player.exception.AlreadyExistEmailException;
import com.bb.stardium.player.exception.AuthenticationFailException;
import com.bb.stardium.player.exception.NotExistPlayerException;
import com.bb.stardium.player.repository.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService(final PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player register(final PlayerRequestDto requestDto) {
        final String email = requestDto.getEmail();
        playerRepository
                .findByEmail(email)
                .ifPresent(player -> { throw new AlreadyExistEmailException(); });
        return playerRepository.save(new Player(requestDto));
    }

    public Player findByPlayerEmail(final String email) {
        return playerRepository.findByEmail(email).orElseThrow(NotExistPlayerException::new);
    }

    public Player findByPlayerRequestDto(final PlayerRequestDto requestDto) {
        return findByPlayerEmail(requestDto.getEmail());
    }

    public Player login(final PlayerRequestDto requestDto) {
        final Player player = findByPlayerRequestDto(requestDto);
        if (player.isMatchPassword(requestDto)) {
            return player;
        }
        throw new AuthenticationFailException();
    }

    // TODO: 사용자 정보 수정
    // TODO: 사용자 정보 삭제
}
