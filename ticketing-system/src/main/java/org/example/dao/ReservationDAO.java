package org.example.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Long memberId, Long concertId, Long seatId, Long payId) {
        String sql = "INSERT INTO reservations (member_id, concert_id, seat_id, status, pay_id) "
                + "VALUES (?, ?, ?, 'PAID', ?)";
        return jdbcTemplate.update(sql, memberId, concertId, seatId, payId);
    }

    public Long getReservationId(Long memberId, Long concertId, Long seatId) {
        String sql = "SELECT id FROM reservations "
                + "WHERE member_id = ? AND concert_id = ? AND seat_id = ? "
                + "ORDER BY reserved_at DESC LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Long.class, memberId, concertId, seatId);
    }
}
