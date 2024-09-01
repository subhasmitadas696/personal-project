package com.csmtech.sjta.serviceImpl;

import java.math.BigInteger;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.LandAllotmentReportDTO;
import com.csmtech.sjta.dto.LandAllotmentTahasilReportDTO;
import com.csmtech.sjta.dto.LandAllotmentVillageReportDTO;
import com.csmtech.sjta.repository.LandAllotmentReportRepository;
import com.csmtech.sjta.service.LandAllotmentReportService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LandAllotmentReportServiceImpl implements LandAllotmentReportService {
	
	@Autowired
	private LandAllotmentReportRepository repo;
	
	@Override
	public List<LandAllotmentReportDTO> getDistrictWiseLandAllotmentReport(String data) {
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		List<LandAllotmentReportDTO> getRecord = repo.getDistrictWiseLandAllotmentReport(pageSize, offset);
		log.info("LandAllotmentReportServiceImpl of method getDistrictWiseLandAllotmentReport execution success.....!!");
		return getRecord;
	}

	@Override
	public BigInteger getDistrictWiseLandAllotmentReportCount() {
		return repo.getDistrictWiseLandAllotmentReportCount();
	}

	@Override
	public List<LandAllotmentTahasilReportDTO> getTahasilWiseLandAllotmentReport(String data) {
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String districtCode = jsonData.getString("districtCode");
		Integer offset = (pageNo - 1) * pageSize;
		List<LandAllotmentTahasilReportDTO> getRecord = repo.getTahasilWiseLandAllotmentReport(pageSize, offset,districtCode);
		log.info("LandAllotmentReportServiceImpl of method getTahasilWiseLandAllotmentReport execution success.....!!");
		return getRecord;
	}

	@Override
	public BigInteger getTahasilWiseLandAllotmentReportCount(String data) {
		JSONObject jsonData = new JSONObject(data);
		String districtCode = jsonData.getString("districtCode");
		return repo.getTahasilWiseLandAllotmentReportCount(districtCode);
	}

	@Override
	public List<LandAllotmentVillageReportDTO> getVillageWiseLandAllotmentReport(String data) {
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String tahasilCode = jsonData.getString("tahasilCode");
		Integer offset = (pageNo - 1) * pageSize;
		List<LandAllotmentVillageReportDTO> getRecord = repo.getVillageWiseLandAllotmentReport(pageSize, offset,tahasilCode);
		log.info("LandAllotmentReportServiceImpl of method getVillageWiseLandAllotmentReport execution success.....!!");
		return getRecord;
	}

	@Override
	public BigInteger getVillageWiseLandAllotmentReportCount(String data) {
		JSONObject jsonData = new JSONObject(data);
		String tahasilCode = jsonData.getString("tahasilCode");
		return repo.getVillageWiseLandAllotmentReportCount(tahasilCode);
	}

}
