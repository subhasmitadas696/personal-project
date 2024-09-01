package com.csmtech.sjta.entity;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Table(name = "land_documents")
@Entity
public class LandDocuments {
	@Transient
	private List<LandDocuments> addMoreUploadDocuments;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "land_doc_id")
	private Integer intId;

	@Column(name = "`document_name`")
	private String amtxtDocumentName;
	@Column(name = "`docs_path`")
	private String amfileDocument;

	@Column(name = "created_by")
	private Integer intCreatedBy;

	@Column(name = "land_applicant_id")
	@JoinColumn
	private Integer landApplicantId;
	@Column(name = "updated_by")
	private BigInteger intUpdatedBy;

	@Column(name = "created_on")
	@CreationTimestamp
	private Date dtmCreatedOn;

	@Column(name = "updated_on")
	@UpdateTimestamp
	private Date stmUpdatedOn;

	@Column(name = "deleted_flag")
	private Boolean bitDeletedFlag = false;

}