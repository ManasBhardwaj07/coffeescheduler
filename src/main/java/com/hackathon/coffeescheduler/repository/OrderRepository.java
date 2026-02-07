package com.hackathon.coffeescheduler.repository;

import com.hackathon.coffeescheduler.model.Order;
import com.hackathon.coffeescheduler.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(OrderStatus status);
}
