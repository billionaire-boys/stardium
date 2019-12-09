package com.bb.stardium.player.web.controller;

import com.bb.stardium.player.dto.Player2RequestDto;
import com.bb.stardium.player.dto.Player2ResponseDto;
import com.bb.stardium.player.service.PlayerService2;
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
    private final PlayerService2 playerService;

    public PlayerController(final PlayerService2 playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/new")
    public String signupPage() {
        return "signup.html";
    }

    @PostMapping("/new")
    public String register(final Player2RequestDto requestDto) {
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
    public String edit(final Player2RequestDto requestDto, final HttpSession session,
                       final RedirectAttributes redirectAttributes) {
        if (Objects.isNull(session.getAttribute("login"))) {
            return "redirect:/login";
        }
        final Player2ResponseDto sessionDto = (Player2ResponseDto) session.getAttribute("login");
        final Player2ResponseDto responseDto = playerService.update(requestDto, sessionDto);
        redirectAttributes.addFlashAttribute("message", "회원 정보가 수정되었습니다.");
        return "redirect:/";
    }
}
