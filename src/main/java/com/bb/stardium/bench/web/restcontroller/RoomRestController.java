package com.bb.stardium.bench.web.restcontroller;


import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.bench.service.RoomService;
import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.dto.PlayerResponseDto;
import com.bb.stardium.player.service.PlayerService;
import com.bb.stardium.player.service.exception.AuthenticationFailException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rooms")
public class RoomRestController {

    private final PlayerService playerService;
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity create(@RequestBody final RoomRequestDto roomRequest, final HttpSession session) {
        PlayerResponseDto loginPlayerDto = (PlayerResponseDto) session.getAttribute("login");
        Player loginPlayer = playerService.findByPlayerEmail(loginPlayerDto.getEmail());
        Long roomId = roomService.create(roomRequest, loginPlayer);
        return ResponseEntity.ok(roomId);
    }

    @PostMapping("/join/{roomId}")
    public ResponseEntity join(@PathVariable Long roomId, final HttpSession session) {
        PlayerResponseDto playerResponseDto = (PlayerResponseDto) session.getAttribute("login");
        if (playerResponseDto == null) {
            throw new AuthenticationFailException();
        }

        roomService.join(playerResponseDto.getEmail(), roomId);
        return ResponseEntity.ok(roomId);
    }

    @PostMapping("/quit/{roomId}")
    public ResponseEntity quit(@PathVariable Long roomId, final HttpSession session) {
        PlayerResponseDto playerResponseDto = (PlayerResponseDto) session.getAttribute("login");
        if (playerResponseDto == null) {
            throw new AuthenticationFailException();
        }

        roomService.quit(playerResponseDto.getEmail(), roomId);
        return ResponseEntity.ok(roomId);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity update(@PathVariable Long roomId,
                                 @RequestBody RoomRequestDto roomRequestDto,
                                 HttpSession httpSession) {
        PlayerResponseDto loginPlayerDto = (PlayerResponseDto) httpSession.getAttribute("login");
        Player player = playerService.findByPlayerEmail(loginPlayerDto.getEmail());
        Long updatedRoomId = roomService.update(roomId, roomRequestDto, player);
        return ResponseEntity.ok(updatedRoomId);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity delete(@PathVariable Long roomId, HttpSession httpSession) {
        PlayerResponseDto loginPlayer = (PlayerResponseDto) httpSession.getAttribute("login");
        roomService.delete(roomId, loginPlayer.getEmail());
        return ResponseEntity.ok().build();
    }
}
