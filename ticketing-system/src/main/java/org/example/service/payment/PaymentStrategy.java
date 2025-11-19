package org.example.service.payment;

import org.example.dto.request.PaymentRequestDTO;
import org.example.dto.response.PaymentResultDTO;

public interface PaymentStrategy {
    PaymentResultDTO pay(PaymentRequestDTO request);
    void refund(Long memberId, Long reservationId);
}