package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.LandApplicantQueryLastReturnDTO;
import com.csmtech.sjta.dto.PaginationInRegisterDTO;

@Repository
public class PaginationInRegisterClassRepository {

	@Autowired
	@PersistenceContext
	private EntityManager entityManager;


	@Transactional
	public List<PaginationInRegisterDTO> getUserDetailsPage(int pageNumber, int pageSize) {
		String query = "SELECT user_id, user_name, full_name, mobile_no, email_id, user_type, created_by "
				+ "FROM user_details" + " WHERE status='0' " + "ORDER BY user_id " + "LIMIT :pageSize "
				+ "OFFSET :offset ";

		int offset = (pageNumber - 1) * pageSize;

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = entityManager.createNativeQuery(query).setParameter("pageSize", pageSize)
				.setParameter("offset", offset).getResultList();

		entityManager.close();
		return transformResultList(resultList);
	}

	private List<PaginationInRegisterDTO> transformResultList(List<Object[]> resultList) {
		List<PaginationInRegisterDTO> roleInfoList = new ArrayList<>();
		for (Object[] row : resultList) {
			BigInteger userId = (BigInteger) row[0];
			String userName = (String) row[1];
			String fullName = (String) row[2];
			String mobileNo = (String) row[3];
			String emailId = (String) row[4];
			Character userTypeChar = (Character) row[5];
			BigInteger createdBy = (BigInteger) row[6];

			PaginationInRegisterDTO roleInfo = new PaginationInRegisterDTO();
			roleInfo.setUserId(userId);
			roleInfo.setUserName(userName);
			roleInfo.setUserName(userName);
			roleInfo.setFullName(fullName);
			roleInfo.setMobileNo(mobileNo);
			roleInfo.setEmailId(emailId);
			roleInfo.setUserType(userTypeChar);
			roleInfo.setCreatedBy(createdBy);

			roleInfoList.add(roleInfo);

		}

		return roleInfoList;
	}

	@Transactional
	public Integer getTotalUserCount() {
		String query = "SELECT COUNT(*) FROM user_details WHERE status = '0' ";
		BigInteger count = (BigInteger) entityManager.createNativeQuery(query).getSingleResult();
		PaginationInRegisterDTO dto = new PaginationInRegisterDTO();
		dto.setCountint(count);
		
		entityManager.close();
		return count.intValue();
	}
	
	@Transactional
	public LandApplicantQueryLastReturnDTO findLatestLandApplicantByMobileNo() {
        String nativeQuery = "SELECT applicant_no FROM land_applicant ORDER BY land_applicant_id DESC LIMIT 1 ";
        Query query = entityManager.createNativeQuery(nativeQuery, LandApplicantQueryLastReturnDTO.class);
        LandApplicantQueryLastReturnDTO data = (LandApplicantQueryLastReturnDTO) query.getSingleResult();
        
        entityManager.close();
        return data;
    }
}
