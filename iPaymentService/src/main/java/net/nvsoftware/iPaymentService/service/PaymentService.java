package net.nvsoftware.iPaymentService.service;

import net.nvsoftware.iPaymentService.model.PaymentRequest;
import net.nvsoftware.iPaymentService.model.PaymentResponse;
import org.springframework.stereotype.Service;

public interface PaymentService {
   long doPayment(PaymentRequest paymentRequest);

   PaymentResponse getPaymentByOrderId(long orderId);
}
