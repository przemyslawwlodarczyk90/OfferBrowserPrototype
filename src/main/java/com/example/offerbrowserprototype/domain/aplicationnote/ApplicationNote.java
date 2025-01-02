package com.example.offerbrowserprototype.domain.aplicationnote;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "application_notes")
public class ApplicationNote {
    @Id
    private String id;
    private String offerId;
    @Indexed(unique = true)
    private String offerUrl;
    private String companyName;
    private LocalDateTime appliedAt;
}
