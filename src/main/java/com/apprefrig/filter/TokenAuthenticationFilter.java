package com.apprefrig.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.apprefrig.config.SecurityConfiguration;
import com.apprefrig.services.TokenServices;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = getTokenFromHeader(request);
		if(isTokenValid(token)) {
			this.authenticate(token);
		}
		
		filterChain.doFilter(request, response);
	}

	private void authenticate(String tokenFromHeader) {
		
		String username = TokenServices.getTokenName(tokenFromHeader);
		
		UserDetails userFromToken = SecurityConfiguration.getUserFromUsername(username);
		
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userFromToken.getUsername(),userFromToken.getPassword(),userFromToken.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	}
		
	
	
	private String getTokenFromHeader(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		
		return token.substring(7, token.length());
	}

	
	private Boolean isTokenValid(String token) {
		return TokenServices.isTokenValid(token);
	}
	

}