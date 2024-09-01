package com.csmtech.sjta.dto;

import java.math.BigInteger;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LandapprovalDTO {
	private BigInteger landApplicationApprovalId;
    private BigInteger landApplicantId;
    private String approvalLevel;
    private BigInteger pendingAtRoleId;
    private BigInteger applicationStatusId;
    private BigInteger createdBy;
    private Date createdOn;
    private BigInteger updatedBy;
    private Date updatedOn;
    private boolean status;
    private BigInteger approvalActionId;
    private String remark;
    private Date action;

}
