package net.nvsoftware.iOrderService.external.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.nvsoftware.iOrderService.model.PaymentMode;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
   private long orderId;
   private PaymentMode paymentMode;
   private String referenceNumber;
   private long totalAmount;
}
