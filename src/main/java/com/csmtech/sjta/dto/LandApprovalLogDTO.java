package com.csmtech.sjta.dto;

import java.math.BigInteger;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LandApprovalLogDTO {

	 private BigInteger landApplicationApprovallogId;
	    private BigInteger landApplicantapprovalId;
	    private BigInteger landApplicantId;
	    private String approvalLevel;
	    private BigInteger applicationstatusid;
	    private BigInteger createdBy;
	    private Date createdOn;
	    private BigInteger updatedBy;
	    private Date updatedOn;
	    private boolean status;
	    private BigInteger approvalActionId;
	    private Date action;
}
