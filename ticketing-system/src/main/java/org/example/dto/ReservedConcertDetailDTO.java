package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
public class ReservedConcertDetailDTO {
    private Long reservationId;
    private String title;
    private String imageUrl;
    private String venueName;
    private String section;
    private int number;
    private int price;
    private String reservationStatus;
    private LocalDateTime reservedAt;

    public String getReservedAtFormatted() {
        if (reservedAt == null) return "";
        return reservedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
