package org.example.service.payment;


import org.example.dao.CouponPaymentDAO;
import org.example.dao.MemberCouponDAO;
import org.example.dto.AvailableCouponDTO;
import org.example.dto.PaymentDetail;
import org.example.dto.request.PaymentRequestDTO;
import org.example.dto.request.PaymentType;
import org.example.dto.response.PaymentResultDTO;
import org.example.model.CouponPayment;
import org.example.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CouponPaymentService implements PaymentStrategy {
    @Autowired
    private MemberCouponDAO memberCouponDAO;
    @Autowired
    private CouponPaymentDAO couponPaymentDAO;

    @Override
    public PaymentResultDTO pay(PaymentRequestDTO request) {
        PaymentDetail detail = request.getPaymentDetails().getFirst();
        Object memberCouponObj = detail.getExtraData().get("memberCouponId");
        Long memberCouponId = null;
        if (memberCouponObj != null) {
            if (memberCouponObj instanceof Number) {
                memberCouponId = ((Number) memberCouponObj).longValue();
            } else if (memberCouponObj instanceof String) {
                memberCouponId = Long.valueOf((String) memberCouponObj);
            }
        }
        int updated = memberCouponDAO.updateCouponToUsed(memberCouponId, request.getMemberId());
        if (updated != 1) {
            throw new IllegalStateException("사용할 수 없는 쿠폰입니다.");
        }
        int originalPrice = request.getTotalAmount();
        int discountedPrice = originalPrice - detail.getAmount();
        AvailableCouponDTO coupon = memberCouponDAO.findById(memberCouponId);
        if (discountedPrice != CouponService.getDiscountedPrice(originalPrice, coupon)) {
            return new PaymentResultDTO(false, "사용할 수 없는 쿠폰입니다.", request.getTotalAmount(), null);
        }
        int remainingAmount = request.getTotalAmount() - detail.getAmount();
        List<PaymentDetail> paymentDetails = new ArrayList<>();
        paymentDetails.add(
                new PaymentDetail(
                        PaymentType.COUPON,
                        detail.getAmount(),
                        Map.of(
                                "memberCouponId", memberCouponId
                        )
                )
        );
        CouponPayment couponPayment = new CouponPayment(request.getReservationId(), memberCouponId, originalPrice, discountedPrice);
        int updatedPayment = couponPaymentDAO.save(couponPayment);
        if (updatedPayment != 1) {
            return new PaymentResultDTO(false, "쿠폰 적용 실패", request.getTotalAmount(), null);
        }
        return new PaymentResultDTO(true, "쿠폰 적용 성공", remainingAmount, paymentDetails);
    }

    @Override
    public boolean refund(Long memberId, Long reservationId) {
        Optional<CouponPayment> couponPaymentOptional = couponPaymentDAO.findByReservationId(reservationId);
        if (couponPaymentOptional.isPresent()) {
            CouponPayment couponPayment = couponPaymentOptional.get();
            LocalDate expiresAt = memberCouponDAO.getExpiresAt(couponPayment.getMemberCouponId(), memberId);
            if (expiresAt.isAfter(LocalDate.now())) {
                int updated = memberCouponDAO.updateToNotUsed(couponPayment.getMemberCouponId());
                return updated == 1;
            } else {
                return false;
            }
        }
        return true;
    }
}
