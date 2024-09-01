package com.csmtech.sjta.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.csmtech.sjta.dto.LandAllotmentConfigurationDTO;
import com.csmtech.sjta.entity.LandAllotmentConfigurationEntity;

public interface LandAllotmentConfigurationService {
	
	public  Map<String, Object> landAlertmentSaveRecord(LandAllotmentConfigurationEntity dto);
	
	public List<LandAllotmentConfigurationDTO> getAllRecord(String formParms);
	
	public Integer getCount();
	
	public  List<LandAllotmentConfigurationDTO> getFindById(BigInteger landId);
	
	public Integer updatDeleteFlageId(BigInteger landId);
	
	public List<LandAllotmentConfigurationDTO> getFindByIdMore(BigInteger landId);
	
	

}
