package com.csmtech.sjta.entity;

import java.math.BigInteger;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "land_application_approval_log", schema = "public")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class LandApprovalLog {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "land_approval_application_log_id")
    private BigInteger landApplicationApprovallogId;
	
    @Column(name = "land_application_approval_id")
	private BigInteger landApplicantapprovalId;

    @Column(name = "land_applicant_id")
    private BigInteger landApplicantId;

    @Column(name = "approval_level")
    private String approvalLevel;

    @Column(name = "application_status_id")
    private BigInteger applicationstatusid;

    @Column(name = "created_by")
    private BigInteger createdBy;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "updated_by")
    private BigInteger updatedBy;

    @Column(name = "updated_on")
    private Date updatedOn;

    @Column(name = "status")
    private boolean status;

    @Column(name = "approval_action_id")
    private BigInteger approvalActionId;

    @Column(name = "action_on")
    private Date action;
    

}
