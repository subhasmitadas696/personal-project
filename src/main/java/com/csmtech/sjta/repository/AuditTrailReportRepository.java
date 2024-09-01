package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.List;

public interface AuditTrailReportRepository {

	public List<Object[]> getAuditTrailReport(Integer pagesize, Integer offset, String formDate, String todate,
			Integer reportType, String userName, String plotNo, String applicationNo, Integer applicationType,
			String userrole);
	
	public BigInteger getAuditTrailReportCount(Integer pagesize, Integer offset, String formDate, String todate,
			Integer reportType, String userName, String plotNo, String applicationNo, Integer applicationType,
			String userrole);

}
