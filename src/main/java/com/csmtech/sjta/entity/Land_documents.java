package com.csmtech.sjta.entity;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.csmtech.sjta.dto.AddMoreUploadDocumentsDTO;

import lombok.Data;

@Data
@Table(name = "land_documents")
@Entity
public class Land_documents {
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

	@Column(name = "land_application_id")
	private Integer intParentId;

	@Column(name = "updated_by")
	private Integer intUpdatedBy;

	@Column(name = "created_on")
	@CreationTimestamp
	private Date dtmCreatedOn;

	@Column(name = "updated_on")
	@UpdateTimestamp
	private Date stmUpdatedOn;

	@Column(name = "document_no")
	private String documentNumber;

	@Column(name = "deleted_flag")
	private Boolean bitDeletedFlag = false;
	@Transient
	private List<AddMoreUploadDocumentsDTO> addMoreUploadDocuments;

	@Transient
	private Short appStage;

}