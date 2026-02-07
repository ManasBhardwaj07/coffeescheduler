package com.hackathon.coffeescheduler.controller;

import com.hackathon.coffeescheduler.model.Barista;
import com.hackathon.coffeescheduler.model.MetricsView;
import com.hackathon.coffeescheduler.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class SimulationController {

    private final SchedulerService scheduler;

    @PostMapping("/run")
    public String runOne() {
        return scheduler.runNextAssignment();
    }

    @GetMapping("/queue-size")
    public Map<String, Integer> queueSize() {
        return Map.of("queueSize", scheduler.queueSize());
    }

    @GetMapping("/baristas")
    public List<Barista> baristas() {
        return scheduler.allBaristas();
    }
    @GetMapping("/metrics")
    public MetricsView metrics() {
        return scheduler.metrics();
    }
    @PostMapping("/run-all")
    public String runAll() {
        int n = scheduler.runAllAssignments();
        return "Processed " + n + " orders";
    }


}
