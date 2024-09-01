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

import com.csmtech.sjta.entity.DistrictBoundary;
import com.csmtech.sjta.entity.DistrictMaster;
import com.csmtech.sjta.repository.DistrictBoundaryRepository;
import com.csmtech.sjta.repository.DistrictMasterRepository;
import com.csmtech.sjta.service.DistrictMasterService;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service
public class DistrictMasterServiceImpl implements DistrictMasterService {
	@Autowired
	private DistrictMasterRepository district_masterRepository;
	@Autowired
	EntityManager entityManager;

	@Autowired
	private DistrictBoundaryRepository disBoundRepo;

	String parentId = null;
	Object dynamicValue = null;
	private static final Logger logger = LoggerFactory.getLogger(DistrictMasterServiceImpl.class);
	JSONObject json = new JSONObject();

	@Override
	public JSONObject save(String data) {
		logger.info("Inside save method of District_masterServiceImpl");
		try {
			ObjectMapper om = new ObjectMapper();
			DistrictMaster district_master = om.readValue(data, DistrictMaster.class);

			DistrictMaster getEntity = district_masterRepository
					.findByTxtDistrictCode(district_master.getTxtDistrictCode());

			if (!Objects.isNull(getEntity)) {

				getEntity.setSelStateCode(district_master.getSelStateCode());
				getEntity.setTxtDistrictCode(district_master.getTxtDistrictCode());
				getEntity.setTxtDistrictName(district_master.getTxtDistrictName());
				DistrictMaster updateData = district_masterRepository.save(getEntity);
				parentId = updateData.getTxtDistrictCode();
				json.put("status", 202);
			} else {
				DistrictMaster saveData = district_masterRepository.save(district_master);
				parentId = saveData.getTxtDistrictCode();
				json.put("status", 200);
			}
			json.put("id", parentId);
		} catch (Exception e) {
			logger.error("Inside save method of District_masterServiceImpl some error occur:" + e);
			json.put("status", 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(String id) {
		logger.info("Inside getById method of District_masterServiceImpl");
		DistrictMaster entity = district_masterRepository.findByTxtDistrictCode(id);
		Object[] dynamicValue = null;
		try {
			dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
					"select state_name,country_code from land_bank.state_master where state_code = '"
							+ entity.getSelStateCode() + "'");
		} catch (Exception ex) {
			dynamicValue = new Object[] { "--" };
		}
		entity.setSelStateCodeVal((String) dynamicValue[0]);

		try {
			dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
					"select  country_name, country_code from land_bank.country_master where country_code='"
							+ (String) dynamicValue[1] + "'");
		} catch (Exception ex) {
			dynamicValue = new Object[] { "--" };
		}
		entity.setSelCountryCodeVal((String) dynamicValue[0]);
		entity.setSelCountryCode((String) dynamicValue[1]);

		entityManager.close();

		return new JSONObject(entity);
	}

	@Override
	public JSONObject getAll(String formParams) {
		logger.info("Inside getAll method of District_masterServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		String txtDistrictCode = "0";
		if (jsonData.has("txtDistrictCode") && !jsonData.isNull("txtDistrictCode")
				&& !jsonData.getString("txtDistrictCode").equals("")) {
			txtDistrictCode = jsonData.getString("txtDistrictCode");
		}
		Integer totalDataPresent = district_masterRepository.countByAll();
		Pageable pageRequest = PageRequest.of(jsonData.has("pageNo") ? jsonData.getInt("pageNo") - 1 : 0,
				jsonData.has("size") ? jsonData.getInt("size") : totalDataPresent);
		List<DistrictMaster> district_masterResp = district_masterRepository.findAll(pageRequest, txtDistrictCode);
		Object[] dynamicValue = null;

		for (DistrictMaster entity : district_masterResp) {
			try {
				dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
						"select state_name,country_code from land_bank.state_master where state_code = '"
								+ entity.getSelStateCode() + "'");
			} catch (Exception ex) {
				dynamicValue = new Object[] { "--" };
			}
			entity.setSelStateCodeVal((String) dynamicValue[0]);

			try {
				dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
						"select  country_name, country_code from land_bank.country_master where country_code='"
								+ (String) dynamicValue[1] + "'");
			} catch (Exception ex) {
				dynamicValue = new Object[] { "--" };
			}
			logger.info((String) dynamicValue[0]);
			logger.info((String) dynamicValue[1]);

			entity.setSelCountryCodeVal((String) dynamicValue[0]);
			entity.setSelCountryCode((String) dynamicValue[1]);

		}
		logger.info("after  query execution method of District_masterServiceImpl with value:  " + district_masterResp);
		json.put("status", 200);
		json.put("result", new JSONArray(district_masterResp));
		json.put("count", totalDataPresent);
		return json;
	}

	public static JSONArray fillselCountryCodeList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselCountryCodeList method of District_masterServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select country_code,country_name from land_bank.country_master";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("country_code", data[0]);
			jsonObj.put("country_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselStateCodeList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselStateCodeList method of District_masterServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = jsonDepend.get("country_code").toString();
		String query = "Select state_code,state_name from land_bank.state_master where country_code = '" + val + "'";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("state_code", data[0]);
			jsonObj.put("state_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	@Override
	public JSONObject viewAll() {
		List<Object[]> districtBoundariesResp = disBoundRepo.viewAll();
		List<DistrictBoundary> districtBoundaries = new ArrayList<>();

		for (Object[] result : districtBoundariesResp) {
			DistrictBoundary districtBoundary = new DistrictBoundary();
			districtBoundary.setDistrictCode((String) result[0]);
			districtBoundary.setDistrictName((String) result[1]);
			districtBoundary.setExtent((String) result[2]);
			districtBoundaries.add(districtBoundary);
		}

		json.put("status", 200);
		json.put("message", "success");
		json.put("result", new JSONArray(districtBoundaries));

		return json;

	}

}