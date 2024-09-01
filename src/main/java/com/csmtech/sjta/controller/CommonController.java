package com.csmtech.sjta.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@RestController

@Slf4j
public class CommonController {

	@Autowired
	private EntityManager entityManager;

	@Value("${file.path}")
	private String docPath;

	@Value("${tempUpload.path}")
	private String tempPath;

	@Value("${scanfile.path}")
	private String scanDocPath;

	@PostMapping(value = "/saveFileToTemp")
	public ResponseEntity<?> saveDocImgToTemp(@RequestParam("file") MultipartFile multipart) {
		JSONObject response = new JSONObject();
		try {
			if (!multipart.isEmpty()) {
				SecureRandom rand = SecureRandom.getInstanceStrong();
				int x = rand.nextInt(900 - 100) + 100;
				Timestamp tt = new Timestamp(System.currentTimeMillis());
				String fileNameForType = multipart.getOriginalFilename();

				if (fileNameForType != null) {
					String[] fileArray = fileNameForType.split("[.]");
					if (fileArray.length > 1) {
						String actualType = fileArray[fileArray.length - 1];
						File file1 = new File(tempPath + "/" + x + tt.getTime() + "." + actualType);
						try (FileOutputStream fileOutputStream = new FileOutputStream(file1);
								BufferedOutputStream bf = new BufferedOutputStream(fileOutputStream)) {

							byte[] bytes = multipart.getBytes();
							bf.write(bytes);

							response.put("originalFileName", fileNameForType);
							response.put("fileName", "" + x + tt.getTime() + "." + actualType);
							response.put(CommonConstant.STATUS_KEY, 200);
							return ResponseEntity.ok(response.toString());
						} catch (IOException e) {
							log.error(e.getMessage());

							e.getMessage();
							response.put(CommonConstant.STATUS_KEY, 500);
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.toString());
						}
					} else {
						response.put(CommonConstant.STATUS_KEY, 400);
						response.put(CommonConstant.ERROR, "Invalid file name format");
					}
				} else {
					response.put(CommonConstant.STATUS_KEY, 400);
					response.put(CommonConstant.ERROR, "Original file name is null");
				}
			} else {
				response.put(CommonConstant.STATUS_KEY, 400);
				response.put(CommonConstant.ERROR, "No file provided");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			response.put(CommonConstant.STATUS_KEY, 500);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());
	}

