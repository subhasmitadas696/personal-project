package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.ApprovalActionResultDTO;
import com.csmtech.sjta.dto.ApprovalDocumentDTO;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ApprovalActionRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entityManager;

	@Transactional
	public List<ApprovalActionResultDTO> findApprovalActionsForRoleId(Long roleId) {
		String sql = "SELECT X.approval_action_id, Y.approval_action "
				+ "FROM (SELECT UNNEST(STRING_TO_ARRAY(approval_action_ids, ',')) AS approval_action_id "
				+ "FROM approval_configration " + " WHERE role_id = :roleId "
				+ "AND approval_type = 'Land Application' AND status=b'0') X "
				+ "INNER JOIN m_approval_action Y ON CAST(X.approval_action_id AS INTEGER) = CAST(Y.approval_action_id AS INTEGER)";
		log.info(":: query Are execute sucessfully..!!");
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("roleId", roleId);

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.getResultList();
		List<ApprovalActionResultDTO> approvalActionResults = new ArrayList<>();

		for (Object[] row : resultList) {
			String approvalActionId = (String) row[0];
			String approvalAction = (String) row[1];
			approvalActionResults.add(new ApprovalActionResultDTO(approvalActionId, approvalAction));
		}

		entityManager.close();
		log.info(":: resultList Are execute and bind the result and return to user ..!!");
		return approvalActionResults;
	}

	// approval user-1
	@Transactional
	public Short findApprovalLevelForRoleIdAndType(Integer roleId, String approvalType, Integer landId) {
		String sql = "SELECT app.approval_level " + "FROM approval_configration app "
				+ "JOIN land_application_approval land ON land.pending_at_role_id = app.role_id "
				+ "WHERE app.role_id = :roleId AND land.land_application_id = :landId AND app.approval_type = :approvalType AND app.status = '0'";

		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("roleId", roleId);
		query.setParameter("landId", landId);
		query.setParameter("approvalType", approvalType);

		try {
			Object result = query.getSingleResult();

			entityManager.close();
			if (result != null) {
				return (Short) result;
			}
		} catch (NoResultException e) {
			return null;
		}

		return null;
	}

	@SuppressWarnings("removal")
	@Transactional
	public Integer findRoleIdByApprovalLevel(Integer approvalLevel) {
		String sql = "SELECT role_id " + "FROM approval_configration "
				+ "WHERE approval_level = :approvalLevel AND approval_type='Land Application'";

		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("approvalLevel", approvalLevel);

		Object result = query.getSingleResult();

		entityManager.close();
		if (result != null) {
			return new Integer(result.toString());
		}

		return null;
	}

	@Transactional
	public Integer updateLandApplicationApproval(Integer landApplicantId, Integer newPendingAtRoleId,
			Short newApplicationStatusId, Integer newUpdatedBy, Boolean newStatus, Integer newApprovalActionId,
			String newRemark, Short result, String docsIds) {

		String sqlUpdate = "UPDATE public.land_application_approval " + "SET "
				+ " pending_at_role_id = :newPendingAtRoleId, " + " application_status_id = :newApplicationStatusId, "
				+ "    updated_by = :newUpdatedBy, " + " updated_on=CURRENT_TIMESTAMP , " + "    status = '0' , "
				+ " approval_action_id = :newApprovalActionId, " + "    remark = :newRemark, "
				+ " action_on = CURRENT_TIMESTAMP, " + "    approval_level = :newApprovalLevel " + "WHERE "
				+ " land_application_id = :landApplicantId";

		entityManager.createNativeQuery(sqlUpdate).setParameter("newPendingAtRoleId", newPendingAtRoleId)
				.setParameter("newApplicationStatusId", newApplicationStatusId)
				.setParameter("newUpdatedBy", newUpdatedBy).setParameter("newApprovalActionId", newApprovalActionId)
				.setParameter("newRemark", newRemark).setParameter("newApprovalLevel", result)
				.setParameter("landApplicantId", landApplicantId).executeUpdate();

		String sqlInsert = "INSERT INTO public.land_application_approval_log "
				+ " (land_application_approval_id, land_application_id, application_status_id, "
				+ " created_by, updated_by, status, approval_action_id,  action_on, approval_level,document_check_officer) "
				+ " SELECT " + "    land_application_approval_id, land_application_id, application_status_id, "
				+ " created_by, updated_by, status, approval_action_id,  action_on, approval_level ," + " :docsIds "
				+ " FROM public.land_application_approval " + "WHERE " + "    land_application_id = :landApplicantId";

		Integer status = entityManager.createNativeQuery(sqlInsert).setParameter("landApplicantId", landApplicantId)
				.setParameter("docsIds", docsIds).executeUpdate();
		entityManager.close();
		return status;
	}

	@Transactional
	public Short findApplicationStatusId(long approvalActionId, long roleId) {
		String sql = "SELECT application_status_id " + "FROM m_application_status "
				+ "WHERE approval_action_id = :approvalActionId AND role_id = :roleId";
		log.info("appActId: " + approvalActionId);
		log.info("roleId: " + roleId);

		Short applicationStatusId = (Short) entityManager.createNativeQuery(sql)
				.setParameter("approvalActionId", approvalActionId).setParameter("roleId", roleId).getSingleResult();
		log.info("appstaId: " + applicationStatusId);

		entityManager.close();
		return applicationStatusId;
	}

	@Transactional
	public List<Object[]> findMailandApplicant(Integer landApplicantId) {
		String sql = "SELECT email_address, applicant_name FROM public.land_application "
				+ "WHERE land_application_id = :LandApplicantId ";

		return entityManager.createNativeQuery(sql).setParameter("LandApplicantId", landApplicantId).getResultList();
	}

	@Transactional
	public List<ApprovalDocumentDTO> findApprovaldocumentForRoleId(Long roleId) {
		String sql = "SELECT X.doc_type_id, Y.doc_name \r\n"
				+ "FROM (SELECT UNNEST(STRING_TO_ARRAY(document_checklist_ids, ',')) AS doc_type_id  \r\n"
				+ "FROM approval_configration        \r\n" + "WHERE role_id =:roleId\r\n"
				+ "AND approval_type = 'Land Application' AND status=b'0') X \r\n"
				+ "INNER JOIN public.m_document_type Y ON CAST(X.doc_type_id AS INTEGER) = CAST(Y.doc_type_id AS INTEGER)";

		log.info(":: query Are execute sucessfully..!!");
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("roleId", roleId);

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.getResultList();
		List<ApprovalDocumentDTO> approvalActionResults = new ArrayList<>();

		for (Object[] row : resultList) {
			ApprovalDocumentDTO dto = new ApprovalDocumentDTO();
			dto.setDocsTypeId((String) row[0]);
			dto.setDocsName((String) row[1]);
			approvalActionResults.add(dto);
		}

		entityManager.close();
		log.info(":: resultList Are execute and bind the result and return to user ..!!");
		return approvalActionResults;
	}
	
	@Transactional
	public Object getPlot(BigInteger applicantId) {
		String plotIdQuery = "SELECT STRING_AGG(CONCAT('''', plot_code, ''''), ',') AS plotCode FROM land_schedule WHERE deleted_flag = '0' AND land_application_id = :applicantId";
		return entityManager.createNativeQuery(plotIdQuery).setParameter("applicantId", applicantId).getSingleResult();
	}
}
