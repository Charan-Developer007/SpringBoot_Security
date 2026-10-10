package com.spring.security.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
		
	private final String SECRET="super_secret_jwt_test_key_12345_abcde!";
	 
	private final SecretKey key=Keys.hmacShaKeyFor(SECRET.getBytes());
	
	private final long EXPIRATION_TIME=1000*60*60; // 1hour
	
	public String generateToken(String username)
	{	
		return Jwts.builder()
			.subject(username)
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
			.signWith(key)
			.compact();
		
	}
	
	public String extractUsername(String token)
	{
		return extractClaims(token).getSubject();
	}
	
	
	private Claims extractClaims(String token)
	{
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	
	public boolean validateToken(String username,UserDetails userDetails,String token)
	{
		//TODO check if username is same as username in userdetails
		//TODO check if token is expired
		
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
		
		
	}

	
	private boolean isTokenExpired(String token)
	{	
		return extractClaims(token).getExpiration().before(new Date());
	}

}
