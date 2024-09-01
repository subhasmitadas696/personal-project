package com.csmtech.sjta.mobile.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LandTypeDto {
	
	private Integer landTypeStatusId;
	
	private String landTypeStatus;

	public LandTypeDto(Integer landTypeStatusId, String landTypeStatus) {
		this.landTypeStatusId = landTypeStatusId;
		this.landTypeStatus = landTypeStatus;
	}

	
}
