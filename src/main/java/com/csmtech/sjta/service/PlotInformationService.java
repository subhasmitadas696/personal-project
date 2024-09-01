package com.csmtech.sjta.service;

import org.json.JSONObject;

public interface PlotInformationService {
	JSONObject save(String plot_information);

	JSONObject getById(String Id);

	JSONObject getAll(String formParams);

	JSONObject viewAll(String villageCode, String khataNo);

	JSONObject plotInfoForTableView(String villageCode);

	JSONObject getKissam();

}