package com.csmtech.sjta.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionPlotIdDetrailsDTO {
	
	private BigInteger auctionPlotDetailsId;
	private BigInteger auctionPlotId;
	private String plotNo;
	private String plotCode;
	private String areaAcre;
	private String kissam;

}
