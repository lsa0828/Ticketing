package org.example.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class SeatReservationDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int updateSeatToBooking(Long seatId, Long concertId, Long memberId) {
        // int currentVersion = getVersion(seatId, concertId);
        String sql = """
                UPDATE seat_reservations
                SET status = 'BOOKING', member_id = ?, booking_time = NOW(), expires_at = DATE_ADD(NOW(), INTERVAL 15 MINUTE), version = version + 1
                WHERE concert_id = ? AND seat_id = ?
                AND (status = 'AVAILABLE' OR (status = 'BOOKING' AND member_id = ?) OR (status = 'BOOKING' AND expires_at < NOW()))
                """;
        return jdbcTemplate.update(sql, memberId, concertId, seatId, memberId);
    }

    public void releaseSeatForOtherMember(Long seatId, Long concertId, Long memberId) {
        String sql = """
                UPDATE seat_reservations
                SET status = 'AVAILABLE', member_id = NULL, booking_time = NULL, expires_at = NULL, version = version + 1
                WHERE seat_id != ? AND concert_id = ? AND member_id = ? AND status = 'BOOKING'
                """;
        int updated = jdbcTemplate.update(sql, seatId, concertId, memberId);
        if (updated > 0) {
            System.out.println("[SeatReleaseScheduler] Released " + updated + " expired seats.");
        }
    }

    public int releaseExpiredSeats() {
        String sql = """
                UPDATE seat_reservations
                SET status = 'AVAILABLE', member_id = NULL, booking_time = NULL, expires_at = NULL, version = version + 1
                WHERE status = 'BOOKING' AND expires_at < NOW()
                """;
        return jdbcTemplate.update(sql);
    }

    public int updateSeatToSold(Long concertId, Long seatId, Long memberId) {
        String sql = """
                UPDATE seat_reservations
                SET status = 'SOLD', version = version + 1
                WHERE concert_id = ? AND seat_id = ? AND member_id = ? AND status = 'BOOKING' AND expires_at > NOW()
                """;
        return jdbcTemplate.update(sql, concertId, seatId, memberId);
    }

    public int getPrice(Long concertId, Long seatId, Long memberId) {
        String sql = """
                SELECT price
                FROM seat_reservations
                WHERE concert_id = ? AND seat_id = ? AND status = 'SOLD' AND member_id = ? 
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class, concertId, seatId, memberId);
    }

    public void updateSeatToAvailable(Long concertId, Long seatId) {
        String sql = """
                UPDATE seat_reservations
                SET status = 'AVAILABLE', version = version + 1, member_id = NULL, booking_time = NULL, expires_at = NULL
                WHERE concert_id = ? AND seat_id = ?
                """;
        jdbcTemplate.update(sql, concertId, seatId);
    }

    public int getVersion(Long seatId, Long concertId) {
        String sql = "SELECT version FROM seat_reservations WHERE seat_id = ? AND concert_id = ?";
        return jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                seatId,
                concertId
        );
    }

    public List<Map<String, Object>> getReleasingVersion(Long seatId, Long concertId, Long memberId) {
        String sql = "SELECT seat_id, version FROM seat_reservations WHERE seat_id != ? AND concert_id = ? AND member_id = ? AND status = 'BOOKING'";
        return jdbcTemplate.queryForList(
                sql,
                seatId,
                concertId,
                memberId
        );
    }
}
