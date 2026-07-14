package com.nanakusa.zanshin.service;

import com.nanakusa.zanshin.dto.user.UserDto;
import com.nanakusa.zanshin.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserSeviceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void deberia_validar_y_crear_usuario(){
        // Arrange
        when(userRepository.existsByEmail("johnchaves2023@gmail.com")).thenReturn(false);

        UserDto dto = new UserDto("johnchavesdev", "johnchaves2023@gmail.com", "1234567890", Role.ADMIN);

        // Act
        userService.validateAndCreateUser(dto);

        // Assert
        verify(userRepository).existsByEmail("johnchaves2023@gmail.com");
    }
}
