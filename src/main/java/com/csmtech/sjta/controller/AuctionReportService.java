/**
 * 
 */
package com.csmtech.sjta.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import com.csmtech.sjta.dto.AuctionReportDTO;

/**
 * 
 */
public interface AuctionReportService {

	public List<AuctionReportDTO> getAuctionReportData(String data);

	public BigInteger getReportCount();

	public List<AuctionReportDTO> getParticipantData(String data);

	public BigInteger getParticipantCount();
	
	public ByteArrayInputStream getAuctionDataForReportExcel() throws IOException ;
	
	public List<AuctionReportDTO> getBidderAllRecordHistory(String data);
	
	public BigInteger getBidderAllRecordHistoryCount(String data);

}
