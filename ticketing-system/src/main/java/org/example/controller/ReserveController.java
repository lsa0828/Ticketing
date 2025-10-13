package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.dto.ConcertVenueDTO;
import org.example.dto.MemberResponseDTO;
import org.example.dto.SeatDTO;
import org.example.service.ConcertService;
import org.example.service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReserveController {
    @Autowired
    private ReserveService reserveService;
    @Autowired
    private ConcertService concertService;

    @GetMapping("/reserve")
    public String reserveSeat(@RequestParam("seatId") Long seatId, @RequestParam("concertId") Long concertId,
                              HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }
        MemberResponseDTO member = (MemberResponseDTO) session.getAttribute("loginMember");
        if (member == null) {
            return "redirect:/login";
        }

        try {
            SeatDTO seat = reserveService.updateSeatReservation(seatId, concertId, member.getId());
            ConcertVenueDTO concert = concertService.getConcertVenue(concertId);
            model.addAttribute("concert", concert);
            model.addAttribute("seat", seat);
            return "reserve";
        } catch (OptimisticLockingFailureException e) {
            redirectAttributes.addFlashAttribute("error", "이미 선택된 좌석입니다.");
            return "redirect:/concert/" + concertId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "예매 중 오류가 발생했습니다.");
            return "redirect:/concert/" + concertId;
        }
    }
}
