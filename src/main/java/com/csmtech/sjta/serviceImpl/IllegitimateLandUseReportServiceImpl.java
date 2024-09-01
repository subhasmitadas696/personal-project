package com.csmtech.sjta.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.IllegitimateLandUseDTO;
import com.csmtech.sjta.repository.IllegitimateLandUseReportRepository;
import com.csmtech.sjta.service.IllegitimateLandUseReportService;
import com.csmtech.sjta.util.ExcelHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author prasanta.sethi
 */
@Slf4j
@Service
public class IllegitimateLandUseReportServiceImpl implements IllegitimateLandUseReportService {

	@Autowired
	private IllegitimateLandUseReportRepository reportNativeRepo;

	@Override
	public List<IllegitimateLandUseDTO> getDistrictReportData(String data) {
		List<IllegitimateLandUseDTO> finalResultDistrict = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getDistrictReportData(pageSize, offset);
		for (Object[] result : getRecord) {
			IllegitimateLandUseDTO dto = new IllegitimateLandUseDTO();
			dto.setDistrictCode((String) result[0]);
			dto.setDistrictName((String) result[1]);
			dto.setTotalApplicant((BigInteger) result[2]);
			dto.setPending((BigInteger) result[3]);
			dto.setAssigned((BigInteger) result[4]);
			dto.setInspectionCompleted((BigInteger) result[5]);
			dto.setApplicationOnHold((BigInteger) result[6]);
			dto.setClosed((BigInteger) result[7]);
			dto.setApplDiscarded((BigInteger) result[8]);
			finalResultDistrict.add(dto);
		}
		return finalResultDistrict;
	}

	@Override
	public List<IllegitimateLandUseDTO> getTahasilWiseDetails(String data) {
		List<IllegitimateLandUseDTO> finalResultTahasil = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String districtCode = jsonData.getString("districtCode");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getTahasilWiseDetails(pageSize, offset, districtCode);
		for (Object[] result : getRecord) {
			IllegitimateLandUseDTO dto = new IllegitimateLandUseDTO();
			dto.setDistrictCode((String) result[0]);
			dto.setDistrictName((String) result[1]);
			dto.setTahasilCode((String) result[2]);
			dto.setTahasilName((String) result[3]);
			dto.setTotalApplicant((BigInteger) result[4]);
			dto.setPending((BigInteger) result[5]);
			dto.setAssigned((BigInteger) result[6]);
			dto.setInspectionCompleted((BigInteger) result[7]);
			dto.setApplicationOnHold((BigInteger) result[8]);
			dto.setClosed((BigInteger) result[9]);
			dto.setApplDiscarded((BigInteger) result[8]);
			finalResultTahasil.add(dto);
		}
		return finalResultTahasil;

	}

	@Override
	public List<IllegitimateLandUseDTO> getVillageWiseDetails(String data) {
		List<IllegitimateLandUseDTO> finalResultVillage = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String tahasilCode = jsonData.getString("tahasilCode");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getVillageWiseDetails(pageSize, offset, tahasilCode);
		for (Object[] result : getRecord) {
			IllegitimateLandUseDTO dto = new IllegitimateLandUseDTO();
			dto.setDistrictCode((String) result[0]);
			dto.setDistrictName((String) result[1]);
			dto.setTahasilCode((String) result[2]);
			dto.setTahasilName((String) result[3]);
			dto.setVillageCode((String) result[4]);
			dto.setVillageName((String) result[5]);
			dto.setTotalApplicant((BigInteger) result[6]);
			dto.setPending((BigInteger) result[7]);
			dto.setAssigned((BigInteger) result[8]);
			dto.setApplicationOnHold((BigInteger) result[9]);
			dto.setInspectionCompleted((BigInteger) result[10]);
			dto.setClosed((BigInteger) result[11]);
			dto.setApplDiscarded((BigInteger) result[12]);
			finalResultVillage.add(dto);
		}
		return finalResultVillage;
	}

