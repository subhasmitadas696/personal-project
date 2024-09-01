package com.csmtech.sjta.service;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import com.csmtech.sjta.dto.LandInformationReportDTO;

import net.sf.jasperreports.engine.JRException;

public interface LandInformationReportService {

	public List<LandInformationReportDTO> getDistrictReportData(String data);

	public BigInteger getCountForDistrictReportData();

	public List<LandInformationReportDTO> getTahasilReportData(String data);

	public BigInteger getCountForTahasilReportData(String data);

	public List<LandInformationReportDTO> getVillageReportData(String data);

	public BigInteger getCountForVillageReportData(String data);

	public List<LandInformationReportDTO> getKhataReportData(String data);

	public BigInteger getCountForKahtaReportData(String data);

	public List<LandInformationReportDTO> getPlotReportData(String data);

	public BigInteger getCountForPlotReportData(String data);
	
	public ByteArrayInputStream exportReportToDistrict() throws IOException;
	
	public ByteArrayInputStream exportReportToTahasil(String districtCode) throws IOException;
	
	public ByteArrayInputStream exportReportToVillage(String tahasilCode) throws IOException;
	
	public ByteArrayInputStream exportReportToKhata(String villageCode) throws IOException;
	
	public ByteArrayInputStream exportReportToPlot(String khataCode) throws IOException;
	
	public List<LandInformationReportDTO> ReverseRecordForDistrict(String data);

	public List<LandInformationReportDTO> ReverseRecordForTahasil(String data);

	public List<LandInformationReportDTO> ReverseRecordForVillage(String data);
	
	public List<LandInformationReportDTO> ReverseRecordForKhata(String data);


}
