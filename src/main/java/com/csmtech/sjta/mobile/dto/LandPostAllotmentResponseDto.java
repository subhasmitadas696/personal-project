package com.csmtech.sjta.mobile.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LandPostAllotmentResponseDto {
	
	private Integer status;
	private String message;
	private String statusMessage;
	private List<LandPostAllotmentDto> landPostAllotmentList;
	private LandPostAllotmentDto dto;
	private Integer count;
	private Integer totalPlot;
	private String fcmToken;

}
