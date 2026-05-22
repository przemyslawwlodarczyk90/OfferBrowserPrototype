package com.example.offerbrowserprototype.domain.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private boolean active;
    private long appliedCount;
    private long watchlistCount;
    private long uselessCount;
    private long notesCount;
}
