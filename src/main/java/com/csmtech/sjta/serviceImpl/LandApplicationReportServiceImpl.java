package com.csmtech.sjta.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.LandApplicationReportDTO;
import com.csmtech.sjta.repository.LandApplicationReportRepository;
import com.csmtech.sjta.service.LandApplicationReportService;
import com.csmtech.sjta.util.ExcelHelper;

@Service
public class LandApplicationReportServiceImpl implements LandApplicationReportService {

	@Autowired
	private LandApplicationReportRepository reportNativeRepo;

	@Override
	public List<LandApplicationReportDTO> getLandReportData(String data) {
		List<LandApplicationReportDTO> finalResult = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer userRole = jsonData.getInt("userRole");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getLandReportData(pageSize, offset, userRole);
		for (Object[] result : getRecord) {
			LandApplicationReportDTO dto = new LandApplicationReportDTO();
			dto.setDistrictCode((String) result[0]);
			dto.setDistrictName((String) result[1]);
			dto.setTotal((BigInteger) result[2]);
			dto.setAreaApplied((BigDecimal) result[3]);
			dto.setApproved((BigInteger) result[4]);
			dto.setForwarded((BigInteger) result[5]);
			dto.setReverted((BigInteger) result[6]);
			dto.setRejected((BigInteger) result[7]);
			dto.setPending((BigInteger) result[8]);
			finalResult.add(dto);
		}
		return finalResult;
	}

	@Override
	public List<LandApplicationReportDTO> getDistrictWiseLandRecord(String data) {
		List<LandApplicationReportDTO> finalResult = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String districtCode = jsonData.getString("districtCode");
		Integer userRole = jsonData.getInt("userRole");

		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getDistrictWiseLandRecord(pageSize, offset, districtCode, userRole);
		for (Object[] result : getRecord) {
			LandApplicationReportDTO dto = new LandApplicationReportDTO();
			dto.setTahasilCode((String) result[0]);
			dto.setTahasilName((String) result[1]);
			dto.setTotal((BigInteger) result[2]);
			dto.setAreaApplied((BigDecimal) result[3]);
			dto.setApproved((BigInteger) result[4]);
			dto.setForwarded((BigInteger) result[5]);
			dto.setReverted((BigInteger) result[6]);
			dto.setRejected((BigInteger) result[7]);
			dto.setPending((BigInteger) result[8]);
			dto.setDistrictName((String) result[9]);
			finalResult.add(dto);
		}
		return finalResult;
	}

	@Override
	public List<LandApplicationReportDTO> getTahasilWiseLandRecord(String data) {
		List<LandApplicationReportDTO> finalResult = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String tahasilCode = jsonData.getString("tahasilCode");
		Integer userRole = jsonData.getInt("userRole");

		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getTahasilWiseLandRecord(pageSize, offset, tahasilCode, userRole);
		for (Object[] result : getRecord) {
			LandApplicationReportDTO dto = new LandApplicationReportDTO();
			dto.setVillageCode((String) result[0]);
			dto.setVillageName((String) result[1]);
			dto.setTotal((BigInteger) result[2]);
			dto.setAreaApplied((BigDecimal) result[3]);
			dto.setApproved((BigInteger) result[4]);
			dto.setForwarded((BigInteger) result[5]);
			dto.setReverted((BigInteger) result[6]);
			dto.setRejected((BigInteger) result[7]);
			dto.setPending((BigInteger) result[8]);
			dto.setDistrictName((String) result[9]);
			dto.setTahasilName((String) result[10]);
			finalResult.add(dto);
		}
		return finalResult;
	}

	@Override
	public ByteArrayInputStream exportReportForLand() throws IOException {
		List<LandApplicationReportDTO> finalResult = new ArrayList<>();
		List<Object[]> getRecord = reportNativeRepo.getLandReportExcelData();
		for (Object[] result : getRecord) {
			LandApplicationReportDTO dto = new LandApplicationReportDTO();
			dto.setDistrictCode((String) result[0]);
			dto.setDistrictName((String) result[1]);
			dto.setTotal((BigInteger) result[2]);
			dto.setAreaApplied((BigDecimal) result[3]);
			dto.setApproved((BigInteger) result[4]);
			dto.setForwarded((BigInteger) result[5]);
			dto.setReverted((BigInteger) result[6]);
			dto.setRejected((BigInteger) result[7]);
			dto.setPending((BigInteger) result[8]);
			finalResult.add(dto);
		}
		return ExcelHelper.landRecordToExcel(finalResult);
	}

	@Override
	public ByteArrayInputStream exportReportForLandTahasil(String districtCode) throws IOException {
		List<LandApplicationReportDTO> finalResult = new ArrayList<>();

		List<Object[]> getRecord = reportNativeRepo.getDistrictWiseLandExcel(districtCode);
		for (Object[] result : getRecord) {
			LandApplicationReportDTO dto = new LandApplicationReportDTO();
			dto.setTahasilCode((String) result[0]);
			dto.setTahasilName((String) result[1]);
			dto.setTotal((BigInteger) result[2]);
			dto.setAreaApplied((BigDecimal) result[3]);
			dto.setApproved((BigInteger) result[4]);
			dto.setForwarded((BigInteger) result[5]);
			dto.setReverted((BigInteger) result[6]);
			dto.setRejected((BigInteger) result[7]);
			dto.setPending((BigInteger) result[8]);
			finalResult.add(dto);
		}
		return ExcelHelper.landRecordDistrictWiseToExcel(finalResult);

	}

	@Override
	public ByteArrayInputStream exportReportForLandVillage(String tahasilCode) throws IOException {
		List<LandApplicationReportDTO> finalResult = new ArrayList<>();

		List<Object[]> getRecord = reportNativeRepo.getTahasilWiseLandExcel(tahasilCode);
		for (Object[] result : getRecord) {
			LandApplicationReportDTO dto = new LandApplicationReportDTO();
			dto.setVillageCode((String) result[0]);
			dto.setVillageName((String) result[1]);
			dto.setTotal((BigInteger) result[2]);
			dto.setAreaApplied((BigDecimal) result[3]);
			dto.setApproved((BigInteger) result[4]);
			dto.setForwarded((BigInteger) result[5]);
			dto.setReverted((BigInteger) result[6]);
			dto.setRejected((BigInteger) result[7]);
			dto.setPending((BigInteger) result[8]);
			finalResult.add(dto);
		}
		return ExcelHelper.landRecordTahasilWiseToExcel(finalResult);
	}

	@Override
	public List<LandApplicationReportDTO> getApplicationDetails(String data) {
		List<LandApplicationReportDTO> finalResult = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String districtCode = jsonData.getString("districtCode");
		String type = jsonData.getString("type");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getApplicationDetails(pageSize, offset, districtCode, type);
		for (Object[] result : getRecord) {
			LandApplicationReportDTO dto = new LandApplicationReportDTO();
			dto.setApplicationNo((String) result[0]);
			dto.setApplicantName((String) result[1]);
			dto.setDistrictName((String) result[2]);
			dto.setTahasilName((String) result[3]);
			dto.setVillageName((String) result[4]);
			dto.setKhataNo((String) result[5]);
			dto.setPlotNo((String) result[6]);
			dto.setApplicationStatus((String) result[7]);

			finalResult.add(dto);
		}
		return finalResult;
	}

}
