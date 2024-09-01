package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.AddOfficerDTO;
import com.csmtech.sjta.entity.RoleEntity;
import com.csmtech.sjta.entity.User;
import com.csmtech.sjta.exception.ExistingRecordCheckException;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class OfficerRegistrationNativeRepo {

	/**
	 * @author guru.prasad
	 */

	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	public int checkExistingRecord(String mobileNo, String userName) {
		try {
			String query = "SELECT COUNT(*) FROM user_details WHERE (user_name = :userName OR mobile_no = :mobileNo) AND status = '0';"
					+ " ";

			BigInteger count = (BigInteger) entityManager.createNativeQuery(query).setParameter("userName", userName)
					.setParameter("mobileNo", mobileNo).getSingleResult();

			log.info("user count : " + count.intValue());
			return count.intValue();
		} catch (Exception e) {

			throw new ExistingRecordCheckException("Error while checking existing record: " + e.getMessage(), e);
		} finally {
			if (entityManager.isOpen()) {
				entityManager.close();
			}
		}
	}

	@Transactional
	public Integer insertUserWithRole(String userName, String password, String fullName, String mobileNo,
			String emailId, String otp, String userType, BigInteger createdBy, BigInteger departmentId,
			BigInteger roleId) {
		String query = "    WITH inserted_user AS ( "
				+ "    INSERT INTO public.user_details (user_name, password, full_name, mobile_no, email_id, otp, user_type,created_by, department_id, created_on) "
				+ "    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP) " + "    RETURNING user_id " + "    ) "
				+ "    INSERT INTO public.user_role (user_id, role_id, created_by, created_on) "
				+ "    SELECT user_id, ? , ?, CURRENT_TIMESTAMP " + "    FROM inserted_user";
		Integer status = entityManager.createNativeQuery(query).setParameter(1, userName).setParameter(2, password)
				.setParameter(3, fullName).setParameter(4, mobileNo).setParameter(5, emailId).setParameter(6, otp)
				.setParameter(7, userType).setParameter(8, createdBy.longValue())
				.setParameter(9, departmentId.longValue()).setParameter(10, roleId.longValue())
				.setParameter(11, createdBy.longValue()).executeUpdate();
		entityManager.close();

		return status;

	}

	@Transactional
	public List<RoleEntity> getRoleInfoList(String roleName) {
		String query = "SELECT role_id, role_name FROM public.m_role WHERE role_name = :roleName ";
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = entityManager.createNativeQuery(query).setParameter("roleName", roleName)
				.getResultList();
		entityManager.close();

		return transformResultList(resultList);
	}

	private List<RoleEntity> transformResultList(List<Object[]> resultList) {
		List<RoleEntity> roleInfoList = new ArrayList<>();
		for (Object[] row : resultList) {
			Short roleId = (Short) row[0];
			String roleName = (String) row[1];

			RoleEntity roleInfo = new RoleEntity();
			roleInfo.setRoleId(roleId);
			roleInfo.setRoleName(roleName);

			roleInfoList.add(roleInfo);
		}
		return roleInfoList;
	}

	@Transactional
	public List<AddOfficerDTO> getOfficers(int pageNumber, int pageSize, String fullName) {
		String baseQuery = "SELECT u.user_id, u.full_name, u.mobile_no, u.email_id, d.department_name, mr.role_name, u.user_block_status, u.user_name "
				+ "FROM user_details u " + "LEFT JOIN m_department d ON u.department_id = d.department_id "
				+ "JOIN user_role ur USING(user_id) " + "JOIN m_role mr ON ur.role_id = mr.role_id "
				+ "WHERE u.user_type = 'O' AND u.status = '0'";

		String query = fullName != null ? baseQuery + " AND u.full_name ILIKE :fullName" : baseQuery;

		query += " LIMIT :pageSize OFFSET :offset";

		int offset = (pageNumber - 1) * pageSize;

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = entityManager.createNativeQuery(query).setParameter("pageSize", pageSize)
				.setParameter("offset", offset).setParameter("fullName", "%" + fullName + "%").getResultList();

		entityManager.close();

		List<AddOfficerDTO> officerDTOList = new ArrayList<>();
		for (Object[] result : resultList) {
			AddOfficerDTO officerInfoDTO = createOfficerDTO(result);
			officerDTOList.add(officerInfoDTO);
		}

		return officerDTOList;
	}

	private AddOfficerDTO createOfficerDTO(Object[] result) {
		BigInteger userId = (BigInteger) result[0];
		String fullName1 = (String) result[1];
		String mobileNo = (String) result[2];
		String emailId = (String) result[3];
		String departmentName = (String) result[4];
		String roleName = (String) result[5];
		Boolean userBlockStatus = (Boolean) result[6];
		String userName = ((String) result[7]);

		AddOfficerDTO officerInfoDTO = new AddOfficerDTO();
		officerInfoDTO.setUserId(userId);
		officerInfoDTO.setFullName(fullName1);
		officerInfoDTO.setMobileNo(mobileNo);
		officerInfoDTO.setEmailId(emailId);
		officerInfoDTO.setDepartmentName(departmentName);
		officerInfoDTO.setRoleName(roleName);
		officerInfoDTO.setUserBlockStatus(userBlockStatus);
		officerInfoDTO.setUserName(userName);

		return officerInfoDTO;
	}

	@Transactional
	public Integer updatedetails(String newfullName, String newmobileNo, String newemailId, String newuserName,
			BigInteger departmentId, BigInteger roleId, BigInteger newupdatedBy, BigInteger userId,
			BigInteger createdBy) {
		entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();
		Date currentDateTime = new Date();

		int updatedRows = entityManager.createNativeQuery(
				"UPDATE public.user_details SET full_name = :setfullname, mobile_no = :setmobileno, email_id = :setemailid, user_name =:setusername, department_id =:departmentId, updated_by = :setupdatedby, updated_on = :setupdatedon WHERE user_id = :givenuserid")
				.setParameter("setfullname", newfullName).setParameter("setmobileno", newmobileNo)
				.setParameter("setemailid", newemailId).setParameter("setusername", newuserName)
				.setParameter("departmentId", departmentId).setParameter("setupdatedby", createdBy)
				.setParameter("setupdatedon", currentDateTime).setParameter("givenuserid", userId).executeUpdate();

		int updatedRoleRows = entityManager.createNativeQuery(
				"UPDATE public.user_role SET role_id =:setroleId, updated_by = :setupdatedby, updated_on = :setupdatedon WHERE user_id = :givenuserid")
				.setParameter("setroleId", roleId).setParameter("setupdatedby", createdBy)
				.setParameter("setupdatedon", currentDateTime).setParameter("givenuserid", userId).executeUpdate();
		entityManager.close();

		return updatedRows + updatedRoleRows;
	}

	@Transactional
	public Integer deleteRecord(BigInteger createdBy, BigInteger userId) {
		entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();
		Date currentDateTime = new Date();

		Integer status = entityManager.createNativeQuery(
				"UPDATE public.user_details SET status = CAST(:setstatus AS BIT), updated_by = :setupdatedby, updated_on = :setupdatedon WHERE user_id = :givenuserid")
				.setParameter("setstatus", 1).setParameter("setupdatedby", createdBy.longValue())
				.setParameter("setupdatedon", currentDateTime).setParameter("givenuserid", userId).executeUpdate();
		entityManager.close();

		return status;
	}

	public Integer getTotalOfficerCount(String fullName) {
		String query = "SELECT COUNT(*)  FROM user_details u LEFT JOIN m_department d "
				+ " ON u.department_id = d.department_id JOIN user_role ur USING(user_id)  JOIN m_role mr "
				+ " ON ur.role_id = mr.role_id WHERE  u.user_type ='O' AND u.status = '0'   ";
		if (fullName != null) {
			query += " AND full_name ILIKE :fullName ";
		}
		BigInteger count = (BigInteger) entityManager.createNativeQuery(query)
				.setParameter("fullName", "%" + fullName + "%").getSingleResult();
		AddOfficerDTO dto = new AddOfficerDTO();
		dto.setCountint(count);
		entityManager.close();

		return count.intValue();
	}

	@Transactional

	public User findDataById(BigInteger userId) {
		String query = "SELECT u.full_name, u.mobile_no, u.email_id, u.department_id, d.department_name, r.role_id, r.role_name,u.user_name "
				+ "FROM user_details u " + " LEFT JOIN m_department d ON u.department_id=d.department_id "
				+ "JOIN user_role ur USING (user_id) " + "JOIN m_role r ON ur.role_id = r.role_id "
				+ "WHERE u.user_id = :userId";

		try {
			Object[] result = (Object[]) entityManager.createNativeQuery(query).setParameter("userId", userId)
					.getSingleResult();
			entityManager.close();

			User user = new User();
			user.setFullName((String) result[0]);
			user.setMobileNo((String) result[1]);
			user.setEmailId((String) result[2]);
			user.setDepartmentId((Short) result[3]);
			user.setDepartment((String) result[4]);
			user.setRoleId((Short) result[5]);
			user.setRoleName((String) result[6]);
			user.setUserName((String) result[7]);

			return user;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional
	public List<AddOfficerDTO> getOfficerDetails() {
		String query = "SELECT u.user_id,u.full_name, u.mobile_no, u.email_id, d.department_name, mr.role_name "
				+ "FROM user_details u " + " LEFT JOIN m_department d ON u.department_id=d.department_id\r\n"
				+ "JOIN user_role ur USING(user_id)\r\n" + "JOIN m_role mr ON ur.role_id=mr.role_id"
				+ " WHERE u.user_type ='O' AND u.status='0' AND u.user_block_status = false ORDER BY u.full_name ";

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = entityManager.createNativeQuery(query).getResultList();

		List<AddOfficerDTO> officerDTOList = new ArrayList<>();
		for (Object[] result : resultList) {
			BigInteger userId = (BigInteger) result[0];
			String fullName1 = (String) result[1];
			String mobileNo = (String) result[2];
			String emailId = (String) result[3];
			String departmentName = (String) result[4];
			String roleName = (String) result[5];

			AddOfficerDTO officerInfoDTO = new AddOfficerDTO();

			officerInfoDTO.setUserId(userId);
			officerInfoDTO.setFullName(fullName1);
			officerInfoDTO.setMobileNo(mobileNo);
			officerInfoDTO.setEmailId(emailId);
			officerInfoDTO.setDepartmentName(departmentName);
			officerInfoDTO.setRoleName(roleName);

			officerDTOList.add(officerInfoDTO);
		}
		entityManager.close();

		return officerDTOList;
	}

}
