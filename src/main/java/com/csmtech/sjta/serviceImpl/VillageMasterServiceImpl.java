package com.csmtech.sjta.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.entity.VillageBoundary;
import com.csmtech.sjta.entity.Village_master;
import com.csmtech.sjta.repository.VillageBoundaryRepository;
import com.csmtech.sjta.repository.VillageMasterRepo;
import com.csmtech.sjta.service.VillageMasterService;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service
public class VillageMasterServiceImpl implements VillageMasterService {
	@Autowired
	private VillageMasterRepo village_masterRepository;
	@Autowired
	EntityManager entityManager;
	@Autowired
	private VillageBoundaryRepository villageBoundRepo;

	String parentId = null;
	Object dynamicValue = null;
	private static final Logger logger = LoggerFactory.getLogger(VillageMasterServiceImpl.class);
	JSONObject json = new JSONObject();

	@Override
	public JSONObject save(String data) {
		logger.info("Inside save method of Village_masterServiceImpl");
		try {
			ObjectMapper om = new ObjectMapper();
			Village_master village_master = om.readValue(data, Village_master.class);
			Village_master getEntity = village_masterRepository
					.findByTxtVillageCode(village_master.getTxtVillageCode());

			if (!Objects.isNull(getEntity)) {

				getEntity.setSelTahasilCode(village_master.getSelTahasilCode());
				getEntity.setTxtVillageCode(village_master.getTxtVillageCode());
				getEntity.setTxtVillageName(village_master.getTxtVillageName());
				getEntity.setTxtPSName(village_master.getTxtPSName());
				getEntity.setTxtThanaNo(village_master.getTxtThanaNo());
				getEntity.setLastPublicationYear(village_master.getLastPublicationYear());
				Village_master updateData = village_masterRepository.save(getEntity);
				parentId = updateData.getTxtVillageCode();
				json.put("status", 202);
			} else {
				village_master.setTxtVillageCode(village_master.getTahasilVillageCode());
				Village_master saveData = village_masterRepository.save(village_master);
				parentId = saveData.getTxtVillageCode();
				json.put("status", 200);
			}
			json.put("id", parentId);
		} catch (Exception e) {
			logger.error("Inside save method of Village_masterServiceImpl some error occur {} ", e.getMessage());
			json.put("status", 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(String id) {
		logger.info("Inside getById method of Village_masterServiceImpl");
		Village_master entity = village_masterRepository.findByTxtVillageCode(id);
		Object[] dynamicValue = null;
		try {
			dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
					"select tahasil_name, district_code from land_bank.tahasil_master where tahasil_code ='"
							+ entity.getSelTahasilCode() + "'");
		} catch (Exception ex) {
			dynamicValue = new Object[] { "--" };
		}
		entity.setSelTahasilCodeVal((String) dynamicValue[0]);

		try {
			dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
					"select  district_name, state_code, district_code from land_bank.district_master where district_code='"
							+ (String) dynamicValue[1] + "'");
		} catch (Exception ex) {
			dynamicValue = new Object[] { "--" };
		}
		entity.setSelDistrictCodeVal((String) dynamicValue[0]);
		entity.setSelDistrictCode((String) dynamicValue[2]);

		try {
			dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
					"select  state_name, country_code, state_code from land_bank.state_master where state_code='"
							+ (String) dynamicValue[1] + "'");
		} catch (Exception ex) {
			dynamicValue = new Object[] { "--" };
		}
		entity.setSelStateCodeVal((String) dynamicValue[0]);
		entity.setSelStateCode((String) dynamicValue[2]);

		try {
			dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
					"select  country_name, country_code from land_bank.country_master where country_code='"
							+ (String) dynamicValue[1] + "'");
		} catch (Exception ex) {
			dynamicValue = new Object[] { "--" };
		}
		entity.setSelCountryCodeVal((String) dynamicValue[0]);
		entity.setSelCountryCode((String) dynamicValue[1]);

