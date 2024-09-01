package com.csmtech.sjta.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.AuctionDTO;
import com.csmtech.sjta.dto.AuctionDetails;
import com.csmtech.sjta.dto.BidderRegistratatonViewMoreDTO;
import com.csmtech.sjta.dto.FromMApplicationViewTenderWise;
import com.csmtech.sjta.dto.NotificationDTO;
import com.csmtech.sjta.dto.TenderViewOfficerDTO;
import com.csmtech.sjta.dto.VIewCitizenAuctionPlotDetailsDTO;
import com.csmtech.sjta.entity.Bidderregistrara;
import com.csmtech.sjta.mobile.dto.ApplicationFlowDto;
import com.csmtech.sjta.mobile.repository.ApplicationFlowRepository;
import com.csmtech.sjta.mobile.repository.NotificationDetailsRepository;
import com.csmtech.sjta.mobile.service.CommonService;
import com.csmtech.sjta.mobile.service.NotificationDetailsServiceImpl;
import com.csmtech.sjta.repository.BidderRegistratationRepository;
import com.csmtech.sjta.repository.BidderregistraraRepository;
import com.csmtech.sjta.service.BidderregistraraService;
import com.csmtech.sjta.util.TransactionNumberGenerateHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
public class BidderregistraraServiceImpl implements BidderregistraraService {
	@Autowired
	private BidderregistraraRepository bidderregistraraRepository;
	@Autowired
	EntityManager entityManager;

	@Autowired
	private BidderRegistratationRepository repo;

	@Autowired
	NotificationDetailsRepository notificationDetailsRepo;

	@Autowired
	NotificationDetailsServiceImpl notificationDetailsServiceImpl;

	@Autowired
	private CommonService commonService;

	@Autowired
	ApplicationFlowRepository applicationFlowRepo;

	BigInteger parentId;
	Object dynamicValue = null;
	private static final Logger logger = LoggerFactory.getLogger(BidderregistraraServiceImpl.class);
	JSONObject json = new JSONObject();
	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${file.path}")
	private String finalUploadPath;

