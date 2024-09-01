package com.csmtech.sjta.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.csmtech.sjta.dto.DigitalFileKhatianInformation;

import lombok.Data;

@Data
@Table(name = "khatian_information", schema = "land_bank")
@Entity
public class Khatian_information {
	@Id
	@Column(name = "khatian_code")
	private String txtKhatianCode;
	@Column(name = "village_code")
	private String selVillageCode;
	@Transient
	private String selVillageCodeVal;
	@Transient
	private String selTahasilCode;

	@Transient
	private String villKhatianCode;

	@Transient
	private String selTahasilCodeVal;

	@Transient
	private String selDistrictCode;

	@Transient
	private String selDistrictCodeVal;

	@Transient
	private String selStateCode;

	@Transient
	private String selStateCodeVal;

	@Transient
	private String selCountryCode;

	@Transient
	private String selCountryCodeVal;

	@Column(name = "khata_no")
	private String txtKhataNo;
	@Column(name = "owner_name")
	private String txtOwnerName;
	@Column(name = "marfatdar_name")
	private String txtmarfatdarname;
	@Column(name = "sotwa")
	private String txtSotwa;
	@Column(name = "water_rent_amount")
	private BigDecimal txtWaterRentAmount;
	@Column(name = "khajana_amount")
	private BigDecimal txtKhajanaAmount;
	@Column(name = "cess_amount")
	private BigDecimal txtCessAmount;
	@Column(name = "other_cess_amount")
	private BigDecimal txtOtherCessAmount;
	@Column(name = "total_amount")
	private BigDecimal txtTotalAmount;
	@Column(name = "remarks")
	private String txtrremarks;
	@Column(name = "publication_date")
	private Date txtPublicationDate;

	@Transient
	private Integer createdBy;

	@Transient
	private Date createdOn;

	@Transient
	private String originalFileName;

	@Transient
	private String txtPlotNo;

	@Transient
	private List<DigitalFileKhatianInformation> addMoreUploadDocuments;
	@Transient
	private String extent;

}