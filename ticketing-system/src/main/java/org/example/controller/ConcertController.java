package org.example.controller;

import org.example.dto.SeatDTO;
import org.example.model.Concert;
import org.example.model.Venue;
import org.example.service.ConcertService;
import org.example.service.SeatService;
import org.example.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ConcertController {
    @Autowired
    private ConcertService concertService;
    @Autowired
    private VenueService venueService;
    @Autowired
    private SeatService seatService;

    @GetMapping("/concert/{concertId}")
    public String showSeatsOfConcert(@PathVariable("concertId") Long concertId, Model model) {
        Concert concert = concertService.getConcert(concertId);
        model.addAttribute("concert", concert);

        Venue venue = venueService.getVenue(concert.getVenueId());
        model.addAttribute("venue", venue);

        List<SeatDTO> seats = seatService.getSeatsOfVenue(concert.getVenueId());
        model.addAttribute("seats", seats);

        return "concert";
    }
}
