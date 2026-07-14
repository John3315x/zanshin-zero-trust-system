package com.nanakusa.zanshin.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/*
 🚧
 Se crea este UserResponse ya que como ahora trabajamos con el microservicio de usuarios desde el microservicio de
 autenticación, no podemos usar la entidad User que teníamos antes, ya que esta estaba mapeada a la base de datos del
 microservicio de usuarios, y ahora necesitamos una entidad que se adapte a la respuesta que nos devuelve el
 microservicio de usuarios.
*/
public class UserResponse {


    private Long id;
    private String username;
    private String email;
    private String password_hash;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password_hash='" + password_hash + '\'' +
                ", role=" + role +
                ", createdAt=" + createdAt +
                '}';
    }
}
