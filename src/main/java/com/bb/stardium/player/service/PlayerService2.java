package com.bb.stardium.player.service;

import com.bb.stardium.player.domain.Player2;
import com.bb.stardium.player.domain.repository.Player2Repository;
import com.bb.stardium.player.dto.Player2RequestDto;
import com.bb.stardium.player.dto.Player2ResponseDto;
import com.bb.stardium.player.service.exception.AuthenticationFailException;
import com.bb.stardium.player.service.exception.EmailNotExistException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlayerService2 {
    private final Player2Repository playerRepository;

    public PlayerService2(final Player2Repository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional(readOnly = true)
    public Player2 findByPlayerEmail(final String email) {
        return playerRepository.findByEmail(email).orElseThrow(EmailNotExistException::new);
    }

    public Player2 register(final Player2RequestDto requestDto) {
        if (playerRepository.existsByEmail(requestDto.getEmail())) {
            throw new EmailNotExistException();
        }
        return playerRepository.save(requestDto.toEntity());
    }

    public Player2ResponseDto login(final Player2RequestDto requestDto) {
        final Player2 player = findByPlayerEmail(requestDto.getEmail());
        if (player.isMatchPassword(requestDto.getPassword())) {
            return new Player2ResponseDto(player);
        }
        throw new AuthenticationFailException();
    }

    public Player2ResponseDto update(final Player2RequestDto requestDto, final Player2ResponseDto sessionDto) {
        final Player2 player = findByPlayerEmail(requestDto.getEmail());
        if (!player.getEmail().equals(sessionDto.getEmail())) {
            throw new AuthenticationFailException();
        }
        player.update(requestDto.toEntity());
        return new Player2ResponseDto(player);
    }
}
