package com.csmtech.sjta.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.ForgotPasswordCaptchaFinalRequest;
import com.csmtech.sjta.dto.ForgotPasswordRequest;
import com.csmtech.sjta.entity.LandAppRegistrationEntity;
import com.csmtech.sjta.service.ForgotPasswordService;
import com.csmtech.sjta.util.CommonCaptchaGenerate;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")

@Slf4j
public class ForgotPasswordController {
	@Autowired
	private ForgotPasswordService forgotPasswordService;
	JSONObject response = new JSONObject();

	@Value("${sjta.bcryptpassword.secretKey}")
	private String SECRET_KEY;

	String data = "";

	@PostMapping("/sendotp")
	public ResponseEntity<String> forgotPassword(@RequestBody String formParams) {
		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
				data = CommonUtil.inputStreamDecoder(formParams);
				ObjectMapper om = new ObjectMapper();
				ForgotPasswordRequest request = om.readValue(data, ForgotPasswordRequest.class);
				LandAppRegistrationEntity user = forgotPasswordService.findByMobileNo(request.getMobileNo());
				if (user != null) {
					if (user.getMobileno().equals(request.getMobileNo())
							|| user.getUsername().equals(request.getMobileNo())) {
						forgotPasswordService.updateOtp(user);
						response.put(CommonConstant.MESSAGE_KEY, "OTP Sent Successfully");
						response.put(CommonConstant.STATUS_KEY, 200);
						log.info("inside forgotPassword() in controller");
					} else {
						response.put(CommonConstant.MESSAGE_KEY, "No User Id Found");
						response.put(CommonConstant.STATUS_KEY, 401);
					}
				} else {
					response.put(CommonConstant.MESSAGE_KEY, "No User Found");
					response.put(CommonConstant.STATUS_KEY, 401);
				}
			} else {
				response.put("msg", "error");
				response.put("status", 417);
			}

		} catch (Exception e) {
			log.info(e.getMessage());
			response.put(CommonConstant.MESSAGE_KEY, "No User Found");
			response.put(CommonConstant.STATUS_KEY, 401);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}

	@PostMapping("/matchotp")
	public ResponseEntity<String> matchOtp(@RequestBody String formParams) {
		log.info("inside matchOtp() in controller");
		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
				data = CommonUtil.inputStreamDecoder(formParams);
				ObjectMapper om = new ObjectMapper();
				ForgotPasswordRequest request = om.readValue(data, ForgotPasswordRequest.class);
				LandAppRegistrationEntity user = forgotPasswordService.findByMobileNo(request.getMobileNo());
				if (request.getOtp().equals(user.getOtp())) {
					response.put(CommonConstant.MESSAGE_KEY, "OTP matched");
					response.put(CommonConstant.STATUS_KEY, 200);
				} else {
					response.put(CommonConstant.MESSAGE_KEY, "Error Validating OTP");
					response.put(CommonConstant.STATUS_KEY, 401);
				}

			} else {
				response.put("msg", "error");
				response.put("status", 417);
			}

		} catch (Exception e) {
			log.info(e.getMessage());
			response.put(CommonConstant.MESSAGE_KEY, "Something went wrong");
			response.put(CommonConstant.STATUS_KEY, 403);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());

	}

	@PostMapping("/setnewpassword")
	public ResponseEntity<?> setnewpassword(@RequestBody String formParams)
			throws JsonProcessingException {
		log.info("inside setnewpassword() in controller");
		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
				data = CommonUtil.inputStreamDecoder(formParams);
				ObjectMapper om = new ObjectMapper();
				ForgotPasswordCaptchaFinalRequest request = om.readValue(data, ForgotPasswordCaptchaFinalRequest.class);
				boolean validateCaptcha;
				Integer captchaResult = CommonCaptchaGenerate.get(request.getCaptchaId());

				if (captchaResult != null && request.getAnswer() == captchaResult) {
					CommonCaptchaGenerate.remove(request.getCaptchaId());
					validateCaptcha = true;
				} else {
					validateCaptcha = false;
				}

				if (!validateCaptcha) {
					response.put(CommonConstant.STATUS_KEY, 223);
					response.put(CommonConstant.MESSAGE_KEY, "Invalid captcha");
					return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
				}

				if (request.getNewPassword().equals(request.getConfirmPassword())) {
					BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
					String encodedPassword = encoder.encode(SECRET_KEY + request.getNewPassword());

					if (encoder.matches(SECRET_KEY + request.getNewPassword(),
							forgotPasswordService.checkPassword(request.getNewPassword(), request.getMobileNo()))) {
						response.put(CommonConstant.MESSAGE_KEY, "Password can not be same as previous password.");
						response.put(CommonConstant.STATUS_KEY, 401);
					} else {
						forgotPasswordService.updatepassword(encodedPassword, request.getMobileNo());
						response.put(CommonConstant.MESSAGE_KEY, "Password Updated Successfully");
						response.put(CommonConstant.STATUS_KEY, 200);
					}

				} else {
					response.put(CommonConstant.MESSAGE_KEY, "Error Updating Password");
					response.put(CommonConstant.STATUS_KEY, 401);
				}

			} else {
				response.put("msg", "error");
				response.put("status", 417);
			}
		} catch (Exception e) {
			log.info(e.getMessage());
			response.put(CommonConstant.MESSAGE_KEY, "Something went wrong");
			response.put(CommonConstant.STATUS_KEY, 403);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());

	}

}
