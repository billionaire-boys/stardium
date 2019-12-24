package com.bb.stardium.player.web.advice;

import com.bb.stardium.common.web.argumentresolver.Redirection;
import com.bb.stardium.player.service.exception.EmailAlreadyExistException;
import com.bb.stardium.player.service.exception.MisMatchedPasswordException;
import com.bb.stardium.player.service.exception.NicknameAlreadyExistException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class PlayerControllerAdvice {

    @ExceptionHandler(EmailAlreadyExistException.class)
    public RedirectView handleEmailAlreadyExistException(EmailAlreadyExistException e, RedirectAttributes redirectAttributes, Redirection redirection) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView(redirection.getRedirectUrl());
    }

    @ExceptionHandler(NicknameAlreadyExistException.class)
    public RedirectView handleNickNameAlreadyExistException(NicknameAlreadyExistException e, RedirectAttributes redirectAttributes, Redirection redirection) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView(redirection.getRedirectUrl());
    }

    @ExceptionHandler(MisMatchedPasswordException.class)
    public RedirectView handleMisMatchedPasswordException(MisMatchedPasswordException e, RedirectAttributes redirectAttributes, Redirection redirection) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView(redirection.getRedirectUrl());
    }
}
