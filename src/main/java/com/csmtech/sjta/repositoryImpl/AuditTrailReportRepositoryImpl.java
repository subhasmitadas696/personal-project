package com.csmtech.sjta.repositoryImpl;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.repository.AuditTrailReportRepository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AuditTrailReportRepositoryImpl implements AuditTrailReportRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getAuditTrailReport(Integer pagesize, Integer offset, String formDate, String todate,
			Integer reportType, String userName, String plotNo, String applicationNo, Integer applicationType,
			String userrole) {
		Integer getUserRole = null;
		Integer getUserId = null;
		if (userrole != null && !"".equals(userrole)) {
			getUserRole = Integer.parseInt(userrole);
			getUserId = Integer.parseInt(userName);
		}
		String addCondition = "";
		Date todateRe = null;
		Date formDateRe = null;
		if (reportType == 1) {
			addCondition = "";
		} else if (reportType == 2) {
			if (userrole.equalsIgnoreCase("3") || userrole.equalsIgnoreCase("4") || userrole.equalsIgnoreCase("5")
					|| userrole.equalsIgnoreCase("2") || userrole.equalsIgnoreCase("1")) { // dealing
				addCondition = "AND user_role= " + getUserRole + "  AND user_id= " + getUserId;
			}
		} else if (reportType == 3) {
			if (applicationType == 1) { // land application
				addCondition = "AND application_type=1 AND action_unique_no= '" + applicationNo + "'";
			} else {
				addCondition = "AND application_type=2 AND action_unique_no= '" + applicationNo + "'";
			}
		} else if (reportType == 4) {
			addCondition = " AND application_type=3 AND action_unique_no='" + plotNo + "'";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			formDateRe = dateFormat.parse(formDate);
			todateRe = dateFormat.parse(todate);
		} catch (ParseException e) {
			log.info("error " + e);
		}
		String sqlQuery = " SELECT activity_log_id, view_url,server_ip, user_agent, user_role, user_id, user_name,created_on,action_unique_no , "
				+ " (select role_name from public.m_role where role_id=al.user_role) , device_type ,change_field,action_remark "
				+ "FROM public.activity_log al WHERE created_on >= :formDate AND created_on <= :todate  " + addCondition
				+ "ORDER BY activity_log_id DESC " + " OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("limit", pagesize).setParameter("offset", offset)
				.setParameter("formDate", formDateRe).setParameter("todate", todateRe);
		log.info("getAuditTrailReport execution  success..!!");
		return query.getResultList();
	}
	
	
	
	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public BigInteger getAuditTrailReportCount(Integer pagesize, Integer offset, String formDate, String todate,
			Integer reportType, String userName, String plotNo, String applicationNo, Integer applicationType,
			String userrole) {
		Integer getUserRole = null;
		Integer getUserId = null;
		if (userrole != null && !"".equals(userrole)) {
			getUserRole = Integer.parseInt(userrole);
			getUserId = Integer.parseInt(userName);
		}
		String addCondition = "";
		Date todateRe = null;
		Date formDateRe = null;
		if (reportType == 1) {
			addCondition = "";
		} else if (reportType == 2) {
			if (userrole.equalsIgnoreCase("3") || userrole.equalsIgnoreCase("4") || userrole.equalsIgnoreCase("5")
					|| userrole.equalsIgnoreCase("2") || userrole.equalsIgnoreCase("1")) { // dealing
				addCondition = "AND user_role= " + getUserRole + "  AND user_id= " + getUserId;
			}
		} else if (reportType == 3) {
			if (applicationType == 1) { // land application
				addCondition = "AND application_type=1 AND action_unique_no= '" + applicationNo + "'";
			} else {
				addCondition = "AND application_type=2 AND action_unique_no= '" + applicationNo + "'";
			}
		} else if (reportType == 4) {
			addCondition = " AND application_type=3 AND action_unique_no='" + plotNo + "'";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			formDateRe = dateFormat.parse(formDate);
			todateRe = dateFormat.parse(todate);
		} catch (ParseException e) {
			log.info("error " + e);
		}
		String sqlQuery = " select count(*) from (SELECT activity_log_id, view_url,server_ip, user_agent, user_role, user_id, user_name,created_on,action_unique_no , "
				+ " (select role_name from public.m_role where role_id=al.user_role),device_type  "
				+ "FROM public.activity_log al WHERE created_on >= :formDate AND created_on <= :todate  " + addCondition
				+ "ORDER BY activity_log_id DESC ) as subquery " ;
		Query query = entity.createNativeQuery(sqlQuery)
				.setParameter("formDate", formDateRe).setParameter("todate", todateRe);
		log.info("getAuditTrailReport execution  success..!!");
		return (BigInteger) query.getSingleResult();
	}
	
	
	

}
