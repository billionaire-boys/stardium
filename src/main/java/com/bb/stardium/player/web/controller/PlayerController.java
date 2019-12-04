package com.bb.stardium.player.web.controller;

import com.bb.stardium.player.dto.PlayerRequestDto;
import com.bb.stardium.player.service.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/player")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(final PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/new")
    public String signupPage() {
        return "signup.html";
    }

    @PostMapping("/new")
    public String register(final PlayerRequestDto requestDto) {
        playerService.register(requestDto);
        return "redirect:/login";
    }

    @GetMapping("/edit")
    public String editPage() {
        return "user-edit.html";
    }
}
