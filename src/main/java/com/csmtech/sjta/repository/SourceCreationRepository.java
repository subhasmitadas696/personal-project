package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.csmtech.sjta.dto.SourceCreationPlotsRecord;
import com.csmtech.sjta.dto.SourcerCreationDto;

public interface SourceCreationRepository {

	public Integer insertAuctionPlotDetails(SourcerCreationDto sourcerCreationDto, BigInteger auctionPlotId);

	public BigInteger insertAuctionPlot(SourcerCreationDto dto);

	public Integer auctionUpdate(SourcerCreationDto sourcerCreationDto);

	public Integer deleteAuctionPlotDetails(BigInteger dto);

	public List<Object[]> auctionPlotDetrails(Integer limit, Integer offset);

	public BigInteger countAuctionPlots();

	public List<Object[]> auctionPlotIdDetrails(BigInteger auctionPlotId);

	public List<Object[]> auctionPlotDetrailsThroughId(BigInteger initId);

	public Integer markAuctionPlotAsDeleted(BigInteger auctionPlotId);

	public Integer updateAddAuctionPlotDetails(BigInteger auctionPlotId, String auctionDocument);

	public List<Object[]> auctionPlotDetrailsViewByAuctionFlag(Integer limit, Integer offset, String approvalStatus);

	public BigInteger countAuctionPlotsAuctionFlag(String approvalStatus);

	public Integer updateAuctionPlot(Integer auctionPlotId, String status, String remark);

	public BigInteger countAuctionPlotsAuctionFlagValidate(String plotCode);

	public List<SourceCreationPlotsRecord> getSelectedPlotsInfo(Integer limit, Integer offset);

	public BigInteger getSelectedPlotsInfoCount();

	public List<Object[]> auctionPlotDetrailsUseLike(Integer limit, Integer offset, String plotNo, String auctionFlag);

	public BigInteger countAuctionPlotsLikeCont(String plotNo, String auctionFlag);

	public BigInteger createMeeting(BigInteger createdBy);

	public BigInteger createMeetingSchedule(String venue, Date meetingDate, String meetingPurpose, Short meetingLevelId,
			BigInteger createdBy, BigInteger meetingId);

	public Integer createMeetingScheduleApplicant(BigInteger meetingScheduleId, BigInteger createdBy, String plotNo);

	public BigInteger createAuctionPlot(String districtCode, String tahasilCode, String villageCode, String khatianCode,
			BigInteger createdBy);
	
	public BigInteger createAuctionPlotDetails(BigInteger auctionPlotId, String plotCode, BigInteger createdBy,BigInteger meetingSheduleId);
	
	public Integer createMeetingScheduleMailBcc(BigInteger meetingScheduleId, Integer userId, BigInteger createdBy);
	
	public Integer createMeetingScheduleMailCc(BigInteger meetingScheduleId, Integer userId, BigInteger createdBy);
	
	public Integer updateDeletedFladForTheMeetings(BigInteger meetingId);
	
	public BigInteger gerAuctionThroughMeetingId(BigInteger auctionId);

}
