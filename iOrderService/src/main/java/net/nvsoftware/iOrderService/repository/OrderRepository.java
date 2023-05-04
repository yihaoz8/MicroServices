package net.nvsoftware.iOrderService.repository;

import net.nvsoftware.iOrderService.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
