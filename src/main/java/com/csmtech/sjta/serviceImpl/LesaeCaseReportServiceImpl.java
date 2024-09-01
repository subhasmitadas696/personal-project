package com.csmtech.sjta.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.LesaeCaseReportDTO;
import com.csmtech.sjta.repository.LesaeCaseReportRepository;
import com.csmtech.sjta.service.LesaeCaseReportService;
import com.csmtech.sjta.util.ExcelHelper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LesaeCaseReportServiceImpl implements LesaeCaseReportService {

	@Autowired
	private LesaeCaseReportRepository leaseCaseRepo;

	@Override
	public List<LesaeCaseReportDTO> getDistrictLeaseReport(String data) {
		List<LesaeCaseReportDTO> finalResultDistrict = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = leaseCaseRepo.getDistrictWiseLeaseRecord(pageSize, offset);
		for (Object[] result : getRecord) {
			LesaeCaseReportDTO dto = new LesaeCaseReportDTO();
			dto.setDistrictName((String) result[1]);
			dto.setDistrcitCode((String) result[0]);
			dto.setNoOfLeaseCseCount((BigInteger) result[2]);
			dto.setTotalPlotCount((BigInteger) result[3]);
			dto.setDlscCount((BigInteger) result[4]);
			dto.setTlscCount((BigInteger) result[5]);
			dto.setMcCount((BigInteger) result[6]);
			finalResultDistrict.add(dto);
		}
		log.info("fetch data in  getDistrictLeaseReport  method of LesaeCaseReportServiceImpl  return success...!!  ");
		return finalResultDistrict;
	}

	@Override
	public BigInteger getDistrictWiseLeaseCount() {
		log.info(
				"fetch data in  getDistrictWiseLeaseCount  method of LesaeCaseReportServiceImpl  return success...!!  ");
		return leaseCaseRepo.getDistrictWiseLeaseCount();
	}

	@Override
	public ByteArrayInputStream exportgetDistrictLeaseReport() throws IOException {
		List<LesaeCaseReportDTO> finalResultDistrict = new ArrayList<>();
		List<Object[]> getRecord = leaseCaseRepo.getDistrictWiseLeaseRecordForReport();
		for (Object[] result : getRecord) {
			LesaeCaseReportDTO dto = new LesaeCaseReportDTO();
			dto.setDistrictName((String) result[1]);
			dto.setDistrcitCode((String) result[0]);
			dto.setNoOfLeaseCseCount((BigInteger) result[2]);
			dto.setTotalPlotCount((BigInteger) result[3]);
			dto.setDlscCount((BigInteger) result[4]);
			dto.setTlscCount((BigInteger) result[5]);
			dto.setMcCount((BigInteger) result[6]);
			finalResultDistrict.add(dto);
		}
		log.info(
				"fetch data in  exportgetDistrictLeaseReport  method of LesaeCaseReportServiceImpl  return success...!!  ");
		return ExcelHelper.leaseCasedistrictRecordToExcel(finalResultDistrict);
	}

	@Override
	public List<LesaeCaseReportDTO> getTahasilLeaseReport(String data) {
		List<LesaeCaseReportDTO> finalResultTahasil = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String districtCode = jsonData.getString("districtCode");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = leaseCaseRepo.getTahasilWiseLeaseRecord(pageSize, offset, districtCode);
		for (Object[] result : getRecord) {
			LesaeCaseReportDTO dto = new LesaeCaseReportDTO();
			dto.setTahasilName((String) result[1]);
			dto.setTahasilCode((String) result[0]);
			dto.setNoOfLeaseCseCount((BigInteger) result[2]);
			dto.setTotalPlotCount((BigInteger) result[3]);
			dto.setDlscCount((BigInteger) result[4]);
			dto.setTlscCount((BigInteger) result[5]);
			dto.setMcCount((BigInteger) result[6]);
			finalResultTahasil.add(dto);
		}
		log.info("fetch data in  getTahasilLeaseReport  method of LesaeCaseReportServiceImpl  return success...!!  ");
		return finalResultTahasil;
	}

	@Override
	public BigInteger getTahasilWiseLeaseCount(String data) {
		JSONObject jsonData = new JSONObject(data);
		log.info(
				"fetch data in  getTahasilWiseLeaseCount  method of LesaeCaseReportServiceImpl  return success...!!  ");
		return leaseCaseRepo.getTahasilWiseLeaseCount(jsonData.getString("districtCode"));
	}

	@Override
	public ByteArrayInputStream exportgetTahasilLeaseReport(String districtCode) throws IOException {
		List<LesaeCaseReportDTO> finalResulttahasil = new ArrayList<>();
		List<Object[]> getRecord = leaseCaseRepo.getTahasilWiseLeaseRecordForReport(districtCode);
		for (Object[] result : getRecord) {
			LesaeCaseReportDTO dto = new LesaeCaseReportDTO();
			dto.setTahasilName((String) result[1]);
			dto.setTahasilCode((String) result[0]);
			dto.setNoOfLeaseCseCount((BigInteger) result[2]);
			dto.setTotalPlotCount((BigInteger) result[3]);
			dto.setDlscCount((BigInteger) result[4]);
			dto.setTlscCount((BigInteger) result[5]);
			dto.setMcCount((BigInteger) result[6]);
			finalResulttahasil.add(dto);
		}
		log.info(
				"fetch data in  exportgetTahasilLeaseReport  method of LesaeCaseReportServiceImpl  return success...!!  ");
		return ExcelHelper.leaseCaseTahasilRecordToExcel(finalResulttahasil);
	}

	@Override
	public List<LesaeCaseReportDTO> getVillageLeaseReport(String data) {
		List<LesaeCaseReportDTO> finalResultVillage = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String tahasilCode = jsonData.getString("tahasilCode");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = leaseCaseRepo.getVillageWiseLeaseRecord(pageSize, offset, tahasilCode);
		for (Object[] result : getRecord) {
			LesaeCaseReportDTO dto = new LesaeCaseReportDTO();
			dto.setVillageName((String) result[1]);
			dto.setVillageCode((String) result[0]);
			dto.setNoOfLeaseCseCount((BigInteger) result[2]);
			dto.setTotalPlotCount((BigInteger) result[3]);
			dto.setDlscCount((BigInteger) result[4]);
			dto.setTlscCount((BigInteger) result[5]);
			dto.setMcCount((BigInteger) result[6]);
			finalResultVillage.add(dto);
		}
		log.info("fetch data in  getVillageLeaseReport  method of LesaeCaseReportServiceImpl  return success...!!  ");
		return finalResultVillage;
	}

	@Override
	public BigInteger getVillageWiseLeaseCount(String data) {
		JSONObject jsonData = new JSONObject(data);
		return leaseCaseRepo.getVillageWiseLeaseCount(jsonData.getString("tahasilCode"));
	}

	@Override
	public ByteArrayInputStream exportgetVillageReport(String tahasilCode) throws IOException {
		List<LesaeCaseReportDTO> finalResultvillage = new ArrayList<>();
		List<Object[]> getRecord = leaseCaseRepo.getVillageWiseLeaseRecordForReport(tahasilCode);
		for (Object[] result : getRecord) {
			LesaeCaseReportDTO dto = new LesaeCaseReportDTO();
			dto.setVillageName((String) result[1]);
			dto.setVillageCode((String) result[0]);
			dto.setNoOfLeaseCseCount((BigInteger) result[2]);
			dto.setTotalPlotCount((BigInteger) result[3]);
			dto.setDlscCount((BigInteger) result[4]);
			dto.setTlscCount((BigInteger) result[5]);
			dto.setMcCount((BigInteger) result[6]);
			finalResultvillage.add(dto);
		}
		log.info("fetch data in  exportgetVillageReport  method of LesaeCaseReportServiceImpl  return success...!!  ");
		return ExcelHelper.leaseCaseVillageRecordToExcel(finalResultvillage);
	}

	@Override
	public List<LesaeCaseReportDTO> getKhataLeaseReport(String data) {
		List<LesaeCaseReportDTO> finalResultKhata = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String villageCode = jsonData.getString("villageCode");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = leaseCaseRepo.getKhataWiseLeaseRecord(pageSize, offset, villageCode);
		for (Object[] result : getRecord) {
			LesaeCaseReportDTO dto = new LesaeCaseReportDTO();
			dto.setKhataNo((String) result[1]);
			dto.setKhataCode((String) result[0]);
			dto.setNoOfLeaseCseCount((BigInteger) result[2]);
			dto.setTotalPlotCount((BigInteger) result[3]);
			dto.setDlscCount((BigInteger) result[4]);
			dto.setTlscCount((BigInteger) result[5]);
			dto.setMcCount((BigInteger) result[6]);
			finalResultKhata.add(dto);
		}
		log.info("fetch data in  getKhataLeaseReport  method of LesaeCaseReportServiceImpl  return success...!!  ");
		return finalResultKhata;
	}

	@Override
	public List<LesaeCaseReportDTO> getPlotLeaseReport(String data) {
		List<LesaeCaseReportDTO> finalResultPlot = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String khataCode = jsonData.getString("khataCode");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = leaseCaseRepo.getPlotWiseLeaseRecord(pageSize, offset, khataCode);
		for (Object[] result : getRecord) {
			LesaeCaseReportDTO dto = new LesaeCaseReportDTO();
			dto.setPlotNo((String) result[0]);
			dto.setPlotCode((String) result[1]);
			dto.setFieldInquery((Short) result[2]);
			dto.setDlsc((Date) result[3]);
			dto.setTlsc((Date) result[4]);
			dto.setMc((Date) result[5]);
			dto.setNoticeIssue((Short) result[6]);
			dto.setConsiderMonyDeposite((Short) result[7]);
			dto.setStatus((Short) result[8]);
			dto.setRemerk((String) result[9]);
			finalResultPlot.add(dto);
		}
		log.info("fetch data in  getPlotLeaseReport  method of LesaeCaseReportServiceImpl  return success...!!  ");
		return finalResultPlot;
	}

	@Override
	public BigInteger getKhataWiseLeaseCount(String data) {
		JSONObject jsonData = new JSONObject(data);
		log.info("fetch data in  getKhataWiseLeaseCount  method of LesaeCaseReportServiceImpl  return success...!!  ");
		return leaseCaseRepo.getKhataWiseLeaseCount(jsonData.getString("villageCode"));
	}

	@Override
	public BigInteger getPlotWiseLeaseCount(String data) {
		JSONObject jsonData = new JSONObject(data);
		log.info("fetch data in  getPlotWiseLeaseCount  method of LesaeCaseReportServiceImpl  return success...!!  ");
		return leaseCaseRepo.getPlotWiseLeaseCount(jsonData.getString("khataCode"));
	}

	@Override
	public ByteArrayInputStream exportgetKhataReport(String villageCode) throws IOException {
		List<LesaeCaseReportDTO> finalResultKhata = new ArrayList<>();
		List<Object[]> getRecord = leaseCaseRepo.getKhataWiseLeaseRecordForReport(villageCode);
		for (Object[] result : getRecord) {
			LesaeCaseReportDTO dto = new LesaeCaseReportDTO();
			dto.setKhataNo((String) result[1]);
			dto.setKhataCode((String) result[0]);
			dto.setNoOfLeaseCseCount((BigInteger) result[2]);
			dto.setTotalPlotCount((BigInteger) result[3]);
			dto.setDlscCount((BigInteger) result[4]);
			dto.setTlscCount((BigInteger) result[5]);
			dto.setMcCount((BigInteger) result[6]);
			finalResultKhata.add(dto);
		}
		log.info("fetch data in  exportgetKhataReport  method of LesaeCaseReportServiceImpl  return success...!!  ");
		return ExcelHelper.leaseCaseKhataRecordToExcel(finalResultKhata);
	}

	@Override
	public ByteArrayInputStream exportgetPlotReport(String khataCode) throws IOException {
		List<LesaeCaseReportDTO> finalResultPlot = new ArrayList<>();
		List<Object[]> getRecord = leaseCaseRepo.getPlotWiseLeaseRecordForReport(khataCode);
		for (Object[] result : getRecord) {
			LesaeCaseReportDTO dto = new LesaeCaseReportDTO();
			dto.setPlotNo((String) result[0]);
			dto.setPlotCode((String) result[1]);
			dto.setFieldInquery((Short) result[2]);
			dto.setDlsc((Date) result[3]);
			dto.setTlsc((Date) result[4]);
			dto.setMc((Date) result[5]);
			dto.setNoticeIssue((Short) result[6]);
			dto.setConsiderMonyDeposite((Short) result[7]);
			dto.setStatus((Short) result[8]);
			dto.setRemerk((String) result[9]);
			finalResultPlot.add(dto);
		}
		log.info("fetch data in  exportgetPlotReport  method of LesaeCaseReportServiceImpl  return success...!!  ");
		return ExcelHelper.leaseCasePlotRecordToExcel(finalResultPlot);
	}

}
