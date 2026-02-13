package com.example.offerbrowserprototype.domain.mapper;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNote;
import com.example.offerbrowserprototype.domain.dto.aplicationnote.ApplicationNoteDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationNoteMapper {

    public ApplicationNoteDTO toDto(ApplicationNote note) {
        ApplicationNoteDTO dto = new ApplicationNoteDTO();
        dto.setId(note.getId());
        dto.setUserId(note.getUserId());
        dto.setOfferId(note.getOfferId());
        dto.setOfferUrl(note.getOfferUrl());
        dto.setCompanyName(note.getCompanyName());
        dto.setAppliedAt(note.getAppliedAt());
        return dto;
    }

    public List<ApplicationNoteDTO> toDtoList(List<ApplicationNote> notes) {
        return notes.stream()
                .map(this::toDto)
                .toList(); // ⬅️ Java 16+ (zamiast collect)
    }

    public ApplicationNote toEntity(ApplicationNoteDTO dto) {
        return ApplicationNote.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .offerId(dto.getOfferId())
                .offerUrl(dto.getOfferUrl())
                .companyName(dto.getCompanyName())
                .appliedAt(dto.getAppliedAt())
                .build();
    }
}