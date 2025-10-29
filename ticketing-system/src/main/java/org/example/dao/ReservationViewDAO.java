package org.example.dao;

import org.example.dto.ReservedConcertDTO;
import org.example.dto.ReservedConcertDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservationViewDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ReservedConcertDetailDTO findById(Long reservationId, Long memberId) {
        String sql = """
                SELECT r.id AS reservation_id,
                    c.title, c.date, c.image_url,
                    v.name AS venue_name,
                    s.section, s.number,
                    co.name AS coupon_name,
                    pp.amount AS point_amount,
                    ap.paid_amount,
                    r.status AS reservation_status, r.reserved_at
                FROM reservations r
                JOIN concerts c ON r.concert_id = c.id
                JOIN venues v ON c.venue_id = v.id
                JOIN seats s ON r.seat_id = s.id
                LEFT JOIN api_payments ap ON ap.reservation_id = r.id
                LEFT JOIN point_payments pp ON pp.reservation_id = r.id
                LEFT JOIN coupon_payments cp ON cp.reservation_id = r.id
                LEFT JOIN member_coupons mc ON mc.id = cp.member_coupon_id
                LEFT JOIN coupons co ON co.id = mc.coupon_id
                WHERE r.id = ? AND r.member_id = ? AND r.status = 'PAID'
                """;
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new ReservedConcertDetailDTO(
                        rs.getLong("reservation_id"),
                        rs.getString("title"),
                        rs.getObject("date", LocalDate.class),
                        rs.getString("image_url"),
                        rs.getString("venue_name"),
                        rs.getString("section"),
                        rs.getInt("number"),
                        rs.getString("coupon_name"),
                        rs.getInt("point_amount"),
                        rs.getInt("paid_amount"),
                        rs.getString("reservation_status"),
                        rs.getTimestamp("reserved_at").toLocalDateTime()
                ),
                reservationId, memberId
        );
    }

    public List<ReservedConcertDTO> findAll(Long memberId) {
        String sql = """
                SELECT r.id AS reservation_id,
                    c.title, c.date,
                    r.status, r.reserved_at, r.refunded_at
                FROM reservations r
                JOIN concerts c ON r.concert_id = c.id
                WHERE r.member_id = ?
                ORDER BY r.reserved_at DESC
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new ReservedConcertDTO(
                        rs.getLong("reservation_id"),
                        rs.getString("title"),
                        rs.getObject("date", LocalDate.class),
                        rs.getString("status"),
                        rs.getObject("reserved_at", LocalDateTime.class),
                        rs.getObject("refunded_at", LocalDateTime.class)
                ),
                memberId
        );
    }
}
