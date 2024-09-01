package com.csmtech.sjta.mobile.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.GenericResponse;
import com.csmtech.sjta.dto.PaginationInRegisterDtoResponse;
import com.csmtech.sjta.dto.TahasilTeamUseRequestDto;
import com.csmtech.sjta.dto.UserDetails;
import com.csmtech.sjta.entity.LandAppRegistrationEntity;
import com.csmtech.sjta.entity.Tahasil_master;
import com.csmtech.sjta.mobile.dto.ImageRequestDto;
import com.csmtech.sjta.mobile.dto.LandDataDto;
import com.csmtech.sjta.mobile.dto.LandLatLng;
import com.csmtech.sjta.mobile.dto.LandTypeDto;
import com.csmtech.sjta.mobile.dto.LandUseDto;
import com.csmtech.sjta.mobile.dto.OtpResponseDto;
import com.csmtech.sjta.mobile.dto.TahasilPlotDto;
import com.csmtech.sjta.mobile.dto.TahasilResponseDTO;
import com.csmtech.sjta.mobile.dto.VillageDTO;
import com.csmtech.sjta.mobile.entity.TahasilLoginEntity;
import com.csmtech.sjta.mobile.entity.TahasilPlotEntity;
import com.csmtech.sjta.mobile.entity.TahasilPlotSurveyDetailsEntity;
import com.csmtech.sjta.mobile.repository.LandTypeRepository;
import com.csmtech.sjta.mobile.repository.LandUseRepository;
import com.csmtech.sjta.mobile.repository.MobileLoginRepository;
import com.csmtech.sjta.mobile.repository.TahasilMobileRepository;
import com.csmtech.sjta.mobile.repository.TahasilPlotSurveyDetailsRepository;
import com.csmtech.sjta.mobile.repository.TahasilPlotSurveyRepository;
import com.csmtech.sjta.mobile.repository.VillageDetailsRepository;
import com.csmtech.sjta.repository.LandAppRegistratationClassRepository;
import com.csmtech.sjta.repository.LandAppRegistrationRepository;
import com.csmtech.sjta.repository.TahasilMasterJPARepository;
import com.csmtech.sjta.serviceImpl.TahasilTeamUseServiceImpl;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.JwtUtil;
import com.csmtech.sjta.util.OtpGenerateCommonUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TahasilServiceImpl implements TahasilService {

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	private LandAppRegistrationRepository repo;

	@Autowired
	private LandAppRegistratationClassRepository landclassrepo;

	@Autowired
	TahasilMobileRepository tahasilRepository;

	@Autowired
	LoginServiceImpl loginServiceImpl;

	@Autowired
	GrievanceMobileServiceImpl grievanceMobileServiceImpl;

	@Autowired
	TahasilMasterJPARepository tMasterRepository;

	@Autowired
	TahasilTeamUseServiceImpl tahasilTeamUseServiceImpl;

	@Autowired
	VillageDetailsRepository villageRepository;

	@Autowired
	TahasilPlotSurveyRepository tahasilPlotSurvey;

	@Autowired
	TahasilPlotSurveyDetailsRepository tahasilPlotSurveyDetailsRepository;

	@Autowired
	LandUseRepository landUseRepository;

	@Autowired
	LandTypeRepository landTypeRepository;
	
	@Autowired
	private MobileLoginRepository mobileLoginRepo;

	@Value("${file.path}")
	private String finalUploadPath;

	@Value("${app.url}")
	private String appUrl;

	@Override
	public GenericResponse tahasilLogin(String token, TahasilTeamUseRequestDto tahasilDto) {
		GenericResponse response = new GenericResponse();
		boolean tokenValidation = jwtUtil.isTokenExpired(token);
		if (tokenValidation == true) {
			response.setStatus(401);
			response.setMessage("Token expired/Invalid ");
			return response;
		} else {
			if ((tahasilDto.getTahasilUserName() != null && tahasilDto.getPassword() != null) && (token != null)) {
				LandAppRegistrationEntity tahasilResultDto = repo
						.findByTahasilUserNameDeletedOrNot(tahasilDto.getTahasilUserName());
//						 LandAppRegistrationEntity getalldata = repo.findByUserNameDeletedOrNot(authRequest.getUsername());
//						tahasilRepository.findByTahasilCode(tahasilDto.getTahasilCode());
				if (tahasilResultDto == null) {
					response.setMessage("Data is not present for the required tahasil user");
					response.setStatus(404);
				} else {
					boolean isMatch = loginServiceImpl.passwordMatch(tahasilDto.getPassword(),
							tahasilResultDto.getPassword());
					log.info("userPassword " + tahasilDto.getPassword() + " userstoredPassword "
							+ tahasilResultDto.getPassword());
					if (isMatch) {
						log.info("password matched");
						response.setMessage("Tahasil Login Successful");
						response.setStatus(200);

					} else {
						response.setStatus(404);
						response.setMessage("Invalid Password");
						return response;
					}

				}
			} else {
				response.setMessage("Insufficient data");

			}
			return response;
		}
	}

	@SuppressWarnings("unused")
	@Override
	public OtpResponseDto tahasilMobileInsertion(TahasilTeamUseRequestDto tahasilDto) throws NoSuchAlgorithmException {
		OtpResponseDto response = new OtpResponseDto();
		LandAppRegistrationEntity entity = new LandAppRegistrationEntity();
		if (tahasilDto.getMobileNo() != null || tahasilDto.getPassword() != null) {
			LandAppRegistrationEntity tahasilResultDto = repo
					.findByTahasilUserNameDeletedOrNot(tahasilDto.getTahasilUserName());
			LandAppRegistrationEntity entityResult = repo.findByTahasilCode(tahasilDto.getTahasilCode());
			if (tahasilResultDto == null) {
				response.setMessage("Data is not present for the required tahasil");
				response.setStatus(404);
			} else {
				tahasilResultDto.setMobileno(tahasilDto.getMobileNo());
				String randomNumber = OtpGenerateCommonUtil.generateOTP();
				tahasilResultDto.setOtp(randomNumber);
				log.info("status result: " + tahasilResultDto.getStatus());
				tahasilResultDto.setStatus("0");
				log.info("AFTER status result: " + tahasilResultDto.getStatus());
				try {
					log.info("trying to save record with mobile number in db");
					log.info("entityResult data: " + tahasilResultDto);
					repo.save(tahasilResultDto);
					log.info("mobile record inserted successfully");
					response.setStatus(200);
					response.setOtp(randomNumber);
					response.setMessage("Mobile Number added successfully");

				} catch (Exception e) {
					response.setMessage("Error occured while inserting mobile number: " + e.getMessage());
					response.setStatus(500);
				}
			}
		}
		return response;

	}

	public TahasilLoginEntity tahasilDtoToTahasilEntity(TahasilTeamUseRequestDto tahasilDto) {
		log.info("dto to entity transformation");
		TahasilLoginEntity entity = new TahasilLoginEntity();
		entity.setTahasilId(tahasilDto.getTahasilId());
		entity.setTahasilCode(tahasilDto.getTahasilCode());
		entity.setPassword(tahasilDto.getPassword());
		entity.setMobileNo(tahasilDto.getMobileNo());
		entity.setOtp(tahasilDto.getOtp());

		return entity;
	}

	@Override
	public TahasilResponseDTO tahasilOtpVerification(TahasilTeamUseRequestDto tahasilDto) {
		TahasilResponseDTO response = new TahasilResponseDTO();
		String result = landclassrepo.fetchOTPByMobileNo(tahasilDto.getMobileNo());
		LandAppRegistrationEntity tahasilResultDto = repo
				.findByTahasilUserNameDeletedOrNot(tahasilDto.getTahasilUserName());
//		String result = tahasilRepository.findOtpByTahasilCodeAndMobileNo(tahasilDto.getTahasilCode(),tahasilDto.getMobileNo());
		log.info("otp fetching from database " + result);
		UserDetails user = mobileLoginRepo.getUserDetails(tahasilDto.getMobileNo());
		response.setTahasilCode(tahasilResultDto.getTahasilCode());
		if (tahasilDto.getOtp().equals(result)) {
			response.setStatus(200);
			response.setMessage("OTP verified Successful");
			response.setStatusMessage("success");
			Tahasil_master tahasilMasterEntity = tMasterRepository
					.findByTxtTahasilCode(tahasilResultDto.getTahasilCode());
			response.setTahasilName(tahasilMasterEntity.getTxtTahasilName());
			response.setUserdetails(user);
		} else {
			response.setStatus(200);
			response.setMessage("Otp verification failed");
			response.setStatusMessage("failed");
		}
		return response;
	}

	@Override
	public TahasilResponseDTO getVillageDetails(TahasilTeamUseRequestDto requestDto) {
		TahasilResponseDTO response = new TahasilResponseDTO();
		List<VillageDTO> village = villageRepository.getVillageDetails(requestDto.getTahasilCode());
		List<VillageDTO> villageList = village.stream().filter(item -> item.getTotalPlot().intValue() > 0)
				.collect(Collectors.toList());
		if (villageList.size() > 0 && villageList != null) {
			response.setMessage("data fetched successfully for tahasil code");
			response.setStatus(200);
			response.setStatusMessage("success");
			response.setVillageDTOs(villageList);
		} else {
			response.setMessage("no data fetched for tahasil code");
			response.setStatus(200);
			response.setStatusMessage("failed");
			response.setVillageDTOs(villageList);
		}

		return response;
	}

	@SuppressWarnings("unused")
	@Override
	public TahasilResponseDTO getVillageDetailsByPlot(VillageDTO villageDto) {
		log.info("fetching resultset from db");
		TahasilResponseDTO response = new TahasilResponseDTO();
		log.info("inside service method before execution");
		List<VillageDTO> village = villageRepository.getVillageDetailsByPlot(villageDto.getVillageCode());
		log.info("inside service method after execution checking result: " + village);
		if (village.size() > 0 && village != null) {
			log.info("checking");
			for (VillageDTO dto : village) {
				LandLatLng landLatLng = new LandLatLng();
				if (!dto.getCoordinates().isEmpty()) {
					dto.setLandLatLong(grievanceMobileServiceImpl.convertCoordinates(dto.getCoordinates()));
				}else {
					dto.setLandLatLong(new ArrayList<>());
				}
			}
			response.setMessage("data fetched successfully for tahasil code");
			response.setStatus(200);
			response.setStatusMessage("success");
			response.setVillageDTOs(village);
			response.setTahasilCode(village.get(0).getTahasilCode());
			response.setTahasilName(village.get(0).getTahasilName());
		} else {
			response.setMessage("Village details does not exist ");
			response.setStatus(200);
			response.setStatusMessage("failed");
		}
		return response;
	}

	@Override
	public TahasilResponseDTO tahasilPlotList(PaginationInRegisterDtoResponse res) {
		TahasilResponseDTO response = new TahasilResponseDTO();
		log.info("fetching resultset from db for tahasilPlotList");
		List<TahasilPlotDto> dtos = new ArrayList<>();
		List<Object[]> resultList = villageRepository.tahasilPlotList(res.getTahasilCode(), res.getPageNumber(),
				res.getPageSize());
		if (resultList != null && !resultList.isEmpty()) {
			dtos = resultList.stream().map(objects -> new TahasilPlotDto(
					(objects[0] != null) ? (Integer) objects[0] : 0, (objects[1] != null) ? (String) objects[1] : "",
					(objects[2] != null) ? (String) objects[2] : "", (objects[3] != null) ? (String) objects[3] : "",
					(objects[4] != null) ? (String) objects[4] : "", (objects[5] != null) ? (String) objects[5] : "",
					(objects[6] != null) ? (String) objects[6] : "", (objects[7] != null) ? (String) objects[7] : "",
					(objects[8] != null) ? (String) objects[8] : "", (objects[9] != null) ? (String) objects[9] : ""))
					.collect(Collectors.toList());

			log.info("data from db: " + dtos);
			response.setStatus(200);
			response.setCount(villageRepository.countTahasilPlotList(res.getTahasilCode()).intValue());
			response.setMessage("Fetching records of tahasilPlotList land use verification by tahasil");
			response.setStatusMessage(CommonConstant.SUCCESS);
			response.setTahasilPlotDtosList(dtos);
		} else {
			response.setStatus(200);
			response.setMessage("No records available of tahasilPlotList land use verification by tahasil");
			response.setStatusMessage(CommonConstant.FAILED);
		}

		return response;
	}

	@Override
	public TahasilResponseDTO fetchLandUseVerificationCompletedDetails(TahasilPlotDto tahasilPlotDto) {
		TahasilResponseDTO response = new TahasilResponseDTO();
		log.info("fetching resultset from db for fetchLandUseVerificationCompletedDetails");
		List<TahasilPlotDto> dtos = new ArrayList<>();

		List<Object[]> resultList = villageRepository.fetchLandUseVerificationDetails(tahasilPlotDto.getTahasilCode());
		if (resultList != null && !resultList.isEmpty()) {
			dtos = mapToDtoForTahasil(resultList);
			if (dtos.size() > 0 && dtos != null) {
				for (TahasilPlotDto dto : dtos) {
					if (!dto.getCoordinates().isEmpty()) {
						dto.setLandLatLong(grievanceMobileServiceImpl.convertCoordinates(dto.getCoordinates()));
					}else {
						dto.setLandLatLong(new ArrayList<>());
					}
					if (dto.getPlotSurveyId() != null) {
						List<LandDataDto> landDataDtosList = new ArrayList<>();
						List<Object[]> list1 = tahasilPlotSurveyDetailsRepository
								.findByPlotSurveyId(dto.getPlotSurveyId());
						if (list1.size() > 0 && list1 != null) {
							landDataDtosList = fetchlandDataDtosList(list1);
						}
						log.info("result: " + landDataDtosList);
						if (landDataDtosList.size() > 0 && landDataDtosList != null) {
							dto.setLandData(landDataDtosList);
						}
					}
				}
			}
			log.info("data from db: " + dtos);
			response.setStatus(200);
			response.setMessage("Fetching records of completed land use verification by tahasil");
			response.setStatusMessage(CommonConstant.SUCCESS);
			response.setTahasilPlotDtosList(dtos);
		} else {
			response.setStatus(200);
			response.setMessage("No records available of completed land use verification by tahasil");
			response.setStatusMessage(CommonConstant.FAILED);
		}

		return response;
	}

	@Override
	public TahasilResponseDTO fetchWebLandUseVerificationCompletedDetails(TahasilPlotDto tahasilPlotDto) {
		TahasilResponseDTO response = new TahasilResponseDTO();
		log.info("fetching resultset from db for fetchLandUseVerificationCompletedDetails");
		List<TahasilPlotDto> dtos = new ArrayList<>();

		List<Object[]> resultList = villageRepository.fetchLandUseVerificationDetails(tahasilPlotDto.getTahasilCode());
		if (resultList != null && !resultList.isEmpty()) {
			dtos = mapToDtoForTahasil(resultList);
			if (dtos.size() > 0 && dtos != null) {

				for (TahasilPlotDto dto : dtos) {
					if (dto.getCoordinates() != null && !dto.getCoordinates().isEmpty()) {
						dto.setCoordinates("");
						dto.setLandLatLong(null);
					}
					if (dto.getPlotSurveyId() != null) {
						List<LandDataDto> landDataDtosList = new ArrayList<>();
						List<Object[]> list1 = tahasilPlotSurveyDetailsRepository
								.findByPlotSurveyId(dto.getPlotSurveyId());
						if (list1.size() > 0 && list1 != null) {
							landDataDtosList = fetchlandDataDtosList(list1);
						}
						log.info("result: " + landDataDtosList);
						if (landDataDtosList.size() > 0 && landDataDtosList != null) {
							dto.setLandData(landDataDtosList);
						}
					}
				}
			}
			List<TahasilPlotDto> result = null;
			result = dtos.stream().filter(dto -> dto.getPlotSurveyId().equals(tahasilPlotDto.getPlotSurveyId()))
					.collect(Collectors.toList());

			log.info("data from db: " + dtos);
			response.setStatus(200);
			response.setMessage("Fetching records of completed land use verification by tahasil");
			response.setStatusMessage(CommonConstant.SUCCESS);
			response.setTahasilPlotDto(result.get(0));
//			response.setTahasilPlotDtosList(dtos);
		} else {
			response.setStatus(200);
			response.setMessage("No records available of completed land use verification by tahasil");
			response.setStatusMessage(CommonConstant.FAILED);
		}

		return response;
	}

	@SuppressWarnings("unused")
	public List<LandDataDto> fetchlandDataDtosList(List<Object[]> list1) {
		List<LandDataDto> resultList = new ArrayList<>();
		return list1.stream().map(element -> {
			LandDataDto landDataDto = new LandDataDto();
			landDataDto.setLandTypeStatusId(element[1] != null ? (Integer) element[1] : 0);
			landDataDto.setLandTypeStatus(element[2] != null ? (String) element[2].toString() : "");
			landDataDto.setLandUseId(element[3] != null ? (Integer) element[3] : 0);
			landDataDto.setLandUse(element[4] != null ? (String) element[4].toString() : "");
			landDataDto.setBuildUpTypeName(element[5] != null ? (String) element[5].toString() : "");
			landDataDto.setLatitude(element[6] != null ? (String) element[6].toString() : "");
			landDataDto.setLongitude(element[7] != null ? (String) element[7].toString() : "");
			landDataDto.setImage(element[8] != null ? (String) element[8].toString() : "");
			ImageRequestDto dto = new ImageRequestDto();
			List<String> strList = new ArrayList<>();
			strList.add(landDataDto.getImage());
			dto.setFileNames(strList);

			dto.setForPurpose("1");
			List<String> strList1 = new ArrayList<>();
			strList1 = viewImage(dto);
			landDataDto.setImageLink(strList1.get(0));
			return landDataDto;

		}).collect(Collectors.toList());
	}

	public List<TahasilPlotDto> mapToDtoForTahasil(List<Object[]> resultList) {

		return resultList.stream().map(objects -> {
			TahasilPlotDto dto = new TahasilPlotDto();
			dto.setPlotSurveyId((Integer) objects[0]);
			dto.setDistrictCode(objects[1] != null ? (String) objects[1].toString() : "");
			dto.setDistrictName(objects[2] != null ? objects[2].toString() : "");
			dto.setTahasilCode(objects[3] != null ? objects[3].toString() : "");
			dto.setTahasilName(objects[4] != null ? objects[4].toString() : "");
			dto.setVillageCode(objects[5] != null ? objects[5].toString() : "");
			dto.setVillageName(objects[6] != null ? objects[6].toString() : "");
			dto.setRemarks(objects[7] != null ? objects[7].toString() : "");
			dto.setPlotCode(objects[8] != null ? objects[8].toString() : "");
			dto.setPlotNo(objects[9] != null ? objects[9].toString() : "");
			dto.setKissam(objects[10] != null ? objects[10].toString() : "");
			dto.setKhataNo(objects[11] != null ? objects[11].toString() : "");
			dto.setKhatianCode(objects[12] != null ? objects[12].toString() : "");
			dto.setAreaAcre(objects[13] != null ? objects[13].toString() : "");
			dto.setSurveyDate(objects[14] != null ? objects[14].toString() : "");
			dto.setCentralLatitude(objects[15] != null ? objects[15].toString() : "");
			dto.setCentralLongitude(objects[16] != null ? objects[16].toString() : "");
			dto.setCoordinates(objects[17] != null ? objects[17].toString() : "");
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public TahasilResponseDTO savePlotAction(TahasilPlotDto tahasilPlotDto) {
		TahasilResponseDTO response = new TahasilResponseDTO();
		try {
			Date parseDate = new Date();
			TahasilPlotEntity entity = new TahasilPlotEntity();
			BigInteger duplicateRecordCount = tahasilPlotSurvey.findDuplicateRecordCount(tahasilPlotDto.getPlotCode());
			entity.setTahasilCode(tahasilPlotDto.getTahasilCode());
			entity.setRemarks(tahasilPlotDto.getRemarks());
			entity.setPlotNo(tahasilPlotDto.getPlotNo());
			entity.setVillageCode(tahasilPlotDto.getVillageCode());
			entity.setKhatianCode(tahasilPlotDto.getKhatianCode());
			entity.setPlotCode(tahasilPlotDto.getPlotCode() != null && !tahasilPlotDto.getPlotCode().isEmpty()
					? tahasilPlotDto.getPlotCode()
					: "");
			if (tahasilPlotDto.getSurveyDate() != null || !tahasilPlotDto.getSurveyDate().isEmpty()) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				try {
					parseDate = dateFormat.parse(tahasilPlotDto.getSurveyDate());
				} catch (ParseException e) {
					log.error("inspection date error: " + e.getMessage());
				}
			}
			
			//BigInteger duplicateRecordCount = tahasilPlotSurvey.findDuplicateRecordCount(tahasilPlotDto.getPlotCode());
			
			entity.setSurveyDate(parseDate);
//			entity.setStatus(true);
			
			
			log.info("trying to save data in db");
			if(duplicateRecordCount.intValue() > 0) {
				response.setStatus(208);
				response.setMessage("record already exist for plot in database");
				return response;
			}
			tahasilPlotSurvey.save(entity);
			log.info("details saved in database");
//			TahasilPlotEntity detailsEntity = tahasilPlotSurvey.findSingleRowRecord(tahasilPlotDto.getTahasilCode(),tahasilPlotDto.getRemarks(),
//					tahasilPlotDto.getPlotNo(),tahasilPlotDto.getSurveyDate(),tahasilPlotDto.getVillageCode(),tahasilPlotDto.getKhatianCode());
			TahasilPlotEntity details = tahasilPlotSurvey.findTopByOrderByPlotSurveyIdDesc();
			// or
			TahasilPlotEntity details1 = tahasilPlotSurvey.findByLatestEntry(tahasilPlotDto.getTahasilCode(),
					tahasilPlotDto.getRemarks(), tahasilPlotDto.getPlotNo(), tahasilPlotDto.getPlotCode());
			log.info("details data :" + details);
			int count = 1;
			for (LandDataDto data : tahasilPlotDto.getLandData()) {
				// LocalDate time = new LocalDate(); CURRENT_TIMESTAMP
				String fileName = "";
				String newFileName = "";
				Date parsedDate = new Date();

				log.info("inside inner loop data :");
				TahasilPlotSurveyDetailsEntity landDetailsEntity = new TahasilPlotSurveyDetailsEntity();
				if (data.getCreatedDate() != null || data.getCreatedDate().isEmpty()) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					try {
						parsedDate = dateFormat.parse(data.getCreatedDate());
					} catch (ParseException e) {
						log.error("created date error: " + e.getMessage());
					}
				}

				landDetailsEntity
						.setBuildUpTypeName(data.getBuildUpTypeName() != null ? data.getBuildUpTypeName() : "");
				landDetailsEntity.setPlotSurveyId(details.getPlotSurveyId());
				landDetailsEntity.setLandTypeStatusId(data.getLandTypeStatusId());
				landDetailsEntity.setLandUseId(data.getLandUseId());
				landDetailsEntity.setLatitude(data.getLatitude());
				landDetailsEntity.setLongitude(data.getLongitude());
				if (data.getImage() != null && !data.getImage().isEmpty() && !data.getImage().trim().isEmpty()) {
					fileName = details.getPlotSurveyId() + "TahasilPlot" + tahasilPlotDto.getPlotNo() + "-PlotSurvey"
							+ count + ".jpg";
					if (fileName.contains("/")) {
						newFileName = fileName.replace("/", "_");
						log.info("file outer save method contains / " + newFileName);
						landDetailsEntity.setImage(newFileName);
						data.setFileName(newFileName);
					} else {
						log.info("file outer save method  " + fileName);
						landDetailsEntity.setImage(fileName);
						data.setFileName(fileName);
					}

				} else {
					landDetailsEntity.setImage(fileName);
					data.setFileName(fileName);
				}

//				landDetailsEntity.setStatus(true);
				landDetailsEntity.setCreatedDateTime(
						(data.getCreatedDate() != null && !data.getCreatedDate().isEmpty()) ? parsedDate : null);
				String[] strings = data.getImage().split(",");
				log.info("strings subpart" + strings[0] + " strings full" + data.getImage());
				if (strings[0].startsWith("data")) {
					GenericResponse resp = saveFileTodirectory(data, tahasilPlotDto.getPlotNo(),
							details.getPlotSurveyId());
					if (resp.getMessage() != null) {
						response.setMessage(resp.getMessage());
						response.setStatus(32);
					}
				}
				tahasilPlotSurveyDetailsRepository.save(landDetailsEntity);
				count++;
			}

			// latitude and longitude parameter to be added

			response.setStatus(200);
			response.setMessage("Details saved in database");
			response.setTahasilCode(tahasilPlotDto.getTahasilCode());
			// file saving code is commented
			// ------------------------------

		} catch (Exception e) {
			log.error("error while saving the plot survey details: " + e.getMessage());
			response.setStatus(500);
			response.setMessage("error while saving the plot survey details: " + e.getMessage());
		}
		return response;
	}

	@Override
	public TahasilResponseDTO fetchLandTypeDetails() {
		TahasilResponseDTO response = new TahasilResponseDTO();
		log.info("inside service method before execution to fetch land types");
		List<LandTypeDto> dtosList = landTypeRepository.findDetails();
		response.setStatus(200);
		response.setMessage("data fetched successfully");
		response.setLandTypeDtos(dtosList);
		return response;
	}

	@Override
	public TahasilResponseDTO fetchLandUseDetails() {
		TahasilResponseDTO response = new TahasilResponseDTO();
		log.info("inside service method before execution to fetch land uses");
		List<LandUseDto> dtosList = landUseRepository.findDetails();
		response.setStatus(200);
		response.setMessage("data fetched successfully");
		response.setLandUseDtos(dtosList);
		return response;
	}

	@SuppressWarnings("unused")
	private OffsetDateTime offsetDateTime(Date surveyDate) {
		return surveyDate.toInstant().atOffset(ZoneOffset.UTC);
	}

	@SuppressWarnings("unused")
	public GenericResponse saveFileTodirectory(LandDataDto landDataDto, String plotNo, Integer plotSurveyId) {
		GenericResponse response = new GenericResponse();
//		LandDataDto landDataDto = new LandDataDto() ;

		if (landDataDto.getImage() != null && !landDataDto.getImage().isEmpty()
				&& !landDataDto.getImage().trim().isEmpty()) {
			String[] strings = landDataDto.getImage().split(",");
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
			log.info("file contents " + data + " fileName : " + landDataDto.getFileName());
			String fileName = landDataDto.getFileName();
			log.info("file save " + fileName);
			if (fileName.contains("/")) {
				fileName = fileName.replace("/", "_");
			}
			String filePath = finalUploadPath + "/tahasilPlot";
			File folder = new File(filePath);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			String filePath1 = finalUploadPath + "/tahasilPlot/" + "PlotSurvey";
			File folder1 = new File(filePath1);
			if (!folder1.exists()) {
				folder1.mkdirs();
			}

			File file = new File(finalUploadPath + "/tahasilPlot/" + "PlotSurvey/" + fileName);
			log.info("fgdcsgcsa file " + file);
			try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
				outputStream.write(data);
				log.info("outputStream save " + outputStream);
			} catch (IOException e) {
				log.error("Error occured while saving file: " + e.getMessage());
				response.setMessage("Error occured while saving file");

			}
		}

		return response;

	}

	@Override
	public List<String> viewImage(ImageRequestDto imageRequestDto) {
		// to fetch purpose use common controller viewDocuments
		List<String> imagePaths = new ArrayList<>();
		imageRequestDto.getFileNames().forEach(fileName -> {
			String imagePath = appUrl + "/viewDocuments/" + imageRequestDto.getForPurpose() + "/" + fileName;
			imagePaths.add(imagePath.toString());
		});

		return imagePaths;
	}



}
