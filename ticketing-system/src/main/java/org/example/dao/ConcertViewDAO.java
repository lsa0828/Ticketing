package org.example.dao;

import org.example.dto.ConcertSeatCountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConcertViewDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ConcertSeatCountDTO> findAllConcertSeatCount() {
        String sql = "SELECT c.id AS concert_id, c.title, c.image_url, "
                + "COUNT(CASE WHEN r.status IN ('SOLD', 'BOOKING') THEN 1 END) AS sold_count, "
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
}
