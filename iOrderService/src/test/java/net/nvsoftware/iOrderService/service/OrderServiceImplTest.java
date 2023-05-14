package net.nvsoftware.iOrderService.service;

import net.nvsoftware.iOrderService.external.client.PaymentServiceFeignClient;
import net.nvsoftware.iOrderService.external.client.ProductServiceFeighClient;
import net.nvsoftware.iOrderService.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

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

      // Actual Call
      orderService.getOrderDetailByOrderId(2);
      // Verify Call

      // Assert Response


   }
}