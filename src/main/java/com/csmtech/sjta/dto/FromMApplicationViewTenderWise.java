package com.csmtech.sjta.dto;

import java.io.Serializable;
import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FromMApplicationViewTenderWise implements Serializable {

	/**
	 *@author rashmi.jena 
	 */
	private static final long serialVersionUID = 4739998891087375238L;
	
	private BigInteger applicantId;
	private String personName;
	private String applicationNo;
	private String approvalStatus;
	private String approvalRemark;
}
