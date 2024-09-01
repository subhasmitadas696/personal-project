package com.csmtech.sjta.entity;

import java.math.BigInteger;
import java.sql.Date;

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
@Table(name = "land_plot")
@Entity
public class LandPlot {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "land_applicant_plot_id")
	private BigInteger intId;

	@Transient
	private String selDistrictNameVal;

	@Transient
	private String selTehsilNameVal;

	@Transient
	private String selMouzaVal;

	@Transient
	private String selKhataNoVal;
	@Column(name = "`plot_no_id`")
	private Integer selPlotNo;
	@Transient
	private String selPlotNoVal;
	@Column(name = "`total_area`")
	private String txtTotalRakba;
	@Column(name = "`purchase_area`")
	private String txtPurchaseRakba;
	@Column(name = "`varieties_id`")
	private Integer selVarieties;
	@Transient
	private String selVarietiesVal;

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

	@Column(name = "`land_applicant_id`")
	@JoinColumn
	private BigInteger landApplicantId;

	@Transient
	private String varitiesName;

}