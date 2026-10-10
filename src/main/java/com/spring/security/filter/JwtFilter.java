package com.spring.security.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.security.serivce.CustomUserDetailsSerivce;
import com.spring.security.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private CustomUserDetailsSerivce customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
			
			String authHeader=request.getHeader("Authorization");
			
			String token=null;
			String username=null;
			
			if(authHeader!=null && authHeader.startsWith("Bearer "))
			{
				 token=authHeader.substring(7);
				 username=jwtUtil.extractUsername(token);
			}
			
			//TODO Validate Token
			//TODO Set to Spring Context 
			
			if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
			{
				//TODO Fetch Username from database
				UserDetails userDetails= customUserDetailsService.loadUserByUsername(username);	
				if(jwtUtil.validateToken(username,userDetails,token))
				{
					UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
					
				}
			}
			filterChain.doFilter(request, response);
			
		}

}
