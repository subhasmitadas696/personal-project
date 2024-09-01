package com.csmtech.sjta.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "online_service_approval_notings")
public class OnlineServiceApprovalNoting {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer notingsId;
	private Integer applicationId;
	private Integer processId;
	private Integer fromAuthority;
	private String toAuthority;
	private Date actionTaken;
	private Integer status;
	@Column(columnDefinition = "TEXT")
	private String noting;
	private Byte resubmitStatus;
	@Column(columnDefinition = "TEXT")
	private String revertRemark;
	private Date resubmitDate;
	private Integer stageCtr;
	private Byte queryTo;
	private Date createdOn;
	private Integer createdBy;
	@Column(columnDefinition = "BIT")
	private Boolean deletedFlag;
	@Column(columnDefinition = "TEXT")
	private String otherDetails;

}
