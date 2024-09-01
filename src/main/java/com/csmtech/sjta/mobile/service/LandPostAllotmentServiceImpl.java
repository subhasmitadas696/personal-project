package com.csmtech.sjta.mobile.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.xml.bind.DatatypeConverter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.GenericResponse;
import com.csmtech.sjta.dto.NotificationDTO;
import com.csmtech.sjta.mobile.dto.ImageRequestDto;
import com.csmtech.sjta.mobile.dto.LandPostAllotmentDto;
import com.csmtech.sjta.mobile.dto.LandPostAllotmentResponseDto;
import com.csmtech.sjta.mobile.entity.LandPostAllotmentEntity;
import com.csmtech.sjta.mobile.repository.LandInspectionRepository;
import com.csmtech.sjta.mobile.repository.LandPostAllotmentRepository;
import com.csmtech.sjta.mobile.repository.NotificationDetailsRepository;
import com.csmtech.sjta.util.CommonConstant;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LandPostAllotmentServiceImpl implements LandPostAllotmentService {

	@Autowired
	LandPostAllotmentRepository landPostAllotmentRepository;

	@Autowired
	GrievanceMobileServiceImpl grievanceMobileServiceImpl;
	
	@Autowired
	LandInspectionRepository landInspectionRepository;

	@Autowired
	TahasilServiceImpl tahasilService;
	
	@Autowired
	NotificationDetailsRepository notificationDetailsRepo;
	
	@Autowired
	NotificationDetailsServiceImpl notificationDetailsServiceImpl;

	@Value("${file.path}")
	private String finalUploadPath;

	@Override
	public LandPostAllotmentResponseDto assignToCOForLandPostAllotment(LandPostAllotmentDto landPlotAllotmentDto) {
		LandPostAllotmentResponseDto response = new LandPostAllotmentResponseDto();
		try {
			LandPostAllotmentEntity landPostAllotmentEntity = new LandPostAllotmentEntity();
			Date parsedDate1 = new Date();
			if (landPlotAllotmentDto.getScheduledInspectionDate() != null
					|| !landPlotAllotmentDto.getScheduledInspectionDate().isEmpty()) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				try {
					parsedDate1 = dateFormat.parse(landPlotAllotmentDto.getScheduledInspectionDate());

				} catch (ParseException e) {
					log.error("scheduled inspection date error: " + e.getMessage());
				}
			}

			landPostAllotmentEntity = landPostAllotmentRepository
					.findByPlotCodeAndScheduledInspectionDate(landPlotAllotmentDto.getPlotCode(), parsedDate1);
			if (landPostAllotmentEntity != null) {
				response.setStatus(210);
				response.setMessage("You can not assign the same plot on the same date");
			} else {
				int status = 1;
				int i = landPostAllotmentRepository.updateAssignStatusToCO(landPlotAllotmentDto.getDistrictCode(),
						landPlotAllotmentDto.getTahasilCode(), landPlotAllotmentDto.getVillageCode(),
						landPlotAllotmentDto.getKhatianCode(), landPlotAllotmentDto.getPlotCode(), parsedDate1,
						landPlotAllotmentDto.getLoRemarks(), landPlotAllotmentDto.getCreatedBy(), status,
						BigDecimal.valueOf(Double.parseDouble(landPlotAllotmentDto.getTotalArea())),
						BigDecimal.valueOf(Double.parseDouble(landPlotAllotmentDto.getPurchaseArea())),
						BigDecimal.valueOf(Double.parseDouble(landPlotAllotmentDto.getPricePerAcre())),
						BigDecimal.valueOf(Double.parseDouble(landPlotAllotmentDto.getTotalPrice())));
				if (i > 0) {
					response.setStatus(200);
					response.setMessage("Assigned to CO successfully");
					String token = landInspectionRepository.fetchFcmToken();
					if(token != null && !token.isEmpty()) {
						response.setFcmToken(token);
					}
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
	public LandPostAllotmentResponseDto coInspectionSubmit(LandPostAllotmentDto landPlotAllotmentDto) {
		LandPostAllotmentResponseDto response = new LandPostAllotmentResponseDto();
		try {
			Date parsedDate1 = new Date();
			Date parsedDate = new Date();
			String fileName = "";
			if (landPlotAllotmentDto.getScheduledInspectionDate() != null
					|| !landPlotAllotmentDto.getScheduledInspectionDate().isEmpty()) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				try {
					parsedDate = dateFormat.parse(landPlotAllotmentDto.getScheduledInspectionDate());

				} catch (ParseException e) {
					log.error("scheduled inspection date error: " + e.getMessage());
				}
			}
			LandPostAllotmentEntity landPostAllotmentEntity = landPostAllotmentRepository
					.findByPlotCodeAndScheduledInspectionDate(landPlotAllotmentDto.getPlotCode(), parsedDate);
			log.info("checking entity value: " + landPostAllotmentEntity);
			if (landPlotAllotmentDto.getCoUploadedPhoto() != null
					&& !landPlotAllotmentDto.getCoUploadedPhoto().isEmpty()
					&& !landPlotAllotmentDto.getCoUploadedPhoto().trim().isEmpty()) {
				fileName = landPostAllotmentEntity.getPostAllotmentInspectionId() + "-PostAllotmentInspection"
						+ landPostAllotmentEntity.getPlotCode() + ".jpg";
			}
			landPlotAllotmentDto.setFileName(fileName);

			if (landPlotAllotmentDto.getInspectionDate() != null
					|| !landPlotAllotmentDto.getInspectionDate().isEmpty()) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				try {
					parsedDate1 = dateFormat.parse(landPlotAllotmentDto.getInspectionDate());

				} catch (ParseException e) {
					log.error("inspection date error: " + e.getMessage());
				}
			}

			if (landPostAllotmentEntity != null) {
				landPostAllotmentEntity.setCoRemarks(landPlotAllotmentDto.getCoRemarks());
				landPostAllotmentEntity.setCoUploadedPhoto(fileName);
				landPostAllotmentEntity.setInspectionDate(parsedDate1);
				landPostAllotmentEntity.setLatitude(
						landPlotAllotmentDto.getLatitude() != null ? landPlotAllotmentDto.getLatitude() : "");
				landPostAllotmentEntity.setLongitude(
						landPlotAllotmentDto.getLongitude() != null ? landPlotAllotmentDto.getLongitude() : "");
				landPostAllotmentRepository.save(landPostAllotmentEntity);
				if (fileName != null && !fileName.isEmpty() && !fileName.trim().isEmpty()) {
					GenericResponse resp = saveFileTodirectory(landPlotAllotmentDto, "COORDINATE OFFICER");
					if (resp.getMessage() != null) {
						response.setMessage(resp.getMessage());
						response.setStatus(32);
					}
				}
				response.setStatus(200);
				response.setMessage("CO inspection data saved successfully");
				
				//add the manual notification for Leased Land Verification done by CO and notify to Land Officer
				String[] plotNosPart = landPlotAllotmentDto.getPlotCode().split("-");
				String plotNo = plotNosPart[1];
				List<BigInteger> landUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("4"));
				if(landUserList != null && landUserList.size() > 0) {
					for(BigInteger landUser:landUserList) {
						//gfghhg
						NotificationDTO notificationDtoForLand = new NotificationDTO();
						notificationDtoForLand.setNotification("Land verification has been done by CO for plot "+plotNo );
						notificationDtoForLand.setUserId(landUser);
						notificationDtoForLand.setCreatedBy(landUser);
						notificationDtoForLand.setUserType("O");
						notificationDetailsServiceImpl.submitNotification(notificationDtoForLand);
					}
				}
				
			} else {
				response.setMessage("kindly assign the plot first");
			}
		} catch (Exception e) {
			log.error("error while persisting data into db: " + e.getMessage());
			response.setStatus(500);
			response.setMessage("error while persisting data into db");
		}
		return response;
	}

	public GenericResponse saveFileTodirectory(LandPostAllotmentDto landDto, String savedBy) {
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
			filePath = finalUploadPath + "/PostAllotmentInspection";
			File folder = new File(filePath);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			filepath1 = finalUploadPath + "/PostAllotmentInspection/" + "PlotSurvey";
			File folder1 = new File(filepath1);
			if (!folder1.exists()) {
				folder1.mkdirs();
			}
			file = new File(finalUploadPath + "/PostAllotmentInspection/" + "PlotSurvey/" + fileName);
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
	public LandPostAllotmentResponseDto fetchPrePostAllotmentList(LandPostAllotmentDto landPlotAllotmentDto) {
		LandPostAllotmentResponseDto response = new LandPostAllotmentResponseDto();
		List<LandPostAllotmentDto> dtoList = new ArrayList<>();
		List<Object[]> initialList = landPostAllotmentRepository.fetchPrePostAllotmentList();
		if (initialList != null && initialList.size() > 0) {
			dtoList = mapToLandPostAllotmentDtoInitial(initialList);
			response.setStatus(200);
			response.setMessage("Fetching records of land post allotment ");
			response.setStatusMessage(CommonConstant.SUCCESS);
			response.setLandPostAllotmentList(dtoList);
			response.setCount(dtoList.size());

		} else {
			response.setStatus(200);
			response.setMessage("No records found");
			response.setStatusMessage(CommonConstant.FAILED);
		}
		return response;
	}

	@Override
	public LandPostAllotmentResponseDto fetchPostAllotmentList(LandPostAllotmentDto landPlotAllotmentDto) {
		LandPostAllotmentResponseDto response = new LandPostAllotmentResponseDto();
		List<Object[]> initialVillageList = landPostAllotmentRepository
				.getVillageDetails(landPlotAllotmentDto.getTahasilCode());
		List<LandPostAllotmentDto> initialDtoList = mappingToDto(initialVillageList);
		List<LandPostAllotmentDto> dtoList = initialDtoList.stream().filter(item -> item.getTotalPlot().intValue() > 0)
				.collect(Collectors.toList());
		if (dtoList != null && dtoList.size() > 0) {
			response.setMessage("data fetched successfully for tahasil code");
			response.setStatus(200);
			response.setStatusMessage("success");
			response.setLandPostAllotmentList(dtoList);
		} else {
			response.setMessage("no data fetched for tahasil code");
			response.setStatus(200);
			response.setStatusMessage("failed");
			response.setLandPostAllotmentList(dtoList);
		}

		return response;
	}

	@Override
	public LandPostAllotmentResponseDto fetchPostAllotmentPendingPlotDetails(
			LandPostAllotmentDto landPlotAllotmentDto) {
		LandPostAllotmentResponseDto response = new LandPostAllotmentResponseDto();
		List<Object[]> plotList = landPostAllotmentRepository
				.fetchPostAllotmentPendingPlotDetails(landPlotAllotmentDto.getVillageCode());
		List<LandPostAllotmentDto> dtoList = mappingToDtoForPendingPLotDetails(plotList);
		if (dtoList != null && dtoList.size() > 0) {
			log.info("checking for coordinates");
			for (LandPostAllotmentDto dto : dtoList) {
				if (!dto.getCoordinates().isEmpty()) {
					dto.setLandLatLong(grievanceMobileServiceImpl.convertCoordinates(dto.getCoordinates()));
				}else {
					dto.setLandLatLong(new ArrayList<>());
				}
			}
			response.setMessage("data fetched successfully for list of plots of the village");
			response.setStatus(200);
			response.setStatusMessage(CommonConstant.SUCCESS);
			response.setLandPostAllotmentList(dtoList);
		} else {
			response.setMessage("Village details does not exist ");
			response.setStatus(200);
			response.setStatusMessage(CommonConstant.FAILED);
		}
		return response;
	}

	@Override
	public LandPostAllotmentResponseDto fetchPostAllotmentCompletedList(LandPostAllotmentDto landPlotAllotmentDto) {
		LandPostAllotmentResponseDto response = new LandPostAllotmentResponseDto();
		List<LandPostAllotmentDto> dtoList = new ArrayList<>();
		List<Object[]> initialCompletedList = landPostAllotmentRepository.fetchPostAllotmentCompletedList();
		dtoList = mapToLandPostAllotmentDtoCompleted(initialCompletedList);
		if (dtoList != null && dtoList.size() > 0) {
			for (LandPostAllotmentDto dto : dtoList) {
				if (!dto.getCoordinates().isEmpty()) {
					dto.setLandLatLong(grievanceMobileServiceImpl.convertCoordinates(dto.getCoordinates()));
				}else {
					dto.setLandLatLong(new ArrayList<>());
				}
				if (dto.getCoUploadedPhoto() != null && !dto.getCoUploadedPhoto().isEmpty()) {
					ImageRequestDto imageDto = new ImageRequestDto();
					List<String> strList = new ArrayList<>();
					strList.add(dto.getCoUploadedPhoto());
					LandPostAllotmentDto postAllotmentDto = new LandPostAllotmentDto();
					imageDto.setFileNames(strList);
					imageDto.setForPurpose("5");
					List<String> strList1 = new ArrayList<>();
					strList1 = tahasilService.viewImage(imageDto);
					dto.setImageLink(strList1.get(0));

				} else {
					dto.setImageLink("");
				}

			}
			response.setStatus(200);
			response.setStatusMessage(CommonConstant.SUCCESS);
			response.setMessage("Fetching records of land post allotment completed list");
			response.setLandPostAllotmentList(dtoList);
			response.setCount(dtoList.size());
		} else {
			response.setStatus(200);
			response.setStatusMessage(CommonConstant.FAILED);
			response.setMessage("No records found");
		}
		return response;
	}

	@Override
	public LandPostAllotmentResponseDto fetchPostAllotmentCompletedPlot(LandPostAllotmentDto landPlotAllotmentDto) {
		LandPostAllotmentResponseDto response = new LandPostAllotmentResponseDto();
		List<LandPostAllotmentDto> dtoList = new ArrayList<>();
		List<Object[]> initialCompletedList = landPostAllotmentRepository.fetchPostAllotmentCompletedList();
		dtoList = mapToLandPostAllotmentDtoCompleted(initialCompletedList);
		List<LandPostAllotmentDto> resultList = dtoList.stream().filter(
				dto -> dto.getPostAllotmentInspectionId().equals(landPlotAllotmentDto.getPostAllotmentInspectionId()))
				.collect(Collectors.toList());

		if (resultList != null && resultList.size() > 0) {
			for (LandPostAllotmentDto dto : resultList) {
				if (!dto.getCoordinates().isEmpty()) {
					dto.setLandLatLong(grievanceMobileServiceImpl.convertCoordinates(dto.getCoordinates()));
				}else {
					dto.setLandLatLong(new ArrayList<>());
				}
				if (dto.getCoUploadedPhoto() != null && !dto.getCoUploadedPhoto().isEmpty()) {
					ImageRequestDto imageDto = new ImageRequestDto();
					List<String> strList = new ArrayList<>();
					strList.add(dto.getCoUploadedPhoto());
					LandPostAllotmentDto postAllotmentDto = new LandPostAllotmentDto();
					imageDto.setFileNames(strList);
					imageDto.setForPurpose("5");
					List<String> strList1 = new ArrayList<>();
					strList1 = tahasilService.viewImage(imageDto);
					dto.setImageLink(strList1.get(0));

				} else {
					dto.setImageLink("");
				}
			}
			response.setStatus(200);
			response.setStatusMessage(CommonConstant.SUCCESS);
			response.setMessage("Fetching records of land post allotment completed plot details ");
			response.setDto(resultList.get(0));
		} else {
			response.setStatus(200);
			response.setStatusMessage(CommonConstant.FAILED);
			response.setMessage("No records found");
		}
		return response;
	}

	public LandPostAllotmentResponseDto fetchPostAllotmentListDetails(LandPostAllotmentDto landPlotAllotmentDto) {
		LandPostAllotmentResponseDto response = new LandPostAllotmentResponseDto();
		List<LandPostAllotmentDto> dtoList = new ArrayList<>();
		List<Object[]> initialList = landPostAllotmentRepository.fetchPostAllotmentListDetails();
		dtoList = mapToLandPostAllotmentDtoCompleted(initialList);
		if (dtoList != null && dtoList.size() > 0) {
			for (LandPostAllotmentDto dto : dtoList) {
				if (!dto.getCoordinates().isEmpty()) {
					dto.setLandLatLong(grievanceMobileServiceImpl.convertCoordinates(dto.getCoordinates()));
				}else {
					dto.setLandLatLong(new ArrayList<>());
				}
				if (dto.getCoUploadedPhoto() != null && !dto.getCoUploadedPhoto().isEmpty()) {
					ImageRequestDto imageDto = new ImageRequestDto();
					List<String> strList = new ArrayList<>();
					strList.add(dto.getCoUploadedPhoto());
					LandPostAllotmentDto postAllotmentDto = new LandPostAllotmentDto();
					imageDto.setFileNames(strList);
					imageDto.setForPurpose("5");
					List<String> strList1 = new ArrayList<>();
					strList1 = tahasilService.viewImage(imageDto);
					dto.setImageLink(strList1.get(0));

				} else {
					dto.setImageLink("");
				}

			}
			response.setStatus(200);
			response.setStatusMessage(CommonConstant.SUCCESS);
			response.setMessage("Fetching records of land post allotment completed list");
			response.setLandPostAllotmentList(dtoList);
			response.setCount(dtoList.size());
		} else {
			response.setStatus(200);
			response.setStatusMessage(CommonConstant.FAILED);
			response.setMessage("No records found");
		}
		return response;
	}

	public List<LandPostAllotmentDto> mappingToDtoForPendingPLotDetails(List<Object[]> plotList) {
		return plotList.stream().map(objects -> {
			LandPostAllotmentDto dto = new LandPostAllotmentDto();
			dto.setVillageCode(objects[0] != null ? objects[0].toString() : "");
			dto.setVillageName(objects[1] != null ? objects[1].toString() : "");
			dto.setTahasilCode(objects[2] != null ? objects[2].toString() : "");
			dto.setTahasilName(objects[3] != null ? objects[3].toString() : "");
			dto.setDistrictName(objects[4] != null ? objects[4].toString() : "");
			dto.setPlotNo(objects[5] != null ? objects[5].toString() : "");
			dto.setKissam(objects[6] != null ? objects[6].toString() : "");
			dto.setKhataNo(objects[7] != null ? objects[7].toString() : "");
			dto.setTotalAreaVillage(objects[8] != null ? objects[8].toString() : "");
			dto.setCentralLatitude(objects[9] != null ? objects[9].toString() : "");
			dto.setCentralLongitude(objects[10] != null ? objects[10].toString() : "");
			dto.setCoordinates(objects[11] != null ? objects[11].toString() : "");
			dto.setDistrictCode(objects[12] != null ? objects[12].toString() : "");
			dto.setPlotCode(objects[13] != null ? objects[13].toString() : "");
			dto.setKhatianCode(objects[14] != null ? objects[14].toString() : "");
			dto.setScheduledInspectionDate(objects[15] != null ? objects[15].toString() : "");
			dto.setLoRemarks(objects[16] != null ? objects[16].toString() : "");
			dto.setTotalArea(objects[17] != null ? objects[17].toString() : "");
			dto.setPurchaseArea(objects[18] != null ? objects[18].toString() : "");
			dto.setPricePerAcre(objects[19] != null ? objects[19].toString() : "");
			dto.setTotalPrice(objects[20] != null ? objects[20].toString() : "");
			return dto;
		}).collect(Collectors.toList());
	}

	public List<LandPostAllotmentDto> mappingToDto(List<Object[]> initialVillageList) {
		return initialVillageList.stream().map(
				objects -> new LandPostAllotmentDto((String) objects[0], (String) objects[1], (BigInteger) objects[2]))
				.collect(Collectors.toList());
	}

	public List<LandPostAllotmentDto> mapToLandPostAllotmentDtoInitial(List<Object[]> initialList) {
		return initialList.stream().map(objects -> {
			LandPostAllotmentDto dto = new LandPostAllotmentDto();
			dto.setDistrictCode(objects[0] != null ? objects[0].toString() : "");
			dto.setDistrictName(objects[1] != null ? objects[1].toString() : "");
			dto.setTahasilCode(objects[2] != null ? objects[2].toString() : "");
			dto.setTahasilName(objects[3] != null ? objects[3].toString() : "");
			dto.setVillageCode(objects[4] != null ? objects[4].toString() : "");
			dto.setVillageName(objects[5] != null ? objects[5].toString() : "");
			dto.setKhatianCode(objects[6] != null ? objects[6].toString() : "");
			dto.setKhataNo(objects[7] != null ? objects[7].toString() : "");
			dto.setPlotCode(objects[8] != null ? objects[8].toString() : "");
			dto.setPlotNo(objects[9] != null ? objects[9].toString() : "");
			dto.setTotalArea(objects[10] != null ? objects[10].toString() : "");
			dto.setPurchaseArea(objects[11] != null ? objects[11].toString() : "");
			dto.setPricePerAcre(objects[12] != null ? objects[12].toString() : "");
			dto.setTotalPrice(objects[13] != null ? objects[13].toString() : "");
			return dto;
		}).collect(Collectors.toList());
	}

	public List<LandPostAllotmentDto> mapToLandPostAllotmentDtoCompleted(List<Object[]> initialList) {
		return initialList.stream().map(objects -> {
			LandPostAllotmentDto dto = new LandPostAllotmentDto();
			dto.setPostAllotmentInspectionId(objects[0] != null ? (Integer) objects[0] : 0);
			dto.setDistrictCode(objects[1] != null ? objects[1].toString() : "");
			dto.setDistrictName(objects[2] != null ? objects[2].toString() : "");
			dto.setTahasilCode(objects[3] != null ? objects[3].toString() : "");
			dto.setTahasilName(objects[4] != null ? objects[4].toString() : "");
			dto.setVillageCode(objects[5] != null ? objects[5].toString() : "");
			dto.setVillageName(objects[6] != null ? objects[6].toString() : "");
			dto.setKhatianCode(objects[7] != null ? objects[7].toString() : "");
			dto.setKhataNo(objects[8] != null ? objects[8].toString() : "");
			dto.setPlotCode(objects[9] != null ? objects[9].toString() : "");
			dto.setPlotNo(objects[10] != null ? objects[10].toString() : "");
			dto.setKissam(objects[11] != null ? objects[11].toString() : "");
			dto.setCoRemarks(objects[12] != null ? objects[12].toString() : "");
			dto.setCoUploadedPhoto(objects[13] != null ? objects[13].toString() : "");
			dto.setInspectionDate(objects[14] != null ? objects[14].toString() : "");
			dto.setScheduledInspectionDate(objects[15] != null ? objects[15].toString() : "");
			dto.setLoRemarks(objects[16] != null ? objects[16].toString() : "");
			dto.setTotalArea(objects[17] != null ? objects[17].toString() : "");
			dto.setPurchaseArea(objects[18] != null ? objects[18].toString() : "");
			dto.setPricePerAcre(objects[19] != null ? objects[19].toString() : "");
			dto.setTotalPrice(objects[20] != null ? objects[20].toString() : "");
			dto.setCentralLatitude(objects[21] != null ? objects[21].toString() : "");
			dto.setCentralLongitude(objects[22] != null ? objects[22].toString() : "");
			dto.setCoordinates(objects[23] != null ? objects[23].toString() : "");

			return dto;
		}).collect(Collectors.toList());
	}

}
