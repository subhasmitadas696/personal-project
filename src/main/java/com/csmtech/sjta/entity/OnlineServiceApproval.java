/**
 * 
 */
package com.csmtech.sjta.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author rohit.behera
 *
 */
@Entity
@Data
@Table(name = "online_service_approval")
public class OnlineServiceApproval {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer onlineServiceApprovalId;
	private Integer processId;
	private Integer applicationId;
	private Integer aTAProcessId;
	private Integer stageNo;
	private String pendingAt;
	private Integer forwardTo;
	private Integer sentFrom;
	private LocalDateTime statusDate;
	private Integer status;
	private Integer queryTo;
	private Integer resubmitStatus;
	private LocalDateTime createdOn;
	private Integer createdBy;
	private LocalDateTime updatedOn;
	private Integer updatedBy;
	private Boolean deletedFlag=false;
	private LocalDateTime approvalDate;
	private String approvalDoc;
	private Integer verifiedBy;
	private String aTAAuths;
	private String remainingATA;
}
