package org.example.service;

import org.example.dao.SeatDAO;
import org.example.dao.SeatReservationDAO;
import org.example.dto.SeatDTO;
import org.example.model.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService {
    @Autowired
    private SeatDAO seatDAO;
    @Autowired
    private SeatReservationDAO seatReservationDAO;

    public List<SeatDTO> getSeatsOfVenue(Long venueId) {
        List<Seat> seats = seatDAO.findByVenueId(venueId);
        return seats.stream()
                .map(seat -> new SeatDTO(
                        seat.getId(),
                        seat.getSection(),
                        seat.getNumber(),
                        seat.getPosX(),
                        seat.getPosY()
                ))
                .collect(Collectors.toList());
    }
}
