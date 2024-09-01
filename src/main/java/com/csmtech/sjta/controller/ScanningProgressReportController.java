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

import com.csmtech.sjta.service.ScanningProgressReportService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController

@RequestMapping("/scanning-progress")
public class ScanningProgressReportController {
	@Autowired
	private ScanningProgressReportService scanning_progress_report;
	String data = "";
	private static final Logger logger = LoggerFactory.getLogger(ScanningProgressReportController.class);
	JSONObject resp = new JSONObject();

	@PostMapping("/updateProgress")
	public ResponseEntity<?> updateProgress(@RequestBody String requestParam)
			throws JsonMappingException, JsonProcessingException {
		logger.info("Inside create method of Scanned_docController");
		JSONObject requestObj = new JSONObject(requestParam);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA), requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(requestParam);

			resp = scanning_progress_report.saveProgressData(data);

		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
	
	@PostMapping("/getProgressReport")
	public ResponseEntity<?> getProgressReport(@RequestBody String requestParam)
			throws JsonMappingException, JsonProcessingException {
		logger.info("Inside create method of Scanned_docController");
		JSONObject requestObj = new JSONObject(requestParam);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA), requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(requestParam);

			resp = scanning_progress_report.getProgressData(data);

		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
}
