package com.bb.stardium.player.web.advice;

import com.bb.stardium.common.web.argumentresolver.Redirection;
import com.bb.stardium.player.service.exception.AuthenticationFailException;
import com.bb.stardium.player.service.exception.EmailAlreadyExistException;
import com.bb.stardium.player.service.exception.EmailNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class LoginControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(LoginControllerAdvice.class);

    @ExceptionHandler(AuthenticationFailException.class)
    public RedirectView handleAuthenticationFailException(AuthenticationFailException e, RedirectAttributes redirectAttributes, Redirection redirection) {
        log.error(e.getMessage());
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView(redirection.getRedirectUrl());
    }

    @ExceptionHandler(EmailNotExistException.class)
    public RedirectView handleEmailNotExistException(EmailNotExistException e, RedirectAttributes redirectAttributes, Redirection redirection) {
        log.error(e.getMessage());
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView(redirection.getRedirectUrl());
    }
}
