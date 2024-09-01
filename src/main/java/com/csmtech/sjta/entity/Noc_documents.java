package com.csmtech.sjta.entity;

import java.sql.Date;

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
@Table(name = "noc_documents")
@Entity
public class Noc_documents {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "noc_doc_id")
	private Integer intId;

	@Column(name = "`hal_patta`")
	private String fileHalPatta;
	@Column(name = "`sabik_patta`")
	private String fileSabikPatta;
	@Column(name = "`sabik_hal_compar`")
	private String fileSabikHalComparisonStatement;
	@Column(name = "`setlement_yadast`")
	private String fileSettlementYaddast;
	@Column(name = "`registered_deed`")
	private String fileRegisteredDeed;

	@Column(name = "created_by")
	private Integer intCreatedBy;

	@Column(name = "updated_by")
	private Integer intUpdatedBy;

	@Column(name = "created_on")
	@CreationTimestamp
	private Date dtmCreatedOn;

	@Column(name = "updated_on")
	@UpdateTimestamp
	private Date stmUpdatedOn;

	@Column(name = "deleted_flag")
	private Boolean bitDeletedFlag = false;

	@Column(name = "jsonopttxtdetails")
	private String jsonopttxtdetails;

}