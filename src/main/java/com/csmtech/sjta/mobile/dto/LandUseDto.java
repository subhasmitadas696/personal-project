package com.csmtech.sjta.mobile.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LandUseDto {
	
	private Integer landUseId;
	
	private Integer landTypeStatusId;
	
	private String landUse;

	public LandUseDto(Integer landUseId, String landUse) {
		this.landUseId = landUseId;
		this.landUse = landUse;
	}

	
}
