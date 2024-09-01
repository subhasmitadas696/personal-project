package com.csmtech.sjta.mobile.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.GrievanceMainDTO;
import com.csmtech.sjta.dto.LandRegisterMobileNoOrOtpVerifiedDTO;
import com.csmtech.sjta.mobile.dto.GrievanceResponseDto;
  
@Service
public interface GrievanceMobileService {
  
		
		public GrievanceResponseDto saveAfterInspection(GrievanceMainDTO dto);

		public GrievanceResponseDto saveGrievance(GrievanceMainDTO grievanceMainDTO);

		public GrievanceResponseDto fetchPendingGrievanceFromCO();

		public GrievanceResponseDto fetchPendingAndCompleteGrievanceCount();

		public GrievanceResponseDto fetchCompleteGrievanceFromCO();

		public GrievanceResponseDto fetchGrievanceByGrievanceNo(String grievanceNo);

		public GrievanceResponseDto saveMobile(String mobileNo) throws NoSuchAlgorithmException;

		public GrievanceResponseDto otpVerify(LandRegisterMobileNoOrOtpVerifiedDTO otpDto);

		public GrievanceResponseDto areaForPlot(GrievanceMainDTO grievanceMainDTO);

  
  }
 