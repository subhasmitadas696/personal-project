package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.List;

import com.csmtech.sjta.dto.LandAllotmentReportDTO;
import com.csmtech.sjta.dto.LandAllotmentTahasilReportDTO;
import com.csmtech.sjta.dto.LandAllotmentVillageReportDTO;

public interface LandAllotmentReportRepository {
	
	public List<LandAllotmentReportDTO> getDistrictWiseLandAllotmentReport(Integer pagesize,Integer offset);
	
	public BigInteger getDistrictWiseLandAllotmentReportCount();

	public List<LandAllotmentTahasilReportDTO> getTahasilWiseLandAllotmentReport(Integer pageSize, Integer offset,
			String districtCode);

	public BigInteger getTahasilWiseLandAllotmentReportCount(String districtCode);
	
	public BigInteger getVillageWiseLandAllotmentReportCount(String tahasilCode);

	public List<LandAllotmentVillageReportDTO> getVillageWiseLandAllotmentReport(Integer pageSize, Integer offset,
			String tahasilCode);	
	

}
