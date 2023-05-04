package net.nvsoftware.iPaymentService.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private long paymentId;
   private long orderId;
   private String paymentMode;
   private String referenceNumber;
   private long totalAmount;
   private Instant paymentDate;
   private String paymentStatus;

}
