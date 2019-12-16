package com.bb.stardium.player.web.controller;

import com.bb.stardium.player.dto.PlayerRequestDto;
import com.bb.stardium.player.dto.PlayerResponseDto;
import com.bb.stardium.player.service.PlayerService;
import com.bb.stardium.player.service.exception.AuthenticationFailException;
import com.bb.stardium.player.service.exception.EmailNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class LoginController {
    private static final String REDIRECT = "redirect:";
    private static final String IS_LOGIN_SUCCESS = "isLoginSuccess";
    private static final String LOGIN = "login";

    private final PlayerService playerService;

    @GetMapping("/login")
    public String loginPage() {
        return "login.html";
    }

    @PostMapping("/login")
    public String login(final PlayerRequestDto requestDto, final HttpSession session,
                        final RedirectAttributes redirectAttributes) {
        try {
            final PlayerResponseDto responseDto = playerService.login(requestDto);
            session.setAttribute(LOGIN, responseDto);
            redirectAttributes.addFlashAttribute(IS_LOGIN_SUCCESS, true);
            return REDIRECT + "/";
        } catch (final AuthenticationFailException | EmailNotExistException exception) {
            redirectAttributes.addFlashAttribute(IS_LOGIN_SUCCESS, false);
            return REDIRECT + "/login";
        }
    }

    @GetMapping("/logout")
    public String logout(final HttpSession session) {
        session.invalidate();
        return REDIRECT + "/";
    }
}
