package com.csmtech.sjta.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.repository.DashboardRepository;
import com.csmtech.sjta.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {
	@Autowired
	private DashboardRepository dashboardRepo;

	private static final Logger logger = LoggerFactory.getLogger(CountryMasterServiceImpl.class);
	JSONObject json = new JSONObject();
	
	@Override
	public List<Map<String, Object>> getDistrictDetailsCitizen(String param) {
		List<Object[]> resultList = dashboardRepo.getDistrictDetailsCitizen(param);
		List<Map<String, Object>> keyValueList = new ArrayList<>();

		for (Object[] row : resultList) {
            Map<String, Object> keyValueMap = new HashMap<>();

            keyValueMap.put("district_code", row[0]);
            keyValueMap.put("district_name", row[1]);
            keyValueMap.put("tahasil", row[2]);
            keyValueMap.put("village", row[3]);
            keyValueMap.put("khata", row[4]);
            keyValueMap.put("plot", row[5]);
            keyValueMap.put("plot_area", row[6]);

            keyValueList.add(keyValueMap);
        }
		
		return keyValueList;
	}

	@Override
	public List<Map<String, Object>> getLandDetails(String param) {
		List<Object[]> resultList = dashboardRepo.getLandDetails(param);
		List<Map<String, Object>> keyValueList = new ArrayList<>();
		int rowIndex = 1;
		for (Object[] row : resultList) {
            Map<String, Object> keyValueMap = new HashMap<>();
            keyValueMap.put("keyId", rowIndex);
            keyValueMap.put("landAppId", row[0]);
            keyValueMap.put("appNo", row[1]);
            keyValueMap.put("tahasilName", row[2]);
            keyValueMap.put("khataNo", row[3]);
            keyValueMap.put("plotNo", row[4]);
            keyValueMap.put("purchaseArea", row[5].toString());
            keyValueMap.put("appStatusId", row[6]);
            keyValueMap.put("approvalActionId", row[7]);
            keyValueMap.put("appStatus", row[8]);
            keyValueMap.put("paidAmount", row[9]);
            keyValueMap.put("extent", row[10]);
            keyValueMap.put("plotExtend", row[11]);

            keyValueList.add(keyValueMap);
            rowIndex++;
        }
		
		return keyValueList;
	}

	@Override
	public List<Map<String, Object>> getApplicationStatistics(String param) {
		List<Object[]> resultList = dashboardRepo.getApplicationStatistics(param);
		List<Map<String, Object>> keyValueList = new ArrayList<>();

		for (Object[] row : resultList) {
            Map<String, Object> keyValueMap = new HashMap<>();

            keyValueMap.put("received", row[0]);
            keyValueMap.put("areaApplied", row[1].toString());
            keyValueMap.put("approved", row[2]);
            keyValueMap.put("forwarded", row[3]);
            keyValueMap.put("reverted", row[4]);
            keyValueMap.put("rejected", row[5]);
            keyValueMap.put("pending", row[6]);

            keyValueList.add(keyValueMap);
        }
		
		return keyValueList;
	}

	@Override
	public List<Map<String, Object>> getGrievanceStatus(String param) {
		List<Object[]> resultList = dashboardRepo.getGrievanceStatus(param);
		List<Map<String, Object>> keyValueList = new ArrayList<>();

		for (Object[] row : resultList) {
            Map<String, Object> keyValueMap = new HashMap<>();

            keyValueMap.put("total", row[0]);
            keyValueMap.put("assigned", row[1].toString());
            keyValueMap.put("closed", row[2]);
            keyValueMap.put("discarded", row[3]);
            keyValueMap.put("pending", row[4]);

            keyValueList.add(keyValueMap);
        }
		
		return keyValueList;
	}
	
	@Override
	public List<Map<String, Object>> fetchPendingPayment(String param){
		List<Map<String, Object>> keyValueList = new ArrayList<>();
		List<Object[]> resultList = dashboardRepo.fetchPendingPayment(param);
		for(Object[] row : resultList) {
			Map<String, Object> keyValueMap = new HashMap<>();
			
            keyValueMap.put("month", row[0]);
            keyValueMap.put("amount", String.format("%.2f", (row[1] != null ? Double.parseDouble(row[1].toString()) : 0.00)));
            keyValueList.add(keyValueMap);
        }
		return keyValueList;
	}

	@Override
	public List<Map<String, Object>> fetchLandSellPayment(String param) {
		List<Map<String, Object>> keyValueList = new ArrayList<>();
		List<Object[]> resultList = dashboardRepo.fetchLandSellPayment(param);
		for(Object[] row : resultList) {
			Map<String, Object> keyValueMap = new HashMap<>();
			
            keyValueMap.put("month", row[0]);
            keyValueMap.put("amount", String.format("%.2f", (row[1] != null ? Double.parseDouble(row[1].toString()) : 0.00)));
            keyValueList.add(keyValueMap);
        }
		return keyValueList;
	}

	@Override
	public List<Map<String, Object>> getDistrictDetailsLease(String param) {
		List<Object[]> resultList = dashboardRepo.getDistrictDetailsLease(param);
		List<Map<String, Object>> keyValueList = new ArrayList<>();

		for (Object[] row : resultList) {
            Map<String, Object> keyValueMap = new HashMap<>();

            keyValueMap.put("district_code", row[0]);
            keyValueMap.put("district_name", row[1]);
            keyValueMap.put("tahasil", row[2]);
            keyValueMap.put("village", row[3]);
            keyValueMap.put("khata", row[4]);
            keyValueMap.put("plot", row[5]);
            keyValueMap.put("plot_area", row[6]);
            keyValueMap.put("applied_area", row[7]);

            keyValueList.add(keyValueMap);
        }
		
		return keyValueList;
	}

}
