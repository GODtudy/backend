package com.example.godtudy.global.config.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //Header에서 토큰 추출
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        //토큰이 존재하는지 존재한다면 만료시간이 지나지 않았는지 확인
        if (token != null && jwtTokenProvider.validateTokenExpiration(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token); // 인증객체 받아옴
            SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContextHolder에 저장하여 인증 할 수 있게 함
        }
        chain.doFilter(request, response);
    }
}
