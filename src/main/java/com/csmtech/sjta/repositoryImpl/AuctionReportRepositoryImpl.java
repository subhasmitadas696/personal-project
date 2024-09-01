/**
 * 
 */
package com.csmtech.sjta.repositoryImpl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.repository.AuctionReportRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author prasanta.sethi
 */
@Slf4j
@Repository
public class AuctionReportRepositoryImpl implements AuctionReportRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getReportData(Integer pageSize, Integer offset) {
		String sqlQuery = "SELECT ta.tender_auction_id, auction_number_gen AS auctionNumber, "
				+ " ta.slot_for_auction_from_date AS auctionDate, "
				+ " (SELECT full_name FROM public.citizen_profile_details WHERE citizen_profile_details_id = user_id) AS winnerName, "
				+ " la.highest_bid_price AS winnerBidAmount,  dm.district_name AS district, "
				+ " tm.tahasil_name AS tahasil, vm.village_name AS village, "
				+ " ki.khata_no AS khataNo,  pi.plot_no AS plotNo  FROM  "
				+ " application.tender_auction ta  LEFT JOIN  "
				+ " application.live_auction la ON (la.tender_auction_id = ta.tender_auction_id)   " + "LEFT JOIN  "
				+ " application.auction_plot_details apd ON (ta.auction_plot_id = apd.auction_plot_details_id)   "
				+ "LEFT JOIN application.auction_plot ap ON (ap.auction_plot_id = apd.auction_plot_id)   "
				+ "LEFT JOIN land_bank.plot_information pi ON (apd.plot_code = pi.plot_code)  "
				+ "LEFT JOIN land_bank.khatian_information ki ON (pi.khatian_code = ki.khatian_code)  "
				+ "LEFT JOIN land_bank.village_master vm ON (vm.village_code = ki.village_code)  "
				+ "LEFT JOIN land_bank.tahasil_master tm ON (tm.tahasil_code = vm.tahasil_code)  "
				+ "LEFT JOIN land_bank.district_master dm ON (tm.district_code = dm.district_code)  "
				+ "WHERE ta.deleted_flag = false  AND la.deleted_flag = '0' "
				+ " AND apd.deleted_flag = '0'  AND ap.deleted_flag = '0' ORDER BY  "
				+ " auctionDate ASC  OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("limit", pageSize).setParameter("offset", offset);
		return query.getResultList();
	}

	@Override
	@Transactional
	public BigInteger getReportCount() {
		String sqlQuery = "SELECT COUNT(*) AS count " + "FROM ( " + " SELECT  " + " ta.tender_auction_id, "
				+ " auction_number_gen AS auctionNumber, "
				+ " CAST(CONCAT(dm.district_name, ', ', tm.tahasil_name, ', ', vm.village_name, ', Khata: ', ki.khata_no, ', Plot: ', pi.plot_no) AS CHARACTER VARYING) AS location, "
				+ " ta.slot_for_auction_from_date AS auctionDate, " + "        ( " + " SELECT full_name  "
				+ " FROM public.citizen_profile_details  " + " WHERE citizen_profile_details_id = user_id "
				+ " ) AS winnerName, " + " la.highest_bid_price AS winnerBidAmount " + "    FROM  "
				+ " application.tender_auction ta " + "    LEFT JOIN  "
				+ " application.live_auction la ON (la.tender_auction_id = ta.tender_auction_id) " + " LEFT JOIN  "
				+ " application.auction_plot_details apd ON (ta.auction_plot_id = apd.auction_plot_details_id) "
				+ " LEFT JOIN  " + " application.auction_plot ap ON (ap.auction_plot_id = apd.auction_plot_id) "
				+ " LEFT JOIN  " + " land_bank.plot_information pi ON(apd.plot_code = pi.plot_code)  " + " LEFT JOIN  "
				+ " land_bank.khatian_information ki ON(pi.khatian_code = ki.khatian_code)  " + " LEFT JOIN  "
				+ " land_bank.village_master vm ON(vm.village_code=ki.village_code) " + " LEFT JOIN  "
				+ " land_bank.tahasil_master tm ON(tm.tahasil_code=vm.tahasil_code) " + " LEFT JOIN  "
				+ " land_bank.district_master dm ON(tm.district_code=dm.district_code) " + " WHERE "
				+ " ta.deleted_flag = false  " + " AND la.deleted_flag = '0' " + " AND apd.deleted_flag = '0'  "
				+ " AND ap.deleted_flag = '0' " + ") AS subquery";
		Query query = entity.createNativeQuery(sqlQuery);
		log.info("getAuctionReportCount execution  success..!!");
		return (BigInteger) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> participantData(BigInteger tenderAuctionId) {
		String sqlQuery = "select  " + "la.tender_auction_id, " + "MAX(lal.bid_price) AS highest_bid_price, "
				+ "(select full_name from public.citizen_profile_details where citizen_profile_details_id=lal.user_id) "
				+ "from application.live_auction la "
				+ "JOIN application.live_auction_log lal ON(lal.live_auction_id=la.live_auction_id) "
				+ "WHERE  la.deleted_flag='0'  "
				+ "AND la.tender_auction_id=:tenderAuctionId GROUP BY la.tender_auction_id,lal.user_id "
				+ "ORDER BY full_name";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("tenderAuctionId", tenderAuctionId);
		return query.getResultList();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAuctionDataForReportExcel() {
		String sqlQuery = " SELECT   " + "ta.tender_auction_id,  "
				+ "auction_number_gen AS auctionNumber, CAST(CONCAT(dm.district_name, ', ', tm.tahasil_name, ', ', vm.village_name, ', Khata: ', ki.khata_no, ', Plot: ', pi.plot_no) AS CHARACTER VARYING) AS landdetails, "
				+ "ta.slot_for_auction_from_date AS auctionDate,  " + "(SELECT full_name   "
				+ "FROM public.citizen_profile_details   "
				+ "WHERE citizen_profile_details_id = user_id) AS winnerName,  "
				+ "la.highest_bid_price AS winnerBidAmount,  "
				+ "(SELECT CONCAT( ' Full Name -  ', (SELECT full_name   " + "FROM public.citizen_profile_details   "
				+ "WHERE citizen_profile_details_id = la1_aggregated.max_bid_user_id ),  "
				+ "' , Highest Bid Amount -  ', la1_aggregated.max_bid_price) AS VARCHAR  "
				+ "FROM ( SELECT  la1.tender_auction_id, MAX(lal1.bid_price) AS max_bid_price,  "
				+ "lal1.user_id AS max_bid_user_id  " + "FROM application.live_auction la1  "
				+ "JOIN application.live_auction_log lal1 ON lal1.live_auction_id = la1.live_auction_id  "
				+ "WHERE la1.deleted_flag = '0'   " + "AND la1.tender_auction_id = ta.tender_auction_id   "
				+ "GROUP BY la1.tender_auction_id, lal1.user_id  " + ") la1_aggregated  "
				+ "LIMIT 1) AS participantDetails  " + "FROM application.tender_auction ta  "
				+ "LEFT JOIN application.live_auction la ON la.tender_auction_id = ta.tender_auction_id  "
				+ "LEFT JOIN application.auction_plot_details apd ON ta.auction_plot_id = apd.auction_plot_details_id  "
				+ "LEFT JOIN application.auction_plot ap ON ap.auction_plot_id = apd.auction_plot_id  "
				+ "LEFT JOIN land_bank.plot_information pi ON apd.plot_code = pi.plot_code   "
				+ "LEFT JOIN land_bank.khatian_information ki ON pi.khatian_code = ki.khatian_code   "
				+ "LEFT JOIN land_bank.village_master vm ON vm.village_code = ki.village_code  "
				+ "LEFT JOIN land_bank.tahasil_master tm ON tm.tahasil_code = vm.tahasil_code  "
				+ "LEFT JOIN land_bank.district_master dm ON tm.district_code = dm.district_code  "
				+ "WHERE ta.deleted_flag = false AND la.deleted_flag = '0' AND apd.deleted_flag = '0' AND ap.deleted_flag = '0'";
		Query query = entity.createNativeQuery(sqlQuery);
		return query.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> getBidderAllRecordHistory(BigInteger tenderAuctionId) {
		String sqlQuery = "select  la.tender_auction_id,  lal.bid_price  AS highest_bid_price,  "
				+ "(select full_name from public.citizen_profile_details where citizen_profile_details_id=lal.user_id) ,bidding_date_time "
				+ "from application.live_auction la "
				+ "JOIN application.live_auction_log lal ON(lal.live_auction_id=la.live_auction_id) "
				+ "WHERE  la.deleted_flag='0'  "
				+ "AND la.tender_auction_id=:tenderAuctionId  "
				+ "ORDER BY full_name";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("tenderAuctionId", tenderAuctionId);
		return query.getResultList();
	}
	
	
	@Override
	@Transactional
	public BigInteger getBidderAllRecordHistoryCount(BigInteger tenderAuctionId) {
		String sqlQuery = "select count(*) from (select  la.tender_auction_id,  lal.bid_price  AS highest_bid_price,  "
				+ "(select full_name from public.citizen_profile_details where citizen_profile_details_id=lal.user_id) "
				+ "from application.live_auction la "
				+ "JOIN application.live_auction_log lal ON(lal.live_auction_id=la.live_auction_id) "
				+ "WHERE  la.deleted_flag='0'  "
				+ "AND la.tender_auction_id=:tenderAuctionId  "
				+ "ORDER BY full_name ) as subquery";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("tenderAuctionId", tenderAuctionId);
		return (BigInteger) query.getSingleResult();
	}

}
