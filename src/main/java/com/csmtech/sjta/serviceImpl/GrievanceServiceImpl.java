package com.csmtech.sjta.serviceImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.GrievanceGoSeenRecordDTO;
import com.csmtech.sjta.dto.GrievanceMainDTO;
import com.csmtech.sjta.dto.NotificationDTO;
import com.csmtech.sjta.entity.Grievance;
import com.csmtech.sjta.mobile.repository.LandInspectionRepository;
import com.csmtech.sjta.mobile.repository.NotificationDetailsRepository;
import com.csmtech.sjta.mobile.service.NotificationDetailsServiceImpl;
import com.csmtech.sjta.repository.GrievanceRepository;
import com.csmtech.sjta.repository.GrivenceUseNativeQueryClassRepoitory;
import com.csmtech.sjta.service.GrievanceService;
import com.csmtech.sjta.util.CommonCaptchaGenerate;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service
public class GrievanceServiceImpl implements GrievanceService {
	
	@Autowired
	NotificationDetailsRepository notificationDetailsRepo;
	
	@Autowired
	NotificationDetailsServiceImpl notificationDetailsServiceImpl;

	@Autowired
	LandInspectionRepository landInspectionRepository;

	@Autowired
	private GrievanceRepository grievanceRepository;
	@Autowired
	EntityManager entityManager;

	@Autowired
	private GrivenceUseNativeQueryClassRepoitory reponative;

	JSONObject selCaste = new JSONObject("{General:General,SC:SC,ST:ST,OBC:OBC,SEBC:SEBC,EBC:EBC}");
	JSONObject chkDiscloseyourdetails = new JSONObject("{1:Yes}");
	JSONObject selModeofOccupation = new JSONObject("{1:Cultivation,2:Erection of House,3:Any other Manner}");

	Integer parentId = 0;
	Object dynamicValue = null;
	private static final Logger logger = LoggerFactory.getLogger(GrievanceServiceImpl.class);
	JSONObject json = new JSONObject();
	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${file.path}")
	private String finalUploadPath;

