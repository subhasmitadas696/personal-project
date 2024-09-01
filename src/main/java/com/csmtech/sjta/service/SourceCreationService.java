package com.csmtech.sjta.service;

import java.math.BigInteger;
import java.util.List;

import org.json.JSONObject;

import com.csmtech.sjta.dto.AuctionPlotMainDTO;
import com.csmtech.sjta.dto.SourceCreationPlotsRecord;
import com.csmtech.sjta.dto.AuctionPlotDTO;
import com.csmtech.sjta.dto.AuctionPlotIdDetrailsMainDTO;
import com.csmtech.sjta.dto.SourcerCreationDto;

public interface SourceCreationService {

	Integer save(SourcerCreationDto sourcerCreationDto);

	// Rashmi Changes
	public List<AuctionPlotDTO> auctionPlotDetrails(String formParms);

	public AuctionPlotIdDetrailsMainDTO auctionPlotIdDetrails(BigInteger auctionPlotId);
	
	public Integer softDeleteAuctionDetails(BigInteger initId);
	
	public Integer updateAddAuctionDocument(BigInteger auctionPlotId, String auctionDocumen);
	
	public List<AuctionPlotDTO> auctionPlotDetrailsSelectAuctionFlag(String formParms);
	
	public Integer updateDeputyOfficerAction(Integer auctionPlotId,String status,String remark);
	
	public BigInteger countAuctionPlotsAuctionFlagValidate(String plotCode);
	
	public List<SourceCreationPlotsRecord> getSelectedPlotsInfo(String formsParm);
	
	public BigInteger getSelectedPlotsInfoCount();
	
	public BigInteger getCountAuctionRecord();
	
	public BigInteger countForAuctionApprovalRecord(String parms);
	
	public BigInteger getCountAuctionRecordUseLike(String formParms);
	
	public JSONObject saveSourceSecondProcess(String data);

}
