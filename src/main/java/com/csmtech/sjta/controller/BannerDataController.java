package com.csmtech.sjta.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.csmtech.sjta.dto.DistrictCode;
import com.csmtech.sjta.service.BannerDataService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/banner")

public class BannerDataController {
	/**
	 * @author prasanta.sethi
	 */
	
	String jsonResponse = null;

	@Autowired
	private BannerDataService bannerDataService;

	private static final Logger log = LoggerFactory.getLogger(BannerDataController.class);
	private static final String STATUS_KEY = "status";
	private static final String MESSAGE_KEY = "message";

	@PostMapping("/districtcodes")
	public ResponseEntity<String> getDistrictCodes(@RequestBody String formParams) {
	    Map<String, Object> response = new HashMap<>();
	    JSONObject requestObj = new JSONObject(formParams);
	    if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA), requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
	        List<DistrictCode> districtCodes = bannerDataService.getDistrictCodes();
	        log.info("District codes fetched successfully!");

	        if (districtCodes != null && !districtCodes.isEmpty()) {
	            response.put(STATUS_KEY, HttpStatus.OK.value());
	            response.put(MESSAGE_KEY, "DATA FOUND");
	            response.put("result", districtCodes);
	        } else {
	            response.put(STATUS_KEY, HttpStatus.OK.value());
	            response.put(MESSAGE_KEY, "NO DATA FOUND");
	        }
	    } else {
	        response.put(STATUS_KEY, HttpStatus.UNAUTHORIZED.value()); // Unauthorized status for token mismatch
	        response.put(MESSAGE_KEY, "Token verification failed");
	    }
		try {
			jsonResponse = new ObjectMapper().writeValueAsString(response);
		} catch (JsonProcessingException e) {
			log.info("Inside Exception in Banner Data"+ e.getMessage());
		}

	    return ResponseEntity.ok(CommonUtil.inputStreamEncoder(jsonResponse).toString());
	}
}
