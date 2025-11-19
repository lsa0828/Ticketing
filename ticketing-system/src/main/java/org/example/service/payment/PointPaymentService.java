package org.example.service.payment;

import org.example.dao.MemberDAO;
import org.example.dao.PointPaymentDAO;
import org.example.dto.PaymentDetail;
import org.example.dto.request.PaymentRequestDTO;
import org.example.dto.response.PaymentResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointPaymentService implements PaymentStrategy {
    @Autowired
    private MemberDAO memberDAO;
    @Autowired
    private PointPaymentDAO pointPaymentDAO;

    @Override
    public PaymentResultDTO pay(PaymentRequestDTO request) {
        try {
            PaymentDetail detail = request.getPaymentDetails().getFirst();
            int amount = detail.getAmount();

            int updated = memberDAO.minusPoint(request.getMemberId(), amount);
            if (updated != 1) {
                return new PaymentResultDTO(false, "포인트가 부족합니다.", request.getTotalAmount(), null);
            }

            int remainingAmount = request.getTotalAmount() - amount;
            int updatedPayment = pointPaymentDAO.save(request.getReservationId(), amount);
            if (updatedPayment != 1) {
                throw new IllegalStateException("포인트 결제 내역 저장 실패");
            }
            return new PaymentResultDTO(true, "포인트 결제 성공", remainingAmount, List.of(detail));
        } catch (DataAccessException e) {
            throw new RuntimeException("포인트 결제 처리 중 데이터베이스 오류 발생", e);
        } catch (Exception e) {
            throw new RuntimeException("포인트 결제 처리 중 알 수 없는 오류 발생", e);
        }
    }

    @Override
    public void refund(Long memberId, Long reservationId) {
        int amount = pointPaymentDAO.findByReservationId(reservationId);
        if (amount > 0) {
            int update = memberDAO.plusPoint(memberId, amount);
            if (update != 1) {
                throw new IllegalStateException("포인트 환불 실패");
            }
        }
    }
}
