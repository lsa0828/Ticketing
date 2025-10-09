package org.example.dao;

import org.example.model.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class VenueDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Venue findById(Long id) {
        String sql = "SELECT * FROM venues WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new VenueRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private static class VenueRowMapper implements RowMapper<Venue> {
        @Override
        public Venue mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Venue(
                    rs.getLong("id"),
                    rs.getString("name")
            );
        }
    }
}
