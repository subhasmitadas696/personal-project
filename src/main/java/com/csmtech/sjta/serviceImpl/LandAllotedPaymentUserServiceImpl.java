package com.csmtech.sjta.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.LandAllotedPaymentRecordListDTO;
import com.csmtech.sjta.dto.LandAllotementPaymantRecordDTO;
import com.csmtech.sjta.dto.LandAllotementResponesDTO;
import com.csmtech.sjta.dto.NotificationDTO;
import com.csmtech.sjta.entity.Bidderregistrara;
import com.csmtech.sjta.entity.LandAllotementEntity;
import com.csmtech.sjta.entity.Land_applicant;
import com.csmtech.sjta.mobile.dto.ApplicationFlowDto;
import com.csmtech.sjta.mobile.repository.NotificationDetailsRepository;
import com.csmtech.sjta.mobile.service.CommonService;
import com.csmtech.sjta.mobile.service.NotificationDetailsServiceImpl;
import com.csmtech.sjta.repository.LandAllortmentRepository;
import com.csmtech.sjta.repository.LandAllotedPaymentUserRepository;
import com.csmtech.sjta.repository.LandApplicantJPARepository;
import com.csmtech.sjta.repository.LandAreaStatisticsRepository;
import com.csmtech.sjta.service.LandAllotedPaymentUserService;
import com.csmtech.sjta.util.TransactionNumberGenerateHelper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LandAllotedPaymentUserServiceImpl implements LandAllotedPaymentUserService {

	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${file.path}")
	private String finalUploadPath;
	
	
	@Autowired
	LandAllortmentRepository landAllortmentRepository;
	
	@Autowired
	private LandAllotedPaymentUserRepository repository;

	@Autowired
	private CommonService commonService;

	@Autowired
	LandApplicantJPARepository landApplicantJpaRepository;

	@Autowired
	NotificationDetailsServiceImpl notificationDetailsServiceImpl;

	@Autowired
	NotificationDetailsRepository notificationDetailsRepo;

	@Autowired
	LandAreaStatisticsRepository landGis;

	@Override
	public List<LandAllotementPaymantRecordDTO> getLandAllotementDetails(String parms) {
		JSONObject jsondata = new JSONObject(parms);
		log.info("getLandAllotementDetails servce execute and return the result :: !!!!");
		return repository.getLandAllotementDetails(jsondata.getBigInteger("landAlloteId"));
	}

	@Override
	public Integer tranctionCount(String orderId, String paymentSignature, String paymentId, BigDecimal tranctionAmount,
			BigInteger userId, BigInteger landAllotedId) {
		log.info("inside the tranctionCount return success");
		Integer result =  repository.insertPaymentTransaction(orderId, paymentSignature, paymentId, tranctionAmount, userId,
				landAllotedId,TransactionNumberGenerateHelper.createTransactionNumberForPostAllotement(landAllotedId));
		
			//for manual notification
				//for citizen
				NotificationDTO citizenNotificationDto  = new NotificationDTO();
				LandAllotementEntity allotmentEntity = landAllortmentRepository.findBylandAllotmentId(landAllotedId);
		//		Land_applicant entity =  landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
			//	LandPostAllotmentEntity landAllotEntity = landAllortmentRepository.findBy;
				//Bidderregistrara entity = bidderregistraraRepository.findByIntId(js.getBigInteger("intId"));
				citizenNotificationDto.setNotification("Payment transaction has been done successfully");
		//		citizenNotificationDto.setUserId(new BigInteger(entity.getIntCreatedBy().toString()));
		//		citizenNotificationDto.setCreatedBy(new BigInteger(entity.getIntCreatedBy().toString()));
				citizenNotificationDto.setUserType("CI");
				notificationDetailsServiceImpl.submitNotification(citizenNotificationDto);
				
				//for land officers
				List<BigInteger> landUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("4"));
				if(landUserList != null && landUserList.size() > 0) {
					for(BigInteger landUser: landUserList) {
						NotificationDTO notificationDtoForLand = new NotificationDTO();
			//			notificationDtoForLand.setNotification("Land has been allocated for "+entity.getApplicantNo());
						notificationDtoForLand.setUserId(landUser);
						notificationDtoForLand.setCreatedBy(landUser);
						notificationDtoForLand.setUserType("O");
						notificationDetailsServiceImpl.submitNotification(notificationDtoForLand);
					}
				}
		
		return result;
	}

	@Override
	public List<LandAllotedPaymentRecordListDTO> getPayments(String parms) {
		JSONObject jsondata = new JSONObject(parms);
		log.info("inside the getPayments return success");
		return repository.getPayments(jsondata.getBigInteger("landAlloteId"));
	}

	@Override
	public List<LandAllotementResponesDTO> getLandAllotementDetailsOffcer(String parms) {
		JSONObject jsonData = new JSONObject(parms);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		log.info("inside the getLandAllotementDetailsOffcer return success");
		return repository.getLandAllotementDetails(pageSize, offset);
	}

	@Override
	public BigInteger countLandAllortUser() {
		log.info("inside the countLandAllortUser return success");
		return repository.countLandAllortUser();
	}

	@Override
	public Integer updateWinnerDocument(String data) {
		log.info("inside the updateWinnerDocument start execution ..!!");
		JSONObject js = new JSONObject(data);
		String documentName = js.getString("docsName");
		BigInteger allotementId = js.getBigInteger("landAllotementId");
		if (!documentName.equals("")) {
			File f = new File(tempUploadPath + documentName);
			if (f.exists()) {

				File folder = new File(finalUploadPath + "/");
				if (!folder.exists()) {
					folder.mkdirs();
				}
				File src = new File(tempUploadPath + documentName);
				File dest = new File(folder + "/" + documentName);
				try {
					Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
					Files.delete(src.toPath());
				} catch (IOException e) {
					log.error("error occured while displaying update winner: " + e.getMessage());
				}
			}
		}
		log.info("inside the updateWinnerDocument end execution ..!!");
		//// add the application flow lines of codes for sale deed submission
		BigInteger landApplicationId = js.getBigInteger("landApplicantId");
		BigInteger formMId = js.getBigInteger("formMId");

		if (landApplicationId.intValue() > 0 || formMId.intValue() > 0) {

		}

		Integer result = repository.updateLandAllotementFormRegisterDocsAndFlag(allotementId, documentName);

		ApplicationFlowDto dto = new ApplicationFlowDto();

		dto.setLandApplicationId(landApplicationId != null ? landApplicationId : BigInteger.ZERO);
		// dto.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId()));
		dto.setApplicationFlowId(BigInteger.valueOf(21));
		dto.setActionDateTime(new Date());
		dto.setActionRoleId(BigInteger.valueOf(4));
		commonService.saveApplicationFlow(dto);
		//// add the application flow lines of codes for plot allocated
		ApplicationFlowDto dto1 = new ApplicationFlowDto();
		log.info("landApplicationId  : " + landApplicationId);

		dto1.setLandApplicationId(landApplicationId != null ? landApplicationId : BigInteger.ZERO);
		// dto1.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId()));
		dto1.setApplicationFlowId(BigInteger.valueOf(22));
		dto1.setActionDateTime(new Date());
		dto1.setActionRoleId(BigInteger.valueOf(4));
		commonService.saveApplicationFlow(dto1);

		landGis.updateLandGisSingle(js.getString("plotCode"), 5, 2);

		// for manual notification
		// for citizen
		NotificationDTO citizenNotificationDto = new NotificationDTO();
		Land_applicant entity = landApplicantJpaRepository.findByIntId(landApplicationId.intValue());
		// LandPostAllotmentEntity landAllotEntity = ;
		// Bidderregistrara entity =
		// bidderregistraraRepository.findByIntId(js.getBigInteger("intId"));
		citizenNotificationDto.setNotification("Land has been allocated");
		citizenNotificationDto.setUserId(new BigInteger(entity.getIntCreatedBy().toString()));
		citizenNotificationDto.setCreatedBy(new BigInteger(entity.getIntCreatedBy().toString()));
		citizenNotificationDto.setUserType("CI");
		notificationDetailsServiceImpl.submitNotification(citizenNotificationDto);

		// for land officers
		List<BigInteger> landUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("4"));
		if (landUserList != null && landUserList.size() > 0) {
			for (BigInteger landUser : landUserList) {
				NotificationDTO notificationDtoForLand = new NotificationDTO();
				notificationDtoForLand.setNotification("Land has been allocated for " + entity.getApplicantNo());
				notificationDtoForLand.setUserId(landUser);
				notificationDtoForLand.setCreatedBy(landUser);
				notificationDtoForLand.setUserType("O");
				notificationDetailsServiceImpl.submitNotification(notificationDtoForLand);
			}
		}

		return result;

//		return repository.updateLandAllotementFormRegisterDocsAndFlag(allotementId, documentName);
	}

}
