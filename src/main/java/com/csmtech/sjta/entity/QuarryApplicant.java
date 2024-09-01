package com.csmtech.sjta.entity;

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
@Table(name = "quarry_applicant")
@Entity
public class QuarryApplicant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "quarry_applicant_id")
	private Integer intId;

	@Column(name = "`quarry_name`")
	private String txtQuarryName;
	@Column(name = "`propatory_name`")
	private String txtPropatoryName;
	@Column(name = "`father_name`")
	private String txtFatherHusbandName;
	@Column(name = "`doc_type_id`")
	private Integer selDocumentType;
	@Transient
	private String selDocumentTypeVal;
	@Column(name = "`doc_ref_no`")
	private String txtDocumentRefNo;
	@Column(name = "`docs_path`")
	private String fileDocument;
	@Column(name = "`curr_state_id`")
	private Integer selState;
	@Transient
	private String selStateVal;
	@Column(name = "`curr_district_id`")
	private Integer selDistrict;
	@Transient
	private String selDistrictVal;
	@Column(name = "`curr_block_id`")
	private Integer selBlockULB;
	@Transient
	private String selBlockULBVal;
	@Column(name = "`curr_gp_id`")
	private Integer selGPWardNumber;
	@Transient
	private String selGPWardNumberVal;
	@Column(name = "`curr_village_id`")
	private Integer selVillageLocalAreaName;
	@Transient
	private String selVillageLocalAreaNameVal;
	@Column(name = "`curr_police_station`")
	private String txtPoliceStation;
	@Column(name = "`curr_post_office`")
	private String txtPostOffice;
	@Column(name = "`curr_street_no`")
	private String txtHabitationStreetLandmark;
	@Column(name = "`curr_house_no`")
	private String txtHouseNumberOtherDetails;
	@Column(name = "`curr_pin_code`")
	private String txtPincode;
	@Column(name = "`pre_state_id`")
	private Integer selState16;
	@Transient
	private String selState16Val;
	@Column(name = "`pre_district_id`")
	private Integer selDistrict17;
	@Transient
	private String selDistrict17Val;
	@Column(name = "`pre_block_id`")
	private Integer selBlockULB18;
	@Transient
	private String selBlockULB18Val;
	@Column(name = "`pre_gp_id`")
	private Integer selGPWardNumber19;
	@Transient
	private String selGPWardNumber19Val;
	@Column(name = "`pre_village_id`")
	private Integer selVillageLocalAreaName20;
	@Transient
	private String selVillageLocalAreaName20Val;
	@Column(name = "`pre_police_station`")
	private String txtPoliceStation21;
	@Column(name = "`pre_post_office`")
	private String txtPostOffice22;
	@Column(name = "`pre_street_no`")
	private String txtHabitationStreetLandmark23;
	@Column(name = "`pre_house_no`")
	private String txtHouseNumberOtherDetails24;
	@Column(name = "`pre_pin_code`")
	private String txtPincode25;
	@Column(name = "`start_date`")
	private Date txtDurationStartDate;
	@Column(name = "`end_date`")
	private Date txtDurationEndDate;

	@Column(name = "created_by")
	private Integer intCreatedBy;

	@Column(name = "updated_by")
	private Integer intUpdatedBy;

	@Column(name = "created_on")
	@CreationTimestamp
	private Date dtmCreatedOn;

	@Column(name = "updated_on")
	@UpdateTimestamp
	private Date stmUpdatedOn;

	@Column(name = "deleted_flag")
	private Boolean bitDeletedFlag = false;

	@Column(name = "jsonopttxtdetails")
	private String jsonopttxtdetails;

}