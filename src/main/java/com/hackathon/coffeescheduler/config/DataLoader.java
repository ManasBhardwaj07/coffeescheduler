package com.hackathon.coffeescheduler.config;

import com.hackathon.coffeescheduler.model.Barista;
import com.hackathon.coffeescheduler.repository.BaristaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final BaristaRepository repo;

    @Override
    public void run(String... args) {
        if (repo.count() == 0) {
            repo.save(Barista.builder().name("Asha").skillLevel(5).currentLoad(0).active(true).build());
            repo.save(Barista.builder().name("Ravi").skillLevel(3).currentLoad(0).active(true).build());
            repo.save(Barista.builder().name("Neha").skillLevel(4).currentLoad(0).active(true).build());
        }
    }
}
