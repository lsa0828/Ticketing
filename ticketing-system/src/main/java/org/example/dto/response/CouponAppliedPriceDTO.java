package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CouponAppliedPriceDTO {
    private Long memberCouponId;
    private String name;
    private int discountedPrice;
    private LocalDate expiresAt;
}
