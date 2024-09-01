package com.csmtech.sjta.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.AuctionPlotDTO;
import com.csmtech.sjta.dto.AuctionPlotIdDetrailsDTO;
import com.csmtech.sjta.dto.AuctionPlotIdDetrailsMainDTO;
import com.csmtech.sjta.dto.AuctionPlotSingleDTO;
import com.csmtech.sjta.dto.PlotDetails;
import com.csmtech.sjta.dto.SourceCreationPlotSecondProcessDTO;
import com.csmtech.sjta.dto.SourceCreationPlotsRecord;
import com.csmtech.sjta.dto.SourcerCreationDto;
import com.csmtech.sjta.mobile.dto.ApplicationFlowDto;
import com.csmtech.sjta.mobile.repository.ApplicationFlowRepository;
import com.csmtech.sjta.mobile.service.CommonService;
import com.csmtech.sjta.repository.MeetingScheduleNativeRepository;
import com.csmtech.sjta.repository.SourceCreationRepository;
import com.csmtech.sjta.service.SourceCreationService;
import com.csmtech.sjta.util.CommonConstant;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unchecked")
@Service
@Slf4j
public class SourceCreationServiceImpl implements SourceCreationService {

	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${file.path}")
	private String finalUploadPath;

	@Autowired
	private SourceCreationRepository sourceCreationRepository;

	@Autowired
	private CommonService commonService;

	@Autowired
	ApplicationFlowRepository applicationFlowRepo;

	JSONObject resp = new JSONObject();

	@Autowired
	private MeetingScheduleNativeRepository meetingScheduleRepo;

	@Override
	public Integer save(SourcerCreationDto sourcerCreationDto) {
		Integer status = 0;
		Integer count = null;
		if (sourcerCreationDto.getIntId() != null) {
			sourceCreationRepository.deleteAuctionPlotDetails(sourcerCreationDto.getIntId());
			List<PlotDetails> detailsList = sourcerCreationDto.getPlot_details();
			BigInteger plotValdation = null;
			for (PlotDetails details : detailsList) {
				plotValdation = sourceCreationRepository.countAuctionPlotsAuctionFlagValidate(details.getPlot_id());
			}
			if (plotValdation != null && plotValdation.equals(BigInteger.valueOf(0))) {
				count = sourceCreationRepository.auctionUpdate(sourcerCreationDto);
				sourceCreationRepository.insertAuctionPlotDetails(sourcerCreationDto, sourcerCreationDto.getIntId());
			} else {
				status = 101001;
			}

			status = count;
		} else {
			BigInteger insertplotSingle = sourceCreationRepository.insertAuctionPlot(sourcerCreationDto);
			status = sourceCreationRepository.insertAuctionPlotDetails(sourcerCreationDto, insertplotSingle);
		}

		return status;
	}

	public static JSONArray fillselDistrictNameList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();

		String query = " SELECT DISTINCT dm.district_code AS district_id, dm.district_name AS district_name\r\n"
				+ "FROM land_bank.district_master dm\r\n"
				+ "JOIN land_bank.tahasil_master ki ON ki.district_code = dm.district_code\r\n"
				+ "JOIN land_bank.village_master kii ON ki.tahasil_code = kii.tahasil_code\r\n"
				+ "JOIN land_bank.khatian_information kiii ON kii.village_code = kiii.village_code\r\n"
				+ "WHERE dm.state_code = 'OD'\r\n" + "ORDER BY district_name;\r\n" + " ";

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
		String query = " select DISTINCT (ki.tahasil_code) as tehsil_id,(ki.tahasil_name) as\r\n"
				+ " tehsil_name from land_bank.tahasil_master ki\r\n"
				+ " JOIN land_bank.village_master kii ON ki.tahasil_code = kii.tahasil_code\r\n"
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