		return new JSONObject(entity);
	}

	@Override
	public JSONObject getAll(String formParams) {
		logger.info("Inside getAll method of Village_masterServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		String selTahasilCode = "0";
		String txtVillageCode = "0";
		String txtVillageName = "0";
		List<Village_master> village_masterResp = null;
		if (jsonData.has("selTahasilCode") && !jsonData.isNull("selTahasilCode")
				&& !jsonData.getString("selTahasilCode").equals("")) {
			selTahasilCode = jsonData.getString("selTahasilCode");
			village_masterResp = village_masterRepository.findByselTahasilCode(selTahasilCode);
		}

		if (jsonData.has("txtVillageName") && !jsonData.isNull("txtVillageName")
				&& !jsonData.getString("txtVillageName").equals("")) {
			txtVillageName = jsonData.getString("txtVillageName");
			village_masterResp = village_masterRepository.findByTxtTahasilName(txtVillageName, selTahasilCode);
		}
		Integer totalDataPresent = village_masterRepository.countAll();
		Pageable pageRequest = PageRequest.of(jsonData.has("pageNo") ? jsonData.getInt("pageNo") - 1 : 0,
				jsonData.has("size") ? jsonData.getInt("size") : totalDataPresent);
		village_masterResp = village_masterRepository.findAll(selTahasilCode, txtVillageCode, txtVillageName);
		Object[] dynamicValue = null;

		for (Village_master entity : village_masterResp) {
			try {
				dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
						"select tahasil_name, district_code from land_bank.tahasil_master where tahasil_code ='"
								+ entity.getSelTahasilCode() + "'");
			} catch (Exception ex) {
				dynamicValue = new Object[] { "--" };
			}
			entity.setSelTahasilCodeVal((String) dynamicValue[0]);

			try {
				dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
						"select  district_name, state_code, district_code from land_bank.district_master where district_code='"
								+ (String) dynamicValue[1] + "'");
			} catch (Exception ex) {
				dynamicValue = new Object[] { "--" };
			}

			entity.setSelDistrictCodeVal((String) dynamicValue[0]);
			entity.setSelDistrictCode((String) dynamicValue[2]);

			try {
				dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
						"select  state_name, country_code, state_code from land_bank.state_master where state_code='"
								+ (String) dynamicValue[1] + "'");
			} catch (Exception ex) {
				dynamicValue = new Object[] { "--" };
			}

			entity.setSelStateCodeVal((String) dynamicValue[0]);
			entity.setSelStateCode((String) dynamicValue[2]);

			try {
				dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
						"select  country_name, country_code from land_bank.country_master where country_code='"
								+ (String) dynamicValue[1] + "'");
			} catch (Exception ex) {
				dynamicValue = new Object[] { "--" };
			}

			entity.setSelCountryCodeVal((String) dynamicValue[0]);
			entity.setSelCountryCode((String) dynamicValue[1]);

		}
		logger.info("after  query execution method of village_masterServiceImpl with value {}  ", village_masterResp);

		json.put("status", 200);
		json.put("result", new JSONArray(village_masterResp));
		json.put("count", totalDataPresent);
		return json;
	}

	public static JSONArray fillselDistrictCodeList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselDistrictCodeList method of Tahasil_masterServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select district_code,district_name from land_bank.district_master where state_code = 'OD' Order By district_name";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("district_code", data[0]);
			jsonObj.put("district_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselTahasilCodeList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselTahasilCodeList method of Village_masterServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = jsonDepend.get("district_code").toString();
		String query = "Select tahasil_code,tahasil_name from land_bank.tahasil_master where district_code = '" + val
				+ "' Order By tahasil_name";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("tahasil_code", data[0]);
			jsonObj.put("tahasil_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	@Override
	public JSONObject viewAll(String tahasilCode) {
		List<Object[]> villageBoundariesResp = villageBoundRepo.viewAll(tahasilCode);
		List<VillageBoundary> villageBoundaries = new ArrayList<>();

		for (Object[] result : villageBoundariesResp) {
			VillageBoundary villageBoundary = new VillageBoundary();
			villageBoundary.setVillageCode((String) result[0]);
			villageBoundary.setVillageName((String) result[1]);
			villageBoundary.setExtent((String) result[2]);
			villageBoundaries.add(villageBoundary);
		}

		json.put("status", 200);
		json.put("message", "success");
		json.put("result", new JSONArray(villageBoundaries));

		return json;

	}
}