package com.bb.stardium.common.web.interceptor;

import com.bb.stardium.common.web.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class UnAuthenticationInterceptor extends HandlerInterceptorAdapter {
    private static final String ROOT_PATH = "/";

    private final SessionService sessionService;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) throws Exception {
        if (sessionService.isLoggedIn(request.getSession())) {
            response.sendRedirect(ROOT_PATH);
            return false;
        }
        return true;
    }

}
