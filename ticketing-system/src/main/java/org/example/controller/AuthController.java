package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.dto.MemberDTO;
import org.example.dto.MemberResponseDTO;
import org.example.service.MemberService;
import org.example.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
    private MemberService memberService;

    @ModelAttribute("currentPage")
    public String currentPage(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.contains("/login")) return "login";
        if (uri.contains("/register")) return "register";
        return "";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(MemberDTO memberDTO, @RequestParam(name = "rememberMe", required = false) String rememberMe,
                        HttpSession session, HttpServletResponse response) {
        if (memberDTO.getId() == null || memberDTO.getPassword() == null) {
            return "redirect:/login?error=true";
        }
        MemberResponseDTO member = memberService.login(memberDTO.getId(), memberDTO.getPassword());
        if (member == null) return "redirect:/login?error=true";

        session.setAttribute("loginMember", member);

        if (rememberMe != null) {
            String token = memberService.createLoginToken(member.getId());
            CookieUtil.addCookie(response, "loginToken", token, 24 * 60 *60);
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
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(MemberDTO memberDTO, @RequestParam("passwordConfirm") String passwordConfirm, Model model) {
        if (memberDTO.getId() == null || memberDTO.getPassword() == null || passwordConfirm == null) {
            model.addAttribute("error", "입력되지 않은 항목이 있습니다.");
            return "register";
        }

        if (!memberDTO.getPassword().equals(passwordConfirm)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "register";
        }

        boolean exists = memberService.isIdExists(memberDTO.getId());
        if (exists) {
            model.addAttribute("error", "이미 존재하는 아이디입니다.");
            return "register";
        }

        memberService.register(memberDTO.getId(), memberDTO.getPassword());
        return "redirect:/login";
    }
}
