package com.csmtech.sjta.mobile.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrievanceCoordinatorInspectionDTO {

	private Integer grievanceId;
	// added for mobile functionality
	private Date scheduledInspectionDate;
	private String inspectionBy;
	private Date inspectionDate;
	private String goFinalRemarks;
	private String goRemarks;
	private String coRemarks;
	private String coUploadedPhoto;
	private String landLocation;
	private String grievanceNo;

}
