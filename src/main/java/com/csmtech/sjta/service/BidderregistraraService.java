package com.csmtech.sjta.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import com.csmtech.sjta.dto.AuctionDTO;
import com.csmtech.sjta.dto.AuctionDetails;
import com.csmtech.sjta.dto.BidderRegistratatonViewMoreDTO;
import com.csmtech.sjta.dto.FromMApplicationViewTenderWise;
import com.csmtech.sjta.dto.TenderViewOfficerDTO;
import com.csmtech.sjta.dto.VIewCitizenAuctionPlotDetailsDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface BidderregistraraService {

	JSONObject save(String bidderregistrara);

	JSONObject getById(BigInteger Id);

	JSONObject getAll(String formParams);

	JSONObject deleteById(BigInteger id);

	public List<BidderRegistratatonViewMoreDTO> getByIdNative(BigInteger id);

	public List<VIewCitizenAuctionPlotDetailsDTO> getAuctionPlotData(Integer pageNumber, Integer pageSize,BigInteger userId);

	public BigInteger getTotalApplicantCount(BigInteger userId);

	public String updateBidderFormMApplication(Integer appplicationId);

	public Map<String, Object> getTenderAuctionDates(BigInteger appId);

	public List<TenderViewOfficerDTO> getTenderOfficerRecord(String formParams);

	public BigInteger getTenderCount();

	public List<FromMApplicationViewTenderWise> getBidderFormApplicationData(String formParams);
	
	public BigInteger getTenderCountApplicant(BigInteger tenderAuctionId);
	
	public Integer updateBidderFormApplication(String formParams);
	
	public List<AuctionDTO> getAuctions(String data) throws JsonMappingException, JsonProcessingException;
	
	public List<AuctionDetails> getAuctionDetails(BigInteger tenderId);

	public Boolean checkAuctionStatusValid(BigInteger tenderAuctionId);
	
	public JSONObject getAllDataFromMEvalucation(String formParams);
	
	public Integer tranctionCount(BigInteger applicantId, String orderId, String paymentSignature, String paymentId,BigDecimal tranctionAmount);
	
	public BigInteger getAuctionsCount(String data)throws JsonMappingException, JsonProcessingException ;
}