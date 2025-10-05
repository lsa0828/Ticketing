package org.example.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.model.Member;
import org.example.service.MemberService;
import org.example.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loginMember") != null) {
            return true;
        }

        String token = CookieUtil.getCookie(request, "loginToken");
        if (token != null) {
            Member member = memberService.validateToken(token);
            if (member != null) {
                request.getSession(true).setAttribute("loginMember", member);
                return true;
            }
        }

        String uri = request.getRequestURI();
        if (uri.equals("/login") || uri.equals("/register")) {
            return true;
        }

        response.sendRedirect("/login");
        return false;
    }
}
