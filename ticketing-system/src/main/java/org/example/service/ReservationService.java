package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.ReservationDAO;
import org.example.dao.ReservationViewDAO;
import org.example.dao.SeatViewDAO;
import org.example.dao.SeatReservationDAO;
import org.example.dto.ReservedConcertDTO;
import org.example.dto.request.PayRequestDTO;
import org.example.dto.SeatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ReservationService {
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
        seatReservationDAO.releaseSeatForOtherMember(seatId, concertId, memberId);

        int updated = seatReservationDAO.updateSeatToBooking(seatId, concertId, memberId);
        if (updated == 0) {
            throw new OptimisticLockingFailureException("Seat was modified by another user");
        }
        return seatViewDAO.getSeat(seatId, concertId);
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
        return reservationDAO.getReservationId(memberId, concertId, seatId);
    }

    public ReservedConcertDTO getReservation(Long reservationId, Long memberId) {
        return reservationViewDAO.findById(reservationId, memberId);
    }

    public List<ReservedConcertDTO> getReservationList(Long memberId) {
        return reservationViewDAO.findAll(memberId);
    }
}
