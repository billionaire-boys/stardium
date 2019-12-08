package com.bb.stardium.common.web.controller;

import com.bb.stardium.bench.dto.RoomResponseDto;
import com.bb.stardium.bench.service.RoomService;
import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.dto.PlayerResponseDto;
import com.bb.stardium.player.service.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MainPageController {

    private PlayerService playerService;
    private RoomService roomService;

    public MainPageController(PlayerService playerService, RoomService roomService) {
        this.playerService = playerService;
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String homepage(Model model, HttpSession session) {
        if (null == session.getAttribute("login")) {
            return "login.html";
        }
        List<RoomResponseDto> allRooms = roomService.findAllUnexpiredRooms();
        model.addAttribute("rooms", allRooms);
        return "main_all_room.html";
    }

    @GetMapping("/myRoom")
    public String myRoomPage(Model model, HttpSession session) {
        if (null == session.getAttribute("login")) {
            return "login.html";
        } // TODO: 중복 제거 및 로직을 다른 곳으로 이동
        PlayerResponseDto sessionDto = (PlayerResponseDto) session.getAttribute("login");
        Player player = playerService.findByPlayerEmail(sessionDto.getEmail());
        List<RoomResponseDto> myRooms = roomService.findPlayerJoinedRoom(player);
        model.addAttribute("rooms", myRooms);
        return "main_my_room.html";
    }
}
