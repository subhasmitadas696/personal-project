package com.csmtech.sjta.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.AuditTrailReportDTO;
import com.csmtech.sjta.service.AuditTrailReportService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auditTrailReport")
@Slf4j
public class AuditTrailReportController {

	@Autowired
	private AuditTrailReportService service;

	JSONObject resp = new JSONObject();
	String data = null;

	@PostMapping("/audit-report/getAuditTrailReport")
	public ResponseEntity<?> getAuditTrailReport(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject Json = new JSONObject(data);
			String formdate = Json.getString("formdate");
			String todate = Json.getString("todate");
			if (formdate.equalsIgnoreCase("0") && todate.equalsIgnoreCase("0")) {
				resp.put(CommonConstant.STATUS_KEY, 403);
				resp.put(CommonConstant.RESULT, "Kindly select the required fields to fetch details.");
			} else {
				List<AuditTrailReportDTO> districtList = service.getAuditTrailReport(data);
				if (!districtList.isEmpty()) {
					resp.put(CommonConstant.STATUS_KEY, 200);
					resp.put(CommonConstant.RESULT, districtList);
					resp.put(CommonConstant.COUNT, service.getAuditTrailReportCount(data));
				} else {
					resp.put(CommonConstant.STATUS_KEY, 404);
					resp.put(CommonConstant.RESULT, "No Record Found District");
				}
			}

		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		log.info("AuditTrailReportController of  getAuditTrailReport get exucation success..!!");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

}
