package com.example.godtudy.global.security.AccessDeniedHandler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    // 서버에 요청을 할 때 액세스가 가능한지 권한을 체크후 액세스 할 수 없는 요청을 했을시 동작된다. (403)
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.sendRedirect("/member/login");
    }
}
