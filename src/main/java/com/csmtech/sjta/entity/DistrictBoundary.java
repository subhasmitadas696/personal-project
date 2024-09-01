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
@Table(name = "district_boundary", schema = "land_bank")
@Entity
public class DistrictBoundary {
	@Id
	@Column(name = "gid")
	private Integer gid;

	
	@Column(name = "district_code")
	private String districtCode;

	
	@Column(name = "state_code")
	private String stateCode;

	@Column(name = "district_name")
	private String districtName;

	
	@Transient
	private String extent;
}