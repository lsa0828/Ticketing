package org.example.dao;

import org.example.model.IMPPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class IMPPaymentDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Long reservationId, int paidAmount, String type, String impUId) {
        String sql = """
                INSERT INTO imp_payments (reservation_id, paid_amount, type, imp_uid)
                VALUES (?, ?, ?, ?)
                """;
        return jdbcTemplate.update(sql, reservationId, paidAmount, type, impUId);
    }

    public Optional<IMPPayment> findByReservationId(Long reservationId) {
        String sql = "SELECT * FROM imp_payments WHERE reservation_id = ?";
        List<IMPPayment> results = jdbcTemplate.query(sql, (rs, rowNum) -> {
            IMPPayment IMPPayment = new IMPPayment();
            IMPPayment.setReservationId(rs.getLong("reservation_id"));
            IMPPayment.setPaidAmount(rs.getObject("paid_amount", Integer.class));
            IMPPayment.setType(rs.getString("type"));
            IMPPayment.setImpUid(rs.getString("imp_uid"));
            return IMPPayment;
        }, reservationId);
        return results.stream().findFirst();
    }
}
