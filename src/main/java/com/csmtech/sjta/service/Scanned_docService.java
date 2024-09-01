package com.csmtech.sjta.service;

import org.json.JSONArray;
import org.json.JSONObject;

public interface Scanned_docService {
	JSONObject save(String scanned_doc);

	JSONObject getById(Integer Id);

	JSONObject getAll(String formParams);

	JSONObject deleteById(Integer id);

	JSONObject saveMetadata(String data);

	JSONObject getAllData(String formParams);

}