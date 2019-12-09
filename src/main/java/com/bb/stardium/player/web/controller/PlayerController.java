package com.bb.stardium.player.web.controller;

import com.bb.stardium.player.dto.PlayerRequestDto;
import com.bb.stardium.player.dto.PlayerResponseDto;
import com.bb.stardium.player.service.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Objects;

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
    public String editPage(final HttpSession session) {
        if (Objects.isNull(session.getAttribute("login"))) {
            return "redirect:/login";
        }
        return "user_edit.html";
    }

    @PostMapping("/edit")
    public String edit(final PlayerRequestDto requestDto, final HttpSession session,
                       final RedirectAttributes redirectAttributes) {
        if (Objects.isNull(session.getAttribute("login"))) {
            return "redirect:/login";
        }
        final PlayerResponseDto sessionDto = (PlayerResponseDto) session.getAttribute("login");
        final PlayerResponseDto responseDto = playerService.update(requestDto, sessionDto);
        redirectAttributes.addFlashAttribute("message", "회원 정보가 수정되었습니다.");
        return "redirect:/";
    }
}
