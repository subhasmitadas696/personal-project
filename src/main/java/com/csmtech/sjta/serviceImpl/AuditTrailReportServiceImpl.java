package com.csmtech.sjta.serviceImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.csmtech.sjta.dto.AuditTrailReportDTO;
import com.csmtech.sjta.repository.AuditTrailReportRepository;
import com.csmtech.sjta.service.AuditTrailReportService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuditTrailReportServiceImpl implements AuditTrailReportService {
	
	@Autowired
	private AuditTrailReportRepository repo;
	

	public static JSONArray fillUserWiseRole(EntityManager em, String value) {
		JSONArray mainJSONFile = new JSONArray();
		String query = " select role_id,role_name from public.m_role where status='0' AND  role_id IN(1,2,3,4,5) ";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("role_id", data[0]);
			jsonObj.put("role_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}
	
	
	public static JSONArray fillUserWiseUserName(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = jsonDepend.get("roleId").toString();
		String query = " select ud.user_id,ud.full_name   "
				+ "from public.user_details ud  "
				+ "JOIN public.user_role ur ON(ur.user_id=ud.user_id)  "
				+ "WHERE ur.role_id= "+val+ " AND ud.status='0'  "
				+ "union  "
				+ "select cpd.citizen_profile_details_id,cpd.full_name   "
				+ "from  public.citizen_profile_details cpd  "
				+ "WHERE cpd.role_id= "+val+" AND cpd.status='0'  ";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("user_id", data[0]);
			jsonObj.put("full_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}
	
	
	public static JSONArray fillApplicationWoseApplicationNo(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		Integer val = jsonDepend.getInt("applicationType");
		String query = "";
		if(val==1) {
			query=" select land_application_id,application_no from public.land_application  "
					+ "where deleted_flag='0' AND application_no IS NOT NULL ";
		}else if(val==2) {
			query=" select grievance_id,grievance_no from public.grievance  "
					+ "where deleted_flag='0'  ";
		}else {
			query="";
		}
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("app_id", data[0]);
			jsonObj.put("app_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}
	
	
	@Override
	public List<AuditTrailReportDTO> getAuditTrailReport(String data) {
		List<AuditTrailReportDTO> getAuditTrailReport = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String applicationNo = jsonData.getString("applicationNo");
		String plot = jsonData.getString("plot");
		String username = jsonData.getString("username");
		Integer reportType = jsonData.getInt("reportType");
		String formdate=jsonData.getString("formdate");
		String  todate=jsonData.getString("todate");
		Integer  applicationType=jsonData.getInt("applicationType");
		String userrole=jsonData.getString("userrole");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = repo.getAuditTrailReport(pageSize, offset, formdate, todate,reportType,username,plot,applicationNo,applicationType,userrole);
		for (Object[] result : getRecord) {
			AuditTrailReportDTO dto = new AuditTrailReportDTO();
			 dto.setActivityLogId((BigInteger) result[0]);
			 dto.setUrl((String) result[1]);
			 dto.setServerIp((String) result[2]);
			 dto. setUserAgent((String) result[3]);
			 dto.setUserRole((BigInteger) result[4]);
			 dto.setUserId((BigInteger) result[5]);
			 dto.setUserName((String) result[6]);
			 dto.setCreatedOn((Date) result[7]);
			 dto.setUniqueNumber((String) result[8]);
			 dto.setRoleName((String) result[9]);
			 dto.setDeviceType((String) result[10]);
			 dto.setChangeField((String) result[11]);
			 dto.setActionRemerk((String) result[12]);
			getAuditTrailReport.add(dto);
		}
		log.info("AuditTrailReportServiceImpl of method getAuditTrailReport execution success.....!!");
		return getAuditTrailReport;
	}


	@Override
	public BigInteger getAuditTrailReportCount(String data) {
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		String applicationNo = jsonData.getString("applicationNo");
		String plot = jsonData.getString("plot");
		String username = jsonData.getString("username");
		Integer reportType = jsonData.getInt("reportType");
		String formdate=jsonData.getString("formdate");
		String  todate=jsonData.getString("todate");
		Integer  applicationType=jsonData.getInt("applicationType");
		String userrole=jsonData.getString("userrole");
		Integer offset = (pageNo - 1) * pageSize;
		BigInteger getRecord = repo.getAuditTrailReportCount(pageSize, offset, formdate, todate,reportType,username,plot,applicationNo,applicationType,userrole);
		return getRecord;
	}
	
	

}
