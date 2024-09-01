package com.csmtech.sjta.service;

import org.json.JSONArray;
import org.json.JSONObject;

public interface Land_applicantService {
	JSONObject userSave(String land_applicant);

	JSONArray getAll(String formParams);

	JSONObject deleteById(Integer id);

	JSONObject getById(Integer id);

}