package com.nanakusa.zanshin.service;

import com.nanakusa.zanshin.dto.user.UserDto;
import com.nanakusa.zanshin.entity.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     *
     * @param userDto
     * @param httpServletRequest
     * @return
     */
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDto userDto, HttpServletRequest httpServletRequest) {

        String url ="http://user-service:8082/users/createUser";

        RequestEntity<UserDto> requestEntity = RequestEntity
                .post(URI.create(url))
                .header("Content-Type", "application/json")
                .header("X-Forwarded-For", httpServletRequest.getRemoteAddr())
                .header("User-Agent", httpServletRequest.getHeader("User-Agent"))
                .body(userDto);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("User service unavailable");
        }
    }

    /**
     *
     * @param id
     * @param userDto
     * @param httpServletRequest
     * @return
     */
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Valid UserDto userDto, HttpServletRequest httpServletRequest){
        String url = "http://user-service:8082/users/updateUser/" + id;

        RequestEntity<UserDto> requestEntity = RequestEntity
                .put(URI.create(url))
                .header("Content-Type", "application/json")
                .header("X-Forwarded-For", httpServletRequest.getRemoteAddr())
                .header("User-Agent", httpServletRequest.getHeader("User-Agent"))
                .body(userDto);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("User service unavailable");
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id){

        String url = "http://user-service:8082/users/getUser/" + id;
        RequestEntity<Void> requestEntity = RequestEntity.get(URI.create(url)).build();

        try {
            ResponseEntity<UserResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, UserResponse.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();// Por ahora solo se muetra el codigo de error pero se podria mostrar el mensaje de error con e.getResponseBodyAsString()

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     *
     * @return
     */
    public ResponseEntity<?> getUsers(){

        String url = "http://user-service:8082/users/getUsers";
        RequestEntity<Void> requestEntity = RequestEntity.get(URI.create(url)).build();

        try {
            ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<UserResponse>>() {});
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public ResponseEntity<?> deleteUser(@PathVariable Long id){

        String url = "http://user-service:8082/users/deleteUser/" + id;
        RequestEntity<Void> requestEntity = RequestEntity.delete(URI.create(url)).build();

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("User service unavailable");
        }
    }

    /**
     *
     * @param email
     * @return
     */
    public ResponseEntity<UserResponse> getUserByEmail(String email) {
        String url = "http://user-service:8082/users/getUserByEmail/" + email;
        RequestEntity<Void> requestEntity = RequestEntity.get(URI.create(url)).build();

        try {
            ResponseEntity<UserResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, UserResponse.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
