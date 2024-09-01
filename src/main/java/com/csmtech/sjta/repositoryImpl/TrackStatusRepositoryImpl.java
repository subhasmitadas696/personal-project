package com.csmtech.sjta.repositoryImpl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.repository.TrackStatusRepository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class TrackStatusRepositoryImpl implements TrackStatusRepository {
	@PersistenceContext
	@Autowired
	EntityManager entity;

	private static final Logger logger = LoggerFactory.getLogger(TrackStatusRepositoryImpl.class);
	Object[] statusVal = null;

	@Override
	public Object[] fetchGrievanceStatus(JSONObject data) {
		String appNo = data.getString("applicationNo");

		try {
			String sqlQuery = "SELECT grievance_status, grievance_no FROM grievance WHERE deleted_flag = '0' AND grievance_no = :applicationNo";
			statusVal = (Object[]) entity.createNativeQuery(sqlQuery).setParameter("applicationNo", appNo)
					.getSingleResult();

			entity.close();
		} catch (NoResultException | NonUniqueResultException e) {
			statusVal = null;
			logger.error("Inside save method of TrackStatusRepositoryImpl some error occur : ", e.getMessage());
		}

		return statusVal;
	}

	@Override
	public Object[] fetchLandApplicationStatus(JSONObject data) {
		String appNo = data.getString("applicationNo");

		try {
			String sqlQuery = "SELECT laa.application_status_id, mas.application_status FROM land_application AS la\r\n"
					+ "INNER JOIN land_application_approval AS laa ON la.land_application_id = laa.land_application_id\r\n"
					+ "INNER JOIN m_application_status AS mas ON laa.application_status_id = mas.application_status_id\r\n"
					+ "WHERE la.application_no =  :applicationNo";
			statusVal = (Object[]) entity.createNativeQuery(sqlQuery).setParameter("applicationNo", appNo)
					.getSingleResult();
		} catch (NoResultException | NonUniqueResultException e) {
			statusVal = null;
			logger.error("Inside save method of TrackStatusRepositoryImpl some error occur:" + e);
		}

		return statusVal;
	}

}
