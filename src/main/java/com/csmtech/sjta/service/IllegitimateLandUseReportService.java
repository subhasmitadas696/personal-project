/**
 * 
 */
package com.csmtech.sjta.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import com.csmtech.sjta.dto.IllegitimateLandUseDTO;

/**
 * @author prasanta.sethi
 */
public interface IllegitimateLandUseReportService {

	public List<IllegitimateLandUseDTO> getDistrictReportData(String data);

	public List<IllegitimateLandUseDTO> getTahasilWiseDetails(String data);

	public List<IllegitimateLandUseDTO> getVillageWiseDetails(String data);

	public List<IllegitimateLandUseDTO> getDistrictStatusWiseDetails(String data);

	public BigInteger getDistrictStatusWiseRecordCount(String data);

	public List<IllegitimateLandUseDTO> getTahasilStatusWiseDetails(String data);

	public BigInteger getTahasilStatusWiseRecordCount(String data);

	public List<IllegitimateLandUseDTO> getVillageStatusWiseDetails(String data);

	public BigInteger getVillageStatusWiseRecordCount(String data);
	
	public ByteArrayInputStream getDistrictReportDataReport() throws IOException;

	public ByteArrayInputStream getTahasilReportDataReport(String districtCode) throws IOException;

}
