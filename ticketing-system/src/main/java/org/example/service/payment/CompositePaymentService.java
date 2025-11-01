package org.example.service.payment;

import org.example.dao.ConcertDAO;
import org.example.dao.ReservationDAO;
import org.example.dao.SeatReservationDAO;
import org.example.dto.PaymentDetail;
import org.example.dto.request.PaymentRequestDTO;
import org.example.dto.request.PaymentType;
import org.example.dto.response.PaymentResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CompositePaymentService implements PaymentStrategy {

    private final Map<PaymentType, PaymentStrategy> paymentStrategies = new HashMap<>();

    @Autowired
    public CompositePaymentService(List<PaymentStrategy> strategies) {
        for (PaymentStrategy s : strategies) {
            if (s instanceof PointPaymentService) paymentStrategies.put(PaymentType.POINT, s);
            if (s instanceof IMPPaymentService) paymentStrategies.put(PaymentType.IMP, s);
            if (s instanceof CouponPaymentService) paymentStrategies.put(PaymentType.COUPON, s);
        }
    }

    @Autowired
    private ConcertDAO concertDAO;
    @Autowired
    private ReservationDAO reservationDAO;
    @Autowired
    private SeatReservationDAO seatReservationDAO;

    @Override
    public PaymentResultDTO pay(PaymentRequestDTO request) {
        List<PaymentResultDTO> results = new ArrayList<>();
        int originalPrice = request.getTotalAmount();

        for (PaymentDetail detail : request.getPaymentDetails()) {
            if (request.getTotalAmount() <= 0) break;
            PaymentStrategy strategy = paymentStrategies.get(detail.getType());
            if (strategy == null) {
                throw new IllegalArgumentException("지원하지 않는 결제 방식입니다.");
            }
            if (detail.getAmount() <= 0) continue;
            System.out.println("결제 진행 중: " + detail.getType().getDisplayName());
            PaymentRequestDTO requestDTO = new PaymentRequestDTO();
            requestDTO.setMemberId(request.getMemberId());
            requestDTO.setConcertId(request.getConcertId());
            requestDTO.setSeatId(request.getSeatId());
            requestDTO.setReservationId(request.getReservationId());
            requestDTO.setTotalAmount(request.getTotalAmount());
            requestDTO.setPaymentDetails(List.of(detail));

            PaymentResultDTO result = strategy.pay(requestDTO);
            if (!result.isSuccess()) {
                throw new RuntimeException("결제 실패: " + detail.getType());
            }

            results.add(result);
            request.setTotalAmount(result.getTotalAmount());
        }

        if (request.getTotalAmount() != 0) {
            throw new RuntimeException(String.format("결제 금액 불일치: 오차(%d)", request.getTotalAmount()));
        }

        List<PaymentDetail> mergedDetails = results.stream()
                .flatMap(r -> r.getPaymentDetails().stream())
                .collect(Collectors.toList());
        String typeSummary = mergedDetails.stream()
                .map(detail -> detail.getType().getDisplayName())
                .distinct()
                .collect(Collectors.joining(", "));

        PaymentResultDTO finalResult = new PaymentResultDTO();
        finalResult.setSuccess(true);
        finalResult.setMessage("결제 성공 (" + typeSummary + ")");
        finalResult.setTotalAmount(originalPrice);
        finalResult.setPaymentDetails(mergedDetails);
        return finalResult;
    }

    @Override
    public boolean refund(Long memberId, Long reservationId) {
        boolean refundableCoupon = paymentStrategies.get(PaymentType.COUPON).refund(memberId, reservationId);
        if (!refundableCoupon) return false;
        paymentStrategies.get(PaymentType.POINT).refund(memberId, reservationId);
        paymentStrategies.get(PaymentType.IMP).refund(memberId, reservationId);
        return true;
    }
}
