package com.csmtech.sjta.mobile.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LandDataDto {
	
	private Integer plotSurveyDetailsId;
	private Integer landTypeStatusId;
	private String landTypeStatus; 
	private Integer landUseId;
	private String landUse;
	private String buildUpTypeName;
	private String latitude;
	private String longitude;
	private String image;
	private String createdDate;
	private String fileName;
	private String imageLink;
	

}
