package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.entity.RoleEntity;

@Repository
public class LandAppRegistratationClassRepository {

	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public Integer insertUserWithRole(String userName, String password, String fullName, String mobileNo,
			String emailId, String otp) {
		String query = "INSERT INTO public.citizen_profile_details (user_name, password, full_name, mobile_no, email_id, otp, created_by, created_on, role_id, user_type) "
				+ "VALUES (?, ?, ?, ?, ?, ?, currval(pg_get_serial_sequence('public.citizen_profile_details', 'citizen_profile_details_id')), CURRENT_TIMESTAMP, 2, 'CI' ) ";

		Integer status = entityManager.createNativeQuery(query).setParameter(1, userName).setParameter(2, password)
				.setParameter(3, fullName).setParameter(4, mobileNo).setParameter(5, emailId).setParameter(6, otp)
				.executeUpdate();

		entityManager.close();
		return status;
	}

	@Transactional
	public List<RoleEntity> getRoleInfoList(String roleName) {
		String query = "SELECT role_id, role_name FROM public.m_role where role_name=:roleName ";
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
	public int getUserCountByMobileAndEmail(String mobileNo) {
		String query = "SELECT count(*) FROM user_details WHERE user_name = :mobileNo AND status='0' ";

		BigInteger count = (BigInteger) entityManager.createNativeQuery(query).setParameter("mobileNo", mobileNo)
				.getSingleResult();

		entityManager.close();
		return count.intValue();
	}

	@Transactional
	public Integer getUserCountOfCitizen(String mobileNo) {
		String query = "SELECT count(*) FROM citizen_profile_details WHERE user_name = :mobileNo AND status='0' ";

		BigInteger count = (BigInteger) entityManager.createNativeQuery(query).setParameter("mobileNo", mobileNo)
				.getSingleResult();

		entityManager.close();
		return count.intValue();
	}

	@Transactional
	public Integer insertMobileAndOTP(String mobileNo, String otp) {
		String query = "INSERT INTO public.registration_mobile_no_varification (mobile_no, otp) "
				+ "VALUES (:mobileno, :otp)";

		Integer status = entityManager.createNativeQuery(query).setParameter("mobileno", mobileNo)
				.setParameter("otp", otp).executeUpdate();

		entityManager.close();
		return status;
	}

	@Transactional
	public Integer updateMobileNoOrOtp(String mobileNo, String otp) {
		String query = " UPDATE public.registration_mobile_no_varification " + " SET otp = :otp "
				+ " WHERE mobile_no = :mobileno ";

		Integer status = entityManager.createNativeQuery(query).setParameter("mobileno", mobileNo)
				.setParameter("otp", otp).executeUpdate();
		entityManager.close();
		return status;
	}

	@Transactional
	public Integer updateOtp(String mobileNo, String otp) {
		String query = " UPDATE public.user_details " + " SET otp = :otp " + " WHERE mobile_no = :mobileNo ";

		Integer status = entityManager.createNativeQuery(query).setParameter("mobileNo", mobileNo)
				.setParameter("otp", otp).executeUpdate();
		entityManager.close();
		return status;
	}

	@Transactional
	public String fetchOTPByMobileNo(String mobileNo) {
		String query = "SELECT otp FROM public.user_details " + "WHERE mobile_no = :mobileNo";

		try {
			return (String) entityManager.createNativeQuery(query).setParameter("mobileNo", mobileNo).getSingleResult();
		} catch (NoResultException e) {
			return "Mobile No Not Found";
		} finally {
			entityManager.close();
		}
	}

	@Transactional
	public Integer getUserCountByMobileAndEmailFirstRegisterTab(String mobileNo) {
		String query = "SELECT count(*) FROM citizen_profile_details WHERE status = '0' and mobile_no = :mobileNo ";
		BigInteger count = (BigInteger) entityManager.createNativeQuery(query).setParameter("mobileNo", mobileNo)
				.getSingleResult();

		entityManager.close();
		return count.intValue();
	}
	
	@Transactional
	public Integer getUserCountByMobileOfUser(String mobileNo) {
		String query = "SELECT count(*) FROM user_details WHERE status = '0' and user_name = :mobileNo ";
		BigInteger count = (BigInteger) entityManager.createNativeQuery(query).setParameter("mobileNo", mobileNo)
				.getSingleResult();

		entityManager.close();
		return count.intValue();
	}

	@Transactional
	public Integer getUserCountByMobileAndEmailFirstRegisterTabTemp(String mobileNo) {
		String query = "SELECT count(*) FROM registration_mobile_no_varification WHERE mobile_no = :mobileNo ";
		BigInteger count = (BigInteger) entityManager.createNativeQuery(query).setParameter("mobileNo", mobileNo)
				.getSingleResult();

		entityManager.close();
		return count.intValue();
	}
	

	@Transactional
	public String getOTPByMobileNo(String mobileNo) {
		String query = "SELECT otp FROM public.registration_mobile_no_varification " + "WHERE mobile_no = :mobileno";

		try {
			return (String) entityManager.createNativeQuery(query).setParameter("mobileno", mobileNo).getSingleResult();
		} catch (NoResultException e) {
			return "Mobile No Not Found";
		} finally {
			entityManager.close();
		}
	}

}
