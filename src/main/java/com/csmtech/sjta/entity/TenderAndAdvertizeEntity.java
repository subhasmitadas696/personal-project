package com.csmtech.sjta.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tender_advertisement")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenderAndAdvertizeEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tender_advertiesment_id")
	private BigInteger tenderAdvertisementId;

	@Column(name = "letter_no")
	private String letterNo;

	@Column(name = "tender_type_id")
	private Short selTenderType;

	@Transient
	private String tenderTypeVal;

	@Column(name = "title")
	private String txtTitle;

	@Column(name = "upload_doc")
	private String fileUploadTenderDocument;

	@Column(name = "start_date")
	private Date txtStartDate;

	@Column(name = "close_date")
	private Date txtCloseDate;

	@Column(name = "district_code")
	private String selDistrict;

	@Transient
	private String districtVal;

	@Column(name = "tahasil_code")
	private String selTehsil;

	@Transient
	private String tehsilVal;

	@Column(name = "village_code")
	private String selMouza;

	@Transient
	private String mouzaVal;

	@Column(name = "khatian_code")
	private String selKhataNo;

	@Transient
	private String kathaNoVal;

	@Column(name = "plot_code")
	private String selPlotNo;

	@Transient
	private String plotNoVal;

	@Column(name = "created_by")
	private BigInteger createdBy;

	@CreationTimestamp
	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "updated_by")
	private BigInteger updatedBy;

//	@UpdateTimestamp
	@Column(name = "updated_on")
	private Date updatedOn;

	@Column(name = "status")
	private Boolean status = false;
}
