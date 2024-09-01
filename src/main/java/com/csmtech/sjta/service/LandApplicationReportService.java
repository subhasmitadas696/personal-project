package com.csmtech.sjta.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import com.csmtech.sjta.dto.LandApplicationReportDTO;

public interface LandApplicationReportService {

	List<LandApplicationReportDTO> getLandReportData(String data);

	List<LandApplicationReportDTO> getDistrictWiseLandRecord(String data);

	List<LandApplicationReportDTO> getTahasilWiseLandRecord(String data);

	ByteArrayInputStream exportReportForLand() throws IOException;

	ByteArrayInputStream exportReportForLandTahasil(String districtCode) throws IOException;

	ByteArrayInputStream exportReportForLandVillage(String tahasilCode) throws IOException ;

	List<LandApplicationReportDTO> getApplicationDetails(String data);

}
