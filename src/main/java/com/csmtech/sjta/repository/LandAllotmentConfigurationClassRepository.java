package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.List;

import com.csmtech.sjta.dto.LandAllotmentConfigurationDTO;

public interface LandAllotmentConfigurationClassRepository {
	
	public List<LandAllotmentConfigurationDTO> getAllRecord(Integer pageRequest);
	
	public List<LandAllotmentConfigurationDTO> getAllRecordById(BigInteger landId);

	public Integer updateLandAllotmentConfiguration(BigInteger landId);
	
	public List<LandAllotmentConfigurationDTO> findLandAllotmentConfigurationById(BigInteger landId);

}
