package com.csmtech.sjta.mobile.entity;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Entity
@Table(name = "notification_details", schema = "application") // notification_details
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private BigInteger notificationId;

	@Column(name = "user_id")
	private BigInteger userId;

	@Column(name = "user_type")
	private String userType;

	@Column(name = "notification")
	private String notification;

	@Column(name = "read_mode")
	private String readMode;

	@Column(name = "created_by")
	private BigInteger createdBy;

	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "updated_by")
	private BigInteger updatedBy;

	@Column(name = "updated_on")
	private Date updatedOn;

	@Column(name = "deleted_flag", insertable = false, updatable = false)
	private Boolean deletedFlag;
}
