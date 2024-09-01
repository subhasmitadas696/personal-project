package com.csmtech.sjta.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.entity.CitizenProfileEntity;
import com.csmtech.sjta.entity.Land_applicant;
import com.csmtech.sjta.repository.CitizenProfileRepository;
import com.csmtech.sjta.repository.LandApplicantJPARepository;
import com.csmtech.sjta.repository.LandApplicantNativeRepository;
import com.csmtech.sjta.repository.LandApprovalRepository;
import com.csmtech.sjta.repository.LandPlotDetailsRepository;
import com.csmtech.sjta.service.Land_applicantService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
public class Land_applicantServiceImpl implements Land_applicantService {
	@Autowired
	private LandApplicantJPARepository land_applicantRepository;
	@Autowired
	LandApprovalRepository landApprovalRepository;
	@Autowired
	EntityManager entityManager;

	@Autowired
	private LandPlotDetailsRepository repo;

	@Autowired
	private LandApplicantNativeRepository reponative;
	
	@Autowired
	private CitizenProfileRepository profileRepo;

	Integer parentId;
	Object dynamicValue = null;

	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${file.path}")
	private String finalUploadPath;

	@Override
	public JSONObject userSave(String data) {
		JSONObject json = new JSONObject();
		
		try {
			
			ObjectMapper om = new ObjectMapper();
			Land_applicant land_applicant = om.readValue(data, Land_applicant.class);
			List<String> fileUploadList = new ArrayList<>();
			fileUploadList.add(land_applicant.getFileUploadDocument());
			log.info("intId" + land_applicant.getIntId());
			BigInteger citizenId = BigInteger.valueOf(land_applicant.getIntCreatedBy().intValue());
			if (!Objects.isNull(land_applicant.getIntId())) {
				Land_applicant updateRecord = null;
				String rr = reponative.getAppStatusForLandApplicationId(land_applicant.getIntId());
				if (rr.equalsIgnoreCase("4")) {
					updateRecord = reponative.updateLandApplicant(land_applicant);
					CitizenProfileEntity saverAddressDetails = updateAddress(land_applicant,citizenId);
				} else {
					updateRecord = reponative.updateLandApplicant(land_applicant);
					CitizenProfileEntity saverAddressDetails = updateAddress(land_applicant,citizenId);
					int intValue = (int) land_applicant.getAppStage();
					BigInteger bigIntegerValue = new BigInteger(String.valueOf(land_applicant.getIntId()));
					repo.updateAppStatusForLandApplicationId(bigIntegerValue, intValue);
				}
				parentId = updateRecord.getIntId();
				json.put("status", 202);
				json.put("appStatus", rr);
			} else {
				land_applicant.setIntCreatedBy(land_applicant.getIntCreatedBy());
				Integer dataSave = reponative.saveLandApplicantRecord(land_applicant);
				CitizenProfileEntity saverAddressDetails = updateAddress(land_applicant,citizenId);
				parentId = dataSave;
				String rr = reponative.getAppStatusForLandApplicationId(dataSave);
				json.put("status", 200);
				json.put("appStatus", rr);
			}
			json.put("app_id", parentId);

			for (String fileUpload : fileUploadList) {
				if (!fileUpload.equals("")) {
					File f = new File(tempUploadPath + fileUpload);
					if (f.exists()) {
						File src = new File(tempUploadPath + fileUpload);
						File dest = new File(finalUploadPath + "/" + fileUpload);
						try {
							Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
							Files.delete(src.toPath());
						} catch (IOException e) {

						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			json.put("status", 400);
		}
		return json;
	}

	public CitizenProfileEntity updateAddress(Land_applicant land_applicant,BigInteger citizenId) {
		CitizenProfileEntity entity = new CitizenProfileEntity();
		CitizenProfileEntity existingProfile = profileRepo.findByCitizenId(citizenId);
		//set current address
		existingProfile.setSelState(land_applicant.getSelState());
		existingProfile.setSelDistrict(land_applicant.getSelDistrict());
		existingProfile.setSelBlockULB(land_applicant.getSelBlockULB());
		existingProfile.setSelGPWardNo(land_applicant.getSelGPWardNo());
		existingProfile.setSelVillageLocalAreaName(land_applicant.getSelVillageLocalAreaName());
		existingProfile.setTxtPoliceStation(land_applicant.getTxtPoliceStation());
		existingProfile.setTxtPostOffice(land_applicant.getTxtPostOffice());
		existingProfile.setTxtHabitationStreetNoLandmark(land_applicant.getTxtHabitationStreetNoLandmark());
		existingProfile.setTxtHouseNo(land_applicant.getTxtHouseNo());
		existingProfile.setTxtPinCode(land_applicant.getTxtPinCode());
		existingProfile.setIsChecked(land_applicant.getIsChecked());
		
		//for permanent address
		existingProfile.setSelState17(land_applicant.getSelState17());
		existingProfile.setSelDistrict18(land_applicant.getSelDistrict18());
		existingProfile.setSelBlockULB19(land_applicant.getSelBlockULB19());
		existingProfile.setSelGPWARDNumber(land_applicant.getSelGPWARDNumber());
		existingProfile.setSelVillageLocalAreaName21(land_applicant.getSelVillageLocalAreaName21());
		existingProfile.setTxtPoliceStation22(land_applicant.getTxtPoliceStation());
		existingProfile.setTxtPostOffice23(land_applicant.getTxtPostOffice());
		existingProfile.setTxtHabitationStreetNoLandmark24(land_applicant.getTxtHabitationStreetNoLandmark24());
		existingProfile.setTxtHouseNo25(land_applicant.getTxtHouseNo25());
		existingProfile.setTxtPinCode26(land_applicant.getTxtPinCode26());
		existingProfile.setIsChecked(land_applicant.getIsChecked());
		profileRepo.save(existingProfile);
		return existingProfile;
	}

	@Override
	public JSONObject getById(Integer id) {
		Land_applicant entity = land_applicantRepository.findByIntId(id);
		try {
			dynamicValue = entityManager
					.createNativeQuery("select doc_name from m_document_type where doc_type_id= :docTypeId")
					.setParameter("docTypeId", entity.getSelDocumentType()).getSingleResult();

		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelDocumentTypeVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery("SELECT state_name FROM m_state WHERE CAST(state_id AS varchar) = :stateId")
					.setParameter("stateId", entity.getSelState()).getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelStateVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery(
							"select district_name from m_district where CAST(district_id AS varchar)= :districtId")
					.setParameter("districtId", entity.getSelDistrict()).getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelDistrictVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery("select block_name from m_block where CAST(block_id AS varchar)= :blockId")
					.setParameter("blockId", entity.getSelBlockULB()).getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelBlockULBVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery("select gp_name from m_gp where CAST(gp_id AS varchar)= :gpId")
					.setParameter("gpId", entity.getSelGPWardNo()).getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelGPWardNoVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery(
							"select village_name from m_village_master where CAST(village_id AS varchar)= :villageId")
					.setParameter("villageId", entity.getSelVillageLocalAreaName()).getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelVillageLocalAreaNameVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery("select state_name from m_state where CAST(state_id AS varchar)= :stateId")
					.setParameter("stateId", entity.getSelState17()).getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelState17Val(dynamicValue.toString());

		try {
			dynamicValue = entityManager
					.createNativeQuery(
							"select district_name from m_district where CAST(district_id AS varchar)= :districtId")
					.setParameter("districtId", entity.getSelDistrict18()).getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelDistrict18Val(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery("select block_name from m_block where CAST(block_id AS varchar)= :blockId")
					.setParameter("blockId", entity.getSelBlockULB19()).getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelBlockULB19Val(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery("select gp_name from m_gp where CAST(gp_id AS varchar)= :gpId")
					.setParameter("gpId", entity.getSelGPWARDNumber()).getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelGPWARDNumberVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery(
							"select village_name from m_village_master where CAST(village_id AS varchar)= :villageId")
					.setParameter("villageId", entity.getSelVillageLocalAreaName21()).getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelVillageLocalAreaName21Val(dynamicValue.toString());

		return new JSONObject(entity);
	}

	@Override
	public JSONArray getAll(String formParams) {
		List<Land_applicant> land_applicantResp = land_applicantRepository.findAllByBitDeletedFlag();
		for (Land_applicant entity : land_applicantResp) {
			try {
				dynamicValue = entityManager
						.createNativeQuery("select doc_name from m_document_type where doc_type_id= :docTypeId")
						.setParameter("docTypeId", entity.getSelDocumentType()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelDocumentTypeVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select state_name from m_state where state_id= :stateId")
						.setParameter("stateId", entity.getSelState()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelStateVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select district_name from m_district where district_id= :districtId")
						.setParameter("districtId", entity.getSelDistrict()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelDistrictVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select block_name from m_block where block_id= :blockId")
						.setParameter("blockId", entity.getSelBlockULB()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelBlockULBVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager.createNativeQuery("select gp_name from m_gp where gp_id= :gpId")
						.setParameter("gpId", entity.getSelGPWardNo()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelGPWardNoVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select village_name from m_village_master where village_id= :villageId")
						.setParameter("villageId", entity.getSelVillageLocalAreaName()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelVillageLocalAreaNameVal(dynamicValue.toString());

			try {
				dynamicValue = entityManager
						.createNativeQuery("select state_name from m_state where state_id= :stateId")
						.setParameter("stateId", entity.getSelState17()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelState17Val(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select district_name from m_district where district_id= :districtId")
						.setParameter("districtId", entity.getSelDistrict18()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelDistrict18Val(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select block_name from m_block where block_id= :blockId")
						.setParameter("blockId", entity.getSelBlockULB19()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelBlockULB19Val(dynamicValue.toString());
			try {
				dynamicValue = entityManager.createNativeQuery("select gp_name from m_gp where gp_id= :gpId")
						.setParameter("gpId", entity.getSelGPWARDNumber()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelGPWARDNumberVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select village_name from m_village_master where village_id= :villageId")
						.setParameter("villageId", entity.getSelVillageLocalAreaName21()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelVillageLocalAreaName21Val(dynamicValue.toString());

		}
		return new JSONArray(land_applicantResp);
	}

	@Override
	public JSONObject deleteById(Integer id) {
		JSONObject json = new JSONObject();
		try {
			Land_applicant entity = land_applicantRepository.findByIntId(id);
			land_applicantRepository.save(entity);
			json.put("status", 200);
		} catch (Exception e) {
			log.error(e.getMessage());
			json.put("status", 400);
		}
		return json;
	}

	public static JSONArray fillselDocumentTypeList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select doc_type_id,doc_name,min_length,max_length,doc_type from m_document_type where deleted_flag = '0'";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("doc_type_id", data[0]);
			jsonObj.put("doc_name", data[1]);
			jsonObj.put("min_length", data[2]);
			jsonObj.put("max_length", data[3]);
			jsonObj.put("doc_type", data[4]);

			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselStateList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select state_id,state_name from m_state where deleted_flag='0'";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("state_id", data[0]);
			jsonObj.put("state_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselDistrictList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);

		String val = jsonDepend.optString("state_id", null);

//		String val = "";
//		val = jsonDepend.get("state_id").toString();
		if (val != null && !val.isEmpty()) {

			String query = "Select district_id,district_name from m_district where deleted_flag ='0' and state_id="
					+ val;
			@SuppressWarnings("unchecked")
			List<Object[]> dataList = em.createNativeQuery(query).getResultList();
			for (Object[] data : dataList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("district_id", data[0]);
				jsonObj.put("district_name", data[1]);
				mainJSONFile.put(jsonObj);
			}
		}
		return mainJSONFile;
	}

	

	public static JSONArray fillselBlockULBList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = jsonDepend.optString("district_id", null);

		if (val != null && !val.isEmpty()) {
			String query = "SELECT block_id, block_name FROM m_block WHERE deleted_flag = '0' AND district_id = CAST(:districtId AS INTEGER) ORDER BY block_name";

			@SuppressWarnings("unchecked")
			List<Object[]> dataList = em.createNativeQuery(query).setParameter("districtId", val).getResultList();

			for (Object[] data : dataList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("block_id", data[0]);
				jsonObj.put("block_name", data[1]);
				mainJSONFile.put(jsonObj);
			}
		}

		return mainJSONFile;
	}

	public static JSONArray fillselGPWardNoList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();

		try {
			JSONObject jsonDepend = new JSONObject(jsonVal);
			String val = jsonDepend.optString("block_id", "");

			if (val != null && !val.isEmpty()) {
				String query = "SELECT gp_id, gp_name FROM m_gp WHERE deleted_flag = '0' AND block_id = CAST(:blockId AS INTEGER )";

				@SuppressWarnings("unchecked")
				List<Object[]> dataList = em.createNativeQuery(query).setParameter("blockId", val).getResultList();

				for (Object[] data : dataList) {
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("gp_id", data[0]);
					jsonObj.put("gp_name", data[1]);
					mainJSONFile.put(jsonObj);
				}
			}
		} catch (Exception e) {
			log.error("in fillselGPWardNoList error..");
		}

		return mainJSONFile;
	}


	public static JSONArray fillselVillageLocalAreaNameList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = jsonDepend.optString("gp_id", "");

		if (val != null && !val.isEmpty()) {

			String query = "Select village_id,village_name from m_village_master where deleted_flag='0' and gp_id = CAST(:gpId AS INTEGER) ";
			@SuppressWarnings("unchecked")
			List<Object[]> dataList = em.createNativeQuery(query).setParameter("gpId", val).getResultList();
			for (Object[] data : dataList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("village_id", data[0]);
				jsonObj.put("village_name", data[1]);
				mainJSONFile.put(jsonObj);
			}
		}
		return mainJSONFile;
	}

	public static JSONArray fillselState17List(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select state_id,state_name from m_state where deleted_flag ='0'";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("state_id", data[0]);
			jsonObj.put("state_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselDistrict18List(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);

		String val = jsonDepend.optString("state_id", null);
//		String val = "";
//		val = jsonDepend.get("state_id").toString();
		if (val != null && !val.isEmpty()) {

			String query = "Select district_id,district_name from m_district where deleted_flag = '0' and state_id="
					+ val;
			@SuppressWarnings("unchecked")
			List<Object[]> dataList = em.createNativeQuery(query).getResultList();
			for (Object[] data : dataList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("district_id", data[0]);
				jsonObj.put("district_name", data[1]);
				mainJSONFile.put(jsonObj);
			}
		}
		return mainJSONFile;
	}

	public static JSONArray fillselBlockULB19List(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = jsonDepend.optString("district_id", "");

		if (val != null && !val.isEmpty()) {

			String query = "Select block_id,block_name from m_block where deleted_flag = '0' and district_id= CAST(:districtId AS INTEGER) ORDER BY block_name ";
			@SuppressWarnings("unchecked")
			List<Object[]> dataList = em.createNativeQuery(query).setParameter("districtId", val).getResultList();
			for (Object[] data : dataList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("block_id", data[0]);
				jsonObj.put("block_name", data[1]);
				mainJSONFile.put(jsonObj);
			}
		}
		return mainJSONFile;
	}

	public static JSONArray fillselGPWARDNumberList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();

		try {
			JSONObject jsonDepend = new JSONObject(jsonVal);
			String val = jsonDepend.optString("block_id", "");

			if (val != null && !val.isEmpty()) {
				String query = "SELECT gp_id, gp_name FROM m_gp WHERE deleted_flag = '0' AND block_id = CAST(:blockId AS INTEGER)";

				@SuppressWarnings("unchecked")
				List<Object[]> dataList = em.createNativeQuery(query).setParameter("blockId", val).getResultList();

				for (Object[] data : dataList) {
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("gp_id", data[0]);
					jsonObj.put("gp_name", data[1]);
					mainJSONFile.put(jsonObj);
				}
			}
		} catch (Exception e) {
			log.error("in fillselGPWARDNumberList error");
		}

		return mainJSONFile;
	}


	public static JSONArray fillselVillageLocalAreaName21List(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = jsonDepend.optString("gp_id", "");
		if (val != null && !val.isEmpty()) {
			String query = "SELECT village_id, village_name FROM m_village_master WHERE deleted_flag = '0' AND gp_id = CAST(:gpId AS INTEGER) ORDER BY village_name";
			@SuppressWarnings("unchecked")
			List<Object[]> dataList = em.createNativeQuery(query).setParameter("gpId", val).getResultList();
			for (Object[] data : dataList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("village_id", data[0]);
				jsonObj.put("village_name", data[1]);
				mainJSONFile.put(jsonObj);
			}
		}
		return mainJSONFile;
	}

	public static JSONArray filltxtPoliceStationList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);

		String val = jsonDepend.optString("district_id", "");

		if (val != null && !val.isEmpty()) {
			String query = "SELECT police_station_id, police_station_name FROM public.m_police_station WHERE deleted_flag = '0' AND district_id = CAST(:districtId AS INTEGER)";

			@SuppressWarnings("unchecked")
			List<Object[]> dataList = em.createNativeQuery(query).setParameter("districtId", val).getResultList();

			for (Object[] data : dataList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("police_station_id", data[0]);
				jsonObj.put("police_station_name", data[1]);
				mainJSONFile.put(jsonObj);
			}
		}

		return mainJSONFile;
	}

	public static JSONArray filltxtPoliceStation22List(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = jsonDepend.optString("district_id", "");
		if (val != null && !val.isEmpty()) {

			String query = "Select police_station_id,police_station_name from public.m_police_station where deleted_flag = '0' and district_id= CAST(:districtId AS INTEGER) ";
			@SuppressWarnings("unchecked")
			List<Object[]> dataList = em.createNativeQuery(query).setParameter("districtId", val).getResultList();
			for (Object[] data : dataList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("police_station_id", data[0]);
				jsonObj.put("police_station_name", data[1]);
				mainJSONFile.put(jsonObj);
			}
		}
		return mainJSONFile;
	}

}
