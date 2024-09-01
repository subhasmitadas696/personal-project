/**
 * 
 */
package com.csmtech.sjta.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.jfree.util.Log;
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

import com.csmtech.sjta.dto.IllegitimateLandUseDTO;
import com.csmtech.sjta.service.IllegitimateLandUseReportService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.google.common.net.HttpHeaders;

import lombok.extern.slf4j.Slf4j;

/**
 * @author prasanta.sethi
 */
@Slf4j
@RestController
@RequestMapping("/reports")
public class IllegitimateLandUseReportController {

	@Autowired
	private IllegitimateLandUseReportService reportService;

	JSONObject resp = new JSONObject();
	String data = null;

	@PostMapping("/illegitimate-land-use/getDistrictWiseRecord")
	public ResponseEntity<?> getDistrictRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<IllegitimateLandUseDTO> districtList = reportService.getDistrictReportData(data);
			if (!districtList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, districtList);
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

	@PostMapping("/illegitimate-land-use/getTahasilWiseRecord")
	public ResponseEntity<?> getTahasilWiseDetails(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<IllegitimateLandUseDTO> tahasilwiseList = reportService.getTahasilWiseDetails(data);
			if (!tahasilwiseList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, tahasilwiseList);
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

	@PostMapping("/illegitimate-land-use/getVillageWiseRecord")
	public ResponseEntity<?> getVillageWiseDetails(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<IllegitimateLandUseDTO> villagewiseList = reportService.getVillageWiseDetails(data);
			if (!villagewiseList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, villagewiseList);
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

	@PostMapping("/illegitimate-land-use/getDistrictStatusWiseRecord")
	public ResponseEntity<?> getDistrictStatusWiseRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<IllegitimateLandUseDTO> villagewiseList = reportService.getDistrictStatusWiseDetails(data);
			BigInteger count = reportService.getDistrictStatusWiseRecordCount(data);
			// New method to get count
			if (!villagewiseList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, villagewiseList);
				resp.put("count", count);
				// Include count in the response
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

	@PostMapping("/illegitimate-land-use/getTahasilStatusWiseRecord")
	public ResponseEntity<?> getTahasilStatusWiseRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<IllegitimateLandUseDTO> villagewiseList = reportService.getTahasilStatusWiseDetails(data);
			BigInteger count = reportService.getTahasilStatusWiseRecordCount(data);
			// New method to get count
			if (!villagewiseList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, villagewiseList);
				resp.put("count", count);
				// Include count in the response
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
	
	@PostMapping("/illegitimate-land-use/getVillageStatusWiseRecord")
	public ResponseEntity<?> getVillageStatusWiseRecord(@RequestBody String formParams) {
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			List<IllegitimateLandUseDTO> villageStauswiseList = reportService.getVillageStatusWiseDetails(data);
			BigInteger count = reportService.getVillageStatusWiseRecordCount(data);
			// New method to get count
			if (!villageStauswiseList.isEmpty()) {
				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put(CommonConstant.RESULT, villageStauswiseList);
				resp.put("count", count);
				// Include count in the response
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
	
	
	@GetMapping(value = "/illegitimate-land-use/generateDistrictWiseIllegitimateExcel", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generateDistrictWiseIllegitimateExcel() throws IOException {
		String fileName = "district_wise_illegitimate_land_use.xlsx";
		ByteArrayInputStream districtStream = reportService.getDistrictReportDataReport();
		InputStreamResource file = new InputStreamResource(districtStream);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}
	
	@GetMapping(value = "/illegitimate-land-use/generateTahasilWiseIllegitimateExcel/{districtCode}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> generateTahasilWiseIllegitimateExcel(HttpServletResponse response,@PathVariable("districtCode") String districtCode) throws IOException {
		String fileName = "tahasil_wise_illegitimate_land_use.xlsx";
		ByteArrayInputStream tahasilStream = reportService.getTahasilReportDataReport(districtCode);
		InputStreamResource file = new InputStreamResource(tahasilStream);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName)
				.contentType(MediaType.parseMediaType(CommonConstant.APPLICATION_MS_EXCEL)).body(file);
	}

}
