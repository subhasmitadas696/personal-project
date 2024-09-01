package com.csmtech.sjta.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VIewCitizenAuctionPlotDetailsDTO {

	private String districtCode;
	private String tahasilCode;
	private String villageCode;
	private String khatianCode;
	private String plotCode;
	private String totalArea;
	private BigInteger auctionPlotId;
	private BigInteger tenderAuctionId;
	private Date fromMStateDate;
	private Date fromMEndDate;
	private Boolean dateStatus;
	private String uniqueNoGen;
	private String plotNo;

}
