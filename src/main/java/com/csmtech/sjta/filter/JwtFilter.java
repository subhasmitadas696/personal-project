package com.csmtech.sjta.filter;

/**
 * @Auth Rashmi Ranjan Jena
 */

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.csmtech.sjta.serviceImpl.LandAppLoginServiceImpl;
import com.csmtech.sjta.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private LandAppLoginServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authorizationHeader = request.getHeader("Authorization");
		String token = null;
		String userName = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
			token = authorizationHeader.substring(7);
			userName = jwtUtil.extractUsername(token);
		}

		try {
			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

				if (jwtUtil.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
		} catch (ExpiredJwtException expiredJwtException) {
			// Token expired
			handleJwtException(response, HttpStatus.UNAUTHORIZED, "Token expired");
			return;
		} catch (JwtException jwtException) {
			// Invalid token or other JWT exception
			handleJwtException(response, HttpStatus.UNAUTHORIZED, "Invalid token");
			return;
		} catch (AuthenticationException authenticationException) {
			// Authentication exception
			handleJwtException(response, HttpStatus.UNAUTHORIZED, "Authentication failed");
			return;
		}
		filterChain.doFilter(request, response);
	}

	private void handleJwtException(HttpServletResponse response, HttpStatus httpStatus, String message)
			throws IOException {
		response.setStatus(httpStatus.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		JSONObject responseBody = new JSONObject();
		responseBody.put("message", message);
		response.getWriter().write(responseBody.toString());
	}
}