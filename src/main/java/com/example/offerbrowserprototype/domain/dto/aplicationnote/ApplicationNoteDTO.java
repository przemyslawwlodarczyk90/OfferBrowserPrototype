package com.example.offerbrowserprototype.domain.dto.aplicationnote;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationNoteDTO {
    private Long id;
    private Long userId;
    private Long offerId;
    private String offerUrl;
    private String companyName;
    private LocalDateTime appliedAt;
}