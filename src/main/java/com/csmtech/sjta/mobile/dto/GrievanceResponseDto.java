package com.csmtech.sjta.mobile.dto;


import java.util.List;

import com.csmtech.sjta.dto.GrievanceMainDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrievanceResponseDto {
	private Integer status;
	private String message;
	private String GrievanceId;
	private String grievanceNo;
	private String otp;
	private String mobileNo;
	private String otpStatus;
	private Integer pendingCount;
	private Integer completedCount;
	List<GrievanceMainDTO> grievanceDtos;
	private GrievanceMainDTO grievance;
	private String statusMessage;
	private Integer landPendingCount;
	private Integer landCompletedCount; //added for mobility team
	private Integer landPostAllotmentPendingCount;
	private Integer landPostAllotmentCompletedCount;
	
	
}