	@SuppressWarnings("null")
	@Override
	public JSONObject save(String data) {
		BigInteger tenderAuctionId;
		BigDecimal applicationFeeNotRefund = null;
		BigDecimal securityAmountDeposit = null;
		logger.info("Inside save method of BidderregistraraServiceImpl");
		try {
			ObjectMapper om = new ObjectMapper();
			Bidderregistrara bidderregistrara = om.readValue(data, Bidderregistrara.class);
			List<String> fileUploadList = new ArrayList<String>();
			fileUploadList.add(bidderregistrara.getFileUploadPANCard());
			fileUploadList.add(bidderregistrara.getFileUploadedAadharCard());
			fileUploadList.add(bidderregistrara.getFileUploadedResentPhotoOfTheBidder());
			fileUploadList.add(bidderregistrara.getFileUploadedResentSignatureOfTheBidder());
			if (!Objects.isNull(bidderregistrara.getIntId())
					&& bidderregistrara.getIntId().compareTo(BigInteger.ZERO) > 0) {
				Bidderregistrara getEntity = entityManager.find(Bidderregistrara.class, bidderregistrara.getIntId());
				getEntity.setTxtContactPersonName(bidderregistrara.getTxtContactPersonName());
				getEntity.setTxtPanNumber(bidderregistrara.getTxtPanNumber());
				getEntity.setFileUploadPANCard(bidderregistrara.getFileUploadPANCard());
				getEntity.setTxtAadharNumber(bidderregistrara.getTxtAadharNumber());
				getEntity.setFileUploadedAadharCard(bidderregistrara.getFileUploadedAadharCard());
				getEntity.setFileUploadedResentPhotoOfTheBidder(
						bidderregistrara.getFileUploadedResentPhotoOfTheBidder());
				Boolean checkTimeValidation = repo.getCountDateAndTimeValidation(getEntity.getTenderAuctionId());
				Bidderregistrara updateData = null;
				if (!checkTimeValidation) {
					updateData = bidderregistraraRepository.save(getEntity);
					parentId = updateData.getIntId();
					List<Object[]> results = repo.getAmountRecord(updateData.getTenderAuctionId(),
							updateData.getPlotCode());
					for (Object[] result : results) {
						tenderAuctionId = (BigInteger) result[0];
						applicationFeeNotRefund = (BigDecimal) result[1];
						securityAmountDeposit = (BigDecimal) result[2];
					}
					// manual notification
					NotificationDTO dto = new NotificationDTO();
					dto.setNotification(data);
					dto.setUserId(null);
					dto.setCreatedBy(null);
					dto.setUserType(data);

					// application flow
					BigInteger tenderAuctionIdFormM = applicationFlowRepo.findTenderAuctionId(parentId);
					BigInteger auctionPlotId = applicationFlowRepo.findAuctionPlotId(tenderAuctionIdFormM);
					List<BigInteger> landAppId = applicationFlowRepo.findLandAppIds(auctionPlotId.intValue());

					for (BigInteger appId : landAppId) {
						ApplicationFlowDto flowDto = new ApplicationFlowDto();
						flowDto.setLandApplicationId(appId != null ? appId : BigInteger.ZERO);
						flowDto.setApplicationFlowId(BigInteger.valueOf(27));
						flowDto.setActionDateTime(new Date());
						flowDto.setActionRoleId(BigInteger.valueOf(2));
						commonService.saveApplicationFlow(flowDto);

					}

					json.put("status", 202);
					json.put("applicationFee", applicationFeeNotRefund);
					json.put("securityAmount", securityAmountDeposit);
				} else {
					json.put("status", 232);
					json.put("errMsg", " Auction Time Expired. ");
					// parentId = getEntity.getIntId();
				}
			} else {
				Boolean checkTimeValidation = repo.getCountDateAndTimeValidation(bidderregistrara.getTenderAuctionId());
				if (!checkTimeValidation) {
					BigInteger getDuplicateCount = repo.getCountDuplicateApplication(
							bidderregistrara.getTenderAuctionId(), bidderregistrara.getIntCreatedBy());
					if (getDuplicateCount.equals(BigInteger.valueOf(0))) {
						List<Object[]> results = repo.getAmountRecord(bidderregistrara.getTenderAuctionId(),
								bidderregistrara.getPlotCode());
						for (Object[] result : results) {
							tenderAuctionId = (BigInteger) result[0];
							applicationFeeNotRefund = (BigDecimal) result[1];
							securityAmountDeposit = (BigDecimal) result[2];
						}
						try {
							for (String fileUpload : fileUploadList) {
								if (!fileUpload.equals("")) {
									File src = new File(tempUploadPath + fileUpload);
									if (src.exists()) {
										File dest = new File(finalUploadPath + "/" + fileUpload);
										try {
											Files.copy(src.toPath(), dest.toPath(),
													StandardCopyOption.REPLACE_EXISTING);
											Files.delete(src.toPath());
										} catch (IOException e) {
											log.error("error occured while single file operation: " + e.getMessage());
										}
									} else {
										logger.info("File does not exist at");
									}
								}
							}

						} catch (Exception e) {
							log.error("error occured while upload file operation: " + e.getMessage());
						}
						Bidderregistrara saveData = bidderregistraraRepository.save(bidderregistrara);
						parentId = saveData.getIntId();
						logger.info("Update Success");
						json.put("applicationFee", applicationFeeNotRefund);
						json.put("securityAmount", securityAmountDeposit);
						json.put("status", 200);

						// application flow
						BigInteger tenderAuctionIdFormM = applicationFlowRepo.findTenderAuctionId(parentId);
						BigInteger auctionPlotId = applicationFlowRepo.findAuctionPlotId(tenderAuctionIdFormM);
						List<BigInteger> landAppId = applicationFlowRepo.findLandAppIds(auctionPlotId.intValue());

						for (BigInteger appId : landAppId) {
							ApplicationFlowDto flowDto = new ApplicationFlowDto();
							flowDto.setLandApplicationId(appId != null ? appId : BigInteger.ZERO);
							flowDto.setApplicationFlowId(BigInteger.valueOf(27));
							flowDto.setActionDateTime(new Date());
							flowDto.setActionRoleId(BigInteger.valueOf(2));
							commonService.saveApplicationFlow(flowDto);

						}

						// add the manual notification part
						// for citizen
						NotificationDTO dto = new NotificationDTO();
						Bidderregistrara entity = bidderregistraraRepository
								.findByIntId(new BigInteger(parentId.toString()));
						dto.setNotification(
								"Form M has been uploaded with application number " + entity.getUniqueNo() + " .");
						dto.setUserId(entity.getIntCreatedBy());
						dto.setCreatedBy(entity.getIntCreatedBy());
						dto.setUserType("CI");

						// for officers
						List<BigInteger> revenueUserList = notificationDetailsRepo
								.fetchUserDetailsOnRoleId(new BigInteger("6"));
						List<BigInteger> landUserList = notificationDetailsRepo
								.fetchUserDetailsOnRoleId(new BigInteger("4"));
						if (revenueUserList != null && revenueUserList.size() > 0) {
							for (BigInteger revenueUser : revenueUserList) {
								NotificationDTO notificationDtoForRevenue = new NotificationDTO();
								notificationDtoForRevenue
										.setNotification("Form M has been uploaded with application number "
												+ entity.getUniqueNo() + " .");
								notificationDtoForRevenue.setUserId(revenueUser);
								notificationDtoForRevenue.setCreatedBy(revenueUser);
								notificationDtoForRevenue.setUserType("O");
								notificationDetailsServiceImpl.submitNotification(notificationDtoForRevenue);
							}
						}
						if (landUserList != null && landUserList.size() > 0) {
							for (BigInteger landUser : landUserList) {
								NotificationDTO notificationDtoForLand = new NotificationDTO();
								notificationDtoForLand
										.setNotification("Form M has been uploaded with application number "
												+ entity.getUniqueNo() + " .");
								notificationDtoForLand.setUserId(landUser);
								notificationDtoForLand.setCreatedBy(landUser);
								notificationDtoForLand.setUserType("O");
								notificationDetailsServiceImpl.submitNotification(notificationDtoForLand);
							}
						}

					} else {
						json.put("status", 233);
						json.put("errMsg", " Auction Duplicate Entry . ");
					}
				} else {
					json.put("status", 232);
					json.put("errMsg", " Auction Time Expired. ");
				}
			}
			json.put("id", parentId);

		} catch (Exception e) {
			logger.error("Inside save method of BidderregistraraServiceImpl some error occur:" + e, e.getMessage());
			json.put("status", 400);
		}
		return json;
	}

