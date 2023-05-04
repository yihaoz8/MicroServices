package net.nvsoftware.iOrderService.external.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PAYMENT-SERVICE/payments")
public interface PaymentServiceFeignClient {
   @PostMapping("/doPayment")
   public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);
}
