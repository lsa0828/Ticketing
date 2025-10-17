package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ReservedConcertDTO {
    private Long reservationId;
    private String title;
    private String imageUrl;
    private String venueName;
    private String section;
    private int number;
    private int price;
    private String reservationStatus;
    private LocalDateTime reservedAt;
}
