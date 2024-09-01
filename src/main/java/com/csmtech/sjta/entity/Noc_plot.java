package com.csmtech.sjta.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Table(name = "noc_plot")
@Entity
public class Noc_plot {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "noc_plot_id")
	private Integer intId;

	@Column(name = "`district_id`")
	private Integer selDistrictName;
	@Transient
	private String selDistrictNameVal;
	@Column(name = "`tehsil_id`")
	private Integer selTehsilName;
	@Transient
	private String selTehsilNameVal;
	@Column(name = "`mouza_id`")
	private Integer selMouza;
	@Transient
	private String selMouzaVal;
	@Column(name = "`khata_no_id`")
	private Integer selKhataNo;
	@Transient
	private String selKhataNoVal;
	@Column(name = "`plot_no_id`")
	private Integer selPlotNo;
	@Transient
	private String selPlotNoVal;
	@Column(name = "`total_rakba_plot`")
	private String txtTotalRakba;
	@Column(name = "`purchase_rakba`")
	private String txtPurchaseRakba;
	@Column(name = "`varieties_id`")
	private Integer selVarieties;
	@Transient
	private String selVarietiesVal;
	@Column(name = "`docs_path`")
	private String fileDocumentaryProofofOccupancyifany;
	@Column(name = "`price_per_acre`")
	private String txtFixedPriceperAcreofPurchasedLand;
	@Column(name = "`total_cost_land`")
	private String txtTotalCostofLandPurchased;
	@Column(name = "`others`")
	private String txtOthers;

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

}