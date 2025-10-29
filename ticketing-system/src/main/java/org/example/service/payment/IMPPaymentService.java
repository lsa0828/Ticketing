package org.example.service.payment;

import org.example.dao.ApiPaymentDAO;
import org.example.dto.PaymentDetail;
import org.example.dto.request.PaymentRequestDTO;
import org.example.dto.request.PaymentType;
import org.example.dto.response.PaymentResultDTO;
import org.example.model.ApiPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class IMPPaymentService implements PaymentStrategy {
    @Autowired
    private ApiPaymentDAO apiPaymentDAO;

    @Override
    public PaymentResultDTO pay(PaymentRequestDTO request) {
        PaymentDetail detail = request.getPaymentDetails().getFirst();
        String transactionId = detail.getExtraData().get("transactionId").toString();
        List<PaymentDetail> paymentDetails = new ArrayList<>();
        paymentDetails.add(
                new PaymentDetail(
                        PaymentType.IMP,
                        request.getTotalAmount(),
                        Map.of(
                                "transactionId", transactionId
                        )
                )
        );
        int updated = apiPaymentDAO.save(request.getReservationId(), request.getTotalAmount(), String.valueOf(PaymentType.IMP), transactionId);
        if (updated != 1) {
            return new PaymentResultDTO(true, "아임포트 결제 실패", request.getTotalAmount(), null);
        }
        return new PaymentResultDTO(true, "아임포트 결제 성공", 0, paymentDetails);
    }

    @Override
    public boolean refund(Long memberId, Long reservationId) {
        Optional<ApiPayment> apiPayment = apiPaymentDAO.findByReservationId(reservationId);
        if (apiPayment.isPresent()) {
            // transactionId 이용한 환불 진행
        }
        return true;
    }
}
