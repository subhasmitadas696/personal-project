package com.csmtech.sjta.mobile.service;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.NotificationDTO;
import com.csmtech.sjta.entity.Bidderregistrara;
import com.csmtech.sjta.mobile.dto.NotificationResponseDto;
import com.csmtech.sjta.mobile.entity.NotificationDetails;
import com.csmtech.sjta.mobile.repository.NotificationDetailsRepository;
import com.csmtech.sjta.util.CommonConstant;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationDetailsServiceImpl implements NotificationDetailsService {

	@Autowired
	NotificationDetailsRepository notificationDetailsRepo;

	@Override
	public NotificationResponseDto showNotificationCount(NotificationDTO notificationDto) {
		NotificationResponseDto response = new NotificationResponseDto();
		log.info("fetching notification count for user");

		List<NotificationDetails> entityList = notificationDetailsRepo
				.findNotificationDetails(notificationDto.getUserId());
		if (entityList != null && !entityList.isEmpty()) {
			response.setStatus(200);
			response.setCount(entityList.size());
			response.setMessage("fetched notification count");
			response.setStatusMessage(CommonConstant.SUCCESS);
		} else {
			response.setStatus(210);
			response.setCount(0);
			response.setMessage("no record found");
			response.setStatusMessage(CommonConstant.NO_RECORD_FOUND);
		}
		return response;
	}

	@Override
	public NotificationResponseDto fetchNotification(NotificationDTO notificationDto) {
		NotificationResponseDto response = new NotificationResponseDto();
		log.info("fetching notification count for user");
		List<NotificationDetails> entityList = notificationDetailsRepo
				.findNotificationDetails(notificationDto.getUserId());
		if (entityList != null && !entityList.isEmpty()) {
			response.setStatus(200);
			response.setNotifications(entityList);
			response.setCount(entityList.size());
			response.setMessage("fetched notification details");
			response.setStatusMessage(CommonConstant.SUCCESS);
		} else {
			response.setStatus(210);
			response.setStatusMessage(CommonConstant.NO_RECORD_FOUND);
		}
		return response;
	}

	@Override
	public NotificationResponseDto updateNotification(NotificationDTO notificationDto) {
		NotificationResponseDto response = new NotificationResponseDto();
		log.info("updating notification read unread parameter");
		NotificationDetails entity = notificationDetailsRepo.findByNotificationId(notificationDto.getNotificationId());
		entity.setReadMode("Y");
		entity.setUpdatedBy(notificationDto.getUserId());
		entity.setUpdatedOn(new Date());
		notificationDetailsRepo.save(entity);
		response.setStatus(200);
		response.setStatusMessage(CommonConstant.SUCCESS);
		return response;
	}
	
	@Override 
	public NotificationResponseDto submitNotification(NotificationDTO notificationDto) {
		NotificationResponseDto response = new NotificationResponseDto();
		NotificationDetails entity = new NotificationDetails();
		entity.setNotification(notificationDto.getNotification());
		entity.setReadMode("N");
		entity.setUserType(notificationDto.getUserType());
		entity.setCreatedOn(new Date());
		entity.setCreatedBy(notificationDto.getCreatedBy());
		entity.setUserId(notificationDto.getUserId());
		notificationDetailsRepo.save(entity);
		log.info("submitting notification details");
		return response;
	}
	
	@Override
	public NotificationResponseDto notificationRerun(NotificationDTO notificationDto) {
		NotificationResponseDto response = new NotificationResponseDto();
		log.info(" notification rerun until activity completes");
		//LocalDateTime from = LocalDateTime.now();
		
		NotificationDetails entity = notificationDetailsRepo.findByNotificationId(notificationDto.getNotificationId());
		
		//LocalDateTime to2 = entity.getCreatedOn();
	//	Long range = ChronoUnit.DAYS.between((Temporal) entity.getCreatedOn(), (Temporal) new Date()) ;
		log.info("days interval: "+entity.getCreatedOn() +" and type of it: "+entity.getCreatedOn().getClass());
		//between(entity.getCreatedOn(), new Date());
//		if(notificationDto.getUserId().equals(entity.getUserId()) && range > 27 ) {
//			rerun();
//		}
	//	rerun()
		response.setMessage("message");
		return response;
	}
	
	@Scheduled(cron = "0 0 0 * * ?")//fixedRate = 86400000)//(cron = "0 0 0 * * ?"--- 86400000)
	public void rerun() {
		
		
		//to fetch the details where the userId and roleId have completed their work or 
		//not if not then send notifications periodically for last 3 days
		
		List<Object[]> toDoList = notificationDetailsRepo.fetchLandApplicationDetailsToSendNotifications();
		List<JSONObject> obj = new ArrayList<>();
		obj = toDoList.stream().map(objects ->{
			JSONObject object = new JSONObject();
			object.put("userId", objects[0]!= null? (BigInteger)objects[0]:BigInteger.ZERO);
			object.put("landApplicationId", objects[1]!= null? (BigInteger)objects[1]:BigInteger.ZERO);
			object.put("landApplicationNo", objects[2]!= null? objects[2].toString():"0");
			object.put("applicatantName", objects[3]!= null? objects[3].toString():"0");
			object.put("mobileNo", objects[4]!= null? objects[4].toString():"0");
			object.put("distict", objects[5]!= null? objects[5].toString():"0");
			object.put("tahasil", objects[6]!= null? objects[6].toString():"0");
			object.put("village", objects[7]!= null? objects[7].toString():"0");
			object.put("khata", objects[8]!= null? objects[8].toString():"0");
			object.put("khatianCode", objects[9]!= null? objects[9].toString():"0");
			object.put("createdOn", objects[10]!= null? objects[10].toString():"0");
			object.put("ditrictCode", objects[11]!= null? objects[11].toString():"0");
			object.put("tahasilCode", objects[12]!= null? objects[12].toString():"0");
			object.put("villageCode", objects[13]!= null? objects[13].toString():"0");
			object.put("pendingAtRoleId", objects[14]!= null? objects[14].toString():"0");
			return object;
		}).collect(Collectors.toList()); 
		for(JSONObject object : obj) {
			runwork(object);
		//	log.info("action is pending on your end for "+object.getString("landApplicationNo")+" ");
		}
		autoTriggerForCitizenForFormM();
		autoTriggerForLandOfficerForFormM();
		
		
	//	log.info("action pending at your end ");
	}

	public void autoTriggerForLandOfficerForFormM() {
		
		List<Bidderregistrara> landOfficerAutoNotificationList = notificationDetailsRepo.findListForFormM();
		List<BigInteger> landUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("4"));
		if(landOfficerAutoNotificationList != null && landOfficerAutoNotificationList.size() > 0) {
			for(Bidderregistrara entity :landOfficerAutoNotificationList) {
				if(landUserList != null && landUserList.size() > 0) {
					for(BigInteger landUser: landUserList) {
						NotificationDTO notificationDtoForLand = new NotificationDTO();
						notificationDtoForLand.setNotification("action is pending on your end for evaluation form M for "+entity.getUniqueNo()+" .");
						notificationDtoForLand.setUserId(landUser);
						notificationDtoForLand.setCreatedBy(landUser);
						notificationDtoForLand.setUserType("O");
						submitNotification(notificationDtoForLand);
					}
				}
			}
		
		
		}
		
	}

	public void autoTriggerForCitizenForFormM() {
		List<Object[]> citizenAutoNotificationList = notificationDetailsRepo.findCitizens();
		List<JSONObject> objList = citizenAutoNotificationList.stream().map(objects -> {
			JSONObject object = new JSONObject();
			object.put("userId", objects[0]!= null? (BigInteger)objects[0]:BigInteger.ZERO);
			object.put("tenderAuctionId", objects[1]!= null? (BigInteger)objects[1]:BigInteger.ZERO);
			return object;
		}).collect(Collectors.toList());
		for(JSONObject object : objList) {
			BigInteger userIdValue = new BigInteger(object.getString("userId"));
			NotificationDTO dto = new NotificationDTO();
			 dto.setUserType("CI");
			 dto.setCreatedBy(userIdValue);
			 dto.setUserId(userIdValue);
			 dto.setNotification("action is pending on your end for form M. ");
			 try {
				 log.info("user actions required");
				 submitNotification(dto);
				 log.info("submitting notifications");
			 }catch(Exception e) {
				 log.error("error occured while submitting notifications: "+e.getMessage());
			 } 
		}
		
	}

	public void runwork(JSONObject obj) {
		log.info("inside transfer method");
		Date d1 = new Date();
		String value = obj.getString("createdOn");
		BigInteger roleValue = new BigInteger(obj.getString("pendingAtRoleId"));
		List<BigInteger> userList = notificationDetailsRepo.fetchUserDetailsOnRoleId(roleValue);
		log.info("user list are: "+userList);
		Date parsedDate = new Date();
		if (value != null || !value.isEmpty()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				log.info("date conversion");
				parsedDate = dateFormat.parse(value);
			} catch (ParseException e) {
				log.error("date error: " + e.getMessage());
			}
		}
		
		 int diffInDays = (int) ((d1.getTime() - parsedDate.getTime()) / (1000 * 60 * 60 * 24));
		 log.info("date diff." +diffInDays);
		 if(diffInDays >= 27) {
			 for(BigInteger userId :userList) {
				 NotificationDTO dto = new NotificationDTO();
				 dto.setUserType("O");
				 dto.setCreatedBy(userId);
				 dto.setUserId(userId);
				 dto.setNotification("action is pending on your end for "+obj.getString("landApplicationNo")+" ");
				 try {
					 log.info("user actions required");
					 submitNotification(dto);
					 log.info("action is pending on your end for "+obj.getString("landApplicationNo")+" ");
				 }catch(Exception e) {
					 log.error("error occured while submitting notifications: "+e.getMessage());
				 } 
			 }
			 
			 //fetchNotification(dto);
			 
			 /*
			  * entity.setNotification(notificationDto.getNotification());
		entity.setReadMode("N");
		entity.setUserType(notificationDto.getUserType());
		entity.setCreatedOn(new Date());
		entity.setCreatedBy(notificationDto.getCreatedBy());
		entity.setUserId(notificationDto.getUserId());
			  */
		//	submitNotification(dto); 
		//	 log.info("user actions required");
		//	 log.info("action is pending on your end for "+obj.getString("landApplicationNo")+" ");
			 //log.info("action is pending on your end for "+obj.getString("landApplicationNo")+" ");
			 //fetchNotification(null);
		 }
		// TODO Auto-generated method stub
		//call send /fetch notificatioons api
		
	}

}
