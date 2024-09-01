package com.csmtech.sjta.controller;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.AddOfficerDTO;
import com.csmtech.sjta.dto.OfficerPaginationDTO;
import com.csmtech.sjta.dto.PaginationInRegisterDtoResponse;
import com.csmtech.sjta.dto.RoleDTO;
import com.csmtech.sjta.dto.UserDetails;
import com.csmtech.sjta.entity.Department;
import com.csmtech.sjta.entity.RoleEntity;
import com.csmtech.sjta.entity.User;
import com.csmtech.sjta.service.OfficerRegistrationService;
import com.csmtech.sjta.service.UserService;
import com.csmtech.sjta.util.CommonConstant;

@RestController
@RequestMapping("/api/officer")

public class OfficerRegistrationController {

	/**
	 * @author guru.prasad
	 */

	@Autowired
	private UserService userService;

	@Autowired
	private OfficerRegistrationService officerRegisterService;

	private static final Logger log = LoggerFactory.getLogger(OfficerRegistrationController.class);

	@PostMapping("/getalldept")
	public ResponseEntity<Map<String, Object>> getAllDept() {
		Map<String, Object> response = new HashMap<>();
		List<Department> list = officerRegisterService.getAllDept();
		if (list != null && !list.isEmpty()) {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			response.put(CommonConstant.MESSAGE_KEY, "DATA FOUND");
			response.put(CommonConstant.RESULT, list);
		} else {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.NOT_FOUND.value());
			response.put(CommonConstant.MESSAGE_KEY, "NO DATA FOUND");
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/getallrole")
	public ResponseEntity<Map<String, Object>> getAllRoleByDepartment(@RequestBody RoleDTO role) {
		Map<String, Object> response = new HashMap<>();
		List<RoleEntity> list = officerRegisterService.getAllRoleByDepartment(role.getDepartmentId());
		if (list != null && !list.isEmpty()) {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			response.put(CommonConstant.MESSAGE_KEY, "DATA FOUND");
			response.put(CommonConstant.RESULT, list);
		} else {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.NOT_FOUND.value());
			response.put(CommonConstant.MESSAGE_KEY, "NO DATA FOUND");
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> registerOfficer(@RequestBody UserDetails officerDTO,
			HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException {
		Map<String, Object> response = new HashMap<>();

		List<User> officerList = officerRegisterService.checkOffficerRecord(officerDTO.getUserName(),
				officerDTO.getMobileNo());
		if (officerList != null && !officerList.isEmpty()) {

			response.put(CommonConstant.STATUS_KEY, HttpStatus.BAD_REQUEST.value());
			response.put(CommonConstant.MESSAGE_KEY, "Username or Mobile Number is already registered. ");

		} else {
			log.info("data checked");
			Integer registerOfficer = officerRegisterService.registerOfficer(officerDTO);
			if (registerOfficer == 1) {
				response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				response.put(CommonConstant.MESSAGE_KEY, "Officer Registered Successfully");
			} else {
				response.put(CommonConstant.STATUS_KEY, HttpStatus.NO_CONTENT.value());
				response.put(CommonConstant.MESSAGE_KEY, "not saved");
			}
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/viewallofficer")
	public ResponseEntity<Map<String, Object>> viewAllOfficer(@RequestBody PaginationInRegisterDtoResponse res) {
		Map<String, Object> response = new HashMap<>();

		try {
			Integer countint = officerRegisterService.getTotalOfficerCount(res.getFullName());
			List<AddOfficerDTO> data = officerRegisterService.getOfficerInfo(res.getPageNumber(), res.getPageSize(),
					res.getFullName());

			response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			response.put(CommonConstant.MESSAGE_KEY, "Data retrieved successfully");
			response.put(CommonConstant.RESULT, new OfficerPaginationDTO(data, countint));

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.put(CommonConstant.MESSAGE_KEY, "An error occurred while processing the request");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/getonerecord")
	public ResponseEntity<Map<String, Object>> getDetails(@RequestBody UserDetails userDto,
			HttpServletRequest request) {
		log.info("get details started");
		Map<String, Object> response = new HashMap<>();
		try {
			User user = userService.findByuserId(userDto.getUserId());

			if (user != null) {
				response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				response.put(CommonConstant.MESSAGE_KEY, "Officer found");
				response.put("User_Details", user);

			} else {
				response.put(CommonConstant.STATUS_KEY, HttpStatus.NOT_FOUND.value());
				response.put(CommonConstant.MESSAGE_KEY, "Officer not found");
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
	public ResponseEntity<Map<String, Object>> updateDetails(@RequestBody UserDetails officerDto) {
		Map<String, Object> response = new HashMap<>();
		try {

			List<User> existingOfficerList = officerRegisterService.checkOffficerRecord(officerDto.getUserName(),
					officerDto.getMobileNo());
			boolean usernameOrMobileChanged = false;
			if (existingOfficerList != null && !existingOfficerList.isEmpty()) {
				for (User existingOfficer : existingOfficerList) {
					if (!existingOfficer.getUserId().equals(officerDto.getUserId())) {
						usernameOrMobileChanged = true;
						break;
					}
				}
			}

			if (existingOfficerList == null || existingOfficerList.isEmpty() || !usernameOrMobileChanged) {
				officerRegisterService.updateDetails(officerDto.getFullName(), officerDto.getMobileNo(),
						officerDto.getEmailId(), officerDto.getUserName(), officerDto.getDepartmentId(),
						officerDto.getRoleId(), officerDto.getUpdatedBy(), officerDto.getUserId(),
						officerDto.getCreatedBy());

				response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				response.put(CommonConstant.MESSAGE_KEY, "Officer details updated");
			} else {
				response.put(CommonConstant.STATUS_KEY, HttpStatus.BAD_REQUEST.value());
				response.put(CommonConstant.MESSAGE_KEY, "Username or Mobile Number is already registered.");
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.put(CommonConstant.MESSAGE_KEY, "Server Error");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/deleterecord")
	public ResponseEntity<Map<String, Object>> deleteRecord(@RequestBody UserDetails officerDto) {
		log.info("delete started");
		Map<String, Object> response = new HashMap<>();
		try {
			if (officerDto.getUserId() != null) {
				log.info("delete operation started");
				officerRegisterService.deleteRecord(officerDto.getCreatedBy(), officerDto.getUserId());
				log.info("deletion completed");
				response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				response.put(CommonConstant.MESSAGE_KEY, "Officer deleted");
			} else {
				response.put(CommonConstant.STATUS_KEY, HttpStatus.BAD_REQUEST.value());
				response.put(CommonConstant.MESSAGE_KEY, "Officer not deleted");
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("Error occurred while getting details: {}", e.getMessage());

			response.put(CommonConstant.STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.put(CommonConstant.MESSAGE_KEY, "Server Error");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
}
