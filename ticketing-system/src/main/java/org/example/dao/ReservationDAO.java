package org.example.dao;

import org.example.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long save(Long memberId, Long concertId, Long seatId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = """
                INSERT INTO reservations (member_id, concert_id, seat_id, status)
                VALUES (?, ?, ?, 'PAID')
                """;
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, memberId);
            ps.setLong(2, concertId);
            ps.setLong(3, seatId);
            return ps;
        }, keyHolder);
        Number generatedKey = keyHolder.getKey();
        if (generatedKey == null) {
            throw new IllegalStateException("예약 정보 검색을 실패했습니다.");
        }
        return generatedKey.longValue();
    }

    public Long getReservationId(Long memberId, Long concertId, Long seatId) {
        String sql = "SELECT id FROM reservations "
                + "WHERE member_id = ? AND concert_id = ? AND seat_id = ? "
                + "ORDER BY reserved_at DESC LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Long.class, memberId, concertId, seatId);
    }

    public Optional<Reservation> findById(Long reservationId) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        List<Reservation> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Reservation.class), reservationId);
        return list.stream().findFirst();
    }

    public void updateStatusToRefunded(Long reservationId) {
        String sql = """
                UPDATE reservations
                SET status = 'REFUNDED', refunded_at = ?
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, LocalDateTime.now(), reservationId);
    }
}
