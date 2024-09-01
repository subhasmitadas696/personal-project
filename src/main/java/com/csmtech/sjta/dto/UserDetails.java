package com.csmtech.sjta.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * @Auth  GuruPrasad
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigInteger userId;
	private String userName;
	private String fullName;
	private String mobileNo;
	private String emailId;
	private BigInteger updatedBy;
	private Date updatedOn;
	private String password;
	private BigInteger departmentId;
	private BigInteger roleId;
	private BigInteger createdBy;
	private String designation;

	private BigInteger citizenId;
	private String selState;
	private String selStateVal;
	private String selDistrict;
	private String selDistrictVal;
	private String selBlockULB;
	private String selBlockULBVal;
	private String selGPWardNo;
	private String selGPWardNoVal;
	private String selVillageLocalAreaName;
	private String selVillageLocalAreaNameVal;
	private String txtPoliceStation;
	private String txtPostOffice;
	private String txtHabitationStreetNoLandmark;
	private String txtHouseNo;
	private String txtPinCode;
	private String selState17;
	private String selState17Val;
	private String selDistrict18;
	private String selDistrict18Val;
	private String selBlockULB19;
	private String selBlockULB19Val;
	private String selGPWARDNumber;
	private String selGPWARDNumberVal;
	private String selVillageLocalAreaName21;
	private String selVillageLocalAreaName21Val;

	private String txtPoliceStation22;
	private String txtPostOffice23;
	private String txtHabitationStreetNoLandmark24;
	private String txtHouseNo25;
	private String txtPinCode26;

	private String aadhaarNo;

	private String panNo;

	private Integer intCreatedBy;

	private Integer intUpdatedBy;

	private Date dtmCreatedOn;

	private Date stmUpdatedOn;
	
	private Short isChecked;
	
	private String userType;

	public UserDetails(BigInteger userId, String userName, String fullName, String mobileNo, String emailId,
			String designation) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.fullName = fullName;
		this.mobileNo = mobileNo;
		this.emailId = emailId;
		this.designation = designation;
	}

}
