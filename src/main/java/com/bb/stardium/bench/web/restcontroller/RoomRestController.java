package com.bb.stardium.bench.web.restcontroller;

import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.bench.service.RoomService;
import com.bb.stardium.bench.service.exception.FixedReadyRoomException;
import com.bb.stardium.common.web.argumentresolver.annotation.LoggedInPlayer;
import com.bb.stardium.player.domain.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rooms")
public class RoomRestController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity create(@RequestBody final RoomRequestDto roomRequest, @LoggedInPlayer final Player loggedInPlayer) {
        Long roomId = roomService.create(roomRequest, loggedInPlayer);
        return ResponseEntity.ok(roomId);
    }

    @PostMapping("/join/{roomId}")
    public ResponseEntity join(@PathVariable Long roomId, @LoggedInPlayer final Player loggedInPlayer) {
        final Room joinRoom = roomService.join(loggedInPlayer, roomId);
        return ResponseEntity.ok(joinRoom.getId());
    }

    @PostMapping("/quit/{roomId}")
    public ResponseEntity quit(@PathVariable Long roomId, @LoggedInPlayer final Player loggedInPlayer) {
        roomService.quit(roomId, loggedInPlayer);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{roomId}")
    public ResponseEntity update(@PathVariable Long roomId,
                                 @RequestBody RoomRequestDto roomRequestDto,
                                 @LoggedInPlayer final Player loggedInPlayer) {
        Long updatedRoomId = roomService.update(roomId, roomRequestDto, loggedInPlayer);
        return ResponseEntity.ok(updatedRoomId);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity delete(@PathVariable Long roomId, @LoggedInPlayer final Player loggedInPlayer) {
        roomService.delete(roomId, loggedInPlayer);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(FixedReadyRoomException.class)
    public ResponseEntity badRequest() {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
