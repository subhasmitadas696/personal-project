package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.CitizenInfoDTO;
import com.csmtech.sjta.dto.UserDetailsDTO;

/**
 * @Auth Prasanta Kumar Sethi
 */

@Repository
public class ShowCitizenDetailsNativeRepository {
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public List<CitizenInfoDTO> getActiveUsers() {
		String query = "SELECT citizen_profile_details_id, user_type, full_name, mobile_no, email_id, block_status, user_name FROM citizen_profile_details WHERE status = '0'";
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = entityManager.createNativeQuery(query).getResultList();

		List<CitizenInfoDTO> citizenInfoDTOList = new ArrayList<>();
		for (Object[] result : resultList) {
			BigInteger userId = (BigInteger) result[0];
			String userType = (String) result[1];
			String fullName = (String) result[2];
			String mobileNo = (String) result[3];
			String emailId = (String) result[4];
			Boolean userBlockStatus = (Boolean) result[5];
			String userName = (String) result[6];

			CitizenInfoDTO citizenInfoDTO = new CitizenInfoDTO();
			citizenInfoDTO.setUserId(userId);
			citizenInfoDTO.setUserType(userType);
			citizenInfoDTO.setFullName(fullName);
			citizenInfoDTO.setMobileNo(mobileNo);
			citizenInfoDTO.setEmailId(emailId);
			citizenInfoDTO.setUserBlockStatus(userBlockStatus);
			citizenInfoDTO.setUserName(userName);
			citizenInfoDTOList.add(citizenInfoDTO);
		}
		entityManager.close();
		return citizenInfoDTOList;
	}

	@Transactional
	public Integer getTotalUser() {
		String query = "SELECT COUNT(*) FROM citizen_profile_details WHERE status = '0' ";
		BigInteger count = (BigInteger) entityManager.createNativeQuery(query).getSingleResult();
		UserDetailsDTO dto = new UserDetailsDTO();
		dto.setCountint(count);

		entityManager.close();
		return count.intValue();
	}

	private List<UserDetailsDTO> transformResultList(List<Object[]> resultList) {
		List<UserDetailsDTO> roleInfoList = new ArrayList<>();
		for (Object[] row : resultList) {
			BigInteger citizenProfileDetailsId = (BigInteger) row[0];
			String userType = (String) row[1];
			String fullName = (String) row[2];
			String mobileNo = (String) row[3];
			String emailId = (String) row[4];
			Boolean userBlockStatus = (Boolean) row[5];
			String userName = (String) row[6];

			UserDetailsDTO userInfo = new UserDetailsDTO();
			userInfo.setUserId(citizenProfileDetailsId);
			userInfo.setUserType(userType);
			userInfo.setFullName(fullName);
			userInfo.setMobileNo(mobileNo);
			userInfo.setEmailId(emailId);
			userInfo.setUserBlockStatus(userBlockStatus);
			userInfo.setUserName(userName);

			roleInfoList.add(userInfo);

		}

		return roleInfoList;
	}

	@Transactional
	public List<UserDetailsDTO> getUserDetails(int pageNumber, int pageSize) {
		String query = "SELECT citizen_profile_details_id, user_type, full_name, mobile_no, email_id, block_status, user_name"
				+ " FROM citizen_profile_details" + "  WHERE status='0' " + "ORDER BY full_name " + "LIMIT :pageSize "
				+ "OFFSET :offset ";

		int offset = (pageNumber - 1) * pageSize;

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = entityManager.createNativeQuery(query).setParameter("pageSize", pageSize)
				.setParameter("offset", offset).getResultList();

		entityManager.close();
		return transformResultList(resultList);
	}

	@Transactional
	public List<UserDetailsDTO> getSearchUserDetails(String searchKeyword, int pageNumber, int pageSize) {
		String query = "SELECT citizen_profile_details_id, user_type, full_name, mobile_no, email_id, block_status, user_name "
				+ "FROM citizen_profile_details "
				+ "WHERE (LOWER(mobile_no) LIKE LOWER(CONCAT('%', :searchKeyword, '%')) OR "
				+ " LOWER(full_name) LIKE LOWER(CONCAT('%', :searchKeyword, '%'))) " + "AND status='0' "
				+ "ORDER BY full_name " + "LIMIT :pageSize " + "OFFSET :offset ";

		int offset = (pageNumber - 1) * pageSize;

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = entityManager.createNativeQuery(query)
				.setParameter("searchKeyword", "%" + searchKeyword + "%").setParameter("pageSize", pageSize)
				.setParameter("offset", offset).getResultList();

		entityManager.close();
		return transformResultList(resultList);
	}

	public Integer getSearchedUser(String searchKeyword) {
		String query = "SELECT COUNT(*) FROM citizen_profile_details "
				+ " WHERE (LOWER(mobile_no) LIKE LOWER(CONCAT('%', :searchKeyword, '%')) OR "
				+ " LOWER(full_name) LIKE LOWER(CONCAT('%', :searchKeyword, '%'))) AND status='0'  ";
		BigInteger count = (BigInteger) entityManager.createNativeQuery(query)
				.setParameter("searchKeyword", "%" + searchKeyword + "%").getSingleResult();
		UserDetailsDTO dto = new UserDetailsDTO();
		dto.setCountint(count);

		entityManager.close();
		return count.intValue();
	}

}
