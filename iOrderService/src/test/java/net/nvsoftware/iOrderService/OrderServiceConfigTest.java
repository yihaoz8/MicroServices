package net.nvsoftware.iOrderService;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class OrderServiceConfigTest {

   public ServiceInstanceListSupplier supplier() {
      return new ServiceInstanceListSupplierTest();
   }
}
