package org.example.service;

import org.example.dao.SeatViewDAO;
import org.example.dto.CurrentSeatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
    @Autowired
    private SeatViewDAO seatViewDAO;

    public List<CurrentSeatDTO> getSeatStatusOfConcert(Long concertId) {
        return seatViewDAO.findCurrentSeatsByConcertId(concertId);
    }
}
