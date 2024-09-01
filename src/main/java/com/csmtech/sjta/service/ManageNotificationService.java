package com.csmtech.sjta.service;

import org.json.JSONArray;
import org.json.JSONObject;

public interface ManageNotificationService {
	JSONObject save(String m_notification);

	JSONObject getById(Integer Id);

	JSONArray getAll(String formParams);

	JSONObject deleteById(Integer id, Integer updatedby);

	Integer getTotalAppCount(String formParams);

	JSONObject unpublishById(Integer id, Integer updatedby);

	Integer checkPublish(Integer id);

	JSONObject publishById(Integer id, Integer updatedby);
}