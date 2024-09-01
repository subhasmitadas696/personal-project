package com.csmtech.sjta.controller;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.ChangePasswordRequest;
import com.csmtech.sjta.dto.UserDetails;
import com.csmtech.sjta.entity.CitizenProfileEntity;
import com.csmtech.sjta.entity.User;
import com.csmtech.sjta.service.CitizenService;
import com.csmtech.sjta.service.DashboardService;
import com.csmtech.sjta.service.UserService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;

@RestController
@RequestMapping("/api/users")

public class DashboardController {

	/**
	 * @author guru.prasad
	 */

	private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

	@Value("${sjta.bcryptpassword.secretKey}")
	private String secretkey;

	@Autowired
	private UserService userService;

	@Autowired
	private CitizenService citizenService;
	@Autowired
	private DashboardService dashboardService;

	JSONObject resp = new JSONObject();

	@PostMapping("/checkcurrpassword")
	public ResponseEntity<Map<String, Object>> checkCurrPassword(@RequestBody ChangePasswordRequest request,
			HttpServletRequest httpServletRequest, HttpSession session) {

		log.info("Check password method start.");

		Map<String, Object> response = new HashMap<>();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		try {
			User user = userService.findByuserNameFalse(request.getUserName());

			if (!encoder.matches(secretkey + request.getCurrentPassword(), user.getPassword())) {
				response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				response.put(CommonConstant.MESSAGE_KEY, "Current password is incorrect.");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			response.put(CommonConstant.STATUS_KEY, 204);
			response.put(CommonConstant.MESSAGE_KEY, "Current password is correct.");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error occurred while checking password: {}", e.getMessage());
			response.put(CommonConstant.STATUS_KEY, HttpStatus.BAD_REQUEST.value());
			response.put(CommonConstant.MESSAGE_KEY, "Invalid username");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/changepassword")
	public ResponseEntity<Map<String, Object>> changePassword(@RequestBody ChangePasswordRequest request,
			HttpServletRequest httpServletRequest, HttpSession session) {

		log.info("change password method start.");

		Map<String, Object> response = new HashMap<>();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		try {
			User user = userService.findByuserNameFalse(request.getUserName());

			if (!encoder.matches(secretkey + request.getCurrentPassword(), user.getPassword())) {
				response.put(CommonConstant.STATUS_KEY, HttpStatus.UNAUTHORIZED.value());
				response.put(CommonConstant.MESSAGE_KEY, "Current password is incorrect.");
			} else if (request.getCurrentPassword().equals(request.getNewPassword())) {
				response.put(CommonConstant.STATUS_KEY, HttpStatus.FORBIDDEN.value());
				response.put(CommonConstant.MESSAGE_KEY, "New Password should be different from the old password.");
			} else if (!request.getNewPassword().equals(request.getConfirmPassword())) {
				response.put(CommonConstant.STATUS_KEY, HttpStatus.BAD_REQUEST.value());
				response.put(CommonConstant.MESSAGE_KEY, "Confirm Password does not match with new password.");
			} else {
				String encodedPassword = encoder.encode(secretkey + request.getNewPassword());
				log.info("update password started");
				if ("C".equals(user.getUserType()) || "CI".equals(user.getUserType())) {
					log.info("inside citizen ps update");
					userService.updateCitizen(encodedPassword, request.getUpdatedBy(), request.getUserId(),
							request.getUserName());
				} else {
					log.info("inside officer or admin ps update");
					userService.updateUser(encodedPassword, request.getUpdatedBy(), request.getUserId(),
							request.getUserName());
				}

				log.info("password updation end");
				response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				response.put(CommonConstant.MESSAGE_KEY, "Password changed successfully.");
			}

			return ResponseEntity.status(
					(int) response.getOrDefault(CommonConstant.STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value()))
					.body(response);
		} catch (Exception e) {
			log.error("Error occurred while changing password: {}", e.getMessage());
			response.put(CommonConstant.STATUS_KEY, HttpStatus.BAD_REQUEST.value());
			response.put(CommonConstant.MESSAGE_KEY, "Invalid username");
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/current-user")
	public BigInteger getCurrentUser(HttpServletRequest httpServletRequest) {
		String username = httpServletRequest.getUserPrincipal().getName();
		return userService.findByUserName(username).getUserId();
	}

	@PostMapping("/getdetails")
	public ResponseEntity<Map<String, Object>> getDetails(@RequestBody UserDetails userDto,
			HttpServletRequest request) {
		Map<String, Object> response = new HashMap<>();
		User user = new User();
		CitizenProfileEntity profile = new CitizenProfileEntity();
		try {
			if ("O".equals(userDto.getUserType()) || "A".equals(userDto.getUserType())) {
				log.info("inside officer or admin check..");
				user = userService.findByuserIdFalse(userDto.getUserId());
			} else if ("CI".equals(userDto.getUserType()) || "C".equals(userDto.getUserType())) {
				log.info("inside citizen check..");
				profile = citizenService.findByCitizenId(userDto.getUserId());
			}

			if (user.getUserId() != null || profile.getCitizenProfileDetailsId() != null) {
				response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				response.put(CommonConstant.MESSAGE_KEY, "User found");
				response.put(CommonConstant.DATA, profile);
				response.put(CommonConstant.RESULT, user);

			} else {
				response.put(CommonConstant.STATUS_KEY, HttpStatus.NOT_FOUND);
				response.put(CommonConstant.MESSAGE_KEY, "user not found");

			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.error("Error occurred while getting details: {}", e.getMessage());
			response.put(CommonConstant.STATUS_KEY, HttpStatus.BAD_REQUEST.value());
			response.put(CommonConstant.MESSAGE_KEY, "Invalid userid");
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/updatedetails")
	public ResponseEntity<Map<String, Object>> updateDetails(@RequestBody UserDetails userDto) {
		log.info("update start");
		Map<String, Object> response = new HashMap<>();

		try {
			if (userDto.getUserId() != null) {
				if ("A".equals(userDto.getUserType()) || "O".equals(userDto.getUserType())) {
					log.info("updated start");
					userService.updateDetails(userDto.getMobileNo(), userDto.getFullName(), userDto.getEmailId(),
							userDto.getUpdatedBy(), userDto.getUserId());
					log.info("user details updated end");
				}

				CitizenProfileEntity existingProfile = citizenService.findByCitizenId(userDto.getUserId());

				if (existingProfile == null) {
					CitizenProfileEntity newProfile = new CitizenProfileEntity();
					newProfile.setAadhaarNo(userDto.getAadhaarNo());
					newProfile.setPanNo(userDto.getPanNo());
					newProfile.setSelBlockULB(userDto.getSelBlockULB());
					newProfile.setSelBlockULB19(userDto.getSelBlockULB19());
					newProfile.setSelDistrict(userDto.getSelDistrict());
					newProfile.setSelDistrict18(userDto.getSelDistrict18());
					newProfile.setSelGPWARDNumber(userDto.getSelGPWARDNumber());
					newProfile.setSelGPWardNo(userDto.getSelGPWardNo());
					newProfile.setSelState(userDto.getSelState());
					newProfile.setSelState17(userDto.getSelState17());
					newProfile.setSelVillageLocalAreaName(userDto.getSelVillageLocalAreaName());
					newProfile.setSelVillageLocalAreaName21(userDto.getSelVillageLocalAreaName21());
					newProfile.setTxtHabitationStreetNoLandmark(userDto.getTxtHabitationStreetNoLandmark());
					newProfile.setTxtHabitationStreetNoLandmark24(userDto.getTxtHabitationStreetNoLandmark24());
					newProfile.setTxtHouseNo(userDto.getTxtHouseNo());
					newProfile.setTxtHouseNo25(userDto.getTxtHouseNo25());
					newProfile.setTxtPinCode(userDto.getTxtPinCode());
					newProfile.setTxtPinCode26(userDto.getTxtPinCode26());
					newProfile.setTxtPoliceStation(userDto.getTxtPoliceStation());
					newProfile.setTxtPoliceStation22(userDto.getTxtPoliceStation22());
					newProfile.setTxtPostOffice(userDto.getTxtPostOffice());
					newProfile.setTxtPostOffice23(userDto.getTxtPostOffice23());
					newProfile.setIntUpdatedBy(userDto.getIntUpdatedBy());
					newProfile.setIsChecked(userDto.getIsChecked());
					citizenService.saveDetails(newProfile);
					log.info("New CitizenProfileEntity created");
				} else {
					existingProfile.setMobileno(userDto.getMobileNo());
					existingProfile.setFullName(userDto.getFullName());
					existingProfile.setEmailId(userDto.getEmailId());
					existingProfile.setAadhaarNo(userDto.getAadhaarNo());
					existingProfile.setPanNo(userDto.getPanNo());
					existingProfile.setSelBlockULB(userDto.getSelBlockULB());
					existingProfile.setSelBlockULB19(userDto.getSelBlockULB19());
					existingProfile.setSelDistrict(userDto.getSelDistrict());
					existingProfile.setSelDistrict18(userDto.getSelDistrict18());
					existingProfile.setSelGPWARDNumber(userDto.getSelGPWARDNumber());
					existingProfile.setSelGPWardNo(userDto.getSelGPWardNo());
					existingProfile.setSelState(userDto.getSelState());
					existingProfile.setSelState17(userDto.getSelState17());
					existingProfile.setSelVillageLocalAreaName(userDto.getSelVillageLocalAreaName());
					existingProfile.setSelVillageLocalAreaName21(userDto.getSelVillageLocalAreaName21());
					existingProfile.setTxtHabitationStreetNoLandmark(userDto.getTxtHabitationStreetNoLandmark());
					existingProfile.setTxtHabitationStreetNoLandmark24(userDto.getTxtHabitationStreetNoLandmark24());
					existingProfile.setTxtHouseNo(userDto.getTxtHouseNo());
					existingProfile.setTxtHouseNo25(userDto.getTxtHouseNo25());
					existingProfile.setTxtPinCode(userDto.getTxtPinCode());
					existingProfile.setTxtPinCode26(userDto.getTxtPinCode26());
					existingProfile.setTxtPoliceStation(userDto.getTxtPoliceStation());
					existingProfile.setTxtPoliceStation22(userDto.getTxtPoliceStation22());
					existingProfile.setTxtPostOffice(userDto.getTxtPostOffice());
					existingProfile.setTxtPostOffice23(userDto.getTxtPostOffice23());
					existingProfile.setIntUpdatedBy(userDto.getIntUpdatedBy());
					existingProfile.setIsChecked(userDto.getIsChecked());
					citizenService.saveDetails(existingProfile);
					log.info("CitizenProfileEntity already exists, so update it");
				}

				response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				response.put(CommonConstant.MESSAGE_KEY, "User details updated");
			} else {
				response.put(CommonConstant.STATUS_KEY, HttpStatus.BAD_REQUEST.value());
				response.put(CommonConstant.MESSAGE_KEY, "User details not updated");
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("Error occurred while updating details: {}", e.getMessage());
			response.put(CommonConstant.STATUS_KEY, HttpStatus.BAD_REQUEST.value());
			response.put(CommonConstant.MESSAGE_KEY, "Server Error");
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/getDistrictDetailsCitizen")
	public ResponseEntity<?> getDistrictDetailsCitizen(@RequestBody String formParams) {
		log.info("Inside getAll method of getDistrictDetailsCitizen");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {

			resp.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			resp.put(CommonConstant.RESULT,
					dashboardService.getDistrictDetailsCitizen(CommonUtil.inputStreamDecoder(formParams)));

		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/getLandDetails")
	public ResponseEntity<?> getLandDetails(@RequestBody String formParams) {
		log.info("Inside  method of getLandDetails");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {

			resp.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			resp.put(CommonConstant.RESULT, dashboardService.getLandDetails(CommonUtil.inputStreamDecoder(formParams)));

		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/getApplicationStatistics")
	public ResponseEntity<?> getApplicationStatistics(@RequestBody String formParams) {
		log.info("Inside method of getApplicationStatistics");
		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {

				resp.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				resp.put(CommonConstant.RESULT,
						dashboardService.getApplicationStatistics(CommonUtil.inputStreamDecoder(formParams)));

			} else {
				resp.put("msg", CommonConstant.ERROR);
				resp.put(CommonConstant.STATUS_KEY, 417);
				resp.put(CommonConstant.RESULT, "");
			}
		} catch (Exception e) {
			resp.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			resp.put("msg", "data is not present");
			resp.put(CommonConstant.RESULT, "");
		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/getGrievanceStatus")
	public ResponseEntity<?> getGrievanceStatus(@RequestBody String formParams) {
		log.info("Inside method of getGrievanceStatus");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {

			resp.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			resp.put(CommonConstant.RESULT,
					dashboardService.getGrievanceStatus(CommonUtil.inputStreamDecoder(formParams)));

		} else {
			resp.put("msg", "error");
			resp.put(CommonConstant.STATUS_KEY, 417);
			resp.put(CommonConstant.RESULT, "");
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/paymentPending")
	public ResponseEntity<?> fetchPendingPayment(@RequestBody String formParams) {
		try {
			log.info("fetching process of pending payment details started");
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {

				resp.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				resp.put(CommonConstant.RESULT,
						dashboardService.fetchPendingPayment(CommonUtil.inputStreamDecoder(formParams)));
			} else {
				resp.put("msg", "error");
				resp.put(CommonConstant.STATUS_KEY, 417);
				resp.put(CommonConstant.RESULT, "");
			}

		} catch (Exception e) {
			log.error("Error occurred in payment pending: {}", e.getMessage());
			resp.put(CommonConstant.STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value());
			resp.put("message", "error occured in payment pending");
			resp.put(CommonConstant.RESULT, "");

		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
	
	@PostMapping("/paymentLandSell")
	public ResponseEntity<?> fetchLandSellPayment(@RequestBody String formParams) {
		try {
			log.info("fetching process of pending payment details started");
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {

				resp.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				resp.put(CommonConstant.RESULT,
						dashboardService.fetchLandSellPayment(CommonUtil.inputStreamDecoder(formParams)));
			} else {
				resp.put("msg", "error");
				resp.put(CommonConstant.STATUS_KEY, 417);
				resp.put(CommonConstant.RESULT, "");
			}

		} catch (Exception e) {
			log.error("Error occurred in payment pending: {}", e.getMessage());
			resp.put(CommonConstant.STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value());
			resp.put("message", "error occured in payment pending");
			resp.put(CommonConstant.RESULT, "");

		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
	
	@PostMapping("/getDistrictDetailsLease")
	public ResponseEntity<?> getDistrictDetailsLease(@RequestBody String formParams) {
		log.info("Inside getAll method of getDistrictDetailsCitizen");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {

			resp.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			resp.put(CommonConstant.RESULT,
					dashboardService.getDistrictDetailsLease(CommonUtil.inputStreamDecoder(formParams)));

		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
			resp.put(CommonConstant.RESULT, "");
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
}
