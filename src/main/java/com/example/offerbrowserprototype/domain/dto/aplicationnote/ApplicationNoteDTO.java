package com.example.offerbrowserprototype.domain.dto.aplicationnote;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationNoteDTO {
    private String id;
    private String offerId;
    private String offerUrl;
    private String companyName;
    private LocalDateTime appliedAt;
}
