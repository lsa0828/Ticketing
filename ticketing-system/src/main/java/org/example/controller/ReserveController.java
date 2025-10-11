package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.dto.ConcertSeatDTO;
import org.example.dto.ConcertVenueDTO;
import org.example.dto.MemberResponseDTO;
import org.example.service.ConcertService;
import org.example.service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReserveController {
    @Autowired
    private ReserveService reserveService;
    @Autowired
    private ConcertService concertService;

    @GetMapping("/reserve") // status booking 으로 변경
    public String reserveSeat(@RequestParam("seatId") Long seatId, @RequestParam("concertId") Long concertId,
                              HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }
        MemberResponseDTO member = (MemberResponseDTO) session.getAttribute("loginMember");
        if (member == null) {
            return "redirect:/login";
        }

        ConcertSeatDTO seat = reserveService.checkSeat(seatId, concertId, member.getId());
        if (seat != null) {
            ConcertVenueDTO concert = concertService.getConcertVenue(concertId);
            model.addAttribute("concert", concert);
            model.addAttribute("seat", seat);
            return "reserve";
        } else {
            return "redirect:/concert/" + concertId.toString();
        }
    }
}
