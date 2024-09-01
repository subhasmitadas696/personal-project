package com.csmtech.sjta.mobile.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.LandRegisterMobileNoOrOtpVerifiedDTO;
import com.csmtech.sjta.dto.UserDetails;
import com.csmtech.sjta.entity.AuthRequest;
import com.csmtech.sjta.entity.LandAppRegistrationEntity;
import com.csmtech.sjta.mobile.dto.OtpResponseDto;
import com.csmtech.sjta.mobile.repository.MobileLoginRepository;
import com.csmtech.sjta.repository.LandAppRegistratationClassRepository;
import com.csmtech.sjta.repository.LandAppRegistrationRepository;
import com.csmtech.sjta.repository.UserRepository;
import com.csmtech.sjta.util.JwtUtil;
import com.csmtech.sjta.util.OtpGenerateCommonUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

	@Value("${sjta.bcryptpassword.secretKey}")
	private String SECRET_KEY;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	private LandAppRegistrationRepository repo;

	@Autowired
	private LandAppRegistratationClassRepository landclassrepo;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MobileLoginRepository mobileLoginRepo;

	@Override
	public OtpResponseDto mobileTokenGeneration() {
		OtpResponseDto response = new OtpResponseDto();
		String token = jwtUtil.createMobileToken();
		if (token != null) {
			response.setStatus(200);
			response.setMessage("Token generated successfully");
			response.setToken(token);

		} else {
			response.setStatus(412);
			response.setMessage("Some error occured. Please try again later");
		}

		return response;
	}

	@Override
	public OtpResponseDto mobileLogin(String token, AuthRequest authRequest) throws NoSuchAlgorithmException {
		OtpResponseDto response = new OtpResponseDto();
		boolean tokenValidation = jwtUtil.isTokenExpired(token);
		if (tokenValidation == true) {
			response.setStatus(401);
			response.setMessage("Token expired/Invalid ");
			return response;
		} else {
			log.info("jwt part checking is successful");
		//	authRequest.setMobileno(authRequest.getUsername());
			LandAppRegistrationEntity getalldata = repo.findByUserNameDeletedOrNot(authRequest.getUsername());
			if (getalldata == null) {
				response.setStatus(404);
				response.setMessage("UserName doesn't exist/ Invalid UserName");
				return response;
			} else {
				LandAppRegistrationEntity getalldata1 =repo.findBymobileno(getalldata.getMobileno().toString());
				//Integer count = repo.findBymobilenoBlockOrNot(getalldata.getMobileno());
				if (getalldata1 == null) {
					response.setStatus(444);
				
					response.setMessage("User blocked. Please contact Admin");
					return response;
				} else {
					boolean isMatch = passwordMatch(authRequest.getPassword(), getalldata.getPassword());

					if (isMatch) {
						String randomNumber = OtpGenerateCommonUtil.generateOTP();

						log.info("otp  " + randomNumber);
						response.setMessage("Login Successful");
						response.setStatus(200);
						response.setOtpStatus("OTP sent successfully");
						response.setToken(token);
						response.setOtp(randomNumber);
						response.setMobileNo(getalldata.getMobileno());
						landclassrepo.updateOtp(getalldata.getMobileno(), randomNumber);
						// to be changed to user details table instead of
						// registration_mobile_no_varification table
					} else {
						response.setStatus(404);
						response.setMessage("Invalid Password");
						return response;
					}
				}

			}

		}

		return response;
	}

	public boolean passwordMatch(String userGivenPassword, String hashedPassword) {
		log.info(" ::BCryptPasswordEncoder start the matching ");
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String generatedBcryptHexeash = encoder.encode(SECRET_KEY + userGivenPassword);

		boolean isMatch = encoder.matches(SECRET_KEY + userGivenPassword, hashedPassword);
		log.info(" ::BCryptPasswordEncoder matching done ");
		return isMatch;
	}

	@Override
	public OtpResponseDto otpVerification(String token, LandRegisterMobileNoOrOtpVerifiedDTO otpDto) {
		OtpResponseDto response = new OtpResponseDto();
		boolean tokenValidation = jwtUtil.isTokenExpired(token);

		if (tokenValidation == true) {
			response.setStatus(401);
			response.setMessage("Token expired/Invalid ");
			return response;
		} else {
			UserDetails userDetails = new UserDetails();
			log.info("jwt part checking is successful");
			String result = landclassrepo.fetchOTPByMobileNo(otpDto.getMobileno());
//			String result = landclassrepo.getOTPByMobileNo(otpDto.getMobileno());
			UserDetails user = mobileLoginRepo.getUserDetails(otpDto.getMobileno());
			
			log.info("user details from db " + user);
			if (otpDto.getOtp().equals(result)) {
				Integer i = mobileLoginRepo.saveFcmToken(otpDto.getFcmToken(),otpDto.getMobileno());
				response.setStatus(200);
				response.setMessage("OTP verified Successful");
			//	response.setUserdetails(userDetails);
				response.setUserdetails(user);
				response.setStatusMessage("success");
//				userDetails.setUserId(user.getUserId());
//				userDetails.setUserName(user.getFullName());
//				userDetails.setMobileNo(user.getMobileNo());
//				userDetails.setDesignation(user.getDesignation());
			//	userDetails.set
			} else {
				response.setStatus(200);
				response.setMessage("Otp verification failed");
				response.setStatusMessage("failed");
			}

		}
		return response;
	}

}
