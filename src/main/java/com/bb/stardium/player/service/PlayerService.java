package com.bb.stardium.player.service;

import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.domain.repository.PlayerRepository;
import com.bb.stardium.player.dto.PlayerRequestDto;
import com.bb.stardium.player.dto.PlayerResponseDto;
import com.bb.stardium.player.service.exception.AuthenticationFailException;
import com.bb.stardium.player.service.exception.EmailAlreadyExistException;
import com.bb.stardium.player.service.exception.EmailNotExistException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class PlayerService {
    private final PlayerRepository playerRepository;

    @Transactional(readOnly = true)
    public Player findByPlayerEmail(final String email) {
        return playerRepository.findByEmail(email).orElseThrow(EmailNotExistException::new);
    }

    public Player register(final PlayerRequestDto requestDto) {
        if (playerRepository.existsByEmail(requestDto.getEmail())) {
            throw new EmailAlreadyExistException();
        }
        return playerRepository.save(requestDto.toEntity());
    }

    public PlayerResponseDto login(final PlayerRequestDto requestDto) {
        final Player player = findByPlayerEmail(requestDto.getEmail());
        if (player.isMatchPassword(requestDto.getPassword())) {
            return new PlayerResponseDto(player);
        }
        throw new AuthenticationFailException();
    }

    public PlayerResponseDto update(final PlayerRequestDto requestDto, final PlayerResponseDto sessionDto) {
        final Player player = findByPlayerEmail(requestDto.getEmail());
        if (!player.getEmail().equals(sessionDto.getEmail())) {
            throw new AuthenticationFailException();
        }
        player.update(requestDto.toEntity());
        return new PlayerResponseDto(player);
    }
}
