package com.csmtech.sjta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auth Prasanta Kumar Sethi
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordCaptchaFinalRequest {
	String mobileNo;
	String otp;
	String newPassword;
	String confirmPassword;
	Integer answer;
	String captchaId;

}
