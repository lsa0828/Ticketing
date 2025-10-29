package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@AllArgsConstructor
public class ReservedConcertDTO {
    private Long reservationId;
    private String title;
    private LocalDate date;
    private String reservationStatus;
    private LocalDateTime reservedAt;
    private LocalDateTime refundedAt;

    public String getReservedAtFormatted() {
        if (reservedAt == null) return "";
        return reservedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getRefundedAtFormatted() {
        if (refundedAt == null) return "";
        return refundedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public Long getDaysUntilConcert() {
        return ChronoUnit.DAYS.between(LocalDate.now(), date);
    }
}
