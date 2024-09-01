package com.csmtech.sjta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "land_gis", schema = "land_bank")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LandGIS {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer landGisId;

	@Column(name = "plot_code")
	private String plotCode;

	@Column(name = "app_status")
	private Integer appStatus;

	@Column(name = "land_eve")
	private Integer landEva;

	@Column(name = "meeting")
	private Integer meeting;

	@Column(name = "land_allotment")
	private Integer landAllotment;

	@Column(name = "post_allotment")
	private Integer postAllotment;

	@Column(name = "grievance")
	private Integer grievance;

	@Column(name = "current_status")
	private Integer currentStatus;

}
