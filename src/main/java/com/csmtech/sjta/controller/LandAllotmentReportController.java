package com.csmtech.sjta.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.LandAllotmentReportDTO;
import com.csmtech.sjta.dto.LandAllotmentTahasilReportDTO;
import com.csmtech.sjta.dto.LandAllotmentVillageReportDTO;
import com.csmtech.sjta.service.LandAllotmentReportService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/landAllotReport")
@RestController
@Slf4j
public class LandAllotmentReportController {
	
	@Autowired
	private LandAllotmentReportService service;
	
	JSONObject resp = new JSONObject();
	String data = null;

	@PostMapping("/getDistrictWiseLandAllotmentReport")
	public ResponseEntity<?> getDistrictWiseLandAllotmentReport(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LandAllotmentReportDTO> districtList = service.getDistrictWiseLandAllotmentReport(data);
			if (!districtList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, districtList);
				resp.put(CommonConstant.COUNT, service.getDistrictWiseLandAllotmentReportCount());
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found District");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("LandAllotmentReportController of  getDistrictWiseLandAllotmentReport get exucation success..!!");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
	
	@PostMapping("/getTahasilWiseLandAllotmentReport")
	public ResponseEntity<?> getTahasilWiseLandAllotmentReport(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LandAllotmentTahasilReportDTO> districtList = service.getTahasilWiseLandAllotmentReport(data);
			if (!districtList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, districtList);
				resp.put(CommonConstant.COUNT, service.getTahasilWiseLandAllotmentReportCount(data));
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found District");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("LandAllotmentReportController of  getDistrictWiseLandAllotmentReport get exucation success..!!");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
	
	@PostMapping("/getVillageWiseLandAllotmentReport")
	public ResponseEntity<?> getVillageWiseLandAllotmentReport(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LandAllotmentVillageReportDTO> districtList = service.getVillageWiseLandAllotmentReport(data);
			if (!districtList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, districtList);
				resp.put(CommonConstant.COUNT, service.getVillageWiseLandAllotmentReportCount(data));
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found District");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("LandAllotmentReportController of  getDistrictWiseLandAllotmentReport get exucation success..!!");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

}
