package org.example.service;

import org.example.dao.LoginTokenDAO;
import org.example.dao.MemberDAO;
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

    public void register(String id, String plainPassword) {
        String salt = BCrypt.gensalt();
        String password = BCrypt.hashpw(plainPassword, salt);
        Member member = new Member();
        member.setId(id);
        member.setPassword(password);
        memberDAO.save(member);
    }

    public Member login(String id, String plainPassword) {
        Member member = memberDAO.findById(id);
        if (member != null && BCrypt.checkpw(plainPassword, member.getPassword())) {
            member.setPassword(null);
            return member;
        }
        return null;
    }

    public String createLoginToken(String memberId) {
        String token = TokenUtil.generateToken();
        LocalDateTime expiry = LocalDateTime.now().plusDays(7);
        loginTokenDAO.save(token, memberId, expiry);
        return token;
    }

    public Member validateToken(String token) {
        try {
            return loginTokenDAO.findMemberByToken(token);
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteToken(String token) {
        loginTokenDAO.deleteByToken(token);
    }
}
