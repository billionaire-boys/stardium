package com.bb.stardium.bench.web.controller;

import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.dto.RoomResponseDto;
import com.bb.stardium.bench.service.RoomService;
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
    public String updateRoom(@PathVariable Long roomId, Model model) {
        Room room = roomService.findRoom(roomId);
        model.addAttribute("room", room);
        return "update-room";
    }

    @GetMapping("/{roomId}")
    public String get(@PathVariable Long roomId, Model model) {
        Room room = roomService.findRoom(roomId);
        model.addAttribute("room", room);
        return "room";
    }

}
