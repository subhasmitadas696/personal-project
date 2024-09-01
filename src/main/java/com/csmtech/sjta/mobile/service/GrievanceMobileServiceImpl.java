package com.csmtech.sjta.mobile.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.GenericResponse;
import com.csmtech.sjta.dto.GrievanceMainDTO;
import com.csmtech.sjta.dto.LandRegisterMobileNoOrOtpVerifiedDTO;
import com.csmtech.sjta.dto.NotificationDTO;
import com.csmtech.sjta.entity.Grievance;
import com.csmtech.sjta.entity.Land_applicant;
import com.csmtech.sjta.mobile.dto.GrievanceResponseDto;
import com.csmtech.sjta.mobile.dto.ImageRequestDto;
import com.csmtech.sjta.mobile.dto.LandLatLng;
import com.csmtech.sjta.mobile.dto.PlotLandInspectionDto;
import com.csmtech.sjta.mobile.entity.PlotLandInspectionEntity;
import com.csmtech.sjta.mobile.repository.GrievanceMobileRepository;
import com.csmtech.sjta.mobile.repository.LandInspectionRepository;
import com.csmtech.sjta.mobile.repository.LandPostAllotmentRepository;
import com.csmtech.sjta.mobile.repository.NotificationDetailsRepository;
import com.csmtech.sjta.repository.GrievanceRepository;
import com.csmtech.sjta.repository.LandAppRegistratationClassRepository;
import com.csmtech.sjta.repository.LandApplicantJPARepository;
import com.csmtech.sjta.serviceImpl.GrievanceServiceImpl;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.OtpGenerateCommonUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GrievanceMobileServiceImpl implements GrievanceMobileService {

	@Autowired
	private GrievanceRepository grievanceRepository;

	@Autowired
	private GrievanceMobileRepository mobileRepository;

	@Autowired
	private LandAppRegistratationClassRepository repo;
	
	@Autowired
	LandApplicantJPARepository landApplicantRepository;
	
	@Autowired
	LandInspectionRepository landInspectionRepository;
	
	@Autowired
	LandPostAllotmentRepository landPostAllotmentRepository;
	
	@Autowired
	NotificationDetailsRepository notificationDetailsRepo;
	
	@Autowired
	NotificationDetailsServiceImpl notificationDetailsServiceImpl;

	@Value("${file.path}")
	private String finalUploadPath;
	
	@Value("${app.url}")
	private String appUrl;
	

	@Override
	public GrievanceResponseDto saveAfterInspection(GrievanceMainDTO dto) {
		GrievanceResponseDto response = new GrievanceResponseDto();
		log.info("Grievance service module starts for CO take action process");
		Grievance getEntity = grievanceRepository.findByIntId(dto.getGrievanceId());
		log.info("Grievance entity fetching from db: " + getEntity);
		Date parsedDate = new Date();
		log.info("Grievance entity data checking with dto: " + "entity no:"+getEntity.getGrievanceNumber()+ "dto no:"+dto.getGrievanceNo()+"entity id:"+getEntity.getIntId()+"dto id:"+dto.getGrievanceId());
		if ((getEntity.getGrievanceNumber() != null && getEntity.getGrievanceNumber().equals(dto.getGrievanceNo()))
				&& (getEntity.getIntId().equals(dto.getGrievanceId()))) {
			String fileName = dto.getGrievanceNo() + "-CoInspection" + ".jpg";
			getEntity.setTxtName((dto.getName() != null && !dto.getName().isEmpty() && !dto.getName().trim().isEmpty())
					? dto.getName()
					: "");
			getEntity.setTxtFatherName((dto.getFatherName() != null && !dto.getFatherName().isEmpty()
					&& !dto.getFatherName().trim().isEmpty()) ? dto.getFatherName() : "");
			getEntity.setSelCaste((dto.getCasteName() != null && !dto.getCasteName().isEmpty()
					&& !dto.getCasteName().trim().isEmpty()) ? dto.getCasteName() : "");
			getEntity.setSelKhataNo((dto.getKhatianCode() != null && !dto.getKhatianCode().isEmpty()
					&& !dto.getKhatianCode().trim().isEmpty()) ? dto.getKhatianCode() : "");
			getEntity.setSelPlotNo(
					(dto.getPlotNo() != null && !dto.getPlotNo().isEmpty() && !dto.getPlotNo().trim().isEmpty())
							? dto.getPlotNo()
							: "");
			BigDecimal area = dto.getAreaAcre() != null ? new BigDecimal(dto.getAreaAcre()) : BigDecimal.ZERO;
			getEntity.setTxtTotalAreainacre(area);
			BigDecimal areaExtent = dto.getExtentOccupied() != null ? new BigDecimal(dto.getExtentOccupied())
					: BigDecimal.ZERO;
			getEntity.setTxtExtentOccupied(areaExtent);
			getEntity.setClassOfLandEncroached(
					(dto.getClassOfLandEncroached() != null && !dto.getClassOfLandEncroached().isEmpty()
							&& !dto.getClassOfLandEncroached().trim().isEmpty()) ? dto.getClassOfLandEncroached() : "");
			// class of land encroached - not written to be discussed later
			getEntity.setLandLocation((dto.getLandLocation() != null && !dto.getLandLocation().isEmpty()
					&& !dto.getLandLocation().trim().isEmpty()) ? dto.getLandLocation() : "");
			getEntity.setSelModeofOccupation(
					(dto.getModeOfOccupation() != null && dto.getModeOfOccupation() > 0) ? dto.getModeOfOccupation()
							: 0);
			getEntity.setTxtOccupationDetails((dto.getOtherOccupation() != null && !dto.getOtherOccupation().isEmpty()
					&& !dto.getOtherOccupation().trim().isEmpty()) ? dto.getOtherOccupation() : "");
			getEntity.setRemarkByCO((dto.getCoRemarks() != null && !dto.getCoRemarks().isEmpty()
					&& !dto.getCoRemarks().trim().isEmpty()) ? dto.getCoRemarks() : "");
			getEntity.setInspectionBy((dto.getInspectionBy() != null && !dto.getInspectionBy().isEmpty()
					&& !dto.getInspectionBy().trim().isEmpty()) ? Integer.parseInt(dto.getInspectionBy()) : 0);
			// getEntity.set
			if (dto.getInspectionDate() != null || dto.getInspectionDate().isEmpty()) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				try {
					parsedDate = dateFormat.parse(dto.getInspectionDate());
				} catch (ParseException e) {
					log.error("inspection date error: " + e.getMessage());
				}
			}
			getEntity.setInspectionDate(parsedDate);
			getEntity.setSelDistrict13(dto.getDistrictCode());
			getEntity.setSelTahasil(dto.getTahasilCode());
			getEntity.setSelVillage15(dto.getVillageCode());
			getEntity.setCoUploadedPhoto((dto.getCoUploadedPhoto() != null && !dto.getCoUploadedPhoto().isEmpty()
					&& !dto.getCoUploadedPhoto().trim().isEmpty()) ? fileName : "");
			// newly added requirement due to change in apk
			getEntity.setSelDistrict((dto.getDistrictId() != null && !dto.getDistrictId().isEmpty()
					&& !dto.getDistrictId().trim().isEmpty()) ? Integer.parseInt(dto.getDistrictId()) : 0);
			getEntity.setSelBlock(
					(dto.getBlockId() != null && !dto.getBlockId().isEmpty() && !dto.getBlockId().trim().isEmpty())
							? Integer.parseInt(dto.getBlockId())
							: 0);
			getEntity.setSelGP((dto.getGpId() != null && !dto.getGpId().isEmpty() && !dto.getGpId().trim().isEmpty())
					? Integer.parseInt(dto.getGpId())
					: 0);
			getEntity.setSelVillage((dto.getVillageId() != null && !dto.getVillageId().isEmpty()
					&& !dto.getVillageId().trim().isEmpty()) ? Integer.parseInt(dto.getVillageId()) : 0);
			getEntity.setGrievanceStatus(3);
			log.info("saving record into db: " + getEntity);
			grievanceRepository.save(getEntity);
			Grievance fetchRecord = grievanceRepository.findByGrievanceNumber(dto.getGrievanceNo());
			log.info("record saved in db: " + fetchRecord);
			dto.setFileName(getEntity.getCoUploadedPhoto());
			String[] strings = dto.getCoUploadedPhoto().split(",");
			log.info("strings subpart" + strings[0] + " strings full" + dto.getCoUploadedPhoto());
			if (strings[0].startsWith("data")) {
				log.info("going to save the file");
				GenericResponse resp = saveFileTodirectory(dto);
				if (resp.getMessage() != null) {
					response.setMessage(resp.getMessage());
					response.setStatus(32);
				}
			}
			response.setStatusMessage(CommonConstant.SUCCESS);
			response.setStatus(200);
			response.setMessage("grievance updated with application number: " + dto.getGrievanceNo());
		} else {
			response.setMessage("wrong input data, grievance data does not match");
			response.setStatus(400);
			response.setStatusMessage(CommonConstant.FAILED);
		}
		return response;
	}

	
	@SuppressWarnings("unused")
	public GenericResponse saveFileTodirectory(GrievanceMainDTO grievanceMainDTO) {
		GenericResponse response = new GenericResponse();
		log.info("outside file validation if ");
		if (grievanceMainDTO.getCoUploadedPhoto() != null && !grievanceMainDTO.getCoUploadedPhoto().isEmpty()
				&& !grievanceMainDTO.getCoUploadedPhoto().trim().isEmpty()) {
			String[] strings = grievanceMainDTO.getCoUploadedPhoto().split(",");
			String extension;
			switch (strings[0]) {
			case "data:image/jpeg;base64":
				extension = "jpeg";
				break;
			case "data:image/png;base64":
				extension = "png";
				break;
			default:
				extension = "jpg";
				break;
			}
			log.info("inside if after switch");
			byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
			log.info("file contents " + data);
			String fileName = grievanceMainDTO.getFileName();
			log.info("file save " + fileName);
			String filePath = finalUploadPath + "/grievance";
			File folder = new File(filePath);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			File file = new File(finalUploadPath + "/grievance/" + fileName);
			log.info("fgdcsgcsa file " + file);
			try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
				outputStream.write(data);
				log.info("outputStream save " + outputStream);
			} catch (IOException e) {
				response.setMessage("Error occured while saving file");

			}
		}
		return response;
	}

	
	@SuppressWarnings("unused")
	public GenericResponse saveGrievanceFileTodirectory(GrievanceMainDTO grievanceMainDTO) {
		GenericResponse response = new GenericResponse();
		if (grievanceMainDTO.getUploadFile() != null && !grievanceMainDTO.getUploadFile().isEmpty()
				&& !grievanceMainDTO.getUploadFile().trim().isEmpty()) {
			String[] strings = grievanceMainDTO.getUploadFile().split(",");
			String extension;
			switch (strings[0]) {
			case "data:image/jpeg;base64":
				extension = "jpeg";
				break;
			case "data:image/png;base64":
				extension = "png";
				break;
			default:
				extension = "jpg";
				break;
			}
			byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
			log.info("file contents " + data);
			String fileName = grievanceMainDTO.getFileName();
			log.info("file save " + fileName);
			String filePath = finalUploadPath + "/grievance";
			File folder = new File(filePath);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			File file = new File(finalUploadPath + "/grievance/" + fileName);
			log.info("fgdcsgcsa file " + file);
			try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
				outputStream.write(data);
				log.info("outputStream save " + outputStream);
			} catch (IOException e) {
				response.setMessage("Error occured while saving file");

			}
		}

		return response;

	}

	@Override
	public GrievanceResponseDto saveGrievance(GrievanceMainDTO grievanceMainDTO) {
		GrievanceResponseDto response = new GrievanceResponseDto();
		Grievance grievance = new Grievance();
		String generateGrievanceNo = GrievanceServiceImpl.generateApplicantUniqueNumber("GR");
		String fileName = generateGrievanceNo + "-Grievance" + ".jpg";
		log.info("Grievance saving service module starts");

		try {
			if (grievanceMainDTO != null) {
				grievance.setSelTahasil(grievanceMainDTO.getTahasilCode());
				grievance.setSelVillage15(grievanceMainDTO.getVillageCode());
				grievance.setSelDistrict13(grievanceMainDTO.getDistrictCode());
				grievance.setBitDeletedFlag(false);
				grievance.setSaveMode("M");
				grievance.setSelKhataNo(
						grievanceMainDTO.getKhatianCode() != null ? grievanceMainDTO.getKhatianCode() : "");
				grievance.setTxtName(grievanceMainDTO.getName() != null ? grievanceMainDTO.getName() : "");
				grievance.setGrievanceNumber(generateGrievanceNo);
				grievance.setFileFileUpload(
						(grievanceMainDTO.getUploadFile() != null && !grievanceMainDTO.getUploadFile().isEmpty()
								&& !grievanceMainDTO.getUploadFile().trim().isEmpty()) ? fileName : "");
				grievance.setChkDiscloseyourdetails(
						grievanceMainDTO.getDiscloseDetails() != null ? grievanceMainDTO.getDiscloseDetails() : 1);
				grievance.setSelPlotNo(grievanceMainDTO.getPlotNo() != null ? grievanceMainDTO.getPlotNo() : "");
				grievance.setTxtFatherName(
						grievanceMainDTO.getFatherName() != null ? grievanceMainDTO.getFatherName() : "");
				grievance.setTxtrTextarea(grievanceMainDTO.getRemarks() != null ? grievanceMainDTO.getRemarks() : "");
				grievance.setGrievanceStatus(0);
				// added afterwards
				BigDecimal area = grievanceMainDTO.getAreaAcre() != null
						? new BigDecimal(grievanceMainDTO.getAreaAcre())
						: BigDecimal.ZERO;
				grievance.setTxtTotalAreainacre(area);
				BigDecimal areaExtent = grievanceMainDTO.getExtentOccupied() != null
						? new BigDecimal(grievanceMainDTO.getExtentOccupied())
						: BigDecimal.ZERO;
				grievance.setTxtExtentOccupied(areaExtent);
				grievance.setSelModeofOccupation(
						grievanceMainDTO.getModeOfOccupation() != null ? grievanceMainDTO.getModeOfOccupation() : 0);
				grievance.setTxtLandmark(grievanceMainDTO.getLandmark() != null ? grievanceMainDTO.getLandmark() : "");
				grievance.setSelCaste(grievanceMainDTO.getCasteName() != null ? grievanceMainDTO.getCasteName() : "");
				grievance.setSelDistrict(
						grievanceMainDTO.getDistrictId() != null && !grievanceMainDTO.getDistrictId().isEmpty()
								? Integer.parseInt(grievanceMainDTO.getDistrictId())
								: 0);
				grievance.setSelBlock(grievanceMainDTO.getBlockId() != null && !grievanceMainDTO.getBlockId().isEmpty()
						? Integer.parseInt(grievanceMainDTO.getBlockId())
						: 0);
				grievance.setSelGP(grievanceMainDTO.getGpId() != null && !grievanceMainDTO.getGpId().isEmpty()
						? Integer.parseInt(grievanceMainDTO.getGpId())
						: 0);
				grievance.setSelVillage(
						grievanceMainDTO.getVillageId() != null && !grievanceMainDTO.getVillageId().isEmpty()
								? Integer.parseInt(grievanceMainDTO.getVillageId())
								: 0);
				grievance.setTxtMobileNo(
						(grievanceMainDTO.getMobileNo() != null && !grievanceMainDTO.getMobileNo().isEmpty())
								? grievanceMainDTO.getMobileNo()
								: "");
				grievance.setPostedBy(grievanceMainDTO.getPostedBy());
				log.info("saving record into db: " + grievance);
				grievanceMainDTO.setFileName(grievance.getFileFileUpload());
				Grievance saveRecord = grievanceRepository.save(grievance);
				Grievance fetchRecord = grievanceRepository.findByGrievanceNumber(generateGrievanceNo);
				log.info("record saved in db: " + fetchRecord);
				response.setGrievanceNo(generateGrievanceNo);
				response.setStatus(200);
				response.setMessage("grievance saved with application number: " + generateGrievanceNo);
				if (grievanceMainDTO.getUploadFile() != null && !grievanceMainDTO.getUploadFile().isEmpty()
						&& !grievanceMainDTO.getUploadFile().trim().isEmpty()) {
					String[] strings = grievanceMainDTO.getUploadFile().split(",");
					log.info("strings subpart" + strings[0] + " strings full" + grievanceMainDTO.getUploadFile());
					if (strings[0].startsWith("data")) {
						GenericResponse resp = saveGrievanceFileTodirectory(grievanceMainDTO);
						if (resp.getMessage() != null) {
							response.setMessage(resp.getMessage());
							response.setStatus(32);
						}
					}
				}
				
				// add the notification
				//for manual notification Illegitimate Land Use Apply  by Citizen done then notify to Land Officer
				List<BigInteger> landUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("4"));
				if(landUserList != null && landUserList.size() > 0) {
					for(BigInteger landUser:landUserList) {
						NotificationDTO notificationDtoForLand = new NotificationDTO();
						notificationDtoForLand.setNotification("Grievance  "+ generateGrievanceNo+"is created.");
						notificationDtoForLand.setUserId(landUser);
						notificationDtoForLand.setCreatedBy(landUser);
						notificationDtoForLand.setUserType("O");
						notificationDetailsServiceImpl.submitNotification(notificationDtoForLand);
					}
				}
				
				//for citizen
				NotificationDTO notificationDto = new NotificationDTO();
				notificationDto.setNotification("Grievance  "+ generateGrievanceNo+"is created.");
				notificationDto.setUserId(fetchRecord.getIntCreatedBy() != null ?new BigInteger(fetchRecord.getIntCreatedBy().toString()):BigInteger.ZERO);
				notificationDto.setCreatedBy(fetchRecord.getIntCreatedBy() != null ?new BigInteger(fetchRecord.getIntCreatedBy().toString()):BigInteger.ZERO);
				notificationDto.setUserType("CI");
				notificationDetailsServiceImpl.submitNotification(notificationDto);

			} else {
				response.setStatus(400);
				response.setMessage("data required is not present");
			}

		} catch (Exception e) {
			log.error("error while saving grievance record: " + e.getMessage());
			response.setStatus(500);
			response.setMessage("error while saving grievance record");
		}
		return response;
	}

	@Override
	public GrievanceResponseDto fetchPendingGrievanceFromCO() {
		GrievanceResponseDto grievanceResponseDto = new GrievanceResponseDto();
		List<GrievanceMainDTO> dtos = mobileRepository.findByGrievanceStatus(1, "P");
		log.info("fetching record for pending grievance by CO");
		if (!dtos.isEmpty() && dtos != null) {
			for (GrievanceMainDTO dto : dtos) {
				if (!dto.getCoordinates().isEmpty()) {
					dto.setLandLatLong(convertCoordinates(dto.getCoordinates()));
				}//uploadFile
				if(dto.getUploadFile() != null && !dto.getUploadFile().isEmpty() ) {
					List<String> imagePaths = new ArrayList<>();
					ImageRequestDto imagedto = new ImageRequestDto();
					List<String> strList = new ArrayList<>();
					strList.add(dto.getUploadFile());
					imagedto.setFileNames(strList);
					imagedto.setForPurpose("4");
					imagedto.getFileNames().forEach(fileName -> {
						String imagePath = appUrl + "/viewDocuments/" + imagedto.getForPurpose() + "/" + fileName;
						imagePaths.add(imagePath.toString());
					});
					dto.setImageLink(imagePaths.get(0));
				}else {
					dto.setImageLink("");
				}
				
				
			}
			grievanceResponseDto.setGrievanceDtos(dtos);
			grievanceResponseDto.setStatus(200);
			grievanceResponseDto.setMessage("Fetching records of Pending grievence of CO");
			grievanceResponseDto.setStatusMessage(CommonConstant.SUCCESS);
		} else {
			grievanceResponseDto.setStatus(200);
			grievanceResponseDto.setStatusMessage(CommonConstant.FAILED);
			grievanceResponseDto.setMessage("No records found of Pending grievence of CO");
		}
		return grievanceResponseDto;
	}

	@Override
	public GrievanceResponseDto fetchCompleteGrievanceFromCO() {
		GrievanceResponseDto grievanceResponseDto = new GrievanceResponseDto();
		List<GrievanceMainDTO> dtos = mobileRepository.findByGrievanceStatus(3, "C");
		log.info("fetched record for completed grievance by CO");
		if (!dtos.isEmpty() && dtos != null) {
			for (GrievanceMainDTO dto : dtos) {
				if (!dto.getCoordinates().isEmpty()) {
					dto.setLandLatLong(convertCoordinates(dto.getCoordinates()));
				}
				if(dto.getUploadFile() != null && !dto.getUploadFile().isEmpty() ) {
					List<String> imagePaths = new ArrayList<>();
					ImageRequestDto imagedto = new ImageRequestDto();
					List<String> strList = new ArrayList<>();
					strList.add(dto.getUploadFile());
					imagedto.setFileNames(strList);
					imagedto.setForPurpose("4");
					imagedto.getFileNames().forEach(fileName -> {
						String imagePath = appUrl + "/viewDocuments/" + imagedto.getForPurpose() + "/" + fileName;
						imagePaths.add(imagePath.toString());
					});
					dto.setImageLink(imagePaths.get(0));
				}else {
					dto.setImageLink("");
				}
				if(!dto.getCoUploadedPhoto().isEmpty() && dto.getCoUploadedPhoto() != null) {
					List<String> imagePaths = new ArrayList<>();
					ImageRequestDto imagedto = new ImageRequestDto();
					List<String> strList = new ArrayList<>();
					strList.add(dto.getCoUploadedPhoto());
					imagedto.setFileNames(strList);
					imagedto.setForPurpose("4");
					imagedto.getFileNames().forEach(fileName -> {
						String imagePath = appUrl + "/viewDocuments/" + imagedto.getForPurpose() + "/" + fileName;
						imagePaths.add(imagePath.toString());
					});
					dto.setVerifiedImageLink(imagePaths.get(0));
				}else {
					dto.setVerifiedImageLink("");
				}
			}
			grievanceResponseDto.setGrievanceDtos(dtos);
			grievanceResponseDto.setStatus(200);
			grievanceResponseDto.setStatusMessage(CommonConstant.SUCCESS);
			grievanceResponseDto.setMessage("Fetched records of Completed grievence of CO");
		} else {
			grievanceResponseDto.setStatus(200);
			grievanceResponseDto.setStatusMessage(CommonConstant.FAILED);
			grievanceResponseDto.setMessage("No records found of Completed grievence of CO");
		}

		return grievanceResponseDto;
	}

	@Override
	public GrievanceResponseDto fetchPendingAndCompleteGrievanceCount() {
		GrievanceResponseDto grievanceResponseDto = new GrievanceResponseDto();
		List<GrievanceMainDTO> grievanceList = mobileRepository.findByGrievanceStatus(1, "P");
		List<GrievanceMainDTO> grievanceList1 = mobileRepository.findByGrievanceStatus(3, "C");
		log.info("fetching record");
		grievanceResponseDto.setStatus(200);
		grievanceResponseDto.setMessage("Fetching Pending and Completed records of grievence and Land Application");
		grievanceResponseDto.setPendingCount(grievanceList.size());
		grievanceResponseDto.setCompletedCount(grievanceList1.size());
		grievanceResponseDto.setLandPendingCount(landInspectionRepository.findByNullCoRemarks().size());
		grievanceResponseDto.setLandCompletedCount(landInspectionRepository.findByCoRemarks().size());
		grievanceResponseDto.setLandPostAllotmentPendingCount(landPostAllotmentRepository.fetchNullCoRemarksCount());
		grievanceResponseDto.setLandPostAllotmentCompletedCount(landPostAllotmentRepository.fetchCoRemarksCount());
		return grievanceResponseDto;
	}
	
	@Override
	public GrievanceResponseDto areaForPlot(GrievanceMainDTO grievanceMainDTO) {
		GrievanceResponseDto response = new GrievanceResponseDto();
		GrievanceMainDTO dto = mobileRepository.findDetailsByPlotCode(grievanceMainDTO.getPlotNo());
		if(dto.getPlotNumber() != null) {
			response.setStatus(200);
			response.setMessage("Data succesfully fetched ");
			response.setStatusMessage("success");
			response.setGrievance(dto);
		}else {
			response.setStatus(200);
			response.setMessage("No data found for given plot ");
			response.setStatusMessage("failed");
			response.setGrievance(dto);
		}
		return response;
	}

	@Override
	public GrievanceResponseDto fetchGrievanceByGrievanceNo(String grievanceNo) {
		GrievanceResponseDto response = new GrievanceResponseDto();
		GrievanceMainDTO dto = mobileRepository.findDetailsByGrievanceNumber(grievanceNo);
		if (dto.getGrievanceId() != null) {
			log.info("result: " + dto);
			response.setStatus(200);
			response.setMessage("data fetched based on grievance number");
			if (!dto.getCoordinates().isEmpty()) {
				dto.setLandLatLong(convertCoordinates(dto.getCoordinates()));
				response.setGrievance(dto);
			} else {
				response.setGrievance(dto);
			}
			if(dto.getUploadFile() != null && !dto.getUploadFile().isEmpty() ) {
				List<String> imagePaths = new ArrayList<>();
				ImageRequestDto imagedto = new ImageRequestDto();
				List<String> strList = new ArrayList<>();
				strList.add(dto.getUploadFile());
				imagedto.setFileNames(strList);
				imagedto.setForPurpose("4");
				imagedto.getFileNames().forEach(fileName -> {
					String imagePath = appUrl + "/viewDocuments/" + imagedto.getForPurpose() + "/" + fileName;
					imagePaths.add(imagePath.toString());
				});
				dto.setImageLink(imagePaths.get(0));
			}else {
				dto.setImageLink("");
			}
			if(!dto.getCoUploadedPhoto().isEmpty() && dto.getCoUploadedPhoto() != null) {
				List<String> imagePaths1 = new ArrayList<>();
				ImageRequestDto imagedto = new ImageRequestDto();
				List<String> strList1 = new ArrayList<>();
				strList1.add(dto.getCoUploadedPhoto());
				imagedto.setFileNames(strList1);
				imagedto.setForPurpose("4");
				imagedto.getFileNames().forEach(fileName -> {
					String imagePath1 = appUrl + "/viewDocuments/" + imagedto.getForPurpose() + "/" + fileName;
					imagePaths1.add(imagePath1.toString());
				});
				dto.setVerifiedImageLink(imagePaths1.get(0));
			}else {
				dto.setVerifiedImageLink("");
			}
		
			response.setStatusMessage(CommonConstant.SUCCESS);
		} else {
			response.setStatus(200);
			response.setMessage("no data present for grievance number");
			response.setStatusMessage(CommonConstant.FAILED);
		}
		return response;
	}

	public List<LandLatLng> convertCoordinates(String coordinatesString) {
		String[] coordinatesArr = coordinatesString.split("\\(");
		String coordinates = coordinatesArr[3];
		String withoutBackParenthesis = coordinates.replace(")", "");
		String[] str = withoutBackParenthesis.split(",");
		List<LandLatLng> coordList = new ArrayList<>();
		for (int a = 0; a < str.length; a++) {
			String[] s1 = str[a].split(" ");
			
			LandLatLng object = new LandLatLng();
			object.setLng(s1[0]);
			object.setLat(s1[1]);
			coordList.add(object);
		}
		return coordList;
	}

	@Override
	public GrievanceResponseDto saveMobile(String mobileNo) throws NoSuchAlgorithmException {
		GrievanceResponseDto response = new GrievanceResponseDto();// getUserCountByMobileAndEmailFirstRegisterTab
		int count = repo.getUserCountByMobileAndEmailFirstRegisterTabTemp(mobileNo);
		String randomNumber = OtpGenerateCommonUtil.generateOTP();
		if (count > 0) {
			repo.updateMobileNoOrOtp(mobileNo, randomNumber);
			response.setStatus(200);
			response.setMessage("mobile number already exists, otp generated");
			response.setOtp(randomNumber);
			response.setMobileNo(mobileNo);
			// something about user mobile number
		} else {
			repo.insertMobileAndOTP(mobileNo, randomNumber);
			response.setStatus(200);
			response.setMessage("mobile number saved successfully and otp generated");
			response.setOtp(randomNumber);

		}
		return response;
	}

	@Override
	public GrievanceResponseDto otpVerify(LandRegisterMobileNoOrOtpVerifiedDTO otpDto) {
		GrievanceResponseDto response = new GrievanceResponseDto();// getUserCountByMobileAndEmailFirstRegisterTab
		String result = repo.getOTPByMobileNo(otpDto.getMobileno());
		if (otpDto.getOtp().equals(result)) {
			response.setStatus(200);
			response.setMessage("OTP verified Successful");
			response.setOtpStatus(CommonConstant.SUCCESS);
		} else {
			response.setStatus(200);
			response.setMessage("Otp verification failed");
			response.setOtpStatus(CommonConstant.FAILED);
		}
		return response;
	}

	public GrievanceMainDTO mapToDto(Grievance row) {
		GrievanceMainDTO grievanceMainDTO = new GrievanceMainDTO();
		grievanceMainDTO.setGrievanceId(row.getIntId());
		grievanceMainDTO.setMonthId(
				row.getSelMonthofUnauthorizedOccupation() != null ? row.getSelMonthofUnauthorizedOccupation().toString()
						: "");
		grievanceMainDTO.setName(row.getTxtName() != null ? row.getTxtName() : "");
		grievanceMainDTO.setFatherName(row.getTxtFatherName() != null ? row.getTxtFatherName() : "");
		grievanceMainDTO.setDistrictId(row.getSelDistrict() != null ? row.getSelDistrict().toString() : "");
		grievanceMainDTO.setBlockId(row.getSelBlock() != null ? row.getSelBlock().toString() : "");
		grievanceMainDTO.setGpId(row.getSelGP() != null ? row.getSelGP().toString() : "");
		grievanceMainDTO.setVillageId(row.getSelVillage() != null ? row.getSelVillage().toString() : "");
		grievanceMainDTO.setOtherInformation(row.getTxtOtherInformation() != null ? row.getTxtOtherInformation() : "");
		grievanceMainDTO.setCasteName(row.getSelCaste() != null ? row.getSelCaste() : "");
		grievanceMainDTO.setMobileNo(row.getTxtMobileNo() != null ? row.getTxtMobileNo() : "");
		grievanceMainDTO.setDiscloseDetails(
				row.getChkDiscloseyourdetails() != null ? (Short) row.getChkDiscloseyourdetails() : 0);
		grievanceMainDTO.setDistrictCode(row.getSelDistrict13() != null ? row.getSelDistrict13() : "");
		grievanceMainDTO.setTahasilCode(row.getSelTahasil() != null ? row.getSelTahasil() : "");
		grievanceMainDTO.setVillageCode(row.getSelVillage15() != null ? row.getSelVillage15() : "");
		grievanceMainDTO.setKhatianCode(row.getSelKhataNo() != null ? row.getSelKhataNo() : "");
		grievanceMainDTO.setPlotNo(row.getSelPlotNo() != null ? row.getSelPlotNo() : "");
		grievanceMainDTO
				.setAreaAcre(row.getTxtTotalAreainacre() != null ? row.getTxtTotalAreainacre().toString() : "0");
		grievanceMainDTO
				.setExtentOccupied(row.getTxtExtentOccupied() != null ? row.getTxtExtentOccupied().toString() : "0");
		grievanceMainDTO
				.setModeOfOccupation(row.getSelModeofOccupation() != null ? (Short) row.getSelModeofOccupation() : 0);
		grievanceMainDTO.setOtherOccupation(row.getTxtOccupationDetails() != null ? row.getTxtOccupationDetails() : "");
		grievanceMainDTO.setLandmark(row.getTxtLandmark() != null ? row.getTxtLandmark() : "");
		grievanceMainDTO.setUploadFile(row.getFileFileUpload() != null ? row.getFileFileUpload() : "");
		grievanceMainDTO.setGrievanceNo(row.getGrievanceNumber() != null ? row.getGrievanceNumber() : "");
		grievanceMainDTO.setRemarks(row.getTxtrTextarea() != null ? row.getTxtrTextarea() : "");
		grievanceMainDTO.setLatitude("");
		grievanceMainDTO.setLongitude("");

		return grievanceMainDTO;
	}

}
