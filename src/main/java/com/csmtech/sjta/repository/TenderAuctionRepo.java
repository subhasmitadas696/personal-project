package com.csmtech.sjta.repository;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmtech.sjta.entity.Tender_auction;

public interface TenderAuctionRepo extends JpaRepository<Tender_auction, Serializable> {
	Tender_auction findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

	@Query("From Tender_auction where bitDeletedFlag=:bitDeletedFlag")
	List<Tender_auction> findAllByBitDeletedFlag(Boolean bitDeletedFlag, Pageable pageRequest);

	Integer countByBitDeletedFlag(Boolean bitDeletedFlag);
	
	@Query(value = "SELECT form_m_submit_start_date FROM application.tender_auction WHERE form_m_submit_start_date IS NOT NULL "
			+ "AND tender_auction_id=:tenderId ",nativeQuery = true)
	public Date countDownTimeValidate(@Param("tenderId") BigInteger tenderId);
}