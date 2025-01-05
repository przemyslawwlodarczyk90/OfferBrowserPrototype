package com.example.offerbrowserprototype.domain.mapper;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNote;
import com.example.offerbrowserprototype.domain.dto.aplicationnote.ApplicationNoteDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApplicationNoteMapper {

    public ApplicationNoteDTO toDto(ApplicationNote applicationNote) {
        ApplicationNoteDTO dto = new ApplicationNoteDTO();
        dto.setId(applicationNote.getId());
        dto.setOfferId(applicationNote.getOfferId());
        dto.setOfferUrl(applicationNote.getOfferUrl());
        dto.setCompanyName(applicationNote.getCompanyName());
        dto.setAppliedAt(applicationNote.getAppliedAt());
        return dto;
    }

    public List<ApplicationNoteDTO> toDtoList(List<ApplicationNote> applicationNotes) {
        return applicationNotes.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ApplicationNote toEntity(ApplicationNoteDTO dto) {
        ApplicationNote applicationNote = new ApplicationNote();
        applicationNote.setId(dto.getId());
        applicationNote.setOfferId(dto.getOfferId());
        applicationNote.setOfferUrl(dto.getOfferUrl());
        applicationNote.setCompanyName(dto.getCompanyName());
        applicationNote.setAppliedAt(dto.getAppliedAt());
        return applicationNote;
    }
}
