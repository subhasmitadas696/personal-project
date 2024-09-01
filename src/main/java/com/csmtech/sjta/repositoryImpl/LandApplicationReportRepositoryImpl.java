package com.csmtech.sjta.repositoryImpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.repository.LandApplicationReportRepository;

@Repository
public class LandApplicationReportRepositoryImpl implements LandApplicationReportRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getLandReportData(Integer pagesize, Integer offset, Integer userRole) {
		String sqlQuery = "SELECT dm.district_code, dm.district_name, COUNT(DISTINCT la.land_application_id) AS total, SUM(ls.purchase_area) AS area_applied,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id = 6 THEN la.land_application_id END) AS approved,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id = 1 AND laa.pending_at_role_id != :userRole THEN la.land_application_id END) AS forwarded,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id IN (3, 5) THEN la.land_application_id END) AS reverted,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id = 2 THEN la.land_application_id END) AS rejected,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id NOT IN (2, 3, 5, 6) AND  laa.pending_at_role_id = :userRole THEN la.land_application_id END) AS pending "
				+ "FROM land_application AS la JOIN land_application_approval AS laa ON la.land_application_id = laa.land_application_id "
				+ "JOIN land_bank.district_master dm ON la.district_code = dm.district_code "
				+ "JOIN land_schedule AS ls ON la.land_application_id = ls.land_application_id "
				+ "WHERE la.deleted_flag = '0' AND laa.status = '0' "
				+ "GROUP BY dm.district_code, dm.district_name ORDER BY district_name OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("userRole", userRole)
				.setParameter("limit", pagesize).setParameter("offset", offset);
		return query.getResultList();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDistrictWiseLandRecord(Integer pagesize, Integer offset, String districtCode,
			Integer userRole) {
		String sqlQuery = "SELECT tm.tahasil_code, tm.tahasil_name, COUNT(DISTINCT la.land_application_id) AS total, SUM(ls.purchase_area) AS area_applied,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id = 6 THEN la.land_application_id END) AS approved,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id = 1 AND laa.pending_at_role_id != :userRole THEN la.land_application_id END) AS forwarded,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id IN (3, 5) THEN la.land_application_id END) AS reverted,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id = 2 THEN la.land_application_id END) AS rejected,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id NOT IN (2, 3, 5, 6) AND laa.pending_at_role_id = :userRole THEN la.land_application_id END) AS pending ,dm.district_name "
				+ "FROM land_application AS la JOIN land_application_approval AS laa ON la.land_application_id = laa.land_application_id  "
				+ "JOIN land_bank.district_master dm ON la.district_code = dm.district_code "
				+ "JOIN land_bank.tahasil_master tm ON la.tehsil_code = tm.tahasil_code "
				+ "JOIN land_schedule AS ls ON la.land_application_id = ls.land_application_id "
				+ "WHERE la.deleted_flag = '0' AND laa.status = '0' AND la.district_code =:districtCode "
				+ "GROUP BY tm.tahasil_code, tm.tahasil_name, dm.district_name  "
				+ "ORDER BY tahasil_name OFFSET :offset  limit :limit  ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("userRole", userRole)
				.setParameter("limit", pagesize).setParameter("offset", offset)
				.setParameter("districtCode", districtCode);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Object[]> getTahasilWiseLandRecord(Integer pagesize, Integer offset, String tahasilCode, Integer userRole) {
		String sqlQuery = "SELECT vm.village_code, vm.village_name, COUNT(DISTINCT la.land_application_id) AS total, SUM(ls.purchase_area) AS area_applied,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id = 6 THEN la.land_application_id END) AS approved,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id = 1 AND laa.pending_at_role_id != :userRole  THEN la.land_application_id END) AS forwarded,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id IN (3, 5) THEN la.land_application_id END) AS reverted,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id = 2 THEN la.land_application_id END) AS rejected,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id NOT IN (2, 3, 5, 6) AND laa.pending_at_role_id != :userRole THEN la.land_application_id END) AS pending, dm.district_name, tm.tahasil_name "
				+ "FROM land_application AS la JOIN land_application_approval AS laa ON la.land_application_id = laa.land_application_id "
				+ "JOIN land_bank.district_master dm ON la.district_code = dm.district_code "
				+ "JOIN land_bank.tahasil_master tm ON la.tehsil_code = tm.tahasil_code "
				+ " JOIN land_bank.village_master vm ON la.village_code = vm.village_code "
				+ "JOIN land_schedule AS ls ON la.land_application_id = ls.land_application_id "
				+ "WHERE la.deleted_flag = '0' AND laa.status = '0' AND la.tehsil_code =:tahasilCode "
				+ "GROUP BY vm.village_code, vm.village_name, dm.district_name, tm.tahasil_name "
				+ "ORDER BY village_name OFFSET :offset  limit :limit  ";
		Query query = entity.createNativeQuery(sqlQuery)
				.setParameter("userRole", userRole)
				.setParameter("limit", pagesize).setParameter("offset", offset)
				.setParameter("tahasilCode", tahasilCode);
		return query.getResultList();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getLandReportExcelData() {
		String sqlQuery = "SELECT dm.district_code, dm.district_name, COUNT(DISTINCT la.land_application_id) AS total, SUM(ls.purchase_area) AS area_applied,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id = 6 THEN la.land_application_id END) AS approved,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id = 1  THEN la.land_application_id END) AS forwarded,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id IN (3, 5) THEN la.land_application_id END) AS reverted,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id = 2 THEN la.land_application_id END) AS rejected,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id NOT IN (2, 3, 5, 6) THEN la.land_application_id END) AS pending "
				+ "FROM land_application AS la JOIN land_application_approval AS laa ON la.land_application_id = laa.land_application_id "
				+ "JOIN land_bank.district_master dm ON la.district_code = dm.district_code "
				+ "JOIN land_schedule AS ls ON la.land_application_id = ls.land_application_id "
				+ "WHERE la.deleted_flag = '0' AND laa.status = '0' "
				+ "GROUP BY dm.district_code, dm.district_name ORDER BY district_name ";
		Query query = entity.createNativeQuery(sqlQuery);
		return query.getResultList();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDistrictWiseLandExcel(String districtCode) {
		String sqlQuery = "SELECT tm.tahasil_code, tm.tahasil_name, COUNT(DISTINCT la.land_application_id) AS total, SUM(ls.purchase_area) AS area_applied,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id = 6 THEN la.land_application_id END) AS approved,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id = 1  THEN la.land_application_id END) AS forwarded,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id IN (3, 5) THEN la.land_application_id END) AS reverted,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id = 2 THEN la.land_application_id END) AS rejected,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id NOT IN (2, 3, 5, 6) THEN la.land_application_id END) AS pending "
				+ "FROM land_application AS la JOIN land_application_approval AS laa ON la.land_application_id = laa.land_application_id "
				+ "JOIN land_bank.district_master dm ON la.district_code = dm.district_code "
				+ "JOIN land_bank.tahasil_master tm ON la.tehsil_code = tm.tahasil_code "
				+ "JOIN land_schedule AS ls ON la.land_application_id = ls.land_application_id "
				+ "WHERE la.deleted_flag = '0' AND laa.status = '0' AND la.district_code =:districtCode "
				+ "GROUP BY tm.tahasil_code, tm.tahasil_name " + "ORDER BY tahasil_name   ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("districtCode", districtCode);
		return query.getResultList();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getTahasilWiseLandExcel(String tahasilCode) {
		String sqlQuery = "SELECT vm.village_code, vm.village_name, COUNT(DISTINCT la.land_application_id) AS total, SUM(ls.purchase_area) AS area_applied,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id = 6 THEN la.land_application_id END) AS approved,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id = 1  THEN la.land_application_id END) AS forwarded,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id IN (3, 5) THEN la.land_application_id END) AS reverted,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id = 2 THEN la.land_application_id END) AS rejected,"
				+ "COUNT(DISTINCT CASE WHEN laa.approval_action_id NOT IN (2, 3, 5, 6) THEN la.land_application_id END) AS pending "
				+ "FROM land_application AS la JOIN land_application_approval AS laa ON la.land_application_id = laa.land_application_id "
				+ "JOIN land_bank.district_master dm ON la.district_code = dm.district_code "
				+ "JOIN land_bank.tahasil_master tm ON la.tehsil_code = tm.tahasil_code "
				+ " JOIN land_bank.village_master vm ON la.village_code = vm.village_code "
				+ "JOIN land_schedule AS ls ON la.land_application_id = ls.land_application_id "
				+ "WHERE la.deleted_flag = '0' AND laa.status = '0' AND la.tehsil_code =:tahasilCode "
				+ "GROUP BY vm.village_code, vm.village_name " + "ORDER BY village_name ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("tahasilCode", tahasilCode);
		return query.getResultList();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getApplicationDetails(Integer pagesize, Integer offset, String districtCode, String type) {
		String sqlQuery = "SELECT la.application_no,la.applicant_name,dm.district_name,tm.tahasil_name,\r\n"
				+ "vm.village_name,ki.khata_no,pi.plot_no, ma.approval_action as application_status\r\n"
				+ "from land_application la \r\n" + "JOIN land_schedule ls USING(land_application_id)\r\n"
				+ "JOIN land_application_approval laa USING(land_application_id)\r\n"
				+ "JOIN m_approval_action ma USING (approval_action_id)\r\n"
				+ "JOIN land_bank.district_master dm USING(district_code)\r\n"
				+ "JOIN land_bank.tahasil_master tm ON la.tehsil_code = tm.tahasil_code\r\n"
				+ "JOIN land_bank.village_master vm USING(village_code)\r\n"
				+ "JOIN land_bank.khatian_information ki USING(khatian_code)\r\n"
				+ "JOIN land_bank.plot_information pi ON ls.plot_code = pi.plot_code\r\n"
				+ "WHERE dm.district_code =:districtCode AND laa.approval_action_id IN (6) \r\n"
				+ "ORDER BY la.applicant_name  OFFSET :offset  limit :limit  ";

		Query query = entity.createNativeQuery(sqlQuery).setParameter("limit", pagesize).setParameter("offset", offset)
				.setParameter("districtCode", districtCode);
		return query.getResultList();
	}

}
