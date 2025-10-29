package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
    private Long id;
    private String name;
    private int discountRate;
    private int discountAmount;
    private LocalDate expiresAt;
}