	// hear
	@Override
	public JSONObject getById(BigInteger id) {
		logger.info("Inside getById method of BidderregistraraServiceImpl");
		Bidderregistrara entity = bidderregistraraRepository.getAllRecord(id);
		return new JSONObject(entity);
	}

	@Override
	public JSONObject getAll(String formParams) {
		logger.info("Inside getAll method of BidderregistraraServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		Integer totalDataPresent = bidderregistraraRepository.countByBitDeletedFlag(jsonData.getBigInteger("intId"));
		Pageable pageRequest = PageRequest.of(jsonData.has("pageNo") ? jsonData.getInt("pageNo") - 1 : 0,
				jsonData.has("size") ? jsonData.getInt("size") : totalDataPresent);
		List<Bidderregistrara> bidderregistraraResp = bidderregistraraRepository.getallDataById(pageRequest,
				jsonData.getBigInteger("intId"));
		json.put("status", 200);
		json.put("result", new JSONArray(bidderregistraraResp));
		json.put("count", totalDataPresent);
		return json;
	}

	@Override
	public JSONObject deleteById(BigInteger id) {
		logger.info("Inside deleteById method of BidderregistraraServiceImpl");
		try {
			Integer count = repo.markBidderFormMApplicationAsDeleted(id);
			json.put("status", 200);
		} catch (Exception e) {
			logger.error("Inside deleteById method of BidderregistraraServiceImpl some error occur:" + e);
			json.put("status", 400);
		}
		return json;
	}

	@Override
	public List<BidderRegistratatonViewMoreDTO> getByIdNative(BigInteger id) {
		logger.info("Inside getById method of BidderregistraraServiceImpl");
		List<Object[]> data = repo.getBidderFormMApplicationById(id);
		List<BidderRegistratatonViewMoreDTO> auctionPreviewDTOs = new ArrayList<>();
		for (Object[] row : data) {
			BidderRegistratatonViewMoreDTO dto = new BidderRegistratatonViewMoreDTO();
			dto.setIntId((BigInteger) row[0]);
			dto.setFileUploadPANCard((String) row[1]);
			dto.setFileUploadedAadharCard((String) row[2]);
			dto.setFileUploadedResentSignatureOfTheBidder((String) row[3]);
			dto.setFileUploadedResentPhotoOfTheBidder((String) row[4]);
			dto.setTxtAadharNumber((String) row[5]);
			dto.setTxtContactPersonName((BigInteger) row[6]);
			dto.setTxtPanNumber((String) row[7]);
			dto.setSelState((String) row[8]);
			dto.setSelDistrict((String) row[9]);
			dto.setSelBlockULB((String) row[10]);
			dto.setSelGPWARDNumber((String) row[11]);
			dto.setSelVillageLocalAreaName((String) row[12]);
			dto.setTxtPoliceStation((String) row[13]);
			dto.setTxtPostOffice((String) row[14]);
			dto.setTxtHabitationStreetNoLandmark((String) row[15]);
			dto.setTxtHouseNo((String) row[16]);
			dto.setTxtPinCode((String) row[17]);
			dto.setSelState17((String) row[18]);
			dto.setSelDistrict18((String) row[19]);
			dto.setSelBlockULB19((String) row[20]);
			dto.setSelGPWardNo((String) row[21]);
			dto.setSelVillageLocalAreaName21((String) row[22]);
			dto.setTxtPoliceStation22((String) row[23]);
			dto.setTxtPostOffice23((String) row[24]);
			dto.setTxtHabitationStreetNoLandmark24((String) row[25]);
			dto.setTxtHouseNo25((String) row[26]);
			dto.setTxtPinCode26((String) row[27]);
			dto.setStatus((Character) row[28]);
			dto.setApplicationNo((String) row[29]);
			dto.setApplicantName((String) row[30]);
			dto.setTenderAuctionId((BigInteger) row[31]);
			auctionPreviewDTOs.add(dto);
		}
		return auctionPreviewDTOs;
	}

	@Override
	public List<VIewCitizenAuctionPlotDetailsDTO> getAuctionPlotData(Integer pageNumber, Integer pageSize,
			BigInteger userId) {
		List<Object[]> data = repo.getAuctionPlotData(pageNumber, pageSize, userId);
		List<VIewCitizenAuctionPlotDetailsDTO> auctionPreviewDTOs = new ArrayList<>();
		for (Object[] row : data) {
			VIewCitizenAuctionPlotDetailsDTO dto = new VIewCitizenAuctionPlotDetailsDTO();
			dto.setTenderAuctionId((BigInteger) row[0]);
			dto.setAuctionPlotId((BigInteger) row[1]);
			dto.setDistrictCode((String) row[2]);
			dto.setTahasilCode((String) row[3]);
			dto.setVillageCode((String) row[4]);
			dto.setKhatianCode((String) row[5]);
			dto.setPlotNo((String) row[6]);
			dto.setTotalArea(((BigDecimal) row[7]).toString());
			dto.setFromMStateDate((Date) row[8]);
			dto.setFromMEndDate((Date) row[9]);
			dto.setDateStatus((Boolean) row[10]);
			dto.setUniqueNoGen((String) row[11]);
			dto.setPlotCode((String) row[12]);
			auctionPreviewDTOs.add(dto);
		}
		return auctionPreviewDTOs;
	}

	@Override
	public BigInteger getTotalApplicantCount(BigInteger userId) {
		return repo.getTotalApplicantCount(userId);
	}

	@Override
	public String updateBidderFormMApplication(Integer appplicationId) {
		String grivaneGenerateNo = GrievanceServiceImpl.generateApplicantUniqueNumber("AU");
		return repo.updateBidderFormMApplication(appplicationId, grivaneGenerateNo);
	}

	@Override
	public Map<String, Object> getTenderAuctionDates(BigInteger appId) {
		return repo.getTenderAuctionDates(appId);
	}

	@Override
	public List<TenderViewOfficerDTO> getTenderOfficerRecord(String formParams) {
		JSONObject jsonData = new JSONObject(formParams);
		BigInteger count = repo.getTenderCount();
		Integer integerValue = count.intValue();
		Pageable pageRequest = PageRequest.of(jsonData.has("pageNo") ? jsonData.getInt("pageNo") - 1 : 0,
				jsonData.has("size") ? jsonData.getInt("size") : integerValue, Sort.by(Sort.Direction.DESC, "intId"));
		List<Object[]> results = repo.getTenderNames(pageRequest);
		List<TenderViewOfficerDTO> getTenderRecord = new ArrayList<>();
		for (Object[] result : results) {
			TenderViewOfficerDTO dto = new TenderViewOfficerDTO();
			dto.setTenderId((BigInteger) result[0]);
			dto.setTenderName((String) result[1]);
			dto.setTotalApplicant((String) result[2]);
			dto.setApprovedApplicant((String) result[3]);
			dto.setRejectApplicant((String) result[4]);
			getTenderRecord.add(dto);
		}
		return getTenderRecord;
	}

	@Override
	public BigInteger getTenderCount() {
		return repo.getTenderCount();
	}

	@Override
	public List<FromMApplicationViewTenderWise> getBidderFormApplicationData(String formParams) {
		JSONObject jsonData = new JSONObject(formParams);
		BigInteger id = jsonData.getBigInteger("intId");
		BigInteger count = repo.getTenderCountApplicant(jsonData.getBigInteger("intId"));
		Integer integerValue = count.intValue();
		Pageable pageRequest = PageRequest.of(jsonData.has("pageNo") ? jsonData.getInt("pageNo") - 1 : 0,
				jsonData.has("size") ? jsonData.getInt("size") : integerValue, Sort.by(Sort.Direction.DESC, "intId"));
		List<Object[]> results = repo.getBidderFormApplicationData(id, pageRequest);
		List<FromMApplicationViewTenderWise> getfromMRecord = new ArrayList<>();
		for (Object[] result : results) {
			FromMApplicationViewTenderWise dto = new FromMApplicationViewTenderWise();
			dto.setApplicantId((BigInteger) result[0]);
			dto.setPersonName((String) result[1]);
			dto.setApplicationNo((String) result[2]);
			dto.setApprovalStatus((String) result[3]);
			dto.setApprovalRemark((String) result[4]);
			getfromMRecord.add(dto);
		}
		return getfromMRecord;
	}

	@Override
	public BigInteger getTenderCountApplicant(BigInteger tenderAuctionId) {
		return repo.getTenderCountApplicant(tenderAuctionId);
	}

	@Override
	public Integer updateBidderFormApplication(String formParams) {
		JSONObject js = new JSONObject(formParams);
		BigInteger checkTime = repo.getCountForTenderAuction(js.getBigInteger("tenderAuctionId"));
		if (checkTime.equals(BigInteger.valueOf(1))) {
			Integer result = repo.updateBidderFormApplication(js.getBigInteger("intId"), js.getString("remark"),
					js.getString("status"));
			// form M evaluation by LO
			// for citizen
			// add the manual notification part
			NotificationDTO dto = new NotificationDTO();
			Bidderregistrara entity = bidderregistraraRepository.findByIntId(js.getBigInteger("intId"));
			dto.setNotification("Form M has been evaluated for " + entity.getUniqueNo() + " by Land Officer.");
			dto.setUserId(entity.getIntCreatedBy());
			dto.setCreatedBy(entity.getIntCreatedBy());
			dto.setUserType("CI");
			notificationDetailsServiceImpl.submitNotification(dto);

			// application flow

			BigInteger auctionPlotId = applicationFlowRepo.findAuctionPlotId(js.getBigInteger("tenderAuctionId"));
			List<BigInteger> landAppId = applicationFlowRepo.findLandAppIds(auctionPlotId.intValue());

			for (BigInteger appId : landAppId) {
				ApplicationFlowDto flowDto = new ApplicationFlowDto();
				flowDto.setLandApplicationId(appId != null ? appId : BigInteger.ZERO);
				flowDto.setApplicationFlowId(BigInteger.valueOf(28));
				flowDto.setActionDateTime(new Date());
				flowDto.setActionRoleId(BigInteger.valueOf(4));
				commonService.saveApplicationFlow(flowDto);
			}

			// for LO
			List<BigInteger> landUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("4"));
			if (landUserList != null && landUserList.size() > 0) {
				for (BigInteger landUser : landUserList) {
					NotificationDTO notificationDtoForLand = new NotificationDTO();
					notificationDtoForLand.setNotification(
							"Form M has been evaluated for " + entity.getUniqueNo() + " by Land Officer.");
					notificationDtoForLand.setUserId(landUser);
					notificationDtoForLand.setCreatedBy(landUser);
					notificationDtoForLand.setUserType("O");
					notificationDetailsServiceImpl.submitNotification(notificationDtoForLand);
				}
			}
			return result;
		} else {
			return 42;
		}
	}

