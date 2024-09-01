package com.csmtech.sjta.mobile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Entity
@Table(name = "m_land_type_status")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LandTypeEntity {
	
	@Id
	@Column(name = "land_type_status_id")
	private Integer landTypeStatusId;
	
	@Column(name = "land_type_status")
	private String landTypeStatus;
	
	@Column(name = "status", insertable = false, updatable = false)
	private boolean status ;

	public LandTypeEntity(Integer landTypeStatusId, String landTypeStatus) {
		this.landTypeStatusId = landTypeStatusId;
		this.landTypeStatus = landTypeStatus;
	}
	
	
	
}
