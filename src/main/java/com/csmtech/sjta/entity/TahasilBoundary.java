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
@Table(name = "tahasil_boundary", schema = "land_bank")
@Entity
public class TahasilBoundary {
	@Id
	@Column(name = "gid")
	private Integer gid;

	@Column(name = "tahasil_code")
	private String tahasilCode;
	
	@Column(name = "tahasil_name")
	private String tahasilName;

	@Column(name = "district_code")
	private String districtCode;

	@Transient
	private String extent;
}