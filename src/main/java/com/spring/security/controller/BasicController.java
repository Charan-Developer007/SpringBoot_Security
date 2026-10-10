package com.spring.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api")
public class BasicController {	
	
	@GetMapping("/name")
	@PreAuthorize("isAuthenticated()")
	public String getName()
	{
		return "Hello Charan!!";
	}
	
	
	@GetMapping("/city")
	@PreAuthorize("hasAuthority('read')")
	public String getPlace()
	{
		return "Chintamani";
	}
	


}
