package com.csmtech.sjta.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingScheduleDTO {

	private BigInteger meetingScheduleId;

	private Date meetingDate;

	private String meetingPurpose;

	private Short meetingLevelId;

	private BigInteger createdBy;

	private Date createdOn;

	private BigInteger updatedBy;

	private Date updatedOn;

	private Boolean status;

	private String meetingNo;

	private String meetingLevel;

	private String venue;

	private String uploadMom;

	private List<Map<String, String>> applicantData;

	private String landApplicantIds;

	private String officerCCIds;

	private String officerBCCIds;
	
	private Short pdfStatus;
	
	private List<LandApplicantMeetingDTO> landApplicantResult;
	
	private Short meetingLevleId;
	
	private BigInteger meetingId;
	
	private String afterMeetingFlag;
	
	private BigInteger bidderFromApplicationId;
	
	private Short flagForNoMeeting;
	
	private Short directMeetingFlag;

}
