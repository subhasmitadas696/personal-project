package com.csmtech.sjta.service;

import org.json.JSONArray;
import org.json.JSONObject;

public interface Land_documentsService {
	JSONObject save(String land_documents);

	JSONObject getById(Integer Id);

	JSONArray getAll(String formParams);

	JSONObject deleteById(Integer id);

	JSONObject getByIntParentId(Integer intParentId);

	String getCurrentStage(Integer Id);
}