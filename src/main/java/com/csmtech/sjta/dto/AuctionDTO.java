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
public class AuctionDTO {

	private String auctionNumberGen;
	private String auctionName;
	private Date slotForAuctionFromDate;
	private Date slotForAuctionToDate;
	private String auctionStartTime;
	private String auctionEndTime;
	private BigDecimal royalty;
	private BigDecimal basePrice;
	private BigDecimal highestBidPrice;
	private String now;
	private BigInteger tenderAuctionId;
	private Date formSubmitStartDate;
	private Date formSubmitEndDate;
	private BigInteger bddingNumber;

}
