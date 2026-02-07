package com.hackathon.coffeescheduler.engine;

import com.hackathon.coffeescheduler.model.Barista;

import java.util.Comparator;
import java.util.List;

public class WorkloadTracker {

    public Barista pickLeastLoaded(List<Barista> baristas) {
        return baristas.stream()
                .min(Comparator.comparingInt(Barista::getCurrentLoad))
                .orElse(null);
    }

    public void incrementLoad(Barista b) {
        b.setCurrentLoad(b.getCurrentLoad() + 1);
    }

    public void decrementLoad(Barista b) {
        b.setCurrentLoad(Math.max(0, b.getCurrentLoad() - 1));
    }
}
