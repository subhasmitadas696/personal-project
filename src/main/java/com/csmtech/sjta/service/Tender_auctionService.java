package com.csmtech.sjta.service;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.List;
import org.json.JSONObject;
import com.csmtech.sjta.dto.AuctionPriviewDTO;

import net.sf.jasperreports.engine.JRException;

public interface Tender_auctionService {
	
	JSONObject save(String tender_auction);

	List<AuctionPriviewDTO> getById(BigInteger id);

	JSONObject getAll(String formParams);

	public JSONObject deleteById(BigInteger id,BigInteger auctionPlotDetailsId);
	
	public JSONObject getPriviewTenderRecord(String formParams);
	
	public Integer getPublicApproval(String tenderId,BigInteger createdBy);
	
	public Integer getUnPublicApproval(BigInteger tenderId);
	
	
}