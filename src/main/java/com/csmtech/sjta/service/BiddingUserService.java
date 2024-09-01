package com.csmtech.sjta.service;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.csmtech.sjta.dto.BidderDTO;

public interface BiddingUserService {

	public Integer updateInsertSelectForBiddingUser(BidderDTO dto);

	public Boolean getTimeExpiredOrNot(BigInteger tenderId);

	public BigDecimal getBasePrice(BigInteger tenderId);

	public String getBaseHighestPrice(BigInteger tenderId,BigDecimal bidPrice);
	
	public BigDecimal getHighestPriceCheck(BigInteger tenderId);
	
	public Boolean checkApprovalStatus( BigInteger tenderAuctionId, BigInteger userId);
	
	public BigDecimal getCheckBiddingIncresePrice(BigInteger tenderId);
	
	public BigInteger getCheckBiddingCount(BigInteger tenderId);
	
	public BigDecimal getMaxBidPriceForUser(BigInteger userId, BigInteger tenderAuctionId);
	
	public BigDecimal getHighestPriceForBid(BigInteger tenderId);
}
