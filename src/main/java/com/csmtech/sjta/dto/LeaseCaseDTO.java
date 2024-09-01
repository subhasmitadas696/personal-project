package com.csmtech.sjta.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaseCaseDTO implements Serializable {

	/**
	 * @author rashmi.jena
	 */
	private static final long serialVersionUID = 1L;

	
	private BigInteger leaseCaseId;
	private String caseYear;
	private String applicantName;
	private String address;
	private String contactNo;
	private String documentSubmitted;
	private String caseNo;
	
	
	private BigInteger leaseCasePlotId;
	private String districtName;
	private String tahasilName;
	private String villageName;
	private String policeName;
	private String khataNo;
	private String plotNo;
	private BigDecimal totalArea;
	private BigDecimal purchaseArea;
	private String kissam;
	private String rsdNo;
	private Short isCaseMatter;
	private BigInteger leaseCaseStatusId;
	private Short fieldInquery;
	private Date dlscMeeting;
	private Date tlscMeeting;
	private Date mcMeeting;
	private Short noticeIssued;
	private Short considerationMonyDeposite;
	private Short status;
	private String remerk;
	private Date slcMeeting;
	private String extent;
	

}
