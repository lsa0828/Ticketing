package org.example.dao;

import org.example.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
            return jdbcTemplate.queryForObject(sql, new MemberRowMapper(), name);
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

    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("role")
            );
        }
    }
}
