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
@Table(name = "village_boundary", schema = "land_bank")
@Entity
public class VillageBoundary {
	@Id
	@Column(name = "gid")
	private Integer gid;

	@Column(name = "village_code")
	private String villageCode;
	
	@Column(name = "village_name")
	private String villageName;

	@Column(name = "tahasil_code")
	private String tahasilCode;

	@Transient
	private String extent;
}