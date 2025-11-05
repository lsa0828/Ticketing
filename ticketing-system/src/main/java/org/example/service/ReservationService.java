package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.*;
import org.example.dto.ReservedConcertDTO;
import org.example.dto.response.ReservedConcertDetailDTO;
import org.example.dto.request.PaymentRequestDTO;
import org.example.dto.SeatDTO;
import org.example.dto.response.PaymentResultDTO;
import org.example.model.Concert;
import org.example.model.Reservation;
import org.example.service.payment.CompositePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class ReservationService {
    @Autowired
    private CompositePaymentService compositePaymentService;
    @Autowired
    private ConcertDAO concertDAO;
    @Autowired
    private SeatViewDAO seatViewDAO;
    @Autowired
    private SeatReservationDAO seatReservationDAO;
    @Autowired
    private ReservationDAO reservationDAO;
    @Autowired
    private ReservationViewDAO reservationViewDAO;

    @Transactional
    public SeatDTO updateSeatReservation(Long seatId, Long concertId, Long memberId) {
        checkConcert(concertId);

        seatReservationDAO.releaseSeatForOtherMember(seatId, concertId, memberId);

        int updated = seatReservationDAO.updateSeatToBooking(seatId, concertId, memberId);
        if (updated == 0) {
            throw new OptimisticLockingFailureException("이미 선택된 좌석입니다.");
        }
        return seatViewDAO.getSeat(seatId, concertId);
    }

    @Transactional
    public Long reserve(PaymentRequestDTO paymentRequest) {
        Long memberId = paymentRequest.getMemberId();
        Long concertId = paymentRequest.getConcertId();
        Long seatId = paymentRequest.getSeatId();

        checkConcert(concertId);

        int sold = seatReservationDAO.updateSeatToSold(concertId, seatId, memberId);
        if (sold == 0) {
            throw new OptimisticLockingFailureException("Seat expired");
        }
        int price = seatReservationDAO.getPrice(concertId, seatId, memberId);
        if (price >= 0 && price != paymentRequest.getTotalAmount()) {
            throw new IllegalArgumentException("금액 정보가 일치하지 않습니다.");
        }
        Long reservationId = reservationDAO.save(memberId, concertId, seatId);
        paymentRequest.setReservationId(reservationId);

        try {
            PaymentResultDTO paymentResult = compositePaymentService.pay(paymentRequest);
            if (!paymentResult.isSuccess()) {
                throw new RuntimeException("Payment failed: " + paymentResult.getMessage());
            }
            return reservationId;
        } catch (Exception e) {
            System.out.println("서비스 오류: " + e.getMessage());
            compositePaymentService.refund(memberId, reservationId);
            throw e;
        }
    }

    @Transactional
    public String refund(Long memberId, Long reservationId) {
        Reservation reservation = reservationDAO.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));
        if (!reservation.getMemberId().equals(memberId)) {
            throw new SecurityException("본인의 예매만 환불할 수 있습니다.");
        }

        Concert concert = concertDAO.findById(reservation.getConcertId())
                .orElseThrow(() -> new IllegalArgumentException("공연 정보를 찾을 수 없습니다."));
        if (concert.getDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("이미 종료된 공연은 환불할 수 없습니다.");
        }

        if (reservation.getStatus().equals("REFUNDED")) {
            throw new IllegalStateException("이미 환불된 결제입니다.");
        }

        compositePaymentService.refund(memberId, reservationId);

        reservationDAO.updateStatusToRefunded(reservationId);
        seatReservationDAO.updateSeatToAvailable(reservation.getConcertId(), reservation.getSeatId());

        return "공연 '" + concert.getTitle() + "'을(를) 환불했습니다.";
    }

    public ReservedConcertDetailDTO getReservation(Long reservationId, Long memberId) {
        return reservationViewDAO.findById(reservationId, memberId);
    }

    public List<ReservedConcertDTO> getReservationList(Long memberId) {
        return reservationViewDAO.findAll(memberId);
    }

    public void checkConcert(Long concertId) {
        Concert concert = concertDAO.findById(concertId)
                .orElseThrow(() -> new IllegalArgumentException("공연 정보를 찾을 수 없습니다."));
        if (!concert.isBookable()) {
            throw new IllegalStateException("이미 예매가 마감된 공연입니다.");
        }
    }
}
