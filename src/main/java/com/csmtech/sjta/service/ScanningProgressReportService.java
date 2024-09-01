package com.csmtech.sjta.service;

import org.json.JSONObject;

public interface ScanningProgressReportService {

	JSONObject saveProgressData(String data);

	JSONObject getProgressData(String data);

}
