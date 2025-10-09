package org.example.service;

import org.example.dao.ConcertDAO;
import org.example.dao.SeatReservationDAO;
import org.example.dto.ConcertSeatStatusCountDTO;
import org.example.dto.SeatStatusCountDTO;
import org.example.model.Concert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConcertService {
    @Autowired
    private ConcertDAO concertDAO;
    @Autowired
    private SeatReservationDAO seatReservationDAO;

    public List<Concert> getAllConcerts() {
        return concertDAO.findAll();
    }

    public Concert getConcert(Long id) {
        return concertDAO.findById(id);
    }

    public List<ConcertSeatStatusCountDTO> getAllConcertsStatusCount() {
        List<Concert> concerts = concertDAO.findAll();
        List<ConcertSeatStatusCountDTO> concertStatusCounts = new ArrayList<>();
        for (Concert c : concerts) {
            SeatStatusCountDTO statusCount = seatReservationDAO.countSeatStatus(c.getId());
            concertStatusCounts.add(
                    new ConcertSeatStatusCountDTO(
                            c.getId(),
                            c.getVenueId(),
                            c.getTitle(),
                            c.getImageUrl(),
                            statusCount.getSoldCount(),
                            statusCount.getTotalCount()
                    )
            );
        }
        return concertStatusCounts;
    }
}
