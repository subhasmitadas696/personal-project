package com.csmtech.sjta.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.service.DistrictMasterService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.csmtech.sjta.util.DistrictMasterValidationCheck;

@RestController

@RequestMapping("/land-bank")
public class DistrictMasterController {

	/**
	 * @author guru.prasad
	 */

	@Autowired
	private DistrictMasterService district_masterService;
	String data = "";
	private static final Logger logger = LoggerFactory.getLogger(DistrictMasterController.class);
	JSONObject resp = new JSONObject();

	@PostMapping("/district-master/addEdit")
	public ResponseEntity<?> create(@RequestBody String district_master) {
		logger.info("Inside create method of District_masterController");
		JSONObject requestObj = new JSONObject(district_master);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(district_master);
			String validationMsg = DistrictMasterValidationCheck.BackendValidation(new JSONObject(data));
			if (validationMsg != null) {
				resp.put(CommonConstant.STATUS_KEY, 502);
				resp.put("errMsg", validationMsg);
				logger.warn("Inside create method of District_masterController Validation Error");
			} else {
				resp = district_masterService.save(data);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/district-master/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) {
		logger.info("Inside getById method of District_masterController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			JSONObject entity = district_masterService.getById(json.getString("intId"));
			resp.put(CommonConstant.STATUS_KEY, 200);
			resp.put("result", entity);
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/district-master/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		logger.info("Inside getAll method of District_masterController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			resp = district_masterService.getAll(CommonUtil.inputStreamDecoder(formParams));
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@GetMapping("/district-master/viewall")
	public ResponseEntity<?> viewAll() {
		logger.info("Inside getAll method of District_masterController");
		resp = district_masterService.viewAll();
		return ResponseEntity.ok(resp.toString());
	}

}