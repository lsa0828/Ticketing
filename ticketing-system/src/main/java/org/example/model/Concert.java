package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Concert {
    private int id;
    private String title;
    private String imageUrl;
}
