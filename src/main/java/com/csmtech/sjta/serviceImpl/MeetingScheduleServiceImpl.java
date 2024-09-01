package com.csmtech.sjta.serviceImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.MeetingPaginationDTO;
import com.csmtech.sjta.dto.MeetingPlotsRecordDTO;
import com.csmtech.sjta.dto.MeetingProceedingsDTO;
import com.csmtech.sjta.dto.MeetingProceedingsOneDTO;
import com.csmtech.sjta.dto.MeetingProceedingsThreeDTO;
import com.csmtech.sjta.dto.MeetingProceedingsTwoDTO;
import com.csmtech.sjta.dto.MeetingScheduleDTO;
import com.csmtech.sjta.dto.MettingUplodeMomAuctionApprovalDTO;
import com.csmtech.sjta.dto.MettingUplodeMomDTO;
import com.csmtech.sjta.dto.MettingUplodeMomPlotsRecordDTO;
import com.csmtech.sjta.dto.NotificationDTO;
import com.csmtech.sjta.entity.Grievance;
import com.csmtech.sjta.entity.Land_applicant;
import com.csmtech.sjta.entity.MeetingSchedule;
import com.csmtech.sjta.mobile.dto.ApplicationFlowDto;
import com.csmtech.sjta.mobile.repository.ApplicationFlowRepository;
import com.csmtech.sjta.mobile.repository.NotificationDetailsRepository;
import com.csmtech.sjta.mobile.service.CommonService;
import com.csmtech.sjta.mobile.service.NotificationDetailsServiceImpl;
import com.csmtech.sjta.repository.LandApplicantJPARepository;
import com.csmtech.sjta.repository.LandAreaStatisticsRepository;
import com.csmtech.sjta.repository.MeetingScheduleNativeRepository;
import com.csmtech.sjta.repository.MeetingScheduleRepository;
import com.csmtech.sjta.service.MeetingScheduleService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.csmtech.sjta.util.MailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
@Service
public class MeetingScheduleServiceImpl implements MeetingScheduleService {
	@Autowired
	private MeetingScheduleRepository meetingScheduleRepository;

	@Autowired
	private MeetingScheduleNativeRepository meetingScheduleRepo;

	@Autowired
	private MailUtil mailutil;

	@Autowired
	EntityManager entityManager;

	@Autowired
	LandAreaStatisticsRepository landGis;

	@Autowired
	CommonService commonService;

	@Autowired
	ApplicationFlowRepository applicationFlowRepo;
	
	@Autowired
	NotificationDetailsServiceImpl notificationDetailsServiceImpl;
	
	@Autowired
	LandApplicantJPARepository landApplicantJpaRepository;
	
	@Autowired
	NotificationDetailsRepository notificationDetailsRepo;

	Integer parentId = 0;
	Object dynamicValue = null;
	private static final Logger logger = LoggerFactory.getLogger(MeetingScheduleServiceImpl.class);

	JSONObject json = new JSONObject();
	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${file.path}")
	private String finalUploadPath;