	@Override
	public List<AuctionDTO> getAuctions(String data) throws JsonMappingException, JsonProcessingException {
		List<Object[]> results = repo.getAuctions(data);
		List<AuctionDTO> respones = new ArrayList<>();
		for (Object[] result : results) {
			AuctionDTO dto = new AuctionDTO();
			dto.setAuctionNumberGen((String) result[0]);
			dto.setAuctionName((String) result[1]);
			dto.setSlotForAuctionFromDate((Date) result[2]);
			dto.setSlotForAuctionToDate((Date) result[3]);
			dto.setAuctionStartTime((String) result[4]);
			dto.setAuctionEndTime((String) result[5]);
			dto.setRoyalty((BigDecimal) result[6]);
			dto.setBasePrice((BigDecimal) result[7]);
			dto.setHighestBidPrice((BigDecimal) result[8]);
			dto.setNow((String) result[9]);
			dto.setTenderAuctionId((BigInteger) result[10]);
			dto.setFormSubmitStartDate((Date) result[11]);
			dto.setFormSubmitEndDate((Date) result[12]);
			dto.setBddingNumber((BigInteger) result[13]);
			respones.add(dto);
		}
		return respones;
	}

	@Override
	public List<AuctionDetails> getAuctionDetails(BigInteger tenderId) {
		List<Object[]> results = repo.getAuctionDetails(tenderId);
		List<AuctionDetails> resultsData = new ArrayList<>();
		for (Object[] result : results) {
			AuctionDetails dto = new AuctionDetails();
			dto.setAuctionNumberGen((String) result[0]);
			dto.setAuctionName((String) result[1]);
			dto.setSource((String) result[2]);
			dto.setSlotForAuctionFromDate((Date) result[3]);
			dto.setAreaAcre((BigDecimal) result[4]);
			dto.setSlotForAuctionToDate((Date) result[5]);
			dto.setRoyality((BigDecimal) result[6]);
			dto.setCombineValueSource((String) result[7]);
			resultsData.add(dto);
		}
		return resultsData;

	}

