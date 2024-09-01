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

/**
 * @author prasanta.sethi
 */
@Repository
public class LandAppApprovalSlaReportRepositoryImpl
		implements com.csmtech.sjta.repository.LandAppApprovalSlaReportRepository {
	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getReportData(Integer pageSize, Integer offset) {
		String sqlQuery = "SELECT DISTINCT ON (laa.land_application_approval_id)  "
				+ "laa.land_application_approval_id, la.application_no AS applicationNo, "
				+ "la.applicant_name AS applicantName, CASE "
				+ "WHEN laa.pending_at_role_id = 3 THEN 'Dealing Officer' "
				+ "WHEN laa.pending_at_role_id = 4 THEN 'Land Officer' "
				+ "WHEN laa.pending_at_role_id = 5 THEN 'Deputy Administrator' ELSE NULL END AS pendingAt, "
				+ "(CAST(now() AS DATE) - CAST(la.created_on AS DATE) - ac.timeline_days) AS noOfDayDelayed FROM  "
				+ "public.land_application_approval laa  JOIN "
				+ "public.land_application la ON (la.land_application_id = laa.land_application_id) JOIN  "
				+ "public.land_application_approval_log laal ON (laal.land_application_approval_id = laa.land_application_approval_id) "
				+ "JOIN public.approval_configration ac ON laa.pending_at_role_id = ac.role_id  "
				+ "AND ac.approval_type = 'Land Application' AND ac.status = '0'  WHERE  "
				+ "laa.status = '0'  AND laa.pending_at_role_id IN (3, 4, 5)  " + "AND la.application_no IS NOT NULL "
				+ "AND (CAST(now() AS DATE) - CAST(la.created_on AS DATE) - ac.timeline_days) > 0 "
				+ "ORDER BY laa.land_application_approval_id  OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("limit", pageSize).setParameter("offset", offset);
		return query.getResultList();
	}

	@Override
	public BigInteger getReportCount() {
		String sqlQuery = "SELECT COUNT(*) FROM(SELECT DISTINCT ON (laa.land_application_approval_id)  "
				+ "laa.land_application_approval_id, la.application_no AS applicationNo, "
				+ "la.applicant_name AS applicantName, CASE  "
				+ "WHEN laa.pending_at_role_id = 3 THEN 'Dealing Officer' "
				+ "WHEN laa.pending_at_role_id = 4 THEN 'Land Officer' "
				+ "WHEN laa.pending_at_role_id = 5 THEN 'Deputy Administrator' ELSE NULL END AS pendingAt, "
				+ "(CAST(now() AS DATE) - CAST(la.created_on AS DATE) - ac.timeline_days) AS noOfDayDelayed FROM "
				+ "public.land_application_approval laa  JOIN "
				+ "public.land_application la ON (la.land_application_id = laa.land_application_id) JOIN "
				+ "public.land_application_approval_log laal ON (laal.land_application_approval_id = laa.land_application_approval_id) "
				+ "JOIN public.approval_configration ac ON laa.pending_at_role_id = ac.role_id  "
				+ "AND ac.approval_type = 'Land Application' AND ac.status = '0'  WHERE  laa.status = '0'  "
				+ "AND laa.pending_at_role_id IN (3, 4, 5) AND la.application_no IS NOT NULL "
				+ "AND (CAST(now() AS DATE) - CAST(la.created_on AS DATE) - ac.timeline_days) > 0  ORDER BY  "
				+ "laa.land_application_approval_id) AS subquery ";
		Query query = entity.createNativeQuery(sqlQuery);
		return (BigInteger) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAuctionReportData(Integer pageSize, Integer offset) {
		String sqlQuery = "SELECT bfma.bidder_form_m_application_id,bfma.approval_status,   "
				+ " ta.date_of_techinical_evaluation,   "
				+ " CAST(DATE_PART('day', NOW() - CAST(bfma.created_on AS DATE)) "
				+ " - DATE_PART('day', ta.date_of_techinical_evaluation) as integer) as noOfDayDelayed, "
				+ " (select full_name from public.citizen_profile_details where citizen_profile_details_id=bfma.user_id ) as fullName, "
				+ " CAST('Land Officer' as varchar) as pendingAt, bidder_form_m_application_number "
				+ " FROM  application.bidder_form_m_application bfma   "
				+ " JOIN  application.tender_auction ta ON (bfma.tender_auction_id = ta.tender_auction_id) "
				+ " WHERE  bfma.approval_status = 'N' AND bfma.bidder_form_m_application_number IS NOT NULL AND ta.deleted_flag='false' AND bfma.deleted_flag='0' OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("limit", pageSize).setParameter("offset", offset);
		return query.getResultList();
	}

	@Override
	public BigInteger getAuctionReportCount() {
		String sqlQuery = "SELECT COUNT(*) FROM (SELECT  " + "bfma.bidder_form_m_application_id,  "
				+ "bfma.approval_status, " + "ta.date_of_techinical_evaluation, "
				+ "CAST(DATE_PART('day', NOW() - CAST(bfma.created_on AS DATE))  "
				+ "- DATE_PART('day', ta.date_of_techinical_evaluation) as integer) as noOfDayDelayed, "
				+ " (select full_name from public.citizen_profile_details where citizen_profile_details_id=bfma.user_id ) as fullName, "
				+ " CAST('Land Officer' as varchar) as pendingAt, " + "bidder_form_m_application_number "
				+ " FROM  application.bidder_form_m_application bfma "
				+ " JOIN  application.tender_auction ta ON (bfma.tender_auction_id = ta.tender_auction_id) "
				+ " WHERE  bfma.approval_status = 'N' AND ta.deleted_flag='false' AND bfma.deleted_flag='0') AS subquery ";
		Query query = entity.createNativeQuery(sqlQuery);
		return (BigInteger) query.getSingleResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getLandVerificationData(Integer pageSize, Integer offset) {
		String sqlQuery = "SELECT dm.district_name, tm.tahasil_name, vm.village_name,"
				+ "ki.khata_no, pii.plot_no,  approve_status,tahasil_status, CAST(DATE_PART('day', NOW()) "
				+ "- DATE_PART('day', scheduled_inspection_date) as integer) as noOfDayDelayed "
				+ "FROM application.plot_land_inspection pli1 "
				+ "JOIN land_bank.plot_information pii ON(pii.plot_code=pli1.plot_code) "
				+ "JOIN land_bank.khatian_information ki ON(ki.khatian_code=pli1.khatian_code) "
				+ "JOIN land_bank.village_master vm   ON(vm.village_code=pli1.village_code) "
				+ "JOIN land_bank.tahasil_master tm  ON(tm.tahasil_code=pli1.tahasil_code) "
				+ "JOIN land_bank.district_master dm ON(dm.district_code=pli1.district_code) "
				+ "AND approve_status=0 AND tahasil_status=0  AND DATE_PART('day', NOW()) "
				+ "- DATE_PART('day', scheduled_inspection_date)>0 AND deleted_status='0' "
				+ "ORDER BY dm.district_name ASC offset :offset  limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("limit", pageSize).setParameter("offset", offset);
		return query.getResultList();
	}

	@Override
	public BigInteger getLandVerificationReportCount() {
		String sqlQuery = "SELECT COUNT(*) FROM ( SELECT  dm.district_name,  tm.tahasil_name, "
				+ "vm.village_name, ki.khata_no, pii.plot_no, approve_status, tahasil_status, "
				+ "CAST(DATE_PART('day', NOW()) - DATE_PART('day', scheduled_inspection_date) AS INTEGER) AS noOfDayDelayed "
				+ "FROM  application.plot_land_inspection pli1 JOIN "
				+ "land_bank.plot_information pii ON (pii.plot_code = pli1.plot_code) JOIN "
				+ "land_bank.khatian_information ki ON (ki.khatian_code = pli1.khatian_code) JOIN "
				+ "land_bank.village_master vm ON (vm.village_code = pli1.village_code) JOIN "
				+ "land_bank.tahasil_master tm ON (tm.tahasil_code = pli1.tahasil_code) JOIN "
				+ "land_bank.district_master dm ON (dm.district_code = pli1.district_code) WHERE "
				+ "approve_status = 0 AND tahasil_status = 0  "
				+ "AND DATE_PART('day', NOW()) - DATE_PART('day', scheduled_inspection_date) > 0 "
				+ "AND deleted_status = '0' ORDER BY dm.district_name ASC ) AS subquery ";
		Query query = entity.createNativeQuery(sqlQuery);
		return (BigInteger) query.getSingleResult();
	}

}
