package com.csmtech.sjta.dto;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MeetingProceedingsOneDTO {
	
	@Id
	private BigInteger meetingId;
	private BigInteger meetingScheduleId;
	private String venue;
	private Date meetingDate;
	private String meetingPurpose;
	private Short meetingLevelId;
	private Integer meetingScheduleApplicantId;
	private BigInteger landAppId;
	private String plotNo;
	private String plotIds;
	private Integer approvalStatus;
	private BigInteger bccUser;
	private BigInteger ccUser;
	private String landAppName;
	private String landAppNo;
	private Integer meetingAuctionStatus;
	private String plotCode;
	private Short directMeetingFlag;

}
