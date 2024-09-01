package com.csmtech.sjta.entity;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.csmtech.sjta.dto.MeetingRemoveApplicantDto;

import lombok.Data;

@Data
@Table(name = "meeting_schedule", schema = "application")
@Entity
public class MeetingSchedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "meeting_schedule_id")
	private Integer intId;

	@Column(name = "venue")
	private String venue;

	@Column(name = "meeting_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private java.util.Date txtMeetingDate53;
	@Column(name = "meeting_level_id")
	private Short selMeetingLevel55;
	@Transient
	private String selMeetingLevel55Val;
	@Column(name = "meeting_purpose")
	private String txtrMeetingPurpose54;

	@Column(name = "created_by")
	private Integer intCreatedBy;

	@Column(name = "upload_mom")
	private String fileUploadDocument;

	@Column(name = "updated_by")
	private Integer intUpdatedBy;

	@Column(name = "created_on")
	@CreationTimestamp
	private Date dtmCreatedOn;

	@Column(name = "updated_on")
	@UpdateTimestamp
	private Date stmUpdatedOn;

	@Column(name = "meeting_no")
	private String meetingNo;

	@Column(name = "status")
	private Boolean bitDeletedFlag;

	@Transient
	private Integer landApplicationId;

	@Transient
	private Integer applicantCreatedBy;

	@Transient
	private Integer ccCreatedBy;

	@Transient
	private Integer bccCreatedBy;

	@Transient
	private Integer userId;

	@Transient
	private Integer[] selectedLandApplicationId;

	@Transient
	private Integer[] selectedCCOfficers;

	@Transient
	private Integer[] selectedBCCOfficers;

	@Transient
	private List<Map<String, String>> applicantData;
	
	@Column(name = "go_for_auction")
	private Short auctionStatus;
	
	@Transient
	private String[] plotsNos;
	
	@Transient
	BigInteger  meetingIdRe;
	
	@Transient
	BigInteger meetingSheduleIdRe;
	
	@Transient
    Short meetingLevleIdRe;
	
	@Transient
	private Integer changeTabFlag;
	
	@Transient
	private Integer auctionFlag;
	
	@Transient
	private Integer auctionEditFlag;
	
	@Transient
	private BigInteger[] applicantRemovedIds; 
	
	@Transient
	private List<MeetingRemoveApplicantDto> removedApplicants;
	
	@Transient
	private List<MeetingRemoveApplicantDto> landApplicantAndPlots;
	
	@Transient
	private BigInteger auctionPlotId;

}