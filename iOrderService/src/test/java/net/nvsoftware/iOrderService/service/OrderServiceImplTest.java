package net.nvsoftware.iOrderService.service;

import net.nvsoftware.iOrderService.entity.OrderEntity;
import net.nvsoftware.iOrderService.external.client.PaymentServiceFeignClient;
import net.nvsoftware.iOrderService.external.client.ProductServiceFeighClient;
import net.nvsoftware.iOrderService.model.OrderResponse;
import net.nvsoftware.iOrderService.model.PaymentMode;
import net.nvsoftware.iOrderService.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceImplTest {
   @Mock
   RestTemplate restTemplate;

   @Mock
   private OrderRepository orderRepository;

   @Mock
   private ProductServiceFeighClient productServiceFeighClient;

   @Mock
   private PaymentServiceFeignClient paymentServiceFeignClient;

   @InjectMocks
   OrderService orderService = new OrderServiceImpl();

   @DisplayName("Get Order Detail Success")
   @Test
   void testWhenGetOrderSuccess() {
      // Mock Part
      OrderEntity orderEntity = getMockOrderEntity();
      Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(orderEntity));

      Mockito.when(restTemplate.getForObject(
            "http://PRODUCT-SERVICE/products/" + orderEntity.getProductId(),
            OrderResponse.ProductResponse.class
      )).thenReturn(getMockProductResponse());

      Mockito.when(restTemplate.getForObject(
            "http://PAYMENT-SERVICE/payments/" + orderEntity.getOrderId(),
            OrderResponse.PaymentResponse.class
      )).thenReturn(getMockPaymentResponse());

      // Actual Call
      OrderResponse orderResponse = orderService.getOrderDetailByOrderId(2);

      // Verify Call
      Mockito.verify(orderRepository, Mockito.times(1)).findById(Mockito.anyLong());
      Mockito.verify(restTemplate, Mockito.times(1)).getForObject(
            "http://PRODUCT-SERVICE/products/" + orderEntity.getProductId(),
            OrderResponse.ProductResponse.class
      );
      Mockito.verify(restTemplate, Mockito.times(1)).getForObject(
            "http://PAYMENT-SERVICE/payments/" + orderEntity.getOrderId(),
            OrderResponse.PaymentResponse.class
      );
      // Assert Response
      Assertions.assertNotNull(orderResponse);
      Assertions.assertEquals(orderEntity.getOrderId(), orderResponse.getOrderId());

   }

   private OrderEntity getMockOrderEntity() {
      return OrderEntity.builder()
            .orderId(1)
            .productId(3)
            .orderQuantity(1)
            .totalAmount(1299)
            .orderDate(Instant.now())
            .orderStatus("PLACED")
            .build();
   }

   private OrderResponse.PaymentResponse getMockPaymentResponse() {
      return OrderResponse.PaymentResponse.builder()
            .orderId(1)
            .paymentId(1)
            .paymentDate(Instant.now())
            .paymentMode(PaymentMode.CASH)
            .paymentStatus("SUCCESS")
            .totalAmount(2599)
            .build();
   }

   private OrderResponse.ProductResponse getMockProductResponse() {
      return OrderResponse.ProductResponse.builder()
            .productId(2)
            .productName("MacMini")
            .productPrice(1299)
            .productQuantity(2)
            .build();
   }
}