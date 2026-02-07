package com.hackathon.coffeescheduler.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MetricsView {

    private long totalOrders;
    private long queuedOrders;
    private long assignedOrders;

    private double avgPriority;

    private int maxLoad;
    private int minLoad;
    private double fairnessSpread;

    private int estimatedSeconds;
    private double estimatedMinutes;
}
