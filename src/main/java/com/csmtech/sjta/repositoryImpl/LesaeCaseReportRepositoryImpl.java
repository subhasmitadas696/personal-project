package com.csmtech.sjta.repositoryImpl;

import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.csmtech.sjta.repository.LesaeCaseReportRepository;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class LesaeCaseReportRepositoryImpl implements LesaeCaseReportRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> getDistrictWiseLeaseRecord(Integer limit, Integer offset) {
		String leaseCaseQuery = "select " + "DISTINCT ON(dm.district_code) dm.district_code,dm.district_name, "
				+ "(select count(*) from application.lease_case where lc.lease_case_id=lease_case_id) as caseCount, "
				+ "(select count(*) from application.lease_case_plot where lc.lease_case_id=lease_case_id) as plotCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND dlsc_date IS NOT NULL), "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND tlsc_date IS NOT NULL) as tlscCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND mc_date IS NOT NULL) as mcCount "
				+ "from application.lease_case_plot lcp "
				+ "JOIN land_bank.plot_information pi ON pi.plot_code = lcp.plot_code  "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code=pi.khatian_code  "
				+ "JOIN land_bank.village_master vm ON vm.village_code=ki.village_code  "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code=vm.tahasil_code  "
				+ "JOIN land_bank.district_master dm ON dm.district_code=tm.district_code "
				+ "JOIN application.lease_case lc ON(lc.lease_case_id=lcp.lease_case_id) AND lcp.deleted_flag='0' AND lc.deleted_flag='0'  "
				+ "JOIN application.lease_case_status lcs ON(lcs.lease_case_plot_id=lcp.lease_case_plot_id) "
				+ " ORDER BY dm.district_code  OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(leaseCaseQuery).setParameter("limit", limit).setParameter("offset",
				offset);
		log.info("getDistrictWiseLeaseRecord method return data success..!!");
		return query.getResultList();
	}

	@Override
	public BigInteger getDistrictWiseLeaseCount() {
		String leaseCaseQueryCount = "select count(*) from (select "
				+ "DISTINCT ON(dm.district_code) dm.district_code,dm.district_name, "
				+ "(select count(*) from application.lease_case where lc.lease_case_id=lease_case_id), "
				+ "(select count(*) from application.lease_case_plot where lc.lease_case_id=lease_case_id), "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND dlsc_date IS NOT NULL), "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND tlsc_date IS NOT NULL) as tlscCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND mc_date IS NOT NULL) as mcCount "
				+ "from application.lease_case_plot lcp "
				+ "JOIN land_bank.plot_information pi ON pi.plot_code = lcp.plot_code  "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code=pi.khatian_code  "
				+ "JOIN land_bank.village_master vm ON vm.village_code=ki.village_code  "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code=vm.tahasil_code  "
				+ "JOIN land_bank.district_master dm ON dm.district_code=tm.district_code "
				+ "JOIN application.lease_case lc ON(lc.lease_case_id=lcp.lease_case_id) "
				+ "JOIN application.lease_case_status lcs ON(lcs.lease_case_plot_id=lcp.lease_case_plot_id) AND lcp.deleted_flag='0' AND lc.deleted_flag='0'  "
				+ "ORDER BY dm.district_code) subquery";
		Query query = entity.createNativeQuery(leaseCaseQueryCount);
		log.info("getDistrictWiseLeaseCount method return data success..!!");
		return (BigInteger) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> getDistrictWiseLeaseRecordForReport() {
		String leaseCaseQuery = "select " + "DISTINCT ON(dm.district_code) dm.district_code,dm.district_name, "
				+ "(select count(*) from application.lease_case where lc.lease_case_id=lease_case_id) as caseCount, "
				+ "(select count(*) from application.lease_case_plot where lc.lease_case_id=lease_case_id) as plotCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND dlsc_date IS NOT NULL), "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND tlsc_date IS NOT NULL) as tlscCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND mc_date IS NOT NULL) as mcCount "
				+ "from application.lease_case_plot lcp "
				+ "JOIN land_bank.plot_information pi ON pi.plot_code = lcp.plot_code  "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code=pi.khatian_code  "
				+ "JOIN land_bank.village_master vm ON vm.village_code=ki.village_code  "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code=vm.tahasil_code  "
				+ "JOIN land_bank.district_master dm ON dm.district_code=tm.district_code "
				+ "JOIN application.lease_case lc ON(lc.lease_case_id=lcp.lease_case_id) "
				+ "JOIN application.lease_case_status lcs ON(lcs.lease_case_plot_id=lcp.lease_case_plot_id) AND lcp.deleted_flag='0' AND lc.deleted_flag='0'   "
				+ " ORDER BY dm.district_code   ";
		Query query = entity.createNativeQuery(leaseCaseQuery);
		log.info("getDistrictWiseLeaseRecordForReport method return data success..!!");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> getTahasilWiseLeaseRecord(Integer limit, Integer offset, String districtCode) {
		String leaseCaseQuery = "select " + "DISTINCT ON(tm.tahasil_code) tm.tahasil_code,tm.tahasil_name, "
				+ "(select count(*) from application.lease_case where lc.lease_case_id=lease_case_id) as caseCount, "
				+ "(select count(*) from application.lease_case_plot where lc.lease_case_id=lease_case_id) as plotCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND dlsc_date IS NOT NULL), "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND tlsc_date IS NOT NULL) as tlscCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND mc_date IS NOT NULL) as mcCount "
				+ "from application.lease_case_plot lcp "
				+ "JOIN land_bank.plot_information pi ON pi.plot_code = lcp.plot_code  "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code=pi.khatian_code  "
				+ "JOIN land_bank.village_master vm ON vm.village_code=ki.village_code  "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code=vm.tahasil_code  "
				+ "JOIN land_bank.district_master dm ON dm.district_code=tm.district_code "
				+ "JOIN application.lease_case lc ON(lc.lease_case_id=lcp.lease_case_id) "
				+ "JOIN application.lease_case_status lcs ON(lcs.lease_case_plot_id=lcp.lease_case_plot_id)  "
				+ "WHERE dm.district_code=:districtCode AND lcp.deleted_flag='0' AND lc.deleted_flag='0'   ORDER BY tm.tahasil_code OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(leaseCaseQuery).setParameter("limit", limit)
				.setParameter("offset", offset).setParameter("districtCode", districtCode);
		log.info("getTahasilWiseLeaseRecord method return data success..!!");
		return query.getResultList();
	}

	@Override
	public BigInteger getTahasilWiseLeaseCount(String districtCode) {
		String leaseCaseQueryCount = "select count(*) from (select "
				+ "DISTINCT ON(tm.tahasil_code) tm.tahasil_code,tm.tahasil_name, "
				+ "(select count(*) from application.lease_case where lc.lease_case_id=lease_case_id), "
				+ "(select count(*) from application.lease_case_plot where lc.lease_case_id=lease_case_id), "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND dlsc_date IS NOT NULL), "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND tlsc_date IS NOT NULL) as tlscCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND mc_date IS NOT NULL) as mcCount "
				+ "from application.lease_case_plot lcp "
				+ "JOIN land_bank.plot_information pi ON pi.plot_code = lcp.plot_code  "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code=pi.khatian_code  "
				+ "JOIN land_bank.village_master vm ON vm.village_code=ki.village_code  "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code=vm.tahasil_code  "
				+ "JOIN land_bank.district_master dm ON dm.district_code=tm.district_code "
				+ "JOIN application.lease_case lc ON(lc.lease_case_id=lcp.lease_case_id) "
				+ "JOIN application.lease_case_status lcs ON(lcs.lease_case_plot_id=lcp.lease_case_plot_id)  "
				+ "WHERE dm.district_code=:districtCode AND lcp.deleted_flag='0' AND lc.deleted_flag='0'   ORDER BY tm.tahasil_code) as subquery ";
		Query query = entity.createNativeQuery(leaseCaseQueryCount).setParameter("districtCode", districtCode);
		log.info("getTahasilWiseLeaseCount method return data success..!!");
		return (BigInteger) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> getTahasilWiseLeaseRecordForReport(String districtCode) {
		String leaseCaseQuery = "select " + "DISTINCT ON(tm.tahasil_code) tm.tahasil_code,tm.tahasil_name, "
				+ "(select count(*) from application.lease_case where lc.lease_case_id=lease_case_id) as caseCount, "
				+ "(select count(*) from application.lease_case_plot where lc.lease_case_id=lease_case_id) as plotCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND dlsc_date IS NOT NULL), "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND tlsc_date IS NOT NULL) as tlscCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND mc_date IS NOT NULL) as mcCount "
				+ "from application.lease_case_plot lcp "
				+ "JOIN land_bank.plot_information pi ON pi.plot_code = lcp.plot_code  "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code=pi.khatian_code  "
				+ "JOIN land_bank.village_master vm ON vm.village_code=ki.village_code  "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code=vm.tahasil_code  "
				+ "JOIN land_bank.district_master dm ON dm.district_code=tm.district_code "
				+ "JOIN application.lease_case lc ON(lc.lease_case_id=lcp.lease_case_id) "
				+ "JOIN application.lease_case_status lcs ON(lcs.lease_case_plot_id=lcp.lease_case_plot_id)  "
				+ "WHERE dm.district_code=:districtCode AND lcp.deleted_flag='0' AND lc.deleted_flag='0'   ORDER BY tm.tahasil_code  ";
		Query query = entity.createNativeQuery(leaseCaseQuery).setParameter("districtCode", districtCode);
		log.info("getTahasilWiseLeaseRecordForReport method return data success..!!");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> getVillageWiseLeaseRecord(Integer limit, Integer offset, String tahasilCode) {
		String leaseCaseQuery = " select " + "DISTINCT ON(vm.village_code) vm.village_code,vm.village_name, "
				+ "(select count(*) from application.lease_case where lc.lease_case_id=lease_case_id) as caseCount, "
				+ "(select count(*) from application.lease_case_plot where lc.lease_case_id=lease_case_id) as plotCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND dlsc_date IS NOT NULL), "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND tlsc_date IS NOT NULL) as tlscCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND mc_date IS NOT NULL) as mcCount "
				+ "from application.lease_case_plot lcp "
				+ "JOIN land_bank.plot_information pi ON pi.plot_code = lcp.plot_code  "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code=pi.khatian_code  "
				+ "JOIN land_bank.village_master vm ON vm.village_code=ki.village_code  "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code=vm.tahasil_code  "
				+ "JOIN land_bank.district_master dm ON dm.district_code=tm.district_code "
				+ "JOIN application.lease_case lc ON(lc.lease_case_id=lcp.lease_case_id) "
				+ "JOIN application.lease_case_status lcs ON(lcs.lease_case_plot_id=lcp.lease_case_plot_id)  "
				+ "WHERE tm.tahasil_code=:tahasilCode AND lcp.deleted_flag='0' AND lc.deleted_flag='0'   ORDER BY vm.village_code "
				+ "OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(leaseCaseQuery).setParameter("limit", limit)
				.setParameter("offset", offset).setParameter("tahasilCode", tahasilCode);
		log.info("getVillageWiseLeaseRecord method return data success..!!");
		return query.getResultList();
	}

	@Override
	public BigInteger getVillageWiseLeaseCount(String tahasilCode) {
		String leaseCaseQueryCount = "select count(*) from (select "
				+ "DISTINCT ON(vm.village_code) vm.village_code,vm.village_name, "
				+ "(select count(*) from application.lease_case where lc.lease_case_id=lease_case_id) as caseCount, "
				+ "(select count(*) from application.lease_case_plot where lc.lease_case_id=lease_case_id) as plotCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND dlsc_date IS NOT NULL), "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND tlsc_date IS NOT NULL) as tlscCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND mc_date IS NOT NULL) as mcCount "
				+ "from application.lease_case_plot lcp "
				+ "JOIN land_bank.plot_information pi ON pi.plot_code = lcp.plot_code  "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code=pi.khatian_code  "
				+ "JOIN land_bank.village_master vm ON vm.village_code=ki.village_code  "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code=vm.tahasil_code  "
				+ "JOIN land_bank.district_master dm ON dm.district_code=tm.district_code "
				+ "JOIN application.lease_case lc ON(lc.lease_case_id=lcp.lease_case_id) "
				+ "JOIN application.lease_case_status lcs ON(lcs.lease_case_plot_id=lcp.lease_case_plot_id)  "
				+ "WHERE tm.tahasil_code=:tahasilCode AND lcp.deleted_flag='0' AND lc.deleted_flag='0'   ORDER BY vm.village_code) subquery ";
		Query query = entity.createNativeQuery(leaseCaseQueryCount).setParameter("tahasilCode", tahasilCode);
		log.info("getVillageWiseLeaseCount method return data success..!!");
		return (BigInteger) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> getVillageWiseLeaseRecordForReport(String tahasilCode) {
		String leaseCaseQuery = " select " + "DISTINCT ON(vm.village_code) vm.village_code,vm.village_name, "
				+ "(select count(*) from application.lease_case where lc.lease_case_id=lease_case_id) as caseCount, "
				+ "(select count(*) from application.lease_case_plot where lc.lease_case_id=lease_case_id) as plotCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND dlsc_date IS NOT NULL), "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND tlsc_date IS NOT NULL) as tlscCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND mc_date IS NOT NULL) as mcCount "
				+ "from application.lease_case_plot lcp "
				+ "JOIN land_bank.plot_information pi ON pi.plot_code = lcp.plot_code  "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code=pi.khatian_code  "
				+ "JOIN land_bank.village_master vm ON vm.village_code=ki.village_code  "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code=vm.tahasil_code  "
				+ "JOIN land_bank.district_master dm ON dm.district_code=tm.district_code "
				+ "JOIN application.lease_case lc ON(lc.lease_case_id=lcp.lease_case_id) "
				+ "JOIN application.lease_case_status lcs ON(lcs.lease_case_plot_id=lcp.lease_case_plot_id)  "
				+ "WHERE tm.tahasil_code=:tahasilCode AND lcp.deleted_flag='0' AND lc.deleted_flag='0'   ORDER BY vm.village_code ";
		Query query = entity.createNativeQuery(leaseCaseQuery).setParameter("tahasilCode", tahasilCode);
		log.info("getVillageWiseLeaseRecordForReport method return data success..!!");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> getKhataWiseLeaseRecord(Integer limit, Integer offset, String villageCode) {
		String leaseCaseQuery = " select " + "DISTINCT ON(ki.khatian_code) ki.khatian_code,ki.khata_no, "
				+ "(select count(*) from application.lease_case where lc.lease_case_id=lease_case_id) as caseCount, "
				+ "(select count(*) from application.lease_case_plot where lc.lease_case_id=lease_case_id) as plotCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND dlsc_date IS NOT NULL), "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND tlsc_date IS NOT NULL) as tlscCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND mc_date IS NOT NULL) as mcCount "
				+ "from application.lease_case_plot lcp "
				+ "JOIN land_bank.plot_information pi ON pi.plot_code = lcp.plot_code  "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code=pi.khatian_code  "
				+ "JOIN land_bank.village_master vm ON vm.village_code=ki.village_code  "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code=vm.tahasil_code  "
				+ "JOIN land_bank.district_master dm ON dm.district_code=tm.district_code "
				+ "JOIN application.lease_case lc ON(lc.lease_case_id=lcp.lease_case_id) "
				+ "JOIN application.lease_case_status lcs ON(lcs.lease_case_plot_id=lcp.lease_case_plot_id)  "
				+ "WHERE vm.village_code=:villageCode AND lcp.deleted_flag='0' AND lc.deleted_flag='0'   ORDER BY ki.khatian_code "
				+ "OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(leaseCaseQuery).setParameter("limit", limit)
				.setParameter("offset", offset).setParameter("villageCode", villageCode);
		log.info("getKhataWiseLeaseRecord method return data success..!!");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> getPlotWiseLeaseRecord(Integer limit, Integer offset, String khataCode) {
		String leaseCaseQuery = " select " + " pi.plot_no,pi.plot_code, "
				+ "lcs.field_enquiry,lcs.dlsc_date,lcs.tlsc_date,lcs.mc_date,  "
				+ "lcs.notice_issued,lcs.consideration_money_deposited, " + "lcs.status,lcs.remarks  "
				+ "from application.lease_case_plot lcp "
				+ "JOIN land_bank.plot_information pi ON pi.plot_code = lcp.plot_code  "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code=pi.khatian_code  "
				+ "JOIN land_bank.village_master vm ON vm.village_code=ki.village_code  "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code=vm.tahasil_code  "
				+ "JOIN land_bank.district_master dm ON dm.district_code=tm.district_code "
				+ "JOIN application.lease_case lc ON(lc.lease_case_id=lcp.lease_case_id) "
				+ "JOIN application.lease_case_status lcs ON(lcs.lease_case_plot_id=lcp.lease_case_plot_id)  "
				+ "WHERE ki.khatian_code=:khataCode AND lcp.deleted_flag='0' AND lc.deleted_flag='0'   ORDER BY pi.plot_no "
				+ "OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(leaseCaseQuery).setParameter("limit", limit)
				.setParameter("offset", offset).setParameter("khataCode", khataCode);
		log.info("getPlotWiseLeaseRecord method return data success..!!");
		return query.getResultList();
	}

	@Override
	public BigInteger getKhataWiseLeaseCount(String villageCode) {
		String leaseCaseQueryCount = "  select count (*) from (select "
				+ "DISTINCT ON(ki.khatian_code) ki.khatian_code,ki.khata_no, "
				+ "(select count(*) from application.lease_case where lc.lease_case_id=lease_case_id) as caseCount, "
				+ "(select count(*) from application.lease_case_plot where lc.lease_case_id=lease_case_id) as plotCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND dlsc_date IS NOT NULL), "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND tlsc_date IS NOT NULL) as tlscCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND mc_date IS NOT NULL) as mcCount "
				+ "from application.lease_case_plot lcp "
				+ "JOIN land_bank.plot_information pi ON pi.plot_code = lcp.plot_code  "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code=pi.khatian_code  "
				+ "JOIN land_bank.village_master vm ON vm.village_code=ki.village_code  "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code=vm.tahasil_code  "
				+ "JOIN land_bank.district_master dm ON dm.district_code=tm.district_code "
				+ "JOIN application.lease_case lc ON(lc.lease_case_id=lcp.lease_case_id) "
				+ "JOIN application.lease_case_status lcs ON(lcs.lease_case_plot_id=lcp.lease_case_plot_id)  "
				+ "WHERE vm.village_code=:villageCode  AND lcp.deleted_flag='0' AND lc.deleted_flag='0'   ORDER BY ki.khatian_code) subquery ";
		Query query = entity.createNativeQuery(leaseCaseQueryCount).setParameter("villageCode", villageCode);
		log.info("getKhataWiseLeaseCount method return data success..!!");
		return (BigInteger) query.getSingleResult();
	}

	@Override
	public BigInteger getPlotWiseLeaseCount(String khataCode) {
		String leaseCaseQueryCount = " select count(*) from (select "
				+ "DISTINCT ON(pi.plot_no) pi.plot_no,pi.plot_code, lc.case_year,lc.case_no,lc.applicant_name "
				+ "from application.lease_case_plot lcp "
				+ "JOIN land_bank.plot_information pi ON pi.plot_code = lcp.plot_code  "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code=pi.khatian_code  "
				+ "JOIN land_bank.village_master vm ON vm.village_code=ki.village_code  "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code=vm.tahasil_code  "
				+ "JOIN land_bank.district_master dm ON dm.district_code=tm.district_code "
				+ "JOIN application.lease_case lc ON(lc.lease_case_id=lcp.lease_case_id) "
				+ "JOIN application.lease_case_status lcs ON(lcs.lease_case_plot_id=lcp.lease_case_plot_id)  "
				+ "WHERE ki.khatian_code=:khataCode AND lcp.deleted_flag='0' AND lc.deleted_flag='0'   ORDER BY pi.plot_no) subquery ";
		Query query = entity.createNativeQuery(leaseCaseQueryCount).setParameter("khataCode", khataCode);
		log.info("getPlotWiseLeaseCount method return data success..!!");
		return (BigInteger) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> getKhataWiseLeaseRecordForReport(String villageCode) {
		String leaseCaseQuery = " select " + "DISTINCT ON(ki.khatian_code) ki.khatian_code,ki.khata_no, "
				+ "(select count(*) from application.lease_case where lc.lease_case_id=lease_case_id) as caseCount, "
				+ "(select count(*) from application.lease_case_plot where lc.lease_case_id=lease_case_id) as plotCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND dlsc_date IS NOT NULL), "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND tlsc_date IS NOT NULL) as tlscCount, "
				+ "(select count(*) from application.lease_case_status lcs1 "
				+ "JOIN application.lease_case_plot lcp1 ON(lcp1.lease_case_plot_id=lcs1.lease_case_plot_id) "
				+ "JOIN application.lease_case lc1 ON(lc1.lease_case_id=lcp1.lease_case_id) "
				+ "where lc1.lease_case_id=lc.lease_case_id AND mc_date IS NOT NULL) as mcCount "
				+ "from application.lease_case_plot lcp "
				+ "JOIN land_bank.plot_information pi ON pi.plot_code = lcp.plot_code  "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code=pi.khatian_code  "
				+ "JOIN land_bank.village_master vm ON vm.village_code=ki.village_code  "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code=vm.tahasil_code  "
				+ "JOIN land_bank.district_master dm ON dm.district_code=tm.district_code "
				+ "JOIN application.lease_case lc ON(lc.lease_case_id=lcp.lease_case_id) "
				+ "JOIN application.lease_case_status lcs ON(lcs.lease_case_plot_id=lcp.lease_case_plot_id)  "
				+ "WHERE vm.village_code=:villageCode AND lcp.deleted_flag='0' AND lc.deleted_flag='0'   ORDER BY ki.khatian_code ";
		Query query = entity.createNativeQuery(leaseCaseQuery).setParameter("villageCode", villageCode);
		log.info("getKhataWiseLeaseRecordForReport method return data success..!!");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> getPlotWiseLeaseRecordForReport(String khataCode) {
		String leaseCaseQuery = " select " + " pi.plot_no,pi.plot_code, "
				+ "lcs.field_enquiry,lcs.dlsc_date,lcs.tlsc_date,lcs.mc_date,  "
				+ "lcs.notice_issued,lcs.consideration_money_deposited, " + "lcs.status,lcs.remarks  "
				+ "from application.lease_case_plot lcp "
				+ "JOIN land_bank.plot_information pi ON pi.plot_code = lcp.plot_code  "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code=pi.khatian_code  "
				+ "JOIN land_bank.village_master vm ON vm.village_code=ki.village_code  "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code=vm.tahasil_code  "
				+ "JOIN land_bank.district_master dm ON dm.district_code=tm.district_code "
				+ "JOIN application.lease_case lc ON(lc.lease_case_id=lcp.lease_case_id) "
				+ "JOIN application.lease_case_status lcs ON(lcs.lease_case_plot_id=lcp.lease_case_plot_id)  "
				+ "WHERE ki.khatian_code=:khataCode AND lcp.deleted_flag='0' AND lc.deleted_flag='0'   ORDER BY pi.plot_no ";
		Query query = entity.createNativeQuery(leaseCaseQuery).setParameter("khataCode", khataCode);
		log.info("getPlotWiseLeaseRecordForReport method return data success..!!");
		return query.getResultList();
	}

}
