package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.List;

public interface LesaeCaseReportRepository {
	
	public List<Object[]> getDistrictWiseLeaseRecord(Integer limit,Integer offset);
	
	public BigInteger getDistrictWiseLeaseCount();
	
	public List<Object[]> getDistrictWiseLeaseRecordForReport();
	
	public List<Object[]> getTahasilWiseLeaseRecord(Integer limit, Integer offset,String districtCode);
	
	public BigInteger getTahasilWiseLeaseCount(String districtCode);
	
	public List<Object[]> getTahasilWiseLeaseRecordForReport(String districtCode);
	
	public List<Object[]> getVillageWiseLeaseRecord(Integer limit, Integer offset,String tahasilCode);
	
	public BigInteger getVillageWiseLeaseCount(String tahasilCode);
	
	public List<Object[]> getVillageWiseLeaseRecordForReport(String tahasilCode);
	
	public List<Object[]> getKhataWiseLeaseRecord(Integer limit, Integer offset,String villageCode);
	
	public List<Object[]> getPlotWiseLeaseRecord(Integer limit, Integer offset,String khataCode);
	
	public BigInteger getKhataWiseLeaseCount(String villageCode);
	
	public BigInteger getPlotWiseLeaseCount(String khataCode);
	
	public List<Object[]> getKhataWiseLeaseRecordForReport(String villageCode);
	
	public List<Object[]> getPlotWiseLeaseRecordForReport(String khataCode);

}
