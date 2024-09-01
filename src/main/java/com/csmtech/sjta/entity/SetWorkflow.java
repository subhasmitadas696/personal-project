package com.csmtech.sjta.entity;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="set_workflow")
public class SetWorkflow {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer workflowId;
	private Integer processId;
	private Integer folderId;
	@Column(columnDefinition = "TEXT")
	private String canvasData;
	@Column(columnDefinition = "BIT")
	private Boolean deletedFlag;
	private BigInteger createdBy;
	private Date createdOn;
	@Column(columnDefinition = "TEXT")
	private String vchMailSmsConfigIds;
	
}
