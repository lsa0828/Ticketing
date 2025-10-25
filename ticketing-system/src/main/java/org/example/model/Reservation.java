package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    private Long id;
    private Long memberId;
    private Long concertId;
    private Long seatId;
    private String status;
    private LocalDateTime reservedAt;
    private LocalDateTime refundedAt;
}
