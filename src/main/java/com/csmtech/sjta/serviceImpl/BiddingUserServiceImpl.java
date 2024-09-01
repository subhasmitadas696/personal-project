package com.csmtech.sjta.serviceImpl;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.BidderDTO;
import com.csmtech.sjta.repository.BiddingUserRepository;
import com.csmtech.sjta.service.BiddingUserService;

@Service
public class BiddingUserServiceImpl implements BiddingUserService {

	@Autowired
	private BiddingUserRepository repo;

	@Override
	public Integer updateInsertSelectForBiddingUser(BidderDTO dto) {
		String enteredVal = dto.getPrice();
		BigDecimal bidderPriceCast = new BigDecimal(enteredVal);
		return repo.saveAndGetUserHighestAmount(dto.getInitId(), bidderPriceCast, dto.getTenderid());
	}

	@Override
	public Boolean getTimeExpiredOrNot(BigInteger tenderId) {
		return repo.getTimeExpiredOrNot(tenderId);
	}

	@Override
	public BigDecimal getBasePrice(BigInteger tenderId) {
		return repo.getBasePrice(tenderId);
	}

	@Override
	public String getBaseHighestPrice(BigInteger tenderId, BigDecimal bidPrice) {
		return repo.getBaseHighestPrice(tenderId, bidPrice);
	}

	@Override
	public BigDecimal getHighestPriceCheck(BigInteger tenderId) {
		return repo.getHighestPriceCheck(tenderId);
	}

	@Override
	public Boolean checkApprovalStatus(BigInteger tenderAuctionId, BigInteger userId) {
		return repo.checkApprovalStatus(tenderAuctionId, userId);
	}

	@Override
	public BigDecimal getCheckBiddingIncresePrice(BigInteger tenderId) {
		return repo.getCheckBiddingIncresePrice(tenderId);
	}

	@Override
	public BigInteger getCheckBiddingCount(BigInteger tenderId) {
		return repo.getCheckBiddingCount(tenderId);
	}

	@Override
	public BigDecimal getMaxBidPriceForUser(BigInteger userId, BigInteger tenderAuctionId) {
		return repo.getMaxBidPriceForUser(userId, tenderAuctionId);
	}

	@Override
	public BigDecimal getHighestPriceForBid(BigInteger tenderId) {
		return repo.getHighestPriceForBid(tenderId);
	}

}
