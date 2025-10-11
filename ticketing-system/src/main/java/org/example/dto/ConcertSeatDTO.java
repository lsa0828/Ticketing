package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConcertSeatDTO {
    private Long seatId;
    private Long concertId;
    private String section;
    private int number;
}
