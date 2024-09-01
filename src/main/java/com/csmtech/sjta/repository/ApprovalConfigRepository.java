package com.csmtech.sjta.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ApprovalConfigRepository {
	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@Transactional
	public Integer saveApprovalConfig(JSONArray stageData, String serviceName) {

		String updateQuery = "UPDATE approval_configration SET status = '1' WHERE approval_type = :serviceName";
		Query queryy = entity.createNativeQuery(updateQuery);
		queryy.setParameter("serviceName", serviceName);
		queryy.executeUpdate();
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(
				"INSERT INTO approval_configration(approval_type, role_id, approval_action_ids, created_by, approval_level, timeline_days, document_checklist_ids) VALUES ");

		for (int i = 0; i < stageData.length(); i++) {
			queryBuilder.append("(:approvalType" + i + ", :roleId" + i + ", :actionIds" + i + ", 1, :approvalLevel" + i
					+ ", :timelineDays" + i + ", :checklist" + i + ")");
			if (i < stageData.length() - 1) {
				queryBuilder.append(", ");
			}
		}

		Query query = entity.createNativeQuery(queryBuilder.toString());

		for (int i = 0; i < stageData.length(); i++) {
			JSONObject object = stageData.getJSONObject(i);

			query.setParameter("approvalType" + i, serviceName).setParameter("roleId" + i, object.getInt("roleId"))
					.setParameter("actionIds" + i, object.getString("authActions").toString())
					.setParameter("approvalLevel" + i, object.getInt("tinStageNo"))
					.setParameter("timelineDays" + i, object.getInt("timeline"))
					.setParameter("checklist" + i, object.getString("authDocss").toString());
		}

		entity.close();
		return query.executeUpdate();
	}
}
