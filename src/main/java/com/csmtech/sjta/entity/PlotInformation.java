package com.csmtech.sjta.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plot_information",schema = "land_bank")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlotInformation {

	@Id
	@Column(name = "plot_code")
	private String plotCode;

	@Column(name = "plot_no")
	private String plotNo;

	@Column(name = "kissam")
	private String kissam;
	
	@Column(name = "area_acre")
	private String areaAcre;
	
	@Column(name = "khatian_code")
	private String khatianCode;
}
