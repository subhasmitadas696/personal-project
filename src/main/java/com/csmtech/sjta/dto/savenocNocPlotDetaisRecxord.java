package com.csmtech.sjta.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class savenocNocPlotDetaisRecxord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	BigInteger intId;
	BigInteger selDistrictName;
	BigInteger  selTehsilName;
	BigInteger selMouza;
	BigInteger  selKhataNo;
	BigInteger  selPlotNo;
    String txtTotalRakba;
    BigInteger nocApplicantId;
    BigInteger createdBy;
    
    List<PlotDetailsSubDTO> plot_details;
	

}
