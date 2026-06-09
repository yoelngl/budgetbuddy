package com.budgetbuddy.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        boolean authenticated = session != null && session.getAttribute("userId") != null;

        String path = request.getRequestURI();

        if (authenticated) {
            if (path.equals("/login") || path.equals("/register")) {
                response.sendRedirect("/dashboard");
                return false;
            }
            return true;
        }

        if (path.equals("/login") || path.equals("/register")) {
            return true;
        } else if (path.startsWith("/api/")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"Silakan login terlebih dahulu\"}");
        } else {
            response.sendRedirect("/login");
        }
        return false;
    }
}
