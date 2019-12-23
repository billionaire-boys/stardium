package com.bb.stardium.player.service;

import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.domain.repository.PlayerRepository;
import com.bb.stardium.player.dto.PlayerRequestDto;
import com.bb.stardium.player.dto.PlayerResponseDto;
import com.bb.stardium.player.service.exception.AuthenticationFailException;
import com.bb.stardium.player.service.exception.EmailAlreadyExistException;
import com.bb.stardium.player.service.exception.EmailNotExistException;
import com.bb.stardium.player.service.exception.NicknameAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Transactional(readOnly = true)
    public Player findByPlayerEmail(final String email) {
        return playerRepository.findByEmail(email).orElseThrow(EmailNotExistException::new);
    }

    @Transactional(readOnly = true)
    public Player findByResponseDto(final PlayerResponseDto responseDto) {
        return findByPlayerEmail(responseDto.getEmail());
    }

    public Player register(final PlayerRequestDto requestDto) {
        if (playerRepository.existsByEmail(requestDto.getEmail())) {
            throw new EmailAlreadyExistException();
        }
        if (playerRepository.existsByNickname(requestDto.getNickname())) {
            throw new NicknameAlreadyExistException();
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

    public String findNicknameByPlayerId(final long playerId) {
        return playerRepository
                .findById(playerId)
                .map(Player::getNickname)
                .orElseThrow(EmailNotExistException::new);
    }
}
