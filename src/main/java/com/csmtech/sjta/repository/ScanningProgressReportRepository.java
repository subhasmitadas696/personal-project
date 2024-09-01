package com.csmtech.sjta.repository;

import java.util.Map;

import org.json.JSONArray;

public interface ScanningProgressReportRepository {

	public Integer saveProgressData(Map<String, String>[] excelData);

	public JSONArray getProgressData(String data);

}
