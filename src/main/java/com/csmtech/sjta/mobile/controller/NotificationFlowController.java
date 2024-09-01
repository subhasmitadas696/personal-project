package com.csmtech.sjta.mobile.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.NotificationDTO;
import com.csmtech.sjta.mobile.dto.NotificationResponseDto;
import com.csmtech.sjta.mobile.service.NotificationDetailsService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/notificationsFlow")
public class NotificationFlowController {
	
	@Autowired
	NotificationDetailsService notificationDetailsService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/notificationRerun")
	public ResponseEntity notificationRerun(@RequestBody String formParams) {
		NotificationResponseDto response = new NotificationResponseDto();
		JSONObject requestObj = new JSONObject(formParams);
		try {
			ObjectMapper om = new ObjectMapper();
			NotificationDTO notificationDto;
			if (requestObj.has(CommonConstant.REQUEST_TOKEN)) {
				notificationDto = om.readValue(CommonUtil.inputStreamDecoder(formParams), NotificationDTO.class);
			} else {
				notificationDto = om.readValue(formParams, NotificationDTO.class);
			}
			response= notificationDetailsService.notificationRerun(notificationDto);
		}catch(Exception e) {
			log.error("error occured while running notifications : ",e.getMessage());
			response.setStatus(500);
			response.setMessage("error occured while running notifications");
			return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (requestObj.has(CommonConstant.REQUEST_TOKEN)) {
			return new ResponseEntity(CommonUtil.inputStreamEncoder(new JSONObject(response).toString()).toString(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity(response, HttpStatus.OK);
		} 
		/*
		 * NotificationResponseDto response = new NotificationResponseDto();
		log.info("saving the notification");
		JSONObject requestObj = new JSONObject(formParams);
		try {
			ObjectMapper om = new ObjectMapper();
			NotificationDTO notificationDto;
			if (requestObj.has(CommonConstant.REQUEST_TOKEN)) {
				notificationDto = om.readValue(CommonUtil.inputStreamDecoder(formParams), NotificationDTO.class);
			} else {
				notificationDto = om.readValue(formParams, NotificationDTO.class);
			}
			response= notificationDetailsService.submitNotification(notificationDto);
		}catch(Exception e) {
			log.error("error occured while saving notifications : ",e.getMessage());
			response.setStatus(500);
			response.setMessage("error occured while savinh notifications");
			return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (requestObj.has(CommonConstant.REQUEST_TOKEN)) {
			return new ResponseEntity(CommonUtil.inputStreamEncoder(new JSONObject(response).toString()).toString(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity(response, HttpStatus.OK);
		} 
	}

		 */
		
	}

}
