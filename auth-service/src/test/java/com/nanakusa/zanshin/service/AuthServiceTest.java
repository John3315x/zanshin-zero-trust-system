package com.nanakusa.zanshin.service;

import com.nanakusa.zanshin.dto.AuthResponse;
import com.nanakusa.zanshin.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    // LOTE: 1
    @Mock
    UserRepository userRepository;
    @Mock
    SessionService sessionService;
    @Mock
    IPService ipService;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    HttpServletRequest httpServletRequest;
    @InjectMocks
    AuthService authService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void deberia_loguear_usuario_valido(){
        // Arrange
        User user = new User();
        user.setEmail("koneko@test.com");
        user.setPassword_hash("$2a$10$k/hm6MqKtR5x27Zcj0ZlMeCdSQx3JcQ.k1JPDeKDf8QYD0JKxETsi");
        when(userRepository.findByEmail("koneko@test.com")).thenReturn(Optional.of(user));

        when(bCryptPasswordEncoder.matches("1234567890", user.getPassword_hash())).thenReturn(true);

        // Act
        AuthResponse authResponse = authService.login("koneko@test.com", "1234567890", httpServletRequest);

        // Assert
        //System.out.println(authResponse);
        verify(userRepository).findByEmail("koneko@test.com");
    }

    @Test
    void deberia_lanzar_exepcion_con_usuario_invalido(){
        // Arrange
        //User user = new User();
        //user.setEmail("koneko@test.com");
        //user.setPassword_hash("$2a$10$k/hm6MqKtR5x27Zcj0ZlMeCdSQx3JcQ.k1JPDeKDf8QYD0JKxETsi");
        when(userRepository.findByEmail("koneko@test.com")).thenReturn(Optional.empty());

        //when(bCryptPasswordEncoder.matches("1234567890", user.getPassword_hash())).thenReturn(true);

        // Act
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.login("koneko@test.com", "1234567890", httpServletRequest)
        );

        // Assert
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByEmail("koneko@test.com");
    }
}
