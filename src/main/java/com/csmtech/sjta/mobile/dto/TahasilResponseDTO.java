package com.csmtech.sjta.mobile.dto;

import java.util.List;

import com.csmtech.sjta.dto.TahasilDTO;
import com.csmtech.sjta.dto.UserDetails;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TahasilResponseDTO {
	
	private Integer status;
	private String message;
	private String tahasilCode;
    private String tahasilName;
    private String statusMessage;
    private Integer count;
    private UserDetails userdetails;
	private List<VillageDTO> villageDTOs;
	private List<LandUseDto> landUseDtos;
	private List<LandTypeDto> landTypeDtos;
	private TahasilPlotDto tahasilPlotDto;
	private List<TahasilPlotDto> tahasilPlotDtosList;
	

}
