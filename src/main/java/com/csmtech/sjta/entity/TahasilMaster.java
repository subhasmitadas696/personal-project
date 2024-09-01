package com.csmtech.sjta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tahasil_master", schema = "land_bank")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TahasilMaster {

	@Id
	@Column(name = "tahasil_code")
	private String tahasilCode;

	@Column(name = "tahasil_name")
	private String tahasilName;

	@Column(name = "district_code")
	private String txtDistrictCode;
}
