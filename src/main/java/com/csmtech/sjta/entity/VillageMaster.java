package com.csmtech.sjta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "village_master",schema = "land_bank")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VillageMaster {

	@Id
	@Column(name = "village_code")
	private String villageCode;

	@Column(name = "village_name")
	private String villageName;

	@Column(name = "tahasil_code")
	private String tahasilCode;
}
