package com.bb.stardium.bench.web.controller;

import com.bb.stardium.bench.domain.Room2;
import com.bb.stardium.bench.dto.RoomResponseDto;
import com.bb.stardium.bench.service.RoomService;
import com.bb.stardium.common.web.argumentresolver.annotation.LoggedInPlayer;
import com.bb.stardium.player.domain.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public String mainRoomList(Model model) {
        List<RoomResponseDto> rooms = roomService.findAllUnexpiredRooms();
        model.addAttribute("rooms", rooms);
        return "main-my-room";
    }

    @GetMapping("/create-room")
    public String createRoom() {
        return "create-room";
    }

    @GetMapping("/update-room/{roomId}")
    public String updateRoom(@PathVariable Long roomId, Model model, @LoggedInPlayer final Player loggedInPlayer) {
        Room2 room = roomService.findRoom(roomId);

        if (room.isNotMaster(loggedInPlayer)) {
            return "redirect:/";
        }

        model.addAttribute("room", room);
        return "update-room";
    }

    @GetMapping("/{roomId}")
    public String get(@PathVariable Long roomId, Model model, @LoggedInPlayer final Player loggedInPlayer) {
        Room2 room = roomService.findRoom(roomId);

        if (!room.hasPlayer(loggedInPlayer)) {
            return "redirect:/";
        }

        model.addAttribute("room", room);
        return "room";
    }

    @GetMapping("/{roomId}/details")
    public String getDetail(@PathVariable Long roomId, Model model) {
        Room2 room = roomService.findRoom(roomId);
        model.addAttribute("room", room);
        return "room-detail";
    }

}
