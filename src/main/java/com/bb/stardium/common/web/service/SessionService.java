package com.bb.stardium.common.web.service;

import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.dto.PlayerResponseDto;
import com.bb.stardium.player.service.PlayerService;
import com.bb.stardium.player.service.exception.EmailNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class SessionService {

    private final PlayerService playerService;

    public boolean isLoggedIn(final HttpSession session) {
        final PlayerResponseDto responseDto = (PlayerResponseDto) session.getAttribute("login");
        if (Objects.isNull(responseDto)) {
            return false;
        }
        return comparePlayerByDto(responseDto);
    }

    private boolean comparePlayerByDto(final PlayerResponseDto responseDto) {
        try {
            final Player player = playerService.findByPlayerEmail(responseDto.getEmail());
            return player.isSamePlayer(responseDto.getPlayerId(), responseDto.getEmail());
        } catch (final EmailNotExistException exception) {
            return false;
        }
    }

}
