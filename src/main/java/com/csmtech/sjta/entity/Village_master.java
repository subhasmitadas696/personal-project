package com.csmtech.sjta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Table(name = "village_master", schema = "land_bank")
@Entity
public class Village_master {
	@Id
	@Column(name = "village_code")
	private String txtVillageCode;

	@Column(name = "tahasil_code")
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
	private String tahasilVillageCode;

	@Column(name = "village_name")
	private String txtVillageName;
	@Column(name = "ps_name")
	private String txtPSName;
	@Column(name = "ric_name")
	private String txtricname;
	@Column(name = "thana_no")
	private String txtThanaNo;
	@Column(name = "last_publication_year")
	private Integer lastPublicationYear;
}