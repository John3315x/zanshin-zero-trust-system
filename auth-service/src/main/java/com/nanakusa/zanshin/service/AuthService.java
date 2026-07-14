package com.nanakusa.zanshin.service;

import com.nanakusa.zanshin.dto.AuthResponse;
import com.nanakusa.zanshin.entity.Session;
import com.nanakusa.zanshin.entity.UserResponse;
import com.nanakusa.zanshin.exception.LoginException;
import com.nanakusa.zanshin.exception.LogoutException;
import com.nanakusa.zanshin.repository.SessionRepository;
import com.nanakusa.zanshin.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    SessionService sessionService;

    @Autowired
    UserServiceClient userServiceClient;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    IPService ipService;

    @Autowired
    SecurityLogService securityLogService;



    public AuthResponse login(String email, String password, HttpServletRequest httpServletRequest) {

        UserResponse userResponse = userServiceClient.getUserByEmail(email).getBody();

        if (userResponse == null) {
            // 🔶 LOG
            securityLogService.createLogLoginError(httpServletRequest);
            throw new LoginException("User not found", HttpStatus.NOT_FOUND);
        }

        if (!bCryptPasswordEncoder.matches(password, userResponse.getPassword_hash())){
            // 🔶 LOG
            securityLogService.createLogLoginError(httpServletRequest);
            throw new LoginException("Incorrect password", HttpStatus.UNAUTHORIZED);
        }

        // 1.Generar token JWT 🚩
        String accessToken = jwtUtil.generateToken(userResponse);

        // 2.Se obtiene el IP del cliente 🚩
        String ip = ipService.getClientIp(httpServletRequest);

        // 3.Se crea la sesion en BD 🚩
        String tokenId = UUID.randomUUID().toString();      // identificador
        String secret = UUID.randomUUID().toString();       // token real
        String plainRefreshToken = tokenId + "." + secret;  // token para el cliente
        sessionService.createSession(httpServletRequest, userResponse.getId(), ip, tokenId, secret);

        // 🔶 LOG
        securityLogService.createLogLoginSuccess(httpServletRequest);

        // Se crea y se retorna una respuesta para el usuario.
        return new AuthResponse(accessToken, plainRefreshToken, email);
    }


    public void logout (String plainRefreshToken, HttpServletRequest httpServletRequest){
        // Separar el token 🚩
        String[] parts = plainRefreshToken.split("\\.");

        String tokenId = parts[0];
        String secret = parts[1];

        // Buscar el token hasheado "secret" con el tokeId 🚩
        Session session = sessionRepository.findByTokenId(tokenId);

        // Aplicamos la seguridad 🚩
        if (session == null) {
            // 🔶 LOG
            securityLogService.createLogLogoutError(httpServletRequest);
            throw new LogoutException("Session not found", HttpStatus.UNAUTHORIZED);
        }

        if (!bCryptPasswordEncoder.matches(secret, session.getRefreshTokenHash())){
            // 🔶 LOG
            securityLogService.createLogLogoutError(httpServletRequest);
            throw new LogoutException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }

        if (session.isRevoked()) {
            // 🔶 LOG
            securityLogService.createLogLogoutError(httpServletRequest);
            throw new LogoutException("Session already revoked", HttpStatus.UNAUTHORIZED);
        }

        // Revocamos la sesion 🚩
        session.setRevoked(true);
        sessionRepository.save(session);

        // 🔶 LOG
        securityLogService.createLogLogoutSuccess(httpServletRequest);
    }
}
