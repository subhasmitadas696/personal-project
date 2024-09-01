package com.csmtech.sjta.repositoryImpl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.repository.DashboardRepository;

@Repository
public class DashboardRepositoryImpl implements DashboardRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDistrictDetailsCitizen(String param) {
		String nativeQuery = "SELECT d.district_code AS district, d.district_name AS district_name, COUNT(DISTINCT t.tahasil_code) AS tahasil, COUNT(DISTINCT v.village_code) AS village, COUNT(DISTINCT k.khatian_code) AS khata, COUNT(DISTINCT p.plot_code) AS plot, SUM(p.area_acre) AS plot_area  \r\n"
				+ "FROM land_bank.district_master d  \r\n"
				+ "JOIN land_bank.tahasil_master t ON t.district_code = d.district_code  \r\n"
				+ "JOIN land_bank.village_master v ON t.tahasil_code = v.tahasil_code  \r\n"
				+ "JOIN land_bank.khatian_information k ON v.village_code = k.village_code  \r\n"
				+ "JOIN land_bank.plot_information p ON k.khatian_code = p.khatian_code\r\n"
				+ "GROUP BY d.district_name, d.district_code\r\n" + "ORDER BY d.district_name";

		return entity.createNativeQuery(nativeQuery).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getLandDetails(String param) {
		JSONObject paramF = new JSONObject(param);
		BigInteger userId = new BigInteger((String) paramF.get("userId"));
		String nativeQuery = "SELECT la.land_application_id, la.application_no, tm.tahasil_name, ki.khata_no, pi.plot_no, ls.purchase_area, laa.application_status_id, laa.approval_action_id, mas.application_status, laptd.tranction_amount\r\n"
				+ ",CAST((SELECT st_extent(ST_Transform(geom, 3857)) as extent FROM land_bank.sjta_plot_boundary_view \r\n"
				+ "where plot_code in (ls.plot_code ) group by village_code)as varchar) as extent, \r\n"
				+ "CAST((SELECT st_extent(ST_Transform(geom,3857)) as extent \r\n"
				+ "FROM land_bank.sjta_plot_boundary_view where plot_code in (SELECT STRING_AGG(ls.plot_code, ',') AS plot_code \r\n"
				+ "FROM public.land_schedule ls WHERE ls.land_application_id = la.land_application_id AND ls.deleted_flag='0' ) group by village_code)as varchar) as plot_extend  \r\n"
				+ "FROM land_application AS la \r\n"
				+ "JOIN land_schedule AS ls ON la.land_application_id = ls.land_application_id AND ls.deleted_flag = '0' \r\n"
				+ "JOIN land_bank.tahasil_master AS tm ON la.tehsil_code = tm.tahasil_code  \r\n"
				+ "JOIN land_bank.khatian_information AS ki ON la.khatian_code = ki.khatian_code  \r\n"
				+ "JOIN land_bank.plot_information AS pi ON ls.plot_code = pi.plot_code  \r\n"
				+ "JOIN land_application_approval AS laa ON la.land_application_id = laa.land_application_id  \r\n"
				+ "JOIN m_application_status AS mas ON laa.application_status_id = mas.application_status_id  \r\n"
				+ "JOIN land_application_payment_tranction_details AS laptd ON la.land_application_id = laptd.land_application_id AND laptd.payment_status = 'Success Payment'  \r\n"
				+ "WHERE la.deleted_flag = '0' AND la.app_status > 0 AND la.created_by=:userId "
				+ "ORDER BY la.land_application_id";

		return entity.createNativeQuery(nativeQuery).setParameter("userId", userId).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getApplicationStatistics(String param) {

		JSONObject paramF = new JSONObject(param);
		Integer userRole = paramF.getInt("userRole");

		String nativeQuery = "SELECT COUNT(DISTINCT la.land_application_id) AS application_received, SUM(ls.purchase_area) AS area_applied,\r\n"
				+ " COUNT(DISTINCT CASE WHEN laa.approval_action_id = 6 THEN la.land_application_id END) AS approved,\r\n"
				+ " COUNT(DISTINCT CASE WHEN laa.approval_action_id = 1 AND laa.pending_at_role_id != :userRole THEN la.land_application_id END) AS forwarded,\r\n"
				+ " COUNT(DISTINCT CASE WHEN laa.approval_action_id IN (3, 5) THEN la.land_application_id END) AS reverted,\r\n"
				+ " COUNT(DISTINCT CASE WHEN laa.approval_action_id = 2 THEN la.land_application_id END) AS rejected,\r\n"
				+ " COUNT(DISTINCT CASE WHEN laa.approval_action_id NOT IN (2, 3, 5, 6) AND laa.pending_at_role_id = :userRole THEN la.land_application_id END) AS pending\r\n"
				+ "FROM land_application AS la JOIN land_application_approval AS laa ON la.land_application_id = laa.land_application_id\r\n"
				+ "JOIN land_schedule AS ls ON la.land_application_id = ls.land_application_id\r\n"
				+ "WHERE la.deleted_flag = '0' AND laa.status = '0'";

		return entity.createNativeQuery(nativeQuery).setParameter("userRole", userRole).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getGrievanceStatus(String param) {
		String nativeQuery = "SELECT COUNT(DISTINCT grievance_id) AS total, COUNT(DISTINCT (CASE WHEN grievance_status = 3 THEN grievance_id ELSE NULL END)) AS assigned, COUNT(DISTINCT (CASE WHEN grievance_status = 4 THEN grievance_id ELSE NULL END)) AS closed, COUNT(DISTINCT (CASE WHEN grievance_status = 5 THEN grievance_id ELSE NULL END)) AS discarded, COUNT(DISTINCT (CASE WHEN grievance_status IN (0,1,2) THEN grievance_id ELSE NULL END)) AS pending\r\n"
				+ "FROM grievance\r\n" + "WHERE deleted_flag = '0'";

		return entity.createNativeQuery(nativeQuery).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> fetchPendingPayment(String param) {
		JSONObject paramF = new JSONObject(param);
		Integer userId = paramF.getInt("userId");
		
		String nativeQuery = "SELECT month, SUM(total_amount) AS total_amount FROM ( SELECT TO_CHAR(DATE_TRUNC('month', payment_date_time), 'Month') AS month,"
				+ "SUM(tranction_amount) AS total_amount FROM public.land_application_payment_tranction_details GROUP BY DATE_TRUNC('month', payment_date_time) "
				+ "UNION ALL SELECT TO_CHAR(DATE_TRUNC('month', payment_date_time), 'Month') AS month, SUM(tranction_amount) AS total_amount "
				+ "FROM application.from_m_application_payment_details GROUP BY DATE_TRUNC('month', payment_date_time) ) combined_results GROUP BY month ORDER BY month ASC";

		return entity.createNativeQuery(nativeQuery).getResultList();
	}

	@Override
	public List<Object[]> fetchLandSellPayment(String param) {
		JSONObject paramF = new JSONObject(param);
		Integer userId = paramF.getInt("userId");
		
		String nativeQuery = "SELECT TO_CHAR(DATE_TRUNC('month', payment_date_time), 'Month') AS month, SUM(transaction_paid_amount) AS total_amount FROM application.land_allotement_payment GROUP BY DATE_TRUNC('month', payment_date_time) ORDER BY month ASC";

		return entity.createNativeQuery(nativeQuery).getResultList();
	}

	@Override
	public List<Object[]> getDistrictDetailsLease(String param) {
		String nativeQuery = "SELECT d.district_code AS district, d.district_name AS district_name, COUNT(DISTINCT l.tahasil_code) AS tahasil, COUNT(DISTINCT l.mouza_code) AS village, COUNT(DISTINCT l.khata_code) AS khata, COUNT(DISTINCT l.plot_code) AS plot, SUM(l.total_area) AS plot_area, SUM(l.applied_area) AS applied_area  \r\n"
				+ "FROM application.lease_case_plot l  \r\n"
				+ "JOIN land_bank.district_master d ON l.district_code = d.district_code  \r\n"
				+ "GROUP BY d.district_name, d.district_code ORDER BY d.district_name";

		return entity.createNativeQuery(nativeQuery).getResultList();
	}

}
