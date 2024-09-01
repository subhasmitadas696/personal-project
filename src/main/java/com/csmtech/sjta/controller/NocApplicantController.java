package com.csmtech.sjta.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.csmtech.sjta.dto.ApplicantDTO;
import com.csmtech.sjta.dto.NocApplicantRequestDTO;
import com.csmtech.sjta.dto.savenocNocPlotDetaisRecxord;
import com.csmtech.sjta.repository.NocapplicantClassRepository;
import com.csmtech.sjta.service.NocapplicantSerice;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/nocUserrecord")

public class NocApplicantController {

	@Autowired
	private NocapplicantSerice service;

	@Autowired
	private NocapplicantClassRepository repo;

	@Value("${file.path}")
	private String filePathloc;

	@PostMapping(value = "/savenocRecxord", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> saveREcord(@RequestPart("file") MultipartFile file,
			@RequestParam("userData") String userDataJson) {
		// Convert the JSON string to UserDataDTO
		ObjectMapper objectMapper = new ObjectMapper();
		NocApplicantRequestDTO userDataDTO;
		try {
			userDataDTO = objectMapper.readValue(userDataJson, NocApplicantRequestDTO.class);
		} catch (IOException e) {
			throw new RuntimeException("Failed to parse user data.", e);
		}

		String filePath = saveFileToSystemDrive(file);

		Integer result = service.saveNocApplicant(userDataDTO, filePath);
		BigInteger appId = repo.getLastNocApplicantId();

		JSONObject jsb = new JSONObject();
		jsb.put("status", 200);
		jsb.put("result", result);
		jsb.put("app_id", appId);
		return ResponseEntity.ok(jsb.toString());

	}

	private String saveFileToSystemDrive(MultipartFile file) {
		// Generate a unique file name to avoid collisions
		String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

		try {
			Path filePath = Paths.get(filePathloc, fileName);
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			return filePath.toString();
		} catch (IOException e) {
			throw new RuntimeException("Failed to save the file.", e);
		}
	}

	@PostMapping(value = "/savenocNocRecxord")
	public ResponseEntity<?> saveNocREcord(@RequestBody savenocNocPlotDetaisRecxord dto) {

		Integer count = service.saveNocPlot(dto);

		if (count == 0) {
			JSONObject jsb = new JSONObject();
			jsb.put("status", 500);
			jsb.put("result", "Not Save");
			jsb.put("app_id", count);
			return ResponseEntity.ok(jsb.toString());
		} else {
			JSONObject jsb = new JSONObject();
			jsb.put("status", 200);
			jsb.put("result", " Save");
			jsb.put("app_id", count);
			return ResponseEntity.ok(jsb.toString());
		}
	}

	@PostMapping(value = "/saveNocFileREcord", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> saveNocFileREcord(@RequestParam("fileHalPatta") MultipartFile fileHalPatta,
			@RequestParam("fileSabikPatta") MultipartFile fileSabikPatta,
			@RequestParam("fileSabikHalComparisonStatement") MultipartFile fileSabikHalComparisonStatement,
			@RequestParam("fileSettlementYaddast") MultipartFile fileSettlementYaddast,
			@RequestParam("fileRegisteredDeed") MultipartFile fileRegisteredDeed,
			@RequestParam("fileDocumentaryProofofOccupancyifany") MultipartFile fileDocumentaryProofofOccupancyifany,
			@RequestParam("createdby") BigInteger createdby, @RequestParam("nocappId") BigInteger nocAppId)
			throws IOException {

		MultipartFile fileHalPatta1 = fileHalPatta;
		MultipartFile fileSabikPatta1 = fileSabikPatta;
		MultipartFile fileSabikHalComparisonStatement1 = fileSabikHalComparisonStatement;
		MultipartFile fileSettlementYaddast1 = fileSettlementYaddast;
		MultipartFile fileRegisteredDeed1 = fileRegisteredDeed;
		MultipartFile fileDocumentaryProofofOccupancyifany1 = fileDocumentaryProofofOccupancyifany;

		String fileHalPatta11 = saveFile(fileHalPatta1);
		String fileSabikPatta11 = saveFile(fileSabikPatta1);
		String fileSabikHalComparisonStatement11 = saveFile(fileSabikHalComparisonStatement1);
		String fileSettlementYaddast11 = saveFile(fileSettlementYaddast1);
		String fileRegisteredDeed11 = saveFile(fileRegisteredDeed1);
		String fileDocumentaryProofofOccupancyifany11 = saveFile(fileDocumentaryProofofOccupancyifany1);

		Integer result = service.saveNocDocument(fileHalPatta11, fileSabikPatta11, fileSabikHalComparisonStatement11,
				fileSettlementYaddast11, fileRegisteredDeed11, fileDocumentaryProofofOccupancyifany11, nocAppId,
				createdby);

		if (result != 0) {
			JSONObject jsb = new JSONObject();
			jsb.put("status", 200);
			jsb.put("result", result);
			return ResponseEntity.ok(jsb.toString());
		} else {
			JSONObject jsb = new JSONObject();
			jsb.put("status", 500);
			jsb.put("result", "Data Not Save");
			return ResponseEntity.ok(jsb.toString());
		}
	}

	private String saveFile(MultipartFile file) {
		// Generate a unique file name to avoid collisions
		String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

		try {
			Path filePath = Paths.get(filePathloc, fileName);
			Files.copy(file.getInputStream(), filePath);
			// chenge the auctual file
			return fileName.toString();
		} catch (IOException e) {
			throw new RuntimeException("Failed to save the file.", e);
		}
	}

	// retrun user record individual
	@PostMapping("/individualRecord")
	public ResponseEntity<?> getApplicantDetailsById(@RequestBody String formParams) {
		JSONObject json = new JSONObject(formParams);
		ApplicantDTO respones = service.getApplicantDetailsById(json.getBigInteger("intId"));
		if (respones != null) {
			return ResponseEntity.ok(respones);
		} else {
			JSONObject jsb = new JSONObject();
			jsb.put("status", 500);
			jsb.put("result", "No Record Found");
			return ResponseEntity.ok(jsb.toString());
		}
	}

	@PostMapping("/plotRecord")
	public ResponseEntity<?> getApplicantPlotDetailsById(@RequestBody String formParams) {
		JSONObject json = new JSONObject(formParams);
		savenocNocPlotDetaisRecxord respones = service.getApplicantPlotDetailsById(json.getBigInteger("intId"));
		if (respones != null) {
			return ResponseEntity.ok(respones);
		} else {
			JSONObject jsb = new JSONObject();
			jsb.put("status", 500);
			jsb.put("result", "No Record Found");
			return ResponseEntity.ok(jsb.toString());
		}
	}
}
