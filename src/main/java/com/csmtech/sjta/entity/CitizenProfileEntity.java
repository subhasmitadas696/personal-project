package com.csmtech.sjta.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "citizen_profile_details")
public class CitizenProfileEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -5906547417157767270L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "citizen_profile_details_id")
	private BigInteger citizenProfileDetailsId;

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
	@Column(name = "`per_state`")
	private String selState17;
	@Transient
	private String selState17Val;
	@Column(name = "`per_district`")
	private String selDistrict18;
	@Transient
	private String selDistrict18Val;
	@Column(name = "`per_block`")
	private String selBlockULB19;
	@Transient
	private String selBlockULB19Val;
	@Column(name = "`per_gp`")
	private String selGPWARDNumber;
	@Transient
	private String selGPWARDNumberVal;
	@Column(name = "`per_village`")
	private String selVillageLocalAreaName21;
	@Transient
	private String selVillageLocalAreaName21Val;

	@Column(name = "`per_police`")
	private String txtPoliceStation22;
	@Column(name = "`per_post_office`")
	private String txtPostOffice23;
	@Column(name = "`per_address_line_1`")
	private String txtHabitationStreetNoLandmark24;
	@Column(name = "`per_address_line_2`")
	private String txtHouseNo25;
	@Column(name = "`per_pin_code`")
	private String txtPinCode26;

	@Column(name = "`aadhaar_no`")
	private String aadhaarNo;

	@Column(name = "pan_no")
	private String panNo;

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

	@Column(name = "status", columnDefinition = "bit(1) default false", insertable = false, updatable = false)
	private boolean bitDeletedFlag;

	@Column(name = "is_curr_addr_same_per_addr")
	private Short isChecked;

	@Column(name = "user_name")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "mobile_no")
	private String mobileno;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "otp")
	private String otp;

	@Column(name = "user_type")
	private String userType;

	@Column(name = "is_first_login")
	private Short isFirstLogin;

	
	

}
