package com.csmtech.sjta.entity;

import java.math.BigInteger;
import java.util.Date;

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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_notification")
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private BigInteger notificationId;

	@Column
	private String title;

	@Column
	private String description;

	@Column(name = "upload_doc")
	private String uploadDoc;

	@Column
	private Boolean status;

	@Column(name = "created_by")
	private BigInteger createdBy;

	@Column(name = "updated_by")
	private BigInteger updatedBy;

	@Column(name = "updated_on")
	private Date updatedOn;

	@Column(name = "created_on")
	private Date createdOn;

}