	@Override
	public JSONObject save(String data) {
		logger.info("Inside save method of Meeting_scheduleServiceImpl");
		try {
			ObjectMapper om = new ObjectMapper();
			MeetingSchedule meetingSchedule = om.readValue(data, MeetingSchedule.class);
			if (meetingSchedule.getMeetingIdRe().compareTo(BigInteger.valueOf(0)) > 0
					&& meetingSchedule.getMeetingSheduleIdRe().compareTo(BigInteger.valueOf(0)) > 0
					&& meetingSchedule.getMeetingLevleIdRe() > 0) {
				BigInteger saveData;
				BigInteger getCountLevleIdPresentOrNot = meetingScheduleRepo.getCountToTrueOrFalseMeetings(
						meetingSchedule.getMeetingIdRe(), meetingSchedule.getSelMeetingLevel55());
				if (getCountLevleIdPresentOrNot.compareTo(BigInteger.ZERO) == 0) {
					saveData = meetingScheduleRepo.saveRecordsForAfetrMeetings(meetingSchedule);
				} else {
					json.put(CommonConstant.STATUS_KEY, 303);
					return json;
				}
				Integer intValue = saveData.intValue();
				String meetingGenerateNo = MeetingScheduleServiceImpl.generateApplicantUniqueNumber("ME");
				meetingScheduleRepo.updateMeetingNo(intValue, meetingGenerateNo);
				List<Object[]> meetingNo = meetingScheduleRepo.getMeetingNoById(intValue);
				meetingScheduleRepo.updateMeetingFlag(meetingSchedule.getMeetingSheduleIdRe());
				List<String> meetingDates = new ArrayList<>();
				List<String> meetingnoRec = new ArrayList<>();
				List<String> venues = new ArrayList<>();

				for (Object[] row : meetingNo) {
					String meetingDate = null;
					String meetingNos = (String) row[0];
					if (row[1] != null) {
						meetingDate = row[1].toString();
					}
					String venue = (String) row[2];
					meetingnoRec.add(meetingNos);
					meetingDates.add(meetingDate);
					venues.add(venue);
				}
				List<Object[]> meetingDetails = applicationFlowRepo.fetchRequiredMeetingDetails(saveData);
				JSONObject obj = new JSONObject();
				for (Object[] row : meetingDetails) {
					obj.put("meetingScheduleId", row[0] != null ? (BigInteger) row[0] : BigInteger.ZERO);
					obj.put("meetingScheduleApplicantId", row[1] != null ? (Integer) row[1] : BigInteger.ZERO);
					obj.put("landApplicationId", row[2] != null ? (BigInteger) row[2] : BigInteger.ZERO);
					BigInteger landApplicationId = row[2] != null ? (BigInteger) row[2] : BigInteger.ZERO;
					if (meetingSchedule.getSelMeetingLevel55() == 2) {
						ApplicationFlowDto dto = new ApplicationFlowDto();
						dto.setLandApplicationId(landApplicationId);
						// dto.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId()));
						dto.setApplicationFlowId(BigInteger.valueOf(17));
						dto.setActionDateTime(new Date());
						dto.setActionRoleId(BigInteger.valueOf(4));
						commonService.saveApplicationFlow(dto);
						
						//manual notification
						//for citizen
						NotificationDTO notificationDTO  = new NotificationDTO();
						Land_applicant entity =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
						notificationDTO.setNotification("TLSC meeting has been scheduled for "+entity.getApplicantNo()+" .");
						notificationDTO.setUserId(new BigInteger(entity.getIntCreatedBy().toString()));
						notificationDTO.setCreatedBy(new BigInteger(entity.getIntCreatedBy().toString()));
						notificationDTO.setUserType("CI");
						notificationDetailsServiceImpl.submitNotification(notificationDTO);
						
						//for officers
						List<Integer> selectedCCOfficers = Arrays.asList(meetingSchedule.getSelectedCCOfficers());
						List<Integer> selectedBCCOfficers = Arrays.asList(meetingSchedule.getSelectedBCCOfficers());
						List<BigInteger> tahasildarUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("14"));
						List<BigInteger> collectorUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("13"));
						//for land officer
						if(meetingSchedule.getIntCreatedBy() != null) {
							NotificationDTO ccUserNotificationDTO  = new NotificationDTO();
					//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
							ccUserNotificationDTO.setNotification("TLSC meeting has been scheduled for "+entity.getApplicantNo()+" .");
							ccUserNotificationDTO.setUserId(new BigInteger(meetingSchedule.getIntCreatedBy().toString()));
							ccUserNotificationDTO.setCreatedBy(new BigInteger(meetingSchedule.getIntCreatedBy().toString()));
							ccUserNotificationDTO.setUserType("O");
							notificationDetailsServiceImpl.submitNotification(notificationDTO);
						}
						if(selectedCCOfficers.size() > 0) {
							for(Integer user :selectedCCOfficers) {
								NotificationDTO ccUserNotificationDTO  = new NotificationDTO();
						//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
								ccUserNotificationDTO.setNotification("TLSC meeting has been scheduled for "+entity.getApplicantNo()+" .");
								ccUserNotificationDTO.setUserId(new BigInteger(user.toString()));
								ccUserNotificationDTO.setCreatedBy(new BigInteger(user.toString()));
								ccUserNotificationDTO.setUserType("O");
								notificationDetailsServiceImpl.submitNotification(notificationDTO);
							}
						}
						if(selectedBCCOfficers.size() > 0) {
							for(Integer user :selectedBCCOfficers) {
								NotificationDTO bccUserNotificationDTO  = new NotificationDTO();
						//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
								bccUserNotificationDTO.setNotification("TLSC meeting has been scheduled for "+entity.getApplicantNo()+" .");
								bccUserNotificationDTO.setUserId(new BigInteger(user.toString()));
								bccUserNotificationDTO.setCreatedBy(new BigInteger(user.toString()));
								bccUserNotificationDTO.setUserType("O");
								notificationDetailsServiceImpl.submitNotification(notificationDTO);
							}
						}
						if(tahasildarUserList != null && tahasildarUserList.size() > 0) {
							for(BigInteger user :tahasildarUserList) {
								NotificationDTO tahasildarUserNotificationDTO  = new NotificationDTO();
						//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
								tahasildarUserNotificationDTO.setNotification("TLSC meeting has been scheduled for "+entity.getApplicantNo()+" .");
								tahasildarUserNotificationDTO.setUserId(user);
								tahasildarUserNotificationDTO.setCreatedBy(user);
								tahasildarUserNotificationDTO.setUserType("O");
								notificationDetailsServiceImpl.submitNotification(notificationDTO);
							}
						}
						if(collectorUserList != null && collectorUserList.size() > 0) {
							for(BigInteger user :collectorUserList) {
								NotificationDTO collectorUserNotificationDTO  = new NotificationDTO();
						//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
								collectorUserNotificationDTO.setNotification("TLSC meeting has been scheduled for "+entity.getApplicantNo()+" .");
								collectorUserNotificationDTO.setUserId(user);
								collectorUserNotificationDTO.setCreatedBy(user);
								collectorUserNotificationDTO.setUserType("O");
								notificationDetailsServiceImpl.submitNotification(notificationDTO);
							}
						}

						
					} else if (meetingSchedule.getSelMeetingLevel55() == 3) {
						ApplicationFlowDto dto = new ApplicationFlowDto();
						dto.setLandApplicationId(landApplicationId);
						// dto.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId()));
						dto.setApplicationFlowId(BigInteger.valueOf(18));
						dto.setActionDateTime(new Date());
						dto.setActionRoleId(BigInteger.valueOf(4));
						commonService.saveApplicationFlow(dto);
						
						//manual notification
						//for citizen
						NotificationDTO notificationDTO  = new NotificationDTO();
						Land_applicant entity =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
						notificationDTO.setNotification("MC meeting has been scheduled for "+entity.getApplicantNo()+" .");
						notificationDTO.setUserId(new BigInteger(entity.getIntCreatedBy().toString()));
						notificationDTO.setCreatedBy(new BigInteger(entity.getIntCreatedBy().toString()));
						notificationDTO.setUserType("CI");
						notificationDetailsServiceImpl.submitNotification(notificationDTO);
						
						//for officers
						List<Integer> selectedCCOfficers = Arrays.asList(meetingSchedule.getSelectedCCOfficers());
						List<Integer> selectedBCCOfficers = Arrays.asList(meetingSchedule.getSelectedBCCOfficers());
						List<BigInteger> tahasildarUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("14"));
						List<BigInteger> collectorUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("13"));
						if(meetingSchedule.getIntCreatedBy() != null) {
							NotificationDTO ccUserNotificationDTO  = new NotificationDTO();
					//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
							ccUserNotificationDTO.setNotification("MC meeting has been scheduled for "+entity.getApplicantNo()+" .");
							ccUserNotificationDTO.setUserId(new BigInteger(meetingSchedule.getIntCreatedBy().toString()));
							ccUserNotificationDTO.setCreatedBy(new BigInteger(meetingSchedule.getIntCreatedBy().toString()));
							ccUserNotificationDTO.setUserType("O");
							notificationDetailsServiceImpl.submitNotification(notificationDTO);
						}
						if(selectedCCOfficers.size() > 0) {
							for(Integer user :selectedCCOfficers) {
								NotificationDTO ccUserNotificationDTO  = new NotificationDTO();
						//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
								ccUserNotificationDTO.setNotification("MC meeting has been scheduled for "+entity.getApplicantNo()+" .");
								ccUserNotificationDTO.setUserId(new BigInteger(user.toString()));
								ccUserNotificationDTO.setCreatedBy(new BigInteger(user.toString()));
								ccUserNotificationDTO.setUserType("O");
								notificationDetailsServiceImpl.submitNotification(notificationDTO);
							}
						}
						if(selectedBCCOfficers.size() > 0) {
							for(Integer user :selectedBCCOfficers) {
								NotificationDTO bccUserNotificationDTO  = new NotificationDTO();
						//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
								bccUserNotificationDTO.setNotification("MC meeting has been scheduled for "+entity.getApplicantNo()+" .");
								bccUserNotificationDTO.setUserId(new BigInteger(user.toString()));
								bccUserNotificationDTO.setCreatedBy(new BigInteger(user.toString()));
								bccUserNotificationDTO.setUserType("O");
								notificationDetailsServiceImpl.submitNotification(notificationDTO);
							}
						}
						if(tahasildarUserList != null && tahasildarUserList.size() > 0) {
							for(BigInteger user :tahasildarUserList) {
								NotificationDTO tahasildarUserNotificationDTO  = new NotificationDTO();
						//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
								tahasildarUserNotificationDTO.setNotification("MC meeting has been scheduled for "+entity.getApplicantNo()+" .");
								tahasildarUserNotificationDTO.setUserId(user);
								tahasildarUserNotificationDTO.setCreatedBy(user);
								tahasildarUserNotificationDTO.setUserType("O");
								notificationDetailsServiceImpl.submitNotification(notificationDTO);
							}
						}
						if(collectorUserList != null && collectorUserList.size() > 0) {
							for(BigInteger user :collectorUserList) {
								NotificationDTO collectorUserNotificationDTO  = new NotificationDTO();
						//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
								collectorUserNotificationDTO.setNotification("MC meeting has been scheduled for "+entity.getApplicantNo()+" .");
								collectorUserNotificationDTO.setUserId(user);
								collectorUserNotificationDTO.setCreatedBy(user);
								collectorUserNotificationDTO.setUserType("O");
								notificationDetailsServiceImpl.submitNotification(notificationDTO);
							}
						}

						
					} else if (meetingSchedule.getSelMeetingLevel55() == 4) {
						ApplicationFlowDto dto = new ApplicationFlowDto();
						// dto.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId()));
						dto.setLandApplicationId(landApplicationId);
						dto.setApplicationFlowId(BigInteger.valueOf(19));
						dto.setActionDateTime(new Date());
						dto.setActionRoleId(BigInteger.valueOf(4));
						commonService.saveApplicationFlow(dto);
						
						//manual notification
						//for citizen
						NotificationDTO notificationDTO  = new NotificationDTO();
						Land_applicant entity =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
						notificationDTO.setNotification("SLC meeting has been scheduled for "+entity.getApplicantNo()+" .");
						notificationDTO.setUserId(new BigInteger(entity.getIntCreatedBy().toString()));
						notificationDTO.setCreatedBy(new BigInteger(entity.getIntCreatedBy().toString()));
						notificationDTO.setUserType("CI");
						notificationDetailsServiceImpl.submitNotification(notificationDTO);
						
						//for officers
						List<Integer> selectedCCOfficers = Arrays.asList(meetingSchedule.getSelectedCCOfficers());
						List<Integer> selectedBCCOfficers = Arrays.asList(meetingSchedule.getSelectedBCCOfficers());
						List<BigInteger> tahasildarUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("14"));
						List<BigInteger> collectorUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("13"));
						if(meetingSchedule.getIntCreatedBy() != null) {
							NotificationDTO ccUserNotificationDTO  = new NotificationDTO();
					//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
							ccUserNotificationDTO.setNotification("SLC meeting has been scheduled for "+entity.getApplicantNo()+" .");
							ccUserNotificationDTO.setUserId(new BigInteger(meetingSchedule.getIntCreatedBy().toString()));
							ccUserNotificationDTO.setCreatedBy(new BigInteger(meetingSchedule.getIntCreatedBy().toString()));
							ccUserNotificationDTO.setUserType("O");
							notificationDetailsServiceImpl.submitNotification(notificationDTO);
						}
						if(selectedCCOfficers.size() > 0) {
							for(Integer user :selectedCCOfficers) {
								NotificationDTO ccUserNotificationDTO  = new NotificationDTO();
						//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
								ccUserNotificationDTO.setNotification("SLC meeting has been scheduled for "+entity.getApplicantNo()+" .");
								ccUserNotificationDTO.setUserId(new BigInteger(user.toString()));
								ccUserNotificationDTO.setCreatedBy(new BigInteger(user.toString()));
								ccUserNotificationDTO.setUserType("O");
								notificationDetailsServiceImpl.submitNotification(notificationDTO);
							}
						}
						if(selectedBCCOfficers.size() > 0) {
							for(Integer user :selectedBCCOfficers) {
								NotificationDTO bccUserNotificationDTO  = new NotificationDTO();
						//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
								bccUserNotificationDTO.setNotification("SLC meeting has been scheduled for "+entity.getApplicantNo()+" .");
								bccUserNotificationDTO.setUserId(new BigInteger(user.toString()));
								bccUserNotificationDTO.setCreatedBy(new BigInteger(user.toString()));
								bccUserNotificationDTO.setUserType("O");
								notificationDetailsServiceImpl.submitNotification(notificationDTO);
							}
						}
						if(tahasildarUserList != null && tahasildarUserList.size() > 0) {
							for(BigInteger user :tahasildarUserList) {
								NotificationDTO tahasildarUserNotificationDTO  = new NotificationDTO();
						//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
								tahasildarUserNotificationDTO.setNotification("SLC meeting has been scheduled for "+entity.getApplicantNo()+" .");
								tahasildarUserNotificationDTO.setUserId(user);
								tahasildarUserNotificationDTO.setCreatedBy(user);
								tahasildarUserNotificationDTO.setUserType("O");
								notificationDetailsServiceImpl.submitNotification(notificationDTO);
							}
						}
						if(collectorUserList != null && collectorUserList.size() > 0) {
							for(BigInteger user :collectorUserList) {
								NotificationDTO collectorUserNotificationDTO  = new NotificationDTO();
						//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
								collectorUserNotificationDTO.setNotification("SLC meeting has been scheduled for "+entity.getApplicantNo()+" .");
								collectorUserNotificationDTO.setUserId(user);
								collectorUserNotificationDTO.setCreatedBy(user);
								collectorUserNotificationDTO.setUserType("O");
								notificationDetailsServiceImpl.submitNotification(notificationDTO);
							}
						}
						
					}

				}
				json.put(CommonConstant.STATUS_KEY, 200);
				json.put("meetingNo", meetingnoRec.get(0));
				json.put("meetingDate", meetingDates.get(0));
				json.put("venue", venues.get(0));
				json.put("id", saveData);

			} else {
				if (!Objects.isNull(meetingSchedule.getIntId()) && meetingSchedule.getIntId() > 0) {
					Integer updateData = meetingScheduleRepo.updateRecord(meetingSchedule);
					if (updateData == 0) {
						json.put(CommonConstant.STATUS_KEY, 407);
					} else {
						parentId = updateData;
						json.put(CommonConstant.STATUS_KEY, 202);
					}
				} else {
					String meetingGenerateNo = MeetingScheduleServiceImpl.generateApplicantUniqueNumber("ME");
					Integer saveData = meetingScheduleRepo.saveRecord(meetingSchedule);
					BigInteger idmeeting = new BigInteger(saveData.toString());
					Object noOfPlots = meetingScheduleRepo.getPlot(idmeeting);
					if (meetingSchedule.getSelMeetingLevel55() == 1) {
						Integer countInsert = landGis.updateLandGis(noOfPlots, 3, 1);
					}
					parentId = saveData;
					String updatemeetingNo = meetingScheduleRepo.updateMeetingNo(parentId, meetingGenerateNo);
					List<Object[]> meetingNo = meetingScheduleRepo.getMeetingNoById(parentId);
					List<String> venues = new ArrayList<>();
					List<String> meetingDates = new ArrayList<>();
					List<String> meetingnoRec = new ArrayList<>();

					for (Object[] row : meetingNo) {
						String meetingDate = null;
						String meetingNos = (String) row[0];
						if (row[1] != null) {
							meetingDate = row[1].toString();
						}
						String venue = (String) row[2];
						meetingnoRec.add(meetingNos);
						meetingDates.add(meetingDate);
						venues.add(venue);
					}

					logger.info(updatemeetingNo);
					json.put(CommonConstant.STATUS_KEY, 200);
					json.put("meetingNo", meetingnoRec.get(0));
					json.put("meetingDate", meetingDates.get(0));
					json.put("venue", venues.get(0));
					json.put("id", parentId);

					// add the application flow lines of codes for dlsc meeting
					// added for inserting in db only(application flow) Application Forwarded by
					// done by Land Officer
					BigInteger idValue = new BigInteger(saveData.toString());
					log.info("meetingScheduleIdvalue: " + idmeeting + " and type: " + idmeeting.getClass());
					List<Object[]> meetingDetails = applicationFlowRepo.fetchRequiredMeetingDetails(idValue);
					JSONObject obj = new JSONObject();
					for (Object[] row : meetingDetails) {
						obj.put("meetingScheduleId", row[0] != null ? (BigInteger) row[0] : BigInteger.ZERO);
						obj.put("meetingScheduleApplicantId", row[1] != null ? (Integer) row[1] : BigInteger.ZERO);
						obj.put("landApplicationId", row[2] != null ? (BigInteger) row[2] : BigInteger.ZERO);
						BigInteger landApplicationId = row[2] != null ? (BigInteger) row[2] : BigInteger.ZERO;
						log.info("data value: " + obj);
						ApplicationFlowDto dto = new ApplicationFlowDto();
						dto.setLandApplicationId(landApplicationId);
						// dto.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId()));
						dto.setApplicationFlowId(BigInteger.valueOf(16));
						dto.setActionDateTime(new Date());
						dto.setActionRoleId(BigInteger.valueOf(4));
						commonService.saveApplicationFlow(dto);
						
						
						//manual notification
						//for citizen
						NotificationDTO notificationDTO  = new NotificationDTO();
						Land_applicant entity =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
						notificationDTO.setNotification("DLSC meeting has been scheduled for "+entity.getApplicantNo()+" .");
						notificationDTO.setUserId(new BigInteger(entity.getIntCreatedBy().toString()));
						notificationDTO.setCreatedBy(new BigInteger(entity.getIntCreatedBy().toString()));
						notificationDTO.setUserType("CI");
						notificationDetailsServiceImpl.submitNotification(notificationDTO);
						
						//for officers
						List<Integer> selectedCCOfficers = Arrays.asList(meetingSchedule.getSelectedCCOfficers());
						List<Integer> selectedBCCOfficers = Arrays.asList(meetingSchedule.getSelectedBCCOfficers());
						List<BigInteger> tahasildarUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("14"));
						List<BigInteger> collectorUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("13"));
						if(meetingSchedule.getIntCreatedBy() != null) {
							NotificationDTO ccUserNotificationDTO  = new NotificationDTO();
					//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
							ccUserNotificationDTO.setNotification("DLSC meeting has been scheduled for "+entity.getApplicantNo()+" .");
							ccUserNotificationDTO.setUserId(new BigInteger(meetingSchedule.getIntCreatedBy().toString()));
							ccUserNotificationDTO.setCreatedBy(new BigInteger(meetingSchedule.getIntCreatedBy().toString()));
							ccUserNotificationDTO.setUserType("O");
							notificationDetailsServiceImpl.submitNotification(notificationDTO);
						}
						if(selectedCCOfficers.size() > 0) {
							for(Integer user :selectedCCOfficers) {
								NotificationDTO ccUserNotificationDTO  = new NotificationDTO();
						//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
								ccUserNotificationDTO.setNotification("DLSC meeting has been scheduled for "+entity.getApplicantNo()+" .");
								ccUserNotificationDTO.setUserId(new BigInteger(user.toString()));
								ccUserNotificationDTO.setCreatedBy(new BigInteger(user.toString()));
								ccUserNotificationDTO.setUserType("O");
								notificationDetailsServiceImpl.submitNotification(notificationDTO);
							}
						}
						if(selectedBCCOfficers.size() > 0) {
							for(Integer user :selectedBCCOfficers) {
								NotificationDTO bccUserNotificationDTO  = new NotificationDTO();
						//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
								bccUserNotificationDTO.setNotification("DLSC meeting has been scheduled for "+entity.getApplicantNo()+" .");
								bccUserNotificationDTO.setUserId(new BigInteger(user.toString()));
								bccUserNotificationDTO.setCreatedBy(new BigInteger(user.toString()));
								bccUserNotificationDTO.setUserType("O");
								notificationDetailsServiceImpl.submitNotification(notificationDTO);
							}
						}
						if(tahasildarUserList != null && tahasildarUserList.size() > 0) {
							for(BigInteger user :tahasildarUserList) {
								NotificationDTO tahasildarUserNotificationDTO  = new NotificationDTO();
						//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
								tahasildarUserNotificationDTO.setNotification("DLSC meeting has been scheduled for "+entity.getApplicantNo()+" .");
								tahasildarUserNotificationDTO.setUserId(user);
								tahasildarUserNotificationDTO.setCreatedBy(user);
								tahasildarUserNotificationDTO.setUserType("O");
								notificationDetailsServiceImpl.submitNotification(notificationDTO);
							}
						}
						if(collectorUserList != null && collectorUserList.size() > 0) {
							for(BigInteger user :collectorUserList) {
								NotificationDTO collectorUserNotificationDTO  = new NotificationDTO();
						//		Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
								collectorUserNotificationDTO.setNotification("DLSC meeting has been scheduled for "+entity.getApplicantNo()+" .");
								collectorUserNotificationDTO.setUserId(user);
								collectorUserNotificationDTO.setCreatedBy(user);
								collectorUserNotificationDTO.setUserType("O");
								notificationDetailsServiceImpl.submitNotification(notificationDTO);
							}
						}
						
					}
				}
			}
		} catch (Exception e) {
			logger.error("Inside save method of MeetingScheduleServiceImpl, an error occurred: {}", e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	@Override
	public JSONObject updateFile(String data) {
		logger.info("Inside file update method of Meeting_scheduleServiceImpl");
		try {
			ObjectMapper om = new ObjectMapper();
			MeetingSchedule meetingSchedule = om.readValue(data, MeetingSchedule.class);
			List<String> fileUploadList = new ArrayList<>();
			MeetingSchedule updateData = meetingScheduleRepo.updateFile(meetingSchedule);
			logger.info("file updated");
			parentId = updateData.getIntId();
			json.put(CommonConstant.STATUS_KEY, 202);
			json.put("id", parentId);
			fileUploadList.add(meetingSchedule.getFileUploadDocument());

			for (String fileUpload : fileUploadList) {
				if (fileUpload != null && (!fileUpload.equals(""))) {
					File f = new File(tempUploadPath + fileUpload);
					if (f.exists()) {
						String uploadPath = finalUploadPath;
						Path srcPath = Paths.get(tempUploadPath + fileUpload);
						Path destPath = Paths.get(uploadPath + "/" + "meeting/" + fileUpload);

						try {
							CommonUtil.createDirectories(destPath.getParent());
							CommonUtil.copyAndDeleteFile(srcPath, destPath);
						} catch (IOException e) {
							logger.error("Error occurred , while copying file :{}", e.getMessage());
						}

					}

				}
			}
		} catch (Exception e) {
			logger.error("Inside updateFile method of MeetingScheduleServiceImpl, an error occurred: {}",
					e.getMessage());

			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(Integer id, Integer auctionFlag) {
		logger.info("Inside getById method of Meeting_scheduleServiceImpl");
		MeetingScheduleDTO entity = meetingScheduleRepo.findDataByIntId(id, auctionFlag);
		if (entity != null) {
			return new JSONObject(entity);
		} else {
			return null;
		}
	}

	@Override
	public JSONObject getAll(String formParams) {
		logger.info("Inside getAll method of Meeting_scheduleServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		String txtMeetingDate53 = "";
		String selMeetingLevel55 = "";
		if (jsonData.has("txtMeetingDate53") && !jsonData.isNull("txtMeetingDate53")
				&& !jsonData.getString("txtMeetingDate53").equals("")) {
			txtMeetingDate53 = jsonData.getString("txtMeetingDate53");
		}
		if (jsonData.has("selMeetingLevel55") && !jsonData.isNull("selMeetingLevel55")
				&& !jsonData.getString("selMeetingLevel55").equals("")) {
			selMeetingLevel55 = jsonData.getString("selMeetingLevel55");
		}
		Integer totalDataPresent = meetingScheduleRepository.countByBitDeletedFlag();
		Pageable pageRequest = PageRequest.of(jsonData.has("pageNo") ? jsonData.getInt("pageNo") - 1 : 0,
				jsonData.has("size") ? jsonData.getInt("size") : totalDataPresent);
		List<MeetingSchedule> meeting_scheduleResp = meetingScheduleRepository.findAllByBitDeletedFlag(pageRequest);
		for (MeetingSchedule entity : meeting_scheduleResp) {

			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select meeting_level from m_meeting_level where meeting_level_id="
								+ entity.getSelMeetingLevel55());
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelMeetingLevel55Val(dynamicValue.toString());

		}
		json.put(CommonConstant.STATUS_KEY, 200);
		json.put("result", new JSONArray(meeting_scheduleResp));
		json.put("count", totalDataPresent);
		return json;
	}

	@Override
	public JSONObject deleteById(Integer id, Integer updatedby, Integer auctionFlag) {
		logger.info("Inside deleteById method of Meeting_scheduleServiceImpl");
		try {
			BigInteger bigVal = BigInteger.valueOf(id);
			BigInteger countUpdateApplicant = null;
			if (auctionFlag == 3) {
				countUpdateApplicant = BigInteger.valueOf(1);
			} else {
				countUpdateApplicant = meetingScheduleRepo.getCountForMeetingSchedule(bigVal);
			}

			if (countUpdateApplicant.equals(BigInteger.valueOf(1))) {
				if (auctionFlag == 3) {
					Integer countUpdateAuction = meetingScheduleRepo.updateplotForAuction(bigVal);
					logger.info("countApplicnt::" + countUpdateAuction);
				} else {
					Integer countUpdate = meetingScheduleRepo.updateLandApplicationMeetingFlag(bigVal);
					logger.info("countApplicnt::" + countUpdate);
				}
			}
			meetingScheduleRepo.deleteRecord(id, updatedby);
			json.put(CommonConstant.STATUS_KEY, 200);
		} catch (Exception e) {
			logger.error("Inside deleteById method of MeetingScheduleServiceImpl, an error occurred: {}",
					e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	public static JSONArray fillselMeetingLevel55List(EntityManager em, String jsonVal) {
		logger.info("Inside fillselMeetingLevel55List method of Meeting_scheduleServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		Integer val = jsonDepend.getInt("auctionFlag");
		String query = "";
		if (val == 3) {
			query = "Select meeting_level_id,meeting_level from m_meeting_level where status = '0'  Order by 1";
		} else {
			query = "Select meeting_level_id,meeting_level from m_meeting_level where status = '0'  AND (meeting_level_id = 1 OR  meeting_level_id = 3)   Order by 1";
		}
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("meeting_level_id", data[0]);
			jsonObj.put("meeting_level", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	private static final String DATA_FILE_PATH = "MeetingUniqueNumberGenerator.txt";
	private static Integer coutTabRecord1;
	private static Date lastGeneratedDate;
	static {
		readCounterDataFromFile();
	}

	public static synchronized String generateApplicantUniqueNumber(String appName) {
		String dateFormat = "ddMMyy";
		Date currentDate = new Date();
		if (!isSameDate(currentDate, lastGeneratedDate)) {
			lastGeneratedDate = currentDate;
			coutTabRecord1 = 1;
		}

		String formattedDate = new SimpleDateFormat(dateFormat).format(currentDate);
		String formattedCounter = String.format("%03d", getNextCounterValue());
		saveCounterDataToFile();
		return appName + formattedDate + formattedCounter;
	}

	private static boolean isSameDate(Date date1, Date date2) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return dateFormat.format(date1).equals(dateFormat.format(date2));
	}

	private static int getNextCounterValue() {
		return coutTabRecord1++;
	}

	private static void readCounterDataFromFile() {
		try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE_PATH))) {
			lastGeneratedDate = new SimpleDateFormat("yyyyMMdd").parse(reader.readLine());
			coutTabRecord1 = Integer.parseInt(reader.readLine());
		} catch (Exception e) {
			lastGeneratedDate = new Date();
			coutTabRecord1 = 1;
		}
	}

	private static void saveCounterDataToFile() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE_PATH))) {
			writer.write(new SimpleDateFormat("yyyyMMdd").format(lastGeneratedDate));
			writer.newLine();
			writer.write(coutTabRecord1.toString());
		} catch (IOException e) {
			e.getMessage();
		}
	}

	@Override
	public MeetingPaginationDTO viewAll(Integer pageNumber, Integer pageSize, String applicationNo, String khataNo,
			Date meetingDate, BigInteger meetingId) {
		List<MeetingScheduleDTO> response = null;
		BigInteger count = null;
		response = meetingScheduleRepo.viewMeetingScheduleDetails(pageNumber, pageSize, applicationNo, khataNo,
				meetingDate, meetingId);
		count = meetingScheduleRepo.getTotalMeetingScheduleCount(applicationNo, khataNo, meetingDate, meetingId);
		return new MeetingPaginationDTO(response, count);
	}

	@Override
	public JSONObject validate(String data) {
		logger.info("Inside save method of Meeting_scheduleServiceImpl");
		try {
			ObjectMapper om = new ObjectMapper();
			MeetingSchedule meetingSchedule = om.readValue(data, MeetingSchedule.class);

			Integer saveData = meetingScheduleRepo.validate(meetingSchedule);
			if (saveData == -1) {
				json.put(CommonConstant.STATUS_KEY, 408);
			} else if (saveData == 0) {
				json.put(CommonConstant.STATUS_KEY, 407);
			} else if (saveData == -2) {
				json.put(CommonConstant.STATUS_KEY, 406);
			} else {
				json.put(CommonConstant.STATUS_KEY, 200);
			}

		} catch (Exception e) {

			logger.error("Inside save method of MeetingScheduleServiceImpl, an error occurred: {}", e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	public static JSONArray fillselDistrictNameList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();

		String query = " SELECT DISTINCT dm.district_code AS district_id, dm.district_name AS district_name "
				+ "FROM land_bank.district_master dm "
				+ "JOIN land_bank.tahasil_master ki ON ki.district_code = dm.district_code "
				+ "JOIN land_bank.village_master kii ON ki.tahasil_code = kii.tahasil_code "
				+ "JOIN land_bank.khatian_information kiii ON kii.village_code = kiii.village_code "
				+ "WHERE dm.state_code = 'OD' " + "ORDER BY district_name; " + " ";

		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("district_id", data[0]);
			jsonObj.put("district_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselTehsilNameList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = jsonDepend.get("district_id").toString();
		String query = " select DISTINCT (ki.tahasil_code) as tehsil_id,(ki.tahasil_name) as "
				+ " tehsil_name from land_bank.tahasil_master ki "
				+ " JOIN land_bank.village_master kii ON ki.tahasil_code = kii.tahasil_code "
				+ " JOIN land_bank.khatian_information kiii ON kii.village_code = kiii.village_code  "
				+ " where  district_code= '" + val + "' order by tehsil_name";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("tehsil_id", data[0]);
			jsonObj.put("tehsil_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	@Override
	public JSONObject printPdf(String data) {
		try {
			ObjectMapper om = new ObjectMapper();
			MeetingSchedule meetingSchedule = om.readValue(data, MeetingSchedule.class);
			List<Object> entity = meetingScheduleRepo.printPdf(meetingSchedule.getIntId(),
					meetingSchedule.getAuctionFlag());
			json.put(CommonConstant.STATUS_KEY, 200);
			json.put("result", new JSONArray(entity));

		} catch (Exception e) {
			logger.error("Inside save method of MeetingScheduleServiceImpl, an error occurred: {}", e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	@Override
	public JSONObject sendMail(String data) {
		try {
			ObjectMapper om = new ObjectMapper();
			MeetingSchedule meetingSchedule = om.readValue(data, MeetingSchedule.class);

			meetingScheduleRepo.updateStatus(meetingSchedule.getIntId());
			List<Object[]> result = meetingScheduleRepo.fetchMails(meetingSchedule.getIntId(),
					meetingSchedule.getAuctionFlag());

			if (Arrays.toString(result.get(0)).equals("")) {
				logger.info("Mail is not available.");
			} else {
				String[] applicantMail = null;
				String[] ccMail = null;
				String[] bccMail = null;

				JSONObject mailData = new JSONObject();

				for (Object[] data1 : result) {
					applicantMail = data1[4] != null ? data1[4].toString().split(",") : null;
					ccMail = data1[5] != null ? data1[5].toString().split(",") : null;
					bccMail = data1[6] != null ? data1[5].toString().split(",") : null;

					mailData.put("date", data1[0]);
					mailData.put("time", data1[1]);
					mailData.put("venue", data1[2]);
					mailData.put("purpose", data1[3]);
				}

				List<String> recipientEmails = new ArrayList<>();
				if (applicantMail != null && applicantMail.length != 0) {
					recipientEmails = Arrays.asList(applicantMail);
				}

				List<String> ccEmails = null;
				if (ccMail != null) {
					ccEmails = new ArrayList<>();
					ccEmails = Arrays.asList(ccMail);
				}
				List<String> bccEmails = null;
				if (bccMail != null) {
					bccEmails = new ArrayList<>();
					bccEmails = Arrays.asList(bccMail);
				}
				String subject = "Meeting Schedule Details || SJTA";
				Short status = 0;

				mailutil.sendEmail(4, status, mailData, subject, recipientEmails, ccEmails, bccEmails);
				logger.info("Mail Send Sucess ");
			}

			json.put(CommonConstant.STATUS_KEY, 200);
			json.put("result", "");

		} catch (Exception e) {
			logger.error("Inside save method of MeetingScheduleServiceImpl, an error occurred: {}", e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	public static JSONArray fillselplotsList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String tahasilCode = jsonDepend.get("tehsil_id").toString();
		String nativeQuery = "SELECT DISTINCT ls.plot_code, pi.plot_no " + "FROM public.land_schedule ls "
				+ "JOIN public.land_application la ON(ls.land_application_id=la.land_application_id) "
				+ "JOIN public.land_application_approval laa ON(laa.land_application_id=la.land_application_id) "
				+ "JOIN land_bank.plot_information pi ON ls.plot_code = pi.plot_code "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code = pi.khatian_code "
				+ "JOIN land_bank.village_master vm ON vm.village_code = ki.village_code "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code = vm.tahasil_code "
				+ "LEFT JOIN application.land_allotement_for_auction lafa ON ls.plot_code = lafa.plot_code  "
				+ "WHERE tm.tahasil_code =:tahasilCode  AND ls.deleted_flag = '0' AND la.deleted_flag='0' AND laa.status='0' "
				+ "AND laa.approval_action_id = 6 AND land_allotement_for_auction_id IS NULL  AND ls.meeting_schedule_flag = '0' ORDER BY pi.plot_no ";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(nativeQuery).setParameter("tahasilCode", tahasilCode)
				.getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("plot_code", data[0]);
			jsonObj.put("plot_no", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	@Override
	public JSONObject getLandApplicationData(BigInteger meetingId, Integer auctionFlag) { // BigInteger meetingMainId
		JSONObject jsobj = new JSONObject();
		List<MeetingProceedingsDTO> respones = new ArrayList<>();
		List<MeetingProceedingsOneDTO> respones1 = new ArrayList<>();
		List<MeetingProceedingsTwoDTO> respones2 = new ArrayList<>();
		List<MeetingProceedingsThreeDTO> respones3 = new ArrayList<>();
		if (auctionFlag == 3) {
			List<Object[]> res = meetingScheduleRepo.getAuctionApplicantRecord(meetingId);
			for (Object[] row : res) {
				MeetingProceedingsDTO dto = new MeetingProceedingsDTO();
				dto.setLandAppName((String) row[0]);
				dto.setLandAppNo((String) row[1]);
				dto.setPlotCode((String) row[2]);
				dto.setLandAppId((BigInteger) row[3]);
				dto.setMeetingId((BigInteger) row[4]);
				dto.setApprovalStatus((Integer) row[5]);
				dto.setMettingLevleId((Short) row[6]);
				dto.setPlotIds((String) row[7]);
				dto.setPlotNo((String) row[8]);
				respones.add(dto);
			}
		} else {

			List<Object[]> result = meetingScheduleRepo.getLandApplicationData(meetingId);
			for (Object[] row : result) {
				MeetingProceedingsDTO dto = new MeetingProceedingsDTO();
				dto.setMeetingId((BigInteger) row[0]);
				dto.setPlotNo((String) row[1]);
				dto.setLandAppId((BigInteger) row[2]);
				dto.setLandAppName((String) row[3]);
				dto.setLandAppNo((String) row[4]);
				dto.setMettingLevleId((Short) row[5]);
				dto.setPlotIds((String) row[6]);
				dto.setApprovalStatus((Integer) row[7]);
				dto.setPlotCode((String) row[8]);
				dto.setMeetingAuctionStatus((Integer) row[9]);
				dto.setDirectMeetingFlag((Short) row[10]);
				respones.add(dto);
			}

//			Short getMeetingLevleId=meetingScheduleRepo.getMeetingLevleId(meetingId);
//			if(getMeetingLevleId==1) {
//				List<Object[]> result = meetingScheduleRepo.getLevleIdThroughData(meetingId,1);
//				for (Object[] row : result) {
//					MeetingProceedingsDTO dto = new MeetingProceedingsDTO();
//					dto.setMeetingId((BigInteger) row[0]);
//					dto.setPlotNo((String) row[1]);
//					dto.setLandAppId((BigInteger) row[2]);
//					dto.setLandAppName((String) row[3]);
//					dto.setLandAppNo((String) row[4]);
//					dto.setMettingLevleId((Short) row[5]);
//					dto.setPlotIds((String) row[6]);
//					dto.setApprovalStatus((Integer) row[7]);
//					respones.add(dto);
//				}
//                
//			}else if(getMeetingLevleId==2) {
//				List<Object[]> result = meetingScheduleRepo.getLevleIdThroughData(meetingMainId,1);
//				for (Object[] row : result) {
//					MeetingProceedingsDTO dto = new MeetingProceedingsDTO();
//					dto.setMeetingId((BigInteger) row[0]);
//					dto.setPlotNo((String) row[1]);
//					dto.setLandAppId((BigInteger) row[2]);
//					dto.setLandAppName((String) row[3]);
//					dto.setLandAppNo((String) row[4]);
//					dto.setMettingLevleId((Short) row[5]);
//					dto.setPlotIds((String) row[6]);
//					dto.setApprovalStatus((Integer) row[7]);
//					respones.add(dto);
//				}
//				List<Object[]> result2 = meetingScheduleRepo.getLevleIdThroughData(meetingMainId,2);
//				for (Object[] row : result2) {
//					MeetingProceedingsOneDTO dto = new MeetingProceedingsOneDTO();
//					dto.setMeetingId((BigInteger) row[0]);
//					dto.setPlotNo((String) row[1]);
//					dto.setLandAppId((BigInteger) row[2]);
//					dto.setLandAppName((String) row[3]);
//					dto.setLandAppNo((String) row[4]);
//					dto.setMettingLevleId((Short) row[5]);
//					dto.setPlotIds((String) row[6]);
//					dto.setApprovalStatus((Integer) row[7]);
//					respones1.add(dto);
//				}
//			}else if(getMeetingLevleId==3) {
//				List<Object[]> result = meetingScheduleRepo.getLevleIdThroughData(meetingMainId,1);
//				for (Object[] row : result) {
//					MeetingProceedingsDTO dto = new MeetingProceedingsDTO();
//					dto.setMeetingId((BigInteger) row[0]);
//					dto.setPlotNo((String) row[1]);
//					dto.setLandAppId((BigInteger) row[2]);
//					dto.setLandAppName((String) row[3]);
//					dto.setLandAppNo((String) row[4]);
//					dto.setMettingLevleId((Short) row[5]);
//					dto.setPlotIds((String) row[6]);
//					dto.setApprovalStatus((Integer) row[7]);
//					respones.add(dto);
//				}
//				List<Object[]> result2 = meetingScheduleRepo.getLevleIdThroughData(meetingMainId,2);
//				for (Object[] row : result2) {
//					MeetingProceedingsOneDTO dto = new MeetingProceedingsOneDTO();
//					dto.setMeetingId((BigInteger) row[0]);
//					dto.setPlotNo((String) row[1]);
//					dto.setLandAppId((BigInteger) row[2]);
//					dto.setLandAppName((String) row[3]);
//					dto.setLandAppNo((String) row[4]);
//					dto.setMettingLevleId((Short) row[5]);
//					dto.setPlotIds((String) row[6]);
//					dto.setApprovalStatus((Integer) row[7]);
//					respones1.add(dto);
//				}
//				List<Object[]> result3 = meetingScheduleRepo.getLevleIdThroughData(meetingMainId,3);
//				for (Object[] row : result3) {
//					MeetingProceedingsTwoDTO dto = new MeetingProceedingsTwoDTO();
//					dto.setMeetingId((BigInteger) row[0]);
//					dto.setPlotNo((String) row[1]);
//					dto.setLandAppId((BigInteger) row[2]);
//					dto.setLandAppName((String) row[3]);
//					dto.setLandAppNo((String) row[4]);
//					dto.setMettingLevleId((Short) row[5]);
//					dto.setPlotIds((String) row[6]);
//					dto.setApprovalStatus((Integer) row[7]);
//					respones2.add(dto);
//				}
//				
//
//			}else if(getMeetingLevleId==4) {
//				List<Object[]> result = meetingScheduleRepo.getLevleIdThroughData(meetingMainId,1);
//				for (Object[] row : result) {
//					MeetingProceedingsDTO dto = new MeetingProceedingsDTO();
//					dto.setMeetingId((BigInteger) row[0]);
//					dto.setPlotNo((String) row[1]);
//					dto.setLandAppId((BigInteger) row[2]);
//					dto.setLandAppName((String) row[3]);
//					dto.setLandAppNo((String) row[4]);
//					dto.setMettingLevleId((Short) row[5]);
//					dto.setPlotIds((String) row[6]);
//					dto.setApprovalStatus((Integer) row[7]);
//					respones.add(dto);
//				}
//				List<Object[]> result2 = meetingScheduleRepo.getLevleIdThroughData(meetingMainId,2);
//				for (Object[] row : result2) {
//					MeetingProceedingsOneDTO dto = new MeetingProceedingsOneDTO();
//					dto.setMeetingId((BigInteger) row[0]);
//					dto.setPlotNo((String) row[1]);
//					dto.setLandAppId((BigInteger) row[2]);
//					dto.setLandAppName((String) row[3]);
//					dto.setLandAppNo((String) row[4]);
//					dto.setMettingLevleId((Short) row[5]);
//					dto.setPlotIds((String) row[6]);
//					dto.setApprovalStatus((Integer) row[7]);
//					respones1.add(dto);
//				}
//				List<Object[]> result3 = meetingScheduleRepo.getLevleIdThroughData(meetingMainId,3);
//				for (Object[] row : result3) {
//					MeetingProceedingsTwoDTO dto = new MeetingProceedingsTwoDTO();
//					dto.setMeetingId((BigInteger) row[0]);
//					dto.setPlotNo((String) row[1]);
//					dto.setLandAppId((BigInteger) row[2]);
//					dto.setLandAppName((String) row[3]);
//					dto.setLandAppNo((String) row[4]);
//					dto.setMettingLevleId((Short) row[5]);
//					dto.setPlotIds((String) row[6]);
//					dto.setApprovalStatus((Integer) row[7]);
//					respones2.add(dto);
//				}
//				
//				List<Object[]> result4 = meetingScheduleRepo.getLevleIdThroughData(meetingMainId,4);
//				for (Object[] row : result4) {
//					MeetingProceedingsThreeDTO dto = new MeetingProceedingsThreeDTO();
//					dto.setMeetingId((BigInteger) row[0]);
//					dto.setPlotNo((String) row[1]);
//					dto.setLandAppId((BigInteger) row[2]);
//					dto.setLandAppName((String) row[3]);
//					dto.setLandAppNo((String) row[4]);
//					dto.setMettingLevleId((Short) row[5]);
//					dto.setPlotIds((String) row[6]);
//					dto.setApprovalStatus((Integer) row[7]);
//					respones3.add(dto);
//				}

		}

		jsobj.put("respones", respones);
//		jsobj.put("respones1", respones1);
//		jsobj.put("respones2", respones2);
//		jsobj.put("respones3", respones3);
		return jsobj;
//		return new MeetingProceedingsMainDTO(respones,respones1,respones2,respones3);

	}

	@Override
	public Integer saveRecordForMeetingUplodeMom(String parms) throws JsonMappingException, JsonProcessingException {
		Integer saveRecordAction = 0;
		Integer savePlots = 0;
		ObjectMapper om = new ObjectMapper();
		MettingUplodeMomDTO mettingRecord = om.readValue(parms, MettingUplodeMomDTO.class);
		try {
			if (mettingRecord.getFilename() != null && (!mettingRecord.getFilename().equals(""))) {
				File f = new File(tempUploadPath + mettingRecord.getFilename());
				if (f.exists()) {
					String uploadPath = finalUploadPath;
					Path srcPath = Paths.get(tempUploadPath + mettingRecord.getFilename());
					Path destPath = Paths.get(uploadPath + "/" + "meeting/" + mettingRecord.getFilename());

					try {
						CommonUtil.createDirectories(destPath.getParent());
						CommonUtil.copyAndDeleteFile(srcPath, destPath);
					} catch (IOException e) {
						logger.error("Error occurred , while copying file :{}", e.getMessage());
					}

				}

			}
		} catch (Exception e) {
			logger.error("Inside updateFile method of MeetingScheduleServiceImpl, an error occurred: {}",
					e.getMessage());

			json.put(CommonConstant.STATUS_KEY, 400);
		}
		Integer saveFileRecord = null;
		if (mettingRecord.getDirectAuctionHideFlag() == 1) {
			saveFileRecord = meetingScheduleRepo.updateMeetingScheduleUploadMom(mettingRecord.getMeetingId(),
					mettingRecord.getFilename(), "", mettingRecord.getNoPlotsAvaliableForMeetingFlag());
			if (saveFileRecord != null) {
				// update the all plot flag what are come im meeting
				Integer updateGoFlag = meetingScheduleRepo.updateGoForAuctionFlag(mettingRecord.getMeetingId());
				log.info("update success" + updateGoFlag);
			}
		} else {
			List<MettingUplodeMomPlotsRecordDTO> saverecordForPlots = mettingRecord.getPlotsRecord();
			StringBuilder plotsString = new StringBuilder();
			if (!saverecordForPlots.isEmpty()) {
				for (MettingUplodeMomPlotsRecordDTO respones : saverecordForPlots) {
					plotsString.append(respones.getPlotsNo()).append(",");
				}
			}
			String str = new String(plotsString);
			saveFileRecord = meetingScheduleRepo.updateMeetingScheduleUploadMom(mettingRecord.getMeetingId(),
					mettingRecord.getFilename(), str, mettingRecord.getNoPlotsAvaliableForMeetingFlag());
			List<MettingUplodeMomAuctionApprovalDTO> saveApprovalList = mettingRecord.getLandActionRecord();
			if (saveFileRecord > 0 && !saveApprovalList.isEmpty()) {
				for (MettingUplodeMomAuctionApprovalDTO result : saveApprovalList) {
					meetingScheduleRepo.updateMeetingScheduleApplicantApprovalStatus(result.getLandId(),
							result.getMettingId(), result.getValue(), mettingRecord.getMettingLevleId(),
							mettingRecord.getAuctionFlag(), result.getPlotcode());
					saveRecordAction++;
				}
			}
			if (mettingRecord.getMettingLevleId() == 4) {
				BigInteger idmeeting = new BigInteger(mettingRecord.getMeetingId().toString());
				Object noOfPlots = meetingScheduleRepo.getPlotLevle(idmeeting);
				landGis.updateLandGis(noOfPlots, 3, 2);
				landGis.updateLandGis(noOfPlots, 4, 2);
			}
			if (saveRecordAction > 0 && !saverecordForPlots.isEmpty()) {
				for (MettingUplodeMomPlotsRecordDTO respones : saverecordForPlots) {
					String plotNo = "";
					String sqlQuery = "SELECT plot_no FROM land_bank.plot_information WHERE plot_code = :plotcode";
					Query query = entityManager.createNativeQuery(sqlQuery);
					query.setParameter("plotcode", respones.getPlotsNo());
					@SuppressWarnings("unchecked")
					List<Object> result = query.getResultList();
					if (!result.isEmpty()) {
						plotNo = (String) result.get(0);
					}
					if (!result.isEmpty()) {
						if (respones.getAuctionClickId() > 0) {
							meetingScheduleRepo.insertLandAllotmentForAuction(plotNo, respones.getCreatedBy(),
									mettingRecord.getMeetingId(), respones.getPlotsNo(),
									respones.getAuctionprocessFlag());
//							savePlots++;
						}
					}
				}

			}
			if (!saverecordForPlots.isEmpty() && mettingRecord.getMettingLevleId() == 1) {
				for (MettingUplodeMomPlotsRecordDTO respones : saverecordForPlots) {
					meetingScheduleRepo.getUpdatePlotForAuction(respones.getPlotsNo(), mettingRecord.getMeetingId());
				}

			}
		}
		
		// add the manual notification for meeting result
		List<BigInteger> landUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("4"));
		if(landUserList != null && landUserList.size() > 0) {
			for(BigInteger landUser:landUserList) {
				NotificationDTO notificationDtoForLand = new NotificationDTO();
				notificationDtoForLand.setNotification("Meeting result have been declared.");
				notificationDtoForLand.setUserId(landUser);
				notificationDtoForLand.setCreatedBy(landUser);
				notificationDtoForLand.setUserType("O");
				notificationDetailsServiceImpl.submitNotification(notificationDtoForLand);
			}
		}
		
		return saveFileRecord;

	}

	public static JSONArray getDistrictName(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		String query = " SELECT DISTINCT (select (select (select (select (select dm.district_name  "
				+ "from land_bank.district_master dm where dm.district_code=tm.district_code LIMIT 1)  "
				+ "from land_bank.tahasil_master tm  where tm.tahasil_code=vm.tahasil_code LIMIT 1)  "
				+ "from land_bank.village_master vm  where vm.village_code=ki.village_code LIMIT 1)  "
				+ "from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)   "
				+ "from land_bank.plot_information pii  where pii.plot_code=apd.plot_code limit 1) as district_name, "
				+ "(select (select (select (select (select dm.district_code  "
				+ "from land_bank.district_master dm where dm.district_code=tm.district_code LIMIT 1)  "
				+ "from land_bank.tahasil_master tm  where tm.tahasil_code=vm.tahasil_code LIMIT 1)  "
				+ "from land_bank.village_master vm  where vm.village_code=ki.village_code LIMIT 1)  "
				+ "from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)   "
				+ "from land_bank.plot_information pii  where pii.plot_code=apd.plot_code limit 1) as district_id "
//				+ "FROM application.land_allotement_for_auction lafa "
				+ "FROM application.auction_plot_details apd "
//				+ "ON lafa.plot_code = apd.plot_code "
				+ "JOIN application.auction_plot ap ON ap.auction_plot_id = apd.auction_plot_id "
				+ "JOIN application.tender_auction ta ON ta.auction_plot_id = apd.auction_plot_details_id "
				+ "JOIN application.live_auction la ON la.tender_auction_id = ta.tender_auction_id "
				+ "WHERE la.upload_winner_document IS NOT NULL AND apd.create_meeting_for_auction_flag=0 ";

		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("district_id", data[1]);
			jsonObj.put("district_name", data[0]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselTehsilNameListChange(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = jsonDepend.get("district_id").toString();
		String query = " SELECT DISTINCT (select (select (select (select (select tm.tahasil_name  "
				+ "from land_bank.district_master dm where dm.district_code=tm.district_code AND dm.district_code= '"
				+ val + "'" + "  LIMIT 1)  "
				+ "from land_bank.tahasil_master tm  where tm.tahasil_code=vm.tahasil_code LIMIT 1)  "
				+ "from land_bank.village_master vm  where vm.village_code=ki.village_code LIMIT 1)  "
				+ "from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)   "
				+ "from land_bank.plot_information pii  where pii.plot_code=apd.plot_code  limit 1) as tehsil_name, "
				+ "(select (select (select (select (select tm.tahasil_code  "
				+ "from land_bank.district_master dm where dm.district_code=tm.district_code AND dm.district_code=  '"
				+ val + "'" + "LIMIT 1)  "
				+ "from land_bank.tahasil_master tm  where tm.tahasil_code=vm.tahasil_code LIMIT 1)  "
				+ "from land_bank.village_master vm  where vm.village_code=ki.village_code LIMIT 1)  "
				+ "from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)   "
				+ "from land_bank.plot_information pii  where pii.plot_code=apd.plot_code limit 1) as tehsil_id "
//				+ "FROM application.land_allotement_for_auction lafa "
				+ "FROM application.auction_plot_details apd "
//				+ "ON lafa.plot_code = apd.plot_code "
				+ "JOIN application.auction_plot ap ON ap.auction_plot_id = apd.auction_plot_id "
				+ "JOIN application.tender_auction ta ON ta.auction_plot_id = ap.auction_plot_id "
				+ "JOIN application.live_auction la ON la.tender_auction_id = ta.tender_auction_id "
				+ "WHERE la.upload_winner_document IS NOT NULL AND apd.create_meeting_for_auction_flag=0 ";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("tehsil_id", data[1]);
			jsonObj.put("tehsil_name", data[0]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselplotsListChange(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String tahasilCode = jsonDepend.get("tehsil_id").toString();
		String nativeQuery = " SELECT DISTINCT apd.plot_code as plot_code, "
				+ "(select plot_no from land_bank.plot_information  where plot_code=apd.plot_code) as plot_no ,apd.auction_plot_details_id "
//				+ "FROM application.land_allotement_for_auction lafa "
				+ "FROM application.auction_plot_details apd "
//				+ "ON lafa.plot_code = apd.plot_code "
				+ "JOIN application.auction_plot ap ON ap.auction_plot_id = apd.auction_plot_id "
//                +" JOIN application.auction_plot_details apd  ON(apd.auction_plot_id=ap.auction_plot_id) "
				+ "JOIN application.tender_auction ta ON ta.auction_plot_id = apd.auction_plot_details_id "
				+ "JOIN application.live_auction la ON la.tender_auction_id = ta.tender_auction_id "
				+ "JOIN land_bank.plot_information pii ON(pii.plot_code=apd.plot_code) "
				+ "JOIN land_bank.khatian_information ki ON(ki.khatian_code=pii.khatian_code) "
				+ "JOIN land_bank.village_master vm   ON(vm.village_code=ki.village_code) "
				+ "JOIN land_bank.tahasil_master tm  ON(tm.tahasil_code=vm.tahasil_code) "
				+ "WHERE la.upload_winner_document IS NOT NULL AND tm.tahasil_code='" + tahasilCode
				+ "' AND apd.create_meeting_for_auction_flag=0  AND ta.plot_code=apd.plot_code " + " ORDER BY plot_no";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(nativeQuery).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("plot_code", data[0]);
			jsonObj.put("plot_no", data[1]);
			jsonObj.put("plot_details_id", data[2]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	@Override
	public List<MeetingPlotsRecordDTO> getAllplotsRecord(String pageData) {
		JSONObject mjs = new JSONObject(pageData);
		Integer pageNumber = mjs.getInt("pageNumber");
		Integer pageSize = mjs.getInt("pageSize");
		Integer offset = (pageNumber - 1) * pageSize;
		List<Object[]> res = meetingScheduleRepo.getPlotsRecord(pageSize, offset);
		List<MeetingPlotsRecordDTO> respones = new ArrayList<>();
		for (Object[] row : res) {
			MeetingPlotsRecordDTO dto = new MeetingPlotsRecordDTO();
			dto.setMeetingId((BigInteger) row[0]);
//			dto.setPlotCode((String) row[1]);
			dto.setMeetingUniqueNo((String) row[1]);
			dto.setMeetingSheduleId((BigInteger) row[2]);
//			dto.setPlotDetails((String) row[4]);
			ObjectMapper objectMapper = new ObjectMapper();
			List<Map<String, String>> applicantDataList = null;
			try {
				if (row[3] != null) {
					applicantDataList = objectMapper.readValue(row[3].toString(),
							new TypeReference<List<Map<String, String>>>() {
							});
				}
				dto.setPlotDetails(applicantDataList);
			} catch (IOException e) {
			}
			respones.add(dto);

		}
		return respones;
	}

	@Override
	public BigInteger getRowCountOfDistinctPlotMeetings() {
		return meetingScheduleRepo.getRowCountOfDistinctPlotMeetings();
	}

	@Override
	public JSONObject getHistory(BigInteger meetingId) {
		JSONObject response = new JSONObject();

		List<MeetingProceedingsOneDTO> dtos = new ArrayList<>();
		List<MeetingProceedingsOneDTO> dtos1 = new ArrayList<>();
		List<MeetingProceedingsOneDTO> dtos2 = new ArrayList<>();
		List<MeetingProceedingsOneDTO> dtos3 = new ArrayList<>();
		List<MeetingProceedingsOneDTO> dtos4 = new ArrayList<>();
//		List<MeetingProceedingsTwoDTO> plotRes = new ArrayList<>();

		try {

			List<Object[]> result = meetingScheduleRepo.getHistory(meetingId);
//			List<Object[]> plotsRecord = meetingScheduleRepo.getplotCode(meetingId);
			log.info("result: " + result);
			if (result != null && result.size() > 0) {
				dtos = mapToMeetingDto(result);
				for (MeetingProceedingsOneDTO dto : dtos) {
					if (dto.getMeetingLevelId() != null && dto.getMeetingLevelId() == 1) {
						dtos1.add(dto);
						response.put("level1", dtos1);
					} else if (dto.getMeetingLevelId() != null && dto.getMeetingLevelId() == 2) {
						dtos2.add(dto);
						response.put("level2", dtos2);
					} else if (dto.getMeetingLevelId() != null && dto.getMeetingLevelId() == 3) {
						dtos3.add(dto);
						response.put("level3", dtos3);
					} else if (dto.getMeetingLevelId() != null && dto.getMeetingLevelId() == 4) {
						dtos4.add(dto);
						response.put("level4", dtos4);
					}
				}
				response.put(CommonConstant.MESSAGE_KEY, "data fetched successfully");
				log.info("dtos: " + dtos);
				log.info("response for : ", response);
			} else {
				response.put(CommonConstant.MESSAGE_KEY, "No record found");
			}
//			if(plotsRecord != null && plotsRecord.size() > 0) {
//				plotRes = aptoPlotRecord(plotsRecord);
//				response.put("plotsRecord", plotRes);
//			}else {
//				response.put(CommonConstant.MESSAGE_KEY, "No record found plots");
//			}
			response.put(CommonConstant.STATUS_KEY, 200);

		} catch (Exception e) {
			log.error("error occured due to: " + e.getMessage());
			response.put(CommonConstant.STATUS_KEY, 500);
			response.put(CommonConstant.MESSAGE_KEY, "error occured while fetching data");
		}
		return response;

	}

	public List<MeetingProceedingsOneDTO> mapToMeetingDto(List<Object[]> result) {

		return result.stream().map(objects -> {
			MeetingProceedingsOneDTO dto = new MeetingProceedingsOneDTO();
			dto.setMeetingId((BigInteger) objects[0]);
			dto.setMeetingScheduleId((BigInteger) objects[1]);
			dto.setVenue(objects[2] != null ? (String) objects[2] : "");
			dto.setMeetingDate(objects[3] != null ? (Date) objects[3] : null);
			dto.setMeetingPurpose(objects[4] != null ? (String) objects[4].toString() : "");
			dto.setMeetingLevelId(objects[5] != null ? (Short) objects[5] : null);
			dto.setMeetingScheduleApplicantId(objects[6] != null ? (Integer) objects[6] : null);
			dto.setLandAppId(objects[7] != null ? (BigInteger) objects[7] : null);
			dto.setPlotCode(objects[8] != null ? (String) objects[8] : null);
			dto.setApprovalStatus(objects[9] != null ? (Integer) objects[9] : null);
			dto.setLandAppName(objects[10] != null ? (String) objects[10] : null);
			dto.setLandAppNo(objects[11] != null ? (String) objects[11] : null);
			dto.setPlotNo(objects[12] != null ? (String) objects[12] : null);
			dto.setMeetingAuctionStatus(objects[13] != null ? (Integer) objects[13] : null);
			dto.setDirectMeetingFlag(objects[14] != null ? (Short) objects[14] : null);

			return dto;
		}).collect(Collectors.toList());
	}

	public List<MeetingProceedingsTwoDTO> aptoPlotRecord(List<Object[]> result) {
		return result.stream().map(objects -> {
			MeetingProceedingsTwoDTO dto = new MeetingProceedingsTwoDTO();
			dto.setMeetingId((BigInteger) objects[1]);
			dto.setPlotNo((String) objects[0]);
			return dto;
		}).collect(Collectors.toList());
	}
}