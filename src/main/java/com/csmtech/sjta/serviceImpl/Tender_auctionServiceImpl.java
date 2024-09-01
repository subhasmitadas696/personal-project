package com.csmtech.sjta.serviceImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.AuctionPriviewDTO;
import com.csmtech.sjta.entity.Tender_auction;
import com.csmtech.sjta.mobile.dto.ApplicationFlowDto;
import com.csmtech.sjta.mobile.repository.ApplicationFlowRepository;
import com.csmtech.sjta.mobile.service.CommonService;
import com.csmtech.sjta.repository.TenderAuctionRepo;
import com.csmtech.sjta.repository.TenderAuctionRepository;
import com.csmtech.sjta.service.Tender_auctionService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service
public class Tender_auctionServiceImpl implements Tender_auctionService {

	private static final String DATA_FILE_PATH = "TenderUniqueGenerator.txt";
	private static Integer coutTabRecord1;
	private static Date lastGeneratedDate;

	@Autowired
	private TenderAuctionRepo tender_auctionRepository;
	@Autowired
	EntityManager entityManager;

	@Autowired
	private TenderAuctionRepository nativeRepo;

	@Autowired
	private CommonService commonService;

	@Autowired
	ApplicationFlowRepository applicationFlowRepo;

	BigInteger parentId;
	Object dynamicValue = null;
	private static final Logger logger = LoggerFactory.getLogger(Tender_auctionServiceImpl.class);
	JSONObject json = new JSONObject();
//	@Value("${tempUpload.path}")
//	private String tempUploadPath;
//	@Value("${finalUpload.path}")
//	private String finalUploadPath;

