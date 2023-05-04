package net.nvsoftware.iOrderService.service;

import lombok.extern.log4j.Log4j2;
import net.nvsoftware.iOrderService.entity.OrderEntity;
import net.nvsoftware.iOrderService.external.client.PaymentRequest;
import net.nvsoftware.iOrderService.external.client.PaymentServiceFeignClient;
import net.nvsoftware.iOrderService.external.client.ProductServiceFeighClient;
import net.nvsoftware.iOrderService.model.OrderRequest;
import net.nvsoftware.iOrderService.model.OrderResponse;
import net.nvsoftware.iOrderService.repository.OrderRepository;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{
   @Autowired
   private OrderRepository orderRepository;

   @Autowired
   private ProductServiceFeighClient productServiceFeighClient;

   @Autowired
   private PaymentServiceFeignClient paymentServiceFeign;

   @Autowired
   private RestTemplate restTemplate;

   @Override
   public long placeOrder(OrderRequest orderRequest) {//TODO: make this method as transaction

      //call orderservice(this) to create order enriry with status CREATED, save to orderdb
      log.info("orderService placeOrder - save to orderdb start");
      OrderEntity orderEntity = OrderEntity.builder()
            .productId(orderRequest.getProductId())
            .orderQuantity(orderRequest.getOrderQuantity())
            .totalAmount(orderRequest.getTotalAmount())
            .orderDate(Instant.now())
            .orderStatus("CREATED")
            .paymentMode(orderRequest.getPaymentMode().name())
            .build();
      orderEntity = orderRepository.save(orderEntity);
      log.info("orderService placeOrder - save to orderdb done");

      //call productService to check quantity and reduce quantity if ok
      log.info("ProductServiceFeignClient reduceQuantity start");
      productServiceFeighClient.reduceQuantity(orderRequest.getProductId(), orderRequest.getOrderQuantity());
      log.info("ProductServiceFeignClient reduceQuantity Done");
      //call paymentService to charge paymentMode, mark order COMPLETED if success, otherwise mark CANCELED
      log.info("PaymentServiceFeignClient doPayment start");
      PaymentRequest paymentRequest = PaymentRequest.builder()
            .orderId(orderEntity.getOrderId())
            .paymentMode(orderRequest.getPaymentMode())
            .totalAmount(orderRequest.getTotalAmount())
            .build();

      String orderStatus = null;
      try{
         paymentServiceFeign.doPayment(paymentRequest);
         orderStatus = "PLACED";
      } catch (Exception e) {
         orderStatus = "PAYMENT_FAILED";
      }

      orderEntity.setOrderStatus(orderStatus);
      orderRepository.save(orderEntity);
      log.info("PaymentServiceFeignClient doPayment Done");
      return orderEntity.getOrderId();
   }

   @Override
   public OrderResponse getOrderDetailByOrderId(long orderId) {
      log.info("OrderService getOrderDetailByOrderId start");
      OrderEntity orderEntity = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("OrderService getOrderDetailByOrderId Not Found for: " + orderId));

      log.info("OrderService RestCall ProductService getByProductById: " + orderId);
      OrderResponse.ProductResponse productResponse = restTemplate.getForObject(
            "http://PRODUCT-SERVICE/products/" + orderEntity.getProductId(),
            OrderResponse.ProductResponse.class
      );

      log.info("OrderService: RestTemplate call for paymentDetailByOrderId: " + orderEntity.getOrderId());
      OrderResponse.PaymentResponse paymentResponse = restTemplate.getForObject(
            "http://PAYMENT-SERVICE/payments/" + orderEntity.getOrderId(),
            OrderResponse.PaymentResponse.class
      );

      log.info("OrderService RestCall ProductService getById start with orderId: " + orderEntity.getProductId());
      OrderResponse orderResponse = OrderResponse.builder()
            .orderId(orderEntity.getOrderId())
            .totalAmount(orderEntity.getTotalAmount())
            .orderDate(orderEntity.getOrderDate())
            .orderStatus(orderEntity.getOrderStatus())
            .productResponse(productResponse)
            .paymentResponse(paymentResponse)
            .build();



      log.info("OrderService getOrderDetailByOrderId Done");
      return orderResponse;
   }
}
