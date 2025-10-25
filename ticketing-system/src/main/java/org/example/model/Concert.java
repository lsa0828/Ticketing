package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Concert {
    private Long id;
    private Long venueId;
    private String title;
    private LocalDate date;
    private String imageUrl;

    public boolean isBookable() {
        return ChronoUnit.DAYS.between(LocalDate.now(), date) >= 0;
    }
}
