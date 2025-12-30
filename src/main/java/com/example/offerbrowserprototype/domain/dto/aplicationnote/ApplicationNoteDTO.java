package com.example.offerbrowserprototype.domain.dto.aplicationnote;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationNoteDTO {
    private Long id;
    private String userId;
    private String offerId;
    private String offerUrl;
    private String companyName;
    private String url;
    private LocalDateTime appliedAt;
}
