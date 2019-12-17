package com.bb.stardium.common.web.controller;

import com.bb.stardium.bench.domain.Section;
import com.bb.stardium.bench.dto.RoomResponseDto;
import com.bb.stardium.bench.service.RoomService;
import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.dto.PlayerResponseDto;
import com.bb.stardium.player.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainPageController {

    private final PlayerService playerService;
    private final RoomService roomService;

    @GetMapping("/")
    public String homepage(Model model) {
        List<RoomResponseDto> allRooms = roomService.findAllUnexpiredRooms();
        model.addAttribute("rooms", allRooms);
        model.addAttribute("sections", Section.getAllSections());
        return "main-all-room";
    }

    @GetMapping("/{section}")
    public String filteredHomePage(@PathVariable String section, Model model) {
        List<RoomResponseDto> filteredRooms = roomService.findRoomsFilterBySection(section);
        model.addAttribute("rooms", filteredRooms);
        model.addAttribute("sections", Section.getAllSections());
        return "main-all-room";
    }

    @GetMapping("/search/{searchKeyword}")
    public String searchRooms(@PathVariable String searchKeyword, Model model) {
        List<RoomResponseDto> searchedRooms = roomService.findRoomBySearchKeyword(searchKeyword);
        model.addAttribute("rooms", searchedRooms);
        model.addAttribute("sections", Section.getAllSections());
        return "main-all-room";
    }

    @GetMapping("/my-room")
    public String myRoomPage(Model model, HttpSession session) {
        PlayerResponseDto sessionDto = (PlayerResponseDto) session.getAttribute("login");
        Player player = playerService.findByPlayerEmail(sessionDto.getEmail());
        List<RoomResponseDto> myRooms = roomService.findPlayerJoinedRoom(player);
        model.addAttribute("rooms", myRooms);
        return "main-my-room";
    }
}
