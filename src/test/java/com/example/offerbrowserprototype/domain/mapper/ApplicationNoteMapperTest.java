//package com.example.offerbrowserprototype.domain.mapper;
//
//import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNote;
//import com.example.offerbrowserprototype.domain.dto.aplicationnote.ApplicationNoteDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class ApplicationNoteMapperTest {
//
//    private ApplicationNoteMapper applicationNoteMapper;
//
//    @BeforeEach
//    void setUp() {
//        applicationNoteMapper = new ApplicationNoteMapper();
//    }
//
//    @Test
//    void shouldMapEntityToDto() {
//        // Given
//        ApplicationNote note = new ApplicationNote();
//        note.setId("123");
//        note.setOfferId("offer123");
//        note.setOfferUrl("https://example.com/offer123");
//        note.setCompanyName("Example Company");
//        note.setAppliedAt(LocalDateTime.now());
//
//        // When
//        ApplicationNoteDTO dto = applicationNoteMapper.toDto(note);
//
//        // Then
//        assertThat(dto).isNotNull();
//        assertThat(dto.getId()).isEqualTo(note.getId());
//        assertThat(dto.getOfferId()).isEqualTo(note.getOfferId());
//        assertThat(dto.getOfferUrl()).isEqualTo(note.getOfferUrl());
//        assertThat(dto.getCompanyName()).isEqualTo(note.getCompanyName());
//        assertThat(dto.getAppliedAt()).isEqualTo(note.getAppliedAt());
//    }
//
//    @Test
//    void shouldMapEntityListToDtoList() {
//        // Given
//        ApplicationNote note1 = new ApplicationNote();
//        note1.setId("123");
//        note1.setOfferId("offer123");
//        note1.setOfferUrl("https://example.com/offer123");
//        note1.setCompanyName("Company A");
//        note1.setAppliedAt(LocalDateTime.now());
//
//        ApplicationNote note2 = new ApplicationNote();
//        note2.setId("456");
//        note2.setOfferId("offer456");
//        note2.setOfferUrl("https://example.com/offer456");
//        note2.setCompanyName("Company B");
//        note2.setAppliedAt(LocalDateTime.now());
//
//        List<ApplicationNote> notes = Arrays.asList(note1, note2);
//
//        // When
//        List<ApplicationNoteDTO> dtos = applicationNoteMapper.toDtoList(notes);
//
//        // Then
//        assertThat(dtos).isNotNull();
//        assertThat(dtos).hasSize(2);
//        assertThat(dtos.get(0).getId()).isEqualTo(note1.getId());
//        assertThat(dtos.get(1).getId()).isEqualTo(note2.getId());
//    }
//
//    @Test
//    void shouldMapDtoToEntity() {
//        // Given
//        ApplicationNoteDTO dto = new ApplicationNoteDTO();
//        dto.setId("123");
//        dto.setOfferId("offer123");
//        dto.setOfferUrl("https://example.com/offer123");
//        dto.setCompanyName("Example Company");
//        dto.setAppliedAt(LocalDateTime.now());
//
//        // When
//        ApplicationNote note = applicationNoteMapper.toEntity(dto);
//
//        // Then
//        assertThat(note).isNotNull();
//        assertThat(note.getId()).isEqualTo(dto.getId());
//        assertThat(note.getOfferId()).isEqualTo(dto.getOfferId());
//        assertThat(note.getOfferUrl()).isEqualTo(dto.getOfferUrl());
//        assertThat(note.getCompanyName()).isEqualTo(dto.getCompanyName());
//        assertThat(note.getAppliedAt()).isEqualTo(dto.getAppliedAt());
//    }
//
//    @Test
//    void shouldHandleEmptyListForDtoListMapping() {
//        // When
//        List<ApplicationNoteDTO> dtos = applicationNoteMapper.toDtoList(List.of());
//
//        // Then
//        assertThat(dtos).isNotNull();
//        assertThat(dtos).isEmpty();
//    }
//}
