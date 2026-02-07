package com.hackathon.coffeescheduler.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "baristas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Barista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int skillLevel;

    private int currentLoad;

    private boolean active;
}
