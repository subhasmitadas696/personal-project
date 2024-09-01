package com.csmtech.sjta.serviceImpl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.entity.Noc_applicant;
import com.csmtech.sjta.repository.NocApplicantRepository;
import com.csmtech.sjta.service.Noc_applicantService;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
public class Noc_applicantServiceImpl implements Noc_applicantService {
	@Autowired
	private NocApplicantRepository noc_applicantRepository;
	@Autowired
	EntityManager entityManager;

	Integer parentId = 0;
	Object dynamicValue = null;

	@Override
	public JSONObject save(String data) {
		JSONObject json = new JSONObject();
		try {
			ObjectMapper om = new ObjectMapper();
			Noc_applicant noc_applicant = om.readValue(data, Noc_applicant.class);
			List<String> fileUploadList = new ArrayList<>();
			fileUploadList.add(noc_applicant.getFileUploadDocument());
			if (!Objects.isNull(noc_applicant.getIntId()) && noc_applicant.getIntId() > 0) {
				Noc_applicant getEntity = noc_applicantRepository.findByIntIdAndBitDeletedFlag(noc_applicant.getIntId(),
						false);
				getEntity.setTxtApplicantName(noc_applicant.getTxtApplicantName());
				getEntity.setTxtFatherHusbandName(noc_applicant.getTxtFatherHusbandName());
				getEntity.setTxtMobileNo(noc_applicant.getTxtMobileNo());
				getEntity.setTxtEmail(noc_applicant.getTxtEmail());
				getEntity.setSelDocumentType(noc_applicant.getSelDocumentType());
				getEntity.setTxtDocumentNo(noc_applicant.getTxtDocumentNo());
				getEntity.setFileUploadDocument(noc_applicant.getFileUploadDocument());
				getEntity.setSelState(noc_applicant.getSelState());
				getEntity.setSelDistrict(noc_applicant.getSelDistrict());
				getEntity.setSelBlockULB(noc_applicant.getSelBlockULB());
				getEntity.setSelGPWardNo(noc_applicant.getSelGPWardNo());
				getEntity.setSelVillageLocalAreaName(noc_applicant.getSelVillageLocalAreaName());
				getEntity.setTxtPoliceStation(noc_applicant.getTxtPoliceStation());
				getEntity.setTxtPostOffice(noc_applicant.getTxtPostOffice());
				getEntity.setTxtHabitationStreetNoLandmark(noc_applicant.getTxtHabitationStreetNoLandmark());
				getEntity.setTxtHouseNo(noc_applicant.getTxtHouseNo());
				getEntity.setTxtPinCode(noc_applicant.getTxtPinCode());
				getEntity.setSelState17(noc_applicant.getSelState17());
				getEntity.setSelDistrict18(noc_applicant.getSelDistrict18());
				getEntity.setSelBlockULB19(noc_applicant.getSelBlockULB19());
				getEntity.setSelGPWARDNumber(noc_applicant.getSelGPWARDNumber());
				getEntity.setSelVillageLocalAreaName21(noc_applicant.getSelVillageLocalAreaName21());
				getEntity.setTxtPoliceStation22(noc_applicant.getTxtPoliceStation22());
				getEntity.setTxtPostOffice23(noc_applicant.getTxtPostOffice23());
				getEntity.setTxtHabitationStreetNoLandmark24(noc_applicant.getTxtHabitationStreetNoLandmark24());
				getEntity.setTxtHouseNo25(noc_applicant.getTxtHouseNo25());
				getEntity.setTxtPinCode26(noc_applicant.getTxtPinCode26());
				Noc_applicant updateData = noc_applicantRepository.save(getEntity);
				parentId = updateData.getIntId();
				json.put("status", 202);
			} else {
				Noc_applicant saveData = noc_applicantRepository.save(noc_applicant);
				parentId = saveData.getIntId();
				json.put("status", 200);
			}
			for (String fileUpload : fileUploadList) {
				if (!fileUpload.equals("")) {
					File f = new File("src/storage/tempfile/" + fileUpload);
					if (f.exists()) {
						Path srcPath = Paths.get("src/storage/tempfile/" + fileUpload);
						Path destPath = Paths.get("src/storage/individual-details/" + fileUpload);
						CommonUtil.copyAndDeleteFile(srcPath, destPath);
					}
				}
			}
		} catch (Exception e) {
			log.info("Exception inside save() in Noc_ApplicantServiceImpl !!" + e.getMessage());
			json.put("status", 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(Integer id) {
		Noc_applicant entity = noc_applicantRepository.findByIntIdAndBitDeletedFlag(id, false);
		try {
			dynamicValue = entityManager
					.createNativeQuery(
							"select doc_name from m_document_type where doc_type_id=" + entity.getSelDocumentType())
					.getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelDocumentTypeVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery("select state_name from m_state where state_id=" + entity.getSelState())
					.getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelStateVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery(
							"select district_name from m_district where district_id=" + entity.getSelDistrict())
					.getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelDistrictVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery("select block_name from m_block where block_id=" + entity.getSelBlockULB())
					.getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelBlockULBVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery("select gp_name from m_gp where gp_id=" + entity.getSelGPWardNo())
					.getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelGPWardNoVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager.createNativeQuery(
					"select village_name from m_village_master where village_id=" + entity.getSelVillageLocalAreaName())
					.getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelVillageLocalAreaNameVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery("select state_name from m_state where state_id=" + entity.getSelState17())
					.getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelState17Val(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery(
							"select district_name from m_district where district_id=" + entity.getSelDistrict18())
					.getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelDistrict18Val(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery("select block_name from m_block where block_id=" + entity.getSelBlockULB19())
					.getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelBlockULB19Val(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery("select gp_name from m_gp where gp_id=" + entity.getSelGPWARDNumber())
					.getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelGPWARDNumberVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager.createNativeQuery("select village_name from m_village_master where village_id="
					+ entity.getSelVillageLocalAreaName21()).getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelVillageLocalAreaName21Val(dynamicValue.toString());

		return new JSONObject(entity);
	}

	@Override
	public JSONArray getAll(String formParams) {
		JSONObject jsonData = new JSONObject(formParams);
		List<Noc_applicant> noc_applicantResp = noc_applicantRepository.findAllByBitDeletedFlag(false);
		for (Noc_applicant entity : noc_applicantResp) {
			try {
				dynamicValue = entityManager
						.createNativeQuery(
								"select doc_name from m_document_type where doc_type_id=" + entity.getSelDocumentType())
						.getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelDocumentTypeVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select state_name from m_state where state_id=" + entity.getSelState())
						.getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelStateVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery(
								"select district_name from m_district where district_id=" + entity.getSelDistrict())
						.getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelDistrictVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select block_name from m_block where block_id=" + entity.getSelBlockULB())
						.getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelBlockULBVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select gp_name from m_gp where gp_id=" + entity.getSelGPWardNo())
						.getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelGPWardNoVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select village_name from m_village_master where village_id="
								+ entity.getSelVillageLocalAreaName())
						.getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelVillageLocalAreaNameVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select state_name from m_state where state_id=" + entity.getSelState17())
						.getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelState17Val(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery(
								"select district_name from m_district where district_id=" + entity.getSelDistrict18())
						.getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelDistrict18Val(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select block_name from m_block where block_id=" + entity.getSelBlockULB19())
						.getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelBlockULB19Val(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select gp_name from m_gp where gp_id=" + entity.getSelGPWARDNumber())
						.getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelGPWARDNumberVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select village_name from m_village_master where village_id="
								+ entity.getSelVillageLocalAreaName21())
						.getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelVillageLocalAreaName21Val(dynamicValue.toString());

		}
		return new JSONArray(noc_applicantResp);
	}

	@Override
	public JSONObject deleteById(Integer id) {
		JSONObject json = new JSONObject();
		try {
			Noc_applicant entity = noc_applicantRepository.findByIntIdAndBitDeletedFlag(id, false);
			entity.setBitDeletedFlag(true);
			noc_applicantRepository.save(entity);
			json.put("status", 200);
		} catch (Exception e) {
			log.info("Exception inside deleteById() in Noc_ApplicantServiceImpl !!" + e.getMessage());
			json.put("status", 400);
		}
		return json;
	}

	public static JSONArray fillselDocumentTypeList(EntityManager em) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select doc_type_id,doc_name from m_document_type where deleted_flag = '0'";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("doc_type_id", data[0]);
			jsonObj.put("doc_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselStateList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select state_id,state_name from m_state where deleted_flag = '0'";
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
		String val = "";
		val = jsonDepend.get("state_id").toString();
		String query = "Select district_id,district_name from m_district where deleted_flag = '0' and state_id=" + val;
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

	public static JSONArray fillselBlockULBList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("district_id").toString();
		String query = "Select block_id,block_name from m_block where deleted_flag = '0' and district_id=" + val;
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("block_id", data[0]);
			jsonObj.put("block_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselGPWardNoList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("block_id").toString();
		String query = "Select gp_id,gp_name from m_gp where deleted_flag = '0' and block_id=" + val;
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("gp_id", data[0]);
			jsonObj.put("gp_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselVillageLocalAreaNameList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("gp_id").toString();
		String query = "Select village_id,village_name from m_village_master where deleted_flag = '0' and gp_id=" + val;
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("village_id", data[0]);
			jsonObj.put("village_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselState17List(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select state_id,state_name from m_state where deleted_flag = '0'";
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
		String val = "";
		val = jsonDepend.get("state_id").toString();
		String query = "Select district_id,district_name from m_district where deleted_flag = '0' and state_id=" + val;
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

	public static JSONArray fillselBlockULB19List(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("district_id").toString();
		String query = "Select block_id,block_name from m_block where deleted_flag = '0' and district_id=" + val;
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("block_id", data[0]);
			jsonObj.put("block_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselGPWARDNumberList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("block_id").toString();
		String query = "Select gp_id,gp_name from m_gp where deleted_flag = '0' and block_id=" + val;
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("gp_id", data[0]);
			jsonObj.put("gp_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselVillageLocalAreaName21List(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("gp_id").toString();
		String query = "Select village_id,village_name from m_village_master where deleted_flag = '0' and gp_id=" + val;
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("village_id", data[0]);
			jsonObj.put("village_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

}