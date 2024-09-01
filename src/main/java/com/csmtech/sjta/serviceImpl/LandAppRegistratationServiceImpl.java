package com.csmtech.sjta.serviceImpl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.LandAppRegistratationDTO;
import com.csmtech.sjta.repository.LandAppRegistratationClassRepository;
import com.csmtech.sjta.repository.LandAppRegistrationRepository;
import com.csmtech.sjta.service.LandAppRegistratationService;
import com.csmtech.sjta.util.MailUtil;
import com.csmtech.sjta.util.OtpGenerateCommonUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LandAppRegistratationServiceImpl implements LandAppRegistratationService {

	@SuppressWarnings("unused")
	@Autowired
	private LandAppRegistrationRepository landrepo;

	@Autowired
	private LandAppRegistratationClassRepository landclassrepo;

	@Autowired
	private MailUtil mailutil;

	// use the key
	@Value("${sjta.bcryptpassword.secretKey}")
	private String SECRET_KEY;

	@Override
	public Integer saveUserData(LandAppRegistratationDTO registerdto) throws IOException, NoSuchAlgorithmException {

		if (registerdto.getEmailId().equals("")) {
			log.info("Mail is not available.");
		} else {
			JSONObject mailData = new JSONObject();
			mailData.put("fullName", registerdto.getFullName());
			
			List<String> recipientEmails = new ArrayList<>();
			recipientEmails.add(registerdto.getEmailId());
			
			String subject = "Land Registration || SJTA";
			Short status = 0;
			
			mailutil.sendEmail(1, status, mailData, subject, recipientEmails, null, null);
			log.info("Mail Send Sucess " + registerdto.getEmailId());
		}

		// user_name,password, full_name, mobile_no, email_id, otp, user_type,
		// created_by, created_on
		String username = registerdto.getMobileno();
		String password = registerdto.getPassword();
		String fullName = registerdto.getFullName();
		String mobileNo = registerdto.getMobileno();
		String email = registerdto.getEmailId();
		String otp = OtpGenerateCommonUtil.generateOTP();

		// encoder the password
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedValue = encoder.encode(SECRET_KEY + password);

		return landclassrepo.insertUserWithRole(username, encodedValue, fullName, mobileNo, email, otp);

	}

	// verified user mobile first
	@Override
	public Integer saveRegisterUserMobileNoOrOtp(String mobileNO) throws NoSuchAlgorithmException {
		String otp = OtpGenerateCommonUtil.generateOTP();
		// not use for testing
		return landclassrepo.insertMobileAndOTP(mobileNO, "123456");
	}

	@Override
	public Integer UpdateRegisterUserMobileNoOrOtp(String mobileNO) throws NoSuchAlgorithmException {
		String otp = OtpGenerateCommonUtil.generateOTP();
		// Not Used That testing
		return landclassrepo.updateMobileNoOrOtp(mobileNO, "123456");
	}

	@Override
	public String getOTPByMobileNo(String mobileno) {
		return landclassrepo.getOTPByMobileNo(mobileno);
	}

}
