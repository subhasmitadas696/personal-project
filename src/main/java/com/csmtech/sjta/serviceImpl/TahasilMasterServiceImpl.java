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

import com.csmtech.sjta.entity.TahasilBoundary;
import com.csmtech.sjta.entity.Tahasil_master;
import com.csmtech.sjta.repository.TahasilBoundaryRepository;
import com.csmtech.sjta.repository.TahasilMasterJPARepository;
import com.csmtech.sjta.service.TahasilMasterService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service
public class TahasilMasterServiceImpl implements TahasilMasterService {
	@Autowired
	private TahasilMasterJPARepository tahasil_masterRepository;
	@Autowired
	EntityManager entityManager;
	@Autowired
	private TahasilBoundaryRepository tahasilBoundRepo;

	String parentId = null;
	Object dynamicValue = null;
	Integer totalDataPresent = 0;
	private static final Logger logger = LoggerFactory.getLogger(TahasilMasterServiceImpl.class);
	JSONObject json = new JSONObject();

	@Override
	public JSONObject save(String data) {
		logger.info("Inside save method of Tahasil_masterServiceImpl");
		try {
			ObjectMapper om = new ObjectMapper();
			Tahasil_master tahasil_master = om.readValue(data, Tahasil_master.class);

			Tahasil_master getEntity = tahasil_masterRepository
					.findByTxtTahasilCode(tahasil_master.getTxtTahasilCode());

			if (!Objects.isNull(getEntity)) {

				getEntity.setSelDistrictCode(tahasil_master.getSelDistrictCode());
				getEntity.setTxtTahasilCode(tahasil_master.getTxtTahasilCode());
				getEntity.setTxtTahasilName(tahasil_master.getTxtTahasilName());
				Tahasil_master updateData = tahasil_masterRepository.save(getEntity);
				parentId = updateData.getTxtTahasilCode();
				json.put(CommonConstant.STATUS_KEY, 202);
			} else {
				tahasil_master.setTxtTahasilCode(tahasil_master.getDistTahasilCode());
				Tahasil_master saveData = tahasil_masterRepository.save(tahasil_master);
				parentId = saveData.getTxtTahasilCode();
				json.put(CommonConstant.STATUS_KEY, 200);
			}
			json.put("id", parentId);
		} catch (Exception e) {
			logger.error("Inside save method of Tahasil_masterServiceImpl , some error occured {} ", e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(String id) {
		logger.info("Inside getById method of Tahasil_masterServiceImpl");
		Tahasil_master entity = tahasil_masterRepository.findByTxtTahasilCode(id);
		Object[] dynamicValue = null;
		try {
			dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
					"select district_name, state_code from land_bank.district_master where district_code ='"
							+ entity.getSelDistrictCode() + "'");
		} catch (Exception ex) {
			dynamicValue = new Object[] { "--" };
		}
		entity.setSelDistrictCodeVal((String) dynamicValue[0]);

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
		logger.info("Inside getAll method of Tahasil_masterServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		String selDistrictCode = "0";
		String txtTahasilCode = "0";
		String txtTahasilName = "0";
		List<Tahasil_master> tahasil_masterResp = null;

		if (jsonData.has("selDistrictCode") && !jsonData.isNull("selDistrictCode")
				&& !jsonData.getString("selDistrictCode").equals("")) {
			selDistrictCode = jsonData.getString("selDistrictCode");
			tahasil_masterResp = tahasil_masterRepository.findBySelDistrictCode(selDistrictCode);

			logger.info(selDistrictCode);
			totalDataPresent = tahasil_masterRepository.countOfDistrict(selDistrictCode);
		}
		if (jsonData.has("txtTahasilName") && !jsonData.isNull("txtTahasilName")
				&& !jsonData.getString("txtTahasilName").equals("")) {
			txtTahasilName = jsonData.getString("txtTahasilName");
			tahasil_masterResp = tahasil_masterRepository.findByTxtTahasilName(txtTahasilName, selDistrictCode);

			totalDataPresent = tahasil_masterRepository.countOfTahasilName(txtTahasilName);

		} else {
			tahasil_masterResp = tahasil_masterRepository.findBySelDistrictCodeAndTxtTahasilName(selDistrictCode,
					txtTahasilName);

			totalDataPresent = tahasil_masterRepository.countAll();
		}
//		Pageable pageRequest = PageRequest.of(jsonData.has("pageNo") ? jsonData.getInt("pageNo") - 1 : 0,
//				jsonData.has("size") ? jsonData.getInt("size") : totalDataPresent);

		Object[] dynamicValue = null;

		for (Tahasil_master entity : tahasil_masterResp) {
			try {
				dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
						"select district_name, state_code from land_bank.district_master where district_code ='"
								+ entity.getSelDistrictCode() + "'");
			} catch (Exception ex) {
				dynamicValue = new Object[] { "--" };
			}
			entity.setSelDistrictCodeVal((String) dynamicValue[0]);

		}

		logger.info("count  : " + totalDataPresent);
		json.put(CommonConstant.STATUS_KEY, 200);
		json.put(CommonConstant.RESULT, new JSONArray(tahasil_masterResp));
		json.put("count", totalDataPresent);
		return json;
	}

	public static JSONArray fillselDistrictCodeList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselDistrictCodeList method of Tahasil_masterServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select district_code,district_name from land_bank.district_master where state_code = 'OD' Order By district_name ";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("district_code", data[0]);
			jsonObj.put("district_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	@Override
	public JSONObject viewAll(String districtCode) {
		List<Object[]> tahasilBoundariesResp = tahasilBoundRepo.viewAll(districtCode);
		List<TahasilBoundary> tahasilBoundaries = new ArrayList<>();

		for (Object[] result : tahasilBoundariesResp) {
			TahasilBoundary tahasilBoundary = new TahasilBoundary();
			tahasilBoundary.setTahasilCode((String) result[0]);
			tahasilBoundary.setTahasilName((String) result[1]);
			tahasilBoundary.setExtent((String) result[2]);
			tahasilBoundaries.add(tahasilBoundary);
		}

		json.put(CommonConstant.STATUS_KEY, 200);
		json.put(CommonConstant.MESSAGE_KEY, CommonConstant.SUCCESS);
		json.put(CommonConstant.RESULT, new JSONArray(tahasilBoundaries));

		return json;

	}

	@Override
	public JSONObject getLastTahasilCode(String selDistrictCode) {
		List<Object> obj = tahasil_masterRepository.getLastTahasilCode(selDistrictCode);
		List<Tahasil_master> th = new ArrayList<>();
		for (Object result : obj) {
			Tahasil_master tm = new Tahasil_master();
			tm.setLastTahasilCode((String) result);
			th.add(tm);
		}
		json.put(CommonConstant.STATUS_KEY, 200);
		json.put(CommonConstant.MESSAGE_KEY, CommonConstant.SUCCESS);
		json.put(CommonConstant.RESULT, new JSONArray(th));
		return json;
	}

}