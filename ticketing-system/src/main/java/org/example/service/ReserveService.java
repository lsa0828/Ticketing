package org.example.service;

import org.example.dao.SeatDAO;
import org.example.dto.ConcertSeatDTO;
import org.example.dto.CurrentReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReserveService {
    @Autowired
    private SeatDAO seatDAO;

    public ConcertSeatDTO checkSeat(Long seatId, Long concertId, Long memberId) {
        CurrentReservationDTO currentReservation = seatDAO.findCurrentSeatBySeatIdAndConcertId(seatId, concertId);
        if (currentReservation.getStatus().equals("AVAILABLE") || (currentReservation.getStatus().equals("BOOKING") && currentReservation.getMemberId().equals(memberId) && currentReservation.getExpiresAt().isAfter(LocalDateTime.now()))) {
            return new ConcertSeatDTO(
                    currentReservation.getSeatId(),
                    currentReservation.getConcertId(),
                    currentReservation.getSection(),
                    currentReservation.getNumber()
            );
        } else {
            return null;
        }
    }
}
