package com.nanakusa.zanshin.service;

import com.nanakusa.zanshin.entity.SecurityEventType;
import com.nanakusa.zanshin.entity.SecurityLog;
import com.nanakusa.zanshin.entity.UserResponse;
import com.nanakusa.zanshin.repository.SecurityLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityLogService {
    @Autowired
    SecurityLogRepository securityLogRepository;

    @Autowired
    IPService ipService;

    // ✅Se pueden mejorar los logs pasando nuevo parametro de causa.

    // Este metodo crean un log y registra el usuario, se usa para eventos exitosos (ej: creación de usuario)
    public void createLogWithUser(UserResponse userResponse, HttpServletRequest httpServletRequest) {

        SecurityLog securityLog = new SecurityLog();
        securityLog.setUserId(userResponse.getId());
        securityLog.setEventType(SecurityEventType.USER_CREATION_SUCCESSFULLY);
        securityLog.setIp(ipService.getClientIp(httpServletRequest));
        securityLog.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        securityLog.setDetails("User account created by admin: " + userResponse.getEmail());

        securityLogRepository.save(securityLog);
    }

    // Este metodo crean un log sin usuario, se usa para eventos fallidos (ej: intento de creación de usuario con email ya registrado)
    public void createLogWithoutUser(HttpServletRequest httpServletRequest) {

        SecurityLog securityLog = new SecurityLog();
        securityLog.setEventType(SecurityEventType.USER_CREATION_FAILED);
        securityLog.setIp(ipService.getClientIp(httpServletRequest));
        securityLog.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        securityLog.setDetails("User creation failed: invalid input data: null");

        securityLogRepository.save(securityLog);
    }

    public void createLogLoginSuccess(HttpServletRequest httpServletRequest){
        SecurityLog securityLog = new SecurityLog();
        securityLog.setEventType(SecurityEventType.LOGIN_SUCCESS);
        securityLog.setIp(ipService.getClientIp(httpServletRequest));
        securityLog.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        securityLog.setDetails("User authenticated successfully: null");

        securityLogRepository.save(securityLog);
    }

    public void createLogLoginError(HttpServletRequest httpServletRequest){
        SecurityLog securityLog = new SecurityLog();
        securityLog.setEventType(SecurityEventType.LOGIN_FAILED);
        securityLog.setIp(ipService.getClientIp(httpServletRequest));
        securityLog.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        securityLog.setDetails("Login failed: invalid credentials: null");

        securityLogRepository.save(securityLog);
    }

    public void createLogLogoutSuccess(HttpServletRequest httpServletRequest){
        SecurityLog securityLog = new SecurityLog();
        securityLog.setEventType(SecurityEventType.LOGOUT);
        securityLog.setIp(ipService.getClientIp(httpServletRequest));
        securityLog.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        securityLog.setDetails("User logout successful - session revoked: null");

        securityLogRepository.save(securityLog);
    }

    public void createLogLogoutError(HttpServletRequest httpServletRequest){
        SecurityLog securityLog = new SecurityLog();
        securityLog.setEventType(SecurityEventType.LOGOUT_FAILED);
        securityLog.setIp(ipService.getClientIp(httpServletRequest));
        securityLog.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        securityLog.setDetails("Logout failed - internal error: null");

        securityLogRepository.save(securityLog);
    }

    public void createLogRefreshTokenSuccess(HttpServletRequest httpServletRequest){
        SecurityLog securityLog = new SecurityLog();
        securityLog.setEventType(SecurityEventType.REFRESH_TOKEN_USED);
        securityLog.setIp(ipService.getClientIp(httpServletRequest));
        securityLog.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        securityLog.setDetails("Refresh token success: null");

        securityLogRepository.save(securityLog);
    }

    public void createLogRefreshTokenError(HttpServletRequest httpServletRequest){
        SecurityLog securityLog = new SecurityLog();
        securityLog.setEventType(SecurityEventType.REFRESH_TOKEN_REUSE_DETECTED);
        securityLog.setIp(ipService.getClientIp(httpServletRequest));
        securityLog.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        securityLog.setDetails("Refresh token failed: null");

        securityLogRepository.save(securityLog);
    }

}
