package com.bb.stardium.player.service;

import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.domain.repository.PlayerRepository;
import com.bb.stardium.player.dto.PlayerRequestDto;
import com.bb.stardium.player.service.exception.AlreadyExistEmailException;
import com.bb.stardium.player.service.exception.AuthenticationFailException;
import com.bb.stardium.player.service.exception.NotExistPlayerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService(final PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional(readOnly = true)
    public Player findByPlayerEmail(final String email) {
        return playerRepository.findByEmail(email).orElseThrow(NotExistPlayerException::new);
    }

    public Player register(final PlayerRequestDto requestDto) {
        final String email = requestDto.getEmail();
        playerRepository
                .findByEmail(email)
                .ifPresent(player -> {
                    throw new AlreadyExistEmailException();
                });
        return playerRepository.save(requestDto.toEntity());
    }

    @Transactional(readOnly = true)
    public Player findByPlayerRequestDto(final PlayerRequestDto requestDto) {
        return findByPlayerEmail(requestDto.getEmail());
    }

    public Player login(final PlayerRequestDto requestDto) {
        final Player player = findByPlayerRequestDto(requestDto);
        if (player.isMatchPassword(requestDto.getPassword())) {
            return player;
        }
        throw new AuthenticationFailException();
    }

    // TODO: 사용자 정보 수정
    // TODO: 사용자 정보 삭제
}
