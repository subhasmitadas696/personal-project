package com.csmtech.sjta.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import com.csmtech.sjta.dto.LandAllertmentSlcCompliteDTO;
import com.csmtech.sjta.dto.LandAllotementResponesDTO;
import com.csmtech.sjta.dto.LandAllotementResponesNewDTO;
import com.csmtech.sjta.dto.LandAllotementWinnerResultDto;
import com.csmtech.sjta.entity.LandAllotementForAuctionEntity;

public interface LandAllertmentSlcCompliteSrvive {
	
	public List<LandAllertmentSlcCompliteDTO> getMettingCompliteSlcRecord(String formsParms);
	
	public BigInteger countMeetingSchedulesByMeetingLevelId();
	
	public List<Map<String, Object>>  getDistinctPlotNumbers(String formsParm);
	
	public List<LandAllertmentSlcCompliteDTO> executeCustomQueryGetLandAllertmentAlRequiredDetails(String formsParm);
	
	public JSONObject landAllortmrntSaveRecord(String data);
	
	public List<LandAllotementResponesNewDTO> getLandAllotmentDetails(String formdParm);
	
	public BigInteger countLandAlertUser();
	
	public Integer updateLandAllotementFlag(String landAlloId);
	
	public JSONObject insertRecordForAuction(String data);
	
	public List<LandAllotementForAuctionEntity> gteRecordGoForAuction(String formdParm);
	
	public BigInteger getGoForAuctionCount();
	
	public Integer updateGoForAucton(String landAlloId);
	
	 public Integer updateLandAllotementFrom16Record(String formsParms);
	 
	 public List<LandAllotementWinnerResultDto> getLandAllotmentWnnerDetails(String formsParms);
	 
	 public BigDecimal getLandAllotmentWnnerDetailsCount(String formsParms);

}
