package com.csmtech.sjta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "country_master", schema = "land_bank")
@Entity
public class CountryMaster {
	@Id
	@Column(name = "country_code")
	private String txtCountryCode;
	@Column(name = "country_name")
	private String txtCountryName;

}