	@Override
	public List<IllegitimateLandUseDTO> getDistrictStatusWiseDetails(String data) {
		List<IllegitimateLandUseDTO> finalResultDistrict = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String districtCode = jsonData.getString("districtCode");
		Integer statusValue = jsonData.getInt("statusValue");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getDistrictWiseDetails(pageSize, offset, districtCode, statusValue);
		for (Object[] result : getRecord) {
			IllegitimateLandUseDTO dto = new IllegitimateLandUseDTO();
			String str = "";
			dto.setDistrictCode((String) result[0]);
			dto.setTahasilCode((String) result[1]);
			dto.setGrievanceNo((String) result[2]);
			dto.setApplicantName((String) result[3]);
			List<Map<String, String>> applicantDataList = null;
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				if (result[4] != null) {
					applicantDataList = objectMapper.readValue(result[4].toString(),
							new TypeReference<List<Map<String, String>>>() {
							});
				}

				dto.setDataList(applicantDataList);
			} catch (IOException e) {
				log.info("Inside exception in Illegitimate land use getDistrictStatusWiseDetails!!");
			}
			dto.setGrievanceStatus((Short) result[5]);
			finalResultDistrict.add(dto);
		}
		return finalResultDistrict;
	}

	@Override
	public BigInteger getDistrictStatusWiseRecordCount(String data) {
		JSONObject jsonData = new JSONObject(data);
		String districtCode = jsonData.getString("districtCode");
		Integer statusValue = jsonData.getInt("statusValue");

		// Call the repository or service method to get the count
		return reportNativeRepo.getDistrictWiseDetailsCount(districtCode, statusValue);
	}

	@Override
	public List<IllegitimateLandUseDTO> getTahasilStatusWiseDetails(String data) {
		List<IllegitimateLandUseDTO> statusWiseResultTahasil = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String districtCode = jsonData.getString("districtCode");
		String tahasilCode = jsonData.getString("tahasilCode");
		Integer statusValue = jsonData.getInt("statusValue");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getTahasilStatusWiseDetails(pageSize, offset, districtCode,
				tahasilCode, statusValue);
		for (Object[] result : getRecord) {
			IllegitimateLandUseDTO dto = new IllegitimateLandUseDTO();
			String str = "";
			dto.setDistrictCode((String) result[0]);
			dto.setTahasilCode((String) result[1]);
			dto.setGrievanceNo((String) result[2]);
			dto.setApplicantName((String) result[3]);
			List<Map<String, String>> applicantDataList = null;
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				if (result[4] != null) {
					applicantDataList = objectMapper.readValue(result[4].toString(),
							new TypeReference<List<Map<String, String>>>() {
							});
				}

				dto.setDataList(applicantDataList);
			} catch (IOException e) {
				log.info("Inside exception in Illegitimate land use ServiceImpl getTahasilStatusWiseDetails !!");
			}
			dto.setGrievanceStatus((Short) result[5]);
			statusWiseResultTahasil.add(dto);
		}
		return statusWiseResultTahasil;
	}

	@Override
	public BigInteger getTahasilStatusWiseRecordCount(String data) {
		JSONObject jsonData = new JSONObject(data);
		String districtCode = jsonData.getString("districtCode");
		String tahasilCode = jsonData.getString("tahasilCode");
		Integer statusValue = jsonData.getInt("statusValue");

		// Call the repository or service method to get the count
		return reportNativeRepo.getTahasilStatusWiseRecordCount(districtCode, tahasilCode, statusValue);
	}

	@Override
	public List<IllegitimateLandUseDTO> getVillageStatusWiseDetails(String data) {
		List<IllegitimateLandUseDTO> statusWiseResultVillage = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String districtCode = jsonData.getString("districtCode");
		String tahasilCode = jsonData.getString("tahasilCode");
		String villageCode = jsonData.getString("villageCode");
		Integer statusValue = jsonData.getInt("statusValue");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getVillageStatusWiseDetails(pageSize, offset, districtCode,
				tahasilCode, villageCode, statusValue);
		for (Object[] result : getRecord) {
			IllegitimateLandUseDTO dto = new IllegitimateLandUseDTO();
			String str = "";
			dto.setDistrictCode((String) result[0]);
			dto.setTahasilCode((String) result[1]);
			dto.setVillageCode((String) result[2]);
			dto.setGrievanceNo((String) result[3]);
			dto.setApplicantName((String) result[4]);
			List<Map<String, String>> applicantDataList = null;
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				if (result[5] != null) {
					applicantDataList = objectMapper.readValue(result[5].toString(),
							new TypeReference<List<Map<String, String>>>() {
							});
				}

				dto.setDataList(applicantDataList);
			} catch (IOException e) {
				log.info("Inside exception in Illegitimate land use ServiceImpl !!");
			}
			dto.setGrievanceStatus((Short) result[6]);
			statusWiseResultVillage.add(dto);
		}
		return statusWiseResultVillage;
	}

	@Override
	public BigInteger getVillageStatusWiseRecordCount(String data) {
		JSONObject jsonData = new JSONObject(data);
		String districtCode = jsonData.getString("districtCode");
		String tahasilCode = jsonData.getString("tahasilCode");
		String villageCode = jsonData.getString("villageCode");
		Integer statusValue = jsonData.getInt("statusValue");

		// Call the repository or service method to get the count
		return reportNativeRepo.getVillageStatusWiseRecordCount(districtCode, tahasilCode, villageCode, statusValue);
	}

	@Override
	public ByteArrayInputStream getDistrictReportDataReport() throws IOException {
		List<IllegitimateLandUseDTO> finalResultDistrict = new ArrayList<>();
		List<Object[]> getRecord = reportNativeRepo.getDistrictReportDataReport();
		for (Object[] result : getRecord) {
			IllegitimateLandUseDTO dto = new IllegitimateLandUseDTO();
			dto.setDistrictCode((String) result[0]);
			dto.setDistrictName((String) result[1]);
			dto.setTotalApplicant((BigInteger) result[2]);
			dto.setPending((BigInteger) result[3]);
			dto.setAssigned((BigInteger) result[4]);
			dto.setInspectionCompleted((BigInteger) result[5]);
			dto.setApplicationOnHold((BigInteger) result[6]);
			dto.setClosed((BigInteger) result[7]);
			dto.setApplDiscarded((BigInteger) result[8]);
			finalResultDistrict.add(dto);
		}
		return ExcelHelper.illegitimateLandDistrictWiseExcel(finalResultDistrict);
	}

	@Override
	public ByteArrayInputStream getTahasilReportDataReport(String districtCode) throws IOException {
		List<IllegitimateLandUseDTO> finalResultTahasil = new ArrayList<>();
		List<Object[]> getRecord = reportNativeRepo.getTahasilReportDataReport(districtCode);
		log.info("inside getTahasilReportDataReport !!");
		for (Object[] result : getRecord) {
			IllegitimateLandUseDTO dto = new IllegitimateLandUseDTO();
			dto.setDistrictCode((String) result[0]);
			dto.setDistrictName((String) result[1]);
			dto.setTahasilCode((String) result[2]);
			dto.setTahasilName((String) result[3]);
			dto.setTotalApplicant((BigInteger) result[4]);
			dto.setPending((BigInteger) result[5]);
			dto.setAssigned((BigInteger) result[6]);
			dto.setInspectionCompleted((BigInteger) result[7]);
			dto.setApplicationOnHold((BigInteger) result[8]);
			dto.setClosed((BigInteger) result[9]);
			dto.setApplDiscarded((BigInteger) result[10]);
			finalResultTahasil.add(dto);
		}
		return ExcelHelper.illegitimateLandTahasilWiseExcel(finalResultTahasil);
	}
}