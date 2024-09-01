package com.csmtech.sjta.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantPlotDTO {
	private BigInteger districtId;
	private BigInteger tehsilId;
	private BigInteger mouzaId;
	private BigInteger khataNoId;

//	private BigInteger districtId;
//	private BigInteger tehsilId;
//	private BigInteger mouzaId;
//	private BigInteger khataNoId;
	private BigInteger plotNoId;
	private String totalArea;
	private String purchaseRakba;
//	private BigInteger varietiesId;
//	private String docsPath;
//	private String pricePerAcre;
//	private String totalCostLand;
//	private String others;
}
