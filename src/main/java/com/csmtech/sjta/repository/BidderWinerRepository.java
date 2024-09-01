package com.csmtech.sjta.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.csmtech.sjta.dto.ShowWinerDTO;

public interface BidderWinerRepository {

	public List<ShowWinerDTO> getCustomResults(Integer pageNO, Integer pageSize);

	public BigInteger getCountOfLiveAuctions();

	public List<Object[]> getWinnerResult(BigInteger tenderId);

	public List<Object[]> getWinnerResultMulti(BigInteger tenderId);

	public Integer updateWinnerDocument(BigInteger liveAuctionId, String documentName, BigInteger createdBy,
			BigInteger landApplicationId, String plotNo, BigDecimal totalArea, BigDecimal purchaseArea,
			BigDecimal pricePerAcer, BigDecimal totalPriceInPurchaseArea,Integer flag, String plot);

}
