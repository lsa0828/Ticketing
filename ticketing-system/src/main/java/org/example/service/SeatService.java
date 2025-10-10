package org.example.service;

import org.example.dao.SeatDAO;
import org.example.dto.CurrentSeatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
    @Autowired
    private SeatDAO seatDAO;

    public List<CurrentSeatDTO> getSeatStatusOfConcert(Long concertId) {
        return seatDAO.findCurrentSeatsByConcertId(concertId);
    }
}
