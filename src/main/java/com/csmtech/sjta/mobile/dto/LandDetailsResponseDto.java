package com.csmtech.sjta.mobile.dto;

import java.math.BigInteger;
import java.util.List;

import com.csmtech.sjta.dto.GrievanceMainDTO;
import com.csmtech.sjta.dto.LandPlotViewDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LandDetailsResponseDto {
	

	BigInteger landApplicantId;
	String applicantNo;
	String applicantName;
	String fatherName;
	String mobileNo;
	String emailAddress;
	Short docType;
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
	String plotMouzaId;
	String pendingRoleId;
	String assignStatus;
	String coRemarks;
	String coUploadedPhoto;
	String inspectionDate;
	List<LandPlotViewDTO> plotDto;
}
