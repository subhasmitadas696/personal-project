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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "land_application", schema = "public")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Land_applicant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "land_application_id")
	private Integer intId;

	@Column(name = "`applicant_name`")
	private String txtApplicantName;
	@Column(name = "`father_name`")
	private String txtFatherHusbandName;
	@Column(name = "`mobile_no`")
	private String txtMobileNo;
	@Column(name = "`email_address`")
	private String txtEmail;
	@Column(name = "`doc_type`")
	private Short selDocumentType;
	@Transient
	private String selDocumentTypeVal;
	@Column(name = "`doc_ref_no`")
	private String txtDocumentNo;
	@Column(name = "`docs_path`")
	private String fileUploadDocument;

	@Column(name = "`curr_state`")
	private String selState;
	@Transient
	private String selStateVal;
	@Column(name = "`curr_district`")
	private String selDistrict;
	@Transient
	private String selDistrictVal;
	@Column(name = "`curr_block`")
	private String selBlockULB;
	@Transient
	private String selBlockULBVal;
	@Column(name = "`curr_gp`")
	private String selGPWardNo;
	@Transient
	private String selGPWardNoVal;
	@Column(name = "`curr_village`")
	private String selVillageLocalAreaName;
	@Transient
	private String selVillageLocalAreaNameVal;
	@Column(name = "`curr_police`")
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
	@Transient
	private String selState17Val;
	@Column(name = "`pre_district`")
	private String selDistrict18;
	@Transient
	private String selDistrict18Val;
	@Column(name = "`pre_block`")
	private String selBlockULB19;
	@Transient
	private String selBlockULB19Val;
	@Column(name = "`pre_gp`")
	private String selGPWARDNumber;
	@Transient
	private String selGPWARDNumberVal;
	@Column(name = "`pre_village`")
	private String selVillageLocalAreaName21;
	@Transient
	private String selVillageLocalAreaName21Val;

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

	@Column(name = "deleted_flag", columnDefinition = "bit(1) default false")
	private boolean bitDeletedFlag;

	@Column(name = "`pre_police`")
	private String txtPoliceStation22;
	@Column(name = "`pre_post_office`")
	private String txtPostOffice23;
	@Column(name = "`per_address_line_1`")
	private String txtHabitationStreetNoLandmark24;
	@Column(name = "`per_address_line_2`")
	private String txtHouseNo25;
	@Column(name = "`pre_pin_code`")
	private String txtPinCode26;

	@Column(name = "`doc_name`")
	private String txtDocName;

	@Column(name = "application_no")
	private String applicantNo;

	@Column(name = "`save_status`")
	private Short saveStatus = 1;

	@Column(name = "district_code")
	private String selDistrictName;
	@Transient
	private String selDistrictNameVal;

	@Column(name = "`tehsil_code`")
	private String selTehsilName;

	@Transient
	private String selTehsilNameVal;
	@Column(name = "`village_code`")
	private String selMouza;
	@Transient
	private String selMouzaVal;
	@Column(name = "`khatian_code`")
	private String selKhataNo;
	@Transient
	private String selKhataNoVal;

	@Column(name = "app_status")
	private Short appStage;
	
	@Column(name = "is_curr_addr_same_per_addr")
	private Short isChecked;

	public Land_applicant(Integer intId, Integer intCreatedBy, String applicantNo) {
		super();
		this.intId = intId;
		this.intCreatedBy = intCreatedBy;
		this.applicantNo = applicantNo;
	}
	
	
}