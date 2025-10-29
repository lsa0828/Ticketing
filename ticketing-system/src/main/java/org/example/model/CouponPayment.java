package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CouponPayment {
    private Long reservationId;
    private Long memberCouponId;
    private int originalPrice;
    private int discountedPrice;
}
