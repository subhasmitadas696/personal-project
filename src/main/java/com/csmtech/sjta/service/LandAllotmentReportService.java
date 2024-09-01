package com.csmtech.sjta.service;

import java.math.BigInteger;
import java.util.List;

import com.csmtech.sjta.dto.LandAllotmentReportDTO;
import com.csmtech.sjta.dto.LandAllotmentTahasilReportDTO;
import com.csmtech.sjta.dto.LandAllotmentVillageReportDTO;

public interface LandAllotmentReportService {
	
	public List<LandAllotmentReportDTO> getDistrictWiseLandAllotmentReport(String data);
	
	public BigInteger getDistrictWiseLandAllotmentReportCount();

	public List<LandAllotmentTahasilReportDTO> getTahasilWiseLandAllotmentReport(String data);
	
	public BigInteger getTahasilWiseLandAllotmentReportCount(String data);

	public List<LandAllotmentVillageReportDTO> getVillageWiseLandAllotmentReport(String data);

	public BigInteger getVillageWiseLandAllotmentReportCount(String data);

}

