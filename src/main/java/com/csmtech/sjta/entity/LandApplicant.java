package com.csmtech.sjta.entity;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Table(name = "land_applicant", schema = "public")
@Entity
public class LandApplicant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "land_applicant_id")
	private BigInteger landApplicantId;

	@Column(name = "`applicant_name`")
	private String txtApplicantName;
	@Column(name = "`father_name`")
	private String txtFatherHusbandName;
	@Column(name = "`mobile_no`")
	private String txtMobileNo;
	@Column(name = "`email_address`")
	private String txtEmail;

	@Transient
	private String selDocumentTypeVal;
	@Column(name = "`doc_ref_no`")
	private String txtDocumentNo;
	@Column(name = "`docs_path`")
	private String fileUploadDocument;

	@Transient
	private String selStateVal;

	@Transient
	private String selDistrictVal;

	@Transient
	private String selBlockULBVal;

	@Transient
	private String selGPWardNoVal;

	@Transient
	private String selVillageLocalAreaNameVal;
	@Column(name = "`curr_police_station`")
	private Integer txtPoliceStation;
	@Column(name = "`curr_post_office`")
	private String txtPostOffice;
	@Column(name = "`curr_street_no`")
	private String txtHabitationStreetNoLandmark;
	@Column(name = "`curr_house_no`")
	private String txtHouseNo;
	@Column(name = "`curr_pin_code`")
	private String txtPinCode;

	@Transient
	private String selState17Val;

	@Transient
	private String selDistrict18Val;

	@Transient
	private String selBlockULB19Val;

	@Transient
	private String selGPWARDNumberVal;

	@Transient
	private String selVillageLocalAreaName21Val;

	@Column(name = "`pre_police_station`")
	private Integer txtPoliceStation22;
	@Column(name = "`pre_post_office`")
	private String txtPostOffice23;
	@Column(name = "`pre_street_no`")
	private String txtHabitationStreetNoLandmark24;
	@Column(name = "`pre_house_no`")
	private String txtHouseNo25;
	@Column(name = "`pre_pin_code`")
	private String txtPinCode26;

	@Column(name = "`doc_name`")
	private String txtDocName;

	@Column(name = "applicant_no")
	private String applicantNo;

	@Transient
	private String khataNo;

	@Transient
	private String mouzaName;

	@Transient
	private String tehsilName;

}