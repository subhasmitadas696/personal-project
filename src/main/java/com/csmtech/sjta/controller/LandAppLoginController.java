package com.csmtech.sjta.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.entity.AuthRequest;
import com.csmtech.sjta.entity.LandAppRegistrationEntity;
import com.csmtech.sjta.repository.LandApplicantLoginClassRepository;
import com.csmtech.sjta.service.LandApplicationLoginService;
import com.csmtech.sjta.util.CommonCaptchaGenerate;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auth Rashmi Ranjan Jena
 */

@RestController
@RequestMapping("/landAppregistratation")

@Slf4j
public class LandAppLoginController {

	JSONObject response = new JSONObject();

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private LandApplicationLoginService landAppService;

	@Autowired
	private LandApplicantLoginClassRepository loginrepo;

	@Value("${sjta.bcryptpassword.secretKey}")
	private String secretKey;

	// Test To Create The welcome Method
	@SuppressWarnings("rawtypes")
	@GetMapping("/authreq1")
	public ResponseEntity welcome() {

		// Retrieve the authenticated user-name
		@SuppressWarnings("unused")
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		JSONObject response = new JSONObject();
		response.put(CommonConstant.STATUS_KEY, 200);
		response.put(CommonConstant.RESULT, "Welcome");
		return ResponseEntity.ok(response.toString());

	}

