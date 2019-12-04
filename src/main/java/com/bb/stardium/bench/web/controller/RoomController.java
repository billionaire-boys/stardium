package com.bb.stardium.bench.web.controller;

import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.dto.RoomResponseDto;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.bench.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public String mainRoomList(Model model) {
        List<RoomResponseDto> rooms = roomService.findAllRooms();
        model.addAttribute("rooms", rooms);
        return "mainRooms";
    }

    @GetMapping("/createForm")
    public String createFrom() {
        return "createRoom";
    }

    @GetMapping("/updateForm")
    public String updateForm() {
        return "updateRoom";
    }

    @PostMapping()
    public String create(@RequestBody RoomRequestDto roomRequest) {
        Long roomId = roomService.create(roomRequest);
        return "redirect:/rooms/" + roomId;
    }

    @GetMapping("/{roomId}")
    public String get(@PathVariable Long roomId, Model model) {
        Room room = roomService.findRoom(roomId);
        model.addAttribute("room", room);
        return "room";
    }

    @PutMapping("/{roomId}")
    public String update(@PathVariable Long roomId, @RequestBody RoomRequestDto roomRequestDto) {
        Long updatedRoomId = roomService.update(roomId, roomRequestDto);
        return "redirect:/rooms/" + updatedRoomId;
    }

    @DeleteMapping("/{roomId}")
    public String delete(@PathVariable Long roomId) {
        roomService.delete(roomId);
        return "redirect:/main";
    }

}
