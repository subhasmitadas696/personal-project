package com.csmtech.sjta.entity;

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
@Table(name = "sjta_plot_boundary", schema = "land_bank")
@Entity
public class PlotBoundary {
	@Id
	@Column(name = "gid")
	private Integer gid;

	@Column(name = "plot_code")
	private String plotCode;
	
	@Column(name = "map_plot_no")
	private String mapplotNo;
	
	@Column(name = "village_code")
	private String villageCode;

	@Column(name = "tahasil_code")
	private String tahasilCode;

	@Transient
	private String extent;
}