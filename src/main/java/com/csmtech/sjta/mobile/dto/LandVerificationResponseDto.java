package com.csmtech.sjta.mobile.dto;

import java.util.List;

import com.csmtech.sjta.dto.LandAppResponseStructureDTO;
import com.csmtech.sjta.dto.PlotValuationDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LandVerificationResponseDto {

	private Integer status;
	private String message;
	private List<LandAppResponseStructureDTO> landResponse;
	private List<PlotLandInspectionDto> landDetailResponse;
	private List<PlotValuationDTO> plotResponse;
	private PlotValuationDTO plotDto;
	private PlotLandInspectionDto plotLandInspectionDto;
	private Integer landApplicationPendingCount; // changed from pendingCount
	private Integer landApplicationCompleteCount;// changed from completeCount
	private Integer landUsePendingCount; // newly added
	private Integer landUseCompleteCount; // newly added
	private String statusMessage;
	private List<VillageDTO> villageDTOs; // added for new land api
	private String fcmToken;

}
