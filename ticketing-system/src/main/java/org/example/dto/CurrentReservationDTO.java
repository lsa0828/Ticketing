package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CurrentReservationDTO {
    private Long seatId;
    private Long concertId;
    private Long memberId;
    private String section;
    private int number;
    private String status;
    private LocalDateTime expiresAt;
}
