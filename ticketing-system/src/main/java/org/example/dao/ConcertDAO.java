package org.example.dao;

import org.example.model.Concert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Repository
public class ConcertDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Concert findById(Long id) {
        String sql = "SELECT * FROM concerts WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new ConcertRowMapper(), id);
    }

    private static class ConcertRowMapper implements RowMapper<Concert> {
        @Override
        public Concert mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Concert(
                    rs.getLong("id"),
                    rs.getLong("venue_id"),
                    rs.getString("title"),
                    rs.getObject("date", LocalDate.class),
                    rs.getString("image_url")
            );
        }
    }
}
