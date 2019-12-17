package com.bb.stardium.player.web.controller;

import com.bb.stardium.player.dto.PlayerRequestDto;
import com.bb.stardium.player.dto.PlayerResponseDto;
import com.bb.stardium.player.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class LoginController {
    private static final String REDIRECT = "redirect:";
    private static final String LOGIN = "login";
    private static final String ROOT_PAGE = "/";

    private final PlayerService playerService;

    @GetMapping("/login")
    public String loginPage() {
        return "login.html";
    }

    @PostMapping("/login")
    public String login(final PlayerRequestDto requestDto, final HttpSession session) {
        final PlayerResponseDto responseDto = playerService.login(requestDto);
        session.setAttribute(LOGIN, responseDto);
        return REDIRECT + ROOT_PAGE;
    }

    @GetMapping("/logout")
    public String logout(final HttpSession session) {
        session.invalidate();
        return REDIRECT + ROOT_PAGE;
    }
}
