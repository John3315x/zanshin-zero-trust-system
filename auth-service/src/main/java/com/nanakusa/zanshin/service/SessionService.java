package com.nanakusa.zanshin.service;

import com.nanakusa.zanshin.dto.AuthResponse;
import com.nanakusa.zanshin.entity.Session;
import com.nanakusa.zanshin.entity.UserResponse;
import com.nanakusa.zanshin.exception.RefreshTokenException;
import com.nanakusa.zanshin.exception.TooManySessionsException;
import com.nanakusa.zanshin.repository.SessionRepository;
import com.nanakusa.zanshin.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SessionService {

    private final JwtUtil jwtUtil;

    public SessionService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private final long refreshTokenDurationDays = 7;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    IPService ipService;

    @Autowired
    UserServiceClient userServiceClient;

    @Autowired
    SecurityLogService securityLogService;

    public void createSession(HttpServletRequest httpServletRequest, Long userId, String ip, String tokenId, String secret){

        // Validar maximo 3 sesiones por usuario 🚩
        int activeSessions = sessionRepository.countByUserIdAndRevokedFalse(userId);
        if (activeSessions >= 3) {
            throw new TooManySessionsException("Maximum number of active sessions reached", HttpStatus.FORBIDDEN); // Excepcion personalizeda para manejar este caso en el GlobalExceptionHandler
        }

        Session session = new Session();
        session.setUserId(userId);
        session.setTokenId(tokenId);
        session.setRefreshTokenHash(bCryptPasswordEncoder.encode(secret));
        session.setIp(ip);
        session.setUser_agent(httpServletRequest.getHeader("User-Agent"));
        session.setExpires_at(LocalDateTime.now().plusDays(refreshTokenDurationDays));
        session.setRevoked(false);

        sessionRepository.save(session);
    }


    public AuthResponse refreshAccesToken(String plainRefreshToken, HttpServletRequest httpServletRequest){

        // Separar el token 🚩
        String[] parts = plainRefreshToken.split("\\.");

        String tokenId = parts[0];
        String secret = parts[1];

        // Buscar el token hasheado "secret" con el tokeId 🚩
        Session session = sessionRepository.findByTokenId(tokenId);

        // Aplicamos la seguridad 🚩
        if (session == null){
            // 🔶 LOG
            securityLogService.createLogRefreshTokenError(httpServletRequest);
            throw new RefreshTokenException("Session not found", HttpStatus.NOT_FOUND);
        }

        if (!bCryptPasswordEncoder.matches(secret, session.getRefreshTokenHash())){
            // 🔶 LOG
            securityLogService.createLogRefreshTokenError(httpServletRequest);
            throw new RefreshTokenException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }

        // Verificar si está revocado
        if (session.isRevoked()) {
            // 🔶 LOG
            securityLogService.createLogRefreshTokenError(httpServletRequest);
            throw new RefreshTokenException("This session has already been revoked", HttpStatus.UNAUTHORIZED);
        }

        // Verificar expiración
        if (session.getExpires_at().isBefore(LocalDateTime.now())) {
            // Opcional: revocarlo para mantener consistencia en la DB
            session.setRevoked(true);
            sessionRepository.save(session);

            // 🔶 LOG
            securityLogService.createLogRefreshTokenError(httpServletRequest);
            throw new RefreshTokenException("This session has already expired", HttpStatus.UNAUTHORIZED);
        }

        // Generamos el JWT 🚩
        UserResponse userResponse = userServiceClient.getUser(session.getUserId()).getBody(); // Nos aseguramos de que el usuario exista, aunque debería existir porque la sesión fue creada previamente con un userId válido.

        if (userResponse == null) {
            throw new RefreshTokenException("INTERNAL_FAILURE", HttpStatus.INTERNAL_SERVER_ERROR);//mensaje general para no exibir diseño de arquitectura
        }

        String newAccessToken = jwtUtil.generateToken(userResponse);

        // Rotar el refreshToken 🚩

        // Revocar la antigua sesion
        session.setRevoked(true);
        sessionRepository.save(session);

        // Crear nueva session
        String newTokenId = UUID.randomUUID().toString();
        String newSecret = UUID.randomUUID().toString();
        String newPlainRefreshToken = newTokenId + "." + newSecret;
        String ip = ipService.getClientIp(httpServletRequest);
        //sessionRepository.save(createSession(httpServletRequest, userResponse.getId(), ip, newTokenId, newSecret));
        createSession(httpServletRequest, userResponse.getId(), ip, newTokenId, newSecret);

        // 🔶 LOG
        securityLogService.createLogRefreshTokenSuccess(httpServletRequest);

        return new AuthResponse(newAccessToken, newPlainRefreshToken, userResponse.getEmail());
    }
}
