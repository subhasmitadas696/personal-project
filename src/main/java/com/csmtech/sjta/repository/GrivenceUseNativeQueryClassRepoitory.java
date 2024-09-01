package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class GrivenceUseNativeQueryClassRepoitory {

	@PersistenceContext
	@Autowired
	private EntityManager entityManager;

	@Transactional
	public Integer updateGrievanceCreatedBy(int grievanceId, int newCreatedBy, String grievanceNo) {
		try {
			entityManager.createNativeQuery(
					"UPDATE public.grievance SET created_by = :newCreatedBy, grievance_no=:grievanceNo, grievance_status= 0 WHERE grievance_id = :grievanceId")
					.setParameter("newCreatedBy", newCreatedBy).setParameter("grievanceId", grievanceId)
					.setParameter("grievanceNo", grievanceNo).executeUpdate();

		} catch (Exception e) {
			log.error("inside exception ::", e.getMessage());
		} finally {
			entityManager.close();
		}
		return newCreatedBy;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Object[]> getGrievanceNoById(int grievanceId) {
		try {
			return entityManager
					.createNativeQuery(
							"SELECT grievance_no,mobile_no FROM public.grievance WHERE grievance_id = :grievanceId")
					.setParameter("grievanceId", grievanceId).getResultList();
		} catch (Exception e) {
			log.error("inside exception ::", e.getMessage());
			return null;
		} finally {
			entityManager.close();
		}
	}

	@Transactional
	public List<Object[]> getRecordGrivanceUser(Integer grievanceStatus, Integer pageNumber, Integer pageSize,
			String selDistrictCode, String selTahasilCode, String selVillageCode, String grievanceNo) {

		StringBuilder sqlQueryBuilder = new StringBuilder(
				"SELECT g.grievance_no, d.district_name AS district_code, t.tahasil_name AS tahasil_code, v.village_name AS village_code, k.khata_no AS khatian_code, p.plot_no, g.grievance_id, g.created_on, g.remark_by_go, "
						+ "(SELECT CAST(st_extent(ST_Transform(spb.geom, 3857))AS character varying) as extent FROM land_bank.sjta_plot_boundary spb WHERE spb.plot_code = g.plot_code) as extent "
						+ "FROM public.grievance g LEFT JOIN land_bank.district_master d ON g.district_code = d.district_code LEFT JOIN land_bank.tahasil_master t ON g.tahasil_code = t.tahasil_code "
						+ "LEFT JOIN land_bank.village_master v ON g.village_code = v.village_code LEFT JOIN land_bank.khatian_information k ON g.khatian_code = k.khatian_code LEFT JOIN land_bank.plot_information p ON g.plot_code = p.plot_code "
						+ "WHERE grievance_status = :grievanceStatus");

		if (selDistrictCode != null && !selDistrictCode.equals("")) {
			sqlQueryBuilder.append(" AND d.district_code = :selDistrictCode");
		}

		if (selTahasilCode != null && !selTahasilCode.equals("")) {
			sqlQueryBuilder.append(" AND t.tahasil_code = :selTahasilCode");
		}

		if (selVillageCode != null && !selVillageCode.equals("")) {
			sqlQueryBuilder.append(" AND v.village_code = :selVillageCode");
		}

		if (grievanceNo != null && !grievanceNo.equals("")) {
			sqlQueryBuilder.append(" AND g.grievance_no = :grievanceNo");
		}

		sqlQueryBuilder.append(" ORDER BY g.created_on DESC LIMIT :pageSize OFFSET :offset");

		int offset = (pageNumber - 1) * pageSize;

		Query query = entityManager.createNativeQuery(sqlQueryBuilder.toString())
				.setParameter("grievanceStatus", grievanceStatus).setParameter("pageSize", pageSize)
				.setParameter("offset", offset);

		if (selDistrictCode != null && !selDistrictCode.equals("")) {
			query.setParameter("selDistrictCode", selDistrictCode);
		}

		if (selTahasilCode != null && !selTahasilCode.equals("")) {
			query.setParameter("selTahasilCode", selTahasilCode);
		}

		if (selVillageCode != null && !selVillageCode.equals("")) {
			query.setParameter("selVillageCode", selVillageCode);
		}

		if (grievanceNo != null && !grievanceNo.equals("")) {
			query.setParameter("grievanceNo", grievanceNo);
		}

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.getResultList();
		entityManager.close();

		return resultList;
	}

	@Transactional
	public BigInteger getRecordGrivanceUserCountPagination(Integer grievanceStatus, String selDistrictCode,
			String selTahasilCode, String selVillageCode, String grievanceNo) {

		StringBuilder sqlQueryBuilder = new StringBuilder(
				"SELECT count(*) FROM public.grievance g LEFT JOIN land_bank.district_master d ON g.district_code = d.district_code LEFT JOIN land_bank.tahasil_master t ON g.tahasil_code = t.tahasil_code LEFT JOIN land_bank.village_master v ON g.village_code = v.village_code LEFT JOIN land_bank.khatian_information k ON g.khatian_code = k.khatian_code LEFT JOIN land_bank.plot_information p ON g.plot_code = p.plot_code WHERE grievance_status=:grievanceStatus");

		if (selDistrictCode != null && !selDistrictCode.equals("")) {
			sqlQueryBuilder.append(" AND d.district_code = :selDistrictCode");
		}

		if (selTahasilCode != null && !selTahasilCode.equals("")) {
			sqlQueryBuilder.append(" AND t.tahasil_code = :selTahasilCode");
		}

		if (selVillageCode != null && !selVillageCode.equals("")) {
			sqlQueryBuilder.append(" AND v.village_code = :selVillageCode");
		}

		if (grievanceNo != null && !grievanceNo.equals("")) {
			sqlQueryBuilder.append(" AND g.grievance_no = :grievanceNo");
		}

		Query query = entityManager.createNativeQuery(sqlQueryBuilder.toString()).setParameter("grievanceStatus",
				grievanceStatus);

		if (selDistrictCode != null && !selDistrictCode.equals("")) {
			query.setParameter("selDistrictCode", selDistrictCode);
		}

		if (selTahasilCode != null && !selTahasilCode.equals("")) {
			query.setParameter("selTahasilCode", selTahasilCode);
		}

		if (selVillageCode != null && !selVillageCode.equals("")) {
			query.setParameter("selVillageCode", selVillageCode);
		}

		if (grievanceNo != null && !grievanceNo.equals("")) {
			query.setParameter("grievanceNo", grievanceNo);
		}

		entityManager.close();

		return (BigInteger) query.getSingleResult();
	}

	// get GIS record id through view more record
	public List<Object[]> getRecordGrivanceUserMore(Integer grivanceId) {

		String sqlQuery = " SELECT g.grievance_id, \r\n"
				+ " (select month_name  from public.m_month where month_id=g.month_id LIMIT 1) as month_id, g.name, g.father_name, \r\n"
				+ "  (select district_name from public.m_district where district_id=g.district_id  LIMIT 1) as district_id, \r\n"
				+ " (select block_name from public.m_block where block_id=g.block_id LIMIT 1) as block_id, \r\n"
				+ " (select gp_name from public.m_gp where gp_id=g.gp_id LIMIT 1) as gp_id ,  \r\n"
				+ " (select village_name from public.m_village_master where village_id=g.village_id LIMIT 1) as village_id, \r\n"
				+ " g.other_information,  g.caste_name, g.mobile_no,  g.disclose_details, \r\n"
				+ " d.district_name AS district_code, t.tahasil_name AS tahasil_code, \r\n"
				+ " v.village_name AS village_code, k.khata_no AS khatian_code,  p.plot_no , \r\n"
				+ " g.area_acre, g.extent_occupied, g.mode_of_occupation, g.other_occupation, \r\n"
				+ " g.landmark, g.upload_file, g.remarks, g.grievance_no, \r\n"
				+ " g.grievance_status,g.co_uploaded_photo,g.remark_by_co, g.inspection_date FROM public.grievance g \r\n"
				+ " LEFT JOIN land_bank.district_master d ON g.district_code = d.district_code \r\n"
				+ " LEFT JOIN land_bank.tahasil_master t ON g.tahasil_code = t.tahasil_code  \r\n"
				+ " LEFT JOIN land_bank.village_master v  ON g.village_code = v.village_code  \r\n"
				+ " LEFT JOIN land_bank.khatian_information k ON g.khatian_code = k.khatian_code  \r\n"
				+ " LEFT JOIN land_bank.plot_information p ON g.plot_code = p.plot_code WHERE grievance_id=:grivanceId";

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = entityManager.createNativeQuery(sqlQuery).setParameter("grivanceId", grivanceId)
				.getResultList();
		entityManager.close();
		return resultList;
	}

	// status update when the user are forwarded to next level
	@Transactional
	public Integer updateGrievanceStatus(Integer grievanceId, Integer newStatus, String remerk, java.sql.Date date) {
		Integer count = null;
		try {
			String sql = "UPDATE public.grievance SET grievance_status = :newStatus,schedule_inspection_date=:date, remark_by_go=:remark WHERE grievance_id = :grievanceId ";

			count = entityManager.createNativeQuery(sql).setParameter("newStatus", newStatus).setParameter("date", date)
					.setParameter("remark", remerk).setParameter("grievanceId", grievanceId).executeUpdate();
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		finally {
			entityManager.close();

		}
		return count;
	}

	public Integer updateGrievanceStatusFinal(Integer grievanceId, Integer newStatus, String remark) {
		Integer count = null;
		try {
			String sql = "UPDATE public.grievance SET grievance_status = :newStatus, remark_by_go=:remark WHERE grievance_id = :grievanceId ";

			count = entityManager.createNativeQuery(sql).setParameter("newStatus", newStatus)
					.setParameter("remark", remark).setParameter("grievanceId", grievanceId).executeUpdate();
		} catch (Exception e) {
			log.error("inside exception", e.getMessage());
		} finally {
			entityManager.close();
		}
		return count;
	}

}
