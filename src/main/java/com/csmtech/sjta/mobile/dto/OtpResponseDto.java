package com.csmtech.sjta.mobile.dto;

import java.util.List;

import com.csmtech.sjta.dto.TahasilDTO;
import com.csmtech.sjta.dto.UserDetails;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtpResponseDto {
	
	private Integer status;
	private String message;
	
	private String token;
	
	private String otpStatus;
	
	private String otp;
	
	private String mobileNo;
	
	private UserDetails userdetails;
	
	private TahasilDTO tahsilDto;
	
	private String statusMessage;
	

}
