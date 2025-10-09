package org.example.controller;

import org.example.dto.ConcertSeatStatusCountDTO;
import org.example.service.ConcertService;
import org.example.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private ConcertService concertService;
    @Autowired
    private SeatService seatService;

    @GetMapping("/")
    public String mainPage(Model model) {
        List<ConcertSeatStatusCountDTO> concerts = concertService.getAllConcertsStatusCount();
        model.addAttribute("concerts", concerts);
        return "main";
    }
}