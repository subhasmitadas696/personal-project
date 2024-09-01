package com.csmtech.sjta.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.csmtech.sjta.dto.GrievanceGoSeenRecordDTO;
import com.csmtech.sjta.dto.GrievanceMainDTO;
import com.csmtech.sjta.service.GrievanceService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.csmtech.sjta.util.GrievanceValidationCheck;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@RestController

@RequestMapping("/grievance-module")
@Slf4j
public class GrievanceController {

	@Autowired
	private GrievanceService grievanceService;
	String data = "";
	private static final Logger logger = LoggerFactory.getLogger(GrievanceController.class);
	JSONObject resp = new JSONObject();
	@Value("${file.path}")
	private String finalUploadPath;

	@PostMapping("/grievance/addEdit")
	public ResponseEntity<?> create(@RequestBody String grievance) throws JsonProcessingException {
		logger.info("Inside create method of GrievanceController");
		JSONObject requestObj = new JSONObject(grievance);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(grievance);
			String validationMsg = GrievanceValidationCheck.BackendValidation(new JSONObject(data));

			if (validationMsg != null) {
				resp.put(CommonConstant.STATUS_KEY, 502);
				resp.put("errMsg", validationMsg);
				logger.warn("Inside create method of GrievanceController Validation Error");
			} else {
				resp = grievanceService.save(data);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/grievance/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) {
		logger.info("Inside getById method of GrievanceController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			JSONObject entity = grievanceService.getById(json.getInt("intId"));
			resp.put(CommonConstant.STATUS_KEY, 200);
			resp.put(CommonConstant.RESULT, entity);
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/grievance/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		logger.info("Inside getAll method of GrievanceController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			resp = grievanceService.getAll(CommonUtil.inputStreamDecoder(formParams));
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/grievance/delete")
	public ResponseEntity<?> delete(@RequestBody String formParams) {
		logger.info("Inside delete method of GrievanceController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			resp = grievanceService.deleteById(json.getInt("intId"));
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@GetMapping("/grievance/download/{name}")
	public ResponseEntity<Resource> download(@PathVariable("name") String name) throws IOException {
		logger.info("Inside download method of GrievanceController");
		File file = new File(finalUploadPath + "/" + "grievance/" + name);
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

	@PostMapping("/grievance/goLoginRecord")
	public ResponseEntity<?> getGrivanceAllGoRequiredRecord(@RequestBody String formParams) { // 1
		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
				data = CommonUtil.inputStreamDecoder(formParams);

				JSONObject jsonobj = new JSONObject(data);
				Integer pageType = jsonobj.optInt("pageType");
				Integer pageNo = jsonobj.optInt("pageNo");
				Integer pageSize = jsonobj.optInt("size");

				String selDistrictCode = jsonobj.optString("selDistrictCode");
				String selTahasilCode = jsonobj.optString("selTahasilCode");
				String selVillageCode = jsonobj.optString("selVillageCode");
				String grievanceNo = jsonobj.optString("grievanceNo");

				List<GrievanceGoSeenRecordDTO> respones = grievanceService.getgrivanceUserRecord(pageType, pageNo,
						pageSize, selDistrictCode, selTahasilCode, selVillageCode, grievanceNo);
				BigInteger countrecord = grievanceService.countRecord(pageType, selDistrictCode, selTahasilCode,
						selVillageCode, grievanceNo);
				if (respones.isEmpty()) {
					resp.put(CommonConstant.STATUS_KEY, 404);
					resp.put(CommonConstant.RESULT, "No Record Found");
					resp.put(CommonConstant.COUNT, countrecord);
				} else {
					resp.put(CommonConstant.STATUS_KEY, 200);
					resp.put(CommonConstant.RESULT, respones);
					resp.put(CommonConstant.COUNT, countrecord);
				}

			} else {
				resp.put(CommonConstant.COUNT, 0);
				resp.put("msg", CommonConstant.ERROR);
				resp.put(CommonConstant.STATUS_KEY, 417);
			}

		} catch (JSONException e) {
			resp.put(CommonConstant.COUNT, 0);
			resp.put(CommonConstant.STATUS_KEY, 400);
			resp.put(CommonConstant.ERROR, "Invalid JSON format");
		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/grievance/getMoreRecordGrivanceRecord")
	public ResponseEntity<?> getRecordGrivanceUserMore(@RequestBody String formParams) {
		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
				data = CommonUtil.inputStreamDecoder(formParams);
				JSONObject jsonobj = new JSONObject(data);
				List<GrievanceMainDTO> respones = grievanceService.getRecordGrivanceUserMore(jsonobj.getInt("intId"));
				if (respones.isEmpty()) {
					resp.put(CommonConstant.STATUS_KEY, 404);
					resp.put(CommonConstant.RESULT, "No Record Found");
				} else {
					resp.put(CommonConstant.STATUS_KEY, 200);
					resp.put(CommonConstant.RESULT, respones);
				}
			} else {
				resp.put("msg", CommonConstant.ERROR);
				resp.put(CommonConstant.STATUS_KEY, 417);
			}

		} catch (JSONException e) {
			resp.put(CommonConstant.STATUS_KEY, 400);
			resp.put(CommonConstant.ERROR, "Invalid JSON format");
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/updateStatusGrivance")
	public ResponseEntity<?> updateGrievanceStatus(@RequestBody String formParams) {
		String requestData = "";
		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
				requestData = CommonUtil.inputStreamDecoder(formParams);
				JSONObject jsonobj = new JSONObject(requestData);
				Integer gid = jsonobj.getInt("intId");
				String actionRemerk = jsonobj.getString("actionRemarks");
				java.sql.Date sqlDate = null;
				Integer approvalid = jsonobj.getInt("approvalAction");
				Integer result = null;
				JSONObject resultObj = new JSONObject();
				if (!jsonobj.getString("scheduleinception").equals("")) {
					String sheduleDateString = jsonobj.getString("scheduleinception");
					String scheduleDateString = sheduleDateString;
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date scheduleDate = null;
					try {
						scheduleDate = dateFormat.parse(scheduleDateString);
						sqlDate = new java.sql.Date(scheduleDate.getTime());
					} catch (ParseException e) {
						log.error(e.getMessage());

					}
					resultObj = grievanceService.updateGrievanceStatus(gid, approvalid, actionRemerk, sqlDate);
				} else {
					resultObj = grievanceService.updateGrievanceStatusFinal(gid, approvalid, actionRemerk);
				}

				// here update the data

				String returnstatus = "";
				if (approvalid == 1) {
					returnstatus = "Application assigned successfully";
				} else if (approvalid == 2) {
					returnstatus = "Application discarded successfully";
				}
				if (resultObj.getInt("result") > 0) {
					resp.put(CommonConstant.STATUS_KEY, 200);
					resp.put("message", returnstatus);
					resp.put(CommonConstant.RESULT, "Update Record Success..!!");
					resp.put("fcmToken", resultObj.getString("fcmToken"));
				} else {
					resp.put(CommonConstant.STATUS_KEY, 404);
					resp.put(CommonConstant.RESULT, "Record Not Update (Please Check )");
				}

			}

		} catch (JSONException e) {
			resp.put(CommonConstant.STATUS_KEY, 400);
			resp.put(CommonConstant.ERROR, "Invalid JSON format (Some Input Miss)");
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

}