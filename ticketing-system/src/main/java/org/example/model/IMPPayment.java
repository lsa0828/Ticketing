package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IMPPayment {
    private Long reservationId;
    private int paidAmount;
    private String type;
    private String impUid;
}
