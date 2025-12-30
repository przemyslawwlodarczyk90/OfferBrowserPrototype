package com.example.offerbrowserprototype.domain.aplicationnote;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application_notes")
public class ApplicationNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String companyName;


    @Column
    private String url;

    @Column
    private String offerId;


    @Column
    private String offerUrl;


    @Column
    private LocalDateTime appliedAt;
}
