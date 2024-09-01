package com.csmtech.sjta.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionPlotDTO {
	
	private BigInteger auctionPlotId;
	private String districtCode;
	private String tahasilCode;
	private String villageCode;
	private String khatianCode;
	private Integer auctionFlag;
	private Character actionStatus;
	private String plotNo;
	private String plotExtend;
	private String approvalRemerk;
	private String distCode;
	private String tahaCode;
	private String villCode;

}
