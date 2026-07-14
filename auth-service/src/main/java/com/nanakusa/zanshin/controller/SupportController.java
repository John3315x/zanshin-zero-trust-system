package com.nanakusa.zanshin.controller;

import com.nanakusa.zanshin.dto.user.UserDto;
import com.nanakusa.zanshin.entity.UserResponse;
import com.nanakusa.zanshin.service.UserServiceClient;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/support")
@SecurityRequirement(name = "bearerAuth")
public class SupportController {

    // 👷‍♂️🚧 Microservicio: Zanshin - User Service
    @Autowired
    UserServiceClient userServiceClient;

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDto userDto, HttpServletRequest httpServletRequest) {
        return userServiceClient.createUser(userDto, httpServletRequest);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Valid UserDto userDto, HttpServletRequest httpServletRequest){
        return userServiceClient.updateUser(id, userDto, httpServletRequest);
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id){
        return userServiceClient.getUser(id);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<?> getUsers(){
        return userServiceClient.getUsers();
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        return userServiceClient.deleteUser(id);
    }
}
