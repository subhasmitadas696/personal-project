package com.csmtech.sjta.mobile.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.GenericResponse;
import com.csmtech.sjta.dto.LandAppResponseStructureDTO;
import com.csmtech.sjta.dto.LandApplicantDTO;
import com.csmtech.sjta.dto.LandPlotViewDTO;
import com.csmtech.sjta.dto.NotificationDTO;
import com.csmtech.sjta.dto.PlotValuationDTO;
import com.csmtech.sjta.entity.Land_applicant;
import com.csmtech.sjta.mobile.dto.ApplicationFlowDto;
import com.csmtech.sjta.mobile.dto.ImageRequestDto;
import com.csmtech.sjta.mobile.dto.LandDetailsResponseDto;
import com.csmtech.sjta.mobile.dto.LandVerificationResponseDto;
import com.csmtech.sjta.mobile.dto.PlotLandInspectionDto;
import com.csmtech.sjta.mobile.dto.VillageDTO;
import com.csmtech.sjta.mobile.entity.PlotLandInspectionEntity;
import com.csmtech.sjta.mobile.repository.LandInspectionRepository;
import com.csmtech.sjta.mobile.repository.NotificationDetailsRepository;
import com.csmtech.sjta.mobile.repository.TahasilPlotSurveyRepository;
import com.csmtech.sjta.mobile.repository.VillageDetailsRepository;
import com.csmtech.sjta.repository.LandApplicantJPARepository;
import com.csmtech.sjta.repository.LandApplicantNativeRepository;
import com.csmtech.sjta.util.CommonConstant;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LandOfficerVerificationServiceImpl implements LandOfficerVerificationService {

	@Autowired
	LandApplicantJPARepository landApplicantRepository;

	@Autowired
	TahasilServiceImpl tahasilService;

	@Autowired
	LandApplicantNativeRepository landApplicantNativeRepository;

	@Autowired
	LandInspectionRepository landInspectionRepository;

	@Autowired
	GrievanceMobileServiceImpl grievanceMobileServiceImpl;

	@Autowired
	TahasilPlotSurveyRepository tahasilPlotSurveyRepository;

	@Autowired
	VillageDetailsRepository villageRepository;
	
	@Autowired
	NotificationDetailsRepository notificationDetailsRepo;
	
	@Autowired
	NotificationDetailsServiceImpl notificationDetailsServiceImpl;
	
	@Autowired
	LandApplicantJPARepository landApplicantJpaRepository;

	@Autowired
	CommonService commonService;

	@Value("${file.path}")
	private String finalUploadPath;

	@Override
	public LandVerificationResponseDto assignToCO(PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {
			PlotLandInspectionEntity plotEntity1 = landInspectionRepository.findByPlotCode(landDto.getPlotCode());
			if (plotEntity1 != null) {
				response.setStatus(210);
				response.setMessage("duplicate plot has been assigned");
			} else {
				Date parsedDate1 = new Date();
				if (landDto.getScheduledInspectionDate() != null || !landDto.getScheduledInspectionDate().isEmpty()) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					try {
						parsedDate1 = dateFormat.parse(landDto.getScheduledInspectionDate());
					} catch (ParseException e) {
						log.error("scheduled inspection date error: " + e.getMessage());
					}
				}

				int i = landInspectionRepository.updateAssignStatusToCO(landDto.getDistrictCode(),
						landDto.getTahasilCode(), landDto.getVillageCode(), landDto.getKhatianCode(),
						landDto.getPlotCode(), parsedDate1);
				if (i > 0) {
					String lastInsertId = landInspectionRepository.getLastInsertId().toString();

					int j = landInspectionRepository.updateLandAppToCO(new BigInteger(lastInsertId),
							new BigInteger(landDto.getApplicationId()), new BigInteger(landDto.getCreatedBy()));
					if (j > 0) {
						response.setStatus(200);
						response.setMessage("Assigned to CO successfully");
					}
				}
				
				
				
	//			if(landDto.getCreatedBy() != null && !landDto.getCreatedBy().isEmpty()) {
					String token = landInspectionRepository.fetchFcmToken();
					if(token != null && !token.isEmpty()) {
						response.setFcmToken(token);
					}
		//		}
				
				// application changes
				if(landDto.getApplicationId() != null) {
					applicationFlow(new BigInteger(landDto.getApplicationId()), BigInteger.valueOf(13), new Date(),
							BigInteger.valueOf(4));
				}else {
					applicationFlow(BigInteger.ZERO, BigInteger.valueOf(13), new Date(), BigInteger.valueOf(4));
				}
				
	
				//notification part
				//for manual notification Assign to CO by Land Officer for Land Verification and notify to CO,Citizen and Land Officer
				// and Assign to Tahasildar for Land Verification by Land Officer for Land Verification and notify to CO,Citizen and Land Officer
				Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(new BigInteger(landDto.getApplicationId()).intValue());
				//for co
				List<BigInteger> coUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("11"));
				List<BigInteger> landUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("4"));
				if(coUserList != null && coUserList.size() > 0) {
					for(BigInteger coUser:coUserList) {
						NotificationDTO notificationDtoForLand = new NotificationDTO();
						notificationDtoForLand.setNotification("Land Application "+entity1.getApplicantNo() +"has been assigned to CO for verification." );
						notificationDtoForLand.setUserId(coUser);
						notificationDtoForLand.setCreatedBy(coUser);
						notificationDtoForLand.setUserType("O");
						notificationDetailsServiceImpl.submitNotification(notificationDtoForLand);
					}
				}
				
				//for citizen 
				NotificationDTO notificationDtoForCitizen = new NotificationDTO();
				notificationDtoForCitizen.setNotification("Land Application "+entity1.getApplicantNo() +"has been assigned to CO & Tahasildar for verification." );
				notificationDtoForCitizen.setUserId(new BigInteger(entity1.getIntCreatedBy().toString()));
				notificationDtoForCitizen.setCreatedBy(new BigInteger(entity1.getIntCreatedBy().toString()));
				notificationDtoForCitizen.setUserType("CI");
				notificationDetailsServiceImpl.submitNotification(notificationDtoForCitizen);
				//for LO
				if(landUserList != null && landUserList.size() > 0) {
					for(BigInteger landUser:landUserList) {
						NotificationDTO notificationDtoForLand = new NotificationDTO();
						notificationDtoForLand.setNotification("Land Application "+entity1.getApplicantNo() +"has been assigned to CO & Tahasildar for verification." );
						notificationDtoForLand.setUserId(landUser);
						notificationDtoForLand.setCreatedBy(landUser);
						notificationDtoForLand.setUserType("O");
						notificationDetailsServiceImpl.submitNotification(notificationDtoForLand);
					}
				}
				

			}

			log.info("end of method");

		} catch (Exception e) {
			log.error("error while persisting data into db: " + e.getMessage());
			response.setStatus(500);
			response.setMessage("error while persisting data into db");
		}

		return response;
	}

	public List<PlotLandInspectionDto> mappingForResult(List<Object[]> idList) {
		return idList.stream().map(objects -> {
			PlotLandInspectionDto dto = new PlotLandInspectionDto();
			dto.setLandApplicationId(objects[0] != null ? (BigInteger) objects[0] : BigInteger.ZERO);
			return dto;
		}).collect(Collectors.toList());
		// TODO Auto-generated method stub
//		return null;
	}

	@Override
	public LandVerificationResponseDto assignToCONr(PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {
			Object count = landInspectionRepository.checkDuplicateData(
					BigInteger.valueOf(landDto.getPlotLandInspectionId()), new BigInteger(landDto.getApplicationId()));
			if (count.toString().equals("0")) {
				int i = landInspectionRepository.updateLandAppToCO(
						BigInteger.valueOf(landDto.getPlotLandInspectionId()),
						new BigInteger(landDto.getApplicationId()), new BigInteger(landDto.getCreatedBy()));
				if (i > 0) {
					response.setStatus(200);
					response.setMessage("Assigned to CO successfully");

				}
			}

		} catch (Exception e) {
			log.error("error while persisting data into db: " + e.getMessage());
			response.setStatus(500);
			response.setMessage("error while persisting data into db");
		}

		return response;
	}

	@Override
	public LandVerificationResponseDto coSubmitInspection(PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {

			PlotLandInspectionEntity entity = landInspectionRepository.findByPlotCode(landDto.getPlotCode());
			Integer landId = entity.getPlotLandInspectionId().intValue();
			String fileName = "";
			if (landDto.getCoUploadedPhoto() != null && !landDto.getCoUploadedPhoto().isEmpty()
					&& !landDto.getCoUploadedPhoto().trim().isEmpty()) {
				fileName = entity.getPlotLandInspectionId() + "-LandCOSurvey" + ".jpg";
			}
			landDto.setFileName(fileName);
			log.info("entity data: " + entity);
			Date parsedDate = new Date();
			if (landDto.getInspectionDate() != null || !landDto.getInspectionDate().isEmpty()) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				try {
					parsedDate = dateFormat.parse(landDto.getInspectionDate());
				} catch (ParseException e) {
					log.error("inspection date error: " + e.getMessage());
				}
			}
			landDto.setPlotLandInspectionId(landId);
			int status = 1;
			int count = landInspectionRepository.coSubmitInspection(landDto.getCoRemarks(), fileName, parsedDate,
					landId, landDto.getLatitude(), landDto.getLongitude(), status);
			if (count > 0) {
				response.setStatus(200);
				response.setMessage("CO inspection data saved successfully");
			}
			if (landDto.getCoUploadedPhoto() != null && !landDto.getCoUploadedPhoto().isEmpty()
					&& !landDto.getCoUploadedPhoto().trim().isEmpty()) {
				GenericResponse resp = saveFileTodirectory(landDto, "COORDINATE OFFICER");
				if (resp.getMessage() != null) {
					response.setMessage(resp.getMessage());
					response.setStatus(32);
				}
			}

			// application changes
			List<Object[]> idList = landInspectionRepository.fetchLandApplicationIDs(landDto.getPlotCode());
			List<PlotLandInspectionDto> dtoList = new ArrayList<>();
			dtoList = mappingForResult(idList);

			log.info("checking flow : " + idList + " and dtoMapped: " + dtoList);
			if (idList != null && !idList.isEmpty()) {
				for (PlotLandInspectionDto dto : dtoList) {
					log.info("checking flow for applicationId : " + dto.getLandApplicationId());
					applicationFlow(dto.getLandApplicationId(), BigInteger.valueOf(14), new Date(),
							BigInteger.valueOf(11));
					
					//notification part
					Land_applicant entity1 =  landApplicantJpaRepository.findByIntId(dto.getLandApplicationId().intValue());
					List<BigInteger> landUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("4"));
					if(landUserList != null && landUserList.size() > 0) {
						for(BigInteger landUser:landUserList) {
							NotificationDTO notificationDtoForLand = new NotificationDTO();
							notificationDtoForLand.setNotification("Land verification completed for land Application :" +entity1.getApplicantNo());
							notificationDtoForLand.setUserId(landUser);
							notificationDtoForLand.setCreatedBy(landUser);
							notificationDtoForLand.setUserType("O");
							notificationDetailsServiceImpl.submitNotification(notificationDtoForLand);
						}
					}
				}
			} else {
				applicationFlow(BigInteger.ZERO, BigInteger.valueOf(14), new Date(), BigInteger.valueOf(11));
			}
			
			


