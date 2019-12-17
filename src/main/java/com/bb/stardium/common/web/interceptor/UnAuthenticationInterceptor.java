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

    private final SessionService sessionService;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) throws Exception {
        final boolean loggedIn = sessionService.isLoggedIn(request.getSession());

        if (loggedIn) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }

}
