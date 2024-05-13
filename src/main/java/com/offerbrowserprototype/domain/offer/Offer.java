package com.offerbrowserprototype.domain.offer;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "job_offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;


}