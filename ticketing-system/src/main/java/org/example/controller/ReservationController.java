package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.dto.ReservedConcertDTO;
import org.example.dto.ReservedConcertDetailDTO;
import org.example.dto.response.MemberResponseDTO;
import org.example.dto.request.PaymentRequestDTO;
import org.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private ReservationFacadeService reservationFacadeService;

    @GetMapping("/reserve")
    public String reserveSeat(@RequestParam("seatId") Long seatId, @RequestParam("concertId") Long concertId,
                              HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        HttpSession session = request.getSession(false);
        MemberResponseDTO member = (MemberResponseDTO) session.getAttribute("loginMember");

        try {
            ReservationFacadeService.ReservationData data = reservationFacadeService.reserve(seatId, concertId, member.getId());
            model.addAttribute("concert", data.getConcert());
            model.addAttribute("seat", data.getSeat());
            model.addAttribute("point", data.getPoint());
            model.addAttribute("coupons", data.getCoupons());
            return "reserve";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        } catch (OptimisticLockingFailureException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/concert/" + concertId;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("error", "예매 중 오류가 발생했습니다.");
            return "redirect:/concert/" + concertId;
        }
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> reserveAfterPay(@RequestBody PaymentRequestDTO paymentRequest, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        MemberResponseDTO member = (MemberResponseDTO) session.getAttribute("loginMember");

        paymentRequest.setMemberId(member.getId());

        try {
            Long reservationId = reservationService.reserve(paymentRequest);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("reservationId", reservationId));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", e.getMessage()));
        } catch (OptimisticLockingFailureException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            System.out.println("컨틀롤러 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "Unexpected error occurred"));
        }
    }

    @GetMapping("/refund")
    public String refund(@RequestParam("reservationId") Long reservationId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        MemberResponseDTO member = (MemberResponseDTO) session.getAttribute("loginMember");

        try {
            String message = reservationService.refund(member.getId(), reservationId);
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/reservation/all";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reservation/complete?reservationId=" + reservationId;
        }
    }

    @GetMapping("/reservation/complete")
    public String reservationDetailPage(@RequestParam("reservationId") Long reservationId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        MemberResponseDTO member = (MemberResponseDTO) session.getAttribute("loginMember");

        ReservedConcertDetailDTO reservedConcert = reservationService.getReservation(reservationId, member.getId());
        model.addAttribute("reservedConcert", reservedConcert);
        return "reservationDetail";
    }

    @GetMapping("/reservation/all")
    public String reservationListPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        MemberResponseDTO member = (MemberResponseDTO) session.getAttribute("loginMember");

        List<ReservedConcertDTO> reservedConcerts = reservationService.getReservationList(member.getId());
        model.addAttribute("reservedConcerts", reservedConcerts);
        return "reservationList";
    }
}
