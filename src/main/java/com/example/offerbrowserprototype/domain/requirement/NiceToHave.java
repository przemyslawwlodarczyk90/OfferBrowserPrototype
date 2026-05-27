package com.example.offerbrowserprototype.domain.requirement;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nice_to_have")
public class NiceToHave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "skill")
    private String skill;

    @Column(name = "source")
    private String source;

    @Column(name = "level")
    private String level;
}
