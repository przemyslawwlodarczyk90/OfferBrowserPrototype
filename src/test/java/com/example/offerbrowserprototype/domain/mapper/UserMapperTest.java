//package com.example.offerbrowserprototype.domain.mapper;
//
//import com.example.offerbrowserprototype.domain.dto.loginandregister.RegisterUserDTO;
//import com.example.offerbrowserprototype.domain.dto.loginandregister.UpdateUserDto;
//import com.example.offerbrowserprototype.domain.dto.user.UserDTO;
//import com.example.offerbrowserprototype.domain.user.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//class UserMapperTest {
//
//    private UserMapper userMapper;
//
//
//    @BeforeEach
//    void setUp() {
//        userMapper = new UserMapper();
//    }
//
//
//    @Test
//    void shouldMapUserToUserDTO() {
//        // Given
//        User user = new User();
//        user.setId("123");
//        user.setUsername("waldek_kiepski");
//        user.setEmail("waldek@example.com");
//
//        // When
//        UserDTO userDTO = userMapper.toDTO(user);
//
//        // Then
//        assertEquals(user.getId(), userDTO.getId());
//        assertEquals(user.getUsername(), userDTO.getUsername());
//        assertEquals(user.getEmail(), userDTO.getEmail());
//    }
//
//
//    @Test
//    void shouldMapRegisterUserDTOToUser() {
//        // Given
//        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
//        registerUserDTO.setUsername("waldek_kiepski");
//        registerUserDTO.setEmail("waldek@example.com");
//        registerUserDTO.setPassword("password123");
//        String hashedPassword = "hashedPassword123";
//
//        // When
//        User user = userMapper.toEntity(registerUserDTO, hashedPassword);
//
//        // Then
//        assertEquals(registerUserDTO.getUsername(), user.getUsername());
//        assertEquals(registerUserDTO.getEmail(), user.getEmail());
//        assertEquals(hashedPassword, user.getPassword());
//    }
//
//    @Test
//    void shouldUpdateUserFromDto() {
//        // Given
//        UpdateUserDto updateUserDto = new UpdateUserDto();
//        updateUserDto.setUsername("new_username");
//        updateUserDto.setEmail("new_email@example.com");
//
//        User user = new User();
//        user.setUsername("old_username");
//        user.setEmail("old_email@example.com");
//
//        // When
//        userMapper.updateUserFromDto(updateUserDto, user);
//
//        // Then
//        assertEquals(updateUserDto.getUsername(), user.getUsername());
//        assertEquals(updateUserDto.getEmail(), user.getEmail());
//    }
//
//    @Test
//    void shouldNotUpdateUserWhenFieldsAreEmptyInUpdateUserDto() {
//        // Given
//        UpdateUserDto updateUserDto = new UpdateUserDto();
//        updateUserDto.setUsername("");
//        updateUserDto.setEmail(null);
//
//        User user = new User();
//        user.setUsername("old_username");
//        user.setEmail("old_email@example.com");
//
//        // When
//        userMapper.updateUserFromDto(updateUserDto, user);
//
//        // Then
//        assertEquals("old_username", user.getUsername());
//        assertEquals("old_email@example.com", user.getEmail());
//    }
//}
