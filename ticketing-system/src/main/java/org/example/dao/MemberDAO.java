package org.example.dao;

import org.example.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class MemberDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Member findByName(String name) {
        String sql = "SELECT * FROM members WHERE name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Member.class), name);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void save(Member member) {
        String sql = "INSERT INTO members (name, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getName(), member.getPassword());
    }

    public int countByName(String name) {
        String sql = "SELECT COUNT(*) FROM members WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, name);
    }

    public int getPointById(Long id) {
        String sql = "SELECT point FROM members WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id);
    }

    public int minusPoint(Long memberId, int amount) {
        String sql = """
                UPDATE members
                SET point = point - ?
                WHERE id = ? AND point >= ?
                """;
        return jdbcTemplate.update(sql, amount, memberId, amount);
    }

    public void plusPoint(Long memberId, int amount) {
        String sql = """
                UPDATE members
                SET point = point + ?
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, amount, memberId);
    }
}
