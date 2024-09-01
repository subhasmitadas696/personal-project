package com.csmtech.sjta.mobile.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;


import com.csmtech.sjta.dto.LandRegisterMobileNoOrOtpVerifiedDTO;
import com.csmtech.sjta.entity.AuthRequest;
import com.csmtech.sjta.mobile.dto.OtpResponseDto;

@Service
public interface LoginService {
	

	public OtpResponseDto mobileTokenGeneration();

	public OtpResponseDto mobileLogin(String token, AuthRequest authRequest) throws NoSuchAlgorithmException;

	public OtpResponseDto otpVerification(String token, LandRegisterMobileNoOrOtpVerifiedDTO otpDto);

}
