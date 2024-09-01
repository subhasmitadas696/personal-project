package com.csmtech.sjta.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WinnerMultiDTO {

	
	private BigInteger tenderId;
	private String auctionName;
	private String bidderName;
	private BigDecimal highestBidPrice;
	
}
