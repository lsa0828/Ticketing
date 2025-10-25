package org.example.dto.response;

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
public class PaymentResultDTO {
    private boolean success;
    private String message;
    private int totalAmount;
    private List<PaymentDetail> paymentDetails;
}