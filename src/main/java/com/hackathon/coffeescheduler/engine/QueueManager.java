package com.hackathon.coffeescheduler.engine;

import com.hackathon.coffeescheduler.model.Order;

import java.util.Comparator;
import java.util.PriorityQueue;

public class QueueManager {

    private final PriorityQueue<Order> queue =
            new PriorityQueue<>(Comparator.comparingInt(Order::getPriorityScore).reversed());

    public void add(Order order) {
        queue.add(order);
    }

    public Order poll() {
        return queue.poll();
    }

    public int size() {
        return queue.size();
    }

    public PriorityQueue<Order> snapshot() {
        return new PriorityQueue<>(queue);
    }
}
