/**
 * 
 */
package com.csmtech.sjta.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.AuctionReportDTO;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.google.common.net.HttpHeaders;

/**
 * @author prasanta.sethi
 */

@RequestMapping("/auction-report")
@RestController
public class AuctionReportController {

	@Autowired
	private AuctionReportService reportService;

	JSONObject resp = new JSONObject();
	String data = null;

	@PostMapping("/getAuctionReport")
	public ResponseEntity<?> getDistrictRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<AuctionReportDTO> reportList = reportService.getAuctionReportData(data);
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

	@PostMapping("/getParticipantDetails")
	public ResponseEntity<?> getParticipantRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<AuctionReportDTO> reportList = reportService.getParticipantData(data);
			if (!reportList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, reportList);
				resp.put(CommonConstant.COUNT, reportService.getParticipantCount());
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

	@GetMapping(value = "/getAuctionReportForExcel", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> getAuctionReportForExcel() throws IOException {
		String fileName = "AuctionReport.xlsx";
		ByteArrayInputStream districtStream = reportService.getAuctionDataForReportExcel();
		InputStreamResource file = new InputStreamResource(districtStream);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}
	
	
	@PostMapping("/getBidderAllRecordHistory")
	public ResponseEntity<?> getBidderAllRecordHistory(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<AuctionReportDTO> reportList = reportService.getBidderAllRecordHistory(data);
			if (!reportList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, reportList);
				resp.put(CommonConstant.COUNT, reportService.getBidderAllRecordHistoryCount(data));
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, "No Record Found getBidderAllRecordHistory ");
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
}
