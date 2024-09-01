package com.csmtech.sjta.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LandApplicantViewDTO {

	BigInteger landApplicantId;
	String applicantNo;
	String applicantName;
	String fatherName;
	String mobileNo;
	String emailAddress;
	Short docType;
	String docName;
	String docRefNo;
	String docsPath;
	String currStateId;
	String currDistrictId;
	String currBlockId;
	String currGpId;
	String currVillageId;
	String currPoliceStation;
	String currPostOffice;
	String currStreetNo;
	String currHouseNo;
	String currPinCode;
	String preStateId;
	String preDistrictId;
	String preBlockId;
	String preGpId;
	String preVillageId;
	String prePoliceStation;
	String prePostOffice;
	String preStreetNo;
	String preHouseNo;
	String prePinCode;
	// plot add the main table
	String plotDistrictId;
	String plotTehsilId;
	String plotKhataNoId;
	String plotKhataNo;
	String plotMouzaId;
	Short pendingRoleId;

	String plotDistrict;
	String plotTehsil;
	String plotMouza;

}
