package com.csmtech.sjta.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.LandAppRegistratationDTO;
import com.csmtech.sjta.dto.LandRegisterMobileNoOrOtpDTO;
import com.csmtech.sjta.dto.LandRegisterMobileNoOrOtpVerifiedDTO;
import com.csmtech.sjta.repository.LandAppRegistratationClassRepository;
import com.csmtech.sjta.service.LandAppRegistratationService;
import com.csmtech.sjta.util.CommonCaptchaGenerate;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/userregister")

@Slf4j
public class LandAppRegistratationController {

	@Autowired
	private LandAppRegistratationService service;

	@Autowired
	private LandAppRegistratationClassRepository repo;

	String data = "";

	@PostMapping("/saveuser")
	public ResponseEntity<?> saveUserRecord(@RequestBody LandAppRegistratationDTO dto)
			throws IOException, NoSuchAlgorithmException {

		// captcha validation
		Boolean validateCaptcha;
		Integer captchaResult = CommonCaptchaGenerate.get(dto.getCaptchaId());
		if (captchaResult != null && dto.getAnswer().equals(captchaResult)) {
			CommonCaptchaGenerate.remove(dto.getCaptchaId());
			validateCaptcha = true;
		} else {
			validateCaptcha = false;
		}

		if (Boolean.FALSE.equals(validateCaptcha)) {
			JSONObject response = new JSONObject();
			response.put(CommonConstant.STATUS_KEY, 223);
			response.put(CommonConstant.RESULT, "Invalid captcha");
			return ResponseEntity.ok(response.toString());
		}

		Integer existCheckRecord = repo.getUserCountByMobileAndEmail(dto.getMobileno());
		Integer existCheckCitizenRecord = repo.getUserCountOfCitizen(dto.getMobileno());

		Integer dataSave = null;
		Boolean checkCorrectPassword = dto.getPassword().equals(dto.getConformPassword());

		log.info("Start The saveUserRecord() method execution ");
		if (existCheckRecord == 0 && existCheckCitizenRecord == 0) {
			if (Boolean.TRUE.equals(checkCorrectPassword)) {
				dataSave = service.saveUserData(dto);
				if (dataSave == 1) {
					//
					log.info("  Data Saved Sucess.. ");
					JSONObject response = new JSONObject();
					response.put(CommonConstant.STATUS_KEY, 200);
					response.put(CommonConstant.RESULT, "Data save Sucess..!!");
					return ResponseEntity.ok(response.toString());
				} else {
					log.info("  Data Not  Saved .. ");
					JSONObject response = new JSONObject();
					response.put(CommonConstant.STATUS_KEY, 500);
					response.put(CommonConstant.RESULT, "Data Not Saves..!");
					return ResponseEntity.ok(response.toString());
				}
			} else {
				log.info("  Password Not Match  .. ");
				JSONObject response = new JSONObject();
				response.put(CommonConstant.STATUS_KEY, 110);
				response.put(CommonConstant.RESULT, "Password Not Match");
				return ResponseEntity.ok(response.toString());
			}

		} else {
			log.info("  Data Already Present Duplicate Entry .. ");
			JSONObject response = new JSONObject();
			response.put(CommonConstant.STATUS_KEY, 111);
			response.put(CommonConstant.RESULT, "Data Exist");
			return ResponseEntity.ok(response.toString());
		}

	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/savemobileno")
	public ResponseEntity insertMobileNoInFirstRegister(@RequestBody String formParams) {
		JSONObject response = new JSONObject();

		try {

			JSONObject requestObj = new JSONObject(formParams);

			// Verify the request token
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
				data = CommonUtil.inputStreamDecoder(formParams);
				ObjectMapper om = new ObjectMapper();
				LandRegisterMobileNoOrOtpDTO dto = om.readValue(data, LandRegisterMobileNoOrOtpDTO.class);

				if (dto.getType() != null) {
					// Check if the mobile number exists in temporary table
					Integer countTempData = repo.getUserCountByMobileAndEmailFirstRegisterTabTemp(dto.getMobileno());
					if (countTempData > 0) {
						// Update the user's mobile number
						Integer userUpdate = service.UpdateRegisterUserMobileNoOrOtp(dto.getMobileno());
					} else {
						// Save the user's mobile number
						Integer userSave = service.saveRegisterUserMobileNoOrOtp(dto.getMobileno());
					}
					// Prepare success response
					response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
					response.put("Response", "Verified Done Otp Sent");
					response.put(CommonConstant.RESULT, "Verify Success ..");
				} else {
					// Check if the mobile number exists in the first register table
					Integer countFirstRegisterMobileNo = repo
							.getUserCountByMobileAndEmailFirstRegisterTab(dto.getMobileno());
					Integer countFirstRegisterMobileNoOfUser = repo.getUserCountByMobileOfUser(dto.getMobileno());

					if (countFirstRegisterMobileNo == 0 && countFirstRegisterMobileNoOfUser == 0) {
						Integer countTempData = repo
								.getUserCountByMobileAndEmailFirstRegisterTabTemp(dto.getMobileno());
						Integer save1;
						if (countTempData > 0) {
							save1 = service.UpdateRegisterUserMobileNoOrOtp(dto.getMobileno());
						} else {
							save1 = service.saveRegisterUserMobileNoOrOtp(dto.getMobileno());
						}

						// Prepare success response
						response.put(CommonConstant.STATUS_KEY, 200);
						response.put("Response", save1);
						response.put(CommonConstant.RESULT, "Verify Success ..");
					} else {
						// Prepare duplicate entry response
						response.put(CommonConstant.STATUS_KEY, 500);
						response.put(CommonConstant.RESULT, "Mobile No Already Present Duplicate Entry ..");
					}
				}
			} else {
				response.put("msg", "error");
				response.put("status", 417);
			}
		} catch (Exception e) {
			log.error("An error occurred while processing the request: ", e.getMessage());
			response.put(CommonConstant.MESSAGE_KEY, "Something went wrong: ");
			response.put(CommonConstant.STATUS_KEY, 403);
		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/updatemobilenos")
	public ResponseEntity updateMobileNoInFirstRegister(@RequestBody LandRegisterMobileNoOrOtpDTO dto)
			throws NoSuchAlgorithmException {
		Integer counFirstRegisterMobileNo = repo.getUserCountByMobileAndEmailFirstRegisterTabTemp(dto.getMobileno());
		if (counFirstRegisterMobileNo == 1) {
			Integer res2 = service.UpdateRegisterUserMobileNoOrOtp(dto.getMobileno());
			JSONObject response = new JSONObject();
			response.put(CommonConstant.STATUS_KEY, 200);
			response.put(CommonConstant.RESULT, "Otp Save Sucess " + res2);
			return ResponseEntity.ok(response.toString());
		} else {
			JSONObject response = new JSONObject();
			response.put(CommonConstant.STATUS_KEY, 500);
			response.put(CommonConstant.RESULT, "Mobile No Not Exist ");
			return ResponseEntity.ok(response.toString());
		}
	}

	@PostMapping("/varifiedotp")
	public ResponseEntity<?> getOtpVerified(@RequestBody String formParams) {
		JSONObject response = new JSONObject();

		try {

			JSONObject requestObj = new JSONObject(formParams);

			// Verify the request token
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
				data = CommonUtil.inputStreamDecoder(formParams);
				ObjectMapper om = new ObjectMapper();
				LandRegisterMobileNoOrOtpVerifiedDTO dto = om.readValue(data,
						LandRegisterMobileNoOrOtpVerifiedDTO.class);

				String respones = service.getOTPByMobileNo(dto.getMobileno());
				if (dto.getOtp().equals(respones)) {
					response.put(CommonConstant.STATUS_KEY, 200);
					response.put(CommonConstant.RESULT, "Otp Validate Sucess ");
//					return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
				} else {
					response.put(CommonConstant.STATUS_KEY, 500);
					response.put(CommonConstant.RESULT, "Otp Not Match ");
//					return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
				}
			} else {
				response.put("msg", "error");
				response.put("status", 417);
			}
		} catch (Exception e) {
			log.error("An error occurred while processing the request: ", e.getMessage());
			response.put(CommonConstant.MESSAGE_KEY, "Something went wrong: ");
			response.put(CommonConstant.STATUS_KEY, 403);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());

	}

}
