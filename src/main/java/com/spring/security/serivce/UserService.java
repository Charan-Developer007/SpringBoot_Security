package com.spring.security.serivce;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.security.entities.AuthRequest;
import com.spring.security.entities.Role;
import com.spring.security.entities.Users;
import com.spring.security.repository.UserDetailsRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {
		
	   
	    private final UserDetailsRepository users;
	    private final PasswordEncoder passwordEncoder;
	
        public void registerUser(AuthRequest request) 
        {
        if (users.findByUsername(request.getUsername()).isPresent())
        {
            throw new IllegalArgumentException("Username already exists");
        }

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        users.save(user);
        }
        
        public void registerAdmin(AuthRequest request) 
        {
        if (users.findByUsername(request.getUsername()).isPresent())
        {
            throw new IllegalArgumentException("Admin already exists");
        }

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ADMIN);

        users.save(user);
        }
}
