package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.model.Concert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@AllArgsConstructor
public class ConcertVenueDTO {
    private Long id;
    private String venueName;
    private String title;
    private LocalDate date;
    private String imageUrl;

    public String getDateFormatted() {
        return DateTimeFormatter.ofPattern("yyyy년 MM월 dd일").format(date);
    }

    public boolean getBookable() {
        return ChronoUnit.DAYS.between(LocalDate.now(), date) >= 0;
    }
}
