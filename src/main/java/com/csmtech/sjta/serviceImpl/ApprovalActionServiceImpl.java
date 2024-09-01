package com.csmtech.sjta.serviceImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.ApprovalActionResultDTO;
import com.csmtech.sjta.dto.ApprovalActionUpdateDTO;
import com.csmtech.sjta.dto.ApprovalDocumentDTO;
import com.csmtech.sjta.dto.NotificationDTO;
import com.csmtech.sjta.entity.Land_applicant;
import com.csmtech.sjta.mobile.dto.ApplicationFlowDto;
import com.csmtech.sjta.mobile.repository.NotificationDetailsRepository;
import com.csmtech.sjta.mobile.service.CommonService;
import com.csmtech.sjta.mobile.service.NotificationDetailsServiceImpl;
import com.csmtech.sjta.repository.ApprovalActionRepository;
import com.csmtech.sjta.repository.LandApplicantDetailsApprovalStageRepository;
import com.csmtech.sjta.repository.LandApplicantJPARepository;
import com.csmtech.sjta.repository.LandAreaStatisticsRepository;
import com.csmtech.sjta.service.ApprovalActionService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.MailUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApprovalActionServiceImpl implements ApprovalActionService {

	@Autowired
	private MailUtil mailutil;

	@Autowired
	private ApprovalActionRepository approvalrepo;

	@Autowired
	private LandApplicantDetailsApprovalStageRepository repo;

	@Autowired
	LandAreaStatisticsRepository landGis;

	@Autowired
	CommonService commonService;
	
	@Autowired
	NotificationDetailsRepository notificationDetailsRepo;
	
	@Autowired
	LandApplicantJPARepository landApplicantJpaRepository;
	
	@Autowired
	NotificationDetailsServiceImpl notificationDetailsServiceImpl;

	@Override
	public List<ApprovalActionResultDTO> findApprovalActionsForRoleId(Long roleId) {
		log.info(":: findApprovalActionsForRoleId Are execute sucessfully..!!");
		return approvalrepo.findApprovalActionsForRoleId(roleId);
	}

	@SuppressWarnings("unused")
	@Override
	public Short updateApprovalProcess(ApprovalActionUpdateDTO approvalDto) {
		Short newApplicationStatusId = null;

		if (approvalDto.getNewApprovalActionId() == 1) {
			log.info(":: updateApprovalProcess Start execution-1 ..!!");

			// just use Forward To Next Authority
			Short result = approvalrepo.findApprovalLevelForRoleIdAndType(approvalDto.getRoleId(),
					CommonConstant.LAND_APPLICATION, approvalDto.getLandApplicantId());
			Integer resulofpendingat = null;
			if (result != null) {
				Short resultpending = result;
				Integer finalres = resultpending + 1;
				resulofpendingat = approvalrepo.findRoleIdByApprovalLevel(finalres);
				if (result == 1) {
					BigInteger landAppId = new BigInteger(approvalDto.getLandApplicantId().toString());
					Object plots = approvalrepo.getPlot(landAppId);
					Integer countInsert = landGis.updateLandGis(plots, 2, 1);
				}
			}

			newApplicationStatusId = approvalrepo.findApplicationStatusId(approvalDto.getNewApprovalActionId(),
					approvalDto.getRoleId());
			if (newApplicationStatusId == null) {
				return null;
			}

			Integer updateRecord = approvalrepo.updateLandApplicationApproval(approvalDto.getLandApplicantId(),
					resulofpendingat, newApplicationStatusId, approvalDto.getNewUpdatedBy(), true,
					approvalDto.getNewApprovalActionId(), approvalDto.getNewRemark(), result, approvalDto.getDocsIds());
			log.info(":: updateApprovalProcess end  execution-1 ..!!");

			if (approvalDto.getRoleId() == 3) {
				// added for inserting in db only(application flow) Application Forwarded by
				// Dealing Officer
				ApplicationFlowDto dto = new ApplicationFlowDto();
				dto.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId()));
				dto.setApplicationFlowId(BigInteger.valueOf(3));
				dto.setActionDateTime(new Date());
				dto.setActionRoleId(BigInteger.valueOf(approvalDto.getRoleId()));
				commonService.saveApplicationFlow(dto);
				
				//add the notification part
				
				//for manual notifications Evaluation of Land by Dealing Officer and Notify to Citizen and Land Officer
				List<BigInteger> landOfficerUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("4"));
				log.info("user list are: "+landOfficerUserList);
				Land_applicant entity =  landApplicantJpaRepository.findByIntId(approvalDto.getLandApplicantId());
				//for citizen
				NotificationDTO notificationDTO = new NotificationDTO();
				notificationDTO.setNotification("Land Application "+entity.getApplicantNo() +" is forwarded by Dealing Officer");
				notificationDTO.setUserId(new BigInteger(entity.getIntCreatedBy().toString()));
				notificationDTO.setCreatedBy(new BigInteger(entity.getIntCreatedBy().toString()));
				notificationDTO.setUserType("CI");
				notificationDetailsServiceImpl.submitNotification(notificationDTO);
				
				//for land officers
				if(landOfficerUserList != null && landOfficerUserList.size() >0) {
					for(BigInteger landOfficerUser:landOfficerUserList) {
						NotificationDTO notificationDto = new NotificationDTO();
						notificationDto.setNotification("Land Application "+entity.getApplicantNo() +" is forwarded by Dealing Officer");
						notificationDto.setUserId(landOfficerUser);
						notificationDto.setCreatedBy(landOfficerUser);
						notificationDto.setUserType("O");
						notificationDetailsServiceImpl.submitNotification(notificationDto);
					}
				}
				
//				notificationDTO.set
				/*
				 * NotificationDetails entity = new NotificationDetails();
				entity.setNotification(notificationDto.getNotification());
				entity.setReadMode("N");
				entity.setUserType(notificationDto.getUserType());
				entity.setCreatedOn(new Date());
				entity.setCreatedBy(notificationDto.getCreatedBy());
				entity.setUserId(notificationDto.getUserId());
				 */
			//	notificationDetailsServiceImpl.submitNotification(null);
				
			} else if (approvalDto.getRoleId() == 4) {
				// added for inserting in db only(application flow) Application Forwarded by
				// Land Officer
				ApplicationFlowDto dto = new ApplicationFlowDto();
				dto.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId()));
				dto.setApplicationFlowId(BigInteger.valueOf(7));
				dto.setActionDateTime(new Date());
				dto.setActionRoleId(BigInteger.valueOf(approvalDto.getRoleId()));
				commonService.saveApplicationFlow(dto);
				
				//add the notification part
				
				//for manual notifications Evaluation of Land by Land Officer and Notify to Citizen and Deputy Admin Officer
				List<BigInteger> deputyOfficerUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("5"));
				log.info("user list are: "+deputyOfficerUserList);
				Land_applicant entity =  landApplicantJpaRepository.findByIntId(approvalDto.getLandApplicantId());
				//for citizen
				NotificationDTO notificationDTO = new NotificationDTO();
				notificationDTO.setNotification("Land Application "+entity.getApplicantNo() +" is forwarded by Land Officer");
				notificationDTO.setUserId(new BigInteger(entity.getIntCreatedBy().toString()));
				notificationDTO.setCreatedBy(new BigInteger(entity.getIntCreatedBy().toString()));
				notificationDTO.setUserType("CI");
				notificationDetailsServiceImpl.submitNotification(notificationDTO);
				
				//for Deputy Admin officers
				if(deputyOfficerUserList != null && deputyOfficerUserList.size() > 0) {
					for(BigInteger deputyOfficerUser:deputyOfficerUserList) {
						NotificationDTO notificationDto = new NotificationDTO();
						notificationDto.setNotification("Land Application "+entity.getApplicantNo() +" is forwarded by Land Officer");
						notificationDto.setUserId(deputyOfficerUser);
						notificationDto.setCreatedBy(deputyOfficerUser);
						notificationDto.setUserType("O");
						notificationDetailsServiceImpl.submitNotification(notificationDto);
					}
				}
				
			}

		} else if (approvalDto.getNewApprovalActionId() == 2) {
			log.info(":: updateApprovalProcess Start execution-2 ..!!");

			// just use rejected
			Short result = approvalrepo.findApprovalLevelForRoleIdAndType(approvalDto.getRoleId(),
					CommonConstant.LAND_APPLICATION, approvalDto.getLandApplicantId());
			Integer resulofpendingat = null;
			Integer pendingAt = 0;
			Integer finalres = null;
			if (result != null) {
				Short resultpending = result;

				finalres = resultpending + 1;

			}
			if (resulofpendingat != null) {
				resulofpendingat = approvalrepo.findRoleIdByApprovalLevel(finalres);
			}
			newApplicationStatusId = approvalrepo.findApplicationStatusId(approvalDto.getNewApprovalActionId(),
					approvalDto.getRoleId());
			if (newApplicationStatusId == null) {
				return null;
			}

			Integer updateRecord = approvalrepo.updateLandApplicationApproval(approvalDto.getLandApplicantId(),
					pendingAt, newApplicationStatusId, approvalDto.getNewUpdatedBy(), true,
					approvalDto.getNewApprovalActionId(), approvalDto.getNewRemark(), result, approvalDto.getDocsIds());
			log.info(":: updateApprovalProcess end  execution-2 ..!!");
			if (approvalDto.getRoleId() == 3) {
				// added for inserting in db only(application flow) Application Rejected by
				// Dealing Officer
				ApplicationFlowDto dto = new ApplicationFlowDto();
				dto.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId()));
				dto.setApplicationFlowId(BigInteger.valueOf(4));
				dto.setActionDateTime(new Date());
				dto.setActionRoleId(BigInteger.valueOf(approvalDto.getRoleId()));
				commonService.saveApplicationFlow(dto);
				
				//add the notification part
				
				//for manual notifications Evaluation of Land by Dealing Officer and Notify to Citizen and Land Officer
				List<BigInteger> landOfficerUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("4"));
				log.info("user list are: "+landOfficerUserList);
				Land_applicant entity =  landApplicantJpaRepository.findByIntId(approvalDto.getLandApplicantId());
				//for citizen
				NotificationDTO notificationDTO = new NotificationDTO();
				notificationDTO.setNotification("Land Application "+entity.getApplicantNo() +" is rejected by Dealing Officer");
				notificationDTO.setUserId(new BigInteger(entity.getIntCreatedBy().toString()));
				notificationDTO.setCreatedBy(new BigInteger(entity.getIntCreatedBy().toString()));
				notificationDTO.setUserType("CI");
				notificationDetailsServiceImpl.submitNotification(notificationDTO);
				
				//for land officers
				if(landOfficerUserList != null && landOfficerUserList.size() > 0) {
					for(BigInteger landOfficerUser:landOfficerUserList) {
						NotificationDTO notificationDto = new NotificationDTO();
						notificationDto.setNotification("Land Application "+entity.getApplicantNo() +" is rejected by Dealing Officer");
						notificationDto.setUserId(landOfficerUser);
						notificationDto.setCreatedBy(landOfficerUser);
						notificationDto.setUserType("O");
						notificationDetailsServiceImpl.submitNotification(notificationDto);
					}
				}
				
				
			} else if (approvalDto.getRoleId() == 4) {
				// added for inserting in db only(application flow) Application Rejected by Land
				// Officer
				ApplicationFlowDto dto = new ApplicationFlowDto();
				dto.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId()));
				dto.setApplicationFlowId(BigInteger.valueOf(8));
				dto.setActionDateTime(new Date());
				dto.setActionRoleId(BigInteger.valueOf(approvalDto.getRoleId()));
				commonService.saveApplicationFlow(dto);
				
				//add the notification part
				
				//for manual notifications Evaluation of Land by Land Officer and Notify to Citizen and Deputy Admin Officer
				List<BigInteger> deputyOfficerUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("5"));
				log.info("user list are: "+deputyOfficerUserList);
				Land_applicant entity =  landApplicantJpaRepository.findByIntId(approvalDto.getLandApplicantId());
				//for citizen
				NotificationDTO notificationDTO = new NotificationDTO();
				notificationDTO.setNotification("Land Application "+entity.getApplicantNo() +" is rejected by Land Officer");
				notificationDTO.setUserId(new BigInteger(entity.getIntCreatedBy().toString()));
				notificationDTO.setCreatedBy(new BigInteger(entity.getIntCreatedBy().toString()));
				notificationDTO.setUserType("CI");
				notificationDetailsServiceImpl.submitNotification(notificationDTO);
				
				//for Deputy Admin officers
				if(deputyOfficerUserList != null && deputyOfficerUserList.size() > 0) {
					for(BigInteger deputyOfficerUser:deputyOfficerUserList) {
						NotificationDTO notificationDto = new NotificationDTO();
						notificationDto.setNotification("Land Application "+entity.getApplicantNo() +" is rejected by Land Officer");
						notificationDto.setUserId(deputyOfficerUser);
						notificationDto.setCreatedBy(deputyOfficerUser);
						notificationDto.setUserType("O");
						notificationDetailsServiceImpl.submitNotification(notificationDto);
					}
				}
				
				
			} else if (approvalDto.getRoleId() == 5) {
				// added for inserting in db only(application flow) Application Rejected by
				// Deputy Admin
				ApplicationFlowDto dto = new ApplicationFlowDto();
				dto.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId()));
				dto.setApplicationFlowId(BigInteger.valueOf(11));
				dto.setActionDateTime(new Date());
				dto.setActionRoleId(BigInteger.valueOf(approvalDto.getRoleId()));
				commonService.saveApplicationFlow(dto);
				
				
				//add the notification part
				
				//for manual notifications Evaluation of Land by Deputy Admin and Notify to Citizen and Land Officer and Dealing Officer
				List<BigInteger> dealingUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("3"));
				List<BigInteger> landUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("4"));
			//	log.info("user list are: "+deputyOfficerUserList);
				Land_applicant entity =  landApplicantJpaRepository.findByIntId(approvalDto.getLandApplicantId());
				//for citizen
				NotificationDTO notificationDTO = new NotificationDTO();
				notificationDTO.setNotification("Land Application "+entity.getApplicantNo() +" is rejected by Deputy Admin");
				notificationDTO.setUserId(new BigInteger(entity.getIntCreatedBy().toString()));
				notificationDTO.setCreatedBy(new BigInteger(entity.getIntCreatedBy().toString()));
				notificationDTO.setUserType("CI");
				notificationDetailsServiceImpl.submitNotification(notificationDTO);
				
				//for Dealing officers
				if(dealingUserList != null && dealingUserList.size() > 0) {
					for(BigInteger dealingUser:dealingUserList) {
						NotificationDTO notificationDto = new NotificationDTO();
						notificationDto.setNotification("Land Application "+entity.getApplicantNo() +" is rejected by Deputy Admin");
						notificationDto.setUserId(dealingUser);
						notificationDto.setCreatedBy(dealingUser);
						notificationDto.setUserType("O");
						notificationDetailsServiceImpl.submitNotification(notificationDto);
					}
				}
				
				if(landUserList != null && landUserList.size() > 0) {
					for(BigInteger landUser:landUserList) {
						NotificationDTO notificationDto = new NotificationDTO();
						notificationDto.setNotification("Land Application "+entity.getApplicantNo() +" is rejected by Deputy Admin");
						notificationDto.setUserId(landUser);
						notificationDto.setCreatedBy(landUser);
						notificationDto.setUserType("O");
						notificationDetailsServiceImpl.submitNotification(notificationDto);
					}
				}
				
			}

		} else if (approvalDto.getNewApprovalActionId() == 3) {
			log.info(":: updateApprovalProcess Start execution-3 ..!!");

			// just use Forward To Revert To Citizen
			Short result = approvalrepo.findApprovalLevelForRoleIdAndType(approvalDto.getRoleId(),
					CommonConstant.LAND_APPLICATION, approvalDto.getLandApplicantId());
			Integer pendingAt = 2;
			if (result != null) {
				Short resultpending = result;
				Integer finalres = resultpending + 1;
			}
			newApplicationStatusId = approvalrepo.findApplicationStatusId(approvalDto.getNewApprovalActionId(),
					approvalDto.getRoleId());
			if (newApplicationStatusId == null) {
				return null;
			}

			Integer updateRecord = approvalrepo.updateLandApplicationApproval(approvalDto.getLandApplicantId(),
					pendingAt, newApplicationStatusId, approvalDto.getNewUpdatedBy(), true,
					approvalDto.getNewApprovalActionId(), approvalDto.getNewRemark(), result, approvalDto.getDocsIds());

			log.info(":: updateApprovalProcess end  execution-3 ..!!");
			if (approvalDto.getRoleId() == 3) {
				log.info("inside revert by DO");
				// added for inserting in db only(application flow) Application Reverted to
				// Applicant by Dealing Officer
				ApplicationFlowDto dto = new ApplicationFlowDto();
				dto.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId()));
				dto.setApplicationFlowId(BigInteger.valueOf(5));
				dto.setActionDateTime(new Date());
				dto.setActionRoleId(BigInteger.valueOf(approvalDto.getRoleId()));
				log.info("after");
				commonService.saveApplicationFlow(dto);
				
				
				//add the notification part
				
				//for manual notifications Evaluation of Land by Dealing Officer and Notify to Citizen and Land Officer
				List<BigInteger> landOfficerUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("4"));
				log.info("user list are: "+landOfficerUserList);
				Land_applicant entity =  landApplicantJpaRepository.findByIntId(approvalDto.getLandApplicantId());
				//for citizen
				NotificationDTO notificationDTO = new NotificationDTO();
				notificationDTO.setNotification("Land Application "+entity.getApplicantNo() +" is reverted to citizen by Dealing Officer");
				notificationDTO.setUserId(new BigInteger(entity.getIntCreatedBy().toString()));
				notificationDTO.setCreatedBy(new BigInteger(entity.getIntCreatedBy().toString()));
				notificationDTO.setUserType("CI");
				notificationDetailsServiceImpl.submitNotification(notificationDTO);
				
				//for land officers
				if(landOfficerUserList != null && landOfficerUserList.size() > 0) {
					for(BigInteger landOfficerUser:landOfficerUserList) {
						NotificationDTO notificationDto = new NotificationDTO();
						notificationDto.setNotification("Land Application "+entity.getApplicantNo() +" is reverted to citizen by Dealing Officer");
						notificationDto.setUserId(landOfficerUser);
						notificationDto.setCreatedBy(landOfficerUser);
						notificationDto.setUserType("O");
						notificationDetailsServiceImpl.submitNotification(notificationDto);
					}
				}
				
				
			} else if (approvalDto.getRoleId() == 4) {
				// added for inserting in db only(application flow) Application Reverted to
				// Applicant by Land Officer
				ApplicationFlowDto dto = new ApplicationFlowDto();
				dto.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId()));
				dto.setApplicationFlowId(BigInteger.valueOf(9));
				dto.setActionDateTime(new Date());
				dto.setActionRoleId(BigInteger.valueOf(approvalDto.getRoleId()));
				commonService.saveApplicationFlow(dto);
				
				//add the notification part
				
				//for manual notifications Evaluation of Land by Land Officer and Notify to Citizen and Deputy Admin Officer
				List<BigInteger> deputyOfficerUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("5"));
				log.info("user list are: "+deputyOfficerUserList);
				Land_applicant entity =  landApplicantJpaRepository.findByIntId(approvalDto.getLandApplicantId());
				//for citizen
				NotificationDTO notificationDTO = new NotificationDTO();
				notificationDTO.setNotification("Land Application "+entity.getApplicantNo() +" is reverted by Land Officer");
				notificationDTO.setUserId(new BigInteger(entity.getIntCreatedBy().toString()));
				notificationDTO.setCreatedBy(new BigInteger(entity.getIntCreatedBy().toString()));
				notificationDTO.setUserType("CI");
				notificationDetailsServiceImpl.submitNotification(notificationDTO);
				
				//for Deputy Admin officers
				if(deputyOfficerUserList != null && deputyOfficerUserList.size() > 0) {
					for(BigInteger deputyOfficerUser:deputyOfficerUserList) {
						NotificationDTO notificationDto = new NotificationDTO();
						notificationDto.setNotification("Land Application "+entity.getApplicantNo() +" is reverted by Land Officer");
						notificationDto.setUserId(deputyOfficerUser);
						notificationDto.setCreatedBy(deputyOfficerUser);
						notificationDto.setUserType("O");
						notificationDetailsServiceImpl.submitNotification(notificationDto);
					}
				}
				
				
			} else if (approvalDto.getRoleId() == 5) {
				// added for inserting in db only(application flow) Application Reverted to
				// Applicant by Deputy Admin
				ApplicationFlowDto dto = new ApplicationFlowDto();
				dto.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId()));
				dto.setApplicationFlowId(BigInteger.valueOf(12));
				dto.setActionDateTime(new Date());
				dto.setActionRoleId(BigInteger.valueOf(approvalDto.getRoleId()));
				commonService.saveApplicationFlow(dto);
				
				
				//add the notification part
				
				//for manual notifications Evaluation of Land by Deputy Admin and Notify to Citizen and Land Officer and Dealing Officer
				List<BigInteger> dealingUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("3"));
				List<BigInteger> landUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("4"));
			//	log.info("user list are: "+deputyOfficerUserList);
				Land_applicant entity =  landApplicantJpaRepository.findByIntId(approvalDto.getLandApplicantId());
				//for citizen
				NotificationDTO notificationDTO = new NotificationDTO();
				notificationDTO.setNotification("Land Application "+entity.getApplicantNo() +" is reverted to citizen by Deputy Admin");
				notificationDTO.setUserId(new BigInteger(entity.getIntCreatedBy().toString()));
				notificationDTO.setCreatedBy(new BigInteger(entity.getIntCreatedBy().toString()));
				notificationDTO.setUserType("CI");
				notificationDetailsServiceImpl.submitNotification(notificationDTO);
				
				//for Dealing officers
				if(dealingUserList != null && dealingUserList.size() > 0) {
					for(BigInteger dealingUser:dealingUserList) {
						NotificationDTO notificationDto = new NotificationDTO();
						notificationDto.setNotification("Land Application "+entity.getApplicantNo() +" is reverted to citizen by Deputy Admin");
						notificationDto.setUserId(dealingUser);
						notificationDto.setCreatedBy(dealingUser);
						notificationDto.setUserType("O");
						notificationDetailsServiceImpl.submitNotification(notificationDto);
					}
				}
				
				if(landUserList != null && landUserList.size() > 0) {
					for(BigInteger landUser:landUserList) {
						NotificationDTO notificationDto = new NotificationDTO();
						notificationDto.setNotification("Land Application "+entity.getApplicantNo() +" is reverted to citizen by Deputy Admin");
						notificationDto.setUserId(landUser);
						notificationDto.setCreatedBy(landUser);
						notificationDto.setUserType("O");
						notificationDetailsServiceImpl.submitNotification(notificationDto);
					}
				}
				
			}

		} else if (approvalDto.getNewApprovalActionId() == 4) {
			log.info(":: updateApprovalProcess Start execution-4 ..!!");

			return null;
		} else if (approvalDto.getNewApprovalActionId() == 5) {
			log.info(":: updateApprovalProcess Start execution-5 ..!!");

			// just use Forward To Revert To Citizen
			Short result = approvalrepo.findApprovalLevelForRoleIdAndType(approvalDto.getRoleId(),
					CommonConstant.LAND_APPLICATION, approvalDto.getLandApplicantId());
			Integer resulofpendingat = null;
			Short approvalLevleMin = (short) (result - 1);
			if (result != null) {
				Short resultpending = result;
				Integer finalres = resultpending - 1;
				resulofpendingat = approvalrepo.findRoleIdByApprovalLevel(finalres);
			}
			newApplicationStatusId = approvalrepo.findApplicationStatusId(approvalDto.getNewApprovalActionId(),
					approvalDto.getRoleId());
			if (newApplicationStatusId == null && approvalLevleMin == null) {
				return null;
			}
			if (resulofpendingat == null && approvalLevleMin == null) {
				resulofpendingat = 1;
				approvalLevleMin = 1;
			}

			Integer updateRecord = approvalrepo.updateLandApplicationApproval(approvalDto.getLandApplicantId(),
					resulofpendingat, newApplicationStatusId, approvalDto.getNewUpdatedBy(), true,
					approvalDto.getNewApprovalActionId(), approvalDto.getNewRemark(), approvalLevleMin,
					approvalDto.getDocsIds());
			log.info(":: updateApprovalProcess end  execution-5 ..!!");
			/*
			 * if(approvalDto.getRoleId() == 3) { log.info("inside revert by DO"); //added
			 * for inserting in db only(application flow) Application Reverted to Applicant
			 * by Dealing Officer ApplicationFlowDto dto = new ApplicationFlowDto();
			 * dto.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId())
			 * ); dto.setApplicationFlowId(BigInteger.valueOf(5));
			 * dto.setActionDateTime(null);
			 * dto.setActionRoleId(BigInteger.valueOf(approvalDto.getRoleId()));
			 * log.info("after"); commonService.saveApplicationFlow(dto); }else
			 * if(approvalDto.getRoleId() == 4) { //added for inserting in db
			 * only(application flow) Application Reverted to Applicant by Land Officer
			 * ApplicationFlowDto dto = new ApplicationFlowDto();
			 * dto.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId())
			 * ); dto.setApplicationFlowId(BigInteger.valueOf(9));
			 * dto.setActionDateTime(null);
			 * dto.setActionRoleId(BigInteger.valueOf(approvalDto.getRoleId()));
			 * commonService.saveApplicationFlow(dto); }else if(approvalDto.getRoleId() ==
			 * 5) { //added for inserting in db only(application flow) Application Reverted
			 * to Applicant by Deputy Admin ApplicationFlowDto dto = new
			 * ApplicationFlowDto();
			 * dto.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId())
			 * ); dto.setApplicationFlowId(BigInteger.valueOf(12));
			 * dto.setActionDateTime(null);
			 * dto.setActionRoleId(BigInteger.valueOf(approvalDto.getRoleId()));
			 * commonService.saveApplicationFlow(dto);
			 * 
			 * }
			 */

		} else if (approvalDto.getNewApprovalActionId() == 6) {
			log.info(":: updateApprovalProcess Start execution-6 ..!!");

			Short result = approvalrepo.findApprovalLevelForRoleIdAndType(approvalDto.getRoleId(),
					CommonConstant.LAND_APPLICATION, approvalDto.getLandApplicantId());
			Integer resulofpendingat = 0;
			if (result != null) {
				Short resultpending = result;
				Integer finalres = resultpending + 1;
				BigInteger landAppId = new BigInteger(approvalDto.getLandApplicantId().toString());
				Object plots = approvalrepo.getPlot(landAppId);
				Integer countInsert = landGis.updateLandGis(plots, 2, 2);

			}
			newApplicationStatusId = approvalrepo.findApplicationStatusId(approvalDto.getNewApprovalActionId(),
					approvalDto.getRoleId());
			if (newApplicationStatusId == null) {
				return null;
			}

			Integer updateRecord = approvalrepo.updateLandApplicationApproval(approvalDto.getLandApplicantId(),
					resulofpendingat, newApplicationStatusId, approvalDto.getNewUpdatedBy(), true,
					approvalDto.getNewApprovalActionId(), approvalDto.getNewRemark(), result, approvalDto.getDocsIds());
			log.info(":: updateApprovalProcess end  execution- ..!!");
			// added for inserting in db only(application flow) Application Approved by
			// Deputy Admin
			ApplicationFlowDto dto = new ApplicationFlowDto();
			dto.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId()));
			dto.setApplicationFlowId(BigInteger.valueOf(10));
			dto.setActionDateTime(new Date());
			dto.setActionRoleId(BigInteger.valueOf(approvalDto.getRoleId()));
			commonService.saveApplicationFlow(dto);
			
			//add the notification part
			
			//for manual notifications Evaluation of Land by Deputy Admin and Notify to Citizen and Land Officer and Dealing Officer
			List<BigInteger> dealingUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("3"));
			List<BigInteger> landUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("4"));
		//	log.info("user list are: "+deputyOfficerUserList);
			Land_applicant entity =  landApplicantJpaRepository.findByIntId(approvalDto.getLandApplicantId());
			//for citizen
			NotificationDTO notificationDTO = new NotificationDTO();
			notificationDTO.setNotification("Land Application "+entity.getApplicantNo() +" is approved by Deputy Admin");
			notificationDTO.setUserId(new BigInteger(entity.getIntCreatedBy().toString()));
			notificationDTO.setCreatedBy(new BigInteger(entity.getIntCreatedBy().toString()));
			notificationDTO.setUserType("CI");
			notificationDetailsServiceImpl.submitNotification(notificationDTO);
			
			//for Dealing officers
			if(dealingUserList != null && dealingUserList.size() > 0) {
				for(BigInteger dealingUser:dealingUserList) {
					NotificationDTO notificationDto = new NotificationDTO();
					notificationDto.setNotification("Land Application "+entity.getApplicantNo() +" is approved by Deputy Admin");
					notificationDto.setUserId(dealingUser);
					notificationDto.setCreatedBy(dealingUser);
					notificationDto.setUserType("O");
					notificationDetailsServiceImpl.submitNotification(notificationDto);
				}
			}
			
			if(landUserList != null && landUserList.size() > 0) {
				for(BigInteger landUser:landUserList) {
					NotificationDTO notificationDto = new NotificationDTO();
					notificationDto.setNotification("Land Application "+entity.getApplicantNo() +" is approved by Deputy Admin");
					notificationDto.setUserId(landUser);
					notificationDto.setCreatedBy(landUser);
					notificationDto.setUserType("O");
					notificationDetailsServiceImpl.submitNotification(notificationDto);
				}
			}

		}

		List<Object[]> result = approvalrepo.findMailandApplicant(approvalDto.getLandApplicantId());

		if (result.get(0)[0].equals("")) {
			log.info("Mail is not available.");
		} else {
			JSONObject mailData = new JSONObject();
			mailData.put("fullName", result.get(0)[1]);

			List<String> recipientEmails = new ArrayList<>();
			recipientEmails.add((String) result.get(0)[0]);

			String subject = "Land Application Status || SJTA";

			mailutil.sendEmail(2, newApplicationStatusId, mailData, subject, recipientEmails, null, null);
			log.info("Mail Send Sucess " + result.get(0)[0]);
		}

		return newApplicationStatusId;
	}

	// get the user mssage result
	public String messageData(Integer approvalActionId) {
		if (approvalActionId == 1) {
			return " Forwarded to Next Authority Successfully ";
		} else if (approvalActionId == 2) {
			return " Application Rejected Sucessfully";
		} else if (approvalActionId == 3) {
			return "Application Reverted To Citizen Successfully";
		} else if (approvalActionId == 4) {
			return "Application Query to Citizen Sucessfully";
		} else if (approvalActionId == 5) {
			return "Application Revert To Previous Authority Sucessfully";
		} else if (approvalActionId == 6) {
			return "Application Approved Sucessfully";
		} else if (approvalActionId == 7) {
			return " Application forwarded to Land Officer Sucessfully";
		} else if (approvalActionId == 8) {
			return " Application forwarded to Deputy Administrator Sucessfully";
		} else {
			return "";
		}
	}

	@Override
	public List<ApprovalDocumentDTO> findApprovaldocumentForRoleId(Long roleId) {
		log.info(":: findApprovalActionsForDocumentCheckListRoleId Are execute sucessfully..!!");
		return approvalrepo.findApprovaldocumentForRoleId(roleId);
	}

}
