package org.example.service.payment;


import org.example.dao.CouponPaymentDAO;
import org.example.dao.MemberCouponDAO;
import org.example.dto.AvailableCouponDTO;
import org.example.dto.PaymentDetail;
import org.example.dto.request.PaymentRequestDTO;
import org.example.dto.response.PaymentResultDTO;
import org.example.model.CouponPayment;
import org.example.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CouponPaymentService implements PaymentStrategy {
    @Autowired
    private MemberCouponDAO memberCouponDAO;
    @Autowired
    private CouponPaymentDAO couponPaymentDAO;

    @Override
    public PaymentResultDTO pay(PaymentRequestDTO request) {
        try {
            PaymentDetail detail = request.getPaymentDetails().getFirst();
            Object memberCouponObj = detail.getExtraData().get("memberCouponId");
            Long memberCouponId = ((Number) memberCouponObj).longValue();

            int originalPrice = request.getTotalAmount();
            int discountedPrice = originalPrice - detail.getAmount();
            AvailableCouponDTO coupon = memberCouponDAO.findById(memberCouponId);
            if (coupon == null) {
                throw new IllegalStateException("존재하지 않는 쿠폰입니다.");
            }
            if (discountedPrice != CouponService.getDiscountedPrice(originalPrice, coupon)) {
                return new PaymentResultDTO(false, "사용할 수 없는 쿠폰입니다.", request.getTotalAmount(), List.of(detail));
            }
            int updated = memberCouponDAO.updateCouponToUsed(memberCouponId, request.getMemberId());
            if (updated != 1) {
                throw new IllegalStateException("쿠폰 상태 업데이트 실패");
            }

            int remainingAmount = request.getTotalAmount() - detail.getAmount();
            CouponPayment couponPayment = new CouponPayment(request.getReservationId(), memberCouponId, originalPrice, discountedPrice);
            int updatedPayment = couponPaymentDAO.save(couponPayment);
            if (updatedPayment != 1) {
                throw new IllegalStateException("쿠폰 결제 내역 저장 실패");
            }
            return new PaymentResultDTO(true, "쿠폰 적용 성공", remainingAmount, List.of(detail));
        } catch (DataAccessException e) {
            throw new RuntimeException("쿠폰 결제 처리 중 데이터베이스 오류 발생", e);
        } catch (Exception e) {
            throw new RuntimeException("쿠폰 결제 처리 중 알 수 없는 오류 발생", e);
        }
    }

    @Override
    public void refund(Long memberId, Long reservationId) {
        Optional<CouponPayment> couponPaymentOptional = couponPaymentDAO.findByReservationId(reservationId);
        if (couponPaymentOptional.isPresent()) {
            CouponPayment couponPayment = couponPaymentOptional.get();
            LocalDate expiresAt = memberCouponDAO.getExpiresAt(couponPayment.getMemberCouponId(), memberId);
            if (expiresAt.isAfter(LocalDate.now())) {
                int update = memberCouponDAO.updateToNotUsed(couponPayment.getMemberCouponId());
                if (update != 1) {
                    throw new IllegalStateException("쿠폰 환불 실패");
                }
            }
        }
    }
}
