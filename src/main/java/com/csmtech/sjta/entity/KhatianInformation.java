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
@Table(name = "khatian_information",schema = "land_bank")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KhatianInformation {

	@Id
	@Column(name = "khatian_code")
	private String khatianCode;

	@Column(name = "khata_no")
	private String khataNo;

	@Column(name = "owner_name")
	private String ownerName;
	
	@Column(name = "marfatdar_name")
	private String marfatdarName;
	
	@Column(name = "sotwa")
	private String sotwa;
	
	@Column(name = "publication_date")
	private Date  publicationDate;
	
	@Column(name = "village_code")
	private String villageCode;
}
