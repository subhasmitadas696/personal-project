package com.csmtech.sjta.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.ApplicantNumberAndMobileDTO;
import com.csmtech.sjta.repository.LandApprovalRepository;
import com.csmtech.sjta.service.CommonPdfService;
import com.csmtech.sjta.service.LandApplicantService;
import com.csmtech.sjta.service.Land_applicantService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.csmtech.sjta.util.LandApplicantValidationCheck;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@RestController

@RequestMapping("/land-purchase-application")
@Slf4j
public class LandApplicantController {
	@Autowired
	private Land_applicantService landApplicantService;
	String path = "src/storage/individual-details/";
	String data = "";

	@Autowired
	LandApprovalRepository repo;
	@Autowired
	private LandApplicantService service;

	@Autowired
	private CommonPdfService servicepdf;

	@PostMapping("/individual-details/addEdit")
	public ResponseEntity<?> create(@RequestBody String landApplicant) {
		data = landApplicant;
		JSONObject resp = new JSONObject();
		if (LandApplicantValidationCheck.BackendValidation(new JSONObject(data)) != null) {
			resp.put(CommonConstant.STATUS_KEY, 502);
			resp.put("errMsg", LandApplicantValidationCheck.BackendValidation(new JSONObject(data)));
		} else {
			resp = landApplicantService.userSave(data);
		}
		return ResponseEntity.ok(resp.toString());
	}

	@PostMapping("/individual-details/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) {
		data = formParams;
		JSONObject json = new JSONObject(data);
		JSONObject entity = landApplicantService.getById(json.getInt(CommonConstant.INTID));
		JSONObject jsb = new JSONObject();
		jsb.put(CommonConstant.STATUS_KEY, 200);
		jsb.put(CommonConstant.RESULT, entity);
		return ResponseEntity.ok(jsb.toString());
	}

	@PostMapping("/individual-details/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		JSONArray entity = landApplicantService.getAll(CommonUtil.inputStreamDecoder(formParams));
		JSONObject jsonobj = new JSONObject();
		jsonobj.put(CommonConstant.STATUS_KEY, 200);
		jsonobj.put(CommonConstant.RESULT, entity);
		return ResponseEntity.ok(jsonobj.toString());
	}

	@PostMapping("/individual-details/delete")
	public ResponseEntity<?> delete(@RequestBody String formParams) {
		data = CommonUtil.inputStreamDecoder(formParams);
		return ResponseEntity.ok("");
	}

	@GetMapping("/individual-details/download/{name}")
	public ResponseEntity<Resource> download(@PathVariable("name") String name) throws IOException {
		File file = new File(path + name);
		Path path = Paths.get(file.getAbsolutePath());
		ByteArrayResource byteArrayResource = new ByteArrayResource(Files.readAllBytes(path));
		return ResponseEntity.ok().headers(headers(name)).contentLength(file.length())
				.contentType(MediaType.parseMediaType("application/octet-stream")).body(byteArrayResource);
	}

	private HttpHeaders headers(String name) {
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");
		return header;
	}

	@PostMapping("/updateApplicant")
	public ResponseEntity<?> updateApplicantNumberDetails(@RequestBody String formParams) {
		JSONObject json = new JSONObject(formParams);
		Integer result = service.updateApplicantName(json.getBigInteger(CommonConstant.INTID), json.getString("razorpayOrderId"),
				json.getString("razorpaySignature"), json.getString("razorpayPaymentId"), json.getInt("userId"),
				json.getBigDecimal("paymentAmount"),json.getInt("roleId"));
		log.info("payment success" + result);
		JSONObject jsonobj = new JSONObject();
		jsonobj.put(CommonConstant.STATUS_KEY, 200);
		jsonobj.put("message", "Success");

		return ResponseEntity.ok(jsonobj.toString());
	}

	// return the applicant or mobile no
	@PostMapping("/getAppnoorMobile")
	public ResponseEntity<?> fetchApplicantInfoById(@RequestBody String formParams) {
		JSONObject jsonobj = new JSONObject();
		JSONObject json = new JSONObject(formParams);
		List<ApplicantNumberAndMobileDTO> respones = service.fetchApplicantInfoById(json.getBigInteger(CommonConstant.INTID));
		if (respones.isEmpty()) {
			jsonobj.put(CommonConstant.STATUS_KEY, 404);
			jsonobj.put(CommonConstant.RESULT, "No Result Are Found");
		}
		jsonobj.put(CommonConstant.STATUS_KEY, 200);
		jsonobj.put(CommonConstant.RESULT, respones);

		return ResponseEntity.ok(jsonobj.toString());
	}

	@PostMapping(value = "/generatePdfApplicant", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<?> generateReport(@RequestBody String formParams) throws FileNotFoundException, JRException {
		JSONObject json = new JSONObject(formParams);
		byte[] filename = servicepdf.exportReport("pdf", json.getBigInteger(CommonConstant.INTID));
		log.info(":: respones Done sucessfully.");
		return ResponseEntity.ok(filename);
	}

}