package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
public class ReservedConcertDetailDTO {
    private Long reservationId;
    private String title;
    private LocalDate date;
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

    public String getDateFormatted() {
        return DateTimeFormatter.ofPattern("yyyy년 MM월 dd일").format(date);
    }
}
