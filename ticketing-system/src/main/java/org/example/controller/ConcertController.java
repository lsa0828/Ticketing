package org.example.controller;

import org.example.dto.response.ConcertVenueDTO;
import org.example.dto.CurrentSeatDTO;
import org.example.service.ConcertService;
import org.example.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String showSeatsOfConcert(@PathVariable("concertId") Long concertId, Model model,
                                     RedirectAttributes redirectAttributes) {
        ConcertVenueDTO concert = concertService.getConcertVenue(concertId);
        if (!concert.getBookable()) {
            redirectAttributes.addFlashAttribute("error", "이미 예매가 마감된 공연입니다.");
            return "redirect:/";
        }
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

    @GetMapping("/api/test/concert/{concertId}")
    public ResponseEntity<?> testShowSeatsOfConcert(@PathVariable("concertId") Long concertId) {
        ConcertVenueDTO concert = concertService.getConcertVenue(concertId);
        if (!concert.getBookable()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "이미 예매가 마감된 공연입니다."));
        }
        List<CurrentSeatDTO> seats = seatService.getSeatStatusOfConcert(concertId);

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

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("concert", concert,
                        "seats", seats,
                        "maxPos", maxPos
                ));
    }
}
