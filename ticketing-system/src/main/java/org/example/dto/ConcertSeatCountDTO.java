package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConcertSeatCountDTO {
    private Long concertId;
    private String title;
    private String imageUrl;
    private int soldCount;
    private int totalCount;
}
