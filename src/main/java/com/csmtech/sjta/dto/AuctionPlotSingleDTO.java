package com.csmtech.sjta.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionPlotSingleDTO {
	
	private BigInteger auctionPlotId;
	private String districtCode;
	private String tahasilCode;
	private String villageCode;
	private String khatianCode;
	
	private String districtName;
	private String tahasilName;
	private String villageName;
	private String khatianName;
	private Character approvalStatus;

}
