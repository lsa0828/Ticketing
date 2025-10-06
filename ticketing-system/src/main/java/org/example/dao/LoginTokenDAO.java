package org.example.dao;

import org.example.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
public class LoginTokenDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(String token, String memberId, LocalDateTime expiry) {
        String sql = "INSERT INTO login_tokens (token, member_id, expiry) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, token, memberId, Timestamp.valueOf(expiry));
    }

    public Member findMemberByToken(String token) {
        String sql = "SELECT m.id, m.password, m.role FROM members m" +
                "JOIN login_tokens t ON m.id = t.member_id" +
                "WHERE t.token = ? AND t.expiry > NOW()";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                    new Member(rs.getString("id"), rs.getString("password"),
                            rs.getString("role")),
                    token
            );
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteByToken(String token) {
        String sql = "DELETE FROM login_tokens WHERE token = ?";
        jdbcTemplate.update(sql, token);
    }

    public int deleteExpiredTokens() {
        String sql = "DELETE FROM login_tokens WHERE expiry < NOW()";
        return jdbcTemplate.update(sql);
    }
}
