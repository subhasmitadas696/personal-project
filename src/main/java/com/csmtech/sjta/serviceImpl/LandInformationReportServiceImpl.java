package com.csmtech.sjta.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.LandInformationReportDTO;
import com.csmtech.sjta.repository.LandInformationReportRepository;
import com.csmtech.sjta.service.LandInformationReportService;
import com.csmtech.sjta.util.ExcelHelper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LandInformationReportServiceImpl implements LandInformationReportService {

	@Autowired
	private LandInformationReportRepository reportNativeRepo;

	@Value("${file.jasperPath}")
	private String jasperValPath;

	@Value("${file.path.imageUrl}")
	private String jasperValPathImage;

	@Override
	public List<LandInformationReportDTO> getDistrictReportData(String data) {
		List<LandInformationReportDTO> finalResultDistrict = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getDistrictReportData(pageSize, offset);
		for (Object[] result : getRecord) {
			LandInformationReportDTO dto = new LandInformationReportDTO();
			dto.setDistrictName((String) result[0]);
			dto.setDistrictCode((String) result[1]);
			dto.setTahasilCount((BigInteger) result[2]);
			dto.setVillageCount((BigInteger) result[3]);
			dto.setKhataCount((BigInteger) result[4]);
			dto.setPlotCount((BigInteger) result[5]);
			dto.setSumAreaAcer((String) result[6]);
			dto.setDistrictExtent((String) result[7]);
			finalResultDistrict.add(dto);
		}
		log.info("LandInformationReportServiceImpl of method getDistrictReportData execution success.....!!");
		return finalResultDistrict;
	}

	@Override
	public BigInteger getCountForDistrictReportData() {
		log.info("LandInformationReportServiceImpl of method getCountForDistrictReportData execution success.....!!");
		return reportNativeRepo.getCountForDistrictReportData();
	}

	@Override
	public List<LandInformationReportDTO> getTahasilReportData(String data) {
		List<LandInformationReportDTO> finalResultTahasil = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getTahasilReportData(pageSize, offset,
				jsonData.getString("districtCode"));
		for (Object[] result : getRecord) {
			LandInformationReportDTO dto = new LandInformationReportDTO();
			dto.setTahasilName((String) result[0]);
			dto.setTahasilCode((String) result[1]);
			dto.setVillageCount((BigInteger) result[2]);
			dto.setKhataCount((BigInteger) result[3]);
			dto.setPlotCount((BigInteger) result[4]);
			dto.setSumAreaAcer((String) result[5]);
			dto.setTahasilExtent((String) result[6]);
			finalResultTahasil.add(dto);
		}
		log.info("LandInformationReportServiceImpl of method getTahasilReportData execution success.....!!");
		return finalResultTahasil;
	}

	@Override
	public BigInteger getCountForTahasilReportData(String data) {
		JSONObject jsonData = new JSONObject(data);
		log.info("LandInformationReportServiceImpl of method getCountForTahasilReportData execution success.....!!");
		return reportNativeRepo.getCountForTahasilReportData(jsonData.getString("districtCode"));
	}

	@Override
	public List<LandInformationReportDTO> getVillageReportData(String data) {
		List<LandInformationReportDTO> finalResultVillage = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getVillageReportData(pageSize, offset,
				jsonData.getString("thasilCode"));
		for (Object[] result : getRecord) {
			LandInformationReportDTO dto = new LandInformationReportDTO();
			dto.setVillageName((String) result[0]);
			dto.setVillageCode((String) result[1]);
			dto.setKhataCount((BigInteger) result[2]);
			dto.setPlotCount((BigInteger) result[3]);
			dto.setSumAreaAcer((String) result[4]);
			dto.setVillageExtent((String) result[5]);
			finalResultVillage.add(dto);
		}
		log.info("LandInformationReportServiceImpl of method getVillageReportData execution success.....!!");
		return finalResultVillage;
	}

	@Override
	public BigInteger getCountForVillageReportData(String data) {
		JSONObject jsonData = new JSONObject(data);
		log.info("LandInformationReportServiceImpl of method getCountForVillageReportData execution success.....!!");
		return reportNativeRepo.getCountForVillageReportData(jsonData.getString("thasilCode"));
	}

	@Override
	public List<LandInformationReportDTO> getKhataReportData(String data) {
		List<LandInformationReportDTO> finalResultKhata = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getKhataReportData(pageSize, offset,
				jsonData.getString("villageCode"));
		for (Object[] result : getRecord) {
			LandInformationReportDTO dto = new LandInformationReportDTO();
			dto.setKhataNo((String) result[0]);
			dto.setKhataCode((String) result[1]);
			dto.setPlotCount((BigInteger) result[2]);
			dto.setSumAreaAcer((String) result[3]);
			dto.setOwnerName((String) result[4]);
			dto.setMarfatdarName((String) result[5]);
			dto.setSotwar((String) result[6]);
			dto.setPublicationDate((Date) result[7]);
			dto.setDistrictCode((String) result[8]);
			dto.setTahasilCode((String) result[9]);
			dto.setVillageCode((String) result[10]);
			dto.setKhataExtent((String) result[11]);
			finalResultKhata.add(dto);
		}
		log.info("LandInformationReportServiceImpl of method getKhataReportData execution success.....!!");
		return finalResultKhata;
	}

	@Override
	public BigInteger getCountForKahtaReportData(String data) {
		JSONObject jsonData = new JSONObject(data);
		return reportNativeRepo.getCountForKahtaReportData(jsonData.getString("villageCode"));
	}

	@Override
	public List<LandInformationReportDTO> getPlotReportData(String data) {
		List<LandInformationReportDTO> finalResultPlot = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getPlotReportData(pageSize, offset,
				jsonData.getString("khataCode"));
		for (Object[] result : getRecord) {
			LandInformationReportDTO dto = new LandInformationReportDTO();
			dto.setPlotNo((String) result[0]);
			dto.setPlotCode((String) result[1]);
			dto.setKissam((String) result[2]);
			dto.setAreaAcer((String) result[3]);
			dto.setDistrictCode((String) result[4]);
			dto.setTahasilCode((String) result[5]);
			dto.setVillageCode((String) result[6]);
			dto.setKhataCode((String) result[7]);
			dto.setKhataNo((String) result[8]);
			dto.setPlotExtent((String) result[9]);
			finalResultPlot.add(dto);
		}
		log.info("LandInformationReportServiceImpl of method getPlotReportData execution success.....!!");
		return finalResultPlot;
	}

	@Override
	public BigInteger getCountForPlotReportData(String data) {
		JSONObject jsonData = new JSONObject(data);
		log.info("LandInformationReportServiceImpl of method getCountForPlotReportData execution success.....!!");
		return reportNativeRepo.getCountForPlotReportData(jsonData.getString("khataCode"));
	}

	@Override
	public ByteArrayInputStream exportReportToDistrict() throws IOException {
		List<LandInformationReportDTO> finalResultDistrict = new ArrayList<>();
		List<Object[]> getRecord = reportNativeRepo.getDistrictReportDataForReport();
		for (Object[] result : getRecord) {
			LandInformationReportDTO dto = new LandInformationReportDTO();
			dto.setDistrictName((String) result[0]);
			dto.setDistrictCode((String) result[1]);
			dto.setTahasilCount((BigInteger) result[2]);
			dto.setVillageCount((BigInteger) result[3]);
			dto.setKhataCount((BigInteger) result[4]);
			dto.setPlotCount((BigInteger) result[5]);
			dto.setSumAreaAcer((String) result[6]);
			finalResultDistrict.add(dto);
		}
		log.info("LandInformationReportServiceImpl of method exportReportToDistrict execution success.....!!");
		return ExcelHelper.districtRecordToExcel(finalResultDistrict);
	}

	@Override
	public ByteArrayInputStream exportReportToTahasil(String districtCode) throws IOException {
		List<LandInformationReportDTO> finalResultTahasil = new ArrayList<>();
		List<Object[]> getRecord = reportNativeRepo.getTahasilReportDataForReport(districtCode);
		for (Object[] result : getRecord) {
			LandInformationReportDTO dto = new LandInformationReportDTO();
			dto.setTahasilName((String) result[0]);
			dto.setTahasilCode((String) result[1]);
			dto.setVillageCount((BigInteger) result[2]);
			dto.setKhataCount((BigInteger) result[3]);
			dto.setPlotCount((BigInteger) result[4]);
			dto.setSumAreaAcer((String) result[5]);
			finalResultTahasil.add(dto);
		}
		log.info("LandInformationReportServiceImpl of method exportReportToTahasil execution success.....!!");
		return ExcelHelper.TahasilRecordToExcel(finalResultTahasil);
	}

	@Override
	public ByteArrayInputStream exportReportToVillage(String tahasilCode) throws IOException {
		List<LandInformationReportDTO> finalResultVillage = new ArrayList<>();
		List<Object[]> getRecord = reportNativeRepo.getVillageReportDataForReport(tahasilCode);
		for (Object[] result : getRecord) {
			LandInformationReportDTO dto = new LandInformationReportDTO();
			dto.setVillageName((String) result[0]);
			dto.setVillageCode((String) result[1]);
			dto.setKhataCount((BigInteger) result[2]);
			dto.setPlotCount((BigInteger) result[3]);
			dto.setSumAreaAcer((String) result[4]);
			finalResultVillage.add(dto);
		}
		log.info("LandInformationReportServiceImpl of method exportReportToVillage execution success.....!!");
		return ExcelHelper.VillageRecordToExcel(finalResultVillage);
	}

	@Override
	public ByteArrayInputStream exportReportToKhata(String villageCode) throws IOException {
		List<LandInformationReportDTO> finalResultKhata = new ArrayList<>();
		List<Object[]> getRecord = reportNativeRepo.getKhataReportDataForReport(villageCode);
		for (Object[] result : getRecord) {
			LandInformationReportDTO dto = new LandInformationReportDTO();
			dto.setKhataNo((String) result[0]);
			dto.setKhataCode((String) result[1]);
			dto.setPlotCount((BigInteger) result[2]);
			dto.setSumAreaAcer((String) result[3]);
			dto.setOwnerName((String) result[4]);
			dto.setMarfatdarName((String) result[5]);
			dto.setSotwar((String) result[6]);
			dto.setPublicationDate((Date) result[7]);
			finalResultKhata.add(dto);
		}
		log.info("LandInformationReportServiceImpl of method exportReportToKhata execution success.....!!");
		return ExcelHelper.khataRecordToExcel(finalResultKhata);
	}

	@Override
	public ByteArrayInputStream exportReportToPlot(String khataCode) throws IOException {
		List<LandInformationReportDTO> finalResultPlot = new ArrayList<>();
		List<Object[]> getRecord = reportNativeRepo.getPlotReportDataForReport(khataCode);
		for (Object[] result : getRecord) {
			LandInformationReportDTO dto = new LandInformationReportDTO();
			dto.setPlotNo((String) result[0]);
			dto.setPlotCode((String) result[1]);
			dto.setKissam((String) result[2]);
			dto.setAreaAcer((String) result[3]);
			finalResultPlot.add(dto);
		}
		log.info("LandInformationReportServiceImpl of method exportReportToPlot execution success.....!!");
		return ExcelHelper.plotRecordToExcel(finalResultPlot);
	}

	@Override
	public List<LandInformationReportDTO> ReverseRecordForDistrict(String data) {
		 JSONObject jsonData = new JSONObject(data);
		    List<LandInformationReportDTO> finalResultDist = new ArrayList<>();
		    String districtName = reportNativeRepo.ReverseRecordForDistrict(jsonData.getString("districtCode"));
		    if (districtName != null) {
		        LandInformationReportDTO dto = new LandInformationReportDTO();
		        dto.setDistrictName( districtName);
		        finalResultDist.add(dto);
		    }
		    log.info("LandInformationReportServiceImpl of method ReverseRecordForDistrict execution success.....!!");
		    return finalResultDist;
	}

	@Override
	public List<LandInformationReportDTO> ReverseRecordForTahasil(String data) {
		JSONObject jsonData = new JSONObject(data);
		List<LandInformationReportDTO> finalResultTahsil = new ArrayList<>();
		List<Object[]> getRecord = reportNativeRepo.ReverseRecordForTahasil(jsonData.getString("thasilCode"));
		for (Object[] result : getRecord) {
			LandInformationReportDTO dto = new LandInformationReportDTO();
			dto.setDistrictName((String) result[0]);
			dto.setTahasilName((String) result[1]);
			finalResultTahsil.add(dto);
		}
		log.info("LandInformationReportServiceImpl of method ReverseRecordForTahasil execution success.....!!");
		return finalResultTahsil;
	}

	@Override
	public List<LandInformationReportDTO> ReverseRecordForVillage(String data) {
		JSONObject jsonData = new JSONObject(data);
		List<LandInformationReportDTO> finalResultVillage = new ArrayList<>();
		List<Object[]> getRecord = reportNativeRepo.ReverseRecordForVillage(jsonData.getString("villageCode"));
		for (Object[] result : getRecord) {
			LandInformationReportDTO dto = new LandInformationReportDTO();
			dto.setDistrictName((String) result[0]);
			dto.setTahasilName((String) result[1]);
			dto.setVillageName((String) result[2]);
			finalResultVillage.add(dto);
		}
		log.info("LandInformationReportServiceImpl of method ReverseRecordForVillage execution success.....!!");
		return finalResultVillage;
	}

	@Override
	public List<LandInformationReportDTO> ReverseRecordForKhata(String data) {
		JSONObject jsonData = new JSONObject(data);
		List<LandInformationReportDTO> finalResultKhata = new ArrayList<>();
		List<Object[]> getRecord = reportNativeRepo.ReverseRecordForKhata(jsonData.getString("khataCode"));
		for (Object[] result : getRecord) {
			LandInformationReportDTO dto = new LandInformationReportDTO();
			dto.setDistrictName((String) result[0]);
			dto.setTahasilName((String) result[1]);
			dto.setVillageName((String) result[2]);
			dto.setKhataNo((String) result[3]);
			finalResultKhata.add(dto);
		}
		log.info("LandInformationReportServiceImpl of method ReverseRecordForKhata execution success.....!!");
		return finalResultKhata;
	}

}
