package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@AllArgsConstructor
public class ConcertSeatCountDTO {
    private Long concertId;
    private String title;
    private LocalDate date;
    private String imageUrl;
    private int soldCount;
    private int totalCount;

    public Long getDaysUntilConcert() {
        return ChronoUnit.DAYS.between(LocalDate.now(), date);
    }
}
