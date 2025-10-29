package org.example.dao;

import org.example.model.CouponPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CouponPaymentDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(CouponPayment couponPayment) {
        String sql = """
                INSERT INTO coupon_payments
                VALUES (?, ?, ?, ?)
                """;
        return jdbcTemplate.update(sql,
                couponPayment.getReservationId(),
                couponPayment.getMemberCouponId(),
                couponPayment.getOriginalPrice(),
                couponPayment.getDiscountedPrice()
        );
    }

    public Optional<CouponPayment> findByReservationId(Long reservationId) {
        String sql = "SELECT * FROM coupon_payments WHERE reservation_id = ?";
        List<CouponPayment> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CouponPayment.class), reservationId);
        return results.stream().findFirst();
    }
}
