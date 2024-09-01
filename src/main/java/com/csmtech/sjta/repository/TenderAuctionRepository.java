package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.List;

public interface TenderAuctionRepository {

	public List<Object[]> getAuctions(Integer pageNumber,Integer pageSize);
	
	public BigInteger getActiveAuctionCount();
	
	public List<Object[]> getAuctionsGetId(BigInteger tenderId);
	
	public Integer getPublicApproval(BigInteger tenderId);
	
	public Integer insertLiveAuctionData(BigInteger tenderId,BigInteger createdBy);
	
	 public String updateTenderAuctionAuctionNumber(BigInteger tenderAuctionId, String newAuctionNumber);
	 
	 public BigInteger getAuctionCountTender(BigInteger tenderId);
	 
	 public Integer getUnPublicApproval(BigInteger tenderId);
	 
	 public Integer UnPublishRemoveRecord(BigInteger tenderId);
	 
	 public List<Object[]> getAuctionsUseLike(Integer pageNumber, Integer pageSize,String value,String status);
	 
	 public BigInteger getActiveAuctionForLikeCount(String value,String status);
	 
	 public Integer updatePlotIdThroughTenderFlag(BigInteger auctonId,String plotCode);
	 
	 public Integer updatePlotIdThroughTenderFlagDelete(BigInteger auctonId,BigInteger auctionPlotDetailsId);
	 
	 public BigInteger getAuctionId(BigInteger tenderId);
	 
	 public BigInteger getAuctionCountTenderForPublishOrNot(BigInteger tenderId);

}
