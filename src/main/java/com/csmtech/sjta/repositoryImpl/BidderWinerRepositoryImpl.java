package com.csmtech.sjta.repositoryImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.ShowWinerDTO;
import com.csmtech.sjta.repository.BidderWinerRepository;
import com.csmtech.sjta.repository.LandAreaStatisticsRepository;

@Repository
public class BidderWinerRepositoryImpl implements BidderWinerRepository  {

	@PersistenceContext
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	LandAreaStatisticsRepository landGis;

	@Transactional
	@Override
	public List<ShowWinerDTO> getCustomResults(Integer pageNO,Integer pageSize) {
		String sqlQuery = " SELECT live_auction_id as liveAuctionId ,"
				+ "  auction_plot_details_id as auctionPlotDetailsId , la.tender_auction_id as tenderAuctionId, base_price as basePrice , user_id as userId, "
				+ " (SELECT full_name FROM public.citizen_profile_details WHERE citizen_profile_details_id=user_id LIMIT 1) as userName, "
				+ " highest_bid_price as highestBidPrice , ta.auction_number_gen as auctionNumberGen, ta.auction_name as auctionName , "
				+ " ta.slot_for_auction_from_date as slotForAuctionFromDate , "
				+ " ta.slot_for_auction_to_date as slotForAuctionToDate , ta.royality as royality , la.highest_bid_price as highestBidPriceFromLiveAuction , "
				+ " la.user_id  as winnerId , "
				+ " (SELECT full_name FROM public.citizen_profile_details WHERE citizen_profile_details_id=user_id LIMIT 1) as winnerName, la.upload_winner_document as winnerDocument "
				+ " FROM application.live_auction la "
				+ " JOIN application.tender_auction ta ON (la.tender_auction_id=ta.tender_auction_id) AND  "
				+ " ta.deleted_flag=false"
				+ " WHERE la.deleted_flag = '0' AND ta.slot_for_auction_to_date <= CURRENT_DATE ORDER BY live_auction_id DESC "
				+ " LIMIT :pageSize OFFSET :offset "  ;
		
		Integer offset = (pageNO - 1) * pageSize;

		Query query = entityManager.createNativeQuery(sqlQuery, ShowWinerDTO.class);

		@SuppressWarnings("unchecked")
		List<ShowWinerDTO> resultList = query.setParameter("pageSize", pageSize).setParameter("offset", offset)
				.getResultList();
		
		return resultList;
	}
	
	
	@Transactional
	@Override
	public BigInteger getCountOfLiveAuctions() {
		String sqlQuery = " SELECT COUNT(*) FROM application.live_auction la "
				+ " JOIN application.tender_auction ta ON (la.tender_auction_id=ta.tender_auction_id) "
				+ " WHERE la.deleted_flag = '0' AND ta.slot_for_auction_to_date <= CURRENT_DATE ";

		Query query = entityManager.createNativeQuery(sqlQuery);
		Object result = query.getSingleResult();
		BigInteger count = BigInteger.ZERO;
		if (result != null) {
			count = ((BigInteger) result);
		}

		return count;
	}

	
	@Transactional
	@Override
	public List<Object[]> getWinnerResult(BigInteger tenderId) {
		String sqlQuery = " select auction_number_gen as auctionNumberGen,auction_name as auctionName,royality as royality, "
				+ "live_auction_id as liveAuctionId ,(SELECT full_name  FROM  public.citizen_profile_details WHERE citizen_profile_details_id=la.user_id LIMIT 1) as winnerName, "
				+ "highest_bid_price as highestBidPrice,la.upload_winner_document as winnerDocument,apd.plot_code,la.user_id,(select "
				+ "plot_no from land_bank.plot_information where plot_code=apd.plot_code), "
				+ "(select bfma2.bidder_form_m_application_id from application.bidder_form_m_application bfma2 where user_id=la.user_id  "
				+ "AND bfma2.tender_auction_id=ta.tender_auction_id AND deleted_flag='0' LIMIT 1), "
				+ "CAST((select pi.area_acre as total_area from land_bank.plot_information pi where pi.plot_code=apd.plot_code   LIMIT 1)as varchar), "
				+ "CAST((select pi.area_acre as total_area from land_bank.plot_information pi where pi.plot_code=apd.plot_code LIMIT 1)as varchar) as purchase_area, "
				+ "CAST(highest_bid_price as varchar) as price_per_acer,CAST((select CAST(pi.area_acre * highest_bid_price AS NUMERIC(12,2)) as total_price  "
				+ "from land_bank.plot_information pi where pi.plot_code=apd.plot_code LIMIT 1)as varchar) "
				+ "from application.tender_auction ta    "
				+ "JOIN application.live_auction la ON(la.tender_auction_id=ta.tender_auction_id) "
				+ "JOIN application.auction_plot_details apd ON(apd.auction_plot_details_id=ta.auction_plot_id) "
				+ "JOIN application.auction_plot ap ON(ap.auction_plot_id=apd.auction_plot_id)  "
//				+ "JOIN application.auction_plot_details apd ON(apd.auction_plot_id=ta.auction_plot_id) "
				+ "where ta.tender_auction_id=:tenderId AND ta.deleted_flag=false  "
				+ "AND la.deleted_flag='0' ";
		

		Query query = entityManager.createNativeQuery(sqlQuery);
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.setParameter("tenderId", tenderId)
				.getResultList();
		return resultList;
	}
	
	
	@Transactional
	@Override
	public List<Object[]> getWinnerResultMulti(BigInteger tenderId) {
		String sqlQuery = " SELECT "
				+ "ta.tender_auction_id, "
				+ "ta.auction_name, "
				+ "(SELECT full_name FROM public.citizen_profile_details WHERE lal.user_id=citizen_profile_details_id LIMIT 1) as winner_name, "
				+ "MAX(lal.bid_price) AS highest_bid_price "
				+ "FROM application.tender_auction ta "
				+ "JOIN application.live_auction_log lal ON (lal.tender_auction_id = ta.tender_auction_id) "
				+ "WHERE ta.tender_auction_id = :tenderId "
				+ "AND ta.deleted_flag = false "
				+ "GROUP BY ta.tender_auction_id, lal.user_id "
				+ "ORDER BY highest_bid_price DESC  " ;
		

		Query query = entityManager.createNativeQuery(sqlQuery);
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.setParameter("tenderId", tenderId)
				.getResultList();
		return resultList;
	}
	
	
	@Transactional
	@Override
	public Integer updateWinnerDocument(BigInteger liveAuctionId, String documentName,BigInteger createdBy,BigInteger landApplicationId,
			String plotNo,BigDecimal totalArea,BigDecimal purchaseArea,BigDecimal pricePerAcer,BigDecimal totalPriceInPurchaseArea,Integer flag, String plot) {
        String updateSql = " UPDATE application.live_auction " +
                           " SET upload_winner_document = :documentName " +
                           " WHERE live_auction_id = :liveAuctionId";
        
        Query query = entityManager.createNativeQuery(updateSql);
        query.setParameter("documentName", documentName);
        query.setParameter("liveAuctionId", liveAuctionId);
		
        //update hear
//        String insertQueryForLandAllotement = "INSERT INTO application.land_allotement " +
//                "(created_by,form_m_application_id, plot_no, total_area, purchase_area, price_per_acer, " +
//                "total_price_in_purchase_area,land_allotement_flag) " +
//                "VALUES " +
//                "(:createdBy,:landApplicationId, :plotNo, :totalArea, :purchaseArea, " +
//                ":pricePerAcer, :totalPriceInPurchaseArea,:landAlloteFlag) ";
//        Query query2 = entityManager.createNativeQuery(insertQueryForLandAllotement);
//        query2.setParameter("createdBy",createdBy);
//        query2.setParameter("landApplicationId", landApplicationId );
//        query2.setParameter("plotNo", plotNo );
//        query2.setParameter("totalArea",totalArea  );
//        query2.setParameter("purchaseArea", purchaseArea );
//        query2.setParameter("pricePerAcer", pricePerAcer );
//        query2.setParameter("totalPriceInPurchaseArea",totalPriceInPurchaseArea);
//        query2.setParameter("landAlloteFlag",flag);
//        query2.executeUpdate();
        
        return query.executeUpdate();
    }
	
	
	@Transactional
	public Object getPlot(BigInteger meetingId) {
		String plotIdQuery = "SELECT STRING_AGG(CONCAT('''', plot_no, ''''), ',') AS plotCode FROM application.meeting_schedule_applicant WHERE status = '0' AND meeting_schedule_id = :meetingId";
		return entityManager.createNativeQuery(plotIdQuery).setParameter("meetingId", meetingId).getSingleResult();
	}
}
