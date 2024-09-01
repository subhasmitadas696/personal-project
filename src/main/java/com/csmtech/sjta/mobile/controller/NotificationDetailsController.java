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
@RequestMapping("/notificationDetails")
public class NotificationDetailsController {

	@Autowired
	NotificationDetailsService notificationDetailsService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/showNotificationCount")
	public ResponseEntity showNotificationCount(@RequestBody NotificationDTO notificationDto) {
		NotificationResponseDto response = new NotificationResponseDto();
		log.info("fetching count of notifications for the user");
		try {
			response = notificationDetailsService.showNotificationCount(notificationDto);

		} catch (Exception e) {
			log.error("error occured while fetching count of notifications", e.getMessage());
			response.setStatus(500);
			response.setMessage("error occured while fetching count of notifications");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/showNotification")
	public ResponseEntity showNotification(@RequestBody String formParams) {
		NotificationResponseDto response = new NotificationResponseDto();
		JSONObject requestObj = new JSONObject(formParams);
		log.info("fetching notification");
		try {
			ObjectMapper om = new ObjectMapper();
			NotificationDTO notificationDto;
			if (requestObj.has(CommonConstant.REQUEST_TOKEN)) {
				notificationDto = om.readValue(CommonUtil.inputStreamDecoder(formParams), NotificationDTO.class);
			} else {
				notificationDto = om.readValue(formParams, NotificationDTO.class);
			}
			response = notificationDetailsService.fetchNotification(notificationDto);
		} catch (Exception e) {
			log.error("error occured while fetching notifications", e.getMessage());
			response.setStatus(500);
			response.setMessage("error occured while fetching notifications");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (requestObj.has(CommonConstant.REQUEST_TOKEN)) {
			return new ResponseEntity(CommonUtil.inputStreamEncoder(new JSONObject(response).toString()).toString(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity(response, HttpStatus.OK);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/updateNotification")
	public ResponseEntity updateNotification(@RequestBody String formParams) {
		NotificationResponseDto response = new NotificationResponseDto();
		JSONObject requestObj = new JSONObject(formParams);
		log.info("updating notification");
		try {
			ObjectMapper om = new ObjectMapper();
			NotificationDTO notificationDto;
			if (requestObj.has(CommonConstant.REQUEST_TOKEN)) {
				notificationDto = om.readValue(CommonUtil.inputStreamDecoder(formParams), NotificationDTO.class);
			} else {
				notificationDto = om.readValue(formParams, NotificationDTO.class);
			}
			response = notificationDetailsService.updateNotification(notificationDto);
		} catch (Exception e) {
			log.error("error occured while updating notifications : ", e.getMessage());
			response.setStatus(500);
			response.setMessage("error occured while updating notifications");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (requestObj.has(CommonConstant.REQUEST_TOKEN)) {
			return new ResponseEntity(CommonUtil.inputStreamEncoder(new JSONObject(response).toString()).toString(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity(response, HttpStatus.OK);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/submitNotification")
	public ResponseEntity submitNotification(@RequestBody String formParams) {
		NotificationResponseDto response = new NotificationResponseDto();
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
}
	/*
	 * // @SuppressWarnings({ "unchecked", "rawtypes" })
	 * // @PostMapping("/updateNotification") // public ResponseEntity
	 * updateNotification(@RequestBody NotificationDTO notificationDto) { //
	 * NotificationResponseDto response = new NotificationResponseDto(); //
	 * log.info("updating notification"); // try { // response =
	 * notificationDetailsService.updateNotification(notificationDto); //
	 * }catch(Exception e) { //
	 * log.error("error occured while updating notifications",e.getMessage()); //
	 * response.setStatus(500); //
	 * response.setMessage("error occured while updating notifications"); // return
	 * new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR); // } // //
	 * return new ResponseEntity(response,HttpStatus.OK); // // }
	 * 
	 * @SuppressWarnings({ "unchecked", "rawtypes" })
	 * 
	 * @PostMapping("/showNotification") public ResponseEntity
	 * showNotification(@RequestBody NotificationDTO notificationDto) {
	 * NotificationResponseDto response = new NotificationResponseDto();
	 * log.info("fetching notification"); try { response =
	 * notificationDetailsService.fetchNotification(notificationDto);
	 * }catch(Exception e) {
	 * log.error("error occured while fetching notifications",e.getMessage());
	 * response.setStatus(500);
	 * response.setMessage("error occured while fetching notifications"); return new
	 * ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR); }
	 * 
	 * return new ResponseEntity(response,HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 */

	/*
	 * // @SuppressWarnings({ "unchecked", "rawtypes" })
	 * // @PostMapping("/updateNotification") // public ResponseEntity
	 * updateNotification(@RequestBody NotificationDTO notificationDto) { //
	 * NotificationResponseDto response = new NotificationResponseDto(); //
	 * log.info("updating notification"); // try { // response =
	 * notificationDetailsService.updateNotification(notificationDto); //
	 * }catch(Exception e) { //
	 * log.error("error occured while updating notifications",e.getMessage()); //
	 * response.setStatus(500); //
	 * response.setMessage("error occured while updating notifications"); // return
	 * new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR); // } // //
	 * return new ResponseEntity(response,HttpStatus.OK); // // }
	 * 
	 * @SuppressWarnings({ "unchecked", "rawtypes" })
	 * 
	 * @PostMapping("/showNotification") public ResponseEntity
	 * showNotification(@RequestBody NotificationDTO notificationDto) {
	 * NotificationResponseDto response = new NotificationResponseDto();
	 * log.info("fetching notification"); try { response =
	 * notificationDetailsService.fetchNotification(notificationDto);
	 * }catch(Exception e) {
	 * log.error("error occured while fetching notifications",e.getMessage());
	 * response.setStatus(500);
	 * response.setMessage("error occured while fetching notifications"); return new
	 * ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR); }
	 * 
	 * return new ResponseEntity(response,HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 */