	@Override
	public Boolean checkAuctionStatusValid(BigInteger tenderAuctionId) {
		return repo.checkAuctionStatusValid(tenderAuctionId);
	}

	@Override
	public JSONObject getAllDataFromMEvalucation(String formParams) {
		logger.info("Inside getAll method of BidderregistraraServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String approvalStatus = jsonData.getString("approvalStatus");
		Integer offset = (pageNo - 1) * pageSize;
		Integer totalDataPresent = bidderregistraraRepository.countByBitDeletedFlagFromMEvalucation(approvalStatus);
		List<Object[]> bidderregistraraResp = repo.getBidderFormApplicationData(pageSize, offset, approvalStatus);
		List<Bidderregistrara> getrecord = new ArrayList<>();
		for (Object[] result : bidderregistraraResp) {
			Bidderregistrara dto = new Bidderregistrara();
			dto.setIntId((BigInteger) result[0]);
			dto.setTxtPanNumber((String) result[1]);
			dto.setTxtAadharNumber((String) result[2]);
			dto.setUserName((String) result[3]);
			dto.setUniqueNo((String) result[4]);
			dto.setRemark((String) result[5]);
			getrecord.add(dto);
		}
		json.put("status", 200);
		json.put("result", new JSONArray(getrecord));
		json.put("count", totalDataPresent);
		return json;
	}

	@Override
	public Integer tranctionCount(BigInteger applicantId, String orderId, String paymentSignature, String paymentId,
			BigDecimal tranctionAmount) {
		String paymentStatus = null;
		if (paymentId != null) {
			paymentStatus = "Success Payment.";
		} else {
			paymentStatus = " Payment Not Success .";
		}
		return repo.insertPaymentTransaction(applicantId, orderId, paymentSignature, paymentId, paymentStatus,
				tranctionAmount, TransactionNumberGenerateHelper.createTransactionNumberForForm(applicantId));
	}

	@Override
	public BigInteger getAuctionsCount(String data) throws JsonMappingException, JsonProcessingException {
		return repo.getAuctionsCount(data);
	}

}