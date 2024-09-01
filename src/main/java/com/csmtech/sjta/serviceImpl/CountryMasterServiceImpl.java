package com.csmtech.sjta.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

import com.csmtech.sjta.entity.CountryMaster;
import com.csmtech.sjta.repository.CountryMasterRepository;
import com.csmtech.sjta.service.CountryMasterService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service
public class CountryMasterServiceImpl implements CountryMasterService {
	@Autowired
	private CountryMasterRepository country_masterRepository;

	String parentId = null;
	Object dynamicValue = null;
	private static final Logger logger = LoggerFactory.getLogger(CountryMasterServiceImpl.class);
	JSONObject json = new JSONObject();

	@Override
	public JSONObject save(String data) {
		logger.info("Inside save method of Country_masterServiceImpl");
		try {
			ObjectMapper om = new ObjectMapper();
			CountryMaster country_master = om.readValue(data, CountryMaster.class);

			CountryMaster getEntity = country_masterRepository.findByTxtCountryCode(country_master.getTxtCountryCode());

			if (!Objects.isNull(getEntity)) {
				getEntity.setTxtCountryCode(country_master.getTxtCountryCode());
				getEntity.setTxtCountryName(country_master.getTxtCountryName());
				CountryMaster updateData = country_masterRepository.save(getEntity);
				parentId = updateData.getTxtCountryCode();
				json.put("status", 202);
			} else {
				CountryMaster saveData = country_masterRepository.save(country_master);
				parentId = saveData.getTxtCountryCode();
				json.put("status", 200);
			}
			json.put("id", parentId);
		} catch (Exception e) {
			logger.error("Inside save method of Country_masterServiceImpl some error occur:" + e);
			json.put("status", 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(String id) {
		logger.info("Inside getById method of Country_masterServiceImpl");
		CountryMaster entity = country_masterRepository.findByTxtCountryCode(id);

		return new JSONObject(entity);
	}

	@Override
	public JSONObject getAll(String formParams) {
		logger.info("Inside getAll method of Country_masterServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		Integer totalDataPresent = country_masterRepository.countAll();
		Pageable pageRequest = PageRequest.of(jsonData.has("pageNo") ? jsonData.getInt("pageNo") - 1 : 0,
				jsonData.has("size") ? jsonData.getInt("size") : totalDataPresent);
		Page<CountryMaster> country_masterResp = country_masterRepository.findAll(pageRequest);
		List<CountryMaster> list = new ArrayList<>();

		if (country_masterResp != null && country_masterResp.hasContent()) {
			list = country_masterResp.getContent();
		}

		json.put("status", 200);
		json.put("result", new JSONArray(list));

		json.put("count", totalDataPresent);
		return json;
	}

}