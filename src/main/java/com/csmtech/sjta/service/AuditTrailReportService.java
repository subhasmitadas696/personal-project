package com.csmtech.sjta.service;

import java.math.BigInteger;
import java.util.List;

import com.csmtech.sjta.dto.AuditTrailReportDTO;

public interface AuditTrailReportService {
	
	public List<AuditTrailReportDTO> getAuditTrailReport(String data);
	
	public BigInteger getAuditTrailReportCount(String data);

}
