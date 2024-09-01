package com.csmtech.sjta.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

import com.csmtech.sjta.dto.LandIndividualAppDTO;
import com.csmtech.sjta.repository.LandApplicantNativeRepository;
import com.csmtech.sjta.repository.LandPlotDetailsRepository;
import com.csmtech.sjta.service.Land_plotService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;

@RestController

@RequestMapping("/land-purchase-application")
public class LandPlotController {
	@Autowired
	private Land_plotService landPlotService;
	String path = "src/storage/plot-details/";
	String data = "";

	@Autowired
	private LandPlotDetailsRepository repo;

	@Autowired
	private LandApplicantNativeRepository reponative;

	@PostMapping("/plot-details/addEdit")
	public ResponseEntity<?> create(@RequestBody LandIndividualAppDTO dto) {
		Integer countplotupdate = repo.updateApplicantRecord(dto);
		Integer countinsertolot = repo.batchUpdateDeleteNocPlots(dto);
		BigInteger bigIntegerValue = dto.getIntLandApplicantId();
		Integer landAppUpdate = null;
		Integer integerValue = bigIntegerValue.intValue();
		String rr = reponative.getAppStatusForLandApplicationId(integerValue);
		if (rr.equalsIgnoreCase("4")) {
		} else {
			landAppUpdate = repo.updateAppStatusForLandApplicationId(dto.getIntLandApplicantId(), dto.getAppStage());
		}
		String rrRes = reponative.getAppStatusForLandApplicationId(integerValue);
		if (countinsertolot > 1 || countplotupdate > 1 || landAppUpdate > 0) {
			JSONObject jsb = new JSONObject();
			jsb.put(CommonConstant.STATUS_KEY, 200);
			jsb.put("appStatus", rrRes);
			jsb.put(CommonConstant.RESULT, "Record update and insert success");
			return ResponseEntity.ok(jsb.toString());
		} else {
			JSONObject jsb = new JSONObject();
			jsb.put(CommonConstant.STATUS_KEY, 200);
			jsb.put(CommonConstant.RESULT, " update or insert fail");
			return ResponseEntity.ok(jsb.toString());
		}

	}

	@PostMapping("/plot-details/preview")
	public ResponseEntity<?> getByLandApplicantId(@RequestBody String formParams) {
		JSONObject json = new JSONObject(formParams);
		LandIndividualAppDTO result = repo.getLandIndividualAppDTOByLandApplicantId(json.getInt("intId"));
		String appStage = repo.getCurrentStage(json.getInt("intId"));

		if (result != null) {
			JSONObject jo = new JSONObject();
			jo.put(CommonConstant.STATUS_KEY, 200);
			jo.put(CommonConstant.RESULT, new JSONObject(result));
			jo.put("appStage", appStage);
			return ResponseEntity.ok(jo.toString());
		} else {
			JSONObject jsb = new JSONObject();
			jsb.put(CommonConstant.STATUS_KEY, 500);
			jsb.put(CommonConstant.RESULT, "No Record Found");
			return ResponseEntity.ok(jsb.toString());
		}
	}

	@PostMapping("/plot-details/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		JSONArray entity = landPlotService.getAll(CommonUtil.inputStreamDecoder(formParams));
		JSONObject jsonobj = new JSONObject();
		jsonobj.put(CommonConstant.STATUS_KEY, 200);
		jsonobj.put(CommonConstant.RESULT, entity);
		return ResponseEntity.ok(jsonobj.toString());
	}

	@PostMapping("/plot-details/delete")
	public ResponseEntity<?> delete(@RequestBody String formParams) {
		data = CommonUtil.inputStreamDecoder(formParams);
		JSONObject json = new JSONObject(data);
		JSONObject entity = landPlotService.deleteByIntLandApplicantId(json.getInt("intLandApplicantId"));
		return ResponseEntity.ok(entity.toString());
	}

	@GetMapping("/plot-details/download/{name}")
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
}