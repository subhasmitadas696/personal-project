package com.csmtech.sjta.repository;

import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface BidderRegistratationRepository {
	
	public List<Object[]> getBidderFormMApplicationById(BigInteger bidderFormMApplicationId);
	
	public List<Object[]> getAuctionPlotData(Integer pageNumber, Integer pageSize,BigInteger userId);
	
	public BigInteger getTotalApplicantCount(BigInteger userId);
	
	public BigInteger getCountDuplicateApplication(BigInteger tenderAuctionId,BigInteger createdBy);
	
	public Boolean getCountDateAndTimeValidation(BigInteger auctionTenderId);
	
	public List<Object[]> getAmountRecord(BigInteger tenderAuctionId,String plotCode);
	
	public String updateBidderFormMApplication(Integer appplicationId,String uniqueNumber);
	
	public Map<String, Object> getTenderAuctionDates(BigInteger appId);
	
	public List<Object[]> getExpiredBidderFormApplications(Pageable pageSize);
	
	public List<Object[]> getTenderNames(Pageable pageRequest);
	
	public BigInteger getTenderCount();
	
	public List<Object[]> getBidderFormApplicationData(BigInteger tenderAuctionId,Pageable pageRequest);
	
	public BigInteger getTenderCountApplicant(BigInteger tenderAuctionId);
	
	public Integer updateBidderFormApplication(BigInteger bidderFormApplicationId,String remerk,String status);
	
	public Integer markBidderFormMApplicationAsDeleted(BigInteger bidderFormMApplicationId);
	
	public List<Object[]> getAuctions(String data) throws JsonMappingException, JsonProcessingException;
	
	public Boolean checkAuctionStatusValid(BigInteger tenderAuctionId);
	
	public List<Object[]> getAuctionDetails(BigInteger tenderAuctionId);
	
	public List<Object[]> getBidderFormApplicationData(Integer limit,Integer offset, String approvalStatus);
	
	public Integer insertPaymentTransaction(BigInteger landApplicantId, String orderId, String paymentSignature,
			String paymentId, String paymentStatus,BigDecimal tranctionAmount,String reciptNo);
	
	public BigInteger getCountForTenderAuction(BigInteger tenderAuctionId);
	
	public BigInteger getAuctionsCount(String data) throws JsonMappingException, JsonProcessingException;

}
	