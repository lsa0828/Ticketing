package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Seat {
    private Long id;
    private Long venueId;
    private String section;
    private int number;
    private int posX;
    private int posY;
}
