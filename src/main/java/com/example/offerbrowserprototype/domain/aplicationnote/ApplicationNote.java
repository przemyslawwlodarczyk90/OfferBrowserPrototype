package com.example.offerbrowserprototype.domain.aplicationnote;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Document(collection = "application_notes")
public class ApplicationNote {
    @Id
    private String id;
    private String userId; 
    private String offerId;
    @Indexed(unique = true)
    private String offerUrl;
    private String companyName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime appliedAt;
}
