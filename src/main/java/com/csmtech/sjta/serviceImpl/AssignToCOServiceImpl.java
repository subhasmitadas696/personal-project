/**
 * 
 */
package com.csmtech.sjta.serviceImpl;

import java.util.List;

import javax.persistence.EntityManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @author prasanta.sethi
 */
@Service
public class AssignToCOServiceImpl {
	public static JSONArray fillselDistrictNameList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();

		String query = "SELECT DISTINCT\r\n" + "	(la.district_code) as district_code,\r\n"
				+ "    (dm.district_name) AS district_name\r\n" + "FROM\r\n" + "    land_application la\r\n"
				+ "JOIN\r\n" + "    land_bank.district_master dm ON la.district_code = dm.district_code\r\n"
				+ "	JOIN\r\n" + "    land_schedule ls ON la.land_application_id = ls.land_application_id\r\n";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("district_id", data[0]);
			jsonObj.put("district_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselTehsilNameList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = jsonDepend.get("district_id").toString();
		String query = "SELECT DISTINCT\r\n" + "	(la.tehsil_code) as tahasil_code,\r\n"
				+ "    (dm.tahasil_name) AS tahasil_name\r\n" + "FROM\r\n" + "    land_application la\r\n" + "JOIN\r\n"
				+ "    land_bank.tahasil_master dm ON la.tehsil_code = dm.tahasil_code\r\n" + "	JOIN\r\n"
				+ "    land_schedule ls ON la.land_application_id = ls.land_application_id\r\n" + "	where\r\n"
				+ "	la.district_code = '" + val + "' order by tahasil_name";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("tehsil_id", data[0]);
			jsonObj.put("tehsil_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselMouzaList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("tehsil_id").toString();
		String query = "SELECT DISTINCT\r\n" + "	(la.village_code) as village_code,\r\n"
				+ "    (vm.village_name) AS village_name\r\n" + "FROM\r\n" + "    land_application la\r\n" + "JOIN\r\n"
				+ "    land_bank.village_master vm ON la.village_code = vm.village_code\r\n" + "	JOIN\r\n"
				+ "    land_schedule ls ON la.land_application_id = ls.land_application_id\r\n" + "	where\r\n"
				+ "	la.tehsil_code = '" + val + "'  group by la.village_code,vm.village_name order by vm.village_name";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("mouza_id", data[0]);
			jsonObj.put("mouza_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselKhataNoList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("mouza_id").toString();
		String query = "SELECT DISTINCT\r\n" + "	(la.khatian_code) as khatian_code,\r\n"
				+ "    (ki.khata_no) AS khata_no\r\n" + "FROM\r\n" + "    land_application la\r\n" + "JOIN\r\n"
				+ "    land_bank.khatian_information ki ON la.khatian_code = ki.khatian_code\r\n" + "	JOIN\r\n"
				+ "    land_schedule ls ON la.land_application_id = ls.land_application_id\r\n" + "	where\r\n"
				+ "	la.village_code = '" + val + "'";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("khatian_code", data[0]);
			jsonObj.put("khata_no", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselPlotNoList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("khatian_code").toString();
		String query = "SELECT DISTINCT pi.plot_code, pi.plot_no  \r\n"
				+ "FROM land_bank.plot_information AS pi\r\n"
				+ "JOIN land_schedule AS ls ON pi.plot_code = ls.plot_code\r\n"
				+ "JOIN land_application AS la ON la.land_application_id = ls.land_application_id\r\n"
				+ "LEFT JOIN application.plot_land_inspection AS apli ON ls.plot_code = apli.plot_code\r\n"
				+ "WHERE la.khatian_code = :khatianCode AND apli.plot_land_inspection_id IS NULL";
		
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).setParameter("khatianCode", val).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("plot_code", data[0]);
			jsonObj.put("plot_no", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}
}
