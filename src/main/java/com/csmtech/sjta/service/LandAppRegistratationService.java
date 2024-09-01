package com.csmtech.sjta.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.csmtech.sjta.dto.LandAppRegistratationDTO;

/**
 * @Auth Rashmi Ranjan Jena
 */

public interface LandAppRegistratationService {

	public Integer saveUserData(LandAppRegistratationDTO registerdto) throws IOException, NoSuchAlgorithmException;
	
	public Integer saveRegisterUserMobileNoOrOtp(String mobileNO)throws NoSuchAlgorithmException;
	
	public Integer UpdateRegisterUserMobileNoOrOtp(String mobileNO)throws NoSuchAlgorithmException;
	
	public String getOTPByMobileNo(String mobileno);
	
	

}
