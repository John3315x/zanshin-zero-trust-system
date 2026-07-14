/*
package com.nanakusa.zanshin.service;

import com.nanakusa.zanshin.dto.user.UserDto;
import com.nanakusa.zanshin.dto.user.DeleteUserDto;
import com.nanakusa.zanshin.entity.User;
import com.nanakusa.zanshin.exception.CreateUserException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    SecurityLogService securityLogService;


     //🚩 FALTAN LOGS A LOS METODOS


    public void createUser(UserDto userDto, HttpServletRequest httpServletRequest){

        if (userRepository.existsByEmail(userDto.getEmail())) {
            // 🔶 LOG
            securityLogService.createLogWithoutUser(httpServletRequest);
            throw new CreateUserException("Email already in use");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword_hash(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());

        userRepository.save(user);
        // 🔶 LOG
        securityLogService.createLogWithUser(user, httpServletRequest);
    }

    public void deleteUser(DeleteUserDto deleteUserDto){
        User user = userRepository.findByEmail(deleteUserDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + deleteUserDto.getEmail()));
        userRepository.delete(user);
    }

    public void updateUser(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userDto.getEmail()));

        user.setUsername(userDto.getUsername());
        user.setPassword_hash(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());

        userRepository.save(user);
    }
}*/
