package com.nanakusa.zanshin.controller;

import com.nanakusa.zanshin.dto.user.UserDto;
//import com.nanakusa.zanshin.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@SecurityRequirement(name = "bearerAuth")
public class TestController {


    /*@Autowired
    UserService userService;

    // ⛔ [SOLO TESTING] Endpoint para crear usuarios (sin seguridad)
    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDto userDto, HttpServletRequest httpServletRequest){
        userService.createUser(userDto, httpServletRequest);
        return ResponseEntity.ok("User created successfully");
    }*/

    // ⛔ [SOLO TESTING] POST - Obtiene el Name() del  contexto de seguridad (subject del token valido), en este caso el email
    @GetMapping("/authenticated")
    public String authenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName(); // 👈 usuario

        String roles = auth.getAuthorities()
                .stream()
                .map(granted -> granted.getAuthority())
                .toList()
                .toString();

        return "User: " + username + " | Roles: " + roles;
    }
}
