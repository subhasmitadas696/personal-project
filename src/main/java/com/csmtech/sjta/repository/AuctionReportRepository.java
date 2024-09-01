package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.List;

public interface AuctionReportRepository {

	public List<Object[]> getReportData(Integer pageSize, Integer offset);

	public BigInteger getReportCount();

	public List<Object[]> participantData(BigInteger tenderAuctionId);
	
	public List<Object[]> getAuctionDataForReportExcel();
	
	public List<Object[]> getBidderAllRecordHistory(BigInteger tenderAuctionId);
	
	public BigInteger getBidderAllRecordHistoryCount(BigInteger tenderAuctionId);

	
}
