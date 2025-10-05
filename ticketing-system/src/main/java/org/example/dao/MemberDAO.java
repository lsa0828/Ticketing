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

    public Member findById(String id) {
        String sql = "SELECT id, password, role FROM members WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new MemberRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void save(Member member) {
        String sql = "INSERT INTO members (id, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getId(), member.getPassword());
    }

    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(
                    rs.getString("id"),
                    rs.getString("password"),
                    rs.getString("role")
            );
        }
    }
}
