package com.csmtech.sjta.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ShowWinerDTO {

	@Id
	private BigInteger liveAuctionId;
	private BigInteger auctionPlotDetailsId;
	private BigInteger tenderAuctionId;
	private BigDecimal basePrice;
	private BigInteger userId;
	private String userName;
	private BigDecimal highestBidPrice;
	private String auctionNumberGen;
	private String auctionName;
	private Date slotForAuctionFromDate;
	private Date slotForAuctionToDate;
	private BigDecimal royality;
	private BigDecimal highestBidPriceFromLiveAuction;
	private BigInteger winnerId;
	private String winnerName;
	private String winnerDocument;
	
	

}
