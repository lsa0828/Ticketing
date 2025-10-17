package org.example.controller;

import org.example.dto.response.ConcertVenueDTO;
import org.example.dto.CurrentSeatDTO;
import org.example.service.ConcertService;
import org.example.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ConcertController {
    @Autowired
    private ConcertService concertService;
    @Autowired
    private SeatService seatService;

    @GetMapping("/concert/{concertId}")
    public String showSeatsOfConcert(@PathVariable("concertId") Long concertId, Model model) {
        ConcertVenueDTO concert = concertService.getConcertVenue(concertId);
        model.addAttribute("concert", concert);

        List<CurrentSeatDTO> seats = seatService.getSeatStatusOfConcert(concertId);
        model.addAttribute("seats", seats);

        int maxPosX = seats.stream()
                .mapToInt(CurrentSeatDTO::getPosX)
                .max()
                .orElse(0);
        int maxPosY = seats.stream()
                .mapToInt(CurrentSeatDTO::getPosY)
                .max()
                .orElse(0);

        Map<String, Integer> maxPos = new HashMap<>();
        maxPos.put("x", maxPosX);
        maxPos.put("y", maxPosY);
        model.addAttribute("maxPos", maxPos);

        return "concert";
    }
}
