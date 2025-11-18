package org.example.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.dto.SeatDTO;
import org.example.dto.response.ConcertVenueDTO;
import org.example.dto.response.CouponAppliedPriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationFacadeService {
    @Autowired
    private ConcertService concertService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private MemberService memberService;

    @Transactional
    public ReservationData reserve(Long seatId, Long concertId, Long memberId) {
        ConcertVenueDTO concert = concertService.getConcertVenue(concertId);
        SeatDTO seat = reservationService.getSelectedSeat(seatId, concertId, memberId);
        List<CouponAppliedPriceDTO> coupons = couponService.getAvailableCoupon(memberId, seat.getPrice());
        int point = memberService.getPoint(memberId);
        return new ReservationData(concert, seat, point, coupons);
    }

    @Getter
    @AllArgsConstructor
    public static class ReservationData {
        private ConcertVenueDTO concert;
        private SeatDTO seat;
        private int point;
        private List<CouponAppliedPriceDTO> coupons;
    }
}
