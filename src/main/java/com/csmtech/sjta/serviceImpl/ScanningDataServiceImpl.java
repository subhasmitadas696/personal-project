package com.csmtech.sjta.serviceImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.repository.ScanningDataRepository;
import com.csmtech.sjta.service.ScanningDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
public class ScanningDataServiceImpl implements ScanningDataService {

	@Autowired
	EntityManager em;
	
	@Autowired
	private ScanningDataRepository scanning_data_repo;
	
	JSONObject json = new JSONObject();
	
	@Value("${rordigitalfile.path}")
	private String rorDigitalFile;
	
	@Value("${casefiledigitalfile.path}")
	private String caseFileDigitalFile;
	
	@Override
	public JSONObject getFileList(String data) {
		ObjectMapper om = new ObjectMapper();
		try {
			JsonNode jsonNode = om.readTree(data);
			
			Integer fileType = jsonNode.get("selFileType").asInt();
			String districtCode = jsonNode.get("selDistrictCode").asText();
			String tahasilCode = jsonNode.get("selTahasilCode").asText();
			String villageCode = jsonNode.get("selVillageCode").asText();
//			String filePath = "";
//			String fileNameContains = "";
			
//			if(fileType == 1) {
//				filePath = rorDigitalFile;
//			} else {
//				filePath = caseFileDigitalFile;
//			}
//			
//			if(!districtCode.equals("0")) {
//				fileNameContains = districtCode;
//			}
//			
//			if(!tahasilCode.equals("0")) {
//				fileNameContains = tahasilCode;
//			}
//			
//			if(!villageCode.equals("0")) {
//				fileNameContains = villageCode;
//			}
			
//			if(!fileNameContains.equals("")) {
				//List<String> allFiles = searchFilesByPartialName(filePath, fileNameContains);
				List<String> allFiles = scanning_data_repo.allFiles(districtCode, tahasilCode, villageCode);
				List<String> filterFiles = scanning_data_repo.verifiedDocument();
				
				json.put("status", 200);
				json.put("result", allFiles);
				json.put("fresult", filterFiles);
//			} else {
//				json.put("status", 404);
//			}
		} catch (Exception e) {
			log.error("error occured while fetching file details: "+e.getMessage());
			json.put("status", 500);
		}
		
		return json;
	}
	
	public static JSONArray fillselDistrictList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();

		String query = "select (district_code) as district_id,(district_name)  as district_name  from land_bank.district_master WHERE state_code='OD' order by district_name";

		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("district_id", data[0]);
			jsonObj.put("district_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselTahasilList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val =jsonDepend.get("district_id").toString();
		String query = "select (tahasil_code) as tehsil_id,(tahasil_name) as tehsil_name from land_bank.tahasil_master where  district_code='" + val +"' order by tahasil_name";
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("tahasil_id", data[0]);
			jsonObj.put("tahasil_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	//add here map extension
	public static JSONArray fillselVillageList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val =jsonDepend.get("tahasil_id").toString();
		String query = "select (village_code) as village_id,(village_name) as village_name from land_bank.village_master where  tahasil_code='" + val +"' order by village_name";
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("village_id", data[0]);
			jsonObj.put("village_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	
	 private List<String> searchFilesByPartialName(String directoryPath, String partialName) {
	        List<String> matchingFiles = new ArrayList<>();
	        File directory = new File(directoryPath);

	        if (directory.isDirectory()) {
	            search(directory, partialName, matchingFiles);
	        }

	        return matchingFiles;
	    }

	    private void search(File file, String partialName, List<String> matchingFiles) {
	        if (file.isDirectory()) {
	            File[] files = file.listFiles();
	            if (files != null) {
	                for (File subFile : files) {
	                    search(subFile, partialName, matchingFiles);
	                }
	            }
	        } else if (file.getName().startsWith(partialName)) {
	            matchingFiles.add(file.getName());
	        }
	    }

		@Override
		public JSONObject verifyDocument(String data) {
			try {
				ObjectMapper om = new ObjectMapper();
				JsonNode jsonNode = om.readTree(data);
				
				Integer statusCheck = scanning_data_repo.verifyDocument(jsonNode);
				
				if(statusCheck > 0) {					
					json.put("status", 200);
				} else {
					json.put("status", 500);
				}
			} catch (Exception e) {
				json.put("status", 500);
			}

			return json;
		}

		@Override
		public JSONObject getRejectedFileList(String data) {
			json.put("status", 200);
			json.put("result", scanning_data_repo.getProgressData(data));
			return json;
		}
}
