package com.bb.stardium.bench.web.controller;

import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.bench.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/createForm")
    public String createFrom() {
        return "createRoom";
    }

    @PostMapping()
    public String crate(@RequestBody RoomRequestDto roomRequest) {
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
