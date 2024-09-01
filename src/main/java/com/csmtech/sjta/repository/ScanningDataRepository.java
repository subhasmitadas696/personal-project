package com.csmtech.sjta.repository;

import java.util.Collection;
import java.util.List;

import org.json.JSONArray;

import com.fasterxml.jackson.databind.JsonNode;

public interface ScanningDataRepository {

	Integer verifyDocument(JsonNode jsonNode);

	JSONArray getProgressData(String data);

	List<String> verifiedDocument();

	List<String> allFiles(String districtCode, String tahasilCode, String villageCode);

}
