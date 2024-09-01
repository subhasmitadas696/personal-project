package com.csmtech.sjta.entity;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lease_case_status", schema = "application")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaseCaseStatusEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lease_case_status_id")
	private BigInteger leaseCaseStatusId;

	@Column(name = "lease_case_plot_id")
	private Long leaseCasePlotId;

	@Column(name = "field_enquiry")
	private Short selfieldInqueryId;

	@Column(name = "dlsc_date")
	private Date meetingForDlsc;

	@Column(name = "tlsc_date")
	private Date tlscMeeting;

	@Column(name = "mc_date")
	private Date mcMeeting;

	@Column(name = "notice_issued")
	private Short noticeIssued;

	@Column(name = "consideration_money_deposited")
	private Short considerationMonyDeposite;


	@Column(name = "remarks")
	private String remerk;

	@Column(name = "created_by")
	private BigInteger createdBy;


	@Column(name = "updated_by")
	private BigInteger updatedBy;

	@Column(name = "updated_on")
	private Date updatedOn;


	@Column(name = "slc_date")
	private Date selslcmeeting;
	
	@Transient
	private Integer flagInsertUpdate;

}
