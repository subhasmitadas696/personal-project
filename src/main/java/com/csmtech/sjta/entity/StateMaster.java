package com.csmtech.sjta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "state_master",schema = "land_bank")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateMaster {

	@Id
	@Column(name = "state_code")
	private String stateCode;

	@Column(name = "state_name")
	private String stateName;

	@Column(name = "country_code")
	private String countryCode;
}