	@Override
	public JSONObject save(String data) {
		logger.info("Inside save method of Tender_auctionServiceImpl");
		try {
			ObjectMapper om = new ObjectMapper();
			Tender_auction tender_auction = om.readValue(data, Tender_auction.class);
			if (!Objects.isNull(tender_auction.getIntId())
					&& tender_auction.getIntId().compareTo(BigInteger.ZERO) > 0) {
				Tender_auction getEntity = entityManager.find(Tender_auction.class, tender_auction.getIntId());
				getEntity.setTxtAuctionName20(tender_auction.getTxtAuctionName20());
				getEntity.setSelSelectSource21(tender_auction.getSelSelectSource21());
				getEntity.setTxtLeasePeriodinYears22(tender_auction.getTxtLeasePeriodinYears22());
				getEntity.setSelCommitteeMemberName23(tender_auction.getSelCommitteeMemberName23());
				getEntity.setTxtRoyalityBasePriceofMineral26(tender_auction.getTxtRoyalityBasePriceofMineral26());
				getEntity.setTxtFormMSubmissionStartDate27(tender_auction.getTxtFormMSubmissionStartDate27());
				getEntity.setTxtFormMSubmissionEndDate28(tender_auction.getTxtFormMSubmissionEndDate28());
				getEntity.setTxtSecurityDepositStartDate29(tender_auction.getTxtSecurityDepositStartDate29());
				getEntity.setTxtSecurityDepositEndDate(tender_auction.getTxtSecurityDepositEndDate());
				getEntity.setTxtBidDocumentDownloadStartDate(tender_auction.getTxtBidDocumentDownloadStartDate());
				getEntity.setTxtBidDocumentDownloadEndDate(tender_auction.getTxtBidDocumentDownloadEndDate());
				getEntity.setTxtDateOfTechnicalEvaluation(tender_auction.getTxtDateOfTechnicalEvaluation());
				getEntity.setTxtApplicationFeeNonRefundable(tender_auction.getTxtApplicationFeeNonRefundable());
				getEntity.setTxtSecurityAmount(tender_auction.getTxtSecurityAmount());
				getEntity.setTxtSlotsForAuctionFromDate(tender_auction.getTxtSlotsForAuctionFromDate());
				getEntity.setTxtSlotsForAuctionToDate(tender_auction.getTxtSlotsForAuctionToDate());
				Tender_auction updateData = tender_auctionRepository.save(getEntity);
				parentId = updateData.getIntId();
				json.put(CommonConstant.STATUS_KEY, 202);
			} else {
				Tender_auction saveData = tender_auctionRepository.save(tender_auction);
				nativeRepo.updatePlotIdThroughTenderFlag(tender_auction.getSelSelectSource21(),
						tender_auction.getPlotCode());
				parentId = saveData.getIntId();
				String grivaneGenerateNo = Tender_auctionServiceImpl.generateApplicantUniqueNumber("AU");
				String updateUnqueNo = nativeRepo.updateTenderAuctionAuctionNumber(parentId, grivaneGenerateNo);
				if (updateUnqueNo != null && tender_auction.getHidebidDocsAuctionFlag() == 1) {
					// here insert the live auction when the non publish scenario are come
					BigInteger bigVal = new BigInteger(tender_auction.getIntCreatedBy().toString());
					nativeRepo.insertLiveAuctionData(parentId, bigVal);
				}
				json.put("UniqueNoGen", updateUnqueNo);
				json.put(CommonConstant.STATUS_KEY, 200);
			}
			json.put("id", parentId);
			BigInteger auctionPlotId = applicationFlowRepo.findAuctionPlotId(parentId);
			List<BigInteger> landAppId = applicationFlowRepo.findLandAppIds(auctionPlotId.intValue());

			// application flow
			for (BigInteger appId : landAppId) {

				ApplicationFlowDto dto = new ApplicationFlowDto();
				dto.setLandApplicationId(appId != null ? appId : BigInteger.ZERO);
				dto.setApplicationFlowId(BigInteger.valueOf(26));
				dto.setActionDateTime(new Date());
				dto.setActionRoleId(BigInteger.valueOf(4));
				commonService.saveApplicationFlow(dto);
			}
		} catch (Exception e) {
			logger.error("Inside save method of Tender_auctionServiceImpl some error occur:" + e);
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	@Override
	public List<AuctionPriviewDTO> getById(BigInteger id) {
		logger.info("Inside getById method of Tender_auctionServiceImpl");
		List<Object[]> getRecord = nativeRepo.getAuctionsGetId(id);
		List<AuctionPriviewDTO> auctionPreviewDTOs = new ArrayList<>();
		for (Object[] row : getRecord) {
			AuctionPriviewDTO dto = new AuctionPriviewDTO();
			dto.setIntid((BigInteger) row[0]);
			dto.setAuctionId((BigInteger) row[1]);
			dto.setCombinedValues((String) row[2]);
			dto.setLeasePeriodYears((Integer) row[3]);
			dto.setCommitteeMemberName((String) row[4]);
//			dto.setMgqAnnuallyIncome((BigDecimal) row[5]);
//			dto.setMinimumAdditionalCharge((BigDecimal) row[6]);
			dto.setRoyalty((BigDecimal) row[5]);
			dto.setFormMSubmitStartDate((Date) row[6]);
			dto.setFormMSubmitEndDate((Date) row[7]);
			dto.setSecurityDepositStartDate((Date) row[8]);
			dto.setSecurityDepositEndDate((Date) row[9]);
			dto.setBidDocumentDownloadStartDate((Date) row[10]);
			dto.setBidDocumentDownloadEndDate((Date) row[11]);
			dto.setDateOfTechnicalEvaluation((Date) row[12]);
			dto.setApplicationFeeNotRefund((BigDecimal) row[13]);
			dto.setSecurityAmountDeposit((BigDecimal) row[14]);
			dto.setSlotForAuctionFromDate((Date) row[15]);
			dto.setSlotForAuctionToDate((Date) row[16]);
			dto.setAuctionName2((String) row[17]);
			dto.setCommitteeMemberNameVal((String) row[18]);
			dto.setAuctionPlotDetailsId((BigInteger) row[19]);
			dto.setGoForAuctionFlag((Short) row[20]);
			dto.setMemberName((String) row[21]);
			auctionPreviewDTOs.add(dto);
		}

		return auctionPreviewDTOs;
	}

	@Override
	public JSONObject getAll(String formParams) {
		logger.info("Inside getAll method of Tender_auctionServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		Integer totalDataPresent = tender_auctionRepository.countByBitDeletedFlag(false);
		Pageable pageRequest = PageRequest.of(jsonData.has("pageNo") ? jsonData.getInt("pageNo") - 1 : 0,
				jsonData.has("size") ? jsonData.getInt("size") : totalDataPresent,
				Sort.by(Sort.Direction.DESC, "intId"));
		List<Tender_auction> tender_auctionResp = tender_auctionRepository.findAllByBitDeletedFlag(false, pageRequest);
		for (Tender_auction entity : tender_auctionResp) {
			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select district_name from application.auction_plot where district_code="
								+ entity.getSelSelectSource21());
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelSelectSource21Val(dynamicValue.toString());
			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select full_name from user_details where user_id=" + entity.getSelCommitteeMemberName23());
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelCommitteeMemberName23Val(dynamicValue.toString());

		}
		json.put(CommonConstant.STATUS_KEY, 200);
		json.put("result", new JSONArray(tender_auctionResp));
		json.put("count", totalDataPresent);
		return json;
	}

	@Override
	public JSONObject deleteById(BigInteger id, BigInteger auctionPlotDetailsId) {
		logger.info("Inside deleteById method of Tender_auctionServiceImpl");
		try {
			Date countDown = tender_auctionRepository.countDownTimeValidate(id);
			if (countDown != null) {
				long fiveMinutesInMillis = (long) 5 * 60 * 1000;
				long fiveMinutesAgoMillis = countDown.getTime() - fiveMinutesInMillis;
				Date fiveMinutesAgoDate = new Date(fiveMinutesAgoMillis);
				if (fiveMinutesAgoDate.before(new Date())) {
					json.put(CommonConstant.STATUS_KEY, 406);
					json.put(CommonConstant.TIME_EXPIRED, "time expired");
				} else {
					BigInteger auctionId = nativeRepo.getAuctionId(id);
					nativeRepo.updatePlotIdThroughTenderFlagDelete(auctionId, auctionPlotDetailsId);
					Tender_auction entity = entityManager.find(Tender_auction.class, id);
					entity.setBitDeletedFlag(true);
					json.put(CommonConstant.STATUS_KEY, 200);
				}
			}

		} catch (Exception e) {
			logger.error("Inside deleteById method of Tender_auctionServiceImpl some error occur:" + e);
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	public static JSONArray fillselSelectSourceList(EntityManager em, String jsonVal) {
		JSONObject jsonData = new JSONObject(jsonVal);
		JSONArray mainJSONFile = new JSONArray();
		System.out.println(jsonData.getInt("inserUpdateFlag"));
		if (jsonData.getInt("inserUpdateFlag") == 0) {
			logger.info("Inside fillselSelectSourceList method of Tender_creationServiceImpl");
			String query = " SELECT ap.auction_plot_id, "
					+ "CAST((SELECT district_name FROM land_bank.district_master WHERE district_code = ap.district_code LIMIT 1) || ' - ' || "
					+ "(SELECT tahasil_name FROM land_bank.tahasil_master WHERE tahasil_code = ap.tahasil_code LIMIT 1) || ' - ' || "
					+ "(SELECT village_name FROM land_bank.village_master WHERE village_code = ap.village_code LIMIT 1) || ' - ' || "
					+ "(SELECT khata_no FROM land_bank.khatian_information WHERE khatian_code = ap.khatian_code LIMIT 1)|| ' - ' || "
					+ "(SELECT plot_no FROM land_bank.plot_information WHERE plot_code=apd.plot_code LIMIT 1)AS character varying)AS combined_values,apd.go_for_auction_process_flag,"
					+ "apd.plot_code,apd.auction_plot_details_id  "
					+ " FROM application.auction_plot ap JOIN application.auction_plot_details apd ON(ap.auction_plot_id=apd.auction_plot_id) "
//					+ " JOIN application.land_allotement_for_auction lafa ON(lafa.plot_code=apd.plot_code)  "
					+ " WHERE ap.deleted_flag = '0' AND apd.deleted_flag='0' AND approve_status='A' AND auction_flag=2 AND apd.aucion_tender_creation_flag=0 ";

			List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
			for (Object[] data : dataList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("auction_plot_id", data[0]);
				jsonObj.put("auction_plot_name", data[1]);
				jsonObj.put("auction_plot_flag", data[2]);
				jsonObj.put("plot_code", data[3]);
				jsonObj.put("auction_plot_details_id", data[4]);
				mainJSONFile.put(jsonObj);
			}
		} else if (jsonData.getInt("inserUpdateFlag") == 1) {
			logger.info("Inside fillselSelectSourceList method of Tender_creationServiceImpl");
			String query = " SELECT ap.auction_plot_id, "
					+ "CAST((SELECT district_name FROM land_bank.district_master WHERE district_code = ap.district_code LIMIT 1) || ' - ' || "
					+ "(SELECT tahasil_name FROM land_bank.tahasil_master WHERE tahasil_code = ap.tahasil_code LIMIT 1) || ' - ' || "
					+ "(SELECT village_name FROM land_bank.village_master WHERE village_code = ap.village_code LIMIT 1) || ' - ' || "
					+ "(SELECT khata_no FROM land_bank.khatian_information WHERE khatian_code = ap.khatian_code LIMIT 1)|| ' - ' || "
					+ "(SELECT plot_no FROM land_bank.plot_information WHERE plot_code=apd.plot_code LIMIT 1)AS character varying)AS combined_values,apd.go_for_auction_process_flag,"
					+ "apd.plot_code,apd.auction_plot_details_id  "
					+ " FROM application.auction_plot ap JOIN application.auction_plot_details apd ON(ap.auction_plot_id=apd.auction_plot_id) "
					+ " WHERE ap.deleted_flag = '0' AND apd.deleted_flag='0' AND approve_status='A' AND auction_flag=2 ";
//					+ "AND apd.aucion_tender_creation_flag=0 ";

			List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
			for (Object[] data : dataList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("auction_plot_id", data[0]);
				jsonObj.put("auction_plot_name", data[1]);
				jsonObj.put("auction_plot_flag", data[2]);
				jsonObj.put("plot_code", data[3]);
				jsonObj.put("auction_plot_details_id", data[4]);
				mainJSONFile.put(jsonObj);
			}
		} else {
			return mainJSONFile;
		}

		return mainJSONFile;
	}

	public static JSONArray fillselCommitteeMemberNameList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselCommitteeMemberNameList method of Tender_creationServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		String query = " select user_id,full_name from public.user_details " + "where user_type='O' "
				+ "AND user_block_status = false " + "AND status='0' ";

		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("item_id", data[0]);
			jsonObj.put("item_text", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	@Override
	public JSONObject getPriviewTenderRecord(String formParams) {
		JSONObject jsonData = new JSONObject(formParams);
		Integer pageNo = jsonData.getInt("pageNo");
		Integer pagesize = jsonData.getInt("size");
		String publishStatus = jsonData.getString("publishStatus");
		String sourceName = jsonData.getString("sourceName");
		List<Object[]> getRecord = null;
		BigInteger count = null;
		if (!publishStatus.equals("") || !sourceName.equals("")) {
			getRecord = nativeRepo.getAuctionsUseLike(pageNo, pagesize, sourceName, publishStatus);
			count = nativeRepo.getActiveAuctionForLikeCount(sourceName, publishStatus);
		} else {
			getRecord = nativeRepo.getAuctions(pageNo, pagesize);
			count = nativeRepo.getActiveAuctionCount();
		}

		List<AuctionPriviewDTO> auctionPreviewDTOs = new ArrayList<>();
		for (Object[] row : getRecord) {
			AuctionPriviewDTO dto = new AuctionPriviewDTO();
			dto.setIntid((BigInteger) row[0]);
			dto.setAuctionId((BigInteger) row[1]);
			dto.setCombinedValues((String) row[2]);
			dto.setLeasePeriodYears((Integer) row[3]);
			dto.setCommitteeMemberName((String) row[4]);
			dto.setRoyalty((BigDecimal) row[5]);
			dto.setFormMSubmitStartDate((Date) row[6]);
			dto.setFormMSubmitEndDate((Date) row[7]);
			dto.setSecurityDepositStartDate((Date) row[8]);
			dto.setSecurityDepositEndDate((Date) row[9]);
			dto.setBidDocumentDownloadStartDate((Date) row[10]);
			dto.setBidDocumentDownloadEndDate((Date) row[11]);
			dto.setDateOfTechnicalEvaluation((Date) row[12]);
			dto.setApplicationFeeNotRefund((BigDecimal) row[13]);
			dto.setSecurityAmountDeposit((BigDecimal) row[14]);
			dto.setSlotForAuctionFromDate((Date) row[15]);
			dto.setSlotForAuctionToDate((Date) row[16]);
			dto.setAuctionName2((String) row[17]);
			dto.setPublishStatus((Boolean) row[19]);
			dto.setDistrictCode((String) row[20]);
			dto.setTahasilCode((String) row[21]);
			dto.setVillageCode((String) row[22]);
			dto.setKhataNo((String) row[23]);
			dto.setAuctionProcessFlag((Short) row[24]);
			dto.setAuctionPlotDetailsId((BigInteger) row[25]);
			auctionPreviewDTOs.add(dto);
		}
		json.put(CommonConstant.STATUS_KEY, 200);
		json.put("result", new JSONArray(auctionPreviewDTOs));
		json.put("count", count);
		return json;
	}

	@Override
	public Integer getPublicApproval(String tenderId, BigInteger createdBy) {
		Integer getCount = 0;
		String[] valuesArray = tenderId.split(",");
		List<BigInteger> valueOfTender = new ArrayList<>();
		for (String value : valuesArray) {
			BigInteger bigVal = new BigInteger(value);
			valueOfTender.add(bigVal);
			BigInteger getAuctionCountTender = nativeRepo.getAuctionCountTender(bigVal);
			if (getAuctionCountTender.equals(BigInteger.valueOf(0))) {
				if (nativeRepo.getAuctionCountTenderForPublishOrNot(bigVal).equals(BigInteger.valueOf(0))) {
					nativeRepo.insertLiveAuctionData(bigVal, createdBy);
					getCount = nativeRepo.getPublicApproval(bigVal);
				}
			}
		}
//		if (valuesArray != null) {
//			getCount = nativeRepo.getPublicApproval(valueOfTender);
//		}
		return getCount;
	}

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
	public Integer getUnPublicApproval(BigInteger tenderId) {
		Date countDown = tender_auctionRepository.countDownTimeValidate(tenderId);
		if (countDown != null) {
			long fiveMinutesInMillis = (long) 5 * 60 * 1000;
			long fiveMinutesAgoMillis = countDown.getTime() - fiveMinutesInMillis;
			Date fiveMinutesAgoDate = new Date(fiveMinutesAgoMillis);
			if (fiveMinutesAgoDate.before(new Date())) {
				return 99;
			} else {
				Integer count = nativeRepo.getUnPublicApproval(tenderId);
				if (count > 0) {
					nativeRepo.UnPublishRemoveRecord(tenderId);
					return count;
				} else {
					return 0;
				}
			}

		}
		return 0;
	}

}