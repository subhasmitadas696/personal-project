package com.csmtech.sjta.entity;

import javax.persistence.Entity;
import javax.persistence.Column;
import java.util.List;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.sql.Date;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Table(name = "plot_information", schema = "land_bank")
@Entity
public class Plot_information {
	@Id
	@Column(name = "plot_code")
	private String txtPlotCode;
	@Column(name = "khatian_code")
	private String selKhatianCode;
	@Transient
	private String selKhatianCodeVal;

	@Transient
	private String selVillageCode;
	@Transient
	private String selVillageCodeVal;
	@Transient
	private String selTahasilCode;

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

	@Transient
	private String khatianPlotCode;

	@Column(name = "plot_no")
	private String txtPlotNo;

	@Transient
	private BigInteger totalPlot;

	@Transient
	private BigDecimal totalArea;

	@Column(name = "chaka_number")
	private String txtChakaNumber;
	@Column(name = "chaka_name")
	private String txtChakaName;
	@Column(name = "kissam")
	private String txtKissam;
	@Column(name = "area_acre")
	private BigDecimal txtAreaAcre;
	@Column(name = "remarks")
	private String txtrRemarks;

}