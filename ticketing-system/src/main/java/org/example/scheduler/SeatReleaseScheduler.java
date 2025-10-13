package org.example.scheduler;

import org.example.dao.SeatReservationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SeatReleaseScheduler {
    @Autowired
    private SeatReservationDAO seatReservationDAO;

    @Scheduled(fixedRate = 10 * 60 * 1000) // 10분마다 실행
    public void releaseExpiredSeats() {
        int updatedRows = seatReservationDAO.releaseExpiredSeats();
        if (updatedRows > 0) {
            System.out.println("[SeatReleaseScheduler] Released " + updatedRows + " expired seats.");
        }
    }
}
