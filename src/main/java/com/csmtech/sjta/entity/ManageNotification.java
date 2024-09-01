package com.csmtech.sjta.entity;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Table(name = "m_notification")
@Entity
public class ManageNotification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private Integer intId;

	@Column(name = "title")
	private String txttitle;
	@Column(name = "upload_doc")
	private String fileUploadDocument;
	@Column(name = "description")
	private String txtrDescription;

	@Column(name = "publish")
	private Character publish;

	@Column(name = "created_by")
	private BigInteger intCreatedBy;

	@Column(name = "updated_by")
	private Integer intUpdatedBy;

	@Column(name = "created_on")
	@CreationTimestamp
	private Date dtmCreatedOn;

	@Column(name = "updated_on")
	@UpdateTimestamp
	private Date stmUpdatedOn;

	@Column(name = "status")
	private Boolean bitDeletedFlag;

}