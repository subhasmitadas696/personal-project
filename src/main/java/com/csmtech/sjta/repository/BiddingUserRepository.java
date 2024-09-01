package com.csmtech.sjta.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public interface BiddingUserRepository {

	public Integer saveAndGetUserHighestAmount(BigInteger bidderId, BigDecimal bidderBiddignPrice,
			BigInteger tenderAuctionId);

	public BigDecimal getLastAmount(BigInteger tenderId);

	public Boolean getTimeExpiredOrNot(BigInteger tenderId);

	public BigDecimal getBasePrice(BigInteger tenderId);

	public String getBaseHighestPrice(BigInteger tenderId, BigDecimal bidPrice);

	public BigDecimal getHighestPriceCheck(BigInteger tenderId);

	public Boolean checkApprovalStatus(BigInteger tenderAuctionId, BigInteger userId);

	public BigDecimal getCheckBiddingIncresePrice(BigInteger tenderId);

	public BigInteger getCheckBiddingCount(BigInteger tenderId);

	public BigDecimal getMaxBidPriceForUser(BigInteger userId, BigInteger tenderAuctionId);
	
	public BigDecimal getHighestPriceForBid(BigInteger tenderId);

}
