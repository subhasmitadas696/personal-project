package com.csmtech.sjta.repositoryImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.TenderDTO;
import com.csmtech.sjta.repository.TenderAuctionRepository;
import lombok.extern.slf4j.Slf4j;



@Repository
@Slf4j
public class TenderAuctionRepositoryImpl implements TenderAuctionRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAuctions(Integer pageNumber, Integer pageSize) {
		String nativeQuery = "select  DISTINCT ON(ta.tender_auction_id) ta.tender_auction_id, ta.auction_plot_id,(SELECT CAST(  (SELECT  district_name "
				+ "FROM land_bank.district_master WHERE  district_code = ap.district_code LIMIT 1) || ' - ' ||  (SELECT tahasil_name "
				+ "FROM land_bank.tahasil_master WHERE  tahasil_code = ap.tahasil_code LIMIT 1) || ' - ' ||  (SELECT  village_name FROM "
				+ "land_bank.village_master WHERE  village_code = ap.village_code LIMIT 1) || ' - ' ||  (SELECT  khata_no FROM land_bank.khatian_information WHERE "
				+ "khatian_code = ap.khatian_code LIMIT 1) || ' - ' || (select plot_no from land_bank.plot_information where plot_code=apd.plot_code LIMIT 1) "
				+ "AS character varying) AS combined_values FROM application.auction_plot_details apd1  WHERE "
				+ "apd1.auction_plot_details_id = ta.auction_plot_id) AS auction_id_name,     lease_period_years ,     committee_member_name  ,"
				+ "royality ,form_m_submit_start_date ,form_m_submit_end_date ,secuirity_deposit_start_date ,secuirity_deposit_end_date , "
				+ "bid_document_downlode_start_date ,    bid_document_downlode_end_date ,  date_of_techinical_evaluation ,application_fee_not_refund ,secutity_amount_deposite , "
				+ "slot_for_auction_from_date ,     slot_for_auction_to_date, auction_name,(SELECT STRING_AGG(full_name, ', ') FROM user_details WHERE "
				+ "user_id IN ( SELECT  CAST(UNNEST(string_to_array(committee_member_name, ',')) AS INTEGER)  AS converted_value FROM application.tender_auction as ata "
				+ "WHERE ata.tender_auction_id = ta.tender_auction_id)) as username ,publish_status, ap.district_code, ap.tahasil_code, ap.village_code,(SELECT  khata_no FROM land_bank.khatian_information WHERE \r\n"
				+ " khatian_code = ap.khatian_code LIMIT 1), apd.go_for_auction_process_flag, apd.auction_plot_details_id "
				+ " FROM application.tender_auction ta"
//				+ " INNER JOIN application.auction_plot ap ON ta.auction_plot_id = ap.auction_plot_id "
				+ " INNER JOIN application.auction_plot_details apd ON(apd.auction_plot_details_id=ta.auction_plot_id) "
				+ " INNER JOIN application.auction_plot ap ON apd.auction_plot_id = ap.auction_plot_id " 
//				+ "JOIN application.land_allotement_for_auction lafa ON(lafa.plot_code=apd.plot_code)  "
				+ " WHERE ta.deleted_flag = false ORDER BY ta.tender_auction_id DESC " + "LIMIT :pageSize OFFSET :offset ";
		Integer offset = (pageNumber - 1) * pageSize;
		Query query = entity.createNativeQuery(nativeQuery).setParameter("pageSize", pageSize).setParameter("offset",
				offset);
		return query.getResultList();

	}

	@Transactional
	@Override
	public BigInteger getActiveAuctionCount() {
		String nativeQuery = "SELECT COUNT(*) FROM application.tender_auction ta "
//				+ " INNER JOIN application.auction_plot ap ON ta.auction_plot_id = ap.auction_plot_id  "
				+ " INNER JOIN application.auction_plot_details apd ON(apd.auction_plot_details_id=ta.auction_plot_id)  "
				+ " INNER JOIN application.auction_plot ap ON apd.auction_plot_id = ap.auction_plot_id" 
//				+ "	JOIN application.land_allotement_for_auction lafa ON(lafa.plot_code=apd.plot_code)  "
				+ "	WHERE ta.deleted_flag = false  ";

		Query query = entity.createNativeQuery(nativeQuery);
		entity.close();
		return ((BigInteger) query.getSingleResult());
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAuctionsGetId(BigInteger tenderId) {
		String nativeQuery = " select "
				+ "tender_auction_id, "
				+ "ta.auction_plot_id, "
				+ "(  SELECT "
				+ "CAST(  (SELECT "
				+ "district_name               "
				+ "FROM "
				+ "land_bank.district_master               "
				+ "WHERE "
				+ "district_code = ap.district_code LIMIT 1) || ' - ' ||  (SELECT "
				+ "tahasil_name               "
				+ "FROM land_bank.tahasil_master               "
				+ "WHERE "
				+ "tahasil_code = ap.tahasil_code LIMIT 1) || ' - ' ||  (SELECT "
				+ "village_name               "
				+ "FROM "
				+ "land_bank.village_master               "
				+ "WHERE "
				+ "village_code = ap.village_code LIMIT 1) || ' - ' ||  (SELECT "
				+ "khata_no               "
				+ "FROM "
				+ "land_bank.khatian_information               "
				+ "WHERE "
				+ "khatian_code = ap.khatian_code LIMIT 1)    ||' - '|| (select plot_no from land_bank.plot_information  where plot_code=apd1.plot_code) AS character varying ) AS combined_values            "
				+ "FROM "
				+ "application.auction_plot_details apd1               "
				+ "WHERE "
				+ "apd1.auction_plot_details_id = ta.auction_plot_id            "
				+ ") AS auction_id_name,   "
				+ "  lease_period_years ,     committee_member_name  ,     royality ,  form_m_submit_start_date ,     form_m_submit_end_date ,  secuirity_deposit_start_date ,     secuirity_deposit_end_date e,  bid_document_downlode_start_date ,     bid_document_downlode_end_date ,  date_of_techinical_evaluation ,     application_fee_not_refund ,  secutity_amount_deposite ,     slot_for_auction_from_date ,  slot_for_auction_to_date, auction_name ,  CAST(( SELECT "
				+ "STRING_AGG(full_name, "
				+ "', ')                                           "
				+ "FROM "
				+ "user_details                                           "
				+ "WHERE "
				+ "user_id IN (SELECT "
				+ "CAST(UNNEST(string_to_array(committee_member_name, "
				+ "',')) AS INTEGER)  AS converted_value                                                                "
				+ "FROM "
				+ "application.tender_auction as ata                                                             "
				+ "WHERE "
				+ "ata.tender_auction_id = ta.tender_auction_id                                          "
				+ "))  AS VARCHAR)as username   , apd.auction_plot_details_id ,apd.go_for_auction_process_flag ,  "
				+ "(SELECT (SELECT string_agg(full_name, ', ')FROM public.user_details  "
				+ "WHERE user_id = ANY(CAST(string_to_array(committee_member_name, ',') AS int[])))  AS user_names  "
				+ "FROM application.tender_auction ta1 WHERE ta1.deleted_flag = '0' AND ta1.tender_auction_id = ta.tender_auction_id)  "
				+ "FROM "
				+ "application.tender_auction ta       "
				+ "JOIN application.auction_plot_details apd  ON(apd.auction_plot_details_id=ta.auction_plot_id) "
				+ "INNER JOIN "
				+ "application.auction_plot ap               "
				+ "ON apd.auction_plot_id = ap.auction_plot_id "
				+ "WHERE "
				+ "ta.deleted_flag = false   "
				+ " AND tender_auction_id=:tender_auction_id";

		Query query = entity.createNativeQuery(nativeQuery).setParameter("tender_auction_id", tenderId);
		return query.getResultList();
	}

	@Transactional
	@Override
	public Integer getPublicApproval(BigInteger tenderId) {
		String sql = "UPDATE application.tender_auction " + "   SET publish_status = B'1' "
				+ "   WHERE tender_auction_id =:tenderAuctionId ";
		return entity.createNativeQuery(sql).setParameter("tenderAuctionId", tenderId).executeUpdate();

	}

	@Transactional
	@Override
	public Integer insertLiveAuctionData(BigInteger tenderId, BigInteger createdBy) {
		String nativeQuery = "INSERT INTO application.live_auction (auction_plot_details_id, tender_auction_id, base_price, created_by) "
				+ "SELECT ta.auction_plot_id, ta.tender_auction_id, ta.royality, :createdBy "
				+ "FROM application.tender_auction ta " +
//                             "JOIN application.auction_plot_details apd ON ta.auction_plot_id = apd.auction_plot_id " +
				"WHERE ta.tender_auction_id = :tenderId";

		String retriveAuctionNotification="select(SELECT CAST((SELECT  district_name  "
				+ "FROM land_bank.district_master WHERE  district_code = dm1.district_code LIMIT 1) || ' - ' ||  (SELECT tahasil_name  "
				+ "FROM land_bank.tahasil_master WHERE  tahasil_code = tm1.tahasil_code LIMIT 1) || ' - ' ||  (SELECT  village_name FROM  "
				+ "land_bank.village_master WHERE  village_code = vm1.village_code LIMIT 1) || ' - ' ||  (SELECT  khata_no FROM land_bank.khatian_information WHERE  "
				+ "khatian_code = ki1.khatian_code LIMIT 1) || ' - ' || (select plot_no from land_bank.plot_information where plot_code=apd.plot_code LIMIT 1)  "
				+ "AS character varying) AS combined_values), "
				+ "TO_CHAR(bid_document_downlode_start_date,'DD/MM/YYYY HH:MM:SS') as bid_document_downlode_start_date , "
				+ "TO_CHAR(bid_document_downlode_end_date,'DD/MM/YYYY HH:MM:SS') as bid_document_downlode_end_date , "
				+ "TO_CHAR(form_m_submit_start_date,'DD/MM/YYYY HH:MM:SS') as form_m_submit_start_date , "
				+ "TO_CHAR(form_m_submit_end_date,'DD/MM/YYYY HH:MM:SS') as form_m_submit_end_date , "
				+ "TO_CHAR(slot_for_auction_from_date,'DD/MM/YYYY HH:MM:SS') as slot_for_auction_from_date , "
				+ "TO_CHAR(slot_for_auction_to_date,'DD/MM/YYYY HH:MM:SS') as slot_for_auction_to_date ,auction_name   "
				+ "from application.tender_auction ta  "
				+ "JOIN application.auction_plot_details apd ON(apd.auction_plot_details_id=ta.auction_plot_id) "
				+ "JOIN land_bank.plot_information pi1 ON pi1.plot_code =apd.plot_code  "
				+ "JOIN land_bank.khatian_information ki1 ON ki1.khatian_code=pi1.khatian_code  "
				+ "JOIN land_bank.village_master vm1 ON vm1.village_code=ki1.village_code  "
				+ "JOIN land_bank.tahasil_master tm1 ON tm1.tahasil_code=vm1.tahasil_code  "
				+ "JOIN land_bank.district_master dm1 ON dm1.district_code=tm1.district_code  "
				+ "where ta.tender_auction_id=:tenderId";
	   @SuppressWarnings("unchecked")
		List<Object[]> result = entity.createNativeQuery(retriveAuctionNotification).setParameter("tenderId", tenderId)
				.getResultList();
		List<TenderDTO> td = new ArrayList<>();
		for (Object[] res : result) {
			TenderDTO tender=new TenderDTO();
			tender.setCombineValue((String)res[0]);
			tender.setBiddocsStart((String)res[1]);
			tender.setBiddocsEnd((String)res[2]);
			tender.setFoemMStart((String)res[3]);
			tender.setFoemMEnd((String)res[4]);
			tender.setSlotStart((String)res[5]);
			tender.setSlotEnd((String)res[6]);
			tender.setAuctionName((String)res[7]);
			td.add(tender);
		}
		if(tenderId!=null) {
			String title="Tender for  "+td.get(0).getAuctionName() +".";
			String desc="Tender for the auction process of "+td.get(0).getCombineValue()+".  Bid document download starts form "+td.get(0).getBiddocsStart()+" to "+td.get(0).getBiddocsEnd()
					+", form m submission starts form  "+td.get(0).getFoemMStart()+" to "+td.get(0).getFoemMEnd()+", slots for auction decided from "+td.get(0).getSlotStart()+" to "+
					td.get(0).getSlotEnd() +" .";
		  String insertNotification="INSERT INTO public.m_notification (title,description,publish,created_by) VALUES (:title,:desc,'Y',:createdBy)";
		  entity.createNativeQuery(insertNotification).setParameter("title", title).setParameter("desc", desc).setParameter("createdBy",createdBy ).executeUpdate();
		  
		}
		Query query = entity.createNativeQuery(nativeQuery).setParameter("tenderId", tenderId).setParameter("createdBy",
				createdBy);
		return query.executeUpdate();
	}

	@Transactional
	@Override
	public String updateTenderAuctionAuctionNumber(BigInteger tenderAuctionId, String newAuctionNumber) {

		String updateSql = " UPDATE application.tender_auction " + " SET auction_number_gen = :newAuctionNumber "
				+ " WHERE tender_auction_id = :tenderAuctionId " + " returning auction_number_gen ";

		Query query = entity.createNativeQuery(updateSql);
		query.setParameter("newAuctionNumber", newAuctionNumber);
		query.setParameter("tenderAuctionId", tenderAuctionId);

		return (String) query.getSingleResult();

	}

	@Transactional
	@Override
	public BigInteger getAuctionCountTender(BigInteger tenderId) {
		String nativeQuery = "select count(*) from application.live_auction where tender_auction_id=:tenderId AND deleted_flag='0' ";
		Query query = entity.createNativeQuery(nativeQuery).setParameter("tenderId", tenderId);
		entity.close();
		return ((BigInteger) query.getSingleResult());
	}
	
	@Transactional
	@Override
	public BigInteger getAuctionCountTenderForPublishOrNot(BigInteger tenderId) {
		String nativeQuery = "select COUNT(*) from application.tender_auction ta where ta.tender_auction_id=:tenderId  AND ta.deleted_flag='0' "
				+ " AND bid_document_downlode_end_date <= NOW()  ";
		Query query = entity.createNativeQuery(nativeQuery).setParameter("tenderId", tenderId);
		entity.close();
		return ((BigInteger) query.getSingleResult());
	}

	@Transactional
	@Override
	public Integer getUnPublicApproval(BigInteger tenderId) {
		String sql = "UPDATE application.tender_auction " + "   SET publish_status = B'0' "
				+ "   WHERE tender_auction_id =:tenderAuctionId ";
		return entity.createNativeQuery(sql).setParameter("tenderAuctionId", tenderId).executeUpdate();

	}

	@Transactional
	@Override
	public Integer UnPublishRemoveRecord(BigInteger tenderId) {
		String sql = "UPDATE application.live_auction " + "   SET deleted_flag = B'1' "
				+ "   WHERE tender_auction_id = :tenderAuctionId ";
		Query query = entity.createNativeQuery(sql).setParameter("tenderAuctionId", tenderId);
		return query.executeUpdate();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAuctionsUseLike(Integer pageNumber, Integer pageSize, String value, String status) {
		String publishstatus = "";
		if (status.equalsIgnoreCase("1")) {
			publishstatus = "AND ta.publish_status='1' ";
		} else if (status.equalsIgnoreCase("0")) {
			publishstatus = "AND ta.publish_status='0' ";
		} else {
			publishstatus = "";
		}
		String nativeQuery = "select  DISTINCT ON(ta.tender_auction_id) ta.tender_auction_id, ta.auction_plot_id,(SELECT CAST(  (SELECT  district_name "
				+ "FROM land_bank.district_master WHERE  district_code = ap.district_code LIMIT 1) || ' - ' ||  (SELECT tahasil_name "
				+ "FROM land_bank.tahasil_master WHERE  tahasil_code = ap.tahasil_code LIMIT 1) || ' - ' ||  (SELECT  village_name FROM "
				+ "land_bank.village_master WHERE  village_code = ap.village_code LIMIT 1) || ' - ' ||  (SELECT  khata_no FROM land_bank.khatian_information WHERE "
				+ "khatian_code = ap.khatian_code LIMIT 1) || ' - ' || (select plot_no from land_bank.plot_information where plot_code=apd.plot_code LIMIT 1) "
				+ "AS character varying) AS combined_values FROM application.auction_plot_details apd1  WHERE "
				+ "apd1.auction_plot_details_id = ta.auction_plot_id) AS auction_id_name,     lease_period_years ,     committee_member_name  , "
				+ "royality ,form_m_submit_start_date ,form_m_submit_end_date ,secuirity_deposit_start_date ,secuirity_deposit_end_date e, "
				+ "bid_document_downlode_start_date ,    bid_document_downlode_end_date ,  date_of_techinical_evaluation ,application_fee_not_refund ,secutity_amount_deposite , "
				+ "slot_for_auction_from_date ,     slot_for_auction_to_date, auction_name,(SELECT STRING_AGG(full_name, ', ') FROM user_details WHERE "
				+ "user_id IN ( SELECT  CAST(UNNEST(string_to_array(committee_member_name, ',')) AS INTEGER)  AS converted_value FROM application.tender_auction as ata "
				+ "WHERE ata.tender_auction_id = ta.tender_auction_id)) as username ,publish_status, ap.district_code, ap.tahasil_code, ap.village_code,(SELECT  khata_no FROM land_bank.khatian_information WHERE \r\n"
				+ " khatian_code = ap.khatian_code LIMIT 1), apd.go_for_auction_process_flag ,apd.auction_plot_details_id  "
				+ "FROM application.tender_auction ta   "
				+ "INNER JOIN application.auction_plot_details apd ON(apd.auction_plot_details_id=ta.auction_plot_id)  "
				+ "INNER JOIN application.auction_plot ap ON apd.auction_plot_id = ap.auction_plot_id " 
//				+ "JOIN application.land_allotement_for_auction lafa ON(lafa.plot_code=apd.plot_code) "
				+ "WHERE ta.deleted_flag = false  " + "AND ta.auction_name ILIKE :value " + " " + publishstatus
				+ "ORDER BY ta.tender_auction_id DESC "
				+ "LIMIT :pageSize OFFSET :offset ";
		Integer offset = (pageNumber - 1) * pageSize;
		Query query = entity.createNativeQuery(nativeQuery).setParameter("pageSize", pageSize)
				.setParameter("offset", offset).setParameter("value", "%" + value + "%");
		return query.getResultList();
	}

	@Transactional
	@Override
	public BigInteger getActiveAuctionForLikeCount(String value, String status) {
		String publishstatus = "";
		if (status.equalsIgnoreCase("1")) {
			publishstatus = "AND ta.publish_status='1' ";
		} else if (status.equalsIgnoreCase("0")) {
			publishstatus = "AND ta.publish_status='0' ";
		} else {
			publishstatus = "";
		}
		
		
//		+ "FROM application.tender_auction ta INNER JOIN application.auction_plot ap ON ta.auction_plot_id = ap.auction_plot_id "
//		+ "INNER JOIN application.auction_plot_details apd ON(apd.auction_plot_id=ap.auction_plot_id) "
//		+ "JOIN application.land_allotement_for_auction lafa ON(lafa.plot_code=apd.plot_code)  "
//		+ "WHERE ta.deleted_flag = false " + "LIMIT :pageSize OFFSET :offset ";
		
		String nativeQuery = "SELECT COUNT(*) FROM application.tender_auction ta"
				+ "  INNER JOIN application.auction_plot_details apd ON(apd.auction_plot_details_id=ta.auction_plot_id)  "
				+ " INNER JOIN application.auction_plot ap ON apd.auction_plot_id = ap.auction_plot_id" 
//				+ " JOIN application.land_allotement_for_auction lafa ON(lafa.plot_code=apd.plot_code)  "
				+ " WHERE ta.deleted_flag = false  " 
				+ " AND ta.auction_name ILIKE :value " 
			     + publishstatus ;
		Query query = entity.createNativeQuery(nativeQuery).setParameter("value", "%" + value + "%");
		;
		entity.close();
		return ((BigInteger) query.getSingleResult());
	}

	@Transactional
	@Override
	public Integer updatePlotIdThroughTenderFlag(BigInteger auctonId,String plotCode) {
		String sql = "update application.auction_plot_details " 
	                + "set aucion_tender_creation_flag=1 "
				+ "where auction_plot_details_id =:auctonId AND plot_code=:plotCode ";
		return entity.createNativeQuery(sql).setParameter("auctonId", auctonId).setParameter("plotCode", plotCode).executeUpdate();

	}

	@Transactional
	@Override
	public Integer updatePlotIdThroughTenderFlagDelete(BigInteger auctonId,BigInteger auctionPlotDetailsId) {
		String sql = "update application.auction_plot_details " + "set aucion_tender_creation_flag=0 "
				+ "where auction_plot_id =:auctonId AND auction_plot_details_id=:auctionPlotDetailsId ";
		return entity.createNativeQuery(sql).setParameter("auctonId", auctonId).setParameter("auctionPlotDetailsId", auctionPlotDetailsId).executeUpdate();

	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public BigInteger getAuctionId(BigInteger tenderId) {
		BigInteger auctionPlotId = null;
		String nativeQuery = "SELECT auction_plot_id FROM application.tender_auction WHERE tender_auction_id = :tenderId AND deleted_flag = false LIMIT 1";
		Query query = entity.createNativeQuery(nativeQuery);
		try {
			Object result = query.setParameter("tenderId", tenderId).getSingleResult();
			if (result != null) {
				auctionPlotId = (BigInteger) result;
			}
		} catch (Exception ex) {
			log.info("getting this excetion :: " + ex);
		}
		return auctionPlotId;

	}
}


