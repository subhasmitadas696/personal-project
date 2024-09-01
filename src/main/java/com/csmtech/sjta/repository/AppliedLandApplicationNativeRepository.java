package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.AppliedLandApplicationDTO;
import com.csmtech.sjta.dto.LandApplicantDTO;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AppliedLandApplicationNativeRepository {

	@PersistenceContext
	@Autowired
	EntityManager entityManager;

	@Transactional
	public List<AppliedLandApplicationDTO> getAppliedLandApplicantDetailsPage(Integer createdBy, Integer pageNumber,
			Integer pageSize) {
		String query = "SELECT DISTINCT la.land_application_id,la.application_no,la.applicant_name,"
				+ "la.mobile_no,(select district_name from land_bank.district_master "
				+ "where la.district_code=district_code limit 1) as dist_name,(SELECT DISTINCT tm.tahasil_name "
				+ "FROM public.land_schedule ls JOIN land_bank.plot_information pi ON pi.plot_code = ls.plot_code "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code = pi.khatian_code "
				+ "JOIN land_bank.village_master vm ON vm.village_code = ki.village_code "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code = vm.tahasil_code "
				+ "WHERE ls.plot_code = ls.plot_code limit 1) as tahasil_name,"
				+ "(select village_name from land_bank.village_master  where la.village_code=village_code limit 1) as  village_name,"
				+ "(select khata_no from land_bank.khatian_information  where la.khatian_code=khatian_code limit 1) as  khata_no,"
				+ "G.application_status,F.action_on, F.remark,"
				+ "F.application_status_id,pp.pending_at_role_id,la.district_code,la.tehsil_code,la.village_code FROM "
				+ "public.land_application la LEFT JOIN land_application_approval F  "
				+ "on la.land_application_id=F.land_application_id LEFT JOIN m_application_status G "
				+ "on F.application_status_id= G.application_status_id LEFT JOIN "
				+ "public.land_application_approval pp  " + "on la.land_application_id= pp.land_application_id  "
				+ "WHERE la.created_by=:createdBy AND la.deleted_flag = '0' "
				+ "ORDER BY la.land_application_id DESC LIMIT :pageSize OFFSET :offset";

		int offset = (pageNumber - 1) * pageSize;

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = entityManager.createNativeQuery(query).setParameter("createdBy", createdBy)
				.setParameter("pageSize", pageSize).setParameter("offset", offset).getResultList();

		entityManager.close();
		return transformResultList(resultList);
	}

	@Transactional
	private List<AppliedLandApplicationDTO> transformResultList(List<Object[]> resultList) {
		List<AppliedLandApplicationDTO> roleInfoList = new ArrayList<>();
		for (Object[] row : resultList) {
			BigInteger landApplicantId = (BigInteger) row[0];
			String applicantNo = (String) row[1];
			String applicantName = (String) row[2];
			String mobileNo = (String) row[3];
			String districtName = (String) row[4];
			String tehsilName = (String) row[5];
			String mouzaName = (String) row[6];
			String khataNo = (String) row[7];
			String applicationStatus = (String) row[8];
			Date actionOn = (Date) row[9];
			String remark = (String) row[10];
			Short applicationStatusId = (Short) row[11];
			Short pendingroleid = (Short) row[12];
			String districtCode = (String) row[13];
			String tahasilCode = (String) row[14];
			String villageCode = (String) row[15];

			AppliedLandApplicationDTO roleInfo = new AppliedLandApplicationDTO();
			roleInfo.setLandApplicantId(landApplicantId);
			roleInfo.setApplicantNo(applicantNo);
			roleInfo.setApplicantName(applicantName);
			roleInfo.setMobileNo(mobileNo);
			roleInfo.setDistrictName(districtName);
			roleInfo.setTehsilName(tehsilName);
			roleInfo.setMouzaName(mouzaName);
			roleInfo.setKhataNo(khataNo);
			roleInfo.setApplicationStatus(applicationStatus);
			roleInfo.setActionOn(actionOn);
			roleInfo.setRemark(remark);
			roleInfo.setApplicationStatusId(applicationStatusId);
			roleInfo.setPendingroleid(pendingroleid);
			roleInfo.setDistrictCode(districtCode);
			roleInfo.setTahasilCode(tahasilCode);
			roleInfo.setVillageCode(villageCode);
			roleInfoList.add(roleInfo);

		}
		return roleInfoList;
	}

	@Transactional
	public Integer getTotalLandApplicantCount(Integer createdBy) {
		String query = "SELECT COUNT(*) FROM public.land_application la " + "INNER JOIN land_bank.district_master d "
				+ "ON la.district_code = d.district_code INNER JOIN "
				+ "land_bank.tahasil_master t ON la.tehsil_code = t.tahasil_code "
				+ "INNER JOIN land_bank.village_master m " + "ON la.village_code = m.village_code INNER JOIN "
				+ "land_bank.khatian_information k ON la.khatian_code = k.khatian_code "
				+ "inner join land_application_approval F on la.land_application_id=F.land_application_id "
				+ "inner join m_application_status G on F.application_status_id= G.application_status_id "
				+ "WHERE la.created_by=:createdBy AND la.deleted_flag = '0'";
		BigInteger count = (BigInteger) entityManager.createNativeQuery(query).setParameter("createdBy", createdBy)
				.getSingleResult();

		entityManager.close();
		LandApplicantDTO dto = new LandApplicantDTO();
		dto.setCountint(count);
		return count.intValue();
	}

	@Transactional
	public Integer cancelLandApplication(BigInteger landApplicationId, String remark) {
		String updateLandApplicationQuery = "UPDATE public.land_application SET deleted_flag = B'1' WHERE land_application_id = :id";
		String updateLandScheduleQuery = "UPDATE public.land_schedule SET deleted_flag = B'1' WHERE land_application_id = :id";
		String updateLandDocumentsQuery = "UPDATE public.land_documents SET deleted_flag = B'1' WHERE land_application_id = :id";
		String updateLandApplicationApprovalQuery = "UPDATE public.land_application_approval "
				+ " SET status = B'1', pending_at_role_id = 2, "
				+ "   application_status_id = 17, approval_action_id = 0, remark = :remark, approval_level = 1"
				+ "  WHERE land_application_id = :id";

		entityManager.createNativeQuery(updateLandApplicationQuery).setParameter("id", landApplicationId)
				.executeUpdate();

		entityManager.createNativeQuery(updateLandScheduleQuery).setParameter("id", landApplicationId).executeUpdate();

		entityManager.createNativeQuery(updateLandDocumentsQuery).setParameter("id", landApplicationId).executeUpdate();

		Integer status = entityManager.createNativeQuery(updateLandApplicationApprovalQuery)
				.setParameter("id", landApplicationId).setParameter("remark", remark).executeUpdate();

		entityManager.close();
		return status;
	}

}