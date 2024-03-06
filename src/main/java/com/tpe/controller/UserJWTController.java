package com.tpe.controller;

import com.tpe.dto.LoginRequest;
import com.tpe.dto.RegisterRequest;
import com.tpe.security.JWTUtils;
import com.tpe.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserJWTController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;


    // user registration işlemi
    // localhost:8080/register
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        userService.saveUser(registerRequest);

        return new ResponseEntity<>("User is created successfully!", HttpStatus.CREATED); // 201 - CREATED
    }

    // user login -> response olarak tokenmizi dönücez
    @PostMapping("/login") // standart haline gelmiş bir uygulamadır. Daha sağlıklı şekilde taşınıyor body
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        // alttaki validasyon hatalı olursa exception fırlatılır, doğru ise Authentica olan bir obje return edilir.
        Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(),
                        loginRequest.getPassword())); // username ve password valide edilir.

        String token = jwtUtils.generateToken(authentication);

        return new ResponseEntity<>(token, HttpStatus.CREATED); // 201

    }

    @GetMapping("/goodby")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<String> goodby() {
        return new ResponseEntity<>("Good By Security!", HttpStatus.OK);
    }
}
