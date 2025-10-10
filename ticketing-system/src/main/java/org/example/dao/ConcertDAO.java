package org.example.dao;

import org.example.dto.ConcertSeatCountDTO;
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

    public Concert findById(Long id) {
        String sql = "SELECT * FROM concerts WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new ConcertRowMapper(), id);
    }

    public List<ConcertSeatCountDTO> findAllConcertSeatCount() {
        String sql = "SELECT c.id AS concert_id, c.title, c.image_url, "
                + "COUNT(CASE WHEN r.status = 'SOLD' THEN 1 END) AS sold_count, "
                + "COUNT(s.id) AS total_count "
                + "FROM concerts c JOIN seats s ON s.venue_id = c.venue_id "
                + "LEFT JOIN seat_reservations r ON r.concert_id = c.id AND r.seat_id = s.id "
                + "GROUP BY c.id, c.title, c.image_url "
                + "ORDER BY c.id";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new ConcertSeatCountDTO(
                        rs.getLong("concert_id"),
                        rs.getString("title"),
                        rs.getString("image_url"),
                        rs.getInt("sold_count"),
                        rs.getInt("total_count")
                )
        );
    }

    private static class ConcertRowMapper implements RowMapper<Concert> {
        @Override
        public Concert mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Concert(
                    rs.getLong("id"),
                    rs.getLong("venue_id"),
                    rs.getString("title"),
                    rs.getString("image_url")
            );
        }
    }
}
