package com.hackathon.coffeescheduler.repository;

import com.hackathon.coffeescheduler.model.Barista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaristaRepository extends JpaRepository<Barista, Long> {

    List<Barista> findByActiveTrue();
}
