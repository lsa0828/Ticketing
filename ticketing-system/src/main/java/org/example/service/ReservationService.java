package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.*;
import org.example.dto.ReservedConcertDTO;
import org.example.dto.ReservedConcertDetailDTO;
import org.example.dto.request.PayRequestDTO;
import org.example.dto.SeatDTO;
import org.example.model.Concert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ReservationService {
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
    public Long payAndReserve(PayRequestDTO payRequest, Long memberId) {
        Long concertId = payRequest.getConcertId();
        Long seatId = payRequest.getSeatId();
        Long payId = payRequest.getPayId();

        checkConcert(concertId);

        int sold = seatReservationDAO.updateSeatToSold(concertId, seatId, memberId);
        if (sold == 0) {
            throw new OptimisticLockingFailureException("Seat expired");
        }

        int saved = reservationDAO.save(memberId, concertId, seatId, payId);
        if (saved == 0) {
            throw new OptimisticLockingFailureException("Payment failed");
        }

        return reservationDAO.getReservationId(memberId, concertId, seatId);
    }

    public ReservedConcertDetailDTO getReservation(Long reservationId, Long memberId) {
        return reservationViewDAO.findById(reservationId, memberId);
    }

    public List<ReservedConcertDTO> getReservationList(Long memberId) {
        return reservationViewDAO.findAll(memberId);
    }

    public void checkConcert(Long concertId) {
        Concert concert = concertDAO.findById(concertId);
        if (concert == null) {
            throw new IllegalArgumentException("공연 정보를 찾을 수 없습니다.");
        }
        if (!concert.isBookable()) {
            throw new IllegalStateException("이미 예매가 마감된 공연입니다.");
        }
    }
}
