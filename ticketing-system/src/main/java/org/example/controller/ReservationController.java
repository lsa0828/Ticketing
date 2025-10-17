package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.dto.ReservedConcertDTO;
import org.example.dto.response.ConcertVenueDTO;
import org.example.dto.response.MemberResponseDTO;
import org.example.dto.request.PayRequestDTO;
import org.example.dto.SeatDTO;
import org.example.service.ConcertService;
import org.example.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ConcertService concertService;

    @GetMapping("/reserve")
    public String reserveSeat(@RequestParam("seatId") Long seatId, @RequestParam("concertId") Long concertId,
                              HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        HttpSession session = request.getSession(false);
        MemberResponseDTO member = (MemberResponseDTO) session.getAttribute("loginMember");

        try {
            SeatDTO seat = reservationService.updateSeatReservation(seatId, concertId, member.getId());
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

    @PostMapping("/reserve")
    public ResponseEntity<?> reserveAfterPay(@RequestBody PayRequestDTO payRequest, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        MemberResponseDTO member = (MemberResponseDTO) session.getAttribute("loginMember");

        try {
            Long reservationId = reservationService.payAndReserve(payRequest, member.getId());
            return ResponseEntity.ok(Map.of("reservationId", reservationId));
        } catch (OptimisticLockingFailureException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error occurred"));
        }
    }

    @GetMapping("/reserve/complete")
    public String reservationDetailPage(@RequestParam("reservationId") Long reservationId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        MemberResponseDTO member = (MemberResponseDTO) session.getAttribute("loginMember");

        ReservedConcertDTO reservedConcert = reservationService.getReservation(reservationId, member.getId());
        model.addAttribute("reservedConcert", reservedConcert);
        return "reservationDetail";
    }

    @GetMapping("/reserve/all")
    public String reservationListPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        MemberResponseDTO member = (MemberResponseDTO) session.getAttribute("loginMember");

        List<ReservedConcertDTO> reservedConcerts = reservationService.getReservationList(member.getId());
        model.addAttribute("reservedConcerts", reservedConcerts);
        return "reservationList";
    }
}
