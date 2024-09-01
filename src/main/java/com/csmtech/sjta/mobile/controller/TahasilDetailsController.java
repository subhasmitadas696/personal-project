package com.csmtech.sjta.mobile.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.PaginationInRegisterDtoResponse;
import com.csmtech.sjta.dto.TahasilTeamUseRequestDto;
import com.csmtech.sjta.mobile.dto.ImageRequestDto;
import com.csmtech.sjta.mobile.dto.TahasilPlotDto;
import com.csmtech.sjta.mobile.dto.TahasilResponseDTO;
import com.csmtech.sjta.mobile.dto.VillageDTO;
import com.csmtech.sjta.mobile.service.TahasilService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/mobileTahasilDetails")

@Slf4j
public class TahasilDetailsController {
	
	@Autowired
	TahasilService tahasilService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/getVillageDetails")
	public ResponseEntity getVillageDetails(@RequestBody TahasilTeamUseRequestDto tahasilTeamUseRequestDto) {
		TahasilResponseDTO response = new TahasilResponseDTO();
		try {
			response = tahasilService.getVillageDetails(tahasilTeamUseRequestDto);
		}catch(Exception e) {
			log.error("error occured while fetching village details: "+e.getMessage());
			response.setMessage("error occured while fetching village details");
			response.setStatus(500);
			return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/getVillageDetailsByPlot")
	public ResponseEntity getVillageDetailsByPlot(@RequestBody VillageDTO villageDto) {
		TahasilResponseDTO response = new TahasilResponseDTO();
		try {
			response = tahasilService.getVillageDetailsByPlot(villageDto);
		}catch(Exception e) {
			log.error("error occured while fetching village details by plot: "+e.getMessage());
			response.setMessage("error occured while fetching village details by plot");
			response.setStatus(500);
			return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/savePlotAction")
	public ResponseEntity savePlotAction(@RequestBody TahasilPlotDto tahasilPlotDto) {
		TahasilResponseDTO response = new TahasilResponseDTO();
		log.info("checking the input for saving action of plot");
		if(tahasilPlotDto == null || (tahasilPlotDto.getPlotNo() == null && tahasilPlotDto.getRemarks() == null)) {
			return new ResponseEntity("Data required is not present", HttpStatus.BAD_REQUEST);
		}
		try {
			response = tahasilService.savePlotAction(tahasilPlotDto);
		}catch(Exception e) {
			log.error("error occured while saving details of plot survey: "+e.getMessage());
			
			response.setMessage("error occured while saving details of plot survey");
			response.setStatus(500);
			
			return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	@PostMapping("/fetchLandTypeDetails")
	public ResponseEntity fetchLandTypeDetails() {
		TahasilResponseDTO response = new TahasilResponseDTO();
		try {
			response = tahasilService.fetchLandTypeDetails();
		}catch(Exception e) {
			log.error("error occured while fetching details of land type: "+e.getMessage());
			response.setMessage("error occured while fetching details of land type");
			response.setStatus(500);
			return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({"unchecked","rawtypes"})
	@PostMapping("/fetchLandUseDetails")
	public ResponseEntity fetchLandUseDetails() {
		TahasilResponseDTO response = new TahasilResponseDTO();
		try {
			response = tahasilService.fetchLandUseDetails();
		}catch(Exception e) {
			log.error("error occured while fetching details of land use: "+e.getMessage());
			response.setMessage("error occured while fetching details of land use");
			response.setStatus(500);
			return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	@PostMapping("/fetchLandUseVerificationCompletedDetails") //Land Use Verification completed 
	public ResponseEntity fetchLandUseVerificationCompletedDetails(@RequestBody TahasilPlotDto tahasilPlotDto) {
		TahasilResponseDTO response = new TahasilResponseDTO();
		try {
			response = tahasilService.fetchLandUseVerificationCompletedDetails(tahasilPlotDto);
		}catch(Exception e) {
			log.error("error occured while fetching details of land use: "+e.getMessage());
			response.setMessage("error occured while fetching details of land use");
			response.setStatus(500);
			return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	//fetchWebLandUseVerificationCompletedDetails
	@SuppressWarnings({"unchecked","rawtypes"})
	@PostMapping("/fetchWebLandUseVerificationCompletedDetails") //Land Use Verification completed 
	public ResponseEntity fetchWebLandUseVerificationCompletedDetails(@RequestBody TahasilPlotDto tahasilPlotDto) {
		TahasilResponseDTO response = new TahasilResponseDTO();
		try {
			response = tahasilService.fetchWebLandUseVerificationCompletedDetails(tahasilPlotDto);
		}catch(Exception e) {
			log.error("error occured while fetching details of land use: "+e.getMessage());
			response.setMessage("error occured while fetching details of land use");
			response.setStatus(500);
			return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	@PostMapping("/viewImage") //Land Use Verification completed 
	public ResponseEntity viewImage(@RequestBody ImageRequestDto imageRequestDto) {
		JSONObject object = new JSONObject();
		List<String> result = new ArrayList<>();
		TahasilResponseDTO response = new TahasilResponseDTO();
		try {
			result = tahasilService.viewImage(imageRequestDto);
		}catch(Exception e) {
			log.error("error occured while fetching images of land use: "+e.getMessage());
			response.setMessage("error occured while fetching images of land use");
			response.setStatus(500);
			return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		object.put("filesUrl", result);
		
		return new ResponseEntity(object.toString(), HttpStatus.OK);
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	@PostMapping("/tahasilPlotList") 
	public ResponseEntity tahasilPlotList(@RequestBody PaginationInRegisterDtoResponse res) {
		TahasilResponseDTO response = new TahasilResponseDTO();
		try {
			response = tahasilService.tahasilPlotList(res);
		}catch(Exception e) {
			log.error("error occured while plot details of tahasil for land use: "+e.getMessage());
			response.setMessage("error occured while plot details of tahasil for land use");
			response.setStatus(500);
			return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}
	
}
