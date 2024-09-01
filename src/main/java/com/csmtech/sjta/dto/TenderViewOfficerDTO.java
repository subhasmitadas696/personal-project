package com.csmtech.sjta.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenderViewOfficerDTO {

	private BigInteger tenderId;
	private String tenderName;
	String totalApplicant;
	String approvedApplicant;
	String rejectApplicant;
}
