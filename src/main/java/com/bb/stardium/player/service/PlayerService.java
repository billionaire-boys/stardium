package com.bb.stardium.player.service;

import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.domain.repository.PlayerRepository;
import com.bb.stardium.player.dto.PlayerRequestDto;
import com.bb.stardium.player.dto.PlayerResponseDto;
import com.bb.stardium.player.service.exception.AuthenticationFailException;
import com.bb.stardium.player.service.exception.EmailAlreadyExistException;
import com.bb.stardium.player.service.exception.EmailNotExistException;
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
        return playerRepository.findByEmail(email).orElseThrow(EmailNotExistException::new);
    }

    public Player register(final PlayerRequestDto requestDto) {
        final String email = requestDto.getEmail();
        playerRepository
                .findByEmail(email)
                .ifPresent(player -> {
                    throw new EmailAlreadyExistException();
                });
        return playerRepository.save(requestDto.toEntity());
    }

    @Transactional(readOnly = true)
    public Player findByPlayerRequestDto(final PlayerRequestDto requestDto) {
        return findByPlayerEmail(requestDto.getEmail());
    }

    public PlayerResponseDto login(final PlayerRequestDto requestDto) {
        final Player player = findByPlayerRequestDto(requestDto);
        if (player.isMatchPassword(requestDto.getPassword())) {
            return new PlayerResponseDto(player);
        }
        throw new AuthenticationFailException();
    }

    // TODO: 사용자 정보 수정
    // TODO: 사용자 정보 삭제
}
