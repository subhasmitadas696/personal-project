package com.csmtech.sjta.mobile.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.TahasilTeamUseRequestDto;
import com.csmtech.sjta.dto.UserDetails;
import com.csmtech.sjta.entity.User;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MobileLoginRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entity;

	public UserDetails getUserDetails(String mobileNo) {
		UserDetails	userResult = new UserDetails();
		String nativeQuery = "SELECT u.user_id,u.user_name,u.full_name,u.mobile_no,u.email_id,a.role_name "
				+ "FROM public.user_details u inner join public.m_department r on u.department_id = r.department_id \r\n"
				+ "inner join public.user_role ur on u.user_id = ur.user_id \r\n"
				+ "inner join public.m_role a on a.role_id = ur.role_id\r\n"
				+ "where u.mobile_no =:mobileNo and u.status = '0' ";
		Query query = entity.createNativeQuery(nativeQuery).setParameter("mobileNo", mobileNo);
		try {
			log.info("fetching login details from db");
			log.info("getUserDetails native query:  "+nativeQuery);
			List<Object[]> resultList = query.getResultList();
			List<UserDetails> userDetailsList = mapToDTO(resultList);
			userResult = userDetailsList.get(0);
			 log.info("login details from db: "+userResult);
		}finally {
			entity.close();
		}
		return userResult;
	}
	
	
	private List<UserDetails> mapToDTO(List<Object[]> resultList) {
	return resultList.stream()
			.map(objects -> new UserDetails((BigInteger) objects[0], (String) objects[1],(String) objects[2],
					(String) objects[3],(String) objects[4],(String) objects[5]))
			.collect(Collectors.toList());
}


	@Transactional
	public Integer saveFcmToken(String fcmToken,String mobileNo) {
		Integer status = 0;
		try {
			status = entity.createNativeQuery(
				"update public.user_details set fcm_token =:fcmToken where mobile_no =:mobileNo and status = '0'")
				.setParameter("mobileNo", mobileNo).setParameter("fcmToken",fcmToken).executeUpdate();
		}finally {
			entity.close();
		}
		return status;
		
	}


}
