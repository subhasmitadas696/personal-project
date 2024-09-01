package com.csmtech.sjta.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sjta_plot_boundary_view", schema = "land_bank")
@Entity
public class Plot_info_Boundary {
	@Id

	@Column(name = "plot_code")
	private String plotCode;

	@Column(name = "plot_no")
	private String plotNo;

	@Column(name = "village_code")
	private String villageCode;

	@Column(name = "khata_no")
	private String khataNo;

	@Column(name = "area_acre")
	private BigDecimal area;

	@Column(name = "kissam")
	private String kissam;

	@Column(name = "available_sts")
	private String availableSts;

	@Transient
	private String extent;
}