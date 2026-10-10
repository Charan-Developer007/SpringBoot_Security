package com.spring.security.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.spring.security.entities.Role;
import com.spring.security.entities.Users;
import com.spring.security.repository.UserDetailsRepository;

@Configuration
public class Usernames_Initilizer {
	
	
	//This is for saving initial username and password when application starts u can manually save username and password in database if required
		@Bean
		public CommandLineRunner seedH2Users(UserDetailsRepository users, PasswordEncoder passwordEncoder) {
			return args ->    
			{         // Check if user already exists to avoid duplicate entries on restart
            if (users.findByUsername("admin").isEmpty())
            {
                Users admin = new Users();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); // Always encode passwords!
                admin.setRole(Role.ADMIN); // Ensure Role.ADMIN exists in your Role enum
                
                users.save(admin);
            }
//            if(users.findByUsername("users")==null)
//            {
//            	Users user=new Users();
//            	user.setUsername("users");
//            	user.setPassword(passwordEncoder.encode("user123"));
//            	user.setRole(Role.USER);
//            	
//            	users.save(user);
//            }
			};
		}
}
