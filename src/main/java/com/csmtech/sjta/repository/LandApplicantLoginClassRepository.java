package com.csmtech.sjta.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LandApplicantLoginClassRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entityManager;

	@Transactional
	public List<Long> findRoleIdsByUserId(String userName) {
		String sql = " SELECT ur.role_id  FROM user_role ur JOIN user_details ud USING(user_id) WHERE\r\n"
				+ " user_name =  CAST(:userName AS VARCHAR) AND ud.status = '0'  UNION SELECT\r\n"
				+ " role_id FROM citizen_profile_details  WHERE user_name = CAST(:userName AS VARCHAR) AND status = '0'";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("userName", userName);
		@SuppressWarnings("unchecked")
		List<Long> roleIds = query.getResultList();

		entityManager.close();
		return roleIds;
	}
}
