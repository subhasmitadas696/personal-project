package com.csmtech.sjta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LandAppRegistratationDTO {

	String password;
	String fullName;
	String mobileno;
	String emailId;
	String conformPassword;
	Integer answer;
	String captchaId;

}
