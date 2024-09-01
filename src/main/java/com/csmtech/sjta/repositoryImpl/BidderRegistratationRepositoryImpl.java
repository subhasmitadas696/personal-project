package com.csmtech.sjta.repositoryImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.repository.BidderRegistratationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class BidderRegistratationRepositoryImpl implements BidderRegistratationRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@Transactional
	@Override
	public List<Object[]> getBidderFormMApplicationById(BigInteger bidderFormMApplicationId) {
		String sqlQuery = "SELECT " + "bidder_form_m_application_id, " + "pan_card_document, "
				+ "uploaded_aadhar_card, " + "uploded_resent_signature, " + "uploded_resent_photo_bidder, "
				+ "aadhar_number, " + "user_id, " + "pan_number, "
				+ "(SELECT state_name FROM public.m_state WHERE state_id=CAST(curr_state AS INTEGER) LIMIT 1) AS state_name, "
				+ "(SELECT district_name FROM public.m_district WHERE district_name=curr_district LIMIT 1) AS district_name, "
				+ "(SELECT block_name FROM public.m_block WHERE block_name=curr_block LIMIT 1) AS block_name, "
				+ "(SELECT gp_name FROM public.m_gp WHERE gp_name=curr_gp LIMIT 1) AS gp_name, "
				+ "(SELECT village_name FROM public.m_village_master WHERE village_name=curr_village LIMIT 1) AS village_name, "
				+ " (select police_station_name from public.m_police_station where police_station_name= curr_police_station LIMIT 1)  as curr_police_station ,"
				+ " " + "curr_post_office, " + "curr_address_line_1, " + "curr_address_line_2, " + "curr_pin_code, "
				+ "(SELECT state_name FROM public.m_state WHERE state_id=CAST(pre_state AS INTEGER) LIMIT 1) AS pre_state_name, "
				+ "(SELECT district_name FROM public.m_district WHERE district_name=pre_district LIMIT 1) AS pre_district_name, "
				+ "(SELECT block_name FROM public.m_block WHERE block_name=pre_block LIMIT 1) AS pre_block_name, "
				+ "(SELECT gp_name FROM public.m_gp WHERE gp_name=pre_gp LIMIT 1) AS pre_gp_name, "
				+ "(SELECT village_name FROM public.m_village_master WHERE village_name=pre_village LIMIT 1) AS pre_village_name, "
				+ " (select police_station_name from public.m_police_station where police_station_name = pre_police_station LIMIT 1)  as pre_police_station , "
				+ "pre_post_office, " + "per_address_line_1, " + "per_address_line_2, " + "pre_pin_code ,"
				+ " approval_status," + "bidder_form_m_application_number, "
				+ " (Select full_name from citizen_profile_details where citizen_profile_details_id = bfma.user_id LIMIT 1) AS user_name , tender_auction_id    "
				+ "FROM application.bidder_form_m_application bfma  "
				+ "WHERE bfma.bidder_form_m_application_id = :bidderFormMApplicationId "
				+ "AND bfma.deleted_flag = '0' ";

		Query query = entity.createNativeQuery(sqlQuery);
		query.setParameter("bidderFormMApplicationId", bidderFormMApplicationId);

		@SuppressWarnings("unchecked")
		List<Object[]> result = query.getResultList();

		if (!result.isEmpty()) {
			return result;
		}

		return null;
	}

	@Transactional
	@Override
	public List<Object[]> getAuctionPlotData(Integer pageNumber, Integer pageSize,BigInteger userId) {
		String sqlQuery = " select * from (select ta.tender_auction_id,ap.auction_plot_id, (select district_name  "
				+ "from  land_bank.district_master   where  district_code=ap.district_code LIMIT 1) as district_code, "
				+ "(select  tahasil_name   from  land_bank.tahasil_master   where "
				+ "tahasil_code=ap.tahasil_code LIMIT 1)as tahasil_code,       (select "
				+ "village_name from land_bank.village_master where village_code=ap.village_code LIMIT 1)as village_code, "
				+ "(select  khata_no from land_bank.khatian_information where  khatian_code=ap.khatian_code LIMIT 1)as khatian_code, "
//				+ "(select plot_no from land_bank.plot_information pii "
//				+ "JOIN application.auction_plot_details apd  ON(apd.auction_plot_id=ap.auction_plot_id) "
//				+ "where pii.plot_code=apd.plot_code LIMIT 1)as plot_no, "
                +" (select plot_no from land_bank.plot_information where plot_code=apd.plot_code) as plot_no, "
				+ "(select area_acre from land_bank.plot_information pi join "
				+ "application.auction_plot_details apd  " + "on(apd.auction_plot_id=ap.auction_plot_id)   "
				+ "where apd.plot_code=pi.plot_code LIMIT 1) as total_area, "
				+ "ta.form_m_submit_start_date, ta.form_m_submit_end_date , ta.form_m_submit_start_date <= NOW()  "
				+ "AND NOW() <= ta.form_m_submit_end_date AS dateStatus, " 
				+ "ta.auction_number_gen, "
				+ " apd.plot_code as plot_code "
				+ "from application.auction_plot ap  "
				+" JOIN application.auction_plot_details apd  ON(apd.auction_plot_id=ap.auction_plot_id) "
				+ "join application.tender_auction ta  on(ta.auction_plot_id=apd.auction_plot_details_id ) "
				+ " where ap.deleted_flag='0'  " 
				+ "AND ap.auction_flag='2'  " 
				+ "AND ap.approve_status='A'    "
				+ "AND ta.deleted_flag=false  " 
				+ "AND ta.publish_status='1' AND NOW() <= ta.form_m_submit_end_date AND NOW() >= ta.form_m_submit_start_date  "
				+ "UNION  "
				+"select "
				+ " DISTINCT  ON(ta.tender_auction_id)  ta.tender_auction_id,  "
				+ "ap.auction_plot_id, (select district_name  "
				+ "from  land_bank.district_master   where  district_code=ap.district_code LIMIT 1) as district_code, "
				+ "(select  tahasil_name   from  land_bank.tahasil_master   where "
				+ "tahasil_code=ap.tahasil_code LIMIT 1)as tahasil_code,       (select "
				+ "village_name from land_bank.village_master where village_code=ap.village_code LIMIT 1)as village_code, "
				+ "(select  khata_no from land_bank.khatian_information where  khatian_code=ap.khatian_code LIMIT 1)as khatian_code, "
				+ "(select plot_no from land_bank.plot_information pii "
				+ "JOIN application.auction_plot_details apd  ON(apd.auction_plot_id=ap.auction_plot_id) "
				+ "where pii.plot_code=apd.plot_code LIMIT 1)as plot_code, "
				+ "(select area_acre from land_bank.plot_information pi join "
				+ "application.auction_plot_details apd  " + "on(apd.auction_plot_id=ap.auction_plot_id)   "
				+ "where apd.plot_code=pi.plot_code LIMIT 1) as total_area, "
				+ "ta.form_m_submit_start_date, ta.form_m_submit_end_date , ta.form_m_submit_start_date <= NOW()  "
				+ "AND NOW() <= ta.form_m_submit_end_date AS dateStatus, " 
				+ "ta.auction_number_gen ,apd.plot_code as plotcode "
				+ "from application.auction_plot ap  "
				+ "JOIN application.auction_plot_details apd ON(apd.auction_plot_id=ap.auction_plot_id) "
				+ "join application.tender_auction ta  on(ta.auction_plot_id=apd.auction_plot_details_id )"
				+ "JOIN application.land_allotement_for_auction lafa ON(lafa.plot_code=apd.plot_code) "
				+ "JOIN application.meeting_schedule ms ON(ms.meeting_schedule_id=lafa.meeting_schedule_id) "
				+ "JOIN application.meeting_schedule_applicant msa ON(msa.meeting_schedule_id=ms.meeting_schedule_id) "
				+ "JOIN public.land_application la ON(la.land_application_id=msa.land_application_id)  "
				+ "where ap.deleted_flag='0' AND ap.auction_flag='2' AND ap.approve_status='A'  "
				+ "AND ta.deleted_flag=false AND la.created_by=:userId   "
				+ "AND (SELECT approval_status FROM application.meeting_schedule_applicant msa1  "
				+ "JOIN application.meeting_schedule ms1 ON(ms1.meeting_schedule_id=msa1.meeting_schedule_id)  "
				+ "where msa1.land_application_id=msa.land_application_id and msa1.status='0'  "
				+ "AND ms1.meeting_schedule_id= ms.meeting_schedule_id  AND msa1.plot_no=apd.plot_code)=1   "
//				+ "AND approval_status=1 "
				+ "AND NOW() <= ta.form_m_submit_end_date AND NOW() >= ta.form_m_submit_start_date ) subquery  "
				+ "ORDER BY subquery.tender_auction_id DESC   "
				+ "LIMIT :pageSize OFFSET :offset  ";

		int offset = (pageNumber - 1) * pageSize;

		@SuppressWarnings("unchecked")
		List<Object[]> result = entity.createNativeQuery(sqlQuery).setParameter("pageSize", pageSize)
		.setParameter("userId", userId)
				.setParameter("offset", offset).getResultList();

		return result;
	}

	@Transactional
	@Override
	public BigInteger getTotalApplicantCount(BigInteger userId) {
		String countQuery = "select CAST(sum(count) as bigint)  from (select count(*) from application.auction_plot ap   \r\n"
				+ "JOIN application.auction_plot_details apd ON( apd.auction_plot_id=ap.auction_plot_id) \r\n"
				+ "join  application.tender_auction ta    on( ta.auction_plot_id=apd.auction_plot_details_id)  \r\n"
				+ "where ap.deleted_flag='0'  \r\n"
				+ "AND ap.auction_flag='2' AND ap.approve_status='A' AND ta.deleted_flag=false AND ta.publish_status='1'\r\n"
				+ "AND NOW() <= ta.form_m_submit_end_date\r\n"
				+ "UNION select count(*)  from application.auction_plot ap  \r\n"
				+ "JOIN application.auction_plot_details apd ON( apd.auction_plot_id=ap.auction_plot_id) \r\n"
				+ "join application.tender_auction ta  on( ta.auction_plot_id=apd.auction_plot_details_id)\r\n"
				+ "JOIN application.land_allotement_for_auction lafa  ON(lafa.plot_code=apd.plot_code) \r\n"
				+ "JOIN application.meeting_schedule ms ON(ms.meeting_schedule_id=lafa.meeting_schedule_id) \r\n"
				+ "JOIN application.meeting_schedule_applicant msa ON(msa.meeting_schedule_id=ms.meeting_schedule_id) \r\n"
				+ "JOIN public.land_application la ON(la.land_application_id=msa.land_application_id)  \r\n"
				+ "where ap.deleted_flag='0' AND ap.auction_flag='2'  AND ap.approve_status='A' AND ta.deleted_flag=false \r\n"
				+ "AND la.created_by=:userId AND NOW() <= ta.form_m_submit_end_date) subquery ";

		BigInteger status = (BigInteger) entity.createNativeQuery(countQuery).setParameter("userId", userId).getSingleResult();
		entity.close();
		return status;
	}

	@Transactional
	@Override
	public BigInteger getCountDuplicateApplication(BigInteger tenderAuctionId, BigInteger createdBy) {
		String countQuery = "  " + "        SELECT " + "            count(*) " + "        FROM "
				+ "application.bidder_form_m_application bfma "
				+ "JOIN application.tender_auction ta ON(ta.tender_auction_id=bfma.tender_auction_id) "
				+ "WHERE bfma.tender_auction_id= :tenderAuctionId " + "	   AND bfma.created_by= :createdBy "
				+ "AND bfma.deleted_flag=B'0' AND ta.deleted_flag=false  ";

		BigInteger status = (BigInteger) entity.createNativeQuery(countQuery)
				.setParameter("tenderAuctionId", tenderAuctionId).setParameter("createdBy", createdBy)
				.getSingleResult();
		entity.close();
		return status;
	}

	@Transactional
	@Override
	public Boolean getCountDateAndTimeValidation(BigInteger auctionTenderId) {
		String countQuery = " SELECT " + "ta.form_m_submit_end_date < NOW() AS is_expired " + "FROM "
				+ "application.tender_auction ta " + "WHERE " + "ta.tender_auction_id= :auctionTenderId "
				+ "AND ta.deleted_flag=false ";

		Boolean status = (Boolean) entity.createNativeQuery(countQuery).setParameter("auctionTenderId", auctionTenderId)
				.getSingleResult();
		entity.close();
		return status;
	}

	@Transactional
	@Override
	public List<Object[]> getAmountRecord(BigInteger tenderAuctionId,String plotCode) {
		String updateFlag=null;
		String sqlQuery="";
		String addCondition="";
		if(plotCode!=null) {
			addCondition="AND apd.plot_code= '"+plotCode+"'";
		}else {
			addCondition="";
		}
		String nativeQuery = "SELECT CAST(apd.aucion_tender_creation_flag as varchar)  "
				+ "FROM application.tender_auction ta  "
				+"JOIN application.auction_plot_details apd  ON(apd.auction_plot_details_id=ta.auction_plot_id)  "
				+ "JOIN application.auction_plot ap  ON ap.auction_plot_id = apd.auction_plot_id  "
//				+ "JOIN  application.auction_plot_details apd  ON apd.auction_plot_id = ta.auction_plot_id "
				+ "WHERE tender_auction_id =:tenderAuctionId   AND  ta.deleted_flag='0' "
				+ "AND apd.deleted_flag='0' "+addCondition
				+ "  LIMIT 1 ";

			Query queryflag = entity.createNativeQuery(nativeQuery);
			queryflag.setParameter("tenderAuctionId", tenderAuctionId);
//			.setParameter("plotCode", plotCode);
			try {
				updateFlag = (String) queryflag.getSingleResult();
			} catch (NoResultException e) {
				updateFlag = null;
			}

			if (updateFlag.equalsIgnoreCase("1")) {
				sqlQuery = "  SELECT tender_auction_id, application_fee_not_refund, secutity_amount_deposite FROM application.tender_auction ta WHERE "
						+ "  ta.tender_auction_id = :tenderAuctionId AND ta.deleted_flag = false  ";
			} else {
				sqlQuery = "  SELECT tender_auction_id, application_fee_not_refund, secutity_amount_deposite FROM application.tender_auction ta WHERE "
						+ "  ta.tender_auction_id = :tenderAuctionId AND ta.deleted_flag = false AND ta.publish_status = '1' ";
			}

			Query query = entity.createNativeQuery(sqlQuery).setParameter("tenderAuctionId", tenderAuctionId);
			@SuppressWarnings("unchecked")
			List<Object[]> results = query.getResultList();
			return results;
	}

	@Transactional
	@Override
	public String updateBidderFormMApplication(Integer appplicationId, String uniqueNumber) {
		String sqlQuery = "UPDATE application.bidder_form_m_application "
				+ "SET bidder_form_m_application_number = :uniqueNumber "
				+ "WHERE bidder_form_m_application_id = :appplicationId "
				+ " returning bidder_form_m_application_number ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("appplicationId", appplicationId)
				.setParameter("uniqueNumber", uniqueNumber);
		return (String) query.getSingleResult();
	}

	@Transactional
	@Override
	public Map<String, Object> getTenderAuctionDates(BigInteger appId) {
		Query query = entity
				.createNativeQuery(" WITH TenderId AS ( " + "  SELECT tender_auction_id "
						+ "  FROM application.bidder_form_m_application "
						+ "  WHERE bidder_form_m_application_id = :fromMApplicatonId " + ") "
						+ "SELECT ta.form_m_submit_start_date, ta.form_m_submit_end_date " + "FROM TenderId "
						+ "JOIN application.tender_auction ta ON TenderId.tender_auction_id = ta.tender_auction_id")
				.setParameter("fromMApplicatonId", appId);

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.getResultList();

		if (resultList != null && !resultList.isEmpty()) {
			Map<String, Object> result = new HashMap<>();
			Object[] row = resultList.get(0);
			result.put("form_m_submit_start_date", row[0]);
			result.put("form_m_submit_end_date", row[1]);
			return result;
		} else {
			return null;
		}
	}

	@Transactional
	@Override
	public List<Object[]> getExpiredBidderFormApplications(Pageable pageSize) {
		Integer pageSizes = pageSize.getPageSize();
		Query query = entity
				.createNativeQuery("SELECT " + "  bfma.contact_person_name, " + "  bfma.pan_number, "
						+ "  bfma.aadhar_number, " + "  ta.form_m_submit_end_date < NOW() AS is_expired "
						+ "FROM application.tender_auction ta " + "JOIN application.bidder_form_m_application bfma "
						+ "ON (bfma.tender_auction_id = ta.tender_auction_id) "
						+ "WHERE ta.deleted_flag = false AND bfma.deleted_flag = false "
						+ "ORDER BY bfma.bidder_form_m_application_id DESC " + "LIMIT :pageSize")
				.setParameter("pageSize", pageSizes);

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.getResultList();
		return resultList;
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<Object[]> getTenderNames(Pageable pageRequest) {
		String sqlQuery = "SELECT ta.tender_auction_id, ta.auction_name,"
				+ "     (select(CAST(SUM(CASE WHEN bfma.deleted_flag = false THEN 1 ELSE 0 END) AS varchar)) as total_applicant), "
				+ "(select(CAST(SUM(CASE WHEN bfma.deleted_flag = false AND bfma.approval_status = 'A' THEN 1 ELSE 0 END) AS varchar)) as approval_applicant), "
				+ "(select(CAST(SUM(CASE WHEN bfma.deleted_flag = false AND bfma.approval_status = 'R' THEN 1 ELSE 0 END) AS varchar)) as rejectApplicant) "
				+ "    FROM application.tender_auction ta "
				+ "  JOIN application.bidder_form_m_application bfma ON(bfma.tender_auction_id=ta.tender_auction_id)  "
				+ "  WHERE  " + "  ta.deleted_flag = false  " + "  GROUP BY ta.tender_auction_id limit :pageSize ";
		Query nativeQuery = entity.createNativeQuery(sqlQuery).setParameter("pageSize", pageRequest.getPageSize());
		return nativeQuery.getResultList();
	}

	@Transactional
	@Override
	public BigInteger getTenderCount() {
		String sqlQuery = "SELECT count(*) FROM application.tender_auction ta "
				+ " JOIN application.bidder_form_m_application bfma ON(bfma.tender_auction_id=ta.tender_auction_id) "
				+ " WHERE ta.deleted_flag = false AND ta.publish_status = '1' ";
		Query nativeQuery = entity.createNativeQuery(sqlQuery);
		return ((BigInteger) nativeQuery.getSingleResult());
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Object[]> getBidderFormApplicationData(BigInteger tenderAuctionId, Pageable pageRequest) {
		String sqlQuery = " SELECT bidder_form_m_application_id, "
				+ "  contact_person_name, bidder_form_m_application_number, " + " CASE "
				+ "     WHEN approval_status = 'A' THEN 'Approved' " + "    WHEN approval_status = 'R' THEN 'Rejected' "
				+ "    ELSE 'Pending Action' " + " END AS approval_status, " + " approval_remarks "
				+ " FROM application.bidder_form_m_application " + " WHERE tender_auction_id = :tenderAuctionId "
				+ " AND deleted_flag=false " + " order by contact_person_name " + "limit :pageSize  ";

		Query nativeQuery = entity.createNativeQuery(sqlQuery).setParameter("tenderAuctionId", tenderAuctionId)
				.setParameter("pageSize", pageRequest.getPageSize());

		return nativeQuery.getResultList();
	}

	@Transactional
	@Override
	public BigInteger getTenderCountApplicant(BigInteger tenderAuctionId) {
		String sqlQuery = "SELECT count(*) FROM application.tender_auction ta "
				+ " JOIN application.bidder_form_m_application bfma ON(bfma.tender_auction_id=ta.tender_auction_id) "
				+ " WHERE ta.deleted_flag = false AND ta.publish_status = '1' AND  bfma.tender_auction_id = :tenderAuctionId ";
		Query nativeQuery = entity.createNativeQuery(sqlQuery).setParameter("tenderAuctionId", tenderAuctionId);
		return ((BigInteger) nativeQuery.getSingleResult());
	}

	@Transactional
	@Override
	public Integer updateBidderFormApplication(BigInteger bidderFormApplicationId, String remerk, String status) {
		String sqlQuery = " UPDATE application.bidder_form_m_application "
				+ " SET approval_datetime = CURRENT_TIMESTAMP, " + "    approval_remarks = :remerk, "
				+ "    approval_status = :status " + " WHERE bidder_form_m_application_id = :bidderFormApplicationId";

		return entity.createNativeQuery(sqlQuery).setParameter("bidderFormApplicationId", bidderFormApplicationId)
				.setParameter("status", status).setParameter("remerk", remerk).executeUpdate();
	}

	@Transactional
	@Override
	public Integer markBidderFormMApplicationAsDeleted(BigInteger bidderFormMApplicationId) {
		String queryString = "UPDATE application.bidder_form_m_application SET deleted_flag = '1' WHERE bidder_form_m_application_id = :id";
		return entity.createNativeQuery(queryString).setParameter("id", bidderFormMApplicationId).executeUpdate();

	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Object[]> getAuctions(String data) throws JsonMappingException, JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		JsonNode jsonNode = om.readTree(data);
		String sql = " SELECT auction_number_gen, auction_name, slot_for_auction_from_date, "
				+ " slot_for_auction_to_date, TO_CHAR(slot_for_auction_from_date, 'HH12:MI:SS AM') as auction_start_time, "
				+ " TO_CHAR(slot_for_auction_to_date, 'HH12:MI:SS AM') as auction_end_time, "
				+ " royality, la.base_price, la.highest_bid_price, "
				+ " CASE WHEN CURRENT_TIMESTAMP BETWEEN slot_for_auction_from_date AND slot_for_auction_to_date THEN 'true' ELSE 'false' END AS now "
				+ " , ta.tender_auction_id , " + " form_m_submit_start_date, " + "  form_m_submit_end_date ,"
				+ " (select count(*) " + "from application.live_auction_log "
				+ "where tender_auction_id=ta.tender_auction_id " + "AND deleted_flag='0') as bddingNumber  "
				+ " FROM application.tender_auction ta "
				+ " JOIN application.live_auction la ON(ta.tender_auction_id=la.tender_auction_id) "
				+ " JOIN application.bidder_form_m_application ma ON ma.tender_auction_id=ta.tender_auction_id "
				+ " WHERE  ta.deleted_flag='0'  " 
				+ " AND ma.user_id= :userId " //publish_status='1' AND
				+ " AND ma.approval_status='A'  "
				+ " AND NOW() BETWEEN ta.slot_for_auction_from_date AND ta.slot_for_auction_to_date "
				+ "	ORDER BY la.live_auction_id DESC ";

		return entity.createNativeQuery(sql).setParameter("userId", jsonNode.get("intId").asInt()).getResultList();
	}

	@Transactional
	@Override
	public Boolean checkAuctionStatusValid(BigInteger tenderAuctionId) {
		String sql = " SELECT CASE WHEN CURRENT_TIMESTAMP BETWEEN "
				+ " slot_for_auction_from_date AND slot_for_auction_to_date THEN 'true' ELSE 'false' END AS now "
				+ " FROM application.tender_auction ta "
				+ " WHERE  ta.deleted_flag='0' AND ta.tender_auction_id=:tenderAuctionId"; //publish_status='1' AND

		Query query = entity.createNativeQuery(sql);
		query.setParameter("tenderAuctionId", tenderAuctionId);

		Object result = query.getSingleResult();
		return "true".equals(result);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Object[]> getAuctionDetails(BigInteger tenderAuctionId) {
		String sql = " SELECT " + "auction_number_gen, " + "auction_name, " + "auction_name as source, "
				+ " slot_for_auction_from_date, " + "(select area_acre from application.auction_plot_details apd "
				+ " JOIN land_bank.plot_information pi ON(pi.plot_code=apd.plot_code) "
				+ " where auction_plot_id=auction_plot_id LIMIT 1) as area_acer ,"
				+ " slot_for_auction_to_date , royality , " + " (select \r\n"
				+ "(SELECT district_name FROM land_bank.district_master \r\n"
				+ "WHERE district_code = ap.district_code LIMIT 1)\r\n" + "		 || ' - ' ||\r\n"
				+ "(SELECT tahasil_name FROM land_bank.tahasil_master \r\n"
				+ "WHERE tahasil_code = ap.tahasil_code LIMIT 1) || ' - ' || \r\n"
				+ "(SELECT village_name FROM land_bank.village_master \r\n"
				+ "WHERE village_code = ap.village_code LIMIT 1) || ' - ' ||\r\n"
				+ "(SELECT khata_no FROM land_bank.khatian_information \r\n"
				+ "WHERE khatian_code = ap.khatian_code LIMIT 1)|| ' - ' ||\r\n"
				+ "(SELECT plot_no FROM land_bank.plot_information \r\n" + "WHERE plot_code=apd.plot_code LIMIT 1)\r\n"
				+ "from application.auction_plot ap \r\n"
				+ "JOIN application.auction_plot_details apd ON(ap.auction_plot_id=apd.auction_plot_id)\r\n"
				+ "where apd.auction_plot_details_id=ta.auction_plot_id LIMIT 1) as combineValue "
				+ " FROM application.tender_auction ta "
				+ " WHERE  ta.deleted_flag='0' AND ta.tender_auction_id=:tenderAuctionId"; //publish_status='1' AND

		Query query = entity.createNativeQuery(sql);
		query.setParameter("tenderAuctionId", tenderAuctionId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Object[]> getBidderFormApplicationData(Integer limit, Integer offset, String approvalStatus) {
		String sqlQuery = " SELECT bidder_form_m_application_id, pan_number, aadhar_number, "
				+ "(SELECT full_name FROM citizen_profile_details WHERE citizen_profile_details_id = bfma.user_id LIMIT 1) as userName, "
				+ "bidder_form_m_application_number ,approval_remarks " 
				+ "FROM application.bidder_form_m_application bfma "
				+ "WHERE deleted_flag = '0'  " + "AND payment_status='1' " + "AND approval_status=:approvalStatus "
				+ " ORDER BY bidder_form_m_application_id DESC offset :offset limit :limit  ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("approvalStatus", approvalStatus)
				.setParameter("limit", limit).setParameter("offset", offset);
		return query.getResultList();
	}

	@Transactional
	@Override
	public Integer insertPaymentTransaction(BigInteger landApplicantId, String orderId, String paymentSignature,
			String paymentId, String paymentStatus, BigDecimal tranctionAmount,String reciptNo) {
		String nativeQuery = " INSERT INTO application.from_m_application_payment_details "
				+ " (from_m__application_id, order_id, payment_signature, payment_id, payment_status, created_by,tranction_amount,receipt_no) "
				+ " VALUES (?, ?, ?, ?, ?,  "
				+ " currval(pg_get_serial_sequence('application.from_m_application_payment_details',"
				+ "  'from_m_application_payment_tranction_details_id')),?,? )";

		String updatePaymentStatus = "update application.bidder_form_m_application " + "set payment_status='1' "
				+ "where bidder_form_m_application_id= ? ";
		if (paymentId != null && orderId != null) {
			entity.createNativeQuery(updatePaymentStatus).setParameter(1, landApplicantId).executeUpdate();
		}

		log.info(":: insertPaymentTransaction() method insert the tranction record Success.!!");
		return entity.createNativeQuery(nativeQuery).setParameter(1, landApplicantId).setParameter(2, orderId)
				.setParameter(3, paymentSignature).setParameter(4, paymentId).setParameter(5, paymentStatus)
				.setParameter(6, tranctionAmount).setParameter(7, reciptNo).executeUpdate();
	}

	@Transactional
	@Override
	public BigInteger getCountForTenderAuction(BigInteger tenderAuctionId) {
		String nativeQuery = " SELECT COUNT(*) FROM application.tender_auction  "
				+ "WHERE  tender_auction_id =?   AND form_m_submit_end_date < NOW()  "
				+ "AND date_of_techinical_evaluation > NOW()  ";
		Query query = entity.createNativeQuery(nativeQuery);
		query.setParameter(1, tenderAuctionId);

		BigInteger result = (BigInteger) query.getSingleResult();
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public BigInteger getAuctionsCount(String data) throws JsonMappingException, JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		JsonNode jsonNode = om.readTree(data);
		String sql = " select count(*) from (SELECT auction_number_gen, auction_name, slot_for_auction_from_date, "
				+ " slot_for_auction_to_date, TO_CHAR(slot_for_auction_from_date, 'HH12:MI:SS AM') as auction_start_time, "
				+ " TO_CHAR(slot_for_auction_to_date, 'HH12:MI:SS AM') as auction_end_time, "
				+ " royality, la.base_price, la.highest_bid_price, "
				+ " CASE WHEN CURRENT_TIMESTAMP BETWEEN slot_for_auction_from_date AND slot_for_auction_to_date THEN 'true' ELSE 'false' END AS now "
				+ " , ta.tender_auction_id , " + " form_m_submit_start_date, " + "  form_m_submit_end_date ,"
				+ " (select count(*) " + "from application.live_auction_log "
				+ "where tender_auction_id=ta.tender_auction_id " + "AND deleted_flag='0') as bddingNumber  "
				+ " FROM application.tender_auction ta "
				+ " JOIN application.live_auction la ON(ta.tender_auction_id=la.tender_auction_id) "
				+ " JOIN application.bidder_form_m_application ma ON ma.tender_auction_id=ta.tender_auction_id "
				+ " WHERE  ta.deleted_flag='0'  " 
				+ " AND ma.user_id= :userId " 
				+ " AND ma.approval_status='A'  "
				+ " AND NOW() BETWEEN ta.slot_for_auction_from_date AND ta.slot_for_auction_to_date "
				+ "	ORDER BY la.live_auction_id DESC) as subquery ";

		return (BigInteger) entity.createNativeQuery(sql).setParameter("userId", jsonNode.get("intId").asInt()).getSingleResult();
	}

}
