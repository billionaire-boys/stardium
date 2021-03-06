package com.bb.stardium.common.web.config;

import com.bb.stardium.common.web.argumentresolver.LoginArgumentResolver;
import com.bb.stardium.common.web.interceptor.AuthenticationInterceptor;
import com.bb.stardium.common.web.interceptor.UnAuthenticationInterceptor;
import com.bb.stardium.player.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;
    private final UnAuthenticationInterceptor unAuthenticationInterceptor;
    private final PlayerService playerService;

    @Value("${interceptor.unauthenticated}")
    private final List<String> unAuthExcludedPaths;

    @Value("${interceptor.authenticated}")
    private final List<String> authPaths;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(unAuthExcludedPaths)
                .excludePathPatterns(authPaths);

        registry.addInterceptor(unAuthenticationInterceptor)
                .addPathPatterns(authPaths);
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginArgumentResolver(playerService));
    }
}
