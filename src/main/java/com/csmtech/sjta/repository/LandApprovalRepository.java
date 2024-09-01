package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class LandApprovalRepository {
	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public Integer insertLandWithRole(BigInteger landappid, Integer approvalLevel, Short roleId,
			Integer applicationStatusId, Integer approvalActionId, String remark, Integer userId) {
		String query = "WITH inserted_land AS ( "
				+ " INSERT INTO public.land_application_approval (land_application_id, pending_at_role_id, application_status_id, created_by, approval_action_id, remark, action_on, approval_level) "
				+ " VALUES (:landApplicantId, :pendingAtRoleId, :applicationStatusId, :userId, :approvalActionId, :remark, CURRENT_TIMESTAMP, :approvalLevel) "
				+ " RETURNING land_application_approval_id,land_application_id,approval_level,application_status_id,created_by,approval_action_id,action_on"
				+ " ) INSERT INTO public.land_application_approval_log (land_application_approval_id, land_application_id, approval_level, application_status_id,created_by,approval_action_id,action_on) "
				+ " SELECT land_application_approval_id,land_application_id,approval_level,application_status_id,created_by,approval_action_id,action_on  FROM inserted_land";

		Integer status = entityManager.createNativeQuery(query).setParameter("landApplicantId", landappid)
				.setParameter("pendingAtRoleId", roleId).setParameter("applicationStatusId", applicationStatusId)
				.setParameter("userId", userId).setParameter("approvalActionId", approvalActionId)
				.setParameter("remark", remark).setParameter("approvalLevel", approvalLevel).executeUpdate();

		entityManager.close();
		return status;
	}
	
	@SuppressWarnings("unchecked")
	public BigInteger applicationCount(BigInteger landappid) {
		BigInteger result;
		try {
			String nativeQuery = "select count(land_application_id) from public.land_application_approval "
					+ "where land_application_id =:landappid and status ='0'";
			result = (BigInteger)entityManager.createNativeQuery(nativeQuery).setParameter("landappid", landappid).getSingleResult();
		}finally {
			entityManager.close();
		}
		return result;
		
		
	}

}
