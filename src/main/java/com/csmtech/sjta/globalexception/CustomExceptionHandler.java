package com.csmtech.sjta.globalexception;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity handleAuthenticationException(AuthenticationException ex) {
		return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity handleExpiredJwtException(ExpiredJwtException ex) {
		return buildResponse(HttpStatus.UNAUTHORIZED, "Token expired");
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity handleBadCredentialsException(BadCredentialsException ex) {
		return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid token");
	}

	// Handle other exceptions as needed

	private ResponseEntity buildResponse(HttpStatus status, String message) {
		JSONObject responseBody = new JSONObject();
		responseBody.put("status", status.value());
		responseBody.put("message", message);
		return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(responseBody.toString());
	}

}
