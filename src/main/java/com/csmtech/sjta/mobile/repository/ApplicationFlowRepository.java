package com.csmtech.sjta.mobile.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.mobile.entity.ApplicationFlowEntity;

public interface ApplicationFlowRepository extends JpaRepository<ApplicationFlowEntity, BigInteger> {

	List<ApplicationFlowEntity> findByLandApplicationId(BigInteger applicantId);

	@Query(value = "select ms.meeting_schedule_id,msa.meeting_schedule_applicant_id,msa.land_application_id "
			+ "from application.meeting_schedule ms inner join application.meeting_schedule_applicant msa "
			+ "on ms.meeting_schedule_id = msa.meeting_schedule_id "
			+ "where ms.meeting_schedule_id =:meetingScheduleId order by ms.meeting_schedule_id", nativeQuery = true)
	List<Object[]> fetchRequiredMeetingDetails(BigInteger meetingScheduleId);

	@Query(value = "select ls.land_application_id from application.auction_plot ap "
			+ " LEFT JOIN application.auction_plot_details apd ON(apd.auction_plot_id=ap.auction_plot_id) "
			+ " LEFT JOIN land_schedule ls ON(ls.plot_code = apd.plot_code) "
			+ " WHERE ap.auction_plot_id =:auctionPlotId ", nativeQuery = true)
	List<BigInteger> findLandAppIds(Integer auctionPlotId);

	@Query(value = "select auction_plot_id from application.tender_auction where tender_auction_id =:parentId ", nativeQuery = true)
	BigInteger findAuctionPlotId(BigInteger parentId);

	@Query(value = "select tender_auction_id from application.bidder_form_m_application where bidder_form_m_application_id =:parentId ", nativeQuery = true)
	BigInteger findTenderAuctionId(BigInteger parentId);
	
	@Query(value = "select tender_auction_id from application.live_auction where live_auction_id =:parentId ", nativeQuery = true)
	BigInteger findTenderAuctionIdLiveAuction(BigInteger parentId);

}
