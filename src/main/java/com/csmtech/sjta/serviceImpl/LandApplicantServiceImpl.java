package com.csmtech.sjta.serviceImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.ApplicantNumberAndMobileDTO;
import com.csmtech.sjta.dto.LandAppHistoryDTO;
import com.csmtech.sjta.dto.LandAppResponseStructureDTO;
import com.csmtech.sjta.dto.LandApplicantDTO;
import com.csmtech.sjta.dto.LandPaginationDTO;
import com.csmtech.sjta.dto.NotificationDTO;
import com.csmtech.sjta.entity.Land_applicant;
import com.csmtech.sjta.mobile.dto.ApplicationFlowDto;
import com.csmtech.sjta.mobile.entity.NotificationDetails;
import com.csmtech.sjta.mobile.repository.NotificationDetailsRepository;
import com.csmtech.sjta.mobile.service.CommonService;
import com.csmtech.sjta.mobile.service.NotificationDetailsServiceImpl;
import com.csmtech.sjta.repository.LandApplicantDetailsApprovalStageRepository;
import com.csmtech.sjta.repository.LandApplicantJPARepository;
import com.csmtech.sjta.repository.LandApplicantNativeRepository;
import com.csmtech.sjta.repository.LandApplicantRepo;
import com.csmtech.sjta.repository.LandApplicantRepository;
import com.csmtech.sjta.repository.LandApplicantViewAllDetailsClassRepository;
import com.csmtech.sjta.repository.LandApprovalRepository;
import com.csmtech.sjta.repository.LandAreaStatisticsRepository;
import com.csmtech.sjta.repository.LandDocumentsRepository;
import com.csmtech.sjta.repository.LandPlotDetailsRepository;
import com.csmtech.sjta.repository.LandPlotRepository;
import com.csmtech.sjta.service.LandApplicantService;
import com.csmtech.sjta.util.CommonApplicationNumberGenerator;
import com.csmtech.sjta.util.CommonUtil;
import com.csmtech.sjta.util.TransactionNumberGenerateHelper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LandApplicantServiceImpl implements LandApplicantService {

	/**
	 * @author guru.prasad
	 */

	private static final Logger logger = LoggerFactory.getLogger(LandApplicantServiceImpl.class);

	@Autowired
	private LandApplicantRepository landApplicantRepository;

	@Autowired
	private LandApplicantRepo landApplicantRepo;

	@Autowired
	private LandApplicantNativeRepository landNativeRepo;

	@Autowired
	private LandDocumentsRepository documentsRepository;

	@Autowired
	private LandPlotRepository plotRepository;

	@Autowired
	private LandPlotDetailsRepository nativerepo;

	@Autowired
	private LandApplicantViewAllDetailsClassRepository repo;

	@Autowired
	private LandApplicantDetailsApprovalStageRepository landapprepo;

	@Autowired
	private EntityManager entity;

	@Autowired
	LandApprovalRepository landApprovalRepository;

	@Autowired
	LandAreaStatisticsRepository landGis;

	@Autowired
	CommonService commonService;
	
	@Autowired
	NotificationDetailsServiceImpl notificationDetailsServiceImpl;
	
	@Autowired
	LandApplicantJPARepository landApplicantJpaRepository;
	
	@Autowired
	NotificationDetailsRepository notificationDetailsRepo;

	@Override
	public LandPaginationDTO getSearchLandApplicantDetailsPage(BigInteger roleId, String districtCode,
			Integer pageNumber, Integer pageSize, String tahasilCode, String villageCode, String khatianCode,
			String pageType) {
		List<LandApplicantDTO> respones = null;
		BigInteger count = null;

		if (pageType.equals("New")) {
			log.info(
					":: getSearchLandApplicantDetailsPage() execute and return getSearchLandApplicantDetailsPage() method data");
			if (districtCode != null || tahasilCode != null || villageCode != null || khatianCode != null) {
				count = landNativeRepo.getTotalApplicantCountPagination(roleId, districtCode, tahasilCode, villageCode,
						khatianCode);
				respones = landNativeRepo.getSearchLandApplicantDetailsPage(roleId, districtCode, pageNumber, pageSize,
						tahasilCode, villageCode, khatianCode);
				return new LandPaginationDTO(respones, count);
			} else
				log.info(
						":: getSearchLandApplicantDetailsPage() execute and return getLandApplicantDetailsPage() method data");
			count = landNativeRepo.getTotalApplicantCount(roleId, districtCode, tahasilCode, villageCode, khatianCode);
			respones = landNativeRepo.getLandApplicantDetailsPage(roleId, pageNumber, pageSize);
			return new LandPaginationDTO(respones, count);
		}

		else if (pageType.equals("Under Process")) {
			if (districtCode != null || tahasilCode != null || villageCode != null || khatianCode != null) {
				log.info(
						":: getSearchLandApplicantDetailsPage() execute and return getLandApplicantsWithDetailsSearchFunction() method data");
				respones = landapprepo.getLandApplicantsWithDetailsSearchFunction(roleId, districtCode, pageNumber,
						pageSize, tahasilCode, villageCode, khatianCode);
				count = landapprepo.getUnerProcessUseLike( roleId, districtCode, tahasilCode, villageCode, khatianCode);
				return new LandPaginationDTO(respones, count);
			} else {
				log.info(
						":: getSearchLandApplicantDetailsPage() execute and return getLandApplicantsWithDetails() method data");
				respones = landapprepo.getLandApplicantsWithDetails(roleId, pageNumber, pageSize);
				count = landapprepo.getUnerProcess(roleId);
				return new LandPaginationDTO(respones, count);
			}
		}

		else if (pageType.equals("Revert to Citizen")) {
			if (districtCode != null || tahasilCode != null || villageCode != null || khatianCode != null) {
				log.info(
						":: getSearchLandApplicantDetailsPage() execute and return getLandApplicantsRevertCitizenDetailsSearchFunction() method data");

				respones = landapprepo.getLandApplicantsRevertCitizenDetailsSearchFunction(3, districtCode, pageNumber,
						pageSize, tahasilCode, villageCode, khatianCode);
				count = landapprepo.getRevertToCitizenUseLike(3, districtCode, tahasilCode, villageCode, khatianCode);
				return new LandPaginationDTO(respones, count);
			} else {
				log.info(
						":: getSearchLandApplicantDetailsPage() execute and return getLandApplicantsRevertCitizenDetails() method data");

				respones = landapprepo.getLandApplicantsRevertCitizenDetails(3, pageNumber, pageSize);
				count = landapprepo.getRevertToCitizen(3);
				return new LandPaginationDTO(respones, count);
			}
		}

		else if (pageType.equals("Approved")) {
			if (districtCode != null || tahasilCode != null || villageCode != null || khatianCode != null) {
				log.info(
						":: getSearchLandApplicantDetailsPage() execute and return getLandApplicantsWithApproveDetailsSerchFunction() method data");
				respones = landapprepo.getLandApplicantsWithApproveDetailsSerchFunction(6, districtCode, pageNumber,
						pageSize, tahasilCode, villageCode, khatianCode);
				count = landapprepo.getApproveUseLike(6, districtCode, tahasilCode, villageCode, khatianCode);
				return new LandPaginationDTO(respones, count);

			} else {
				log.info(
						":: getSearchLandApplicantDetailsPage() execute and return getLandApplicantsWithApproveDetails() method data");
				respones = landapprepo.getLandApplicantsWithApproveDetails(6, pageNumber, pageSize);
				count = landapprepo.getApprove(6);
				return new LandPaginationDTO(respones, count);
			}
		}

		else if (pageType.equals("Reject")) {
			if (districtCode != null || tahasilCode != null || villageCode != null || khatianCode != null) {
				log.info(
						":: getSearchLandApplicantDetailsPage() execute and return getLandApplicantsWithRejectDetailsSearchFunction() method data");
				respones = landapprepo.getLandApplicantsWithRejectDetailsSearchFunction(2, districtCode, pageNumber,
						pageSize, tahasilCode, villageCode, khatianCode);
				count = landapprepo.getRejectUseLike(2, districtCode, tahasilCode, villageCode, khatianCode);
				return new LandPaginationDTO(respones, count);
			} else {
				log.info(
						":: getSearchLandApplicantDetailsPage() execute and return getLandApplicantsWithRejectDetails() method data");
				respones = landapprepo.getLandApplicantsWithRejectDetails(2, pageNumber, pageSize);
				count = landapprepo.getReject(2);
				return new LandPaginationDTO(respones, count);
			}
		}

		return new LandPaginationDTO(respones, count);

	}

	@Override
	public LandAppResponseStructureDTO findAllDetailsBylandApplicantId(BigInteger landApplicantId) {
		return landNativeRepo.getCombinedDataForApplicant(landApplicantId);

	}

	@Override
	public Integer updateApplicantName(BigInteger applicantId, String orderId, String paymentSignature,
			String paymentId, Integer userId, BigDecimal amount, Integer userRoleId) {
		String paymentStatus = "";

		if (paymentId != null) {
			paymentStatus = "Success Payment";
		} else {
			paymentStatus = "Fail Payment";
		}
		String applicantName = CommonApplicationNumberGenerator.generateApplicantUniqueNumber("LA");
		String tranctionUniqueNo=TransactionNumberGenerateHelper.createTransactionNumber(applicantId);
		Integer tranctionCount = nativerepo.insertPaymentTransaction(applicantId, orderId, paymentSignature, paymentId,
				paymentStatus, amount,tranctionUniqueNo);
		if (tranctionCount > 0) {
			String queryy = "select application_status_id from land_application_approval WHERE land_application_id = "
					+ applicantId;

			Object applicationsatus = 0;
			try {
				applicationsatus = CommonUtil.getDynSingleData(entity, queryy);
			} catch (Exception ex) {
				applicationsatus = 0;
			}

			if (applicationsatus.toString().equalsIgnoreCase("8") || applicationsatus.toString().equalsIgnoreCase("9")
					|| applicationsatus.toString().equalsIgnoreCase("10")) {
				String updateLandApplicationApprovalQuery = "UPDATE public.land_application_approval "
						+ " SET pending_at_role_id = 3, "
						+ "   application_status_id = 18, approval_action_id = 0, approval_level = 1"
						+ "  WHERE land_application_id = :landappid";

				entity.createNativeQuery(updateLandApplicationApprovalQuery).setParameter("landappid", applicantId)
						.executeUpdate();

			} else {

				String query = "select role_id from approval_configration where status = '0' and approval_type='Land Application' and approval_level = 1";
				Object roleId = entity.createNativeQuery(query).getSingleResult();

				// landApplicationId
				Integer count = landApprovalRepository.applicationCount(applicantId).intValue();
				if (count <= 0) {
					landApprovalRepository.insertLandWithRole(applicantId, 1, (Short) roleId, 1, 0, "", userId);

					String plotIdQuery = "SELECT STRING_AGG(CONCAT('''', plot_code, ''''), ',') AS plotCode FROM land_schedule WHERE deleted_flag = '0' AND land_application_id = :applicantId";
					Object plotIds = entity.createNativeQuery(plotIdQuery).setParameter("applicantId", applicantId)
							.getSingleResult();
					Integer countInsert = landGis.updateLandGis(plotIds, 1, 1);
				}

			}
		}
		nativerepo.updateAppStatusForLandApplicationId(applicantId, 4);
		
		Integer result =nativerepo.updateApplicantName(applicantId, applicantName);

		// added for inserting in db only(application flow) Land Application Applied
		ApplicationFlowDto dto = new ApplicationFlowDto();
		dto.setLandApplicationId(applicantId);
		dto.setApplicationFlowId(BigInteger.valueOf(1));
		dto.setActionDateTime(new Date());
		dto.setActionRoleId(BigInteger.valueOf(userRoleId));
		commonService.saveApplicationFlow(dto);
		
		//for manual notifications to be send to citizen and Dealing Officer and Revenue Officer
		//for citizen
		log.info("value of land APP  :"+applicantId);
		int id = applicantId.intValue();
		NotificationDTO notificationDTO = new NotificationDTO();
		Land_applicant entity =  landApplicantJpaRepository.findByIntId(id);
		log.info("entity value:-------------------------- "+entity);
		notificationDTO.setNotification("Land application "+entity.getApplicantNo() +" is created.");
		notificationDTO.setUserType("CI");
		notificationDTO.setUserId(new BigInteger(entity.getIntCreatedBy().toString()));//   userId.toString()));
		notificationDTO.setCreatedBy(new BigInteger(entity.getIntCreatedBy().toString()));//  new BigInteger(userId.toString()));
//		
		notificationDetailsServiceImpl.submitNotification(notificationDTO);
		
		//for officers
		List<BigInteger> revenueUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("6"));
		List<BigInteger> dealingUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("3"));
		if(revenueUserList != null && revenueUserList.size() > 0) {
			for(BigInteger revenueUser: revenueUserList) {
				NotificationDTO notificationDtoForRevenue = new NotificationDTO();
				notificationDtoForRevenue.setNotification("Land application "+entity.getApplicantNo() +" is successfully created by "+entity.getTxtApplicantName());
				notificationDtoForRevenue.setUserId(revenueUser);
				notificationDtoForRevenue.setCreatedBy(revenueUser);
				notificationDtoForRevenue.setUserType("O");
				notificationDetailsServiceImpl.submitNotification(notificationDtoForRevenue);
			}
		}
		if(dealingUserList != null && dealingUserList.size() > 0) {
			for(BigInteger dealUser:dealingUserList) {
				NotificationDTO notificationDtoForDealing = new NotificationDTO();
				notificationDtoForDealing.setNotification("Land application "+entity.getApplicantNo() +"is successfully created by "+entity.getTxtApplicantName());
				notificationDtoForDealing.setUserId(dealUser);
				notificationDtoForDealing.setCreatedBy(dealUser);
				notificationDtoForDealing.setUserType("O");
				notificationDetailsServiceImpl.submitNotification(notificationDtoForDealing);
			}
		}
		

		
		log.info(":: payment completed and data will updated..!!" + tranctionCount);
		return result;

	}

	private static final String DATA_FILE_PATH = "LAcounter_data.txt";
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
			logger.error(e.getMessage());
		}
	}

	@Override
	public List<ApplicantNumberAndMobileDTO> fetchApplicantInfoById(BigInteger i) {
		return nativerepo.fetchApplicantInfoById(i);
	}

	@Override
	public LandPaginationDTO getLandApplicantDetailsPage(String plotCode, Short mettingLevleId, Integer auctionFlag) {
		List<LandApplicantDTO> respones = null;
		BigInteger count = null;
		try {
			if (auctionFlag == 3) {
				respones = landapprepo.getAuctionFlagData(plotCode);
			} else {
				respones = landapprepo.getLandApplicantsDetails(plotCode, mettingLevleId);
			}
		} catch (Exception e) {
			logger.error("Error,  in getLandApplicantDetailsPage {} ", e.getMessage());
		}

		return new LandPaginationDTO(respones, count);
	}

	@Override
	public LandAppHistoryDTO viewApplicationHistory(BigInteger landApplicantId) {
		List<LandApplicantDTO> response = null;
		response = landapprepo.viewApplicationHistory(landApplicantId);
		return new LandAppHistoryDTO(response);
	}

	@Override
	public LandPaginationDTO getlandForAfterMeetings(Short mettingLevleId, BigInteger meetingId,
			BigInteger meetingSheduleId, Integer auctionFlag) {
		List<LandApplicantDTO> respones = null;
		BigInteger count = null;
		try {
			respones = landapprepo.getlandForAfterMeetings(mettingLevleId, meetingId, meetingSheduleId, auctionFlag);
		} catch (Exception e) {
			logger.error("Error,  in getLandApplicantDetailsPage {} ", e.getMessage());
		}

		return new LandPaginationDTO(respones, count);
	}

	public BigInteger doGet(HttpServletRequest request) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String roleId = "";
		if (session.getAttribute("roleId") != null) {
			roleId = (String) session.getAttribute("roleId");
		}
		return BigInteger.valueOf(Long.parseLong(roleId));
	}

	@Override
	public LandAppHistoryDTO viewCitizenApplicationHistory(BigInteger landApplicantId) {
		List<LandApplicantDTO> response = null;
		response = landapprepo.viewCitizenApplicationHistory(landApplicantId);
		return new LandAppHistoryDTO(response);
	}

	@Override
	public LandAppHistoryDTO getApplicationHistoryReport(Integer pageSize, Integer pageNumber) {
		List<LandApplicantDTO> response = landapprepo.getApplicationHistoryReport(pageSize, pageNumber);

		return new LandAppHistoryDTO(response);
	}

	@Override
	public Integer countOfAppHistoryReport() {
		return landapprepo.getCountOfApplicationHistoryReport();
	}

}