	// method will work generate Token and login
	@SuppressWarnings({ "unused", "rawtypes" })
	@PostMapping("/registratationtookan")
	public ResponseEntity generateToken(@RequestBody AuthRequest authRequest) throws Exception {

		Boolean validateCaptcha = captchaValidation(authRequest);
		if (!validateCaptcha) {
			JSONObject response = new JSONObject();
			response.put(CommonConstant.STATUS_KEY, 223);
			response.put(CommonConstant.RESULT, "Invalid captcha");
			return ResponseEntity.ok(response.toString());
		}
		log.info(" ::Captcha Validate Success..!!");
		log.info(" ::generateToken method are start");
		String matchPs = null;
		LandAppRegistrationEntity getalldata = null;
		Integer count = 0;
		Integer countCitizen = 0;

		List<Long> rollid = null;
		log.info("query starts");

		log.info("inside officer or admin");
		getalldata = landAppService.findByUsername(authRequest.getUsername());

		// hear the validate record will gone 5 time or not

		if (getalldata != null && getalldata.getUsername() != null
				&& getalldata.getUsername().equalsIgnoreCase(authRequest.getUsername())) {
			String providedPassword = authRequest.getPassword();
			String storedPassword = getalldata.getPassword();
			String password = authRequest.getPassword();
			String hashedPassword = getalldata.getPassword();
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String generatedBcryptHexeash = encoder.encode(secretKey + password);
			boolean isMatch = encoder.matches(secretKey + password, hashedPassword);
			Date dbTime = getalldata.getLoginValidateTime();
			Boolean checkTimeValidate = null;
			Long minutesDifference = null;
			if (!isMatch) {
				if (getalldata.getUserType().equals("CI")) {
					Short loginValidate = 0;
					if (getalldata.getLoginValidateTime() == null) {
						loginValidate = getalldata.getLoginValidate();
						loginValidate++;
						if (loginValidate == 5) {
							Integer updateDate = landAppService
									.updateLoginFlagForCitizenDate(authRequest.getUsername());
							Integer updateCitizen = landAppService.updateUserBlockFlag(authRequest.getUsername(),
									loginValidate);
						} else {
							Integer updateCitizen = landAppService.updateUserBlockFlag(authRequest.getUsername(),
									loginValidate);
						}
					} else {
						LocalDateTime currentDateTimeNew = LocalDateTime.now();
						LocalDateTime localDateTimeFromDate = dbTime.toInstant().atZone(ZoneId.systemDefault())
								.toLocalDateTime();
						Duration difference = Duration.between(localDateTimeFromDate, currentDateTimeNew);
						checkTimeValidate = difference.getSeconds() <= 1800;
						minutesDifference = difference.toMinutes();
						if (!Boolean.TRUE.equals(checkTimeValidate)) {
							Integer updateNullCase = landAppService
									.updateLoginFlagForCitizenDateForNull(authRequest.getUsername(), (short) 1);
							response.put(CommonConstant.STATUS_KEY, 432);
							response.put(CommonConstant.RESULT, CommonConstant.INVALID_USERNAME_PASSWORD);
						} else if (Boolean.TRUE.equals(checkTimeValidate)) {
							response.put(CommonConstant.STATUS_KEY, 777);
							response.put(CommonConstant.RESULT, CommonConstant.INVALID_TIME_LOGIN);
							response.put(CommonConstant.TIME_LEFT, minutesDifference);
						}
					}
				} else if (getalldata.getUserType().equals("O")) {
					// when the officer login are come.
					Short loginValidate = 0;
					if (getalldata.getLoginValidateTime() == null) {
						loginValidate = getalldata.getLoginValidate();
						loginValidate++;
						if (loginValidate == 5) {
							Integer updateDate = landAppService
									.updateLoginFlagForOfficerDate(authRequest.getUsername());
							Integer updateCitizen = landAppService.updateLoginFlagForOfficer(authRequest.getUsername(),
									loginValidate);
						} else {
							Integer updateCitizen = landAppService.updateLoginFlagForOfficer(authRequest.getUsername(),
									loginValidate);
						}
					} else {
						LocalDateTime currentDateTimeNew = LocalDateTime.now();
						LocalDateTime localDateTimeFromDate = dbTime.toInstant().atZone(ZoneId.systemDefault())
								.toLocalDateTime();
						Duration difference = Duration.between(localDateTimeFromDate, currentDateTimeNew);
						checkTimeValidate = difference.getSeconds() <= 1800;
						minutesDifference = difference.toMinutes();
						if (!Boolean.TRUE.equals(checkTimeValidate)) {
							Integer updateNullCase = landAppService
									.updateLoginFlagForOfficerDateForNull(authRequest.getUsername(), (short) 1);
							response.put(CommonConstant.STATUS_KEY, 432);
							response.put(CommonConstant.RESULT, CommonConstant.INVALID_USERNAME_PASSWORD);
						} else if (Boolean.TRUE.equals(checkTimeValidate)) {
							response.put(CommonConstant.STATUS_KEY, 777);
							response.put(CommonConstant.RESULT, CommonConstant.INVALID_TIME_LOGIN);
							response.put(CommonConstant.TIME_LEFT, minutesDifference);
						}
					}
				} 
//				else if (getalldata.getUserType().equals("A")) {
//					// when the admit will are come. req wise nothing will happen the admit login
//					// (No Block for Admit)
//				}
				if (getalldata.getLoginValidateTime() == null) {
					response.put(CommonConstant.STATUS_KEY, 432);
					response.put(CommonConstant.RESULT, CommonConstant.INVALID_USERNAME_PASSWORD);
				}

			} else {
				if (getalldata.getLoginValidate() != null && getalldata.getLoginValidate() == 5) {
					LocalDateTime currentDateTimeNew = LocalDateTime.now();
					LocalDateTime localDateTimeFromDate = dbTime.toInstant().atZone(ZoneId.systemDefault())
							.toLocalDateTime();
					Duration difference = Duration.between(localDateTimeFromDate, currentDateTimeNew);
					checkTimeValidate = difference.getSeconds() <= 1800;
					minutesDifference = difference.toMinutes();
				}
				if (checkTimeValidate != null && checkTimeValidate == true) {
					response.put(CommonConstant.STATUS_KEY, 777);
					response.put(CommonConstant.RESULT, CommonConstant.INVALID_TIME_LOGIN);
					response.put(CommonConstant.TIME_LEFT, minutesDifference);
				} else {
					// update and reset all
					if (getalldata.getUserType().equalsIgnoreCase("O")) {
						landAppService.updateLoginFlagForOfficerDateForNull(authRequest.getUsername(), (short) 0);
					} else if (getalldata.getUserType().equalsIgnoreCase("CI")) {
						Integer updateNullCase = landAppService
								.updateLoginFlagForCitizenDateForNull(authRequest.getUsername(), (short) 0);
					}
					// login success.. and time and validation should reset
					log.info("query ends with officer or admin data: ");
					count = landAppService.findByusernameBlockOrNot(authRequest.getUsername());
					countCitizen = landAppService.findCitizenBlockOrNot(authRequest.getUsername());
					if (getalldata == null) {
						JSONObject response = new JSONObject();
						response.put(CommonConstant.STATUS_KEY, 432);
						response.put(CommonConstant.RESULT, CommonConstant.INVALID_USERNAME_PASSWORD);
						return ResponseEntity.ok(response.toString());
					}

					// check user is active or not
					if (count == 0 && countCitizen == 0) {
						JSONObject response = new JSONObject();
						response.put(CommonConstant.STATUS_KEY, 444);
						response.put(CommonConstant.RESULT, "User blocked, Contact Admin");
						return ResponseEntity.ok(response.toString());
					}

					// Retrieve the role form the user
					if (getalldata != null) {
						log.info("inside retrieving role Id ");
						rollid = loginrepo.findRoleIdsByUserId(getalldata.getUsername());
						log.info("query starts for roleId : ");
					}

					if ((providedPassword == null && storedPassword == null)
							&& (storedPassword != null && !storedPassword.equals(providedPassword))) {
						JSONObject response = new JSONObject();
						response.put(CommonConstant.STATUS_KEY, 500);
						response.put(CommonConstant.RESULT, CommonConstant.INVALID_USERNAME_PASSWORD);
						return ResponseEntity.ok(response.toString());
					}

					log.info(" ::BCryptPasswordEncoder start the matching ");
					BCryptPasswordEncoder encoderPassword = new BCryptPasswordEncoder();
					String generatedBcryptHexeashPassword = encoderPassword.encode(secretKey + password);

					boolean isMatchPassword = encoder.matches(secretKey + password, hashedPassword);
					if (isMatchPassword) {
						matchPs = getalldata.getPassword();
					} else {
						matchPs = "Authentication failed. Please try again.";
					}
					log.info(" :: isMatch is validate sucesfully");
					try {

						log.info("inside try method of authentication");
						authenticationManager.authenticate(
								new UsernamePasswordAuthenticationToken(authRequest.getUsername(), matchPs));

					} catch (Exception ex) {
						log.info("exception in authenticating user !!" + ex.getMessage());
						JSONObject response = new JSONObject();
						response.put(CommonConstant.STATUS_KEY, 500);
						response.put(CommonConstant.RESULT, CommonConstant.INVALID_USERNAME_PASSWORD);

						return ResponseEntity.ok(response.toString());

					}
					log.info(" :: Execution end tookan return");
					response.put(CommonConstant.STATUS_KEY, 200);
					response.put("fullName", getalldata.getFullName());
					response.put("emailId", getalldata.getEmailId());
					response.put("userName", getalldata.getUsername());
					response.put("mobileNo", getalldata.getMobileno());
					response.put("userId", getalldata.getId());
					response.put("userType", getalldata.getUserType());
					response.put("isFirstLogin", getalldata.getIsFirstLogin());
					response.put("districtCode", getalldata.getDistrictCode());
					response.put("tahasilCode", getalldata.getTahasilCode());
					response.put("userRollId", rollid.get(0));
					response.put(CommonConstant.RESULT, jwtUtil.generateToken(authRequest.getUsername()));
				}
			}
		} else {
			response.put(CommonConstant.STATUS_KEY, 432);
			response.put(CommonConstant.RESULT, CommonConstant.INVALID_USERNAME_PASSWORD);
		}

		return ResponseEntity.ok(response.toString());
	}

	// Validate C
	private boolean captchaValidation(AuthRequest authRequest) {
		boolean validateCaptcha;
		Integer captchaResult = CommonCaptchaGenerate.get(authRequest.getCaptchaId());
		if (captchaResult != null && authRequest.getAnswer().equals(captchaResult)) {
			CommonCaptchaGenerate.remove(authRequest.getCaptchaId());
			validateCaptcha = true;
		} else {
			validateCaptcha = false;
		}
		return validateCaptcha;
	}

}
