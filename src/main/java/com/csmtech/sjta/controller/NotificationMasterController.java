package com.csmtech.sjta.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONArray;
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

import com.csmtech.sjta.service.ManageNotificationService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.csmtech.sjta.util.ManageNotificationValidationCheck;

@RestController

@RequestMapping("/manage-notification")
public class NotificationMasterController {

	/**
	 * @author guru.prasad
	 */

	@Autowired
	private ManageNotificationService notificationService;

	@Value("${file.path}")
	private String finalUploadPath;

	String path = "src/storage/manage-notification/";
	String data = "";

	private static final Logger logger = LoggerFactory.getLogger(NotificationMasterController.class);

	@PostMapping("/addEdit")
	public ResponseEntity<?> create(@RequestBody String notification) {
		data = CommonUtil.inputStreamDecoder(notification);
		JSONObject resp = new JSONObject();
		if (ManageNotificationValidationCheck.BackendValidation(new JSONObject(data)) != null) {
			resp.put(CommonConstant.STATUS_KEY, 502);
			resp.put("errMsg", ManageNotificationValidationCheck.BackendValidation(new JSONObject(data)));
		} else {
			resp = notificationService.save(data);
		}
		return ResponseEntity.ok(resp.toString());
	}

	@PostMapping("/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) {
		data = CommonUtil.inputStreamDecoder(formParams);
		JSONObject json = new JSONObject(data);
		JSONObject entity = notificationService.getById(json.getInt("intId"));

		JSONObject jsb = new JSONObject();
		jsb.put(CommonConstant.STATUS_KEY, 200);
		jsb.put(CommonConstant.RESULT, entity);
		return ResponseEntity.ok(jsb.toString());
	}

	@PostMapping("/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		JSONArray entity = notificationService.getAll(CommonUtil.inputStreamDecoder(formParams));
		Integer appcount = notificationService.getTotalAppCount(CommonUtil.inputStreamDecoder(formParams));
		JSONObject jsonobj = new JSONObject();
		jsonobj.put(CommonConstant.STATUS_KEY, 200);
		jsonobj.put(CommonConstant.RESULT, entity);
		jsonobj.put("total_app_count", appcount);
		return ResponseEntity.ok(jsonobj.toString());
	}

	@PostMapping("/delete")
	public ResponseEntity<?> delete(@RequestBody String formParams) {
		data = CommonUtil.inputStreamDecoder(formParams);
		JSONObject json = new JSONObject(data);
		JSONObject entity = notificationService.deleteById(json.getInt("intId"), json.getInt("intUpdatedBy"));
		return ResponseEntity.ok(entity.toString());
	}

	@PostMapping("/unpublish")
	public ResponseEntity<?> unpublish(@RequestBody String formParams) {
		data = CommonUtil.inputStreamDecoder(formParams);
		JSONObject json = new JSONObject(data);
		JSONObject entity = notificationService.publishById(json.getInt("intId"), json.getInt("intUpdatedBy"));
		return ResponseEntity.ok(entity.toString());
	}

	@GetMapping("/notification/download/{name}")
	public ResponseEntity<Resource> download(@PathVariable("name") String name) throws IOException {
		logger.info("Inside download method of NotificationMasterController");
		File file = new File(finalUploadPath + "/" + "notification/" + name);
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