package org.example.dao;

import org.example.dto.CurrentReservationDTO;
import org.example.dto.CurrentSeatDTO;
import org.example.model.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class SeatDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<CurrentSeatDTO> findCurrentSeatsByConcertId(Long concertId) {
        String sql = "SELECT s.id AS seat_id, c.id AS concert_id, s.section, s.number, s.pos_x, s.pos_y, COALESCE(r.status, 'AVAILABLE') AS status "
                + "FROM seats s JOIN concerts c ON s.venue_id = c.venue_id "
                + "LEFT JOIN seat_reservations r ON r.concert_id = c.id AND r.seat_id = s.id "
                + "WHERE c.id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new CurrentSeatDTO(
                        rs.getLong("seat_id"),
                        rs.getLong("concert_id"),
                        rs.getString("section"),
                        rs.getInt("number"),
                        rs.getInt("pos_x"),
                        rs.getInt("pos_y"),
                        rs.getString("status")
                ),
                concertId
        );
    }

    public CurrentReservationDTO findCurrentSeatBySeatIdAndConcertId(Long seatId, Long concertId) {
        String sql = "SELECT sr.seat_id, sr.concert_id, sr.member_id, s.section, s.number, sr.status, sr.expires_at "
                + "FROM seat_reservations sr JOIN seats s ON sr.seat_id = s.id "
                + "WHERE sr.seat_id = ? AND sr.concert_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new CurrentReservationDTO(
                        rs.getLong("seat_id"),
                        rs.getLong("concert_id"),
                        rs.getLong("member_id"),
                        rs.getString("section"),
                        rs.getInt("number"),
                        rs.getString("status"),
                        rs.getObject("expires_at", LocalDateTime.class)
                ),
                seatId, concertId
        );
    }

    private static class SeatRowMapper implements RowMapper<Seat> {
        @Override
        public Seat mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Seat(
                    rs.getLong("id"),
                    rs.getLong("venue_id"),
                    rs.getString("section"),
                    rs.getInt("number"),
                    rs.getInt("pos_x"),
                    rs.getInt("pos_y")
            );
        }
    }
}
