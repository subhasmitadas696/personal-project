package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

/*
 * @Auth  GuruPrasad
 */

@Repository
public class UserDetailsNativeRepository {

	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	public Integer updateDetails(String newMobileNo, String newFullName, String newemailId, BigInteger newUpdatedBy,
			BigInteger userId) {
		entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();
		Date currentDateTime = new Date();

		Integer status = entityManager.createNativeQuery(
				"UPDATE public.user_details SET mobile_no =:setmobileno, full_name = :setfullname, email_id = :setemailid, updated_by = :setupdatedby, updated_on = :setupdatedon WHERE user_id = :givenuserid")
				.setParameter("setmobileno", newMobileNo).setParameter("setfullname", newFullName)
				.setParameter("setemailid", newemailId).setParameter("setupdatedby", userId)
				.setParameter("setupdatedon", currentDateTime)

				.setParameter("givenuserid", userId).executeUpdate();

		entityManager.close();
		return status;
	}

	@Transactional
	public Integer updateUser(String encodedPassword, String newUpdatedBy, BigInteger userId, String userName) {
		entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();
		Date currentDateTime = new Date();

		Integer status = entityManager.createNativeQuery(
				"UPDATE public.user_details SET password = :setpassword, updated_by = :setupdatedby, updated_on = :setupdatedon, is_first_login = 0 WHERE user_name = :givenusername")
				.setParameter("setpassword", encodedPassword).setParameter("setupdatedby", userId)
				.setParameter("setupdatedon", currentDateTime).setParameter("givenusername", userName).executeUpdate();

		entityManager.close();
		return status;
	}

	@Transactional
	public Integer updateCitizen(String encodedPassword, String newUpdatedBy, BigInteger userId, String userName) {
		entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();
		Date currentDateTime = new Date();

		Integer status = entityManager.createNativeQuery(
				"UPDATE public.citizen_profile_details SET password = :setpassword, updated_by = :setupdatedby, updated_on = :setupdatedon, is_first_login = 0 WHERE user_name = :givenusername")
				.setParameter("setpassword", encodedPassword).setParameter("setupdatedby", userId)
				.setParameter("setupdatedon", currentDateTime).setParameter("givenusername", userName).executeUpdate();

		entityManager.close();
		return status;
	}

}
