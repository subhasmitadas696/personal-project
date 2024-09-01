package com.csmtech.sjta.repositoryImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.repository.BiddingUserRepository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class BiddingUserRepositoryImpl implements BiddingUserRepository {

	@Autowired
	@PersistenceContext
	private EntityManager entity;

	@Transactional
	@Override
	public Integer saveAndGetUserHighestAmount(BigInteger bidderId, BigDecimal bidderBiddingPrice,
			BigInteger tenderAuctionId) {
		String updateSql = "UPDATE application.live_auction " + "SET user_id = :bidderId, "
				+ "highest_bid_price = :bidderBiddingPrice, " + "last_bidding_date_time = CURRENT_TIMESTAMP, "
				+ "updated_by = :bidderId " + "WHERE tender_auction_id = :tenderAuctionId";

		Query updateQuery = entity.createNativeQuery(updateSql);
		updateQuery.setParameter("bidderId", bidderId);
		updateQuery.setParameter("bidderBiddingPrice", bidderBiddingPrice);
		updateQuery.setParameter("tenderAuctionId", tenderAuctionId);
		updateQuery.executeUpdate();

		// Insert into live_auction_log table
		String insertSql = "INSERT INTO application.live_auction_log "
				+ "(live_auction_id, tender_auction_id, user_id, bid_price, created_by, bidding_date_time) "
				+ "SELECT live_auction_id, tender_auction_id, :bidderId, :bidderBiddingPrice, :bidderId, CURRENT_TIMESTAMP "
				+ "FROM application.live_auction " + "WHERE tender_auction_id = :tenderAuctionId";

		Query insertQuery = entity.createNativeQuery(insertSql);
		insertQuery.setParameter("bidderId", bidderId);
		insertQuery.setParameter("bidderBiddingPrice", bidderBiddingPrice);
		insertQuery.setParameter("tenderAuctionId", tenderAuctionId);
		return insertQuery.executeUpdate();

	}

	@Transactional
	@Override
	public BigDecimal getLastAmount(BigInteger tenderId) {
		String selectSql = "SELECT highest_bid_price FROM application.live_auction WHERE tender_auction_id = :tenderAuctionId ";
		return (BigDecimal) entity.createNativeQuery(selectSql).setParameter("tenderAuctionId", tenderId)
				.getSingleResult();
	}

	@Transactional
	@Override
	public Boolean getTimeExpiredOrNot(BigInteger tenderId) {
		Boolean timeValidation = null;
		String sqlQuery = "SELECT slot_for_auction_from_date, slot_for_auction_to_date "
				+ "FROM application.tender_auction ta "
				+ "WHERE  ta.deleted_flag = '0' AND ta.tender_auction_id = :tenderId "; //publish_status = '1' AND

		Query nativeQuery = entity.createNativeQuery(sqlQuery).setParameter("tenderId", tenderId);
		try {
			Object[] result = (Object[]) nativeQuery.getSingleResult();
			java.sql.Timestamp fromTimestamp = (java.sql.Timestamp) result[0];
			java.sql.Timestamp toTimestamp = (java.sql.Timestamp) result[1];
			java.util.Date currentDate = new java.util.Date();
			java.sql.Time currentTime = new java.sql.Time(currentDate.getTime());

			if (currentTime.after(fromTimestamp) && currentTime.before(toTimestamp)) {
				timeValidation = true;
			} else {
				timeValidation = false;
			}

		} catch (Exception e) {
			log.info("getting exception " + e);
		} finally {
			entity.close();
		}
		return timeValidation;
	}

	@Transactional
	@Override
	public BigDecimal getBasePrice(BigInteger tenderId) {
		String sqlQuery = " select base_price " + " from application.live_auction "
				+ " where tender_auction_id= :tenderId " + " AND deleted_flag='0' ";

		Query nativeQuery = entity.createNativeQuery(sqlQuery).setParameter("tenderId", tenderId);
		BigDecimal result = (BigDecimal) nativeQuery.getSingleResult();
		entity.close();
		return result;
	}

	@Transactional
	@Override
	public String getBaseHighestPrice(BigInteger tenderId, BigDecimal bidPrice) {
		String sqlQuery = " SELECT " + "    CASE "
				+ "        WHEN highest_bid_price IS NULL OR :bidPrice >= highest_bid_price THEN 'true' "
				+ "        ELSE 'false' " + "    END AS is_greater_or_equal " + " FROM "
				+ "    application.live_auction " + "WHERE " + "    tender_auction_id = :tenderId "
				+ "    AND deleted_flag = '0' " + " ";

		Query nativeQuery = entity.createNativeQuery(sqlQuery).setParameter("tenderId", tenderId)
				.setParameter("bidPrice", bidPrice);
		String result = (String) nativeQuery.getSingleResult();
		entity.close();
		return result;
	}

	@Transactional
	@Override
	public BigDecimal getHighestPriceCheck(BigInteger tenderId) {
		String sqlQuery = "  select  highest_bid_price " + " from application.live_auction "
				+ " where tender_auction_id=:tenderId " + " ";

		Query nativeQuery = entity.createNativeQuery(sqlQuery).setParameter("tenderId", tenderId);
		BigDecimal result = (BigDecimal) nativeQuery.getSingleResult();
		entity.close();
		return result;
	}

	@Transactional
	@Override
	public Boolean checkApprovalStatus(BigInteger tenderAuctionId, BigInteger userId) {

		String sqlQuery = " SELECT " + "    CASE  " + "        WHEN COUNT(*) > 0 " + "        THEN 'true'  "
				+ "        ELSE 'false'  " + "    END AS is_approved  " + "FROM "
				+ "    application.bidder_form_m_application  " + "WHERE " + "    tender_auction_id = :tenderAuctionId "
				+ "    AND deleted_flag = '0' " + "    AND approval_status = 'A'  " + "    AND user_id = :userId  "
				+ "    AND created_by = :userId " + " ";

		Query query = entity.createNativeQuery(sqlQuery);
		query.setParameter("tenderAuctionId", tenderAuctionId);
		query.setParameter("userId", userId);
		Object result = query.getSingleResult();
		if (result != null) {
			return result.toString().equalsIgnoreCase("true");
		}
		return false;
	}

	@Transactional
	@Override
	public BigDecimal getCheckBiddingIncresePrice(BigInteger tenderId) {
		String sqlQuery = " select tender_bidding_increse_value " + "from application.tender_auction "
				+ "where tender_auction_id=:tenderId " + "AND deleted_flag=false ";

		Query nativeQuery = entity.createNativeQuery(sqlQuery).setParameter("tenderId", tenderId);
		BigDecimal result = (BigDecimal) nativeQuery.getSingleResult();
		entity.close();
		return result;
	}

	@Transactional
	@Override
	public BigInteger getCheckBiddingCount(BigInteger tenderId) {
		String sqlQuery = " select count(*)  as bdding_number " + " from application.live_auction_log   "
				+ " where tender_auction_id= :tenderId " + " AND deleted_flag='0'  ";

		Query nativeQuery = entity.createNativeQuery(sqlQuery).setParameter("tenderId", tenderId);
		BigInteger result = (BigInteger) nativeQuery.getSingleResult();
		entity.close();
		return result;
	}
	
	@Transactional
	@Override
	public BigDecimal getMaxBidPriceForUser(BigInteger userId, BigInteger tenderAuctionId) {
		String sqlQuery = "SELECT MAX(bid_price) FROM application.live_auction_log "
				+ "WHERE user_id = :userId AND tender_auction_id = :tenderAuctionId";
		Query nativeQuery = entity.createNativeQuery(sqlQuery).setParameter("userId", userId)
				.setParameter("tenderAuctionId", tenderAuctionId);
		BigDecimal maxBidPrice = (BigDecimal) nativeQuery.getSingleResult();
		return maxBidPrice;
	}
	
	
	@Transactional
	@Override
	public BigDecimal getHighestPriceForBid(BigInteger tenderId) {
		String sqlQuery = "  SELECT highest_bid_price FROM application.live_auction \r\n"
				+ "WHERE tender_auction_id =:tenderId AND deleted_flag = '0'   ";
		Query nativeQuery = entity.createNativeQuery(sqlQuery).setParameter("tenderId", tenderId);
		BigDecimal result = (BigDecimal) nativeQuery.getSingleResult();
		entity.close();
		return result;
	}

}
