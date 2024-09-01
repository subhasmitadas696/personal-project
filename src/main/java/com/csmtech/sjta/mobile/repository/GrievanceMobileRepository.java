package com.csmtech.sjta.mobile.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.GrievanceMainDTO;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class GrievanceMobileRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GrievanceMainDTO findDetailsByGrievanceNumber(String grievanceNo) {
		List<GrievanceMainDTO> grievanceList = new ArrayList();
		GrievanceMainDTO grievanceDto = new GrievanceMainDTO();
		String nativeQuery = "select g.grievance_id,g.month_id,g.name,g.father_name,g.district_id,md.district_name as districtName,g.block_id,mb.block_name as blockname, "
				+ "g.gp_id,mgp.gp_name,g.village_id,mv.village_name as villageName,pi.kissam, "
				+ "g.other_information,g.caste_name,g.mobile_no,g.disclose_details,g.district_code,d.district_name,g.tahasil_code, "
				+ "t.tahasil_name,g.village_code,v.village_name,g.khatian_code,g.plot_code,g.area_acre,g.extent_occupied,g.mode_of_occupation, "
				+ "g.other_occupation, " + "g.landmark,g.upload_file,g.remarks,g.grievance_no,g.grievance_status,"
				+ "ST_Y(ST_Centroid(pb.geom)) AS latitude,ST_X(ST_Centroid(pb.geom)) AS longitude,g.remark_by_co, "
				+ "(select ST_AsText(geom) from land_bank.cadastral_plot_boundary where plot_code =pi.plot_code) as coordinates,pi.plot_no,ki.khata_no,m.month_name,g.schedule_inspection_date, "
				+ "g.inspection_date,g.land_location,g.co_uploaded_photo FROM " + " grievance g left join land_bank.district_master d "
				+ "on g.district_code = d.district_code left join land_bank.tahasil_master t on g.tahasil_code = t.tahasil_code "
				+ "left join land_bank.khatian_information ki on g.khatian_code = ki.khatian_code "
				+ "left join public.m_month m on g.month_id = m.month_id "
				+ "left join land_bank.village_master v on g.village_code = v.village_code left join m_district md on "
				+ "g.district_id = md.district_id left join m_block mb on g.block_id = mb.block_id left join m_gp mgp  "
				+ "on g.gp_id = mgp.gp_id left join m_village_master mv "
				+ "on g.village_id = mv.village_id left join land_bank.plot_information pi on g.plot_code = pi.plot_code   "
				+ "LEFT JOIN land_bank.sjta_plot_boundary pb ON(g.plot_code=pb.plot_code) "
				+ "where g.grievance_no =:grievanceNo and g.deleted_flag = '0'";
		Query query = entity.createNativeQuery(nativeQuery).setParameter("grievanceNo", grievanceNo);
		try {
			log.info("query starts executing of findDetailsByGrievanceNumber");
			List<Object[]> resultList = query.getResultList();
			log.info("result fetched from db findDetailsByGrievanceNumber: " + resultList.toString());
			if (resultList != null && resultList.size() > 0) {
				grievanceList = mapToGrievanceDto(resultList);
				grievanceDto = grievanceList.get(0);
			} else {
				return grievanceDto;
			}
			log.info("grievance details from db: " + grievanceDto);
		} finally {
			entity.close();
		}
		return grievanceDto;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<GrievanceMainDTO> findByGrievanceStatus(Integer grievanceStatus, String type) {
		List<GrievanceMainDTO> grievanceList = new ArrayList<>();
		String nativeQuery = "select g.grievance_id,g.month_id,g.name,g.father_name,g.district_id,md.district_name as districtName,g.block_id,mb.block_name as blockname, "
				+ "g.gp_id,mgp.gp_name,g.village_id,mv.village_name as villageName,pi.kissam, "
				+ "g.other_information,g.caste_name,g.mobile_no,g.disclose_details,g.district_code,d.district_name,g.tahasil_code, "
				+ "t.tahasil_name,g.village_code,v.village_name,g.khatian_code,g.plot_code,g.area_acre,g.extent_occupied,g.mode_of_occupation, "
				+ "g.other_occupation, " + "g.landmark,g.upload_file,g.remarks,g.grievance_no,g.grievance_status,"
				+ "ST_Y(ST_Centroid(pb.geom)) AS latitude,ST_X(ST_Centroid(pb.geom)) AS longitude,g.remark_by_co, "
				+ "(select ST_AsText(geom) from land_bank.cadastral_plot_boundary where plot_code =pi.plot_code) as coordinates,pi.plot_no,ki.khata_no,m.month_name,g.schedule_inspection_date, "
				+ "g.inspection_date,g.land_location,g.co_uploaded_photo FROM " + " grievance g left join land_bank.district_master d "
				+ "on g.district_code = d.district_code left join land_bank.tahasil_master t on g.tahasil_code = t.tahasil_code "
				+ "left join land_bank.khatian_information ki on g.khatian_code = ki.khatian_code "
				+ "left join public.m_month m on g.month_id = m.month_id "
				+ "left join land_bank.village_master v on g.village_code = v.village_code left join m_district md on "
				+ "g.district_id = md.district_id left join m_block mb on g.block_id = mb.block_id left join m_gp mgp  "
				+ "on g.gp_id = mgp.gp_id left join m_village_master mv "
				+ "on g.village_id = mv.village_id left join land_bank.plot_information pi on g.plot_code = pi.plot_code   "
				+ "LEFT JOIN land_bank.sjta_plot_boundary pb ON(g.plot_code=pb.plot_code) "
				+ "where g.grievance_status =:grievanceStatus and g.deleted_flag = '0' ";

		if (type.equalsIgnoreCase("P")) {
			nativeQuery += " ORDER BY schedule_inspection_date ASC ";
		} else if (type.equalsIgnoreCase("C")) {
			nativeQuery += " ORDER BY inspection_date DESC ";
		}
		Query query = entity.createNativeQuery(nativeQuery).setParameter("grievanceStatus", grievanceStatus);
		try {
			log.info("query starts executing of findByGrievanceStatus");
			List<Object[]> resultList = query.getResultList();
			log.info("result fetched from db of findByGrievanceStatus: " + resultList);
			if (resultList != null && resultList.size() > 0) {
				grievanceList = mapToGrievanceDto(resultList);
				log.info("result fetched from db in dto: " + grievanceList);
			} else {
				return grievanceList;
			}

			return grievanceList;

		} finally {
			entity.close();
		}

	}

	private List<GrievanceMainDTO> mapToGrievanceDto(List<Object[]> resultList) {

		return resultList.stream().map(objects -> {
			GrievanceMainDTO grievanceMainDTO = new GrievanceMainDTO();
			grievanceMainDTO.setGrievanceId((Integer) objects[0]);
			grievanceMainDTO.setMonthId(objects[1] != null ? (String) objects[1].toString() : "");
			grievanceMainDTO.setName(objects[2] != null ? (String) objects[2].toString() : "");
			grievanceMainDTO.setFatherName(objects[3] != null ? objects[3].toString() : "");
			grievanceMainDTO.setDistrictId(objects[4] != null ? objects[4].toString() : "");
			grievanceMainDTO.setDistrictIdName(objects[5] != null ? objects[5].toString() : "");
			grievanceMainDTO.setBlockId(objects[6] != null ? objects[6].toString() : "");
			grievanceMainDTO.setBlockIdName(objects[7] != null ? objects[7].toString() : "");
			grievanceMainDTO.setGpId(objects[8] != null ? objects[8].toString() : "");
			grievanceMainDTO.setGpIdName(objects[9] != null ? objects[9].toString() : "");
			grievanceMainDTO.setVillageId(objects[10] != null ? objects[10].toString() : "");
			grievanceMainDTO.setVillageIdName(objects[11] != null ? (String) objects[11].toString() : "");
			grievanceMainDTO.setKissam(objects[12] != null ? (String) objects[12].toString() : "");
			grievanceMainDTO.setOtherInformation(objects[13] != null ? (String) objects[13].toString() : "");
			grievanceMainDTO.setCasteName(objects[14] != null ? (String) objects[14].toString() : "");
			grievanceMainDTO.setMobileNo(objects[15] != null ? (String) objects[15].toString() : "");
			grievanceMainDTO.setDiscloseDetails(objects[16] != null ? (Short) objects[16] : 0);
			grievanceMainDTO.setDistrictCode(objects[17] != null ? (String) objects[17].toString() : "");
			grievanceMainDTO.setDistrictName(objects[18] != null ? (String) objects[18].toString() : "");
			grievanceMainDTO.setTahasilCode(objects[19] != null ? (String) objects[19].toString() : "");
			grievanceMainDTO.setTahasilName(objects[20] != null ? (String) objects[20].toString() : "");
			grievanceMainDTO.setVillageCode(objects[21] != null ? (String) objects[21].toString() : "");
			grievanceMainDTO.setVillageName(objects[22] != null ? (String) objects[22].toString() : "");
			grievanceMainDTO.setKhatianCode(objects[23] != null ? (String) objects[23].toString() : "");
			grievanceMainDTO.setPlotNo(objects[24] != null ? (String) objects[24].toString() : "");
			grievanceMainDTO.setAreaAcre(objects[25] != null ? (objects[25]).toString() : "0");
			grievanceMainDTO.setExtentOccupied(objects[26] != null ? (objects[26]).toString() : "0");
			grievanceMainDTO.setModeOfOccupation(objects[27] != null ? (Short) objects[27] : 0);
			grievanceMainDTO.setOtherOccupation(objects[28] != null ? (String) objects[28].toString() : "");
			grievanceMainDTO.setLandmark(objects[29] != null ? (String) objects[29].toString() : "");
			grievanceMainDTO.setUploadFile(objects[30] != null ? (String) objects[30].toString() : "");
			grievanceMainDTO.setRemarks(objects[31] != null ? (String) objects[31].toString() : "");
			grievanceMainDTO.setGrievanceNo(objects[32] != null ? (String) objects[32].toString() : "");
			grievanceMainDTO.setGrivanceStatus(objects[33] != null ? (Short) objects[33] : 0);
			grievanceMainDTO.setLatitude(objects[34] != null ? objects[34].toString() : "");
			grievanceMainDTO.setLongitude(objects[35] != null ? objects[35].toString() : "");
			grievanceMainDTO.setCoRemarks(objects[36] != null ? (String) objects[36].toString() : "");
			grievanceMainDTO.setCoordinates(objects[37] != null ? (String) objects[37].toString() : "");
			grievanceMainDTO.setPlotNumber(objects[38] != null ? (String) objects[38].toString() : "");
			grievanceMainDTO.setKhataNumber(objects[39] != null ? (String) objects[39].toString() : "");
			grievanceMainDTO.setMonthName(objects[40] != null ? (String) objects[40].toString() : "");
			grievanceMainDTO.setScheduledInspectionDate(objects[41] != null ? (String) objects[41].toString() : "");
			grievanceMainDTO.setInspectionDate(objects[42] != null ? (String) objects[42].toString() : "");
			grievanceMainDTO.setLandLocation(objects[43] != null ? (String) objects[43].toString() : "");
			grievanceMainDTO.setCoUploadedPhoto(objects[44] != null ? (String) objects[44].toString() : "");
			return grievanceMainDTO;
		}).collect(Collectors.toList());

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GrievanceMainDTO findDetailsByPlotCode(String plotNo) {
		List<GrievanceMainDTO> grievanceList = new ArrayList();
		GrievanceMainDTO grievanceDto = new GrievanceMainDTO();
		String nativeQuery = "select pi.plot_code,pi.plot_no,pi.area_acre from land_bank.plot_information pi "
				+ "where pi.plot_code =:plotNo";
		Query query = entity.createNativeQuery(nativeQuery).setParameter("plotNo", plotNo);
		try {
			log.info("query starts executing of findDetailsByPlotCode");
			List<Object[]> resultList = query.getResultList();
			log.info("result fetched from db of findDetailsByPlotCode: " + resultList.toString());
			if (resultList != null && resultList.size() > 0) {
				grievanceList = resultList.stream()
						.map(objects -> new GrievanceMainDTO((String) objects[0].toString(),
								(String) objects[1].toString(), (String) objects[2].toString()))
						.collect(Collectors.toList());
				grievanceDto = grievanceList.get(0);
			} else {
				return grievanceDto;
			}
			log.info("grievance details from db: " + grievanceDto);
		} finally {
			entity.close();
		}
		return grievanceDto;
	}

}
