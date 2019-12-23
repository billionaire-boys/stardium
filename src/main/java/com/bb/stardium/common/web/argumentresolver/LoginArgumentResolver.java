package com.bb.stardium.common.web.argumentresolver;

import com.bb.stardium.common.web.argumentresolver.annotation.*;
import com.bb.stardium.player.dto.PlayerResponseDto;
import com.bb.stardium.player.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    private final PlayerService playerService;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoggedInPlayer.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final PlayerResponseDto responseDto = (PlayerResponseDto) request.getSession().getAttribute("login");
        return playerService.findByResponseDto(responseDto);
    }

}