	// add here map extension
	public static JSONArray fillselMouzaList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("tehsil_id").toString();
		String query = "  Select DISTINCT (vm.village_code) as mouza_id,(vm.village_name) as mouza_name,\r\n"
				+ " CAST(ST_extent(ST_Transform(vb.geom, 3857)) AS character varying) AS extent\r\n"
				+ " from land_bank.village_master vm  \r\n"
				+ " left join land_bank.village_boundary vb on(vm.village_code=vb.village_code) \r\n"
				+ " JOIN land_bank.khatian_information kiii ON (vm.village_code = kiii.village_code)  "
				+ "where  vm.tahasil_code='" + val + "'  group by vm.village_code order by vm.village_name";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("mouza_id", data[0]);
			jsonObj.put("mouza_name", data[1]);
			jsonObj.put("extent", data[2]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselKhataNoList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("mouza_id").toString();
		String query = "Select (khatian_code) as khata_no_id,(khata_no) as khata_no "
				+ " from land_bank.khatian_information where village_code='" + val + "'";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("khata_no_id", data[0]);
			jsonObj.put("khata_no", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselPlotNoList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("khata_no_id").toString();

		String query = "SELECT DISTINCT pi.plot_code AS plot_no_id, pi.plot_no AS plot_no, pi.kissam,pi.area_acre,pi.khatian_code  "
				+ "FROM land_bank.plot_information pi  "
				+ " LEFT JOIN application.meeting_schedule_applicant msa  ON(msa.plot_no=pi.plot_code) "
				+ "	LEFT JOIN application.meeting_schedule ms ON(ms.meeting_schedule_id=msa.meeting_schedule_id)  "
				+ "WHERE pi.khatian_code ='" + val
				+ "' AND msa.plot_no IS NULL OR msa.status='1' ORDER BY pi.plot_code ";

		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("plot_code", data[0]);
			jsonObj.put("plot_no", data[1]);
			jsonObj.put("kissam", data[2]);
			jsonObj.put("area_acre", data[3].toString());
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	// Rashmi Changes
	@Override
	public List<AuctionPlotDTO> auctionPlotDetrails(String formParms) {
		JSONObject jsonData = new JSONObject(formParms);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String plotNo = jsonData.getString("plotNo");
		String auctionFlag = jsonData.getString("auctionFlag");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> result = null;
		if (!auctionFlag.equals("") || !plotNo.equals("")) {
			result = sourceCreationRepository.auctionPlotDetrailsUseLike(pageSize, offset, plotNo, auctionFlag);
		} else {
			result = sourceCreationRepository.auctionPlotDetrails(pageSize, offset);
		}

		List<AuctionPlotDTO> dtoList = new ArrayList<>();
		for (Object[] row : result) {
			AuctionPlotDTO dto = new AuctionPlotDTO();
			dto.setAuctionPlotId((BigInteger) row[0]);
			dto.setDistrictCode((String) row[2]);
			dto.setTahasilCode((String) row[3]);
			dto.setVillageCode((String) row[4]);
			dto.setKhatianCode((String) row[5]);
			dto.setAuctionFlag((Integer) row[6]);
			dto.setActionStatus((Character) row[7]);
			dto.setPlotNo((String) row[8]);
			dto.setPlotExtend((String) row[9]);
			dto.setApprovalRemerk((String) row[10]);
			dto.setDistCode((String) row[11]);
			dto.setTahaCode((String) row[12]);
			dto.setVillCode((String) row[13]);
			dtoList.add(dto);
		}
		return dtoList;

	}

	@Override
	public AuctionPlotIdDetrailsMainDTO auctionPlotIdDetrails(BigInteger auctionPlotId) {
		List<Object[]> result = sourceCreationRepository.auctionPlotIdDetrails(auctionPlotId);
		List<AuctionPlotIdDetrailsDTO> dtoList = new ArrayList<>();
		for (Object[] row : result) {
			AuctionPlotIdDetrailsDTO dto = new AuctionPlotIdDetrailsDTO();
			dto.setAuctionPlotDetailsId((BigInteger) row[0]);
			dto.setPlotCode((String) row[1]);
			dto.setPlotNo((String) row[2]);
			dto.setAreaAcre(((BigDecimal) row[4]).toString());
			dto.setKissam((String) row[3]);
			dtoList.add(dto);
		}

		List<Object[]> result2 = sourceCreationRepository.auctionPlotDetrailsThroughId(auctionPlotId);
		List<AuctionPlotSingleDTO> dtoList2 = new ArrayList<>();
		for (Object[] row : result2) {
			AuctionPlotSingleDTO dto = new AuctionPlotSingleDTO();
			dto.setAuctionPlotId((BigInteger) row[0]);
			dto.setDistrictName((String) row[1]);
			dto.setTahasilName((String) row[2]);
			dto.setVillageName((String) row[3]);
			dto.setKhatianName((String) row[4]);
			dto.setDistrictCode((String) row[5]);
			dto.setTahasilCode((String) row[6]);
			dto.setVillageCode((String) row[7]);
			dto.setKhatianCode((String) row[8]);
			dto.setApprovalStatus((Character) row[9]);
			dtoList2.add(dto);
		}

		return new AuctionPlotIdDetrailsMainDTO(dtoList, dtoList2);
	}

	@Override
	public Integer softDeleteAuctionDetails(BigInteger initId) {
		Integer updatePlot = sourceCreationRepository.markAuctionPlotAsDeleted(initId);
		Integer updatePlotDetails = sourceCreationRepository.deleteAuctionPlotDetails(initId);
		Integer countMeetingUpdate = null;
		BigInteger getMeetingId = sourceCreationRepository.gerAuctionThroughMeetingId(initId);
		if (getMeetingId != null) {
			countMeetingUpdate = sourceCreationRepository.updateDeletedFladForTheMeetings(getMeetingId);
		}
		Integer totalCount = updatePlot + updatePlotDetails;
		return totalCount;
	}

	@Override
	public Integer updateAddAuctionDocument(BigInteger auctionPlotId, String auctionDocumen) {
		Integer count = null;
		if (auctionDocumen != null) {
			List<String> fileUploadList = new ArrayList<>();
			fileUploadList.add(auctionDocumen);
			count = sourceCreationRepository.updateAddAuctionPlotDetails(auctionPlotId, fileUploadList.get(0));
			try {
				for (String fileUpload : fileUploadList) {
					if (!fileUpload.equals("")) {
						File src = new File(tempUploadPath + fileUpload);
						if (src.exists()) {
							File dest = new File(finalUploadPath + "/" + fileUpload);
							try {
								Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
								Files.delete(src.toPath());
							} catch (IOException e) {
							}
						} else {
							log.info("File does not exist at: ");
						}
					}
				}

				List<BigInteger> landAppId = applicationFlowRepo.findLandAppIds(auctionPlotId.intValue());

				for (BigInteger appId : landAppId) {
					ApplicationFlowDto dto = new ApplicationFlowDto();
					dto.setLandApplicationId(appId != null ? appId : BigInteger.ZERO);
					dto.setApplicationFlowId(BigInteger.valueOf(24));
					dto.setActionDateTime(new Date());
					dto.setActionRoleId(BigInteger.valueOf(4));
					commonService.saveApplicationFlow(dto);
				}

			} catch (Exception e) {
				log.info("inside exception  in Land_documentsServiceImpl !!" + e.getMessage());
			}
		}
		return count;
	}

	@Override
	public List<AuctionPlotDTO> auctionPlotDetrailsSelectAuctionFlag(String formParms) {
		JSONObject jsonData = new JSONObject(formParms);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String approvalStatus = jsonData.getString("approvalStatus");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> result = sourceCreationRepository.auctionPlotDetrailsViewByAuctionFlag(pageSize, offset,
				approvalStatus);
		List<AuctionPlotDTO> dtoList = new ArrayList<>();
		for (Object[] row : result) {
			AuctionPlotDTO dto = new AuctionPlotDTO();
			dto.setAuctionPlotId((BigInteger) row[0]);
			dto.setDistrictCode((String) row[2]);
			dto.setTahasilCode((String) row[3]);
			dto.setVillageCode((String) row[4]);
			dto.setKhatianCode((String) row[5]);
			dto.setAuctionFlag((Integer) row[6]);
			dto.setPlotNo((String) row[7]);
			dto.setApprovalRemerk((String) row[8]);
			dtoList.add(dto);
		}
		return dtoList;

	}

	@Override
	public Integer updateDeputyOfficerAction(Integer auctionPlotId, String status, String remark) {
		Integer auctionAction = sourceCreationRepository.updateAuctionPlot(auctionPlotId, status, remark);
		List<BigInteger> landAppId = applicationFlowRepo.findLandAppIds(auctionPlotId);

		// application flow

		for (BigInteger appId : landAppId) {
			ApplicationFlowDto dto = new ApplicationFlowDto();
			dto.setLandApplicationId(appId != null ? appId : BigInteger.ZERO);
			dto.setApplicationFlowId(BigInteger.valueOf(25));
			dto.setActionDateTime(new Date());
			dto.setActionRoleId(BigInteger.valueOf(5));
			commonService.saveApplicationFlow(dto);
		}
		return auctionAction;
	}

	@Override
	public BigInteger countAuctionPlotsAuctionFlagValidate(String plotCode) {
		BigInteger plotValdation = sourceCreationRepository.countAuctionPlotsAuctionFlagValidate(plotCode);
		return plotValdation;
	}

	@Override
	public List<SourceCreationPlotsRecord> getSelectedPlotsInfo(String formsParm) {
		JSONObject jsonData = new JSONObject(formsParm);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		return sourceCreationRepository.getSelectedPlotsInfo(pageSize, offset);
	}

	@Override
	public BigInteger getSelectedPlotsInfoCount() {
		return sourceCreationRepository.getSelectedPlotsInfoCount();
	}

	@Override
	public BigInteger getCountAuctionRecord() {
		return sourceCreationRepository.countAuctionPlots();
	}

	@Override
	public BigInteger countForAuctionApprovalRecord(String parms) {
		JSONObject jsonData = new JSONObject(parms);
		return sourceCreationRepository.countAuctionPlotsAuctionFlag(jsonData.getString("approvalStatus"));
	}

	@Override
	public BigInteger getCountAuctionRecordUseLike(String formParms) {
		JSONObject jsonData = new JSONObject(formParms);
		String plotNo = jsonData.getString("plotNo");
		String auctionFlag = jsonData.getString("auctionFlag");
		return sourceCreationRepository.countAuctionPlotsLikeCont(plotNo, auctionFlag);
	}

	@Override
	public JSONObject saveSourceSecondProcess(String data) {
		log.info("Inside  saveSourceSecondProcess");
		try {
			ObjectMapper om = new ObjectMapper();
			SourceCreationPlotSecondProcessDTO sourceDetails = om.readValue(data,
					SourceCreationPlotSecondProcessDTO.class);
			if (sourceDetails.getIntId() != null && sourceDetails.getIntId().compareTo(BigInteger.valueOf(0)) > 0) {
				// update part
			} else {
				// insert part
				BigInteger insertMeeting = sourceCreationRepository.createMeeting(sourceDetails.getCreatedBy());
				BigInteger InsertMeetingShedule = null;
				Integer countMeetingApplicant = 0;
				BigInteger insertPlotRecord = null;
				Integer countplotsDetails = 0;
				if (insertMeeting != null) {
					InsertMeetingShedule = sourceCreationRepository.createMeetingSchedule(sourceDetails.getVenue(),
							sourceDetails.getTxtMeetingDate53(), sourceDetails.getTxtrMeetingPurpose54(),
							sourceDetails.getSelMeetingLevel55(), sourceDetails.getCreatedBy(), insertMeeting);
				} else {
					resp.put(CommonConstant.STATUS_KEY, 501); // some error occur try after some time
				}
				if (InsertMeetingShedule != null) {
					// insert the meeting plots like applicant use the loop bcz multiple plot are
					// come
					List<PlotDetails> getPlotsData = sourceDetails.getPlotDetails();
					for (PlotDetails plot : getPlotsData) {
						Integer countPlotsInsert = sourceCreationRepository.createMeetingScheduleApplicant(
								InsertMeetingShedule, sourceDetails.getCreatedBy(), plot.getPlot_id());
						countMeetingApplicant++;
						log.info("execute and insert the records" + countPlotsInsert);
					}
				} else {
					countMeetingApplicant = 0;
					resp.put(CommonConstant.STATUS_KEY, 501);
				}
				if (countMeetingApplicant > 0) {
					// insert the plot_details source create first table
					insertPlotRecord = sourceCreationRepository.createAuctionPlot(sourceDetails.getSelDistrictName(),
							sourceDetails.getSelTehsilName(), sourceDetails.getSelMouza(),
							sourceDetails.getSelKhataNo(), sourceDetails.getCreatedBy());
				} else {
					// any problem are occur
					resp.put(CommonConstant.STATUS_KEY, 501);
				}
				if (insertPlotRecord != null && insertPlotRecord.compareTo(BigInteger.valueOf(0)) > 0) {
					// insert plot_details record multiple plots
					List<PlotDetails> getPlotsData = sourceDetails.getPlotDetails();
					for (PlotDetails plot : getPlotsData) {
						// insert multiple plots
						BigInteger auctionPlotId = sourceCreationRepository.createAuctionPlotDetails(insertPlotRecord,
								plot.getPlot_id(), sourceDetails.getCreatedBy(), InsertMeetingShedule);
						countplotsDetails++;
						log.info("insert the plot count " + auctionPlotId);
					}
					// hear the return insert success pass 200

					List<Integer> selectedCCOfficers = Arrays.asList(sourceDetails.getSelectedCCOfficers());
					List<Integer> selectedBCCOfficers = Arrays.asList(sourceDetails.getSelectedBCCOfficers());

					if (!selectedCCOfficers.isEmpty()) {
						// hear add the CC officer optional
						for (int i = 0; i < selectedCCOfficers.size(); i++) {
							Integer ccCount = sourceCreationRepository.createMeetingScheduleMailCc(InsertMeetingShedule,
									selectedCCOfficers.get(i), sourceDetails.getCreatedBy());
							log.info("get insert officer" + ccCount);
						}
					}
					if (!selectedBCCOfficers.isEmpty()) {
						// hear add the BCC officer optional
						for (int i = 0; i < selectedBCCOfficers.size(); i++) {
							Integer bccCount = sourceCreationRepository.createMeetingScheduleMailBcc(
									InsertMeetingShedule, selectedBCCOfficers.get(i), sourceDetails.getCreatedBy());
							log.info("add bcc oficer" + bccCount);
						}
					}
					String meetingGenerateNo = MeetingScheduleServiceImpl.generateApplicantUniqueNumber("ME");
					Integer integerValue = InsertMeetingShedule.intValue();
					String updatemeetingNo = meetingScheduleRepo.updateMeetingNo(integerValue, meetingGenerateNo);
					List<Object[]> meetingNo = meetingScheduleRepo.getMeetingNoById(integerValue);
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

					log.info(updatemeetingNo);
					resp.put(CommonConstant.STATUS_KEY, 200);
					resp.put("meetingNo", meetingnoRec.get(0));
					resp.put("meetingDate", meetingDates.get(0));
					resp.put("venue", venues.get(0));
					resp.put("id", InsertMeetingShedule);

				} else {
					// any problem are occur
					resp.put(CommonConstant.STATUS_KEY, 501);
				}
			}
		} catch (Exception e) {
			log.error("Inside saveSourceSecondProcess, an error occurred: {}", e.getMessage());
			resp.put(CommonConstant.STATUS_KEY, 400);
		}
		return resp;
	}

}
