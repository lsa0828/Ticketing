package org.example.dao;

import org.example.dto.SeatStatusCountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SeatReservationDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SeatStatusCountDTO countSeatStatus(Long concertId) {
        String sql = "SELECT "
                + "SUM(CASE WHEN status = 'SOLD' THEN 1 ELSE 0 END) AS sold_count, "
                + "COUNT(*) AS total_count "
                + "FROM seat_reservations WHERE concert_id = ? GROUP BY concert_id";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                        new SeatStatusCountDTO(
                                rs.getInt("sold_count"),
                                rs.getInt("total_count")
                        ),
                concertId
        );
    }
}
