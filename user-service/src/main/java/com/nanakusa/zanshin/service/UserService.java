package com.nanakusa.zanshin.service;

import com.nanakusa.zanshin.dto.UserDto;
import com.nanakusa.zanshin.entity.User;
import com.nanakusa.zanshin.exception.CreateUserException;
import com.nanakusa.zanshin.exception.DeleteUserException;
import com.nanakusa.zanshin.exception.GetUserException;
import com.nanakusa.zanshin.exception.UpdateUserException;
import com.nanakusa.zanshin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public void createUser(UserDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            // 🔶 LOG MISSING
            throw new CreateUserException("Email already in use", HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword_hash(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());

        userRepository.save(user);
        // 🔶 LOG MISSING
    }

    public void updateUser(Long id, UserDto userDto) {

        // Se busca el usuario por ID para asegurarse de que existe antes de intentar actualizarlo. Si no existe, se lanza una excepción.
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UpdateUserException("User not found", HttpStatus.NOT_FOUND));

        // Se busca usuario con email pasado en el DTO para verificar si el email ya está en uso por otro usuario y evitar conflictos de email único.
        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());

        // Se verifica si el email ya está en uso por otro usuario diferente al que se está actualizando. Si es así, se lanza una excepción para evitar duplicados de email.
        // Ejemplo:

        /* Estás actualizando usuario id = 1
        *  Quieres ponerle el email: user1@test.com
        *  Pero ese email ya lo tiene usuario id = 4
        */

        /*
        Se evalúa así:

            existingUser.isPresent()
            Sí, porque el email ya existe (lo tiene el usuario 4)
            → true
            !existingUser.get().getId().equals(id)
            existingUser.get().getId() → 4
            id → 1
            4.equals(1) → false
            !false → true

            El if quedaria: if(true && true)
         */
        if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
            throw new UpdateUserException("Email already in use", HttpStatus.CONFLICT);
        }

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword_hash(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());

        userRepository.save(user);
    }

    public Optional<User> getUser(Long id){
        if (!userRepository.existsById(id)) {
            // 🔶 LOG MISSING
            throw new GetUserException("User not found", HttpStatus.NOT_FOUND);
        }

        // 🔶 LOG MISSING
        return userRepository.findById(id);
    }

    public List<User> getUsers(){
        // 🔶 LOG MISSING
        return userRepository.findAll();
    }

    public void deleteUser(Long id){
        if (!userRepository.existsById(id)) {
            // 🔶 LOG MISSING
            throw new DeleteUserException("User not found", HttpStatus.NOT_FOUND);
        }

        // 🔶 LOG MISSING
        userRepository.deleteById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        if (!userRepository.existsByEmail(email)) {
            // 🔶 LOG MISSING
            throw new GetUserException("User not found", HttpStatus.NOT_FOUND);
        }

        // 🔶 LOG MISSING
        return userRepository.findByEmail(email);
    }
}
