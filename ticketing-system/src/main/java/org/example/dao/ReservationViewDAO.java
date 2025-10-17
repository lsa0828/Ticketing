package org.example.dao;

import org.example.dto.ReservedConcertDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservationViewDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ReservedConcertDTO findById(Long reservationId, Long memberId) {
        String sql = """
                SELECT r.id AS reservation_id,
                    c.title, c.image_url,
                    v.name AS venue_name,
                    s.section, s.number, sr.price,
                    r.status AS reservation_status, r.reserved_at
                FROM reservations r
                JOIN concerts c ON r.concert_id = c.id
                JOIN venues v ON c.venue_id = v.id
                JOIN seats s ON r.seat_id = s.id
                JOIN seat_reservations sr ON sr.concert_id = r.concert_id AND sr.seat_id = s.seat_id AND sr.status = 'SOLD'
                WHERE r.id = ? AND r.member_id = ?
                """;
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new ReservedConcertDTO(
                        rs.getLong("reservation_id"),
                        rs.getString("title"),
                        rs.getString("image_url"),
                        rs.getString("venue_name"),
                        rs.getString("section"),
                        rs.getInt("number"),
                        rs.getInt("price"),
                        rs.getString("reservation_status"),
                        rs.getTimestamp("reserved_at").toLocalDateTime()
                ),
                reservationId, memberId
        );
    }

    public List<ReservedConcertDTO> findAll(Long memberId) {
        String sql = """
                SELECT r.id AS reservation_id,
                    c.title, c.image_url,
                    v.name AS venue_name,
                    s.section, s.number, sr.price,
                    r.status AS reservation_status, r.reserved_at
                FROM reservations r
                JOIN concerts c ON r.concert_id = c.id
                JOIN venues v ON c.venue_id = v.id
                JOIN seats s ON r.seat_id = s.id
                JOIN seat_reservations sr ON sr.concert_id = r.concert_id AND sr.seat_id = s.seat_id AND sr.status = 'SOLD'
                WHERE r.member_id = ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new ReservedConcertDTO(
                        rs.getLong("reservation_id"),
                        rs.getString("title"),
                        rs.getString("image_url"),
                        rs.getString("venue_name"),
                        rs.getString("section"),
                        rs.getInt("number"),
                        rs.getInt("price"),
                        rs.getString("reservation_status"),
                        rs.getTimestamp("reserved_at").toLocalDateTime()
                ),
                memberId
        );
    }
}
