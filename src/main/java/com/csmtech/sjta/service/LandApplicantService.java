package com.csmtech.sjta.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.csmtech.sjta.dto.ApplicantNumberAndMobileDTO;
import com.csmtech.sjta.dto.LandAppHistoryDTO;
import com.csmtech.sjta.dto.LandAppResponseStructureDTO;
import com.csmtech.sjta.dto.LandPaginationDTO;
import com.csmtech.sjta.dto.MeetingPlotsRecordDTO;

/*
 * @Auth  GuruPrasad
 */

public interface LandApplicantService {

	LandPaginationDTO getSearchLandApplicantDetailsPage(BigInteger roleId, String districtCode, Integer pageNumber,
			Integer pageSize, String tahasilCode, String villageCode, String pageType, String khatianCode);

	LandAppResponseStructureDTO findAllDetailsBylandApplicantId(BigInteger landApplicantId);

	public Integer updateApplicantName(BigInteger applicantId, String orderId, String paymentSignature,
			String paymentId, Integer userId,BigDecimal amount, Integer userRoleId);

	public List<ApplicantNumberAndMobileDTO> fetchApplicantInfoById(BigInteger i);

	LandPaginationDTO getLandApplicantDetailsPage(String plotCode,Short mettingLevleId,Integer auctionFlag);

	LandAppHistoryDTO viewApplicationHistory(BigInteger landApplicantId);

	public LandPaginationDTO getlandForAfterMeetings(Short mettingLevleId, BigInteger meetingId,BigInteger meetingSheduleId,Integer auctionFlag);

	LandAppHistoryDTO viewCitizenApplicationHistory(BigInteger landApplicantId);

	LandAppHistoryDTO getApplicationHistoryReport(Integer pageSize, Integer pageNumber);

	Integer countOfAppHistoryReport();

}
