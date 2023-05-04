package net.nvsoftware.iOrderService.service;

import com.netflix.discovery.converters.Auto;
import lombok.extern.log4j.Log4j2;
import net.nvsoftware.iOrderService.entity.OrderEntity;
import net.nvsoftware.iOrderService.external.client.ProductServiceFeighClient;
import net.nvsoftware.iOrderService.model.OrderRequest;
import net.nvsoftware.iOrderService.repository.OrderRepository;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{
   @Autowired
   private OrderRepository orderRepository;

   @Autowired
   private ProductServiceFeighClient productServiceFeighClient;

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
      return orderEntity.getOrderId();
   }
}
