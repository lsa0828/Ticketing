package org.example.dao;

import org.example.model.Concert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ConcertDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Concert> findAll() {
        String sql = "SELECT id, title, image_url FROM concerts";
        return jdbcTemplate.query(sql, new ConcertRowMapper());
    }

    private static class ConcertRowMapper implements RowMapper<Concert> {
        @Override
        public Concert mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Concert(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("image_url")
            );
        }
    }
}
