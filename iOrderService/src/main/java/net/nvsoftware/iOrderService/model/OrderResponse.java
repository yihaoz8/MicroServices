package net.nvsoftware.iOrderService.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
   private long orderId;
   private Instant orderDate;
   private String orderStatus;
   private long totalAmount;
   private ProductResponse productResponse;

   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   @Builder
   public static class ProductResponse {
      private long productId;
      private String productName;
      private long productPrice;
      private long productQuantity;
   }
}
