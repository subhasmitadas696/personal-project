package com.csmtech.sjta.mobile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.mobile.dto.LandPostAllotmentDto;
import com.csmtech.sjta.mobile.dto.LandPostAllotmentResponseDto;
import com.csmtech.sjta.mobile.service.LandPostAllotmentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/landPostAllotment")
@Slf4j
public class LandPostAllotmentController {
	
	@Autowired
	LandPostAllotmentService landPostAllotmentService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/assignToCOForLandPostAllotment")
	public ResponseEntity assignToCOForLandPostAllotment(@RequestBody LandPostAllotmentDto landPlotAllotmentDto ) {
		LandPostAllotmentResponseDto response;
		try {
			log.info("assigning the post land allotment verification to CO");
			response = landPostAllotmentService.assignToCOForLandPostAllotment(landPlotAllotmentDto);
		} catch (Exception e) {
			log.error("error while assigning the post land allotment verification to CO: " + e.getMessage());
			return new ResponseEntity("error while assigning the post land allotment verification to CO",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/CoInspectionSubmit")
	public ResponseEntity coInspectionSubmit(@RequestBody LandPostAllotmentDto landPlotAllotmentDto) {
		LandPostAllotmentResponseDto response;
		try {
			log.info("saving CO Inspection data process starts");
			response = landPostAllotmentService.coInspectionSubmit(landPlotAllotmentDto);
		}catch(Exception e) {
			log.error("error while saving CO inspection data: " + e.getMessage());
			return new ResponseEntity("error while saving CO inspection data", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/fetchPrePostAllotmentList")
	public ResponseEntity fetchPrePostAllotmentList(@RequestBody LandPostAllotmentDto landPlotAllotmentDto) {
		LandPostAllotmentResponseDto response;
		try {
			log.info("fetching details of fetchPrePostAllotmentList");
			response = landPostAllotmentService.fetchPrePostAllotmentList(landPlotAllotmentDto);
		}catch(Exception e) {
			log.error("error while fetching fetchPrePostAllotmentList: "+e.getMessage());
			return new ResponseEntity("error while fetching fetchPrePostAllotmentList", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/fetchPostAllotmentList")
	public ResponseEntity fetchPostAllotmentListVillage(@RequestBody LandPostAllotmentDto landPlotAllotmentDto) {
		LandPostAllotmentResponseDto response;
		try {
			log.info("fetching details of fetch PostAllotmentList");
			response = landPostAllotmentService.fetchPostAllotmentList(landPlotAllotmentDto);
		}catch(Exception e) {
			log.error("error while fetching fetch PostAllotmentList: "+e.getMessage());
			return new ResponseEntity("error while fetching fetch PostAllotmentList", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/fetchPostAllotmentPendingPlotDetails")
	public ResponseEntity fetchPostAllotmentPendingListDetails(@RequestBody LandPostAllotmentDto landPlotAllotmentDto) {
		LandPostAllotmentResponseDto response;
		try {
			log.info("fetching details of fetch PostAllotment Pending List Details");
			response = landPostAllotmentService.fetchPostAllotmentPendingPlotDetails(landPlotAllotmentDto);
		}catch(Exception e) {
			log.error("error while fetching PostAllotment Plot Details: "+e.getMessage());
			return new ResponseEntity("error while fetching PostAllotment Plot Details", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/fetchPostAllotmentCompletedList")
	public ResponseEntity fetchPostAllotmentCompletedList(@RequestBody LandPostAllotmentDto landPlotAllotmentDto) {
		LandPostAllotmentResponseDto response;
		try {
			log.info("fetching details of fetchPostAllotmentCompletedList");
			response = landPostAllotmentService.fetchPostAllotmentCompletedList(landPlotAllotmentDto);
		}catch(Exception e) {
			log.error("error while fetching fetchPostAllotmentCompletedList: "+e.getMessage());
			return new ResponseEntity("error while fetching fetchPostAllotmentCompletedList", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/fetchPostAllotmentListDetails")
	public ResponseEntity fetchPostAllotmentListDetails(@RequestBody LandPostAllotmentDto landPlotAllotmentDto) {
		LandPostAllotmentResponseDto response;
		try {
			log.info("fetching details of fetchPostAllotmentListDetails");
			response = landPostAllotmentService.fetchPostAllotmentListDetails(landPlotAllotmentDto);
		}catch(Exception e) {
			log.error("error while fetching fetchPostAllotmentListDetails: "+e.getMessage());
			return new ResponseEntity("error while fetching fetchPostAllotmentListDetails", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/fetchPostAllotmentCompletedPlotDetails")
	public ResponseEntity fetchPostAllotmentCompletedPlotDetails(@RequestBody LandPostAllotmentDto landPlotAllotmentDto) {
		LandPostAllotmentResponseDto response;
		try {
			log.info("fetching details of fetchPostAllotmentCompletedList");
			response = landPostAllotmentService.fetchPostAllotmentCompletedPlot(landPlotAllotmentDto);
		}catch(Exception e) {
			log.error("error while fetching fetchPostAllotmentCompletedPlot: "+e.getMessage());
			return new ResponseEntity("error while fetching fetchPostAllotmentCompletedPlot", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}
}
