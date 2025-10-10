package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConcertVenueDTO {
    private Long id;
    private String venueName;
    private String title;
    private String imageUrl;
}
