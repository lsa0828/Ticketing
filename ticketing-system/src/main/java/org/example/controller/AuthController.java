package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.model.Member;
import org.example.service.MemberService;
import org.example.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("currentPage", "login");
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("id") String id, @RequestParam("password") String password,
                        @RequestParam(name = "rememberMe", required = false) String rememberMe,
                        HttpSession session, HttpServletResponse response) {
        Member member = memberService.login(id, password);
        if (member == null) return "redirect:/login?error=true";

        session.setAttribute("loginMember", member);

        if (rememberMe != null) {
            String token = memberService.createLoginToken(id);
            CookieUtil.addCookie(response, "loginToken", token, 7 * 24 * 60 *60);
        }

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        session.invalidate();
        String token = CookieUtil.getCookie(request, "loginToken");
        if (token != null) {
            memberService.deleteToken(token);
            CookieUtil.deleteCookie(response, "loginToken");
        }
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("currentPage", "register");
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("id") String id, @RequestParam("password") String password) {
        memberService.register(id, password);
        return "redirect:/login";
    }
}
