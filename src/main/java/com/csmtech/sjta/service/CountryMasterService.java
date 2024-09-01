package com.csmtech.sjta.service;

import org.json.JSONObject;

public interface CountryMasterService {
	JSONObject save(String country_master);

	JSONObject getAll(String formParams);

	JSONObject getById(String Id);
}