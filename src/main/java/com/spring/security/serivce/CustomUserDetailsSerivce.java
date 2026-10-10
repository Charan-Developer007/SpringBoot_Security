package com.spring.security.serivce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.security.repository.UserDetailsRepository;

@Service
public class CustomUserDetailsSerivce implements UserDetailsService{
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userDetailsRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("UserName Not Found"));
	}

}
