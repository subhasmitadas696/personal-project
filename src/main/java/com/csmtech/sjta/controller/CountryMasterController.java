package com.csmtech.sjta.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.service.CountryMasterService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.csmtech.sjta.util.CountryMasterValidationCheck;

@RestController

@RequestMapping("/land-bank")
public class CountryMasterController {

	/**
	 * @author guru.prasad
	 */

	@Autowired
	private CountryMasterService country_masterService;
	String data = "";
	private static final Logger logger = LoggerFactory.getLogger(CountryMasterController.class);
	private static final String error = "error";
	JSONObject resp = new JSONObject();

	@PostMapping("/country-master/addEdit")
	public ResponseEntity<?> create(@RequestBody String country_master) {
		logger.info("Inside create method of Country_masterController");
		JSONObject requestObj = new JSONObject(country_master);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(country_master);
			String validationMsg = CountryMasterValidationCheck.BackendValidation(new JSONObject(data));
			if (validationMsg != null) {
				resp.put(CommonConstant.STATUS_KEY, 502);
				resp.put("errMsg", validationMsg);
				logger.warn("Inside create method of Country_masterController Validation Error");
			} else {
				resp = country_masterService.save(data);
			}
		} else {
			resp.put("msg", error);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/country-master/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) {
		logger.info("Inside getById method of Country_masterController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			JSONObject entity = country_masterService.getById(json.getString("intId"));
			resp.put(CommonConstant.STATUS_KEY, 200);
			resp.put("result", entity);
		} else {
			resp.put("msg", error);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/country-master/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		logger.info("Inside getAll method of Country_masterController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			resp = country_masterService.getAll(CommonUtil.inputStreamDecoder(formParams));
		} else {
			resp.put("msg", error);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

}