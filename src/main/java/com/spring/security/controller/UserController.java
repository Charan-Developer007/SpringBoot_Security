package com.spring.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.security.entities.AuthRequest;
import com.spring.security.serivce.UserService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class UserController {
	
	//@Autowired
	private final UserService userService;
	
    @PostMapping("/register/user")
    public ResponseEntity<String> registerUser(@RequestBody AuthRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }
    
    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registerAdmin(@RequestBody AuthRequest request)
    {
    	userService.registerAdmin(request);
    	return ResponseEntity.ok("Admin registered successfully");   
    }
}
