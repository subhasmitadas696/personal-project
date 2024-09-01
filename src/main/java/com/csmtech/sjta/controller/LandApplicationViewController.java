package com.csmtech.sjta.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.LandAppHistoryDTO;
import com.csmtech.sjta.dto.LandAppResponseStructureDTO;
import com.csmtech.sjta.dto.LandApplicantDTO;
import com.csmtech.sjta.dto.LandPaginationDTO;
import com.csmtech.sjta.dto.PaginationInRegisterDtoResponse;
import com.csmtech.sjta.service.LandApplicantService;
import com.csmtech.sjta.util.CommonConstant;

@RestController
@RequestMapping("/api/landservices")

public class LandApplicationViewController {

	/**
	 * @author guru.prasad
	 */

	@Autowired
	private LandApplicantService landApplicantService;

	private static final Logger log = LoggerFactory.getLogger(LandApplicationViewController.class);

	@PostMapping("/searchpagination")
	public ResponseEntity<?> getPaginationSearchData(@RequestBody PaginationInRegisterDtoResponse res) {
		LandPaginationDTO getdtodata = landApplicantService.getSearchLandApplicantDetailsPage(res.getRoleId(),
				res.getDistrictCode(), res.getPageNumber(), res.getPageSize(), res.getTahasilCode(),
				res.getVillageCode(), res.getKhatianCode(), res.getPageType());
		if (getdtodata == null) {
			log.info(":: getPaginationSearchData() execution Sucess No Record Found..!!");
			JSONObject response = new JSONObject();
			response.put(CommonConstant.STATUS_KEY, 404);
			response.put(CommonConstant.RESULT, "No Record Found");
			return ResponseEntity.ok(response.toString());
		} else
			log.info(":: getPaginationSearchData() execution Sucess ..!!");
		return ResponseEntity.ok(getdtodata);
	}

	@PostMapping("/viewmore")
	public ResponseEntity<Map<String, Object>> getDetails(@RequestBody LandApplicantDTO landDto,
			HttpServletRequest request) {
		log.info("get land details started");
		Map<String, Object> response = new HashMap<>();
		try {
			LandAppResponseStructureDTO land = landApplicantService
					.findAllDetailsBylandApplicantId(landDto.getLandApplicantId());

			if (land != null) {
				response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				response.put(CommonConstant.MESSAGE_KEY, "Land Details found");
				response.put("Land_Details", land);

			} else {
				response.put(CommonConstant.STATUS_KEY, HttpStatus.NOT_FOUND.value());
				response.put(CommonConstant.MESSAGE_KEY, "Land Details not found");
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.error("Error occurred while getting details: {} inside viewmore", e.getMessage());
			response.put(CommonConstant.STATUS_KEY, HttpStatus.BAD_REQUEST.value());
			response.put(CommonConstant.MESSAGE_KEY, "Invalid landId");
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/viewapphistory")
	public ResponseEntity<?> viewApplicationHistory(@RequestBody PaginationInRegisterDtoResponse res) {
		LandAppHistoryDTO getdtodata = landApplicantService.viewApplicationHistory(res.getLandApplicantId());
		JSONObject response = new JSONObject();

		if (getdtodata == null) {
			log.info(": :viewApplicationHistory() execution Sucess No Record Found..!!");
			response.put(CommonConstant.STATUS_KEY, 404);
			response.put(CommonConstant.RESULT, "No Record Found");
			return ResponseEntity.ok(response.toString());
		} else
			log.info(":: viewApplicationHistory() execution Sucess ..!!");
		return ResponseEntity.ok(getdtodata);
	}

	@PostMapping("/viewactivityhistory")
	public ResponseEntity<?> viewActivityHistory(@RequestBody PaginationInRegisterDtoResponse res) {
		LandAppHistoryDTO getdtodata = landApplicantService.viewCitizenApplicationHistory(res.getLandApplicantId());
		Map<String, Object> response = new HashMap<>();
		try {
			if (getdtodata != null) {
				log.info(":: viewactivityhistory() execution Sucess ..!!");
				response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				response.put(CommonConstant.RESULT, getdtodata);
				return ResponseEntity.ok(response);
			} else {
				log.info(":: viewactivityhistory() no record..!!");
				return ResponseEntity.noContent().build();

			}
		} catch (Exception e) {
			log.error("Error retrieving activity history report: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving activity history report.");
		}
	}

	@PostMapping("/getactivityhistoryreport")
	public ResponseEntity<?> getActivityHistoryReport(@RequestBody PaginationInRegisterDtoResponse res) {
		try {
			Map<String, Object> response = new HashMap<>();

			LandAppHistoryDTO getdtodata = landApplicantService.getApplicationHistoryReport(res.getPageSize(),
					res.getPageNumber());
			Integer count = landApplicantService.countOfAppHistoryReport();
			if (getdtodata != null) {
				log.info("Activity history report successfully retrieved.");
				response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
				response.put(CommonConstant.RESULT, getdtodata);
				response.put(CommonConstant.COUNT, count);
				return ResponseEntity.ok(response);
			} else {
				log.info("No records found for activity history report.");
				return ResponseEntity.noContent().build();
			}
		} catch (Exception e) {
			log.error("Error retrieving activity history report: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving activity history report.");
		}
	}

}
