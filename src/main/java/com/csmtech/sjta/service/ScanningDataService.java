package com.csmtech.sjta.service;

import org.json.JSONObject;

public interface ScanningDataService {
	JSONObject getFileList(String data);

	JSONObject verifyDocument(String data);

	JSONObject getRejectedFileList(String data);
}
