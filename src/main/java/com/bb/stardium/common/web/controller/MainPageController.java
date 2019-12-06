package com.bb.stardium.common.web.controller;

import com.bb.stardium.bench.dto.RoomResponseDto;
import com.bb.stardium.bench.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MainPageController {

    private RoomService roomService;

    public MainPageController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String homepage(Model model, HttpSession httpSession) {
        if (null == httpSession.getAttribute("login")) {
            return "login.html";
        }
        List<RoomResponseDto> allRooms = roomService.findAllRooms();
        model.addAttribute("rooms", allRooms);
        return "main_all_room.html";
    }

    @GetMapping("/myRoom")
    public String myRoomPage() {
        return "main_my_room.html";
    }
}
