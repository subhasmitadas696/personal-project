package com.csmtech.sjta.dto;

import java.math.BigInteger;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SourcerCreationDto {

	private BigInteger intId;
	private String selDistrictName;
	private String selTehsilName;
	private String selMouza;
	private String selKhataNo;
	private String selPlotNo;
	private Double txtTotalRakba;
	private BigInteger intCreatedBy;
	private String plotCode;
	private Integer saveFlag;
	private BigInteger landAllotementId;
	
	private List<PlotDetails> plot_details;
}
