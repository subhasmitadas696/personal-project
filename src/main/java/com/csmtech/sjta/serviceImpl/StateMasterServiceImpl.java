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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.entity.State_master;
import com.csmtech.sjta.repository.StateMasterJPARepository;
import com.csmtech.sjta.service.StateMasterService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service
public class StateMasterServiceImpl implements StateMasterService {
	@Autowired
	private StateMasterJPARepository state_masterRepository;
	@Autowired
	EntityManager entityManager;

	String parentId = null;
	Object dynamicValue = null;
	private static final Logger logger = LoggerFactory.getLogger(StateMasterServiceImpl.class);
	JSONObject json = new JSONObject();

	@Override
	public JSONObject save(String data) {
		logger.info("Inside save method of State_masterServiceImpl");
		try {
			ObjectMapper om = new ObjectMapper();
			State_master state_master = om.readValue(data, State_master.class);
			State_master getEntity = state_masterRepository.findByTxtStateCode(state_master.getTxtStateCode());

			if (!Objects.isNull(getEntity)) {
				getEntity.setSelCountryCode(state_master.getSelCountryCode());
				getEntity.setTxtStateCode(state_master.getTxtStateCode());
				getEntity.setTxtStateName(state_master.getTxtStateName());
				State_master updateData = state_masterRepository.save(getEntity);
				parentId = updateData.getTxtStateCode();
				json.put(CommonConstant.STATUS_KEY, 202);
			} else {
				State_master saveData = state_masterRepository.save(state_master);
				parentId = saveData.getTxtStateCode();
				json.put(CommonConstant.STATUS_KEY, 200);
			}
			json.put("id", parentId);
		} catch (Exception e) {
			logger.error("Inside save method of State_masterServiceImpl , some error occur {}", e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(String id) {
		logger.info("Inside getById method of State_masterServiceImpl");
		State_master entity = state_masterRepository.findByTxtStateCode(id);
		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select country_name from land_bank.country_master where country_code = '"
							+ entity.getSelCountryCode() + "'"

			);
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelCountryCodeVal(dynamicValue.toString());

		return new JSONObject(entity);
	}

	@Override
	public JSONObject getAll(String formParams) {
		logger.info("Inside getAll method of State_masterServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		Integer totalDataPresent = state_masterRepository.countAll();
		Pageable pageRequest = PageRequest.of(jsonData.has("pageNo") ? jsonData.getInt("pageNo") - 1 : 0,
				jsonData.has("size") ? jsonData.getInt("size") : totalDataPresent);
		Page<State_master> state_masterResp = state_masterRepository.findAll(pageRequest);
		List<State_master> list = new ArrayList<>();

		if (state_masterResp != null && state_masterResp.hasContent()) {
			list = state_masterResp.getContent();
		}

		for (State_master entity : list) {
			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select country_name from land_bank.country_master where country_code = '"
								+ entity.getSelCountryCode() + "'");
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelCountryCodeVal(dynamicValue.toString());
		}

		json.put(CommonConstant.STATUS_KEY, 200);
		json.put("result", new JSONArray(list));
		json.put("count", totalDataPresent);
		return json;
	}

	public static JSONArray fillselCountryCodeList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselCountryCodeList method of State_masterServiceImpl");
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

}