/**
 * 
 */
package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.List;

/**
 * @author prasanta.sethi
 */
public interface IllegitimateLandUseReportRepository {

	public List<Object[]> getDistrictReportData(Integer pageSize, Integer offset);

	public List<Object[]> getTahasilWiseDetails(Integer pageSize, Integer offset, String districtCode);

	public List<Object[]> getVillageWiseDetails(Integer pageSize, Integer offset, String tahasilCode);

	public List<Object[]> getDistrictWiseDetails(Integer pageSize, Integer offset, String districtCode, Integer statusValue);

	BigInteger getDistrictWiseDetailsCount(String districtCode, Integer statusValue);

	public List<Object[]> getTahasilStatusWiseDetails(Integer pageSize, Integer offset, String districtCode,
			String tahasilCode, Integer statusValue);

	BigInteger getTahasilStatusWiseRecordCount(String districtCode, String tahasilCode, Integer statusValue);

	public List<Object[]> getVillageStatusWiseDetails(Integer pageSize, Integer offset, String districtCode,
			String tahasilCode, String villageCode, Integer statusValue);

	public BigInteger getVillageStatusWiseRecordCount(String districtCode, String tahasilCode, String villageCode,
			Integer statusValue);
	
	public List<Object[]> getDistrictReportDataReport();

	public List<Object[]> getTahasilReportDataReport(String districtCode);
	
}
