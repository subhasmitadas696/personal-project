package com.csmtech.sjta.serviceImpl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.LandAllotmentConfigurationDTO;
import com.csmtech.sjta.entity.LandAllotmentConfigurationEntity;
import com.csmtech.sjta.repository.LandAllotmentConfigurationClassRepository;
import com.csmtech.sjta.repository.LandAllotmentConfigurationRepository;
import com.csmtech.sjta.service.LandAllotmentConfigurationService;

@Service
public class LandAllotmentConfigurationServiceImpl implements LandAllotmentConfigurationService {

	@Autowired
	private LandAllotmentConfigurationRepository repo;

	@Autowired
	private LandAllotmentConfigurationClassRepository classRepo;

	Map<String, Object> responesData = new HashMap<>();
	
	@Override
	public Map<String, Object> landAlertmentSaveRecord(LandAllotmentConfigurationEntity dto) {
	    LandAllotmentConfigurationEntity response = repo.save(dto);
	    responesData.put("landId", response.getLandId());
	    return responesData;
	}

	@Override
	public List<LandAllotmentConfigurationDTO> getAllRecord(String formParms) {
		JSONObject jsonData = new JSONObject(formParms);
		Integer totalDataPresent = repo.countByBitDeletedFlag();
		Pageable pageRequest = PageRequest.of(jsonData.has("pageNo") ? jsonData.getInt("pageNo") - 1 : 0,
				jsonData.has("size") ? jsonData.getInt("size") : totalDataPresent);
		Integer page = pageRequest.getPageSize();
		return classRepo.getAllRecord(page);
	}

	@Override
	public Integer getCount() {
		return repo.countByBitDeletedFlag();
	}

	@Override
	public List<LandAllotmentConfigurationDTO> getFindById(BigInteger landId) {
		return classRepo.getAllRecordById(landId);
	}

	@Override
	public Integer updatDeleteFlageId(BigInteger landId) {
		return classRepo.updateLandAllotmentConfiguration(landId);
	}

	@Override
	public List<LandAllotmentConfigurationDTO> getFindByIdMore(BigInteger landId) {
		return classRepo.findLandAllotmentConfigurationById(landId);
	}
}