//			List<Object[]> idList = landInspectionRepository.fetchLandApplicationIDs(landDto.getPlotCode());
//			
//			
//			for(Object landApplicationId : idList) {
//				applicationFlow((BigInteger)landApplicationId,BigInteger.valueOf(14),new Date(),BigInteger.valueOf(4));
//			}

		} catch (Exception e) {
			log.error("error while saving CO inspection data: " + e.getMessage());
			response.setStatus(500);
			response.setMessage("error while saving CO inspection data");
		}
		return response;
	}

	@Override
	public JSONObject fetchDetailsById(LandApplicantDTO landDto) {
		JSONObject json = new JSONObject();
		Integer landId = landDto.getLandApplicantId().intValue();
		// Land_applicant findByIntId
		Land_applicant result = landApplicantRepository.findByIntId(landId);
		json.put("status", 200);
		json.put("message", "Data fetched successfully");
		json.put("result", result);
		return json;
	}

	@Override
	public LandVerificationResponseDto fetchPendingAndCompleteCount() {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		List<Object[]> pendingList = landInspectionRepository.findByNullCoRemarks();
		List<Object[]> completeList = landInspectionRepository.findByCoRemarks();
		response.setLandApplicationPendingCount(pendingList.size());
		response.setLandApplicationCompleteCount(completeList.size());
		response.setStatus(200);
		response.setMessage("Fetching records of Pending and Completed land application");
		return response;
	}

	@Override
	public LandVerificationResponseDto fetchPendingRecordsByVillage(PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		List<PlotLandInspectionDto> dtos = new ArrayList<>();
		List<Object[]> pendingList = landInspectionRepository.findByNullCoRemarks(landDto.getVillageCode());
		if (pendingList != null && !pendingList.isEmpty()) {
			dtos = mapToPlotDto(pendingList);
			response.setStatus(200);
			response.setMessage("Fetching records of Pending land application by village");
			response.setStatusMessage(CommonConstant.SUCCESS);
			response.setLandDetailResponse(dtos);

		} else {
			response.setStatus(200);
			response.setMessage("No records available of Pending land application by village");
			response.setStatusMessage(CommonConstant.FAILED);
		}

		return response;
	}

	@Override
	public LandVerificationResponseDto fetchPendingRecords() {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		List<PlotLandInspectionDto> dtos = new ArrayList<>();
		List<Object[]> pendingList = landInspectionRepository.findByNullCoRemarks();
		if (pendingList != null && !pendingList.isEmpty()) {
			dtos = mapToPlotDto(pendingList);
			response.setStatus(200);
			response.setMessage("Fetching records of Pending land application");
			response.setStatusMessage(CommonConstant.SUCCESS);
			response.setLandDetailResponse(dtos);

		} else {
			response.setStatus(200);
			response.setMessage("No records available of Pending land application");
			response.setStatusMessage(CommonConstant.FAILED);
		}

		return response;
	}

	public LandDetailsResponseDto replaceNullWithDefault(LandAppResponseStructureDTO dto) {
		List<LandPlotViewDTO> plotDtoList = new ArrayList<>();
		LandDetailsResponseDto responseDto = new LandDetailsResponseDto();
		if (dto.getAppdto() != null) {
			responseDto.setApplicantName(
					(dto.getAppdto().getApplicantName() != null && !dto.getAppdto().getApplicantName().isEmpty()
							&& !dto.getAppdto().getApplicantName().trim().isEmpty())
									? dto.getAppdto().getApplicantName()
									: "");
			responseDto.setApplicantNo(
					(dto.getAppdto().getApplicantNo() != null && !dto.getAppdto().getApplicantNo().isEmpty()
							&& !dto.getAppdto().getApplicantNo().trim().isEmpty()) ? dto.getAppdto().getApplicantNo()
									: "");
//			responseDto.setAssignStatus(dto.getAppdto().getAssignStatus()!= null? dto.getAppdto().getAssignStatus().toString():"0");
			responseDto.setFatherName(
					(dto.getAppdto().getFatherName() != null && !dto.getAppdto().getFatherName().isEmpty()
							&& !dto.getAppdto().getFatherName().trim().isEmpty()) ? dto.getAppdto().getFatherName()
									: "");
			responseDto.setEmailAddress(
					(dto.getAppdto().getEmailAddress() != null && !dto.getAppdto().getEmailAddress().isEmpty()
							&& !dto.getAppdto().getEmailAddress().trim().isEmpty()) ? dto.getAppdto().getEmailAddress()
									: "");
			responseDto.setCurrStateId(
					(dto.getAppdto().getCurrStateId() != null && !dto.getAppdto().getCurrStateId().isEmpty())
							? dto.getAppdto().getCurrStateId()
							: "");
			responseDto.setCurrDistrictId(
					(dto.getAppdto().getCurrDistrictId() != null ? dto.getAppdto().getCurrDistrictId() : ""));
			responseDto
					.setCurrBlockId((dto.getAppdto().getCurrBlockId() != null ? dto.getAppdto().getCurrBlockId() : ""));
			responseDto.setCurrGpId((dto.getAppdto().getCurrGpId() != null ? dto.getAppdto().getCurrGpId() : ""));
			responseDto.setCurrVillageId(
					(dto.getAppdto().getCurrVillageId() != null ? dto.getAppdto().getCurrVillageId() : ""));
			responseDto.setCurrPoliceStation(
					(dto.getAppdto().getCurrPoliceStation() != null ? dto.getAppdto().getCurrPoliceStation() : ""));
			responseDto.setCurrPostOffice(
					(dto.getAppdto().getCurrPostOffice() != null ? dto.getAppdto().getCurrPostOffice() : ""));
			responseDto.setCurrStreetNo(
					(dto.getAppdto().getCurrStreetNo() != null ? dto.getAppdto().getCurrStreetNo() : ""));
			responseDto
					.setCurrHouseNo((dto.getAppdto().getCurrHouseNo() != null ? dto.getAppdto().getCurrHouseNo() : ""));
			responseDto
					.setCurrPinCode((dto.getAppdto().getCurrPinCode() != null ? dto.getAppdto().getCurrPinCode() : ""));
			responseDto.setPreStateId((dto.getAppdto().getPreStateId() != null ? dto.getAppdto().getPreStateId() : ""));
			responseDto.setPreDistrictId(
					(dto.getAppdto().getPreDistrictId() != null ? dto.getAppdto().getPreDistrictId() : ""));
			responseDto.setPreBlockId((dto.getAppdto().getPreBlockId() != null ? dto.getAppdto().getPreBlockId() : ""));
			responseDto.setPreGpId((dto.getAppdto().getPreGpId() != null ? dto.getAppdto().getPreGpId() : ""));
			responseDto.setPreVillageId(
					(dto.getAppdto().getPreVillageId() != null ? dto.getAppdto().getPreVillageId() : ""));
			responseDto.setPrePoliceStation(
					(dto.getAppdto().getPrePoliceStation() != null ? dto.getAppdto().getPrePoliceStation() : ""));
			responseDto.setPrePostOffice(
					(dto.getAppdto().getPrePostOffice() != null ? dto.getAppdto().getPrePostOffice() : ""));
			responseDto
					.setPreStreetNo((dto.getAppdto().getPreStreetNo() != null ? dto.getAppdto().getPreStreetNo() : ""));
			responseDto.setPreHouseNo((dto.getAppdto().getPreHouseNo() != null ? dto.getAppdto().getPreHouseNo() : ""));
			responseDto.setPrePinCode((dto.getAppdto().getPrePinCode() != null ? dto.getAppdto().getPrePinCode() : ""));
			responseDto.setPlotDistrictId(
					(dto.getAppdto().getPlotDistrictId() != null ? dto.getAppdto().getPlotDistrictId() : ""));
			responseDto.setPlotTehsilId(
					(dto.getAppdto().getPlotTehsilId() != null ? dto.getAppdto().getPlotTehsilId() : ""));
			responseDto.setPlotKhataNoId(
					(dto.getAppdto().getPlotKhataNoId() != null ? dto.getAppdto().getPlotKhataNoId() : ""));
			responseDto
					.setPlotMouzaId((dto.getAppdto().getPlotMouzaId() != null ? dto.getAppdto().getPlotMouzaId() : ""));
			responseDto.setPendingRoleId(
					(dto.getAppdto().getPendingRoleId() != null ? dto.getAppdto().getPendingRoleId().toString() : ""));
//			responseDto.setAssignStatus((dto.getAppdto().getAssignStatus() != null ? dto.getAppdto().getAssignStatus().toString():""));
//			responseDto.setCoRemarks((dto.getAppdto().getCoRemarks() != null ? dto.getAppdto().getCoRemarks() :""));
//			responseDto.setCoUploadedPhoto((dto.getAppdto().getCoUploadedPhoto() != null ? dto.getAppdto().getCoUploadedPhoto():""));
//			responseDto.setInspectionDate((dto.getAppdto().getInspectionDate() != null ? dto.getAppdto().getInspectionDate().toString():""));

			for (LandPlotViewDTO plotDto : dto.getPlotto()) {
				LandPlotViewDTO plotViewdto = new LandPlotViewDTO();
				plotViewdto.setPlotNoId(plotDto.getPlotNoId() != null ? plotDto.getPlotNoId() : "");
				plotViewdto.setExtend(plotDto.getExtend() != null ? plotDto.getExtend() : "");
				plotViewdto.setKhataNo(plotDto.getKhataNo() != null ? plotDto.getKhataNo() : "");
				plotViewdto.setPlotCode(plotDto.getPlotCode() != null ? plotDto.getKhataNo() : "");
				plotViewdto.setPurchaseArea(plotDto.getPurchaseArea() != null ? plotDto.getPurchaseArea() : "");
				plotViewdto.setTotalArea(plotDto.getTotalArea() != null ? plotDto.getTotalArea() : "");
				plotViewdto.setVarietiesId(plotDto.getVarietiesId() != null ? plotDto.getVarietiesId() : "");
				plotDtoList.add(plotViewdto);
			}
			responseDto.setPlotDto(plotDtoList);

		}
		return responseDto;

	}

	/*
	 * @Override public LandVerificationResponseDto fetchPendingRecords() {
	 * LandVerificationResponseDto response = new LandVerificationResponseDto();
	 * List<LandDetailsResponseDto> responseDtos = new ArrayList<>();
	 * List<PlotLandInspectionDto> dtos = new ArrayList<>();
	 * List<PlotLandInspectionEntity> pendingList =
	 * landInspectionRepository.findByNullCoRemarks("P");//landApplicantRepository.
	 * findByAssignStatus(1); if(pendingList != null && !pendingList.isEmpty()) {
	 * for(PlotLandInspectionEntity data :pendingList) { PlotLandInspectionDto dto =
	 * entityToPlotDto(data); dtos.add(dto); } response.setStatus(200);
	 * response.setMessage("Fetching records of Pending land application");
	 * response.setStatusMessage(CommonConstant.SUCCESS);
	 * response.setLandDetailResponse(dtos);
	 * 
	 * }else { response.setStatus(200);
	 * response.setMessage("Fetching records of Pending land application");
	 * response.setStatusMessage(CommonConstant.FAILED); }
	 * 
	 * return response; }
	 */
	@Override
	public LandVerificationResponseDto fetchCompleteRecords() {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		List<PlotLandInspectionDto> dtos = new ArrayList<>();
		List<Object[]> completeList = landInspectionRepository.findByCoRemarks();
		if (completeList != null && !completeList.isEmpty()) {
			dtos = mapToPlotDto(completeList);
			// ---------
			for (PlotLandInspectionDto dto : dtos) {
				if (dto.getCoUploadedPhoto() != null && !dto.getCoUploadedPhoto().isEmpty()) {
					ImageRequestDto imagedto = new ImageRequestDto();
					List<String> strList = new ArrayList<>();
					strList.add(dto.getCoUploadedPhoto());
					imagedto.setFileNames(strList);
					imagedto.setForPurpose("2");
					List<String> strList1 = tahasilService.viewImage(imagedto);
					dto.setImageLink(strList1.get(0));
				} else {
					dto.setImageLink("");
				}
			}
			// ---------
			response.setStatus(200);
			response.setMessage("Fetching records of Completed land application");
			response.setStatusMessage(CommonConstant.SUCCESS);
			response.setLandDetailResponse(dtos);
		} else {
			response.setStatus(200);
			response.setMessage("No records available of Completed land application");
			response.setStatusMessage(CommonConstant.FAILED);
		}

		return response;
	}

	@Override
	public LandVerificationResponseDto viewInspectionDetails(PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		List<PlotLandInspectionDto> dtos = new ArrayList<>();
		log.info("record fetching process from db for CoViewInspectionDetails starts");
		List<Object[]> resultList = landInspectionRepository.viewInspectionDetails(landDto.getPlotLandInspectionId());
		// viewTahasildarInspectionDetailsWeb
		log.info("record fetched from db for CoViewInspectionDetails");
		log.info("single record for CoViewInspectionDetails");
		if (resultList != null && !resultList.isEmpty()) {
			dtos = mapToDTO(resultList);
			// ------------------
			for (PlotLandInspectionDto dto : dtos) {
				if (dto.getTahasilUploadedPhoto() != null && !dto.getTahasilUploadedPhoto().isEmpty()) {
					ImageRequestDto imagedto = new ImageRequestDto();
					List<String> strList = new ArrayList<>();
					strList.add(dto.getTahasilUploadedPhoto());
					imagedto.setFileNames(strList);
					imagedto.setForPurpose("3");
					List<String> strList1 = new ArrayList<>();
					strList1 = tahasilService.viewImage(imagedto);
					// plotDto.setImageLink(strList1.get(0));
					dto.setTahasilImageLink(strList1.get(0));
				} else {
					dto.setTahasilImageLink("");
				}
				if (dto.getCoUploadedPhoto() != null && !dto.getCoUploadedPhoto().isEmpty()) {
					ImageRequestDto imagedto1 = new ImageRequestDto();
					List<String> coStrList = new ArrayList<>();
					// strList.add(dto.getCoUploadedPhoto());
					coStrList.add(dto.getCoUploadedPhoto());
					imagedto1.setFileNames(coStrList);
					imagedto1.setForPurpose("2");
					List<String> coStrList1 = new ArrayList<>();
					coStrList1 = tahasilService.viewImage(imagedto1);
					// plotDto.setImageLink(strList1.get(0));
					dto.setImageLink(coStrList1.get(0));
				} else {
					dto.setImageLink("");
				}
			}
			// -------------
			log.info("dtos :" + dtos);
			PlotLandInspectionDto result = dtos.get(0);
			response.setStatus(200);
			response.setMessage("Fetching details of CO land Inspection application");
			response.setStatusMessage(CommonConstant.SUCCESS);
			response.setPlotLandInspectionDto(result);
		} else {
			response.setStatus(200);
			response.setMessage("No records available of CO land Inspection application");
			response.setStatusMessage(CommonConstant.FAILED);
		}
		return response;
	}

	@Override
	public LandVerificationResponseDto viewTahasildarInspectionDetails(PlotLandInspectionDto landDto) {// for web
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		List<PlotLandInspectionDto> dtos = new ArrayList<>();
		List<Object[]> completeList = landInspectionRepository.findByTahasildarRemarks(landDto.getTahasilCode());
		if (completeList != null && !completeList.isEmpty()) {
			dtos = mapToPlotDtoForTahasil(completeList);
			response.setStatus(200);
			response.setMessage("Fetching records of Completed land application by tahasil");
			response.setStatusMessage(CommonConstant.SUCCESS);

			List<PlotLandInspectionDto> result = null;
			result = dtos.stream()
					.filter(dto -> dto.getPlotLandInspectionId().equals(landDto.getPlotLandInspectionId()))
					.collect(Collectors.toList());
			response.setPlotLandInspectionDto(result.get(0));
		} else {
			response.setStatus(200);
			response.setMessage("No records available of land application by tahasil");
			response.setStatusMessage(CommonConstant.FAILED);
		}

		return response;
	}

	@Override
	public LandVerificationResponseDto viewTahasildarInspectionDetailsWeb(PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		List<PlotLandInspectionDto> dtos = new ArrayList<>();
		List<Object[]> completeList = landInspectionRepository
				.viewTahasildarInspectionDetailsWeb(landDto.getTahasilCode());
		if (completeList != null && !completeList.isEmpty()) {
			dtos = mapToPlotDtoForTahasilWeb(completeList);
			for (PlotLandInspectionDto dto : dtos) {

				if (dto.getCoordinates() != null || !dto.getCoordinates().isEmpty()) {
					dto.setCoordinates("");
					dto.setLandLatLong(null);
				}
			}

			response.setStatus(200);
			response.setMessage("Fetching records of Completed land application by tahasil");
			response.setStatusMessage(CommonConstant.SUCCESS);

			List<PlotLandInspectionDto> result = null;
			result = dtos.stream()
					.filter(dto -> dto.getPlotLandInspectionId().equals(landDto.getPlotLandInspectionId()))
					.collect(Collectors.toList());
			response.setPlotLandInspectionDto(result.get(0));
		} else {
			response.setStatus(200);
			response.setMessage("No records available of land application by tahasil");
			response.setStatusMessage(CommonConstant.FAILED);
		}
		return response;
	}

	@Override
	public LandVerificationResponseDto viewTahasildarValuationForm(PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		List<PlotLandInspectionDto> dtos = new ArrayList<>();
		List<Object[]> completeList = landInspectionRepository.viewTahasildarValuationForm(landDto.getPlotCode());
		if (completeList != null && !completeList.isEmpty()) {
			dtos = mapToPlotDtoForTahasilWeb(completeList);
			for (PlotLandInspectionDto dto : dtos) {

				if (dto.getCoordinates() != null || !dto.getCoordinates().isEmpty()) {
					dto.setCoordinates("");
					dto.setLandLatLong(null);
				}
			}

			response.setStatus(200);
			response.setMessage("Fetching records of Completed land application by tahasil");
			response.setStatusMessage(CommonConstant.SUCCESS);

			response.setLandDetailResponse(dtos);
		} else {
			response.setStatus(200);
			response.setMessage("No records available of land application by tahasil");
			response.setStatusMessage(CommonConstant.FAILED);
		}
		return response;
	}

	@Override
	public LandVerificationResponseDto getVillageInformation(PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		List<VillageDTO> dtos = new ArrayList<>();
		log.info("record fetching process from db for village information starts");
		List<Object[]> resultList = landInspectionRepository.getVillageInformation(landDto.getPlotLandInspectionId());
		log.info("record fetched from db for CoViewInspectionDetails");
		log.info("single record for CoViewInspectionDetails");
		if (resultList != null && !resultList.isEmpty()) {
			dtos = resultList.stream()
					.map(objects -> new VillageDTO((String) objects[0], (String) objects[1], (BigInteger) objects[2]))
					.collect(Collectors.toList());
			log.info("dtos :" + dtos);
			response.setVillageDTOs(dtos);
			response.setStatus(200);
			response.setMessage("Fetching details for village information land Inspection application");
			response.setStatusMessage(CommonConstant.SUCCESS);
		} else {
			response.setStatus(200);
			response.setMessage("No records available for village information land Inspection application");
			response.setStatusMessage(CommonConstant.FAILED);
		}
		return response;
	}

	@Override
	public LandVerificationResponseDto saveTahasildarPlotAction(PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {

			PlotLandInspectionEntity entity = landInspectionRepository.findByPlotCode(landDto.getPlotCode());
			Integer landId = entity.getPlotLandInspectionId().intValue();
			String fileName = "";
			log.info("tahasil inspection details saving process starts");
			if (landDto.getTahasilUploadedPhoto() != null && !landDto.getTahasilUploadedPhoto().isEmpty()
					&& !landDto.getTahasilUploadedPhoto().trim().isEmpty()) {
				fileName = landDto.getTahasildarInspectedBy() + "Tahasildar" + entity.getPlotLandInspectionId()
						+ "-LandTahasilSurvey" + ".jpg";
			}
			landDto.setFileName(fileName);
			log.info("entity data: " + entity);
			landDto.setPlotLandInspectionId(landId);
			int tahasilInspectedBy = Integer.parseInt(landDto.getTahasildarInspectedBy());
			Date parsedDate = new Date();
			if (landDto.getTahasildarInspectionDate() != null || !landDto.getTahasildarInspectionDate().isEmpty()) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				try {
					parsedDate = dateFormat.parse(landDto.getTahasildarInspectionDate());
				} catch (ParseException e) {
					log.error("Tahasildar inspection date error: " + e.getMessage());
				}
			}
			int status = 1;
			int count = landInspectionRepository.saveTahasildarPlotAction(landDto.getTahasilRemarks(), fileName, landId,
					landDto.getTahasilLatitude(), landDto.getTahasilLongitude(), tahasilInspectedBy, parsedDate,
					status);
			if (count > 0) {
				response.setStatus(200);
				response.setMessage("Tahasildar inspection data saved successfully");
			}
			if (landDto.getTahasilUploadedPhoto() != null && !landDto.getTahasilUploadedPhoto().isEmpty()
					&& !landDto.getTahasilUploadedPhoto().trim().isEmpty()) {
				log.info("file saving process call starts");
				GenericResponse resp = saveFileTodirectory(landDto, "TAHASILDAR");
				if (resp.getMessage() != null) {
					response.setMessage(resp.getMessage());
					response.setStatus(32);
				} else {
					// response.setMessage(resp.getMessage());
				}
			}

			// application changes
			List<Object[]> idList = landInspectionRepository.fetchLandApplicationIDs(landDto.getPlotCode());
			List<PlotLandInspectionDto> dtoList = new ArrayList<>();
			dtoList = mappingForResult(idList);

			log.info("checking flow : " + idList + " and dtoMapped: " + dtoList);
			if (idList != null && !idList.isEmpty()) {
				for (PlotLandInspectionDto dto : dtoList) {
					log.info("checking flow for applicationId : " + dto.getLandApplicationId());
					applicationFlow(dto.getLandApplicationId(), BigInteger.valueOf(15), new Date(),
							BigInteger.valueOf(14));
				}
			} else {
				applicationFlow(BigInteger.ZERO, BigInteger.valueOf(15), new Date(), BigInteger.valueOf(14));
			}

//			List<Object[]> idList = landInspectionRepository.fetchLandApplicationIDs(landDto.getPlotCode());
//			
//			
//			for(Object landApplicationId : idList) {
//				log.info("checking flow for application history");
//				applicationFlow((BigInteger)landApplicationId,BigInteger.valueOf(15),new Date(),BigInteger.valueOf(4));
//			}

		} catch (Exception e) {
			log.error("error while saving Tahasildar inspection data: " + e.getMessage());
			response.setStatus(500);
			response.setMessage("error while saving Tahasildar inspection data");
		}
		return response;
	}

	@Override
	public LandVerificationResponseDto fetchTahasilPendingRecordsByVillage(PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		List<PlotLandInspectionDto> dtos = new ArrayList<>();
		List<Object[]> pendingList = landInspectionRepository.findByTahasildarNullRemarks(landDto.getVillageCode(),
				landDto.getTahasilCode());
		if (pendingList != null && !pendingList.isEmpty()) {
			dtos = mapToPlotDto(pendingList);
			response.setStatus(200);
			response.setMessage("Fetching records of Pending land application by village");
			response.setStatusMessage(CommonConstant.SUCCESS);
			response.setLandDetailResponse(dtos);

		} else {
			response.setStatus(200);
			response.setMessage("No records available of Pending land application by village");
			response.setStatusMessage(CommonConstant.FAILED);
		}

		return response;
	}

	@Override
	public LandVerificationResponseDto fetchTahasildarPendingRecords(PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		List<PlotLandInspectionDto> dtos = new ArrayList<>();
		List<Object[]> pendingList = landInspectionRepository.findByTahasildarNullRemarks(landDto.getTahasilCode());
		log.info("pending list size: " + pendingList.size());
		if (pendingList != null && !pendingList.isEmpty()) {
			dtos = mapToPlotDtoForTahasil(pendingList);
			response.setStatus(200);
			response.setMessage("Fetching records of Pending land application by tahasil");
			response.setStatusMessage(CommonConstant.SUCCESS);
			response.setLandDetailResponse(dtos);

		} else {
			response.setStatus(200);
			response.setMessage("No records available of Pending land application by tahasil");
			response.setStatusMessage(CommonConstant.FAILED);
		}

		return response;
	}

	@Override
	public LandVerificationResponseDto fetchTahasildarCompleteRecords(PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		List<LandDetailsResponseDto> responseDtos = new ArrayList<>();
		List<PlotLandInspectionDto> dtos = new ArrayList<>();
		List<Object[]> completeList = landInspectionRepository.findByTahasildarRemarks(landDto.getTahasilCode());
		if (completeList != null && !completeList.isEmpty()) {
			dtos = mapToPlotDtoForTahasil(completeList);
			for (PlotLandInspectionDto dto : dtos) {
				if (dto.getTahasilUploadedPhoto() != null && !dto.getTahasilUploadedPhoto().isEmpty()) {
					ImageRequestDto imagedto = new ImageRequestDto();
					PlotLandInspectionDto plotDto = new PlotLandInspectionDto();
					List<String> strList = new ArrayList<>();
					strList.add(dto.getTahasilUploadedPhoto());
					imagedto.setFileNames(strList);
					imagedto.setForPurpose("3");
					List<String> strList1 = new ArrayList<>();
					strList1 = tahasilService.viewImage(imagedto);
					dto.setTahasilImageLink(strList1.get(0));
				} else {
					dto.setTahasilImageLink("");
				}
			}
			response.setStatus(200);
			response.setMessage("Fetching records of Completed land application by tahasil");
			response.setStatusMessage(CommonConstant.SUCCESS);
			response.setLandDetailResponse(dtos);
		} else {
			response.setStatus(200);
			response.setMessage("No records available of Completed land application by tahasil");
			response.setStatusMessage(CommonConstant.FAILED);
		}

		return response;
	}

	@Override
	public LandVerificationResponseDto getVillageInformationForTahasil(PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		List<VillageDTO> dtos = new ArrayList<>();
		log.info("record fetching process from db for village information starts");
		List<Object[]> resultList = landInspectionRepository.getVillageInformationForTahasil(landDto.getTahasilCode());
		log.info("record fetched from db for TahasilViewInspectionDetails");
		log.info("single record for TahasilViewInspectionDetails");
		if (resultList != null && !resultList.isEmpty()) {
			dtos = resultList.stream()
					.map(objects -> new VillageDTO((String) objects[0], (String) objects[1], (BigInteger) objects[2]))
					.collect(Collectors.toList());
			log.info("dtos :" + dtos);
			response.setVillageDTOs(dtos);
			response.setStatus(200);
			response.setMessage("Fetching details for village information land Inspection application by tahasil");
			response.setStatusMessage(CommonConstant.SUCCESS);
		} else {
			response.setStatus(200);
			response.setMessage("No records available for village information land Inspection application by tahasil");
			response.setStatusMessage(CommonConstant.FAILED);
		}
		return response;
	}

	@Override
	public LandVerificationResponseDto fetchTahasilPendingAndCompleteCount(PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		List<Object[]> pendingList = landInspectionRepository.findByTahasildarNullRemarks(landDto.getTahasilCode());
		List<Object[]> completeList = landInspectionRepository.findByTahasildarRemarks(landDto.getTahasilCode());
		List<VillageDTO> village = villageRepository.getVillageDetails(landDto.getTahasilCode());
		List<VillageDTO> villageList = village.stream().filter(item -> item.getTotalPlot().intValue() > 0)
				.collect(Collectors.toList());
		int pendingCount = 0;
		log.info("villageListCount : " + villageList);
		for (VillageDTO dto : villageList) {
			log.info("LandUsePendingCount : " + dto.getTotalPlot());
			pendingCount += dto.getTotalPlot().intValue();
		}
		log.info("PendingCount : " + pendingCount);
		List<Object[]> landUsecompleteList = tahasilPlotSurveyRepository.findByRemarks(landDto.getTahasilCode());
		response.setLandApplicationPendingCount(pendingList.size());
		response.setLandApplicationCompleteCount(completeList.size());
		response.setLandUsePendingCount(pendingCount);
		response.setLandUseCompleteCount(landUsecompleteList.size());
		response.setStatus(200);
		response.setMessage("Fetching records of Pending and Completed land application by tahasil");
		return response;
	}

	private List<PlotLandInspectionDto> mapToDTO(List<Object[]> resultList) {
		return resultList.stream().map(objects -> {
			PlotLandInspectionDto dto = new PlotLandInspectionDto();
			dto.setPlotLandInspectionId((Integer) objects[0]);
			dto.setDistrictCode(objects[1] != null ? (String) objects[1].toString() : "");
			dto.setTahasilCode(objects[2] != null ? (String) objects[2].toString() : "");
			dto.setVillageCode(objects[3] != null ? objects[3].toString() : "");
			dto.setKhatianCode(objects[4] != null ? objects[4].toString() : "");
			dto.setPlotCode(objects[5] != null ? objects[5].toString() : "");
			dto.setCoRemarks(objects[6] != null ? objects[6].toString() : "");
			dto.setCoUploadedPhoto(objects[7] != null ? objects[7].toString() : "");
			dto.setInspectionDate(objects[8] != null ? (String) objects[8].toString() : "");
			dto.setCreatedDate(objects[9] != null ? (String) objects[9].toString() : "");
			dto.setScheduledInspectionDate(objects[10] != null ? (String) objects[10].toString() : "");
			dto.setPlotNumber(objects[11] != null ? objects[11].toString() : "");
			dto.setKhataNo(objects[12] != null ? objects[12].toString() : "");
			dto.setDistrictName(objects[13] != null ? objects[13].toString() : "");
			dto.setTahasilName(objects[14] != null ? objects[14].toString() : "");
			dto.setVillageName(objects[15] != null ? objects[15].toString() : "");
			dto.setAreaAcre(objects[16] != null ? objects[16].toString() : "0");
			dto.setTahasilRemarks(objects[17] != null ? objects[17].toString() : "");
			dto.setTahasilLatitude(objects[18] != null ? objects[18].toString() : "");
			dto.setTahasilLongitude(objects[19] != null ? objects[19].toString() : "");
			dto.setTahasildarInspectedBy(objects[20] != null ? objects[20].toString() : "");
			dto.setTahasilUploadedPhoto(objects[21] != null ? objects[21].toString() : "");
			dto.setTahasildarInspectionDate(objects[22] != null ? objects[22].toString() : "");
			dto.setImageLink(finalUploadPath);
			dto.setTahasilImageLink(finalUploadPath);

			return dto;
		}).collect(Collectors.toList());
	}

	public GenericResponse saveFileTodirectory(PlotLandInspectionDto landDto, String savedBy) {
		GenericResponse response = new GenericResponse();
		byte[] data;
		String filePath = "";
		String fileName = "";
		String filepath1 = "";
		File file;
		if (savedBy.equals("COORDINATE OFFICER")) {
			String[] strings = landDto.getCoUploadedPhoto().split(",");
			data = DatatypeConverter.parseBase64Binary(strings[1]);
			log.info("file contents " + data);
			fileName = landDto.getFileName();
			log.info("file save " + fileName);
			filePath = finalUploadPath + "/LandCOSurvey";
			File folder = new File(filePath);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			filepath1 = finalUploadPath + "/LandCOSurvey/" + "PlotSurvey";
			File folder1 = new File(filepath1);
			if (!folder1.exists()) {
				folder1.mkdirs();
			}
			file = new File(finalUploadPath + "/LandCOSurvey/" + "PlotSurvey/" + fileName);
			log.info("fgdcsgcsa file " + file);
			try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
				outputStream.write(data);
				log.info("outputStream save " + outputStream);
			} catch (IOException e) {
				response.setMessage("Error occured while saving file");

			}

		} else if (savedBy.equals("TAHASILDAR")) {
			String[] strings = landDto.getTahasilUploadedPhoto().split(",");
			data = DatatypeConverter.parseBase64Binary(strings[1]);
			log.info("file contents " + data);
			fileName = landDto.getFileName();
			log.info("file save " + fileName);
			filePath = finalUploadPath + "/LandTahasilSurvey";
			File folder = new File(filePath);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			filepath1 = finalUploadPath + "/LandTahasilSurvey/" + "PlotSurvey";
			File folder1 = new File(filepath1);
			if (!folder1.exists()) {
				folder1.mkdirs();
			}
			file = new File(finalUploadPath + "/LandTahasilSurvey/" + "PlotSurvey/" + fileName);
			log.info("fgdcsgcsa file for LA tahasil " + file);
			try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
				outputStream.write(data);
				log.info("outputStream save " + outputStream);
			} catch (IOException e) {
				response.setMessage("Error occured while saving file");
			}
		}
		return response;

	}

	public void applicationFlow(BigInteger landApplicationId, BigInteger flowId, Date actionDate,
			BigInteger actionRoleId) {
		ApplicationFlowDto dto = new ApplicationFlowDto();
		dto.setLandApplicationId(landApplicationId);
		dto.setApplicationFlowId(flowId);
		dto.setActionDateTime(actionDate);
		dto.setActionRoleId(actionRoleId);
		log.info("flow redirected to common service: " + dto.toString());
		commonService.saveApplicationFlow(dto);
	}

	public List<PlotLandInspectionDto> mapToPlotDtoForTahasil(List<Object[]> resultList) {

		return resultList.stream().map(objects -> {
			PlotLandInspectionDto dto = new PlotLandInspectionDto();
			dto.setPlotLandInspectionId((Integer) objects[0]);
			dto.setDistrictCode(objects[1] != null ? (String) objects[1].toString() : "");
			dto.setTahasilCode(objects[2] != null ? (String) objects[2].toString() : "");
			dto.setVillageCode(objects[3] != null ? objects[3].toString() : "");
			dto.setKhatianCode(objects[4] != null ? objects[4].toString() : "");
			dto.setPlotCode(objects[5] != null ? objects[5].toString() : "");
			dto.setCoRemarks(objects[6] != null ? objects[6].toString() : "");
			dto.setCoUploadedPhoto(objects[7] != null ? objects[7].toString() : "");
			dto.setInspectionDate(objects[8] != null ? (String) objects[8].toString() : "");
			dto.setCreatedDate(objects[9] != null ? (String) objects[9].toString() : "");
			dto.setLatitude(objects[10] != null ? objects[10].toString() : "");
			dto.setLongitude(objects[11] != null ? objects[11].toString() : "");
			dto.setScheduledInspectionDate(objects[12] != null ? (String) objects[12].toString() : "");
			dto.setCentralLatitude(objects[13] != null ? objects[13].toString() : "");
			dto.setCentralLongitude(objects[14] != null ? objects[14].toString() : "");
			dto.setCoordinates(objects[15] != null ? objects[15].toString() : "");
			if (objects[15] != null && !objects[15].toString().isEmpty()) {
				dto.setLandLatLong(grievanceMobileServiceImpl.convertCoordinates(objects[15].toString()));
			} else {
				dto.setLandLatLong(new ArrayList<>());
			}
			dto.setPlotNumber(objects[16] != null ? objects[16].toString() : "");
			dto.setKhataNo(objects[17] != null ? objects[17].toString() : "");
			dto.setDistrictName(objects[18] != null ? objects[18].toString() : "");
			dto.setTahasilName(objects[19] != null ? objects[19].toString() : "");
			dto.setVillageName(objects[20] != null ? objects[20].toString() : "");
			dto.setAreaAcre(objects[21] != null ? objects[21].toString() : "0");
			dto.setTahasilRemarks(objects[22] != null ? objects[22].toString() : "");
			dto.setTahasilLatitude(objects[23] != null ? objects[23].toString() : "");
			dto.setTahasilLongitude(objects[24] != null ? objects[24].toString() : "");
			dto.setTahasildarInspectedBy(objects[25] != null ? objects[25].toString() : "");
			dto.setTahasilUploadedPhoto(objects[26] != null ? objects[26].toString() : "");
			dto.setTahasildarInspectionDate(objects[27] != null ? (String) objects[27].toString() : "");

			return dto;
		}).collect(Collectors.toList());

	}

	public List<PlotLandInspectionDto> mapToPlotDtoForTahasilWeb(List<Object[]> resultList) {

		return resultList.stream().map(objects -> {
			PlotLandInspectionDto dto = new PlotLandInspectionDto();
			dto.setPlotLandInspectionId((Integer) objects[0]);
			dto.setDistrictCode(objects[1] != null ? (String) objects[1].toString() : "");
			dto.setTahasilCode(objects[2] != null ? (String) objects[2].toString() : "");
			dto.setVillageCode(objects[3] != null ? objects[3].toString() : "");
			dto.setKhatianCode(objects[4] != null ? objects[4].toString() : "");
			dto.setPlotCode(objects[5] != null ? objects[5].toString() : "");
			dto.setCoRemarks(objects[6] != null ? objects[6].toString() : "");
			dto.setCoUploadedPhoto(objects[7] != null ? objects[7].toString() : "");
			dto.setInspectionDate(objects[8] != null ? (String) objects[8].toString() : "");
			dto.setCreatedDate(objects[9] != null ? (String) objects[9].toString() : "");
			dto.setLatitude(objects[10] != null ? objects[10].toString() : "");
			dto.setLongitude(objects[11] != null ? objects[11].toString() : "");
			dto.setScheduledInspectionDate(objects[12] != null ? (String) objects[12].toString() : "");
			dto.setCentralLatitude(objects[13] != null ? objects[13].toString() : "");
			dto.setCentralLongitude(objects[14] != null ? objects[14].toString() : "");
			dto.setCoordinates(objects[15] != null ? objects[15].toString() : "");
			if (objects[15] != null && !objects[15].toString().isEmpty()) {
				dto.setLandLatLong(grievanceMobileServiceImpl.convertCoordinates(objects[15].toString()));
			} else {
				dto.setLandLatLong(new ArrayList<>());
			}
			dto.setPlotNumber(objects[16] != null ? objects[16].toString() : "");
			dto.setKhataNo(objects[17] != null ? objects[17].toString() : "");
			dto.setDistrictName(objects[18] != null ? objects[18].toString() : "");
			dto.setTahasilName(objects[19] != null ? objects[19].toString() : "");
			dto.setVillageName(objects[20] != null ? objects[20].toString() : "");
			dto.setAreaAcre(objects[21] != null ? objects[21].toString() : "0");
			dto.setTahasilRemarks(objects[22] != null ? objects[22].toString() : "");
			dto.setTahasilLatitude(objects[23] != null ? objects[23].toString() : "");
			dto.setTahasilLongitude(objects[24] != null ? objects[24].toString() : "");
			dto.setTahasildarInspectedBy(objects[25] != null ? objects[25].toString() : "");
			dto.setTahasilUploadedPhoto(objects[26] != null ? objects[26].toString() : "");
			dto.setTahasildarInspectionDate(objects[27] != null ? (String) objects[27].toString() : "");
			dto.setMarfatdarName(objects[28] != null ? (String) objects[28].toString() : "");
			dto.setSotwa(objects[29] != null ? (String) objects[29].toString() : "");
			dto.setTotalArea(objects[30] != null ? (String) objects[30].toString() : "0");
			dto.setPurchaseArea(objects[31] != null ? (String) objects[31].toString() : "0");
			dto.setApplicantName(objects[32] != null ? (String) objects[32].toString() : "");
			dto.setApplicationNo(objects[33] != null ? (String) objects[33].toString() : "");

			return dto;
		}).collect(Collectors.toList());

	}

	private List<PlotLandInspectionDto> mapToPlotDto(List<Object[]> resultList) {

		return resultList.stream().map(objects -> {
			PlotLandInspectionDto dto = new PlotLandInspectionDto();
			dto.setPlotLandInspectionId((Integer) objects[0]);
			dto.setDistrictCode(objects[1] != null ? (String) objects[1].toString() : "");
			dto.setTahasilCode(objects[2] != null ? (String) objects[2].toString() : "");
			dto.setVillageCode(objects[3] != null ? objects[3].toString() : "");
			dto.setKhatianCode(objects[4] != null ? objects[4].toString() : "");
			dto.setPlotCode(objects[5] != null ? objects[5].toString() : "");
			dto.setCoRemarks(objects[6] != null ? objects[6].toString() : "");
			dto.setCoUploadedPhoto(objects[7] != null ? objects[7].toString() : "");
			dto.setInspectionDate(objects[8] != null ? (String) objects[8].toString() : "");
			dto.setCreatedDate(objects[9] != null ? (String) objects[9].toString() : "");
			dto.setLatitude(objects[10] != null ? objects[10].toString() : "");
			dto.setLongitude(objects[11] != null ? objects[11].toString() : "");
			dto.setScheduledInspectionDate(objects[12] != null ? (String) objects[12].toString() : "");
			dto.setCentralLatitude(objects[13] != null ? objects[13].toString() : "");
			dto.setCentralLongitude(objects[14] != null ? objects[14].toString() : "");
			dto.setCoordinates(objects[15] != null ? objects[15].toString() : "");
			if (objects[15] != null && !objects[15].toString().isEmpty()) {
				dto.setLandLatLong(grievanceMobileServiceImpl.convertCoordinates(objects[15].toString()));
			} else {
				dto.setLandLatLong(new ArrayList<>());
			}
			dto.setPlotNumber(objects[16] != null ? objects[16].toString() : "");
			dto.setKhataNo(objects[17] != null ? objects[17].toString() : "");
			dto.setDistrictName(objects[18] != null ? objects[18].toString() : "");
			dto.setTahasilName(objects[19] != null ? objects[19].toString() : "");
			dto.setVillageName(objects[20] != null ? objects[20].toString() : "");
			dto.setAreaAcre(objects[21] != null ? objects[21].toString() : "0");

			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public LandVerificationResponseDto viewTahasildarValuationFormByplotCode(PlotLandInspectionDto landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		List<PlotValuationDTO> dtos = new ArrayList<>();
		List<Object[]> completeList = landInspectionRepository
				.viewTahasildarValuationFormByplotCode(landDto.getPlotCode());
		if (completeList != null && !completeList.isEmpty()) {
			dtos = mapToPlotDtoByPlotCode(completeList);

			response.setStatus(200);
			response.setMessage("Fetching records of Completed land application by tahasil");
			response.setStatusMessage(CommonConstant.SUCCESS);

			response.setPlotResponse(dtos);
		} else {
			response.setStatus(200);
			response.setMessage("No records available of land application by tahasil");
			response.setStatusMessage(CommonConstant.FAILED);
		}
		return response;
	}

	public List<PlotValuationDTO> mapToPlotDtoByPlotCode(List<Object[]> resultList) {

		return resultList.stream().map(objects -> {
			PlotValuationDTO dto = new PlotValuationDTO();
			dto.setPlotCode(objects[0] != null ? objects[0].toString() : "");
			dto.setKhatianCode(objects[1] != null ? objects[1].toString() : "");

			dto.setAvailableLand((Short) objects[2]);
			dto.setLandUserName(objects[3] != null ? (String) objects[3].toString() : "");
			dto.setLandAddress(objects[4] != null ? objects[4].toString() : "");

			dto.setLandUseType(objects[5] != null ? (String) objects[5].toString() : "");
			dto.setTenure(objects[6] != null ? objects[6].toString() : "");
			dto.setOwnershipRecord((Short) objects[7]);
			dto.setFileUploadOwnershipDocument(objects[8] != null ? (String) objects[8].toString() : "");

			dto.setTotalPrice(objects[10] != null ? (String) objects[10].toString() : "0");
			dto.setPricePerAcre(objects[9] != null ? (String) objects[9].toString() : "0");
			dto.setPlotNo(objects[12] != null ? objects[12].toString() : "");
			dto.setKhataNo(objects[11] != null ? objects[11].toString() : "");

			return dto;
		}).collect(Collectors.toList());

	}

}
