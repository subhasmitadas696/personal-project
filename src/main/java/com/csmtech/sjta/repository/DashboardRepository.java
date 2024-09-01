package com.csmtech.sjta.repository;

import java.util.List;

public interface DashboardRepository {

	List<Object[]> getDistrictDetailsCitizen(String param);

	List<Object[]> getLandDetails(String param);

	List<Object[]> getApplicationStatistics(String param);

	List<Object[]> getGrievanceStatus(String param);

	List<Object[]> fetchPendingPayment(String param);

	List<Object[]> fetchLandSellPayment(String param);

	List<Object[]> getDistrictDetailsLease(String param);

}
