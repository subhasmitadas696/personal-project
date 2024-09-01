package com.csmtech.sjta.service;

import com.csmtech.sjta.entity.LandAppRegistrationEntity;

/**
 * @author prasanta.sethi
 */

public interface ForgotPasswordService {

	public LandAppRegistrationEntity findByMobileNo(String mobileNo);

	public Integer updateOtp(LandAppRegistrationEntity user);

	public Integer updatepassword(String newPassword, String givenmono);

	public String checkPassword(String newPassword, String mobileNo);

}
