package com.csmtech.sjta.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class GenericResponse {

	private Integer status;
	private String message;
	@JsonIgnore
	private String token;
	@JsonIgnore
	private String otpStatus;
	@JsonIgnore
	private Integer otp;

}
