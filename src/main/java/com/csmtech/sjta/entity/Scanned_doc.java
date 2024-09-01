package com.csmtech.sjta.entity;

import javax.persistence.Entity;
import javax.persistence.Column;

import java.util.Date;
import java.util.List;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Transient;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigInteger;

@Data
@Table(name = "scanned_doc")
@Entity
public class Scanned_doc {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "scanned_doc_id")
	private Integer intId;

	@Column(name = "`file_type`")
	private Integer fileType;
	@Column(name = "`district_code`")
	private String selDistrict12;
	@Transient
	private String selDistrict12Val;
	@Column(name = "`tehsil_code`")
	private String selTehsil13;
	@Transient
	private String selTehsil13Val;

	@Column(name = "`file_no`")
	private String fileNo;
	@Column(name = "`scanned_pdf`")
	private String fileUploadScannedPDF16;
	@Column(name = "`upload_csv`")
	private String fileUploadCSV17;

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

	@Column(name = "deleted_flag", insertable = false, updatable = false)
	private Boolean bitDeletedFlag;
	
	@Transient
	private String originalFileName;
	
	@Transient
	private String pdfFileSize;
	
	@Transient
	private String csvFileSize;

}