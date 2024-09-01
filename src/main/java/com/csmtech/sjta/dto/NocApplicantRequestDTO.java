package com.csmtech.sjta.dto;

import java.io.Serializable;
import java.math.BigInteger;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NocApplicantRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Integer applicantid;
	String txtApplicantName;
	String txtFatherHusbandName;

	String txtMobileNo;
	String txtEmail;
	Integer selDocumentType;
	String txtDocumentNo;
	Integer selState;
	Integer selDistrict;
	Integer selBlockULB;
	Integer selGPWardNo;
	Integer selVillageLocalAreaName;
	String txtPoliceStation;
	String txtPostOffice;
	String txtHabitationStreetNoLandmark;
	String txtHouseNo;
	String txtPinCode;
	Integer selState17;
	Integer selDistrict18;
	Integer selBlockULB19;
	Integer selGPWARDNumber;
	Integer selVillageLocalAreaName21;
	String txtPoliceStation22;
	String txtPostOffice23;
	String txtHabitationStreetNoLandmark24;
	String txtHouseNo25;
	String txtPinCode26;
	Integer createdBy;

}
