package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.List;

public interface LandInformationReportRepository {

	public List<Object[]> getDistrictReportData(Integer pagesize, Integer offset);

	public BigInteger getCountForDistrictReportData();

	public List<Object[]> getTahasilReportData(Integer pagesize, Integer offset, String districtCode);

	public BigInteger getCountForTahasilReportData(String districtCode);

	public List<Object[]> getVillageReportData(Integer pagesize, Integer offset, String thasilCode);

	public BigInteger getCountForVillageReportData(String thasilCode);
	
	public List<Object[]> getKhataReportData(Integer pagesize,Integer offset,String villageCode);
	
	public BigInteger getCountForKahtaReportData(String villageCode);
	
	public List<Object[]> getPlotReportData(Integer pagesize,Integer offset,String khataCode);
	
	public BigInteger getCountForPlotReportData(String khataCode);
	
	public List<Object[]> getDistrictReportDataForReport();
	
	public List<Object[]> getTahasilReportDataForReport(String districtCode);
	
	public List<Object[]> getVillageReportDataForReport(String thasilCode);
	
	public List<Object[]> getKhataReportDataForReport(String villageCode);
	
	public List<Object[]> getPlotReportDataForReport(String khataCode);
	
	public String ReverseRecordForDistrict(String districtCode);

	public List<Object[]> ReverseRecordForTahasil(String tahasilCode);

	public List<Object[]> ReverseRecordForVillage(String villageCode);
	
	public List<Object[]> ReverseRecordForKhata(String khataCode);

}
