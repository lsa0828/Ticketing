package org.example.dao;

import org.example.dto.CurrentSeatDTO;
import org.example.dto.SeatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SeatViewDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<CurrentSeatDTO> findCurrentSeatsByConcertId(Long concertId) {
        String sql = "SELECT s.id AS seat_id, c.id AS concert_id, s.section, s.number, s.pos_x, s.pos_y, COALESCE(r.status, 'AVAILABLE') AS status, r.price "
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
                        rs.getString("status"),
                        rs.getInt("price")
                ),
                concertId
        );
    }

    public SeatDTO getBookingSeat(Long seatId, Long concertId, Long memberId) {
        String sql = "SELECT s.id, s.section, s.number, r.price "
                + "FROM seat_reservations r JOIN seats s ON r.seat_id = s.id "
                + "WHERE r.seat_id = ? AND r.concert_id = ? AND r.member_id = ? AND status = 'BOOKING'";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new SeatDTO(
                        rs.getLong("id"),
                        rs.getString("section"),
                        rs.getInt("number"),
                        rs.getInt("price")
                ),
                seatId, concertId, memberId
        );
    }
}
