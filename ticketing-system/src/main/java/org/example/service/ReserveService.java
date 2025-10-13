package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.SeatDAO;
import org.example.dao.SeatReservationDAO;
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

    @Transactional(rollbackFor = Exception.class)
    public SeatDTO updateSeatReservation(Long seatId, Long concertId, Long memberId) {
        seatReservationDAO.releaseSeatForOtherMember(seatId, concertId, memberId);

        int updated = seatReservationDAO.updateSeatToBooking(seatId, concertId, memberId);
        if (updated == 0) {
            throw new OptimisticLockingFailureException("Seat was modified by another user");
        }
        return seatDAO.getSeat(seatId, concertId);
    }
}
