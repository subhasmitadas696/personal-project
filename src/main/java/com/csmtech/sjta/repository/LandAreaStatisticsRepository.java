package com.csmtech.sjta.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.DigitalFileDTO;
import com.csmtech.sjta.dto.StatisticsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class LandAreaStatisticsRepository {
	@Autowired
	private EntityManager entityManager;

	JSONObject reqData = null;

	public StatisticsDTO fetchStatisticsInfo(String data) {
		String district_code = "";
		String tahasil_code = "";
		String village_code = "";
		if (!data.equalsIgnoreCase("\"\"")) {
			reqData = new JSONObject(data);

			district_code = (String) reqData.get("district_code");
			tahasil_code = (String) reqData.get("tahasil_code");
			village_code = (String) reqData.get("village_code");
		}

		String nativeQuery = "SELECT COUNT(DISTINCT d.district_code) AS district, COUNT(DISTINCT t.tahasil_code) AS tahasil, COUNT(DISTINCT v.village_code) AS village, COUNT(DISTINCT k.khatian_code) AS khata, COUNT(DISTINCT p.plot_code) AS plot, SUM(p.area_acre) AS plot_area ";

		if (!village_code.equalsIgnoreCase("")) {
			nativeQuery += " , v.village_name ";
		} else if (!tahasil_code.equalsIgnoreCase("")) {
			nativeQuery += " , t.tahasil_name ";
		} else if (!district_code.equalsIgnoreCase("")) {
			nativeQuery += " , d.district_name ";
		}

		nativeQuery += " FROM land_bank.district_master d "
				+ " JOIN land_bank.tahasil_master t ON t.district_code = d.district_code "
				+ " JOIN land_bank.village_master v ON t.tahasil_code = v.tahasil_code "
				+ " JOIN land_bank.khatian_information k ON v.village_code = k.village_code "
				+ " JOIN land_bank.plot_information p ON k.khatian_code = p.khatian_code WHERE 1=1 ";

		if (!village_code.equalsIgnoreCase("")) {
			nativeQuery += " AND v.village_code = :village_code GROUP BY v.village_name ";
		} else if (!tahasil_code.equalsIgnoreCase("")) {
			nativeQuery += " AND t.tahasil_code = :tahasil_code GROUP BY t.tahasil_name ";
		} else if (!district_code.equalsIgnoreCase("")) {
			nativeQuery += " AND d.district_code = :district_code GROUP BY d.district_name ";
		}

		Query q = entityManager.createNativeQuery(nativeQuery);

		if (!village_code.equalsIgnoreCase("")) {
			q.setParameter("village_code", village_code);
		} else if (!tahasil_code.equalsIgnoreCase("")) {
			q.setParameter("tahasil_code", tahasil_code);
		} else if (!district_code.equalsIgnoreCase("")) {
			q.setParameter("district_code", district_code);
		}

		List<Object[]> result = q.getResultList();

		if (result.isEmpty()) {
			StatisticsDTO statisticsDTO = new StatisticsDTO();
			statisticsDTO.setTotal_district(BigInteger.ZERO);
			statisticsDTO.setTotal_tahasil(BigInteger.ZERO);
			statisticsDTO.setTotal_village(BigInteger.ZERO);
			statisticsDTO.setTotal_khata(BigInteger.ZERO);
			statisticsDTO.setTotal_plot(BigInteger.ZERO);
			statisticsDTO.setTotal_area("0");
			statisticsDTO.setDistrict_name("");
			statisticsDTO.setTahasil_name("");
			statisticsDTO.setVillage_name("");
			return statisticsDTO;
		} else {
			Object[] row = result.get(0);
			StatisticsDTO statisticsDTO = new StatisticsDTO();
			statisticsDTO.setTotal_district((BigInteger) row[0]);
			statisticsDTO.setTotal_tahasil((BigInteger) row[1]);
			statisticsDTO.setTotal_village((BigInteger) row[2]);
			statisticsDTO.setTotal_khata((BigInteger) row[3]);
			statisticsDTO.setTotal_plot((BigInteger) row[4]);
			statisticsDTO.setTotal_area(((BigDecimal) row[5]).toString());
			statisticsDTO.setDistrict_name("");
			statisticsDTO.setTahasil_name("");
			statisticsDTO.setVillage_name("");

			if (!village_code.equalsIgnoreCase("")) {
				statisticsDTO.setVillage_name(row[6].toString());
			} else if (!tahasil_code.equalsIgnoreCase("")) {
				statisticsDTO.setTahasil_name(row[6].toString());
			} else if (!district_code.equalsIgnoreCase("")) {
				statisticsDTO.setDistrict_name(row[6].toString());
			}

			entityManager.close();
			return statisticsDTO;
		}
	}

	@SuppressWarnings("unchecked")
	public List<DigitalFileDTO> plotsDoc(String formParams) {
		JSONObject data = new JSONObject(formParams);
		String dist_code = data.getString("dist_code");
		String tahasil_code = data.getString("tahasil_code");
		String vil_code = data.getString("vil_code");
		String khata_no = data.getString("khata_no");
		String plot_no = data.getString("plot_no");

		String nativeQuery = " SELECT DISTINCT digital_file AS digitalFile FROM land_bank.plot_information AS pi  "
				+ "JOIN land_bank.khatian_digital_information AS kdi ON pi.khatian_code = kdi.khatian_code  "
				+ "WHERE 1=1 ";

		if (!dist_code.isEmpty()) {
			nativeQuery += " AND kdi.district_code = :dist_code ";
		}
		if (!tahasil_code.isEmpty()) {
			nativeQuery += " AND kdi.tahasil_code = :tahasil_code ";
		}
		if (!vil_code.isEmpty()) {
			nativeQuery += " AND kdi.village_code = :vil_code ";
		}
		if (!khata_no.isEmpty()) {
			nativeQuery += " AND kdi.khatian_code = :khata_no ";
		}
		if (!plot_no.isEmpty()) {
			nativeQuery += " AND pi.plot_code = :plot_no ";
		}

		Query query = entityManager.createNativeQuery(nativeQuery, DigitalFileDTO.class);

		if (!dist_code.isEmpty()) {
			query.setParameter("dist_code", dist_code);
		}
		if (!tahasil_code.isEmpty()) {
			query.setParameter("tahasil_code", tahasil_code);
		}
		if (!vil_code.isEmpty()) {
			query.setParameter("vil_code", vil_code);
		}
		if (!khata_no.isEmpty()) {
			query.setParameter("khata_no", khata_no);
		}
		if (!khata_no.isEmpty()) {
			query.setParameter("khata_no", khata_no);
		}
		if (!plot_no.isEmpty()) {
			query.setParameter("plot_no", plot_no);
		}

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> plotInfoForTableView(String formParams) {
		JSONObject data = new JSONObject(formParams);
		String vill_code = data.getString("vill_code");
		String khata_no = data.getString("khata_no");
		String plot_no = data.getString("plot_no");
		String from_area = data.getString("from_area");
		String to_area = data.getString("to_area");
		String current_status = data.getString("current_status");
		
		String nativeQuery = "SELECT CAST(ST_extent(ST_Transform(spbv.geom, 3857)) AS character varying) AS extent,   "
				+ "spbv.area_acre,spbv.plot_no,vm.village_name,spbv.khata_no,spbv.kissam,spbv.plot_code,   "
				+ "spbv.village_code,spbv.chaka_number,spbv.sotwa,spbv.marfatdar_name,spbv.publication_date,   "
				+ "spbv.owner_name,spbv.current_status, "
				+ "(SELECT CAST(string_agg(lulc_description, ', ') AS VARCHAR) "
				+ "FROM land_bank.sjta_plot_lulc AS lulc "
				+ "WHERE spbv.plot_code = lulc.plot_code) AS lulc_descriptions  FROM "
				+ "land_bank.sjta_plot_boundary_view AS spbv  "
				+ "JOIN land_bank.village_master AS vm ON spbv.village_code = vm.village_code WHERE  1=1 ";

		if (!vill_code.isEmpty()) {
			nativeQuery += " AND spbv.village_code = :vill_code ";
		}
		if (!khata_no.isEmpty()) {
			nativeQuery += " AND spbv.khata_no = :khata_no ";
		}
		if (!plot_no.isEmpty()) {
			nativeQuery += " AND spbv.plot_code = :plot_no ";
		}
		if (!from_area.isEmpty() || !to_area.isEmpty()) {
			nativeQuery += " AND (spbv.area_acre BETWEEN :from_area AND :to_area) ";
		}
		if (!current_status.isEmpty()) {
			nativeQuery += " AND spbv.current_status = :current_status ";
		}
		nativeQuery += "GROUP BY spbv.area_acre, spbv.plot_no, vm.village_name, spbv.khata_no, spbv.kissam, spbv.plot_code, spbv.village_code, spbv.chaka_number, spbv.sotwa, spbv.marfatdar_name, spbv.publication_date, spbv.owner_name, spbv.current_status";

		Query query = entityManager.createNativeQuery(nativeQuery);

		if (!vill_code.isEmpty()) {
			query.setParameter("vill_code", vill_code);
		}
		if (!khata_no.isEmpty()) {
			query.setParameter("khata_no", khata_no);
		}
		if (!plot_no.isEmpty()) {
			query.setParameter("plot_no", plot_no);
		}
		if (!from_area.isEmpty() || !to_area.isEmpty()) {
			query.setParameter("from_area", Double.parseDouble(from_area));
			query.setParameter("to_area", Double.parseDouble(to_area));
		}
		if (!current_status.isEmpty()) {
			query.setParameter("current_status", Integer.parseInt(current_status));
		}

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> landApplicationDetails(String formParams) {
		JSONObject data = new JSONObject(formParams);
		String plot_code = data.getString("plot_code");

		String nativeQuery = " SELECT la.application_no, la.applicant_name, CAST(json_agg(json_build_object('document_name', mdt.doc_name, 'docs_path', ld.docs_path)) AS VARCHAR) AS documents,  "
				+ "mas.application_status  " + "FROM land_schedule AS ls  "
				+ "JOIN land_application AS la ON ls.land_application_id = la.land_application_id  "
				+ "JOIN land_documents AS ld ON la.land_application_id = ld.land_application_id  "
				+ "JOIN m_document_type AS mdt ON ld.document_name = mdt.doc_type_id  "
				+ "JOIN m_application_status AS mas On la.app_status = mas.application_status_id  "
				+ "WHERE ls.deleted_flag = '0' ";

		if (!plot_code.isEmpty()) {
			nativeQuery += " AND ls.plot_code = :plot_code ";
		}

		nativeQuery += "GROUP BY la.application_no, la.applicant_name, mas.application_status";

		Query query = entityManager.createNativeQuery(nativeQuery);

		if (!plot_code.isEmpty()) {
			query.setParameter("plot_code", plot_code);
		}

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> landMeetingDetails(String formParams) {
		JSONObject data = new JSONObject(formParams);
		String plot_code = data.getString("plot_code");

		String nativeQuery = " SELECT DISTINCT ms.meeting_no, mml.meeting_level, ms.meeting_date, ms.upload_mom  "
				+ "FROM application.meeting_schedule_applicant AS msa  "
				+ "JOIN application.meeting_schedule AS ms ON msa.meeting_schedule_id = ms.meeting_schedule_id  "
				+ "JOIN m_meeting_level AS mml ON ms.meeting_level_id = mml.meeting_level_id  "
				+ "WHERE msa.status = '0' ";

		if (!plot_code.isEmpty()) {
			nativeQuery += " AND msa.plot_no = :plot_code ";
		}

		Query query = entityManager.createNativeQuery(nativeQuery);

		if (!plot_code.isEmpty()) {
			query.setParameter("plot_code", plot_code);
		}

		return query.getResultList();
	}

	@Transactional
	public Integer updateLandGis(Object plotCode, Integer categoryType, Integer actionType) {
		String nativeQuery = " UPDATE land_bank.land_gis SET  ";
		if (categoryType == 1) { // Land Application
			nativeQuery += "app_status = :actionType, current_status = :categoryType  ";
		} else if (categoryType == 2) { // Land Evaluation
			nativeQuery += "land_eve = :actionType, current_status = :categoryType  ";
		} else if (categoryType == 3) { // Meeting
			nativeQuery += "meeting = :actionType, current_status = :categoryType  ";
		} else if (categoryType == 4) { // Land Allotment
			nativeQuery += "land_allotment = :actionType, current_status = :categoryType  ";
		} else if (categoryType == 5) { // Post Allotment
			nativeQuery += "post_allotment = :actionType, current_status = :categoryType  ";
		} else if (categoryType == 6) { // Grievance
			nativeQuery += "grievance = :actionType  ";
		}
		nativeQuery += "WHERE plot_code IN (" + plotCode + ")";

		Query query = entityManager.createNativeQuery(nativeQuery);
		query.setParameter("actionType", actionType);
		if (categoryType != 6) {
			query.setParameter("categoryType", categoryType);
		}

		return query.executeUpdate();
	}

	@Transactional
	public Integer updateLandGisSingle(String plotCode, Integer categoryType, Integer actionType) {
		String nativeQuery = " UPDATE land_bank.land_gis SET  ";
		if (categoryType == 1) { // Land Application
			nativeQuery += "app_status = :actionType, current_status = :categoryType  ";
		} else if (categoryType == 2) { // Land Evaluation
			nativeQuery += "land_eve = :actionType, current_status = :categoryType  ";
		} else if (categoryType == 3) { // Meeting
			nativeQuery += "meeting = :actionType, current_status = :categoryType  ";
		} else if (categoryType == 4) { // Land Allotment
			nativeQuery += "land_allotment = :actionType, current_status = :categoryType  ";
		} else if (categoryType == 5) { // Post Allotment
			nativeQuery += "post_allotment = :actionType, current_status = :categoryType  ";
		} else if (categoryType == 6) { // Grievance
			nativeQuery += "grievance = :actionType  ";
		}
		nativeQuery += "WHERE plot_code IN ('" + plotCode.toString() + "')";

		Query query = entityManager.createNativeQuery(nativeQuery);
		query.setParameter("actionType", actionType);
		if (categoryType != 6) {
			query.setParameter("categoryType", categoryType);
		}

		return query.executeUpdate();
	}

	@Transactional
	public Integer landMerge(JSONObject data) {
		ObjectMapper objectMapper = new ObjectMapper();

		String selectedPlots = Arrays.stream(objectMapper.convertValue(data.get("selected_plot_ids"), String[].class))
				.map(s -> "'" + s + "'").collect(Collectors.joining(","));
		String selectedPlotsF = Arrays.stream(objectMapper.convertValue(data.get("selected_plot_ids"), String[].class))
				.collect(Collectors.joining(","));

		String geomQuery = "SELECT CAST(ST_UNION(geom) AS VARCHAR) AS geom FROM land_bank.sjta_plot_boundary WHERE plot_code IN ("
				+ selectedPlots + ")";
		Object geom = entityManager.createNativeQuery(geomQuery).getSingleResult();

		String query = "INSERT INTO land_bank.sjta_plot_boundary_merge_split(village_code,geom,ref_id,new_plot_no,khata_no,type) "
				+ " VALUES(:villageCode, ST_Multi(ST_GeomFromEWKB(decode(:geom, 'hex'))), :refId, :newPlot, :khataNo, :type)";

		return entityManager.createNativeQuery(query).setParameter("type", "merge")
				.setParameter("khataNo", data.get("khata_code")).setParameter("villageCode", data.get("village_code"))
				.setParameter("refId", selectedPlotsF).setParameter("geom", geom.toString())
				.setParameter("newPlot", data.get("new_plotno")).executeUpdate();
	}

	@Transactional
	public Integer landSplit(JSONObject data) {
		String query = "INSERT INTO land_bank.sjta_plot_boundary_merge_split(village_code,geom,ref_id,new_plot_no,khata_no,type) "
				+ " VALUES(:villageCode, ST_Multi(ST_MakePolygon(ST_GeomFromText('LINESTRING(" + data.get("first_geom")
				+ ")', 4326))), :refId, :newPlot, :khataNo, :type)";

		entityManager.createNativeQuery(query).setParameter("type", "split")
				.setParameter("khataNo", data.get("khata_code")).setParameter("villageCode", data.get("village_code"))
				.setParameter("refId", data.get("old_plot_id")).setParameter("newPlot", data.get("new_first_plotno"))
//				.setParameter("geom", data.get("first_geom"))
				.executeUpdate();

		String query1 = "INSERT INTO land_bank.sjta_plot_boundary_merge_split(village_code,geom,ref_id,new_plot_no,khata_no,type) "
				+ " VALUES(:villageCode, ST_Multi(ST_MakePolygon(ST_GeomFromText('LINESTRING(" + data.get("second_geom")
				+ ")', 4326))), :refId, :newPlot, :khataNo, :type)";

		return entityManager.createNativeQuery(query1).setParameter("type", "split")
				.setParameter("khataNo", data.get("khata_code")).setParameter("villageCode", data.get("village_code"))
				.setParameter("refId", data.get("old_plot_id")).setParameter("newPlot", data.get("new_second_plotno"))
//				.setParameter("geom", data.get("second_geom"))
				.executeUpdate();
	}

	public List<Object[]> getMergeSplitDetails(String type) {
		String nativeQuery = " SELECT gid, village_code, CAST(ST_extent(ST_Transform(geom, 3857)) AS character varying) AS extent, ref_id, new_plot_no, khata_no, type FROM land_bank.sjta_plot_boundary_merge_split WHERE status = '0' AND type = :type GROUP BY gid, village_code, ref_id, new_plot_no, khata_no, type ";

		Query query = entityManager.createNativeQuery(nativeQuery).setParameter("type", type);

		return query.getResultList();
	}

	@Transactional
	public Integer updateRollBack(JSONObject data) {
		String query1 = "UPDATE land_bank.sjta_plot_boundary_merge_split SET status = '1' WHERE type = :type AND ref_id in (:gid)";

		return entityManager.createNativeQuery(query1).setParameter("type", data.get("type"))
				.setParameter("gid", data.get("ref_id")).executeUpdate();
	}
}