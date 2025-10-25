package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    private Long id;
    private Long venueId;
    private String section;
    private int number;
    private int posX;
    private int posY;
}
