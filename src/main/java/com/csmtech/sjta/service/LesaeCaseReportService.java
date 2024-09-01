package com.csmtech.sjta.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import com.csmtech.sjta.dto.LesaeCaseReportDTO;

public interface LesaeCaseReportService {
	
	public List<LesaeCaseReportDTO> getDistrictLeaseReport(String data);
	
	public BigInteger getDistrictWiseLeaseCount();
	
	public ByteArrayInputStream exportgetDistrictLeaseReport() throws IOException;
	
	public List<LesaeCaseReportDTO> getTahasilLeaseReport(String data);
	
	public BigInteger getTahasilWiseLeaseCount(String data);
	
	public ByteArrayInputStream exportgetTahasilLeaseReport(String data) throws IOException ;
	
	public List<LesaeCaseReportDTO> getVillageLeaseReport(String data);
	
	public BigInteger getVillageWiseLeaseCount(String data);
	
	public ByteArrayInputStream exportgetVillageReport(String tahasilCode) throws IOException;
	
	public List<LesaeCaseReportDTO> getKhataLeaseReport(String data);
	
	public List<LesaeCaseReportDTO> getPlotLeaseReport(String data);
	
	public BigInteger getKhataWiseLeaseCount(String data);
	
	public BigInteger getPlotWiseLeaseCount(String data);
	
	public ByteArrayInputStream exportgetKhataReport(String villageCode) throws IOException ;
	
	public ByteArrayInputStream exportgetPlotReport(String khataCode) throws IOException ;
	
}
