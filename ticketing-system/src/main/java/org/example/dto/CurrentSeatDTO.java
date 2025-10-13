package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CurrentSeatDTO {
    private Long seatId;
    private Long concertId;
    private String section;
    private int number;
    private int posX;
    private int posY;
    private String status;
    private int price;
}
