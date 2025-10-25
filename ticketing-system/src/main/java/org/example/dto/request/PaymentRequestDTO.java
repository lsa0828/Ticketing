package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.dto.PaymentDetail;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {
    private Long memberId;
    private Long concertId;
    private Long seatId;
    private Long reservationId;
    private int totalAmount;
    private List<PaymentDetail> paymentDetails;
}
