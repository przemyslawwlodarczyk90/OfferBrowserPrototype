package com.offerbrowserprototype.domain.loginaandregister.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChangePasswordDTOTest {

    @Test
    public void testChangePasswordDTO() {
        Long id = 1L;
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setId(id);
        dto.setOldPassword(oldPassword);
        dto.setNewPassword(newPassword);

        assertEquals(id, dto.getId());
        assertEquals(oldPassword, dto.getOldPassword());
        assertEquals(newPassword, dto.getNewPassword());
    }
}