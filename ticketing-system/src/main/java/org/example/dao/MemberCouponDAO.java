package org.example.dao;

import org.example.dto.AvailableCouponDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class MemberCouponDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<AvailableCouponDTO> findNotUsedCoupons(Long memberId) {
        String sql = """
                SELECT mc.id, c.name, c.discount_rate, c.discount_amount, c.expires_at
                FROM coupons c JOIN member_coupons mc ON mc.coupon_id = c.id
                WHERE mc.member_id = ? AND mc.status = 'NOT-USED' AND c.expires_at >= CURRENT_DATE
                ORDER BY c.expires_at
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new AvailableCouponDTO(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("discount_rate"),
                        rs.getInt("discount_amount"),
                        rs.getObject("expires_at", LocalDate.class)
                ),
                memberId
        );
    }

    public int updateCouponToUsed(Long memberCouponId, Long memberId) {
        String sql = """
                UPDATE member_coupons mc
                JOIN coupons c ON mc.coupon_id = c.id
                SET mc.status = 'USED'
                WHERE mc.id = ? AND mc.member_id = ?
                AND mc.status = 'NOT-USED' AND c.expires_at >= CURRENT_DATE
                """;
        return jdbcTemplate.update(sql, memberCouponId, memberId);
    }

    public AvailableCouponDTO findById(Long memberCouponId) {
        String sql = """
                SELECT mc.id, name, discount_rate, discount_amount, expires_at
                FROM coupons c JOIN member_coupons mc ON c.id = mc.coupon_id
                WHERE mc.id = ?
                """;
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new AvailableCouponDTO(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("discount_rate"),
                        rs.getInt("discount_amount"),
                        rs.getObject("expires_at", LocalDate.class)
                ),
                memberCouponId
        );
    }

    public LocalDate getExpiresAt(Long memberCouponId, Long memberId) {
        String sql = """
                SELECT c.expires_at
                FROM coupons c JOIN member_coupons mc ON c.id = mc.coupon_id
                WHERE mc.id = ? AND member_id = ?
                """;
        return jdbcTemplate.queryForObject(sql, LocalDate.class, memberCouponId, memberId);
    }

    public int updateToNotUsed(Long memberCouponId) {
        String sql = """
                UPDATE member_coupons
                SET status = 'NOT-USED'
                WHERE id = ?
                """;
        return jdbcTemplate.update(sql, memberCouponId);
    }
}
