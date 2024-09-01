package com.csmtech.sjta.service;

import org.json.JSONArray;
import org.json.JSONObject;

public interface KhatianInformationService {
	JSONObject save(String khatian_information);

	JSONObject getById(String Id);

	JSONObject getAll(String formParams);

	JSONObject viewAll(String selVillageCode);

	JSONObject viewDigitalFile(String txtKhatianCode);

	JSONObject searchPlot(String txtKhatianCode, String txtPlotNo);

	JSONObject getKhatian(String id);
}