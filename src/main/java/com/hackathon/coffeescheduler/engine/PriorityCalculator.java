package com.hackathon.coffeescheduler.engine;

import com.hackathon.coffeescheduler.model.Order;

import java.time.Duration;
import java.time.LocalDateTime;

public class PriorityCalculator {

    public int calculate(Order order) {
        int base = order.getComplexityScore() * 10;

        if (order.getCreatedAt() == null) {
            return base;
        }

        long waitMinutes = Duration.between(
                order.getCreatedAt(),
                LocalDateTime.now()
        ).toMinutes();

        return base + (int) waitMinutes;
    }
}
