package com.csmtech.sjta.repository;

import java.math.BigInteger;

/**
 * @author prasanta.sethi
 */

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.csmtech.sjta.entity.CitizenProfileEntity;
import com.csmtech.sjta.entity.User;

@Repository
public class ForgotPasswordNativeRepository {

	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	public Integer updatepassword(String newPassword, String mobileNo) {
		entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();

		Integer status = 0;
		String userCheckQuery = "SELECT COUNT(*) FROM user_details WHERE user_name=:mobileNo";
		BigInteger userCount = (BigInteger) entityManager.createNativeQuery(userCheckQuery)
				.setParameter("mobileNo", mobileNo).getSingleResult();

		String citizenCheckQuery = "SELECT COUNT(*) FROM citizen_profile_details WHERE user_name=:mobileNo";
		BigInteger citizenCount = (BigInteger) entityManager.createNativeQuery(citizenCheckQuery)
				.setParameter("mobileNo", mobileNo).getSingleResult();

		if (userCount.intValue() > 0) {
			status = entityManager
					.createNativeQuery(
							"UPDATE public.user_details SET password = :newsetpassword WHERE user_name = :mobileNo")
					.setParameter("newsetpassword", newPassword).setParameter("mobileNo", mobileNo).executeUpdate();
		}

		else if (citizenCount.intValue() > 0) {
			status = entityManager.createNativeQuery(
					"UPDATE public.citizen_profile_details SET password = :newsetpassword WHERE user_name = :mobileNo")
					.setParameter("newsetpassword", newPassword).setParameter("mobileNo", mobileNo).executeUpdate();

		} else {
			return 0;
		}
		entityManager.close();
		return status;
	}

	@Transactional
	public Integer updateOtp(String userName, String updateOtp) {
		Integer status = 0;
		String userCheckQuery = "SELECT COUNT(*) FROM user_details WHERE user_name=:userName";
		BigInteger userCount = (BigInteger) entityManager.createNativeQuery(userCheckQuery)
				.setParameter("userName", userName).getSingleResult();

		String citizenCheckQuery = "SELECT COUNT(*) FROM citizen_profile_details WHERE user_name=:userName";
		BigInteger citizenCount = (BigInteger) entityManager.createNativeQuery(citizenCheckQuery)
				.setParameter("userName", userName).getSingleResult();

		if (userCount.intValue() > 0) {
			String query = "UPDATE user_details SET otp=:updateOtp WHERE user_name=:userName";
			status = entityManager.createNativeQuery(query).setParameter("updateOtp", updateOtp)
					.setParameter("userName", userName).executeUpdate();
		} else if (citizenCount.intValue() > 0) {
			String query = "UPDATE citizen_profile_details SET otp=:updateOtp WHERE user_name=:userName";
			status = entityManager.createNativeQuery(query).setParameter("updateOtp", updateOtp)
					.setParameter("userName", userName).executeUpdate();
		} else {
			return 0;
		}

		return status;
	}
	
	/*
	 * @SuppressWarnings("unchecked")
	public BigInteger countTahasilPlotList(String tahasilCode) {
		BigInteger result ;
	try {	
		String nativeQuery = "select count(*) from land_bank.tahasil_master tm\r\n"
				+ "inner join land_bank.village_master vm on tm.tahasil_code = vm.tahasil_code\r\n"
				+ "left join land_bank.khatian_information ki on vm.village_code = ki.village_code\r\n"
				+ "inner join land_bank.plot_information pi on pi.khatian_code = ki.khatian_code\r\n"
				+ "left join public.tahasil_plot_survey tps on pi.plot_code = tps.plot_code\r\n"
				+ "where tm.tahasil_code =:tahasilCode ";
		Query query = entity.createNativeQuery(nativeQuery).setParameter("tahasilCode", tahasilCode);
		log.info("query starts executing for count of tahasilPlotList");
		result = (BigInteger)query.getSingleResult();
	}finally {
		entity.close();
	}
	return result;
	}
	 */
	
	@SuppressWarnings("unchecked")
	public BigInteger countMobileUser(String mobileNo) {
		BigInteger result ;
		try {
			String userCheckQuery = "SELECT COUNT(*) FROM user_details WHERE mobile_no=:mobileNo";
			result = (BigInteger) entityManager.createNativeQuery(userCheckQuery)
					.setParameter("mobileNo", mobileNo).getSingleResult();
			
		}finally {
			entityManager.close();
		}
		return result;
	}
	
//	@SuppressWarnings("unchecked")
//	public User mobileOfficerUser(String mobileNo) {
//		User result ;
//		try {
//			String userCheckQuery = "SELECT user_id, user_name FROM user_details WHERE mobile_no=:mobileNo";
//			result =  entityManager.createNativeQuery(userCheckQuery)
//					.setParameter("mobileNo", mobileNo).getSingleResult();
//			
//		}finally {
//			entityManager.close();
//		}
//		return result;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public CitizenProfileEntity mobileCitizenUser(String mobileNo) {
//		CitizenProfileEntity result ;
//		try {
//			String userCheckQuery = "SELECT citizen_profile_details_id,user_name FROM citizen_profile_details WHERE mobile_no=:mobileNo";
//			result = entityManager.createNativeQuery(userCheckQuery)
//					.setParameter("mobileNo", mobileNo).getSingleResult();
//			
//		}finally {
//			entityManager.close();
//		}
//		return result;
//	}

}
