package com.bb.stardium.player.web.controller;

import com.bb.stardium.player.dto.Player2RequestDto;
import com.bb.stardium.player.dto.Player2ResponseDto;
import com.bb.stardium.player.service.PlayerService2;
import com.bb.stardium.player.service.exception.AuthenticationFailException;
import com.bb.stardium.player.service.exception.EmailNotExistException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    private static final String REDIRECT = "redirect:";
    private static final String IS_LOGIN_SUCCESS = "isLoginSuccess";
    private static final String LOGIN = "login";

    private final PlayerService2 playerService;

    public LoginController(final PlayerService2 playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login.html";
    }

    @PostMapping("/login")
    public String login(final Player2RequestDto requestDto, final HttpSession session,
                        final RedirectAttributes redirectAttributes) {
        try {
            final Player2ResponseDto responseDto = playerService.login(requestDto);
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
