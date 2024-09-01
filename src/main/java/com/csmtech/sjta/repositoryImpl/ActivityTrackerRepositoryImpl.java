package com.csmtech.sjta.repositoryImpl;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.repository.ActivityTrackerRepository;

@Repository
public class ActivityTrackerRepositoryImpl implements ActivityTrackerRepository {

	@Autowired
	private EntityManager entityManager;

	@Transactional
	public Integer save(JSONObject data) {
		String nativeQuery = "INSERT INTO public.activity_log(view_url, server_ip, user_agent, user_role, user_id, user_name, user_session, change_field, action_type, action_remark, action_unique_no, device_type, application_type) "
				+ "VALUES (:url, :ip, :agent, :role, :userid, :name, :session, :field, :type, :remark, :unique, :devicetype, :appType)";
		return entityManager.createNativeQuery(nativeQuery)
				.setParameter("url", data.get("currentUrl"))
				.setParameter("ip", data.get("currentIp"))
				.setParameter("agent", data.get("browserName"))
				.setParameter("role", data.getBigInteger("userRoleId"))
				.setParameter("userid", data.getBigInteger("userId"))
				.setParameter("name", data.get("fullName"))
				.setParameter("session", data.get("userSession"))
				.setParameter("field", data.get("changedFields").toString())
				.setParameter("type", data.get("actionType"))
				.setParameter("remark", data.get("actionRemark"))
				.setParameter("unique", data.get("uniqueNo"))
				.setParameter("devicetype", data.get("deviceType"))
				.setParameter("appType", data.getInt("appType"))
				.executeUpdate();
	}

}
