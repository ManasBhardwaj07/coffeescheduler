package com.hackathon.coffeescheduler.controller;

import com.hackathon.coffeescheduler.model.DrinkType;
import com.hackathon.coffeescheduler.model.Order;
import com.hackathon.coffeescheduler.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final SchedulerService scheduler;

    @PostMapping
    public Order create(@RequestBody Order order) {
        return scheduler.createOrder(order);
    }

    @PostMapping("/bulk")
    public List<Order> bulk(@RequestBody List<Order> orders) {
        return scheduler.createBulkOrders(orders);
    }

    @GetMapping
    public List<Order> list() {
        return scheduler.allOrders();
    }
    @PostMapping("/simulate/peak")
    public String simulatePeak() {

        int total = 200;

        var drinks = DrinkType.values();

        for (int i = 0; i < total; i++) {

            Order o = new Order();
            o.setCustomerName("PeakUser" + i);
            o.setDrinkType(drinks[i % drinks.length]);
            o.setComplexityScore((i % 10) + 1);

            scheduler.createOrder(o);
        }

        return "Generated " + total + " peak-hour orders";
    }

}
