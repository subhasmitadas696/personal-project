package com.csmtech.sjta.repositoryImpl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.repository.IllegitimateLandUseReportRepository;

/**
 * @author prasanta.sethi
 */
@Repository
public class IllegitimateLandUseReportRepositoryImpl implements IllegitimateLandUseReportRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getDistrictReportData(Integer pagesize, Integer offset) {
		String sqlQuery = "select district_code, \r\n"
				+ "(select district_name from land_bank.district_master where district_code=g1.district_code) as district_name,\r\n"
				+ "count(*) as total_aplicant,\r\n"
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=0 AND g1.district_code=g2.district_code limit 1) as pending,\r\n"
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=1 AND g1.district_code=g2.district_code limit 1) as assigned,\r\n"
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=3 AND g1.district_code=g2.district_code limit 1) as inspection_completed,\r\n"
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=2 AND g1.district_code=g2.district_code limit 1) as application_on_hold,\r\n"
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=4 AND g1.district_code=g2.district_code limit 1) as closed,\r\n"
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=5 AND g1.district_code=g2.district_code limit 1) as appl_discarded\r\n"
				+ "from public.grievance g1 WHERE deleted_flag='0' \r\n"
				+ "GROUP BY g1.district_code ORDER BY district_name ASC OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("limit", pagesize).setParameter("offset", offset);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getTahasilWiseDetails(Integer pageSize, Integer offset, String districtCode) {
		String sqlQuery = "select g1.district_code,\r\n"
				+ "(select district_name from land_bank.district_master where district_code=g1.district_code) as districtName,\r\n"
				+ "g1.tahasil_code,\r\n"
				+ "(select tahasil_name  from land_bank.tahasil_master where  tahasil_code=g1.tahasil_code) as tahasilName,\r\n"
				+ "count(*) as totalAplicant,\r\n"
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=0 AND g1.district_code=g2.district_code AND g1.tahasil_code=g2.tahasil_code limit 1) as pending,\r\n"
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=1 AND g1.district_code=g2.district_code AND g1.tahasil_code=g2.tahasil_code limit 1) as assigned,\r\n"
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=3 AND g1.district_code=g2.district_code AND g1.tahasil_code=g2.tahasil_code limit 1) as inspectionComplite,\r\n"
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=2 AND g1.district_code=g2.district_code AND g1.tahasil_code=g2.tahasil_code limit 1) as applicationHold,\r\n"
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=4 AND g1.district_code=g2.district_code AND g1.tahasil_code=g2.tahasil_code limit 1) as closed,\r\n"
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=5 AND g1.district_code=g2.district_code AND g1.tahasil_code=g2.tahasil_code limit 1) as appDiscard\r\n"
				+ "from public.grievance g1 WHERE deleted_flag='0' and g1.district_code= :districtCode "
				+ "GROUP BY g1.district_code,g1.district_code ,g1.tahasil_code ORDER BY tahasilName ASC OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("districtCode", districtCode)
				.setParameter("limit", pageSize).setParameter("offset", offset);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getVillageWiseDetails(Integer pageSize, Integer offset, String tahasilCode) {
		String sqlQuery = "SELECT d.district_code,d.district_name,t.tahasil_code,t.tahasil_name,v.village_code,v.village_name,"
				+ "COUNT(g.grievance_status) AS totalGrievances,\r\n"
				+ "SUM(CASE WHEN g.grievance_status = 0 THEN 1 ELSE 0 END) AS pending,\r\n"
				+ "SUM(CASE WHEN g.grievance_status = 1 THEN 1 ELSE 0 END) AS assigned,\r\n"
				+ "SUM(CASE WHEN g.grievance_status = 2 THEN 1 ELSE 0 END) AS applicationHold,\r\n"
				+ "SUM(CASE WHEN g.grievance_status = 3 THEN 1 ELSE 0 END) AS inspectionComplete,\r\n"
				+ "SUM(CASE WHEN g.grievance_status = 4 THEN 1 ELSE 0 END) AS closed,\r\n"
				+ "SUM(CASE WHEN g.grievance_status = 5 THEN 1 ELSE 0 END) AS appDiscard\r\n"
				+ "FROM land_bank.tahasil_master t\r\n"
				+ "JOIN land_bank.district_master d ON t.district_code = d.district_code\r\n"
				+ "JOIN land_bank.village_master v ON t.tahasil_code = v.tahasil_code\r\n"
				+ "JOIN public.grievance g ON v.village_code = g.village_code\r\n"
				+ "WHERE t.tahasil_code = :tahasilCode  AND g.deleted_flag = '0'\r\n"
				+ "GROUP BY d.district_code, d.district_name, t.tahasil_code, t.tahasil_name, v.village_code, v.village_name\r\n"
				+ "ORDER BY t.tahasil_code, v.village_code ASC  OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("tahasilCode", tahasilCode)
				.setParameter("limit", pageSize).setParameter("offset", offset);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDistrictWiseDetails(Integer pageSize, Integer offset, String districtCode,
			Integer statusValue) {
		String sqlQuery = "SELECT district_code,tahasil_code, grievance_no,name AS applicantName,\r\n"
				+ "(SELECT CAST((JSON_AGG(json_build_object(\r\n"
				+ "'district',(select district_name from land_bank.district_master where district_code=g1.district_code ),\r\n"
				+ "'tahasil',(select tahasil_name  from land_bank.tahasil_master where  tahasil_code=g1.tahasil_code),\r\n"
				+ "'village', (select village_name from land_bank.village_master where village_code=g1.village_code),\r\n"
				+ "'khata', (select khata_no from land_bank.khatian_information where khatian_code=g1.khatian_code),\r\n"
				+ "'plot', (select plot_no from land_bank.plot_information where plot_code=g1.plot_code)\r\n"
				+ "))) AS varchar)FROM public.grievance g1\r\n"
				+ "WHERE g1.deleted_flag = '0' AND g1.grievance_no = g.grievance_no) AS landDetails,\r\n"
				+ "g.grievance_status\r\n" + "FROM public.grievance g WHERE g.deleted_flag = '0'\r\n"
				+ "AND g.grievance_status= :statusValue AND g.district_code = :districtCode "
				+ "ORDER BY g.grievance_no ASC  OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("districtCode", districtCode)
				.setParameter("statusValue", statusValue).setParameter("limit", pageSize)
				.setParameter("offset", offset);
		return query.getResultList();
	}

	@Override
	public BigInteger getDistrictWiseDetailsCount(String districtCode, Integer statusValue) {
		String sqlQuery = "SELECT COUNT(*) FROM (SELECT district_code,tahasil_code, grievance_no,name AS applicantName,\r\n"
				+ "(SELECT CAST((JSON_AGG(json_build_object(\r\n"
				+ "'district',(select district_name from land_bank.district_master where district_code=g1.district_code ),\r\n"
				+ "'tahasil',(select tahasil_name  from land_bank.tahasil_master where  tahasil_code=g1.tahasil_code),\r\n"
				+ "'village', (select village_name from land_bank.village_master where village_code=g1.village_code),\r\n"
				+ "'khata', (select khata_no from land_bank.khatian_information where khatian_code=g1.khatian_code),\r\n"
				+ "'plot', (select plot_no from land_bank.plot_information where plot_code=g1.plot_code)\r\n"
				+ "))) AS varchar)FROM public.grievance g1\r\n"
				+ "WHERE g1.deleted_flag = '0' AND g1.grievance_no = g.grievance_no) AS landDetails,\r\n"
				+ "g.grievance_status\r\n" + "FROM public.grievance g WHERE g.deleted_flag = '0'\r\n"
				+ "AND g.grievance_status= :statusValue AND g.district_code = :districtCode "
				+ "ORDER BY g.grievance_no ASC) subquery ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("districtCode", districtCode)
				.setParameter("statusValue", statusValue);
		return (BigInteger) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getTahasilStatusWiseDetails(Integer pageSize, Integer offset, String districtCode,
			String tahasilCode, Integer statusValue) {
		String sqlQuery = "SELECT district_code,tahasil_code, grievance_no,name AS applicantName,\r\n"
				+ "(SELECT CAST((JSON_AGG(json_build_object(\r\n"
				+ "'district',(select district_name from land_bank.district_master where district_code=g1.district_code ),\r\n"
				+ "'tahasil',(select tahasil_name  from land_bank.tahasil_master where  tahasil_code=g1.tahasil_code),\r\n"
				+ "'village', (select village_name from land_bank.village_master where village_code=g1.village_code),\r\n"
				+ "'khata', (select khata_no from land_bank.khatian_information where khatian_code=g1.khatian_code),\r\n"
				+ "'plot', (select plot_no from land_bank.plot_information where plot_code=g1.plot_code)\r\n"
				+ "))) AS varchar)FROM public.grievance g1\r\n"
				+ "WHERE g1.deleted_flag = '0' AND g1.grievance_no = g.grievance_no) AS landDetails,\r\n"
				+ "g.grievance_status\r\n" + "FROM public.grievance g WHERE g.deleted_flag = '0'\r\n"
				+ "AND g.grievance_status= :statusValue AND g.district_code = :districtCode AND g.tahasil_code = :tahasilCode\n"
				+ "ORDER BY g.grievance_no ASC  OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("districtCode", districtCode)
				.setParameter("tahasilCode", tahasilCode).setParameter("statusValue", statusValue)
				.setParameter("limit", pageSize).setParameter("offset", offset);
		return query.getResultList();
	}

	@Override
	public BigInteger getTahasilStatusWiseRecordCount(String districtCode, String tahasilCode, Integer statusValue) {
		String sqlQuery = "SELECT COUNT(*) FROM (SELECT district_code,tahasil_code, grievance_no,name AS applicantName,\r\n"
				+ "(SELECT CAST((JSON_AGG(json_build_object(\r\n"
				+ "'district',(select district_name from land_bank.district_master where district_code=g1.district_code ),\r\n"
				+ "'tahasil',(select tahasil_name  from land_bank.tahasil_master where  tahasil_code=g1.tahasil_code),\r\n"
				+ "'village', (select village_name from land_bank.village_master where village_code=g1.village_code),\r\n"
				+ "'khata', (select khata_no from land_bank.khatian_information where khatian_code=g1.khatian_code),\r\n"
				+ "'plot', (select plot_no from land_bank.plot_information where plot_code=g1.plot_code)\r\n"
				+ "))) AS varchar)FROM public.grievance g1\r\n"
				+ "WHERE g1.deleted_flag = '0' AND g1.grievance_no = g.grievance_no) AS landDetails,\r\n"
				+ "g.grievance_status\r\n" + "FROM public.grievance g WHERE g.deleted_flag = '0'\r\n"
				+ "AND g.grievance_status= :statusValue AND g.district_code = :districtCode AND g.tahasil_code = :tahasilCode\n"
				+ "ORDER BY g.grievance_no ASC) subquery ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("districtCode", districtCode)
				.setParameter("tahasilCode", tahasilCode).setParameter("statusValue", statusValue);
		return (BigInteger) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getVillageStatusWiseDetails(Integer pageSize, Integer offset, String districtCode,
			String tahasilCode, String villageCode, Integer statusValue) {
		String sqlQuery = "SELECT district_code,tahasil_code,village_code, grievance_no, name AS applicantName,\r\n"
				+ "(SELECT CAST((JSON_AGG(json_build_object(\r\n"
				+ "'district',(select district_name from land_bank.district_master where district_code=g1.district_code ),\r\n"
				+ "'tahasil',(select tahasil_name  from land_bank.tahasil_master where  tahasil_code=g1.tahasil_code),\r\n"
				+ "'village', (select village_name from land_bank.village_master where village_code=g1.village_code),\r\n"
				+ "'khata', (select khata_no from land_bank.khatian_information where khatian_code=g1.khatian_code),\r\n"
				+ "'plot', (select plot_no from land_bank.plot_information where plot_code=g1.plot_code)\r\n"
				+ "))) AS varchar)FROM public.grievance g1\r\n"
				+ "WHERE g1.deleted_flag = '0' AND g1.grievance_no = g.grievance_no) AS landDetails,\r\n"
				+ "g.grievance_status\r\n" + "FROM public.grievance g WHERE g.deleted_flag = '0'\r\n"
				+ "AND g.grievance_status=:statusValue  AND g.district_code =:districtCode AND g.tahasil_code =:tahasilCode\n\r"
				+ "AND g.village_code =:villageCode\r\n" + "ORDER BY g.grievance_no ASC  OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("districtCode", districtCode)
				.setParameter("tahasilCode", tahasilCode).setParameter("villageCode", villageCode)
				.setParameter("statusValue", statusValue).setParameter("limit", pageSize)
				.setParameter("offset", offset);
		return query.getResultList();
	}

	@Override
	public BigInteger getVillageStatusWiseRecordCount(String districtCode, String tahasilCode, String villageCode,
			Integer statusValue) {
		String sqlQuery = "SELECT COUNT (*) FROM (SELECT district_code,tahasil_code,village_code, grievance_no, name AS applicantName,\r\n"
				+ "(SELECT CAST((JSON_AGG(json_build_object(\r\n"
				+ "'district',(select district_name from land_bank.district_master where district_code=g1.district_code ),\r\n"
				+ "'tahasil',(select tahasil_name  from land_bank.tahasil_master where  tahasil_code=g1.tahasil_code),\r\n"
				+ "'village', (select village_name from land_bank.village_master where village_code=g1.village_code),\r\n"
				+ "'khata', (select khata_no from land_bank.khatian_information where khatian_code=g1.khatian_code),\r\n"
				+ "'plot', (select plot_no from land_bank.plot_information where plot_code=g1.plot_code)\r\n"
				+ "))) AS varchar)FROM public.grievance g1\r\n"
				+ "WHERE g1.deleted_flag = '0' AND g1.grievance_no = g.grievance_no) AS landDetails,\r\n"
				+ "g.grievance_status\r\n" + "FROM public.grievance g WHERE g.deleted_flag = '0'\r\n"
				+ "AND g.grievance_status=:statusValue  AND g.district_code =:districtCode AND g.tahasil_code =:tahasilCode AND g.village_code =:villageCode \r\n"
				+ "ORDER BY g.grievance_no ASC) subquery";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("districtCode", districtCode)
				.setParameter("tahasilCode", tahasilCode).setParameter("villageCode", villageCode)
				.setParameter("statusValue", statusValue);
		return (BigInteger) query.getSingleResult();
	}

	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getDistrictReportDataReport() {
		String sqlQuery = "select district_code,  "
				+ "(select district_name from land_bank.district_master where district_code=g1.district_code) as district_name, "
				+ "count(*) as total_aplicant, "
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=0 AND g1.district_code=g2.district_code limit 1) as pending, "
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=1 AND g1.district_code=g2.district_code limit 1) as assigned, "
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=3 AND g1.district_code=g2.district_code limit 1) as inspection_completed, "
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=2 AND g1.district_code=g2.district_code limit 1) as application_on_hold, "
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=4 AND g1.district_code=g2.district_code limit 1) as closed, "
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=5 AND g1.district_code=g2.district_code limit 1) as appl_discarded "
				+ "from public.grievance g1 WHERE deleted_flag='0'  "
				+ "GROUP BY g1.district_code ORDER BY district_name ASC";
		Query query = entity.createNativeQuery(sqlQuery);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getTahasilReportDataReport(String districtCode) {
		String sqlQuery = "select g1.district_code, "
				+ "(select district_name from land_bank.district_master where district_code=g1.district_code) as districtName, "
				+ "g1.tahasil_code,"
				+ "(select tahasil_name  from land_bank.tahasil_master where  tahasil_code=g1.tahasil_code) as tahasilName, "
				+ "count(*) as totalAplicant, "
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=0 AND g1.district_code=g2.district_code AND g1.tahasil_code=g2.tahasil_code limit 1) as pending, "
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=1 AND g1.district_code=g2.district_code AND g1.tahasil_code=g2.tahasil_code limit 1) as assigned, "
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=3 AND g1.district_code=g2.district_code AND g1.tahasil_code=g2.tahasil_code limit 1) as inspectionComplite, "
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=2 AND g1.district_code=g2.district_code AND g1.tahasil_code=g2.tahasil_code limit 1) as applicationHold, "
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=4 AND g1.district_code=g2.district_code AND g1.tahasil_code=g2.tahasil_code limit 1) as closed, "
				+ "(select count(*)  from public.grievance g2 WHERE deleted_flag='0' AND grievance_status=5 AND g1.district_code=g2.district_code AND g1.tahasil_code=g2.tahasil_code limit 1) as appDiscard "
				+ "from public.grievance g1 WHERE deleted_flag='0' and g1.district_code= :districtCode "
				+ "GROUP BY g1.district_code,g1.district_code ,g1.tahasil_code ORDER BY tahasilName ASC";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("districtCode", districtCode);
		return query.getResultList();
	}
}
