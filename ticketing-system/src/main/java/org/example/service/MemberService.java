package org.example.service;

import org.example.dao.LoginTokenDAO;
import org.example.dao.MemberDAO;
import org.example.dto.MemberResponseDTO;
import org.example.model.Member;
import org.example.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MemberService {
    @Autowired
    private MemberDAO memberDAO;
    @Autowired
    private LoginTokenDAO loginTokenDAO;

    public void register(String name, String plainPassword) {
        String salt = BCrypt.gensalt();
        String password = BCrypt.hashpw(plainPassword, salt);
        Member member = new Member();
        member.setName(name);
        member.setPassword(password);
        memberDAO.save(member);
    }

    public MemberResponseDTO login(String name, String plainPassword) {
        Member member = memberDAO.findByName(name);
        if (member != null && BCrypt.checkpw(plainPassword, member.getPassword())) {
            return new MemberResponseDTO(member.getId(), member.getName(), member.getRole());
        }
        return null;
    }

    public String createLoginToken(Long memberId) {
        String token = TokenUtil.generateToken();
        LocalDateTime expiry = LocalDateTime.now().plusDays(1);
        loginTokenDAO.save(token, memberId, expiry);
        return token;
    }

    public MemberResponseDTO validateToken(String token) {
        try {
            Member member = loginTokenDAO.findMemberByToken(token);
            return new MemberResponseDTO(member.getId(), member.getName(), member.getRole());
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteToken(String token) {
        loginTokenDAO.deleteByToken(token);
    }

    public int deleteExpiredTokens() {
        return loginTokenDAO.deleteExpiredTokens();
    }

    public boolean isIdExists(String name) {
        return memberDAO.countByName(name) > 0;
    }
}
