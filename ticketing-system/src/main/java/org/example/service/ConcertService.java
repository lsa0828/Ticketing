package org.example.service;

import org.example.dao.ConcertDAO;
import org.example.dao.ConcertViewDAO;
import org.example.dao.VenueDAO;
import org.example.dto.ConcertSeatCountDTO;
import org.example.dto.response.ConcertVenueDTO;
import org.example.model.Concert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertService {
    @Autowired
    private ConcertDAO concertDAO;
    @Autowired
    private ConcertViewDAO concertViewDAO;
    @Autowired
    private VenueDAO venueDAO;

    public List<ConcertSeatCountDTO> getAllConcertSeatCount() {
        return concertViewDAO.findAllConcertSeatCount();
    }

    public ConcertVenueDTO getConcertVenue(Long concertId) {
        Concert concert = concertDAO.findById(concertId);
        return new ConcertVenueDTO(
                concert.getId(),
                venueDAO.findById(concert.getVenueId()).getName(),
                concert.getTitle(),
                concert.getImageUrl()
        );
    }
}
