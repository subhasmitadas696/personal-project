/**
 * 
 */
package com.csmtech.sjta.mobile.controller;

import com.csmtech.sjta.dto.LandRegisterMobileNoOrOtpVerifiedDTO;
import com.csmtech.sjta.entity.AuthRequest;
import com.csmtech.sjta.mobile.dto.OtpResponseDto;
import com.csmtech.sjta.mobile.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/mobileLogin")
@Slf4j
public class LoginController {

	@Autowired
	LoginService loginService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/getToken")
	public ResponseEntity getToken() {
		OtpResponseDto response = new OtpResponseDto();
		log.debug("generation of token");
		try {
			response = loginService.mobileTokenGeneration();

		} catch (Exception e) {
			log.error("Some error occured "+ e.getMessage());
			response.setStatus(500);
			response.setMessage("Some error occured. Please try again");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(response, HttpStatus.OK);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/login")
	public ResponseEntity mobileLogin(@RequestHeader("Token") String token, @RequestBody AuthRequest authRequest) {
		OtpResponseDto response = new OtpResponseDto();
		try {
			response = loginService.mobileLogin(token, authRequest);

		} catch (Exception e) {
			log.error("Login failed due to some error: "+e.getMessage());
			response.setMessage("Login failed due to some error. Please try again");
			response.setStatus(500);
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/otpVerification")
	public ResponseEntity otpVerification(@RequestHeader("Token") String token,
			@RequestBody LandRegisterMobileNoOrOtpVerifiedDTO otpDto) {
		OtpResponseDto response = new OtpResponseDto();
		try {
			response = loginService.otpVerification(token, otpDto);

		} catch (Exception e) {
			log.error("OTP verification failed due to some error. Please try again " + e.getMessage());
			response.setMessage("OTP verification failed due to some error. Please try again");
			response.setStatus(500);
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

}
