package com.csmtech.sjta.repositoryImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.repository.ScanningProgressReportRepository;
import com.csmtech.sjta.util.CommonUtil;

@Repository
public class ScanningProgressReportRepositoryImpl implements ScanningProgressReportRepository {
	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@Override
	@Transactional
	public Integer saveProgressData(Map<String, String>[] excelData) {
		String truncateQuery = "TRUNCATE TABLE scanning_progress_report";
		entity.createNativeQuery(truncateQuery).executeUpdate();

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("INSERT INTO public.scanning_progress_report(\r\n"
				+ "	district_name, no_of_tahasil, no_of_village, no_of_khatian, no_of_plot, area_acre, total_pages, status)\r\n"
				+ "	VALUES  ");

		for (int i = 0; i < excelData.length; i++) {
			queryBuilder.append("(:districtName" + i + ", :tahasil" + i + ", :village" + i + ", :khata" + i + ", :plot"
					+ i + ", :area" + i + ", :pages" + i + ", :status" + i + ")");
			if (i < excelData.length - 1) {
				queryBuilder.append(", ");
			}
		}

		Query query = entity.createNativeQuery(queryBuilder.toString());

		for (int i = 0; i < excelData.length; i++) {
			Map<String, String> rowMap = excelData[i];

			BigInteger totalTahasil = !rowMap.get("totalTahasil").equals("")
					? new BigDecimal(rowMap.get("totalTahasil")).toBigInteger()
					: BigInteger.valueOf(0);
			BigInteger totalVillage = !rowMap.get("totalVillage").equals("")
					? new BigDecimal(rowMap.get("totalVillage")).toBigInteger()
					: BigInteger.valueOf(0);
			BigInteger totalKhatian = !rowMap.get("totalKhatian").equals("")
					? new BigDecimal(rowMap.get("totalKhatian")).toBigInteger()
					: BigInteger.valueOf(0);
			BigInteger totalPlot = !rowMap.get("totalPlot").equals("")
					? new BigDecimal(rowMap.get("totalPlot")).toBigInteger()
					: BigInteger.valueOf(0);
			BigDecimal area = !rowMap.get("area").equals("") ? new BigDecimal(rowMap.get("area"))
					: BigDecimal.valueOf(0);
			BigInteger totalPages = !rowMap.get("totalPages").equals("")
					? new BigDecimal(rowMap.get("totalPages")).toBigInteger()
					: BigInteger.valueOf(0);

			query.setParameter("districtName" + i, rowMap.get("district")).setParameter("tahasil" + i, totalTahasil)
					.setParameter("village" + i, totalVillage).setParameter("khata" + i, totalKhatian)
					.setParameter("plot" + i, totalPlot).setParameter("area" + i, area)
					.setParameter("pages" + i, totalPages).setParameter("status" + i, rowMap.get("status"));
		}

		entity.close();
		Integer updateStatus = query.executeUpdate();
		return updateStatus;
	}

	@Override
	public JSONArray getProgressData(String data) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "SELECT district_name, no_of_tahasil, no_of_village, no_of_khatian, no_of_plot, area_acre, total_pages, status FROM public.scanning_progress_report ORDER By district_name";

		List<Object[]> dataList = CommonUtil.getDynResultList(entity, query);
		for (Object[] data1 : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("district_name", data1[0]);
			jsonObj.put("no_of_tahasil", data1[1]);
			jsonObj.put("no_of_village", data1[2]);
			jsonObj.put("no_of_khatian", data1[3]);
			jsonObj.put("no_of_plot", data1[4]);
			jsonObj.put("area_acre", data1[5].toString());
			jsonObj.put("total_pages", data1[6]);
			jsonObj.put("status", data1[7]);
			mainJSONFile.put(jsonObj);
		}

		return mainJSONFile;
	}

}
