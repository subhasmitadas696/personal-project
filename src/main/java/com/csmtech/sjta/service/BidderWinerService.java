package com.csmtech.sjta.service;

import java.math.BigInteger;
import java.util.List;
import com.csmtech.sjta.dto.ShowWinerDTO;
import com.csmtech.sjta.dto.WinnerFinalResponesDTO;

public interface BidderWinerService {
	
	public List<ShowWinerDTO> getCustomResults(Integer pageNO,Integer pageSize);
	
	public BigInteger getCountOfLiveAuctions();
	
	public WinnerFinalResponesDTO getWinerFinalRecordData(BigInteger tenderId);
	
	public Integer updateWinnerDocument(BigInteger liveAuctionId, String documentName,String data);

}
