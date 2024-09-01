package com.csmtech.sjta.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionDetails {

	private String auctionNumberGen;
	private String auctionName;
	private String source;
	private Date slotForAuctionFromDate;
	private BigDecimal areaAcre;
	private Date slotForAuctionToDate;
	private BigDecimal royality;
	private String combineValueSource;
}
