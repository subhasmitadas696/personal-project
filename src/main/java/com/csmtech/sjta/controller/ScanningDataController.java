package com.csmtech.sjta.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.service.ScanningDataService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/scanning-data")
public class ScanningDataController {

	@Autowired
	private ScanningDataService scanning_data;
	String data = "";
	private static final Logger logger = LoggerFactory.getLogger(ScanningProgressReportController.class);
	JSONObject resp = new JSONObject();

	@Value("${rordigitalfile.path}")
	private String rorDigitalFile;

	@Value("${casefiledigitalfile.path}")
	private String caseFileDigitalFile;

	@PostMapping("/getFileList")
	public ResponseEntity<?> getFileList(@RequestBody String requestParam)
			throws JsonMappingException, JsonProcessingException {
		logger.info("Inside create method of Scanned_docController");
		JSONObject requestObj = new JSONObject(requestParam);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA), requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(requestParam);

			resp = scanning_data.getFileList(data);

		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@GetMapping(path = "/viewScanDocument/{fileType}/{fileName}", name = "View File")
	public ResponseEntity viewDocument(HttpServletResponse response, @PathVariable("fileType") Integer fileType,
			@PathVariable("fileName") String fileName) throws FileNotFoundException {
		logger.info(fileName);
		String filePath = "";
		if (fileType == 1) {
			filePath = rorDigitalFile;
		} else {
			filePath = caseFileDigitalFile;
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("content-disposition", "inline;filename=" + fileName);
		File file = new File(filePath + "/" + fileName);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		String contentType = "";

		if (null != fileName && fileName.contains(".")) {
			String fileExtension = fileName.split("\\.")[1];

			if (fileExtension.equalsIgnoreCase("pdf"))
				contentType = "application/pdf";
		}

		return ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.parseMediaType(contentType)).body(resource);
	}

	@PostMapping("/verify-document")
	public ResponseEntity<?> verifyDocument(@RequestBody String requestParam)
			throws JsonMappingException, JsonProcessingException {
		logger.info("Inside create method of Scanned_docController");
		JSONObject requestObj = new JSONObject(requestParam);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA), requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(requestParam);

			resp = scanning_data.verifyDocument(data);

		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/getRejectedFileList")
	public ResponseEntity<?> getRejectedFileList(@RequestBody String requestParam)
			throws JsonMappingException, JsonProcessingException {
		logger.info("Inside create method of Scanned_docController");
		JSONObject requestObj = new JSONObject(requestParam);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA), requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(requestParam);

			resp = scanning_data.getRejectedFileList(data);
		} else {
			resp.put("msg", "error");
			resp.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
}
