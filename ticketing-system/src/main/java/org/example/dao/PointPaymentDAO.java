package org.example.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PointPaymentDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Long reservationId, int amount) {
        String sql = """
                INSERT INTO point_payments
                VALUES (?, ?)
                """;
        return jdbcTemplate.update(sql, reservationId, amount);
    }

    public int findByReservationId(Long reservationId) {
        String sql = "SELECT amount FROM point_payments WHERE reservation_id = ?";
        List<Integer> result = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("amount"), reservationId);
        return result.isEmpty() ? 0 : result.getFirst();
    }
}
