package org.example.service.payment;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.example.dao.IMPPaymentDAO;
import org.example.dto.PaymentDetail;
import org.example.dto.request.PaymentRequestDTO;
import org.example.dto.request.PaymentType;
import org.example.dto.response.PaymentResultDTO;
import org.example.model.IMPPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class IMPPaymentService implements PaymentStrategy {
    @Autowired
    private IMPPaymentDAO IMPPaymentDAO;

    private final IamportClient iamportClient;

    public IMPPaymentService(@Value("${iamport.api.key}") String restApiKey,
                             @Value("${iamport.api.secret}") String restApiSecret) {
        this.iamportClient = new IamportClient(restApiKey, restApiSecret);
    }

    @Override
    public PaymentResultDTO pay(PaymentRequestDTO request) {
        try {
            PaymentDetail detail = request.getPaymentDetails().getFirst();
            String impUid = detail.getExtraData().get("impUid").toString();
            IamportResponse<Payment> paymentIamportResponse = iamportClient.paymentByImpUid(impUid);
            System.out.println("지나가기");
            Payment payment = paymentIamportResponse.getResponse();

            int amount = detail.getAmount();
            BigDecimal paidAmount = payment.getAmount();
            BigDecimal expectedAmount = new BigDecimal(amount);

            if (paidAmount.compareTo(expectedAmount) != 0) {
                throw new IllegalStateException("결제 금액 불일치: expected=" + expectedAmount + ", actual=" + paidAmount);
            }

            int updated = IMPPaymentDAO.save(request.getReservationId(), amount, String.valueOf(PaymentType.KAKAOPAY), impUid);
            if (updated != 1) {
                throw new IllegalStateException("DB 저장 실패");
            }

            int remainingAmount = request.getTotalAmount() - amount;
            return new PaymentResultDTO(true, "아임포트 결제 성공", remainingAmount, List.of(detail));
        } catch (IamportResponseException | IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("아임포트 결제 처리 중 오류 발생", e);
        }
    }

    @Override
    public boolean refund(Long memberId, Long reservationId) {
        Optional<IMPPayment> impPayment = IMPPaymentDAO.findByReservationId(reservationId);
        if (impPayment.isPresent()) {
            try {
                iamportClient.cancelPaymentByImpUid(
                        new CancelData(impPayment.get().getImpUid(), true)
                );
            } catch (IamportResponseException | IOException e) {
                throw new RuntimeException("아임포트 환불 요청 중 오류 발생", e);
            }
        }
        return true;
    }
}