	@Override
	public JSONObject save(String data) {
		logger.info("Inside save method of GrievanceServiceImpl");
		try {
			ObjectMapper om = new ObjectMapper();
			Grievance grievance = om.readValue(data, Grievance.class);
			List<String> fileUploadList = new ArrayList<>();
			fileUploadList.add(grievance.getFileFileUpload());
			boolean validateCaptcha;
			Integer captchaResult = CommonCaptchaGenerate.get(grievance.getCaptchaId());

			if (captchaResult != null && grievance.getAnswer() == captchaResult) {
				CommonCaptchaGenerate.remove(grievance.getCaptchaId());
				validateCaptcha = true;
			} else {
				validateCaptcha = false;
			}

			if (!validateCaptcha) {
				json.put("status", 223);
				json.put("message", "Invalid captcha");
				return json;
			}
			if (!Objects.isNull(grievance.getIntId()) && grievance.getIntId() > 0) {
				Grievance getEntity = grievanceRepository.findByIntIdAndBitDeletedFlag(grievance.getIntId(), false);
				getEntity.setSelMonthofUnauthorizedOccupation(grievance.getSelMonthofUnauthorizedOccupation());
				getEntity.setTxtName(grievance.getTxtName());
				getEntity.setTxtFatherName(grievance.getTxtFatherName());
				getEntity.setSelDistrict(grievance.getSelDistrict());
				getEntity.setSelBlock(grievance.getSelBlock());
				getEntity.setSelGP(grievance.getSelGP());
				getEntity.setSelVillage(grievance.getSelVillage());
				getEntity.setTxtOtherInformation(grievance.getTxtOtherInformation());
				getEntity.setSelCaste(grievance.getSelCaste());
				getEntity.setTxtMobileNo(grievance.getTxtMobileNo());
				getEntity.setChkDiscloseyourdetails(grievance.getChkDiscloseyourdetails());
				getEntity.setSelDistrict13(grievance.getSelDistrict13());
				getEntity.setSelTahasil(grievance.getSelTahasil());
				getEntity.setSelVillage15(grievance.getSelVillage15());
				getEntity.setSelKhataNo(grievance.getSelKhataNo());
				getEntity.setSelPlotNo(grievance.getSelPlotNo());
				getEntity.setTxtTotalAreainacre(grievance.getTxtTotalAreainacre());
				getEntity.setTxtExtentOccupied(grievance.getTxtExtentOccupied());
				getEntity.setSelModeofOccupation(grievance.getSelModeofOccupation());
				getEntity.setTxtOccupationDetails(grievance.getTxtOccupationDetails());
				getEntity.setTxtLandmark(grievance.getTxtLandmark());
				getEntity.setFileFileUpload(grievance.getFileFileUpload());
				getEntity.setTxtrTextarea(grievance.getTxtrTextarea());
				getEntity.setPostedBy("CITIZEN");
				Grievance updateData = grievanceRepository.save(getEntity);
				parentId = updateData.getIntId();
				json.put(CommonConstant.STATUS_KEY, 202);
			} else {

				// here generate the unique i and insert to the the table
				String grivaneGenerateNo = GrievanceServiceImpl.generateApplicantUniqueNumber("GR");
				Grievance saveData = new Grievance();
				saveData = grievanceRepository.save(grievance);
				parentId = saveData.getIntId();
				// add the created by
				Integer updatecreatedOn = reponative.updateGrievanceCreatedBy(parentId, parentId, grivaneGenerateNo);
				// return the grievance
				List<Object[]> grivanceno = reponative.getGrievanceNoById(parentId);
				List<String> mobileNumbers = new ArrayList<>();
				List<String> grivancenoRec = new ArrayList<>();

				for (Object[] row : grivanceno) {
					String grivancenos = (String) row[0];
					String mobileNo = (String) row[1];
					grivancenoRec.add(grivancenos);
					mobileNumbers.add(mobileNo);
				}
				for (String fileUpload : fileUploadList) {
					if (fileUpload != null && (!fileUpload.equals(""))) {
						File f = new File(tempUploadPath + fileUpload);
						if (f.exists()) {
							File src = new File(tempUploadPath + fileUpload);
							File dest = new File(finalUploadPath + "/" + "grievance/" + fileUpload);
							if (!dest.getParentFile().exists()) {
								try {
									Files.createDirectories(dest.getParentFile().toPath());
								} catch (IOException e) {
									logger.error("error occured,  while creating folder {}", e.getMessage());
								}
							}
							try {
								Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
								Files.delete(src.toPath());
							} catch (IOException e) {
								logger.error(e.getMessage());
							}
						}

					}
				}
				
				// add the notification
				//for manual notification Illegitimate Land Use Apply  by Citizen done then notify to Land Officer
				List<BigInteger> landUserList = notificationDetailsRepo.fetchUserDetailsOnRoleId(new BigInteger("4"));
				Grievance fetchRecord = grievanceRepository.findByGrievanceNumber(grivaneGenerateNo);
				if(landUserList != null && landUserList.size() > 0) {
					for(BigInteger landUser:landUserList) {
						NotificationDTO notificationDtoForLand = new NotificationDTO();
						notificationDtoForLand.setNotification("Grievance  "+ grivaneGenerateNo+"is created.");
						notificationDtoForLand.setUserId(landUser);
						notificationDtoForLand.setCreatedBy(landUser);
						notificationDtoForLand.setUserType("O");
						notificationDetailsServiceImpl.submitNotification(notificationDtoForLand);
					}
				}
				
				//for citizen
				NotificationDTO notificationDto = new NotificationDTO();
				notificationDto.setNotification("Grievance  "+ grivaneGenerateNo+"is created.");
				notificationDto.setUserId(fetchRecord.getIntCreatedBy() != null ?new BigInteger(fetchRecord.getIntCreatedBy().toString()):BigInteger.ZERO);
				notificationDto.setCreatedBy(fetchRecord.getIntCreatedBy() != null ?new BigInteger(fetchRecord.getIntCreatedBy().toString()):BigInteger.ZERO);
				notificationDto.setUserType("CI");
				notificationDetailsServiceImpl.submitNotification(notificationDto);

				
				json.put(CommonConstant.STATUS_KEY, 200);
				json.put("grivanceno", grivancenoRec.get(0));
				json.put("mobileNumbers", mobileNumbers.get(0));
			}
			json.put("id", parentId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error("Inside save method of GrievanceServiceImpl,  some error occured {}", e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(Integer id) {
		logger.info("Inside getById method of GrievanceServiceImpl");
		Grievance entity = grievanceRepository.findByIntIdAndBitDeletedFlag(id, false);
		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select month_name from m_month where month_id=" + entity.getSelMonthofUnauthorizedOccupation());
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelMonthofUnauthorizedOccupationVal(dynamicValue.toString());
		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select district_name from m_district where district_id=" + entity.getSelDistrict());
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelDistrictVal(dynamicValue.toString());
		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select block_name from m_block where block_id=" + entity.getSelBlock());
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelBlockVal(dynamicValue.toString());
		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select gp_name from m_gp where gp_id=" + entity.getSelGP());
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelGPVal(dynamicValue.toString());
		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select village_name from m_village_master where village_id=" + entity.getSelVillage());
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelVillageVal(dynamicValue.toString());
		dynamicValue = (selCaste.has(entity.getSelCaste().toString())) ? selCaste.get(entity.getSelCaste().toString())
				: "--";
		entity.setSelCasteVal(dynamicValue.toString());
		dynamicValue = (chkDiscloseyourdetails.has(entity.getChkDiscloseyourdetails().toString()))
				? chkDiscloseyourdetails.get(entity.getChkDiscloseyourdetails().toString())
				: "--";
		entity.setChkDiscloseyourdetailsVal(dynamicValue.toString());
		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select district_name from land_bank.district_master where district_code="
							+ entity.getSelDistrict13());
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelDistrict13Val(dynamicValue.toString());
		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select tahasil_name from land_bank.tahasil_master where tahasil_code=" + entity.getSelTahasil());
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelTahasilVal(dynamicValue.toString());
		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select village_name from land_bank.village_master where village_code=" + entity.getSelVillage15());
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelVillage15Val(dynamicValue.toString());
		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select khata_no from land_bank.khatian_information where khatian_code=" + entity.getSelKhataNo());
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelKhataNoVal(dynamicValue.toString());
		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select plot_no from land_bank.plot_information where plot_code=" + entity.getSelPlotNo());
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelPlotNoVal(dynamicValue.toString());
		dynamicValue = (selModeofOccupation.has(entity.getSelModeofOccupation().toString()))
				? selModeofOccupation.get(entity.getSelModeofOccupation().toString())
				: "--";
		entity.setSelModeofOccupationVal(dynamicValue.toString());

		return new JSONObject(entity);
	}

	@Override
	public JSONObject getAll(String formParams) {
		logger.info("Inside getAll method of GrievanceServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		Integer totalDataPresent = grievanceRepository.countByBitDeletedFlag(false);
		Pageable pageRequest = PageRequest.of(jsonData.has("pageNo") ? jsonData.getInt("pageNo") - 1 : 0,
				jsonData.has("size") ? jsonData.getInt("size") : totalDataPresent);
		List<Grievance> grievanceResp = grievanceRepository.findAllByBitDeletedFlag(false, pageRequest);
		for (Grievance entity : grievanceResp) {
			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select month_name from m_month where month_id="
								+ entity.getSelMonthofUnauthorizedOccupation());
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelMonthofUnauthorizedOccupationVal(dynamicValue.toString());
			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select district_name from m_district where district_id=" + entity.getSelDistrict());
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelDistrictVal(dynamicValue.toString());
			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select block_name from m_block where block_id=" + entity.getSelBlock());
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelBlockVal(dynamicValue.toString());
			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select gp_name from m_gp where gp_id=" + entity.getSelGP());
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelGPVal(dynamicValue.toString());
			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select village_name from m_village_master where village_id=" + entity.getSelVillage());
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelVillageVal(dynamicValue.toString());
			dynamicValue = (selCaste.has(entity.getSelCaste().toString()))
					? selCaste.get(entity.getSelCaste().toString())
					: "--";
			entity.setSelCasteVal(dynamicValue.toString());
			dynamicValue = (chkDiscloseyourdetails.has(entity.getChkDiscloseyourdetails().toString()))
					? chkDiscloseyourdetails.get(entity.getChkDiscloseyourdetails().toString())
					: "--";
			entity.setChkDiscloseyourdetailsVal(dynamicValue.toString());
			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select district_name from land_bank.district_master where district_code="
								+ entity.getSelDistrict13());
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelDistrict13Val(dynamicValue.toString());
			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select tahasil_name from land_bank.tahasil_master where tahasil_code="
								+ entity.getSelTahasil());
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelTahasilVal(dynamicValue.toString());
			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select village_name from land_bank.village_master where village_code="
								+ entity.getSelVillage15());
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelVillage15Val(dynamicValue.toString());
			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select khata_no from land_bank.khatian_information where khatian_code="
								+ entity.getSelKhataNo());
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelKhataNoVal(dynamicValue.toString());
			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select plot_no from land_bank.plot_information where plot_code=" + entity.getSelPlotNo());
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelPlotNoVal(dynamicValue.toString());
			dynamicValue = (selModeofOccupation.has(entity.getSelModeofOccupation().toString()))
					? selModeofOccupation.get(entity.getSelModeofOccupation().toString())
					: "--";
			entity.setSelModeofOccupationVal(dynamicValue.toString());

		}
		json.put(CommonConstant.STATUS_KEY, 200);
		json.put("result", new JSONArray(grievanceResp));
		json.put("count", totalDataPresent);
		return json;
	}

	@Override
	public JSONObject deleteById(Integer id) {
		logger.info("Inside deleteById method of GrievanceServiceImpl");
		try {
			Grievance entity = grievanceRepository.findByIntIdAndBitDeletedFlag(id, false);
			entity.setBitDeletedFlag(true);
			grievanceRepository.save(entity);
			json.put(CommonConstant.STATUS_KEY, 200);
		} catch (Exception e) {
			logger.error(e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	public static JSONArray fillselMonthofUnauthorizedOccupationList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselMonthofUnauthorizedOccupationList method of GrievanceServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select month_id,month_name from m_month where deleted_flag ='0' order by month_id";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("month_id", data[0]);
			jsonObj.put("month_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselDistrictList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselDistrictList method of GrievanceServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select district_id,district_name from m_district where deleted_flag = '0' and state_id = 21 "
				+ " ORDER BY " + "    district_name ";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("district_id", data[0]);
			jsonObj.put("district_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselBlockList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselBlockList method of GrievanceServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("district_id").toString();
		String query = "Select block_id,block_name from m_block where deleted_flag = '0' and district_id=" + val;
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("block_id", data[0]);
			jsonObj.put("block_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselGPList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselGPList method of GrievanceServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("block_id").toString();
		String query = "Select gp_id,gp_name from m_gp where deleted_flag = '0' and block_id=" + val;
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("gp_id", data[0]);
			jsonObj.put("gp_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselVillageList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselVillageList method of GrievanceServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("gp_id").toString();
		String query = "Select village_id,village_name from m_village_master where deleted_flag = '0' and gp_id=" + val;
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("village_id", data[0]);
			jsonObj.put("village_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselDistrict13List(EntityManager em, String jsonVal) {
		logger.info("Inside fillselDistrict13List method of GrievanceServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		String query = " SELECT DISTINCT dm.district_code, dm.district_name\r\n"
				+ "FROM land_bank.district_master dm\r\n"
				+ "JOIN land_bank.tahasil_master ki ON ki.district_code = dm.district_code\r\n"
				+ "JOIN land_bank.village_master kii ON ki.tahasil_code = kii.tahasil_code\r\n"
				+ "JOIN land_bank.khatian_information kiii ON kii.village_code = kiii.village_code\r\n"
				+ "WHERE dm.state_code = 'OD'\r\n" + "ORDER BY district_name;\r\n" + " ";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("district_code", data[0]);
			jsonObj.put("district_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselTahasilList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselTahasilList method of GrievanceServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("district_code").toString();
		String query = " select DISTINCT ki.tahasil_code,ki.tahasil_name from land_bank.tahasil_master ki\r\n"
				+ " JOIN land_bank.village_master kii ON ki.tahasil_code = kii.tahasil_code\r\n"
				+ " JOIN land_bank.khatian_information kiii ON kii.village_code = kiii.village_code  "
				+ " where  district_code= '" + val + "' order by tahasil_name";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("tahasil_code", data[0]);
			jsonObj.put("tahasil_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselVillage15List(EntityManager em, String jsonVal) {
		logger.info("Inside fillselVillage15List method of GrievanceServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("tahasil_code").toString();
		String query = "Select vm.village_code ,vm.village_name ,CAST(ST_extent(ST_Transform(vb.geom, 3857))\r\n"
				+ " AS character varying) AS extent  from land_bank.village_master vm \r\n"
				+ "	left join land_bank.village_boundary vb on(vm.village_code=vb.village_code) \r\n"
				+ " JOIN land_bank.khatian_information kiii ON (vm.village_code = kiii.village_code)  "
				+ "	 where vm.tahasil_code='" + val + "' group by vm.village_code ORDER BY vm.village_name ASC";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("village_code", data[0]);
			jsonObj.put("village_name", data[1]);
			jsonObj.put("extent", data[2]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselKhataNoList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselKhataNoList method of GrievanceServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("village_code").toString();
		String query = "Select khatian_code,khata_no from land_bank.khatian_information where village_code='" + val
				+ "' ORDER BY khata_no";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("khatian_code", data[0]);
			jsonObj.put("khata_no", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselPlotNoList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselPlotNoList method of GrievanceServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("khatian_code").toString();
		String query = "Select plot_code,plot_no,area_acre from land_bank.plot_information where khatian_code='" + val
				+ "' ORDER BY plot_no ";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("plot_code", data[0]);
			jsonObj.put("plot_no", data[1]);
			jsonObj.put("area_acre", data[2].toString());
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	private static final String DATA_FILE_PATH = "GrivanceUniqueNumberGenerator.txt";
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
	public List<GrievanceGoSeenRecordDTO> getgrivanceUserRecord(Integer statusId, Integer pageNumber, Integer pageSize,
			String selDistrictCode, String selTahasilCode, String selVillageCode, String grievanceNo) {
		if (statusId == null) {
			List<Object[]> resultList = reponative.getRecordGrivanceUser(0, pageNumber, pageSize, selDistrictCode,
					selTahasilCode, selVillageCode, grievanceNo);
			return resultList.stream().map(result -> {
				GrievanceGoSeenRecordDTO dto = new GrievanceGoSeenRecordDTO();
				dto.setGrievanceNo((String) result[0]);
				dto.setDistrictCode((String) result[1]);
				dto.setTahasilCode((String) result[2]);
				dto.setVillageCode((String) result[3]);
				dto.setKhatianCode((String) result[4]);
				dto.setPlotNo((String) result[5]);
				dto.setGrievanceId((Integer) result[6]);
				dto.setCreatedOn((Date) result[7]);
				dto.setRemarkByGO((String) result[8]);
				dto.setExtent((String) result[9]);
				return dto;
			}).collect(Collectors.toList());
		}
		List<Object[]> resultList = reponative.getRecordGrivanceUser(statusId, pageNumber, pageSize, selDistrictCode,
				selTahasilCode, selVillageCode, grievanceNo);
		return resultList.stream().map(result -> {
			GrievanceGoSeenRecordDTO dto = new GrievanceGoSeenRecordDTO();
			dto.setGrievanceNo((String) result[0]);
			dto.setDistrictCode((String) result[1]);
			dto.setTahasilCode((String) result[2]);
			dto.setVillageCode((String) result[3]);
			dto.setKhatianCode((String) result[4]);
			dto.setPlotNo((String) result[5]);
			dto.setGrievanceId((Integer) result[6]);
			dto.setCreatedOn((Date) result[7]);
			dto.setRemarkByGO((String) result[8]);
			dto.setExtent((String) result[9]);
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public List<GrievanceMainDTO> getRecordGrivanceUserMore(Integer grivanceId) {
		List<Object[]> results = reponative.getRecordGrivanceUserMore(grivanceId);

		return results.stream().map(result -> {
			GrievanceMainDTO dto = new GrievanceMainDTO();
			dto.setGrievanceId((Integer) result[0]);
			dto.setMonthId((String) result[1]);
			dto.setName((String) result[2]);
			dto.setFatherName((String) result[3]);
			dto.setDistrictId((String) result[4]);
			dto.setBlockId((String) result[5]);
			dto.setGpId((String) result[6]);
			dto.setVillageId((String) result[7]);
			dto.setOtherInformation((String) result[8]);
			dto.setCasteName((String) result[9]);
			dto.setMobileNo((String) result[10]);
			dto.setDiscloseDetails((Short) result[11]);
			dto.setDistrictCode((String) result[12]);
			dto.setTahasilCode((String) result[13]);
			dto.setVillageCode((String) result[14]);
			dto.setKhatianCode((String) result[15]);
			dto.setPlotNo((String) result[16]);
			dto.setAreaAcre(result[17] != null ? result[17].toString() : "");

			dto.setExtentOccupied(result[18] != null ? result[18].toString() : "");
			dto.setModeOfOccupation((Short) result[19]);
			dto.setOtherOccupation((String) result[20]);
			dto.setLandmark((String) result[21]);
			dto.setUploadFile((String) result[22]);
			dto.setRemarks((String) result[23]);
			dto.setGrievanceNo((String) result[24]);
			dto.setGrivanceStatus((Short) result[25]);
			dto.setCoUploadedPhoto((String) result[26]);
			dto.setRemarkByCo((String) result[27]);
			dto.setInspectionDate(result[28] != null ? result[28].toString() : "");

			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public JSONObject updateGrievanceStatus(Integer grievanceId, Integer newStatus, String remark, java.sql.Date date) {
		JSONObject resultObj = new JSONObject(); 
		Integer result =  reponative.updateGrievanceStatus(grievanceId, newStatus, remark, date);
		if(result > 0) {
			String token = landInspectionRepository.fetchFcmToken();
			if(token != null && !token.isEmpty()) {
				resultObj.put("fcmToken",token);
			}
		}else {
			resultObj.put("fcmToken","");
		}
		resultObj.put("result",result);
		
		return resultObj;
	}

	@Override
	public JSONObject updateGrievanceStatusFinal(Integer grievanceId, Integer newStatus, String remark) {
		JSONObject resultObj = new JSONObject();
		Integer result = reponative.updateGrievanceStatusFinal(grievanceId, newStatus, remark);
		
		return resultObj;
	}

	@Override
	public BigInteger countRecord(Integer grievanceId, String selDistrictCode, String selTahasilCode,
			String selVillageCode, String grievanceNo) {
		return reponative.getRecordGrivanceUserCountPagination(grievanceId, selDistrictCode, selTahasilCode,
				selVillageCode, grievanceNo);
	}

}
