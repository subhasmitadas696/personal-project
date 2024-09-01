package com.csmtech.sjta.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.LandApplicationReportDTO;
import com.csmtech.sjta.service.LandApplicationReportService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.google.common.net.HttpHeaders;

@RestController
@RequestMapping("/reports")
public class LandApplicationReportController {

	@Autowired
	private LandApplicationReportService landReportService;

	JSONObject resp = new JSONObject();
	String data = null;

	@PostMapping("/landapplication/getLandAppDetails")
	public ResponseEntity<?> getLandRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LandApplicationReportDTO> landAppDetails = landReportService.getLandReportData(data);
			if (!landAppDetails.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, landAppDetails);
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, CommonConstant.NO_RECORD_FOUND);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/landapplication/getDistrictWiseLandRecord")
	public ResponseEntity<?> getDistrictWiseLandRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LandApplicationReportDTO> landAppDetails = landReportService.getDistrictWiseLandRecord(data);
			if (!landAppDetails.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, landAppDetails);
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, CommonConstant.NO_RECORD_FOUND);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/landapplication/getTahasilWiseLandRecord")
	public ResponseEntity<?> getTahasilWiseLandRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LandApplicationReportDTO> landAppDetails = landReportService.getTahasilWiseLandRecord(data);
			if (!landAppDetails.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, landAppDetails);
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, CommonConstant.NO_RECORD_FOUND);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
	
	@PostMapping("/landapplication/getApplicationDetails")
	public ResponseEntity<?> getApplicationDetails(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<LandApplicationReportDTO> landAppDetails = landReportService.getApplicationDetails(data);
			if (!landAppDetails.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, landAppDetails);
			} else {
				resp.put(CommonConstant.STATUS_KEY, 404);
				resp.put(CommonConstant.RESULT, CommonConstant.NO_RECORD_FOUND);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
	
	
	@GetMapping(value = "/landapplication/getLandAppDetailsExcel", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generateLandReport() throws IOException {
		String fileName = "LandRecord.xlsx";
		ByteArrayInputStream districtLandStream = landReportService.exportReportForLand();
		InputStreamResource file = new InputStreamResource(districtLandStream);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}
	
	@GetMapping(value = "/landapplication/getDistrictWiseLandExcel/{districtCode}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generateLandTahasilReport(HttpServletResponse response,@PathVariable("districtCode") String districtCode) throws IOException {
		String fileName = "Tahasil_Land_Record.xlsx";
		ByteArrayInputStream tahasilLandStream = landReportService.exportReportForLandTahasil(districtCode);
		InputStreamResource file = new InputStreamResource(tahasilLandStream);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}
	
	@GetMapping(value = "/landapplication/getTahasilWiseLandExcel/{tahasilCode}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generateLandVillageReport(HttpServletResponse response,@PathVariable("tahasilCode") String tahasilCode) throws IOException {
		String fileName = "Village_Land_Record.xlsx";
		ByteArrayInputStream villageLandStream = landReportService.exportReportForLandVillage(tahasilCode);
		InputStreamResource file = new InputStreamResource(villageLandStream);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}
	
}
