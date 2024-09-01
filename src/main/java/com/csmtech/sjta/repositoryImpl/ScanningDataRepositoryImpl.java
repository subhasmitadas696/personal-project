package com.csmtech.sjta.repositoryImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.repository.ScanningDataRepository;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.JsonNode;

@Repository
public class ScanningDataRepositoryImpl implements ScanningDataRepository {
	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@Override
	public Integer verifyDocument(JsonNode jsonNode) {

		String fileType = jsonNode.get("fileType").asText();
		String districtCode = jsonNode.get("districtCode").asText();
		String khataNo = jsonNode.get("khataNo").asText();
		String fileName = jsonNode.get("fileName").asText();
		String remark = jsonNode.get("remark").asText();
		Integer status = jsonNode.get("status").asInt();
		Integer createdBy = jsonNode.get("createdBy").asInt();

		String query = "INSERT INTO public.scanned_doc_verification(\r\n"
				+ "file_type, district_code, khata_no, file_name, status, reamark, created_by)\r\n"
				+ "VALUES (:fileType, :districtCode, :khataNo, :fileName, :status, :remark, :createdBy)";

		return entity.createNativeQuery(query).setParameter("fileType", fileType)
				.setParameter("districtCode", districtCode).setParameter("khataNo", khataNo)
				.setParameter("fileName", fileName).setParameter("status", status).setParameter("remark", remark)
				.setParameter("createdBy", createdBy).executeUpdate();
	}

	@Override
	public JSONArray getProgressData(String data) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "SELECT sdv.scanned_doc_verification_id, sdv.file_type, dm.district_name, sdv.khata_no, sdv.file_name, sdv.reamark \r\n"
				+ "FROM public.scanned_doc_verification  AS sdv\r\n"
				+ "JOIN land_bank.district_master AS dm ON dm.district_code = sdv.district_code\r\n"
				+ "WHERE deleted_flag = '0' AND status = 2";

		List<Object[]> dataList = CommonUtil.getDynResultList(entity, query);
		for (Object[] data1 : dataList) {
			Map<String, Object> mapObj = new HashMap<>();
			mapObj.put("scanned_doc_verification_id", data1[0]);
			mapObj.put("file_type", data1[1]);
			mapObj.put("district_name", data1[2]);
			mapObj.put("khata_no", data1[3]);
			mapObj.put("file_name", data1[4]);
			mapObj.put("reamark", data1[5]);
			mainJSONFile.put(mapObj);
		}

		return mainJSONFile;
	}

	@Override
	public List<String> verifiedDocument() {
		String query = "SELECT sdv.file_name, sdv.status \r\n" + "FROM public.scanned_doc_verification  AS sdv\r\n"
				+ "WHERE deleted_flag = '0'";

		List<String> dataList = entity.createNativeQuery(query).getResultList();

		return dataList;
	}

	@Override
	public List<String> allFiles(String districtCode, String tahasilCode, String villageCode) {
		String query = "SELECT kd.district_code,  kd.tahasil_code,  kd.village_code, kd.khata_no,  kd.digital_file, tm.tahasil_name,\r\n"
				+ " vm.village_name, k.owner_name, k.marfatdar_name \r\n"
				+ "FROM land_bank.khatian_digital_information kd LEFT JOIN land_bank.khatian_information k USING(khatian_code) LEFT JOIN land_bank.village_master vm ON(vm.village_code = kd.village_code)\r\n"
				+ " LEFT JOIN land_bank.tahasil_master tm ON(tm.tahasil_code = kd.tahasil_code) WHERE 1=1 ";

		if (!districtCode.equals("0")) {
			query += "AND kd.district_code = :districtCode ";
		}

		if (!tahasilCode.equals("0")) {
			query += "AND kd.tahasil_code = :tahasilCode ";
		}

		if (!villageCode.equals("0")) {
			query += "AND kd.village_code = :villageCode ";
		}

		query += "ORDER BY\r\n" + "length(case   \r\n"
				+ "            when POSITION( '/' in kd.khata_no) > 0 then SUBSTRING (kd.khata_no,\r\n" + "  1,\r\n"
				+ "            POSITION( '/' in kd.khata_no)- 1)  \r\n"
				+ "  when POSITION( '-' in kd.khata_no) > 0 then SUBSTRING (kd.khata_no,\r\n" + " 1,\r\n"
				+ "            POSITION( '-' in kd.khata_no)- 1)  \r\n" + "  else kd.khata_no \r\n"
				+ "        end),\r\n" + "        case   \r\n"
				+ "  when POSITION( '/' in kd.khata_no) > 0 then SUBSTRING (kd.khata_no,\r\n" + "  1,\r\n"
				+ "            POSITION( '/' in kd.khata_no)- 1)  \r\n"
				+ " when POSITION( '-' in kd.khata_no) > 0 then SUBSTRING (kd.khata_no,\r\n" + " 1,\r\n"
				+ "            POSITION( '-' in kd.khata_no)- 1)  \r\n" + " else kd.khata_no   \r\n"
				+ "        end     ASC";

		Query q = entity.createNativeQuery(query);
		if (!districtCode.equals("0")) {
			q.setParameter("districtCode", districtCode);
		}

		if (!tahasilCode.equals("0")) {
			q.setParameter("tahasilCode", tahasilCode);
		}

		if (!villageCode.equals("0")) {
			q.setParameter("villageCode", villageCode);
		}

		List<String> dataList = q.getResultList();

		return dataList;
	}

}
