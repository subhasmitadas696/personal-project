package com.csmtech.sjta.mobile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Entity
@Table(name = "m_land_use")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LandUseEntity {
	
	@Id
	@Column(name = "land_use_id")
	private Integer landUseId;
	
	@Column(name = "land_type_status_id")
	private Integer landTypeStatusId;
	
	@Column(name = "land_use")
	private String landUse;
	
	@Column(name = "status", insertable = false, updatable = false)
	private boolean status;

	public LandUseEntity(Integer landUseId, String landUse) {
		this.landUseId = landUseId;
		this.landUse = landUse;
	}

	
}
