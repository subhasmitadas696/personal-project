/**
 * 
 */
package com.csmtech.sjta.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.LandApplicationApprovalSlaDTO;
import com.csmtech.sjta.service.LandAppApprovalSlaReportService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author prasanta.sethi
 */
@Slf4j
@RequestMapping("/sla-report")
@RestController
public class LandAppApprovalSlaReportController {

	@Autowired
	private LandAppApprovalSlaReportService reportService;

	JSONObject resp = new JSONObject();
	String data = null;

	@PostMapping("/getApplicationApprovalReport")
	public ResponseEntity<?> getReportRecord(@RequestBody String formParams) {
		log.info("Inside getApplicationApprovalReport !!");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LandApplicationApprovalSlaDTO> reportList = reportService.getLandApplicationReportData(data);
			if (!reportList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, reportList);
				resp.put(CommonConstant.COUNT, reportService.getReportCount());
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found District");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/getAuctionApprovalReport")
	public ResponseEntity<?> getAuctionApprovalReport(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LandApplicationApprovalSlaDTO> reportList = reportService.getAuctionApprovalReportData(data);
			if (!reportList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, reportList);
				resp.put(CommonConstant.COUNT, reportService.getAuctionReportCount());
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found District");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
	
	
	@PostMapping("/getLandVerificationReport")
	public ResponseEntity<?> getLandVerificationReport(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LandApplicationApprovalSlaDTO> reportList = reportService.getLandVerificationReportData(data);
			System.out.println(reportList.toString());
			if (!reportList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, reportList);
				resp.put(CommonConstant.COUNT, reportService.getLandVerificationReportCount());
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found !!");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
	
	
	

//	@GetMapping(value = "/getAuctionReportForExcel", produces = MediaType.APPLICATION_PDF_VALUE)
//	public ResponseEntity<Resource> getAuctionReportForExcel() throws IOException {
//		String fileName = "AuctionReport.xlsx";
//		ByteArrayInputStream districtStream = reportService.getAuctionDataForReportExcel();
//		InputStreamResource file = new InputStreamResource(districtStream);
//		return ResponseEntity.ok()
//				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
//				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
//	}

}
