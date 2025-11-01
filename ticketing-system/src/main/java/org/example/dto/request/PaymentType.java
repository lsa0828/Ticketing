package org.example.dto.request;

import lombok.Getter;

@Getter
public enum PaymentType {
    POINT("포인트"),
    COUPON("쿠폰"),
    IMP("아임포트"),
    KAKAOPAY("kakaopay");

    private final String displayName;

    PaymentType(String displayName) {
        this.displayName = displayName;
    }
}
