package com.csmtech.sjta.repository;

import java.util.List;

public interface LandApplicationReportRepository {
	public List<Object[]> getLandReportData(Integer pagesize, Integer offset, Integer userRole);

	public List<Object[]> getDistrictWiseLandRecord(Integer pagesize, Integer offset, String districtCode, Integer userRole);

	public List<Object[]> getTahasilWiseLandRecord(Integer pagesize, Integer offset, String tahasilCode, Integer userRole);

	public List<Object[]> getLandReportExcelData();

	public List<Object[]> getDistrictWiseLandExcel(String districtCode);

	public List<Object[]> getTahasilWiseLandExcel(String tahasilCode);

	public List<Object[]> getApplicationDetails(Integer pagesize, Integer offset, String districtCode, String type);

}
