package com.dietiestates.api.filter;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestAuthHeaderValidationFilter extends OncePerRequestFilter {

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (request.getServletPath().contains("/api/v1/auth")) {
			chain.doFilter(request, response);
			return;
		}

		String bearerAuthenticationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (bearerAuthenticationHeader == null ||
				bearerAuthenticationHeader.isBlank() ||
				bearerAuthenticationHeader.isEmpty() ||
				!bearerAuthenticationHeader.startsWith("Bearer ")) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		var jwtToken = bearerAuthenticationHeader.substring(7);

		request.setAttribute("token", jwtToken);

		chain.doFilter(request, response);
	}

}
