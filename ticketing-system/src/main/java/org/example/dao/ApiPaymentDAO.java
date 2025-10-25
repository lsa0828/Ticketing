package org.example.dao;

import org.example.model.ApiPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ApiPaymentDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Long reservationId, int paidAmount, String type, String transactionId) {
        String sql = """
                INSERT INTO api_payments (reservation_id, paid_amount, type, transaction_id)
                VALUES (?, ?, ?, ?)
                """;
        return jdbcTemplate.update(sql, reservationId, paidAmount, type, transactionId);
    }

    public Optional<ApiPayment> findByReservationId(Long reservationId) {
        String sql = "SELECT * FROM api_payments WHERE reservation_id = ?";
        List<ApiPayment> results = jdbcTemplate.query(sql, (rs, rowNum) -> {
            ApiPayment apiPayment = new ApiPayment();
            apiPayment.setReservationId(rs.getLong("reservation_id"));
            apiPayment.setPaidAmount(rs.getObject("paid_amount", Integer.class));
            apiPayment.setType(rs.getString("type"));
            apiPayment.setTransactionId(rs.getString("transaction_id"));
            return apiPayment;
        }, reservationId);
        return results.stream().findFirst();
    }
}
