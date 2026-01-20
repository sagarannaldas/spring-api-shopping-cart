package com.sagarannaldas.shopping.cart.controllers;

import com.sagarannaldas.shopping.cart.dtos.LoginRequest;
import com.sagarannaldas.shopping.cart.dtos.UserDto;
import com.sagarannaldas.shopping.cart.mappers.UserMapper;
import com.sagarannaldas.shopping.cart.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(
            @Valid @RequestBody LoginRequest request
    ) {
        var user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return  ResponseEntity.ok(userMapper.toUserDto(user));
    }
}
