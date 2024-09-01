package com.csmtech.sjta.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import com.csmtech.sjta.dto.LandAllotementResponesDTO;
import com.csmtech.sjta.dto.LandAllotementResponesNewDTO;
import com.csmtech.sjta.dto.LandAllotementWinnerResultDto;
import com.csmtech.sjta.entity.LandAllotementEntity;

public interface LandAllertmentSlcCompliteRepository {

	public List<Object[]> getMeetingSchedulesByMeetingLevelId(Integer limit, Integer offset);

	public BigInteger countMeetingSchedulesByMeetingLevelId();

	public List<Map<String, Object>> getDistinctPlotNumbers(BigInteger getDistinctPlotNumbers,Integer flagStatus);

	public List<Object[]> executeCustomQueryGetLandAllertmentAlRequiredDetails(String plotNo,
			BigInteger meetingScheduleId,Integer flagStatus);

	public List<Object[]> executeCustomQueryGetLandAllertmentAlRequiredDetailsNativeQuery(
			BigInteger meetingScheduleIdParam);

	public List<LandAllotementResponesNewDTO> getLandAllotmentDetails(Integer limit, Integer offset);

	public BigInteger countLandAllortUser();

	public Integer updateLandAllotementFlag(BigInteger landAlloId);

	public BigInteger goForAuctionCount();

	public Integer updateGoForAyction(BigInteger landAlloId);

	public Integer updateLandAllotementFrom16Record(BigInteger landAllotementId, String form16Docs,
			String form16Remark);

	public List<LandAllotementWinnerResultDto> getLandAllotmentWnnerDetails(BigInteger createdBy,Integer limit,Integer offset);

	public BigDecimal getLandAllotmentWnnerDetailsCount(BigInteger createdBy);
	
	public Integer updateformFlag(BigInteger id,BigInteger meetingId,String plotCode);

	public BigInteger insertLandAllotment(LandAllotementEntity landAllotment);
	

}
