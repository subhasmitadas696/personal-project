/**
 * 
 */
package com.csmtech.sjta.serviceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.DigitalFileDTO;
import com.csmtech.sjta.dto.StatisticsDTO;
import com.csmtech.sjta.repository.LandAreaStatisticsRepository;
import com.csmtech.sjta.service.LandAreaStatisticsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 */
@Service
public class LandAreaStatisticsServiceImpl implements LandAreaStatisticsService {

	@Autowired
	private LandAreaStatisticsRepository landAreaStatisticsRepository;

	@Value("${app.url}")
	private String appUrl;

	@Override
	public StatisticsDTO fetchStatisticsInfo(String data) {
		return landAreaStatisticsRepository.fetchStatisticsInfo(data);
	}

	@Override
	public List<Map<String, Object>> plotsDoc(String formParams) {
		List<DigitalFileDTO> resultList = landAreaStatisticsRepository.plotsDoc(formParams);
		List<Map<String, Object>> keyValueList = new ArrayList<>();
		for (DigitalFileDTO row : resultList) {
			Map<String, Object> keyValueMap = new HashMap<>();
			keyValueMap.put("doc_name", row.getDigitalFile());
			keyValueMap.put("link", appUrl + "/scanning-data/viewScanDocument/1/" + row.getDigitalFile());
			keyValueList.add(keyValueMap);
		}
		return keyValueList;
	}

	@Override
	public List<Map<String, Object>> plotInfoForTableView(String formParams) {
		List<Object[]> resultList = landAreaStatisticsRepository.plotInfoForTableView(formParams);
		List<Map<String, Object>> keyValueList = new ArrayList<>();
		for (Object[] row : resultList) {
			Map<String, Object> keyValueMap = new HashMap<>();
			keyValueMap.put("extent", row[0] != null ? row[0] : "");
			keyValueMap.put("areaAcre", row[1] != null ? row[1].toString() : "0.0000");
			keyValueMap.put("plotNo", row[2] != null ? row[2] : "");
			keyValueMap.put("villageName", row[3] != null ? row[3] : "");
			keyValueMap.put("khataNo", row[4] != null ? row[4] : "");
			keyValueMap.put("kissam", row[5] != null ? row[5] : "");
			keyValueMap.put("plotCode", row[6] != null ? row[6] : "");
			keyValueMap.put("villageCode", row[7] != null ? row[7] : "");
			keyValueMap.put("chaka_number", row[8] != null ? row[8] : "");
			keyValueMap.put("sotwa", row[9] != null ? row[9] : "");
			keyValueMap.put("marfatdar_name", row[10] != null ? row[10] : "");
			keyValueMap.put("publication_date", row[11] != null ? row[11] : "");
			keyValueMap.put("owner_name", row[12] != null ? row[12] : "");
			keyValueMap.put("current_status", row[13] != null ? row[13] : "");
			keyValueMap.put("lulc_description", row[14]);
			keyValueList.add(keyValueMap);
		}
		return keyValueList;
	}

	@Override
	public List<Map<String, Object>> landApplicationDetails(String formParams) {
		List<Object[]> resultList = landAreaStatisticsRepository.landApplicationDetails(formParams);
		List<Map<String, Object>> keyValueList = new ArrayList<>();
		for (Object[] row : resultList) {
			Map<String, Object> keyValueMap = new HashMap<>();
			keyValueMap.put("applicationNo", row[0] != null ? row[0] : "");
			keyValueMap.put("applicantName", row[1] != null ? row[1] : "");
			keyValueMap.put("currentStatus", row[3] != null ? row[3] : "");

			ObjectMapper objectMapper = new ObjectMapper();
			List<Map<String, String>> documents = null;
			try {
				if (row[2] != null) {
					documents = objectMapper.readValue(row[2].toString(),
							new TypeReference<List<Map<String, String>>>() {
							});

					for (Map<String, String> document : documents) {
						// Modify the contents of each document map
						document.put("docs_path", appUrl + "/downloadDocument/" + document.get("docs_path"));
					}
				}
			} catch (IOException e) {
			}

			keyValueMap.put("documents", documents);

			keyValueList.add(keyValueMap);
		}
		return keyValueList;
	}

	@Override
	public List<Map<String, Object>> landMeetingDetails(String formParams) {
		List<Object[]> resultList = landAreaStatisticsRepository.landMeetingDetails(formParams);
		List<Map<String, Object>> keyValueList = new ArrayList<>();
		for (Object[] row : resultList) {
			Map<String, Object> keyValueMap = new HashMap<>();
			keyValueMap.put("meetingNo", row[0] != null ? row[0] : "");
			keyValueMap.put("meetingLevel", row[1] != null ? row[1] : "");
			keyValueMap.put("meetingDate", row[2] != null ? row[2] : "");
			keyValueMap.put("document", row[3] != null ? row[3] : "");

			keyValueList.add(keyValueMap);
		}
		return keyValueList;
	}

	@Override
	public Integer landMerge(JSONObject data) {
		return landAreaStatisticsRepository.landMerge(data);
	}

	@Override
	public Integer landSplit(JSONObject data) {
		return landAreaStatisticsRepository.landSplit(data);
	}

	@Override
	public List<Map<String, Object>> getMergeSplitDetails(String type) {
		List<Object[]> resultList = landAreaStatisticsRepository.getMergeSplitDetails(type);

		List<Map<String, Object>> keyValueList = new ArrayList<>();
		if (type.equals("merge")) {
			for (Object[] row : resultList) {
				Map<String, Object> keyValueMap = new HashMap<>();
				keyValueMap.put("gId", row[0]);
				keyValueMap.put("villageCode", row[1]);
				keyValueMap.put("extent", row[2]);
				keyValueMap.put("refId", row[3]);
				keyValueMap.put("newPlotNo", row[4]);
				keyValueMap.put("khataNo", row[5]);
				keyValueMap.put("type", row[6]);
				keyValueList.add(keyValueMap);
			}
		} else if (type.equals("split")) {
			Map<String, Map<String, Object>> groupedEntries = new LinkedHashMap<>();

			for (Object[] row : resultList) {
				String key = row[3] + "-" + row[1] + "-" + row[5] + "-" + row[6]; 

				if (!groupedEntries.containsKey(key)) {
					Map<String, Object> keyValueMap = new HashMap<>();
					keyValueMap.put("villageCode", row[1]);
					keyValueMap.put("refId", row[3]);
					keyValueMap.put("khataNo", row[5]);
					keyValueMap.put("type", row[6]);
					keyValueMap.put("plotDetails", new ArrayList<Map<String, Object>>());
					groupedEntries.put(key, keyValueMap);
				}

				Map<String, Object> plotDetails = new HashMap<>();
				plotDetails.put("gId", row[0]);
				plotDetails.put("extent", row[2]);
				plotDetails.put("newPlotNo", row[4]);

				((List<Map<String, Object>>) groupedEntries.get(key).get("plotDetails")).add(plotDetails);
			}

			keyValueList = new ArrayList<>(groupedEntries.values());
		}

		return keyValueList;
	}

	@Override
	public Integer updateRollBack(JSONObject formParams) {
		return landAreaStatisticsRepository.updateRollBack(formParams);
	}

}
