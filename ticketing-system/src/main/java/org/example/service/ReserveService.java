package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.ReservationDAO;
import org.example.dao.SeatDAO;
import org.example.dao.SeatReservationDAO;
import org.example.dto.PayRequestDTO;
import org.example.dto.SeatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ReserveService {
    @Autowired
    private SeatDAO seatDAO;
    @Autowired
    private SeatReservationDAO seatReservationDAO;
    @Autowired
    private ReservationDAO reservationDAO;

    @Transactional
    public SeatDTO updateSeatReservation(Long seatId, Long concertId, Long memberId) {
        seatReservationDAO.releaseSeatForOtherMember(seatId, concertId, memberId);

        int updated = seatReservationDAO.updateSeatToBooking(seatId, concertId, memberId);
        if (updated == 0) {
            throw new OptimisticLockingFailureException("Seat was modified by another user");
        }
        return seatDAO.getSeat(seatId, concertId);
    }

    @Transactional
    public Long payAndReserve(PayRequestDTO payRequest, Long memberId) {
        Long concertId = payRequest.getConcertId();
        Long seatId = payRequest.getSeatId();
        Long payId = payRequest.getPayId();
        int sold = seatReservationDAO.updateSeatToSold(concertId, seatId, memberId);
        if (sold == 0) {
            throw new OptimisticLockingFailureException("Seat expired");
        }
        int saved = reservationDAO.save(memberId, concertId, seatId, payId);
        if (saved == 0) {
            throw new OptimisticLockingFailureException("Payment failed");
        }
        return reservationDAO.getReserveId(memberId, concertId, seatId);
    }
}
