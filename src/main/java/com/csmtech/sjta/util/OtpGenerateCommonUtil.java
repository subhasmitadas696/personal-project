package com.csmtech.sjta.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OtpGenerateCommonUtil {

	
	public static String generateOTP() throws NoSuchAlgorithmException {
		SecureRandom rand = SecureRandom.getInstanceStrong();
		Integer length = 6;
		String otp = "";
		SecureRandom random;
		try {
			random = SecureRandom.getInstanceStrong();
			for (int i = 0; i < length; i++) {
				int digit = rand.nextInt(9) + 1;
				otp += digit;
			}
			log.info("otp : " + otp);
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage());
		}

		return otp;
	}
}
