package com.hackathon.coffeescheduler.service;

import com.hackathon.coffeescheduler.engine.PriorityCalculator;
import com.hackathon.coffeescheduler.engine.QueueManager;
import com.hackathon.coffeescheduler.engine.WorkloadTracker;
import com.hackathon.coffeescheduler.model.*;
import com.hackathon.coffeescheduler.repository.BaristaRepository;
import com.hackathon.coffeescheduler.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final OrderRepository orderRepo;
    private final BaristaRepository baristaRepo;

    private final PriorityCalculator priorityCalc = new PriorityCalculator();
    private final QueueManager queue = new QueueManager();
    private final WorkloadTracker workload = new WorkloadTracker();

    private static final int SECONDS_PER_COMPLEXITY = 10;

    // =========================
    // ORDER INTAKE
    // =========================

    public Order createOrder(Order o) {

        o.setCreatedAt(LocalDateTime.now());
        o.setStatus(OrderStatus.NEW);

        int score = priorityCalc.calculate(o);
        o.setPriorityScore(score);

        Order saved = orderRepo.save(o);

        saved.setStatus(OrderStatus.QUEUED);
        orderRepo.save(saved);

        queue.add(saved);

        return saved;
    }

    public List<Order> createBulkOrders(List<Order> orders) {
        return orders.stream()
                .map(this::createOrder)
                .toList();
    }

    // =========================
    // SCHEDULING
    // =========================

    public String runNextAssignment() {

        Order next = queue.poll();

        if (next == null) {
            return "No orders in queue";
        }

        List<Barista> active = baristaRepo.findByActiveTrue();

        if (active.isEmpty()) {
            queue.add(next);
            return "No active baristas";
        }

        Barista chosen = workload.pickLeastLoaded(active);

        if (chosen == null) {
            queue.add(next);
            return "No eligible barista";
        }

        workload.incrementLoad(chosen);
        baristaRepo.save(chosen);

        next.setStatus(OrderStatus.ASSIGNED);
        orderRepo.save(next);

        return "Assigned order " + next.getId() +
                " to barista " + chosen.getName();
    }

    public int runAllAssignments() {

        int processed = 0;

        while (queue.size() > 0) {
            runNextAssignment();
            processed++;
        }

        return processed;
    }

    // =========================
    // READ VIEWS
    // =========================

    public int queueSize() {
        return queue.size();
    }

    public List<Order> allOrders() {
        return orderRepo.findAll();
    }

    public List<Barista> allBaristas() {
        return baristaRepo.findAll();
    }

    // =========================
    // METRICS + TIME ESTIMATION
    // =========================

    public MetricsView metrics() {

        var orders = orderRepo.findAll();
        var baristas = baristaRepo.findAll();

        long total = orders.size();

        long queued = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.QUEUED)
                .count();

        long assigned = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.ASSIGNED)
                .count();

        double avgPriority = orders.stream()
                .mapToInt(Order::getPriorityScore)
                .average()
                .orElse(0);

        int maxLoad = baristas.stream()
                .mapToInt(Barista::getCurrentLoad)
                .max()
                .orElse(0);

        int minLoad = baristas.stream()
                .mapToInt(Barista::getCurrentLoad)
                .min()
                .orElse(0);

        double spread = maxLoad - minLoad;

        // ✅ processing time simulation
        int estimatedSeconds = maxLoad * SECONDS_PER_COMPLEXITY;
        double estimatedMinutes = estimatedSeconds / 60.0;

        return MetricsView.builder()
                .totalOrders(total)
                .queuedOrders(queued)
                .assignedOrders(assigned)
                .avgPriority(avgPriority)
                .maxLoad(maxLoad)
                .minLoad(minLoad)
                .fairnessSpread(spread)
                .estimatedSeconds(estimatedSeconds)
                .estimatedMinutes(estimatedMinutes)
                .build();

    }
}
