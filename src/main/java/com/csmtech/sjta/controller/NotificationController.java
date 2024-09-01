package com.csmtech.sjta.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.NotificationDTO;
import com.csmtech.sjta.service.NotificationService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;

@RestController
@RequestMapping("/api/notifications")

public class NotificationController {

	/**
	 * @author guru.prasad
	 */

	JSONObject resp = new JSONObject();

	@Autowired
	private NotificationService notificationService;

	@Value("${file.path}")
	private String finalUploadPath;

	String path = "src/storage/manage-notification/";
	String data = "";

	private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

	@PostMapping("/getnotifications")
	public ResponseEntity<?> getAllNotifications(@RequestBody String formParams) {
		try {
			log.info("notification method started..");
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
					requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
				List<NotificationDTO> notificationDTOs = notificationService.getAllNotifications();
				if (notificationDTOs.isEmpty()) {
					resp.put(CommonConstant.STATUS_KEY, 404);
					resp.put("msg", "No Record Found!");
				}
				log.info("notification found");

				resp.put(CommonConstant.STATUS_KEY, 200);
				resp.put("result", notificationDTOs);

			} else {
				resp.put(CommonConstant.STATUS_KEY, 417);
				resp.put("msg", "Invalid request!");
			}

		} catch (Exception e) {
			log.error("An error occurred in the notification: {}", e.getMessage());

			resp.put(CommonConstant.STATUS_KEY, 500);
			resp.put("msg", "Something went wrong!");
		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/viewnotifications")
	public ResponseEntity<Map<String, Object>> viewNotifications(@RequestBody NotificationDTO dto) {
		Map<String, Object> response = new HashMap<>();
		List<NotificationDTO> list = notificationService.getAllNotification(dto.getDescription(), dto.getStartDate(),
				dto.getEndDate());
		if (list != null && !list.isEmpty()) {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.OK.value());
			response.put(CommonConstant.MESSAGE_KEY, "DATA FOUND");
			response.put("result", list);
		} else {
			response.put(CommonConstant.STATUS_KEY, HttpStatus.NOT_FOUND.value());
			response.put(CommonConstant.MESSAGE_KEY, "NO DATA FOUND");
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/getonenotification")
	public ResponseEntity<ApiResponse<NotificationDTO>> getNotification(@RequestBody NotificationDTO notificationDTO) {
		NotificationDTO notificationDTODetails = notificationService
				.getNotificationById(notificationDTO.getNotificationId());
		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK, "Notification fetched successfully.", notificationDTODetails));
	}

	@PostMapping("/add")
	public ResponseEntity<ApiResponse<NotificationDTO>> addNotification(@RequestBody NotificationDTO notificationDTO) {
		notificationService.addNotification(notificationDTO);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(HttpStatus.CREATED, "Notification added successfully.", notificationDTO));
	}

	@PostMapping("/update")
	public ResponseEntity<ApiResponse<NotificationDTO>> updateNotification(
			@RequestBody NotificationDTO notificationDTO) {
		notificationService.updateNotification(notificationDTO);
		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK, "Notification updated successfully.", notificationDTO));
	}

	@PostMapping("/delete")
	public ResponseEntity<ApiResponse<NotificationDTO>> softDeleteNotification(
			@RequestBody NotificationDTO notificationDTO) {
		notificationService.softDeleteNotification(notificationDTO);
		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK, "Notification soft-deleted successfully.", notificationDTO));
	}

	@GetMapping("/notification/download/{name}")
	public ResponseEntity<Resource> download(@PathVariable("name") String name) throws IOException {
		log.info("Inside download method of Notification Controller");
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

	private static class ApiResponse<T> {
		private HttpStatus status;
		private String message;
		private T data;

		public ApiResponse(HttpStatus status, String message, T data) {
			this.status = status;
			this.message = message;
			this.data = data;
		}

		public HttpStatus getStatus() {
			return status;
		}

		public String getMessage() {
			return message;
		}

		public T getData() {
			return data;
		}

	}
}