	@PostMapping(value = "/fillDropDown")
	public ResponseEntity<?> getAllDynamicDropDownValue(@RequestBody String data)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, JSONException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String getData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject(getData);
		String[] str = json.getString(CommonConstant.METHOD).split("/");
		Class<?> cls = Class.forName(CommonConstant.SERVICE_CLASS_NAME + str[0]);
		Method method = cls.getMethod(str[1], EntityManager.class, String.class);
		JSONObject response = new JSONObject();
		response.put(CommonConstant.STATUS_KEY, 200);
		response.put(CommonConstant.RESULT, method.invoke(method, entityManager, json.toString()));
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());

	}

	@PostMapping(value = "/fillDropDowns")
	public ResponseEntity<?> getAllDynamicDropDownValues(@RequestBody String data)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, JSONException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JSONObject json = new JSONObject(data);
		String[] str = json.getString(CommonConstant.METHOD).split("/");
		Class<?> cls = Class.forName(CommonConstant.SERVICE_CLASS_NAME + str[0]);
		Method method = cls.getMethod(str[1], EntityManager.class, String.class);
		JSONObject response = new JSONObject();
		response.put(CommonConstant.STATUS_KEY, 200);
		response.put(CommonConstant.RESULT, method.invoke(method, entityManager, json.toString()));
		return ResponseEntity.ok(response.toString());

	}

	@PostMapping(value = "/fillRadio")
	public ResponseEntity<?> getAllDynamicRadioValue(@RequestBody String data)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, JSONException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String getData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject(getData);
		String[] str = json.getString(CommonConstant.METHOD).split("/");
		Class<?> cls = Class.forName(CommonConstant.SERVICE_CLASS_NAME + str[0]);
		Method method = cls.getMethod(str[1], EntityManager.class, String.class);
		JSONObject response = new JSONObject();
		response.put(CommonConstant.STATUS_KEY, 200);
		response.put(CommonConstant.RESULT, method.invoke(method, entityManager, json.toString()));
		return ResponseEntity.ok(response.toString());

	}

	// Token based API
	@PostMapping(value = "/fillDropDownToken")
	public ResponseEntity<?> getAllDynamicDropDownValueToken(@RequestBody String data)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, JSONException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		JSONObject json = new JSONObject(data);
		String[] str = json.getString(CommonConstant.METHOD).split("/");
		Class<?> cls = Class.forName(CommonConstant.SERVICE_CLASS_NAME + str[0]);
		Method method = cls.getMethod(str[1], EntityManager.class, String.class);
		JSONObject response = new JSONObject();
		response.put(CommonConstant.STATUS_KEY, 200);
		response.put(CommonConstant.RESULT, method.invoke(method, entityManager, json.toString()));
		return ResponseEntity.ok(response.toString());

	}

	@PostMapping(value = "/downloadDocumentPostCheck")
	public ResponseEntity<byte[]> downloadDocumentPost(HttpServletResponse response, @RequestBody String param) {
		JSONObject json = new JSONObject(param);
		String fileName = json.getString("fileName");
		String fileType = json.getString("fileType");
		String filePath = json.getString("filePath");
		String finalPath = docPath;

		if (fileType.equalsIgnoreCase("S")) {
			finalPath = scanDocPath;
		}
		if (!filePath.equals("")) {
			finalPath = finalPath + "/" + filePath;
		}
		HttpHeaders headers = new HttpHeaders();
		byte[] data = null;
		try {
			if (null != fileName && fileName.contains(".")) {
				String fileExtension = fileName.split("\\.")[1];
				Path path = Paths.get(finalPath + "/" + fileName);
				data = Files.readAllBytes(path);
				headers.setContentType(MediaType.MULTIPART_FORM_DATA);
				if (fileExtension.equalsIgnoreCase("pdf"))
					response.setContentType(CommonConstant.APPLICATION_PDF);
				else if (fileExtension.equalsIgnoreCase("xls"))
					response.setContentType("application/vnd.ms-excel");
				else if (fileExtension.equalsIgnoreCase("xlsx"))
					response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				else if (fileExtension.equalsIgnoreCase("png"))
					response.setContentType("image/png");
				else if (fileExtension.equalsIgnoreCase("jpeg"))
					response.setContentType("image/jpeg");
				else if (fileExtension.equalsIgnoreCase("jpg"))
					response.setContentType("image/jpg");
				else if (fileExtension.equalsIgnoreCase("csv"))
					response.setContentType("text/csv");
				else if (fileExtension.equalsIgnoreCase("zip"))
					response.setContentType("application/zip");

				response.setHeader("Content-disposition", "inline;filename=" + fileName);
				headers.setContentLength(data.length);
			}

			return new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
		} catch (IOException e) {
			log.error(e.getMessage());
			return new ResponseEntity<byte[]>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "/downloadDocument/{appDocId}", name = "Download File")
	public ResponseEntity<byte[]> downloadDocument(HttpServletResponse response,
			@PathVariable("appDocId") String fileName) {
		HttpHeaders headers = new HttpHeaders();
		byte[] data = null;
		try {
			if (null != fileName && fileName.contains(".")) {
				String fileExtension = fileName.split("\\.")[1];
				Path path = Paths.get(docPath + "/" + fileName);
				data = Files.readAllBytes(path);
				headers.setContentType(MediaType.MULTIPART_FORM_DATA);
				if (fileExtension.equalsIgnoreCase("pdf"))
					response.setContentType(CommonConstant.APPLICATION_PDF);
				else if (fileExtension.equalsIgnoreCase("xls"))
					response.setContentType("application/vnd.ms-excel");
				else if (fileExtension.equalsIgnoreCase("xlsx"))
					response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				else if (fileExtension.equalsIgnoreCase("png"))
					response.setContentType("image/png");
				else if (fileExtension.equalsIgnoreCase("jpeg"))
					response.setContentType("image/jpeg");
				else if (fileExtension.equalsIgnoreCase("jpg"))
					response.setContentType("image/jpg");
				else if (fileExtension.equalsIgnoreCase("csv"))
					response.setContentType("text/csv");
				else if (fileExtension.equalsIgnoreCase("zip"))
					response.setContentType("application/zip");

				response.setHeader("Content-disposition", "inline;filename=" + fileName);
				headers.setContentLength(data.length);
			}

			return new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<byte[]>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "/viewDocument/{appDocId}", name = "View File")
	public ResponseEntity viewDocument(HttpServletResponse response, @PathVariable("appDocId") String fileName)
			throws FileNotFoundException {

		HttpHeaders headers = new HttpHeaders();
		headers.add("content-disposition", "inline;filename=" + fileName);
		File file = new File(docPath + "/" + fileName);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		String contentType = "";

		if (null != fileName && fileName.contains(".")) {
			String fileExtension = fileName.split("\\.")[1];

			if (fileExtension.equalsIgnoreCase("pdf"))
				contentType = CommonConstant.APPLICATION_PDF;
			if (fileExtension.equalsIgnoreCase("xls"))
				contentType = "application/vnd.ms-excel";
			if (fileExtension.equalsIgnoreCase("xlsx"))
				contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
			else if (fileExtension.equalsIgnoreCase("png"))
				contentType = "image/png";
			else if (fileExtension.equalsIgnoreCase("jpeg"))
				contentType = "image/jpeg";
			else if (fileExtension.equalsIgnoreCase("jpg"))
				contentType = "image/jpg";
			else if (fileExtension.equalsIgnoreCase("csv"))
				response.setContentType("text/csv");
			else if (fileExtension.equalsIgnoreCase("zip"))
				response.setContentType("application/zip");
		}
		return ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.parseMediaType(contentType)).body(resource);
	}

	@GetMapping(path = "/viewDocuments/{type}/{appDocId}", name = "View File")
	public ResponseEntity viewDocuments(HttpServletResponse response, @PathVariable("type") String fileType,
			@PathVariable("appDocId") String fileName) throws FileNotFoundException {
		String filePath = docPath;
		if (fileType.equals("1")) { // landUseVerificationTahasildar
			filePath = filePath + "/tahasilPlot/" + "PlotSurvey/";
		} else if (fileType.equals("2")) { // landApplicationVerificationCO
			filePath = filePath + "/LandCOSurvey/" + "PlotSurvey";
		} else if (fileType.equals("3")) { // landAppplicationVerificationTahasildar
			filePath = filePath + "/LandTahasilSurvey/" + "PlotSurvey";
		} else if (fileType.equals("4")) { // mobile grievance
			filePath = filePath + "/grievance/";
		} else if (fileType.equals("5")) {
			filePath = filePath +"/PostAllotmentInspection/" + "PlotSurvey/";
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("content-disposition", "inline;filename=" + fileName);
		File file = new File(filePath + "/" + fileName);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		String contentType = "";

		if (null != fileName && fileName.contains(".")) {
			String fileExtension = fileName.split("\\.")[1];

			if (fileExtension.equalsIgnoreCase("pdf"))
				contentType = CommonConstant.APPLICATION_PDF;
			if (fileExtension.equalsIgnoreCase("xls"))
				contentType = "application/vnd.ms-excel";
			if (fileExtension.equalsIgnoreCase("xlsx"))
				contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
			else if (fileExtension.equalsIgnoreCase("png"))
				contentType = "image/png";
			else if (fileExtension.equalsIgnoreCase("jpeg"))
				contentType = "image/jpeg";
			else if (fileExtension.equalsIgnoreCase("jpg"))
				contentType = "image/jpg";
			else if (fileExtension.equalsIgnoreCase("csv"))
				response.setContentType("text/csv");
			else if (fileExtension.equalsIgnoreCase("zip"))
				response.setContentType("application/zip");
		}
		return ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.parseMediaType(contentType)).body(resource);
	}

	@GetMapping("/{imageName}")
	public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws IOException {
		Resource resource = new ClassPathResource("img/" + imageName);
		byte[] data = new byte[resource.getInputStream().available()];
		resource.getInputStream().read(data);

		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG) // Adjust the MediaType based on your image type
				.body(data);
	}

}