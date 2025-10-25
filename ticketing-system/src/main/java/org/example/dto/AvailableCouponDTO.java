package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class AvailableCouponDTO {
    private Long memberCouponId;
    private String name;
    private int discountRate;
    private int discountAmount;
    private LocalDate expiresAt;
}
