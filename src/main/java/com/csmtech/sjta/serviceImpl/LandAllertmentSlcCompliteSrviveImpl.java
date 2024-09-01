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

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.LandAllertmentSlcCompliteDTO;
import com.csmtech.sjta.dto.LandAllotementResponesNewDTO;
import com.csmtech.sjta.dto.LandAllotementWinnerResultDto;
import com.csmtech.sjta.entity.LandAllotementEntity;
import com.csmtech.sjta.entity.LandAllotementForAuctionEntity;
import com.csmtech.sjta.mobile.dto.ApplicationFlowDto;
import com.csmtech.sjta.mobile.service.CommonService;
import com.csmtech.sjta.repository.LandAllertmentSlcCompliteRepository;
import com.csmtech.sjta.repository.LandAllortmentRepository;
import com.csmtech.sjta.repository.LandAllotementForAuctionRepository;
import com.csmtech.sjta.repository.LandAreaStatisticsRepository;
import com.csmtech.sjta.service.LandAllertmentSlcCompliteSrvive;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LandAllertmentSlcCompliteSrviveImpl implements LandAllertmentSlcCompliteSrvive {

	@Autowired
	private LandAllertmentSlcCompliteRepository repo;

	@Autowired
	private LandAllortmentRepository landAlortmentRepo;

	@Autowired
	private LandAllotementForAuctionRepository landAuctionRepo;

	@Autowired
	private CommonService commonService;

	@Autowired
	LandAreaStatisticsRepository landGis;

	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${file.path}")
	private String finalUploadPath;

	@Override
	public List<LandAllertmentSlcCompliteDTO> getMettingCompliteSlcRecord(String formsParms) {
		log.info("inside the getSldCompliteREcord execute start");
		JSONObject jsonData = new JSONObject(formsParms);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;

		List<Object[]> getRecord = repo.getMeetingSchedulesByMeetingLevelId(pageSize, offset);
		List<LandAllertmentSlcCompliteDTO> retrnDto = new ArrayList<>();
		for (Object[] result : getRecord) {
			LandAllertmentSlcCompliteDTO dto = new LandAllertmentSlcCompliteDTO();
			dto.setMeetingId((BigInteger) result[0]);
			dto.setMeetingUniqueNo((String) result[1]);
			dto.setMeetingLevle((Short) result[2]);
			dto.setLandMeetingCount((BigInteger) result[3]);
			dto.setVenue((String) result[4]);
			dto.setMeetingDate((String) result[5]);
			dto.setMeetingPurpose((String) result[6]);
			dto.setBidderId((BigInteger) result[7]);
			dto.setLandId((BigInteger) result[8]);
			retrnDto.add(dto);
		}
		log.info("inside the getSldCompliteREcord execute end");
		return retrnDto;
	}

	@Override
	public BigInteger countMeetingSchedulesByMeetingLevelId() {
		log.info("inside the getSldCompliteREcord execute start");
		return repo.countMeetingSchedulesByMeetingLevelId();
	}

	@Override
	public List<Map<String, Object>> getDistinctPlotNumbers(String formsParm) {
		log.info("inside the getSldCompliteREcord execute start");
		JSONObject jsonData = new JSONObject(formsParm);
		// TEST PROPOUSE
//		return repo.getDistinctPlotNumbers(BigInteger.valueOf(4), "ME291023002",
//		BigInteger.valueOf(1));
		return repo.getDistinctPlotNumbers(jsonData.getBigInteger("meetingScheduleId"), jsonData.getInt("flagStatus"));
	}

	@Override
	public List<LandAllertmentSlcCompliteDTO> executeCustomQueryGetLandAllertmentAlRequiredDetails(String formsParm) {
		JSONObject jsonData = new JSONObject(formsParm);
//		List<Object[]> result = repo.executeCustomQueryGetLandAllertmentAlRequiredDetails(
//				jsonData.getString("plotNo"), jsonData.getBigInteger("meetingLevelId"),
//				jsonData.getBigInteger("meetingScheduleId"));
		// TEST PROPOUSE
//		List<Object[]> result = repo.executeCustomQueryGetLandAllertmentAlRequiredDetails(
//				"201", BigInteger.valueOf(4),
//				BigInteger.valueOf(1));
		log.info("inside the getSldCompliteREcord execute end");
		List<Object[]> result = repo.executeCustomQueryGetLandAllertmentAlRequiredDetails(jsonData.getString("plotNo"),
				jsonData.getBigInteger("meetingScheduleId"), jsonData.getInt("flagStatus"));
		Integer flag = jsonData.getInt("flagStatus");

		List<LandAllertmentSlcCompliteDTO> respones = new ArrayList<>();
		if (flag == 1) {
			for (Object[] row : result) {
				LandAllertmentSlcCompliteDTO dto = new LandAllertmentSlcCompliteDTO();
				dto.setLandAppId((BigInteger) row[0]);
				dto.setCombineName((String) row[1]);
				dto.setTotalArea((String) row[2]);
				dto.setPurchaseArea((String) row[3]);
				dto.setPricePerAcer((String) row[4]);
				dto.setTotalPrice((String) row[5]);
				respones.add(dto);
			}
		} else {
			for (Object[] row : result) {
				LandAllertmentSlcCompliteDTO dto = new LandAllertmentSlcCompliteDTO();
				dto.setMeetingId((BigInteger) row[0]);
				dto.setLandAppId((BigInteger) row[1]);
				dto.setMeetingUniqueNo((String) row[2]);
				dto.setPlotNo((String) row[3]);
				dto.setApplicantName((String) row[4]);
				dto.setPricePerAcer((String) row[5]);
				dto.setTotalArea((String) row[6]);
				dto.setPurchaseArea((String) row[7]);
				dto.setTotalPrice((String) row[8]);
				dto.setCombineName((String) row[9]);
				dto.setPlotCode((String) row[10]);
				respones.add(dto);
			}
		}
		return respones;
	}

	@Override
	public JSONObject landAllortmrntSaveRecord(String data) {
		log.info("inside the landAllortmrntSaveRecord execute start");
		JSONObject js = new JSONObject();
		Integer count = 0;
		try {
			ObjectMapper om = new ObjectMapper();
			LandAllotementEntity registerData = om.readValue(data, LandAllotementEntity.class);
			String fileUploadList = registerData.getFileDocument();
			if (fileUploadList != null) {
				if (!fileUploadList.equals("")) {
					File f = new File(tempUploadPath + fileUploadList);
					if (f.exists()) {
						File src = new File(tempUploadPath + fileUploadList);
						File dest = new File(finalUploadPath + "/" + fileUploadList);
						if (!dest.getParentFile().exists()) {
							try {
								Files.createDirectories(dest.getParentFile().toPath());
							} catch (IOException e) {
								log.error("error occured,  while creating folder {}", e.getMessage());
							}
						}
						try {
							Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
							Files.delete(src.toPath());
							count++;
						} catch (IOException e) {
							log.error(e.getMessage());
						}
					}
				}

			} else {
				js.put("error", "error in file insert");
			}

			String plotCode = "";

			if (registerData.getFlagStatus() == 1) {
				if (count > 0) {
					plotCode = registerData.getSelPlotNo();
					// insert the auction part
					BigInteger id = repo.insertLandAllotment(registerData);
					Integer countUpdate = repo.updateformFlag(id, registerData.getMeetingScheduleId(),
							registerData.getSelPlotNo());
					if (countUpdate > 0) {
						js.put("landAllortmrntId", id);
					}
				}
			} else {
				if (!Objects.isNull(registerData.getLandAllotmentId())
						&& registerData.getLandAllotmentId().compareTo(BigInteger.ZERO) > 0) {
					// update case
				} else {
					// insert the land allotment general
					if (count > 0) {
						LandAllotementEntity saveData = landAlortmentRepo.save(registerData);
						Integer countUpdate = repo.updateformFlag(saveData.getLandAllotmentId(),
								registerData.getMeetingScheduleId(), saveData.getSelPlotNo());

						plotCode = saveData.getSelPlotNo();
						if (countUpdate > 0) {
							js.put("landAllortmrntId", saveData.getLandAllotmentId());
						}
					}
				}
			}

			// add the application flow lines of codes for Form 16 submission
			BigInteger landApplicationId = registerData.getApplicantName();
			ApplicationFlowDto dto = new ApplicationFlowDto();
			dto.setLandApplicationId(landApplicationId != null ? landApplicationId : BigInteger.ZERO);
			// dto.setLandApplicationId(BigInteger.valueOf(approvalDto.getLandApplicantId()));
			dto.setApplicationFlowId(BigInteger.valueOf(20));
			dto.setActionDateTime(new Date());
			dto.setActionRoleId(BigInteger.valueOf(4));
			commonService.saveApplicationFlow(dto);

			landGis.updateLandGisSingle(plotCode, 4, 1);
			landGis.updateLandGisSingle(plotCode, 5, 1);

		} catch (Exception e) {
			log.error("Inside save method of BidderregistraraServiceImpl some error occur:" + e, e.getMessage());
			js.put("status", 400);
		}
		log.info("inside the landAllortmrntSaveRecord execute end");
		return js;
	}

	@Override
	public List<LandAllotementResponesNewDTO> getLandAllotmentDetails(String formdParm) {
		JSONObject jsonData = new JSONObject(formdParm);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		List<LandAllotementResponesNewDTO> getRecord = repo.getLandAllotmentDetails(pageSize, offset);
		return getRecord;
	}

	@Override
	public BigInteger countLandAlertUser() {
		log.info("inside the getSldCompliteREcord execute start");
		return repo.countLandAllortUser();
	}

	@Override
	public Integer updateLandAllotementFlag(String landAlloId) {
		JSONObject jsonData = new JSONObject(landAlloId);
		return repo.updateLandAllotementFlag(jsonData.getBigInteger("landAllotementId"));
	}

	@Override
	public JSONObject insertRecordForAuction(String data) {
		log.info("inside the landAllortmrntSaveRecord execute start");
		JSONObject js = new JSONObject();
		try {
			ObjectMapper om = new ObjectMapper();
			LandAllotementForAuctionEntity registerData = om.readValue(data, LandAllotementForAuctionEntity.class);
			if (!Objects.isNull(registerData.getLandAllotementForAuctionId())
					&& registerData.getLandAllotementForAuctionId().compareTo(BigInteger.ZERO) > 0) {
				// update case
			} else {
				LandAllotementForAuctionEntity saveData = landAuctionRepo.save(registerData);
				js.put("landAllortmrntId", saveData.getLandAllotementForAuctionId());
			}
		} catch (Exception e) {
			log.error("Inside save method of BidderregistraraServiceImpl some error occur:" + e, e.getMessage());
			js.put("status", 400);
		}
		log.info("inside the landAllortmrntSaveRecord execute end");
		return js;
	}

	@Override
	public List<LandAllotementForAuctionEntity> gteRecordGoForAuction(String formdParm) {
		JSONObject jsonData = new JSONObject(formdParm);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		List<LandAllotementForAuctionEntity> getRecord = landAuctionRepo.getMultiRecord(pageSize, offset);
		return getRecord;
	}

	@Override
	public BigInteger getGoForAuctionCount() {
		log.info("inside the getSldCompliteREcord execute start");
		return repo.goForAuctionCount();
	}

	@Override
	public Integer updateGoForAucton(String landAlloId) {
		JSONObject jsonData = new JSONObject(landAlloId);
		return repo.updateGoForAyction(jsonData.getBigInteger("landAllotementId"));
	}

	@Override
	public Integer updateLandAllotementFrom16Record(String formsParms) {
		JSONObject jsonData = new JSONObject(formsParms);
		String fileUploadList = jsonData.getString("fileDocument");
		if (fileUploadList != null) {
			if (!fileUploadList.equals("")) {
				File f = new File(tempUploadPath + fileUploadList);
				if (f.exists()) {
					File src = new File(tempUploadPath + fileUploadList);
					File dest = new File(finalUploadPath + "/" + fileUploadList);
					if (!dest.getParentFile().exists()) {
						try {
							Files.createDirectories(dest.getParentFile().toPath());
						} catch (IOException e) {
							log.error("error occured,  while creating folder {}", e.getMessage());
						}
					}
					try {
						Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
						Files.delete(src.toPath());
					} catch (IOException e) {
						log.error(e.getMessage());
					}
				}
			}
		}

		return repo.updateLandAllotementFrom16Record(jsonData.getBigInteger("landAllotementId"),
				jsonData.getString("fileDocument"), jsonData.getString("actionRemarks"));
	}

	@Override
	public List<LandAllotementWinnerResultDto> getLandAllotmentWnnerDetails(String formsParms) {
		JSONObject jsonData = new JSONObject(formsParms);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		return repo.getLandAllotmentWnnerDetails(jsonData.getBigInteger("createdBy"), pageSize, offset);
	}

	@Override
	public BigDecimal getLandAllotmentWnnerDetailsCount(String formsParms) {
		JSONObject jsonData = new JSONObject(formsParms);
		return repo.getLandAllotmentWnnerDetailsCount(jsonData.getBigInteger("createdBy"));
	}

}
