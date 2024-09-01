package com.csmtech.sjta.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface DashboardService {

	List<Map<String, Object>> getDistrictDetailsCitizen(String param);

	List<Map<String, Object>> getLandDetails(String inputStreamDecoder);

	List<Map<String, Object>> getApplicationStatistics(String inputStreamDecoder);

	List<Map<String, Object>> getGrievanceStatus(String inputStreamDecoder);

	List<Map<String, Object>> fetchPendingPayment(String param);

	List<Map<String, Object>> fetchLandSellPayment(String param);

	List<Map<String, Object>> getDistrictDetailsLease(String inputStreamDecoder);

}
