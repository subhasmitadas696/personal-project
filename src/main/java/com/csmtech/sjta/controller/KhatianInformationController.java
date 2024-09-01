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

import com.csmtech.sjta.dto.DigitalFileKhatianInformation;
import com.csmtech.sjta.entity.Khatian_information;
import com.csmtech.sjta.service.KhatianInformationService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.csmtech.sjta.util.KhatianInformationValidationCheck;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController

@RequestMapping("/land-bank")
public class KhatianInformationController {

	/**
	 * @author guru.prasad
	 */

	@Autowired
	private KhatianInformationService khatianInformationService;
	String data = "";
	private static final Logger logger = LoggerFactory.getLogger(KhatianInformationController.class);
	JSONObject resp = new JSONObject();

	@Value("${rordigitalfile.path}")
	private String rorDigitalFile;

	@Value("${casefiledigitalfile.path}")
	private String caseFileDigitalFile;

	@PostMapping("/khatian-information/addEdit")
	public ResponseEntity<?> create(@RequestBody String khatianInformation) {
		logger.info("Inside create method of Khatian_informationController");
		JSONObject requestObj = new JSONObject(khatianInformation);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(khatianInformation);
			String validationMsg = KhatianInformationValidationCheck.BackendValidation(new JSONObject(data));
			if (validationMsg != null) {
				resp.put(CommonConstant.STATUS_KEY, 502);
				resp.put("errMsg", validationMsg);
				logger.warn("Inside create method of Khatian_informationController Validation Error");
			} else {
				resp = khatianInformationService.save(data);
			}
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/khatian-information/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		logger.info("Inside getAll method of Khatian_informationController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			resp = khatianInformationService.getAll(CommonUtil.inputStreamDecoder(formParams));
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/khatian-information/searchplot")
	public ResponseEntity<?> searchPlot(@RequestBody String formParams) throws JsonProcessingException {
		logger.info("Inside getAll method of Khatian_informationController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			ObjectMapper om = new ObjectMapper();
			Khatian_information khataDto = om.readValue(data, Khatian_information.class);
			resp = khatianInformationService.searchPlot(khataDto.getTxtKhatianCode(), khataDto.getTxtPlotNo());
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/khatian-information/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) throws JsonProcessingException {
		logger.info("Inside getById method of Khatian_informationController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			ObjectMapper om = new ObjectMapper();
			Khatian_information khataDto = om.readValue(data, Khatian_information.class);
			resp = khatianInformationService.getById(khataDto.getTxtKhatianCode());

		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/khatian-information/getkhatian")
	public ResponseEntity<?> getKhatian(@RequestBody String formParams) {
		logger.info("Inside getById method of Khatian_informationController");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			JSONObject json = new JSONObject(data);
			JSONObject entity = khatianInformationService.getKhatian(json.getString("intId"));
			resp.put(CommonConstant.STATUS_KEY, 200);
			resp.put("result", entity);
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/khatian_information/viewall")
	public ResponseEntity<?> viewAll(@RequestBody Khatian_information vh) {
		logger.info("Inside getAll method of khatian_informationController");
		resp = khatianInformationService.viewAll(vh.getSelVillageCode());
		return ResponseEntity.ok(resp.toString());
	}

	@PostMapping("/khatian_information/viewdigitalfile")
	public ResponseEntity<?> viewDigitalFile(@RequestBody String formParams) throws JsonProcessingException {
		logger.info("inside view file");
		JSONObject requestObj = new JSONObject(formParams);
		if (CommonUtil.hashRequestMatch(requestObj.getString(CommonConstant.REQUEST_DATA),
				requestObj.getString(CommonConstant.REQUEST_TOKEN))) {
			data = CommonUtil.inputStreamDecoder(formParams);
			ObjectMapper om = new ObjectMapper();
			DigitalFileKhatianInformation digiFiledto = om.readValue(data, DigitalFileKhatianInformation.class);
			logger.info("Inside getAll digital file  method of khatian_information Controller");
			resp = khatianInformationService.viewDigitalFile(digiFiledto.getKhatianCode());
		} else {
			resp.put("msg", CommonConstant.ERROR);
			resp.put(CommonConstant.STATUS_KEY, 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@GetMapping(path = "/viewKhatianDigitalFile/{fileName}", name = "View File")
	public ResponseEntity viewDocument(HttpServletResponse response, @PathVariable("fileName") String fileName)
			throws FileNotFoundException {
		String filePath = "";
		filePath = rorDigitalFile;

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

}