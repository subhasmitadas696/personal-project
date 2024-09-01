package com.csmtech.sjta.dto;

import java.math.BigInteger;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LandApplicantDetailsUPDTO {

	private BigInteger landApplicantId;
	private String applicantNo;
	private String applicantName;
	private String mobileNo;
	private String districtName;
	private String tehsilName;
	private String mouzaName;
	private String khataNo;
	private String applicationStatus;
	private String actionOn;
	private String remark;
}
