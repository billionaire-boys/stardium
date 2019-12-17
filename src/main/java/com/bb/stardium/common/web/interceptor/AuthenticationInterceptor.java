package com.bb.stardium.common.web.interceptor;

import com.bb.stardium.common.web.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    private final SessionService sessionService;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) throws Exception {
        if (sessionService.isLoggedIn(request.getSession())) {
            return true;
        }
        response.sendRedirect("/login");
        return false;
    }

}
