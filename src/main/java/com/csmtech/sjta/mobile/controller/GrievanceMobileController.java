package com.csmtech.sjta.mobile.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jfree.util.Log;
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

import com.csmtech.sjta.dto.GenericResponse;
import com.csmtech.sjta.dto.GrievanceMainDTO;
import com.csmtech.sjta.dto.LandRegisterMobileNoOrOtpVerifiedDTO;
import com.csmtech.sjta.entity.Grievance;
import com.csmtech.sjta.mobile.dto.GrievanceCoordinatorInspectionDTO;
import com.csmtech.sjta.mobile.dto.GrievanceResponseDto;
import com.csmtech.sjta.mobile.repository.GrievanceMobileRepository;
import com.csmtech.sjta.mobile.service.GrievanceMobileService;
import com.csmtech.sjta.repository.GrievanceRepository;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequestMapping("/mobileGrievance")
public class GrievanceMobileController {

	@Autowired
	GrievanceMobileService grievanceMobileService;
	
	@Autowired
	private GrievanceRepository grievanceRepository;
	
	@Value("${file.path}")
	private String finalUploadPath;

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/CoInspection")
	public ResponseEntity saveAfterInspection(@RequestBody GrievanceMainDTO dto) {
		log.info("To save data after the CoOrdinating officer inspection");
		GrievanceResponseDto response = new GrievanceResponseDto();
		try {
			if((dto.getGrievanceId() != null && (dto.getGrievanceNo() != null && !dto.getGrievanceNo().isEmpty() && !dto.getGrievanceNo().trim().isEmpty()))
					){
				log.info("data after the CoOrdinating officer inspection");
				response = grievanceMobileService.saveAfterInspection(dto);
			}else {
				
				response.setStatus(400);
				response.setMessage("Required data is not provided");
				return new ResponseEntity("Data required is not given", HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e) {
			log.error("Updating failed.Some error occured  " + e.getMessage());
			response.setStatus(500);
			response.setMessage("Updating failed.Some error occured");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}
		
	
	@SuppressWarnings({ "unchecked", "rawtypes"})
	@PostMapping("/saveGrievance")
	public ResponseEntity saveGrievance(@RequestBody GrievanceMainDTO grievanceMainDTO ) {
		log.info("process for saving grievance uploaded by citizen");
		GrievanceResponseDto response = new GrievanceResponseDto();
		try {
			response = grievanceMobileService.saveGrievance(grievanceMainDTO);
		}catch(Exception e) {
			log.error("Saving record failed.Some error occured  " + e.getMessage());
			response.setStatus(500);
			response.setMessage("Saving record failed.Some error occured");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/areaForPlot")
	public ResponseEntity areaForPlot(@RequestBody GrievanceMainDTO grievanceMainDTO) {
		log.info("process for fetching area information for specific plot");
		GrievanceResponseDto response = new GrievanceResponseDto();
		try {
			response = grievanceMobileService.areaForPlot(grievanceMainDTO);
		}catch(Exception e) {
			log.error("Fetching record of area for plot failed: "+e.getMessage());
			response.setStatus(500);
			response.setMessage("Fetching record of area for plot failed");
			return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/fetchPendingGrievance")
	public ResponseEntity pendingGrievance(@RequestBody GrievanceMainDTO grievanceMainDTO ) {
		log.info("process for fetching pending grievance uploaded by citizen");
		GrievanceResponseDto response = new GrievanceResponseDto();
		try {
			response = grievanceMobileService.fetchPendingGrievanceFromCO();
		}catch(Exception e) {
			log.error("Fetching record of pending grievance failed:  " + e.getMessage());
			response.setStatus(500);
			response.setMessage("Fetching record of pending grievance failed.Some error occured");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/fetchCompleteGrievance")
	public ResponseEntity completeGrievance(@RequestBody GrievanceMainDTO grievanceMainDTO ) {
		log.info("process for fetching pending grievance uploaded by citizen");
		GrievanceResponseDto response = new GrievanceResponseDto();
		try {
			response = grievanceMobileService.fetchCompleteGrievanceFromCO();
		}catch(Exception e) {
			log.error("Fetching record of complete grievance failed:  " + e.getMessage());
			response.setStatus(500);
			response.setMessage("Fetching record of complete grievance failed.Some error occured");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/fetchCountGrievance")
	public ResponseEntity countGrievance(@RequestBody GrievanceMainDTO grievanceMainDTO  ) {
		log.info("process for fetching count of different grievance");
		GrievanceResponseDto response = new GrievanceResponseDto();
		try {
			response = grievanceMobileService.fetchPendingAndCompleteGrievanceCount();
		}catch(Exception e) {
		
			log.error("Fetching record failed.Some error occured  " + e.getMessage());
			response.setStatus(500);
			
			response.setMessage("Fetching record failed.Some error occured");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/getGrievanceByNum")
	public ResponseEntity getGrievance(@RequestBody GrievanceMainDTO grievanceMainDTO  ) {
		log.info("process for fetching grievance by grievance number");
		GrievanceResponseDto response = new GrievanceResponseDto();
		try {
			response = grievanceMobileService.fetchGrievanceByGrievanceNo(grievanceMainDTO.getGrievanceNo());
		}catch(Exception e) {
			log.error("Fetching record failed.Some error occured: " + e.getMessage());
			response.setStatus(500);
			response.setMessage("Fetching record failed");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/saveMobile")
	public ResponseEntity saveMobile(@RequestBody GrievanceMainDTO grievanceMainDTO  ) {
		log.info("process for fetching grievance by grievance number");
		GrievanceResponseDto response = new GrievanceResponseDto();
		
		if(grievanceMainDTO.getMobileNo()== null|| grievanceMainDTO.getMobileNo().length()!=10) {
			response.setStatus(400);
			response.setMessage("mobile number is not present");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}
		try {
			response = grievanceMobileService.saveMobile(grievanceMainDTO.getMobileNo());
		}catch(Exception e) {
			log.error("Saving mobile number failed.Some error occured  " + e.getMessage());
			response.setStatus(500);
			response.setMessage("Saving mobile number failed");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/otpVerification")
	public ResponseEntity otpVerification(@RequestBody LandRegisterMobileNoOrOtpVerifiedDTO otpDto ) {
		log.info("process for verification of otp");
		GrievanceResponseDto response = new GrievanceResponseDto();
		
		if(otpDto.getMobileno()==null) {
			response.setStatus(400);
			response.setMessage("mobile number is not present for the otp");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}
		try {
			response = grievanceMobileService.otpVerify(otpDto);
		}catch(Exception e) {
			log.error("Otp verification failed.Some error occured  " + e.getMessage());
			response.setStatus(500);
			response.setMessage("Otp verification failed");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@GetMapping("/grievance/download/{name}")
	public ResponseEntity<Resource> download(@PathVariable("name") String name) throws IOException {
		log.info("Inside download method of GrievanceController");
		File file = new File(finalUploadPath + "/grievance/" + name);
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

