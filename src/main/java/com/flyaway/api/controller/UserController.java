package com.flyaway.api.controller;

import com.flyaway.api.dto.NewIdDTO;
import com.flyaway.api.dto.RegisterUserDTO;
import com.flyaway.api.entity.User;
import com.flyaway.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<NewIdDTO> register(@Valid @RequestBody RegisterUserDTO dto) {
        return ResponseEntity.status(201).body(userService.register(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }
}