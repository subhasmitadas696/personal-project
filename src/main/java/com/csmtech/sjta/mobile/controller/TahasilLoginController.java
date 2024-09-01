package com.csmtech.sjta.mobile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.GenericResponse;
import com.csmtech.sjta.dto.TahasilTeamUseRequestDto;
import com.csmtech.sjta.mobile.dto.OtpResponseDto;
import com.csmtech.sjta.mobile.dto.TahasilResponseDTO;
import com.csmtech.sjta.mobile.service.TahasilService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/mobileTahasilLogin")

@Slf4j
public class TahasilLoginController {
	
	@Autowired
	TahasilService tahasilService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/tahasilLogin")
	public ResponseEntity tahasilLogin(@RequestHeader("Token") String token,@RequestBody TahasilTeamUseRequestDto tahasilDto) {
		GenericResponse response = new GenericResponse();
		try {
			response = tahasilService.tahasilLogin(token,tahasilDto);
		}catch(Exception e) {
			log.error("error while tahasil login: "+e.getMessage());
			response.setStatus(500);
			response.setMessage("error while tahasil login");
			return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(response,HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/tahasilMobileInsertion")
	public ResponseEntity tahasilMobileInsertion(@RequestBody TahasilTeamUseRequestDto tahasilDto) {
		OtpResponseDto response  = new OtpResponseDto();
		try {
			response = tahasilService.tahasilMobileInsertion(tahasilDto);
		}catch(Exception e) {
			log.error("error while tahasil login: "+e.getMessage());
			response.setStatus(500);
			response.setMessage("error while tahasil mobile number insertion");
			return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(response,HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/tahasilOtpVerification")
	public ResponseEntity tahasilOtpVerification(@RequestBody TahasilTeamUseRequestDto tahasilDto) {
		TahasilResponseDTO response  = new TahasilResponseDTO();
		try {
			response = tahasilService.tahasilOtpVerification(tahasilDto);
		}catch(Exception e) {
			log.error("error while tahasil otp verification: "+e.getMessage());
			response.setStatus(500);
			response.setMessage("error while tahasil otp verification");
			return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(response,HttpStatus.OK);
	}

	

}
