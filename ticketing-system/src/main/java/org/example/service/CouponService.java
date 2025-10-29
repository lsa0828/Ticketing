package org.example.service;

import org.example.dao.MemberCouponDAO;
import org.example.dto.AvailableCouponDTO;
import org.example.dto.response.CouponAppliedPriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CouponService {
    @Autowired
    private MemberCouponDAO memberCouponDAO;

    public List<CouponAppliedPriceDTO> getAvailableCoupon(Long memberId, int price) {
        List<AvailableCouponDTO> coupons =  memberCouponDAO.findNotUsedCoupons(memberId);
        List<CouponAppliedPriceDTO> couponAmounts = new ArrayList<>();
        for (AvailableCouponDTO coupon : coupons) {
            CouponAppliedPriceDTO dto = new CouponAppliedPriceDTO();
            dto.setMemberCouponId(coupon.getMemberCouponId());
            dto.setName(coupon.getName());
            dto.setExpiresAt(coupon.getExpiresAt());
            dto.setDiscountedPrice(getDiscountedPrice(price, coupon));
            couponAmounts.add(dto);
        }
        return couponAmounts;
    }

    public static int getDiscountedPrice(int price, AvailableCouponDTO coupon) {
        if (coupon.getDiscountRate() != 0) {
            int discount = (int) Math.round(price * coupon.getDiscountRate() / 100.0);
            int discountedPrice = price - discount;
            return Math.round(discountedPrice / 10.0f) * 10;
        } else if (coupon.getDiscountAmount() != 0) {
            return Math.max(price - coupon.getDiscountAmount(), 0);
        }
        return price;
    }
}
