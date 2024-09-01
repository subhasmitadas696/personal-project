package com.csmtech.sjta.entity;

import java.math.BigInteger;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "khatian_digital_information", schema = "land_bank")
@Entity
public class KhatianDigitalInformation {
	@Id
	@Column(name = "digital_information_id")
	private BigInteger digiInfoId;

	@Column(name = "khatian_code")
	private String khatianCode;
	@Column(name = "village_code")
	private String villageCode;
	@Column(name = "tahasil_code")
	private String tahasilCode;

	@Column(name = "district_code")
	private String districtCode;

	@Column(name = "khata_no")
	private String khataNo;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date_time")
	private Date createdOn;

	@Column(name = "digital_file")
	private String digiFile;

}