package com.csmtech.sjta.repositoryImpl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.LandAllotmentReportDTO;
import com.csmtech.sjta.dto.LandAllotmentTahasilReportDTO;
import com.csmtech.sjta.dto.LandAllotmentVillageReportDTO;
import com.csmtech.sjta.repository.LandAllotmentReportRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class LandAllotmentReportRepositoryImpl implements LandAllotmentReportRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public List<LandAllotmentReportDTO> getDistrictWiseLandAllotmentReport(Integer pagesize, Integer offset) {
		String sqlQuery = "SELECT  dm.district_code as districtCode, dm.district_name as districtName, "
				+ "(select count(*) from application.meeting_schedule ms1  "
				+ "LEFT JOIN application.land_allotement la1  ON(ms1.meeting_schedule_id=la1.meeting_schedule_id) "
				+ "LEFT JOIN application.meeting_schedule_applicant msa1 ON(msa1.meeting_schedule_id=ms1.meeting_schedule_id)  "
				+ "LEFT JOIN land_bank.plot_information pi1 ON(msa1.plot_no = pi1.plot_code) "
				+ "LEFT JOIN land_bank.khatian_information ki1 ON(pi1.khatian_code = ki1.khatian_code)  "
				+ "LEFT JOIN land_bank.village_master vm1 USING(village_code)  "
				+ "LEFT JOIN land_bank.tahasil_master tm1 USING(tahasil_code)  "
				+ "LEFT JOIN land_bank.district_master dm1 USING(district_code) "
				+ "where ms1.meeting_level_id=4 AND ms1.status='0'  AND la1.meeting_schedule_id IS NULL  "
				+ "AND dm1.district_code=dm.district_code) as pendingCount, "
				+ "(select count(*) from application.land_allotement la2 where la2.meeting_schedule_id=la.meeting_schedule_id AND la2.form_16_docs IS NOT NULL) as form16Count, "
				+ "(select count(*) from application.land_allotement la2 where la2.meeting_schedule_id=la.meeting_schedule_id AND  la2.form_register_docs  IS NOT NULL) as registerCount "
				+ "from application.land_allotement la  "
				+ "JOIN land_bank.plot_information pi ON(la.plot_code = pi.plot_code) "
				+ "JOIN land_bank.khatian_information ki ON(pi.khatian_code = ki.khatian_code)  "
				+ "JOIN land_bank.village_master vm USING(village_code)  "
				+ "JOIN land_bank.tahasil_master tm USING(tahasil_code)  "
				+ "JOIN land_bank.district_master dm USING(district_code) " + "where  deleted_flag='0' "
				+ "OFFSET :offset  limit :limit  ";
		Query query = entity.createNativeQuery(sqlQuery, LandAllotmentReportDTO.class).setParameter("limit", pagesize)
				.setParameter("offset", offset);
		log.info("getDistrictWiseLandAllotmentReport execution  success..!!");
		return query.getResultList();
	}

	@Transactional
	@Override
	public BigInteger getDistrictWiseLandAllotmentReportCount() {
		String sqlQuery = "select count(*) from (SELECT  dm.district_code, dm.district_name, "
				+ "(select count(*) from application.meeting_schedule ms1  "
				+ "LEFT JOIN application.land_allotement la1  ON(ms1.meeting_schedule_id=la1.meeting_schedule_id) "
				+ "LEFT JOIN application.meeting_schedule_applicant msa1 ON(msa1.meeting_schedule_id=ms1.meeting_schedule_id)  "
				+ "LEFT JOIN land_bank.plot_information pi1 ON(msa1.plot_no = pi1.plot_code) "
				+ "LEFT JOIN land_bank.khatian_information ki1 ON(pi1.khatian_code = ki1.khatian_code)  "
				+ "LEFT JOIN land_bank.village_master vm1 USING(village_code)  "
				+ "LEFT JOIN land_bank.tahasil_master tm1 USING(tahasil_code)  "
				+ "LEFT JOIN land_bank.district_master dm1 USING(district_code) "
				+ "where ms1.meeting_level_id=4 AND ms1.status='0'  AND la1.meeting_schedule_id IS NULL  "
				+ "AND dm1.district_code=dm.district_code) as pendingCount, "
				+ "(select count(*) from application.land_allotement la2 where la2.meeting_schedule_id=la.meeting_schedule_id AND la2.form_16_docs IS NOT NULL) as form16Count, "
				+ "(select count(*) from application.land_allotement la2 where la2.meeting_schedule_id=la.meeting_schedule_id AND  la2.form_register_docs  IS NOT NULL) as registerCount "
				+ "from application.land_allotement la  "
				+ "JOIN land_bank.plot_information pi ON(la.plot_code = pi.plot_code) "
				+ "JOIN land_bank.khatian_information ki ON(pi.khatian_code = ki.khatian_code)  "
				+ "JOIN land_bank.village_master vm USING(village_code)  "
				+ "JOIN land_bank.tahasil_master tm USING(tahasil_code)  "
				+ "JOIN land_bank.district_master dm USING(district_code) " + "where  deleted_flag='0' ) as subquery ";
		Query query = entity.createNativeQuery(sqlQuery);
		log.info("getDistrictWiseLandAllotmentReportCount execution  success..!!");
		return (BigInteger) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LandAllotmentTahasilReportDTO> getTahasilWiseLandAllotmentReport(Integer pageSize, Integer offset,
			String districtCode) {
		String sqlQuery = "SELECT  dm.district_code as districtCode, dm.district_name as districtName,tm.tahasil_code as tahasilCode , tm.tahasil_name as tahasilName, "
				+ "(select count(*) from application.meeting_schedule ms1  "
				+ "LEFT JOIN application.land_allotement la1  ON(ms1.meeting_schedule_id=la1.meeting_schedule_id) "
				+ "LEFT JOIN application.meeting_schedule_applicant msa1 ON(msa1.meeting_schedule_id=ms1.meeting_schedule_id)  "
				+ "LEFT JOIN land_bank.plot_information pi1 ON(msa1.plot_no = pi1.plot_code) "
				+ "LEFT JOIN land_bank.khatian_information ki1 ON(pi1.khatian_code = ki1.khatian_code)  "
				+ "LEFT JOIN land_bank.village_master vm1 USING(village_code)  "
				+ "LEFT JOIN land_bank.tahasil_master tm1 USING(tahasil_code)  "
				+ "LEFT JOIN land_bank.district_master dm1 USING(district_code) "
				+ "where ms1.meeting_level_id=4 AND ms1.status='0'  AND la1.meeting_schedule_id IS NULL  "
				+ "AND dm1.district_code=dm.district_code) as pendingCount, "
				+ "(select count(*) from application.land_allotement la2 where la2.meeting_schedule_id=la.meeting_schedule_id AND la2.form_16_docs IS NOT NULL) as form16Count, "
				+ "(select count(*) from application.land_allotement la2 where la2.meeting_schedule_id=la.meeting_schedule_id AND  la2.form_register_docs  IS NOT NULL) as registerCount "
				+ "from application.land_allotement la  "
				+ "JOIN land_bank.plot_information pi ON(la.plot_code = pi.plot_code) "
				+ "JOIN land_bank.khatian_information ki ON(pi.khatian_code = ki.khatian_code)  "
				+ "JOIN land_bank.village_master vm USING(village_code)  "
				+ "JOIN land_bank.tahasil_master tm USING(tahasil_code)  "
				+ "JOIN land_bank.district_master dm USING(district_code) "
				+ "where  deleted_flag='0' AND dm.district_code=:districtCode " + "OFFSET :offset  limit :limit  ";
		Query query = entity.createNativeQuery(sqlQuery, LandAllotmentTahasilReportDTO.class)
				.setParameter("limit", pageSize).setParameter("offset", offset)
				.setParameter("districtCode", districtCode);
		log.info("getDistrictWiseLandAllotmentReport execution  success..!!");
		return query.getResultList();
	}

	@Override
	public BigInteger getTahasilWiseLandAllotmentReportCount(String districtCode) {
		String sqlQuery = "select count (*) from (SELECT  tm.tahasil_code, tm.tahasil_name, "
				+ "(select count(*) from application.meeting_schedule ms1  "
				+ "LEFT JOIN application.land_allotement la1  ON(ms1.meeting_schedule_id=la1.meeting_schedule_id) "
				+ "LEFT JOIN application.meeting_schedule_applicant msa1 ON(msa1.meeting_schedule_id=ms1.meeting_schedule_id)  "
				+ "LEFT JOIN land_bank.plot_information pi1 ON(msa1.plot_no = pi1.plot_code) "
				+ "LEFT JOIN land_bank.khatian_information ki1 ON(pi1.khatian_code = ki1.khatian_code)  "
				+ "LEFT JOIN land_bank.village_master vm1 USING(village_code)  "
				+ "LEFT JOIN land_bank.tahasil_master tm1 USING(tahasil_code)  "
				+ "LEFT JOIN land_bank.district_master dm1 USING(district_code) "
				+ "where ms1.meeting_level_id=4 AND ms1.status='0'  AND la1.meeting_schedule_id IS NULL  "
				+ "AND dm1.district_code=dm.district_code) as pendingCount, "
				+ "(select count(*) from application.land_allotement la2 where la2.meeting_schedule_id=la.meeting_schedule_id AND la2.form_16_docs IS NOT NULL) as form16Count, "
				+ "(select count(*) from application.land_allotement la2 where la2.meeting_schedule_id=la.meeting_schedule_id AND  la2.form_register_docs  IS NOT NULL) as registerCount "
				+ "from application.land_allotement la  "
				+ "JOIN land_bank.plot_information pi ON(la.plot_code = pi.plot_code) "
				+ "JOIN land_bank.khatian_information ki ON(pi.khatian_code = ki.khatian_code)  "
				+ "JOIN land_bank.village_master vm USING(village_code)  "
				+ "JOIN land_bank.tahasil_master tm USING(tahasil_code)  "
				+ "JOIN land_bank.district_master dm USING(district_code) "
				+ "where  deleted_flag='0' AND dm.district_code=:districtCode) as subquery";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("districtCode", districtCode);
		log.info("getTahasilWiseLandAllotmentReportCount execution  success..!!");
		return (BigInteger) query.getSingleResult();
	}

	@Override
	public List<LandAllotmentVillageReportDTO> getVillageWiseLandAllotmentReport(Integer pageSize, Integer offset,
			String tahasilCode) {
		String sqlQuery = "SELECT  dm.district_code as districtCode, dm.district_name as districtName, tm.tahasil_code as tahasilCode, tm.tahasil_name as tahasilName ,vm.village_code as villageCode, vm.village_name as villageName, "
				+ "(select count(*) from application.meeting_schedule ms1  "
				+ "LEFT JOIN application.land_allotement la1  ON(ms1.meeting_schedule_id=la1.meeting_schedule_id) "
				+ "LEFT JOIN application.meeting_schedule_applicant msa1 ON(msa1.meeting_schedule_id=ms1.meeting_schedule_id)  "
				+ "LEFT JOIN land_bank.plot_information pi1 ON(msa1.plot_no = pi1.plot_code) "
				+ "LEFT JOIN land_bank.khatian_information ki1 ON(pi1.khatian_code = ki1.khatian_code)  "
				+ "LEFT JOIN land_bank.village_master vm1 USING(village_code)  "
				+ "LEFT JOIN land_bank.tahasil_master tm1 USING(tahasil_code)  "
				+ "LEFT JOIN land_bank.district_master dm1 USING(district_code) "
				+ "where ms1.meeting_level_id=4 AND ms1.status='0'  AND la1.meeting_schedule_id IS NULL  "
				+ "AND dm1.district_code=dm.district_code) as pendingCount, "
				+ "(select count(*) from application.land_allotement la2 where la2.meeting_schedule_id=la.meeting_schedule_id AND la2.form_16_docs IS NOT NULL) as form16Count, "
				+ "(select count(*) from application.land_allotement la2 where la2.meeting_schedule_id=la.meeting_schedule_id AND  la2.form_register_docs  IS NOT NULL) as registerCount "
				+ "from application.land_allotement la  "
				+ "JOIN land_bank.plot_information pi ON(la.plot_code = pi.plot_code) "
				+ "JOIN land_bank.khatian_information ki ON(pi.khatian_code = ki.khatian_code)  "
				+ "JOIN land_bank.village_master vm USING(village_code)  "
				+ "JOIN land_bank.tahasil_master tm USING(tahasil_code)  "
				+ "JOIN land_bank.district_master dm USING(district_code) "
				+ "where  deleted_flag='0' AND vm.tahasil_code=:tahasilCode " + "OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery, LandAllotmentVillageReportDTO.class)
				.setParameter("limit", pageSize).setParameter("offset", offset)
				.setParameter("tahasilCode", tahasilCode);
		log.info("getDistrictWiseLandAllotmentReport execution  success..!!");
		return query.getResultList();
	}

	@Override
	public BigInteger getVillageWiseLandAllotmentReportCount(String tahasilCode) {
		String sqlQuery = "SELECT COUNT(*) FROM (SELECT  dm.district_code, dm.district_name, tm.tahasil_code, tm.tahasil_name,vm.village_code, vm.village_name, "
				+ "(select count(*) from application.meeting_schedule ms1  "
				+ "LEFT JOIN application.land_allotement la1  ON(ms1.meeting_schedule_id=la1.meeting_schedule_id) "
				+ "LEFT JOIN application.meeting_schedule_applicant msa1 ON(msa1.meeting_schedule_id=ms1.meeting_schedule_id)  "
				+ "LEFT JOIN land_bank.plot_information pi1 ON(msa1.plot_no = pi1.plot_code) "
				+ "LEFT JOIN land_bank.khatian_information ki1 ON(pi1.khatian_code = ki1.khatian_code)  "
				+ "LEFT JOIN land_bank.village_master vm1 USING(village_code)  "
				+ "LEFT JOIN land_bank.tahasil_master tm1 USING(tahasil_code)  "
				+ "LEFT JOIN land_bank.district_master dm1 USING(district_code) "
				+ "where ms1.meeting_level_id=4 AND ms1.status='0'  AND la1.meeting_schedule_id IS NULL  "
				+ "AND dm1.district_code=dm.district_code) as pendingCount, "
				+ "(select count(*) from application.land_allotement la2 where la2.meeting_schedule_id=la.meeting_schedule_id AND la2.form_16_docs IS NOT NULL) as form16Count, "
				+ "(select count(*) from application.land_allotement la2 where la2.meeting_schedule_id=la.meeting_schedule_id AND  la2.form_register_docs  IS NOT NULL) as registerCount "
				+ "from application.land_allotement la  "
				+ "JOIN land_bank.plot_information pi ON(la.plot_code = pi.plot_code) "
				+ "JOIN land_bank.khatian_information ki ON(pi.khatian_code = ki.khatian_code)  "
				+ "JOIN land_bank.village_master vm USING(village_code)  "
				+ "JOIN land_bank.tahasil_master tm USING(tahasil_code)  "
				+ "JOIN land_bank.district_master dm USING(district_code) "
				+ "where  deleted_flag='0' AND vm.tahasil_code=:tahasilCode) AS subquery";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("tahasilCode", tahasilCode);
		log.info("getTahasilWiseLandAllotmentReportCount execution  success..!!");
		return (BigInteger) query.getSingleResult();
	}

}
