package org.example.service.payment;

import org.example.dao.MemberDAO;
import org.example.dao.PointPaymentDAO;
import org.example.dto.PaymentDetail;
import org.example.dto.request.PaymentRequestDTO;
import org.example.dto.request.PaymentType;
import org.example.dto.response.PaymentResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PointPaymentService implements PaymentStrategy {
    @Autowired
    private MemberDAO memberDAO;
    @Autowired
    private PointPaymentDAO pointPaymentDAO;

    @Override
    public PaymentResultDTO pay(PaymentRequestDTO request) {
        PaymentDetail detail = request.getPaymentDetails().getFirst();
        int amount = detail.getAmount();
        int updated = memberDAO.minusPoint(request.getMemberId(), amount);
        if (updated != 1) {
            throw new IllegalStateException("포인트가 부족합니다.");
        }
        int remainingAmount = request.getTotalAmount() - amount;
        List<PaymentDetail> paymentDetails = new ArrayList<>();
        paymentDetails.add(
                new PaymentDetail(
                        PaymentType.POINT,
                        amount,
                        null
                )
        );
        int updatedPayment = pointPaymentDAO.save(request.getReservationId(), amount);
        if (updatedPayment != 1) {
            return new PaymentResultDTO(true, "포인트 결제 실패", request.getTotalAmount(), null);
        }
        return new PaymentResultDTO(true, "포인트 결제 성공", remainingAmount, paymentDetails);
    }

    @Override
    public boolean refund(Long memberId, Long reservationId) {
        int amount = pointPaymentDAO.findByReservationId(reservationId);
        if (amount > 0) {
            memberDAO.plusPoint(memberId, amount);
        }
        return true;
    }
}
