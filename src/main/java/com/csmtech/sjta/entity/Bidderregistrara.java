package com.csmtech.sjta.entity;

import java.math.BigInteger;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Table(name = "bidder_form_m_application", schema = "application")
@Entity
public class Bidderregistrara {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bidder_form_m_application_id")
	private BigInteger intId;

	@Column(name = "created_by")
	private BigInteger intCreatedBy;

	@Column(name = "updated_by")
	private Integer intUpdatedBy;

	@Column(name = "created_on")
	@CreationTimestamp
	private Date dtmCreatedOn;

	@Column(name = "updated_on")
	@UpdateTimestamp
	private Date stmUpdatedOn;

	@Column(name = "deleted_flag", insertable = false, updatable = false)
	private Boolean bitDeletedFlag;

	@Column(name = "`user_id`")
	private BigInteger txtContactPersonName;

	@Column(name = "`pan_number`")
	private String txtPanNumber;

	@Column(name = "`pan_card_document`")
	private String fileUploadPANCard;

	@Column(name = "`aadhar_number`")
	private String txtAadharNumber;

	@Column(name = "`uploaded_aadhar_card`")
	private String fileUploadedAadharCard;

	@Column(name = "`uploded_resent_signature`")
	private String fileUploadedResentSignatureOfTheBidder;

	@Column(name = "`uploded_resent_photo_bidder`")
	private String fileUploadedResentPhotoOfTheBidder;

	@Column(name = "`curr_state`")
	private String selState;

	@Transient
	private String selStateVal;

	@Column(name = "`curr_district`")
	private String selDistrict;

	@Column(name = "`curr_block`")
	private String selBlockULB;

	@Column(name = "`curr_gp`")
	private String selGPWardNo;

	@Column(name = "`curr_village`")
	private String selVillageLocalAreaName;

	@Transient
	private String selVillageLocalAreaNameVal;

	@Column(name = "`curr_police_station`")
	private String txtPoliceStation;

	@Column(name = "`curr_post_office`")
	private String txtPostOffice;

	@Column(name = "`curr_address_line_1`")
	private String txtHabitationStreetNoLandmark;

	@Column(name = "`curr_address_line_2`")
	private String txtHouseNo;

	@Column(name = "`curr_pin_code`")
	private String txtPinCode;

	@Column(name = "`pre_state`")
	private String selState17;

	@Column(name = "`pre_district`")
	private String selDistrict18;

	@Column(name = "`pre_block`")
	private String selBlockULB19;

	@Column(name = "`pre_gp`")
	private String selGPWARDNumber;

	@Column(name = "`pre_village`")
	private String selVillageLocalAreaName21;

	@Column(name = "`pre_police_station`")
	private String txtPoliceStation22;

	@Column(name = "`pre_post_office`")
	private String txtPostOffice23;

	@Column(name = "`per_address_line_1`")
	private String txtHabitationStreetNoLandmark24;
	@Column(name = "`per_address_line_2`")
	private String txtHouseNo25;

	@Column(name = "`pre_pin_code`")
	private String txtPinCode26;

	@Column(name = "tender_auction_id")
	private BigInteger tenderAuctionId;

	@Transient
	private Boolean timeExpired;

	@Transient
	private String userName;

	@Column(name = "approval_status", insertable = false)
	private String approvalStatus;

	@Column(name = "bidder_form_m_application_number", insertable = false)
	private String uniqueNo;

	@Column(name = "is_curr_addr_same_per_addr")
	private Short isChecked;
	
	@Column(name = "payment_status",insertable = false)
	private Short paymentStatus;

	
	@Column(name = "approval_remarks",insertable = false)
	private String remark;
	
	@Transient
	private String plotCode;
}