package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.LandAppResponseStructureDTO;
import com.csmtech.sjta.dto.LandAppViewDocumentDTO;
import com.csmtech.sjta.dto.LandApplicantDTO;
import com.csmtech.sjta.dto.LandApplicantViewDTO;
import com.csmtech.sjta.dto.LandPlotViewDTO;
import com.csmtech.sjta.entity.Land_applicant;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class LandApplicantNativeRepository {

	@PersistenceContext
	EntityManager entityManager;

	@Value("${file.path}")
	private String filePathloc;

	@SuppressWarnings("unused")

	@Transactional
	public List<LandApplicantDTO> getLandApplicantDetailsPage(BigInteger roleId, int pageNumber, int pageSize) {

		String query = "SELECT la.land_application_id, la.application_no, la.applicant_name, la.mobile_no, d.district_name, t.tahasil_name, m.village_name, k.khata_no, k.khatian_code, (select CAST(st_extent(ST_Transform(spb.geom, 3857))AS character varying) as extent from public.land_schedule ls \r\n"
				+ "join land_bank.plot_information pi on( ls.plot_code=pi.plot_code ) \r\n"
				+ "join land_bank.sjta_plot_boundary spb on( spb.plot_code=pi.plot_code ) where ls.land_application_id=la.land_application_id) , CAST((SELECT st_extent(ST_Transform(geom,3857)) as extent FROM land_bank.sjta_plot_boundary_view where plot_code in (SELECT STRING_AGG(ls.plot_code, ',') AS plot_code FROM public.land_schedule ls WHERE ls.land_application_id = la.land_application_id AND ls.deleted_flag='0' ) group by village_code)as varchar) as plot_extend, CAST((SELECT STRING_AGG((select plot_no from land_bank.plot_information where plot_code=ls.plot_code), ',') AS plot_no FROM public.land_schedule ls WHERE ls.land_application_id = la.land_application_id AND deleted_flag='0' )as varchar) as plot_no, a.created_on, d.district_code, t.tahasil_code, m.village_code FROM public.land_application la INNER JOIN land_bank.district_master d ON la.district_code = d.district_code \r\n"
				+ "INNER JOIN land_bank.tahasil_master t ON la.tehsil_code = t.tahasil_code \r\n"
				+ "INNER JOIN land_bank.village_master m ON la.village_code = m.village_code \r\n"
				+ "INNER JOIN land_bank.khatian_information k ON la.khatian_code = k.khatian_code \r\n"
				+ "INNER JOIN land_application_approval a ON la.land_application_id = a.land_application_id \r\n"
				+ "WHERE a.pending_at_role_id = :roleId AND la.deleted_flag = '0' \r\n"
				+ "GROUP BY la.land_application_id, d.district_name, t.tahasil_name, m.village_name, k.khata_no, k.khatian_code, a.created_on , d.district_code, t.tahasil_code, m.village_code ORDER BY a.created_on DESC LIMIT :pageSize OFFSET :offset";

		int offset = (pageNumber - 1) * pageSize;

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = entityManager.createNativeQuery(query).setParameter("roleId", roleId.longValue())
				.setParameter("pageSize", pageSize).setParameter("offset", offset).getResultList();

		entityManager.close();
		return transformResultList(resultList);
	}

	private List<LandApplicantDTO> transformResultList(List<Object[]> resultList) {
		List<LandApplicantDTO> roleInfoList = new ArrayList<>();
		for (Object[] row : resultList) {
			BigInteger landApplicantId = (BigInteger) row[0];
			String applicantNo = (String) row[1];
			String applicantName = (String) row[2];
			String mobileNo = (String) row[3];
			String districtName = (String) row[4];
			String tehsilName = (String) row[5];
			String mouzaName = (String) row[6];
			String khataNo = (String) row[7];
			String khatianCode = (String) row[8];
			String extent = (String) row[9];
			String plotExtend = (String) row[10];
			String plotNo = (String) row[11];

			Date createdOn = (Date) row[12];
			String districtCode = (String) row[13];
			String tahasilCode = (String) row[14];
			String villageCode = (String) row[15];

			LandApplicantDTO roleInfo = new LandApplicantDTO();
			roleInfo.setLandApplicantId(landApplicantId);
			roleInfo.setApplicantNo(applicantNo);
			roleInfo.setApplicantName(applicantName);
			roleInfo.setMobileNo(mobileNo);
			roleInfo.setDistrictName(districtName);
			roleInfo.setTehsilName(tehsilName);
			roleInfo.setMouzaName(mouzaName);
			roleInfo.setKhataNo(khataNo);
			roleInfo.setKhatianCode(khatianCode);
			roleInfo.setExtent(extent);
			roleInfo.setLandApplicantId(landApplicantId);
			roleInfo.setPlotExtend(plotExtend);
			roleInfo.setPlotNo(plotNo);
			roleInfo.setCreatedOn(createdOn);
			roleInfo.setDistrictCode(districtCode);
			roleInfo.setTahasilCode(tahasilCode);
			roleInfo.setVillageCode(villageCode);

			roleInfoList.add(roleInfo);

		}

		return roleInfoList;
	}

	@Transactional
	public BigInteger getTotalApplicantCount(BigInteger roleId, String districtCode, String tahasilCode,
			String villageCode, String khatianCode) {
		String countQuery = "SELECT count(*) FROM public.land_application la \r\n"
				+ "INNER JOIN land_bank.district_master d ON la.district_code = d.district_code \r\n"
				+ "INNER JOIN land_bank.tahasil_master t ON la.tehsil_code = t.tahasil_code \r\n"
				+ "INNER JOIN land_bank.village_master m ON la.village_code = m.village_code \r\n"
				+ "INNER JOIN land_bank.khatian_information k ON la.khatian_code = k.khatian_code \r\n"
				+ "INNER JOIN land_application_approval a ON la.land_application_id = a.land_application_id \r\n"
				+ "WHERE a.pending_at_role_id=:roleId AND la.deleted_flag = '0'";

		BigInteger status = (BigInteger) entityManager.createNativeQuery(countQuery).setParameter("roleId", roleId)
				.getSingleResult();

		entityManager.close();
		return status;
	}

	@Transactional
	public BigInteger getTotalApplicantCountPagination(BigInteger roleId, String districtCode, String tahasilCode,
			String villageCode, String khatianCode) {
		String countQuery = "SELECT count(*) FROM public.land_application la \r\n"
				+ "INNER JOIN land_bank.district_master d ON la.district_code = d.district_code \r\n"
				+ "INNER JOIN land_bank.tahasil_master t ON la.tehsil_code = t.tahasil_code \r\n"
				+ "INNER JOIN land_bank.village_master m ON la.village_code = m.village_code \r\n"
				+ "INNER JOIN land_bank.khatian_information k ON la.khatian_code = k.khatian_code \r\n"
				+ "INNER JOIN land_application_approval a ON la.land_application_id = a.land_application_id \r\n"
				+ "WHERE a.pending_at_role_id = :roleId AND la.deleted_flag = '0' AND "
				+ "(d.district_code = :districtCode OR :districtCode IS NULL) AND "
				+ "(t.tahasil_code = :tahasilCode OR :tahasilCode IS NULL) AND "
				+ "(m.village_code = :villageCode OR :villageCode IS NULL) AND "
				+ "(k.khatian_code = :khatianCode OR :khatianCode IS NULL)";

		BigInteger status = (BigInteger) entityManager.createNativeQuery(countQuery).setParameter("roleId", roleId)
				.setParameter("districtCode", districtCode).setParameter("tahasilCode", tahasilCode)
				.setParameter("villageCode", villageCode).setParameter("khatianCode", khatianCode).getSingleResult();

		entityManager.close();
		return status;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<LandApplicantDTO> getSearchLandApplicantDetailsPage(BigInteger roleId, String districtCode,
			int pageNumber, int pageSize, String tahasilCode, String villageCode, String khatianCode) {
		log.info(districtCode + " " + tahasilCode + " " + villageCode + " " + khatianCode);

		String query = "SELECT la.land_application_id, la.application_no, la.applicant_name, la.mobile_no, d.district_name, t.tahasil_name, m.village_name, k.khata_no , k.khatian_code ,(select CAST(st_extent(ST_Transform(spb.geom, 3857))AS character varying) as extent from public.land_schedule ls join land_bank.plot_information pi on( ls.plot_code=pi.plot_code ) \r\n"
				+ "join land_bank.sjta_plot_boundary spb on( spb.plot_code=pi.plot_code ) where ls.land_application_id=la.land_application_id), CAST((SELECT st_extent(ST_Transform(geom,3857)) as extent FROM land_bank.sjta_plot_boundary_view where plot_code in (SELECT STRING_AGG(ls.plot_code, ',') AS plot_code FROM public.land_schedule ls WHERE ls.land_application_id = la.land_application_id AND ls.deleted_flag='0' ) group by village_code)as varchar) as plot_extend , CAST((SELECT STRING_AGG((select plot_no from land_bank.plot_information where plot_code=ls.plot_code), ',') AS plot_no FROM public.land_schedule ls WHERE ls.land_application_id = la.land_application_id AND deleted_flag='0' )as varchar) as plot_no, a.created_on, d.district_code, t.tahasil_code, m.village_code FROM public.land_application la INNER JOIN land_bank.district_master d ON la.district_code = d.district_code \r\n"
				+ "INNER JOIN land_bank.tahasil_master t ON la.tehsil_code = t.tahasil_code \r\n"
				+ "INNER JOIN land_bank.village_master m ON la.village_code = m.village_code \r\n"
				+ "INNER JOIN land_bank.khatian_information k ON la.khatian_code = k.khatian_code \r\n"
				+ "INNER JOIN land_application_approval a ON la.land_application_id = a.land_application_id \r\n"
				+ "WHERE a.pending_at_role_id=:roleId AND d.district_code =:districtCode ";

		if (!tahasilCode.equals("0")) {
			query += "AND t.tahasil_code =:tahasilCode ";
		}

		if (!villageCode.equals("0")) {
			query += "AND m.village_code =:villageCode ";
		}

		if (!khatianCode.equals("0")) {
			query += "AND k.khatian_code =:khatianCode ";
		}

		query += "AND la.deleted_flag = '0' ORDER BY a.created_on, la.land_application_id DESC LIMIT :pageSize OFFSET :offset";

		Integer offset = (pageNumber - 1) * pageSize;

		@SuppressWarnings("unchecked")
		Query resultList = entityManager.createNativeQuery(query).setParameter("roleId", roleId)
				.setParameter("districtCode", districtCode).setParameter("pageSize", pageSize)
				.setParameter("offset", offset);

		if (!tahasilCode.equals("0")) {
			resultList.setParameter("tahasilCode", tahasilCode);
		}

		if (!villageCode.equals("0")) {
			resultList.setParameter("villageCode", villageCode);
		}

		if (!khatianCode.equals("0")) {
			resultList.setParameter("khatianCode", khatianCode);
		}

		return transformResultList(resultList.getResultList());
	}

	@Transactional

	public Land_applicant findByLandApplicantId(Integer landApplicantId) {
		String query = "SELECT  la.applicant_no, la.applicant_name, la.father_name, la.mobile_no,"
				+ " la.email_address," + "  la.doc_ref_no, la.docs_path, "
				+ " la.doc_name, ld.docs_path AS documents_path,  lp.total_area,"
				+ " lp.purchase_area, mb.block_name, md.district_name,   "
				+ " mdt.doc_name AS documents_name, mgp.gp_name, mkn.khata_no,  "
				+ " mm.mouza_name, mpn.plot_no, ms.state_name,   mt.tehsil_name  " + "FROM public.land_applicant la"
				+ " LEFT JOIN public.land_documents ld ON la.land_applicant_id = ld.land_applicant_id"
				+ " LEFT JOIN public.land_plot lp ON la.land_applicant_id = lp.land_applicant_id"
				+ " LEFT JOIN public.m_block mb ON la.curr_block = mb.block_id"
				+ " LEFT JOIN public.m_district md ON la.curr_district = md.district_id"
				+ " LEFT JOIN public.m_document_type mdt ON la.doc_type = mdt.doc_type_id"
				+ " LEFT JOIN public.m_gp mgp ON la.curr_gp = mgp.gp_id"
				+ " LEFT JOIN public.m_khata_no mkn ON la.plot_khata_no_id = mkn.khata_no_id"
				+ " LEFT JOIN public.m_mouza mm ON la.plot_mouza_id = mm.mouza_id"
				+ " LEFT JOIN public.m_plot_no mpn ON lp.plot_no_id = mpn.plot_no_id"
				+ " LEFT JOIN public.m_state ms ON la.curr_state = ms.state_id"
				+ " LEFT JOIN public.m_tehsil mt ON la.curr_district = mt.district_id AND la.curr_block = mt.tehsil_id"
				+ " WHERE la.land_applicant_id =:landApplicantId";

		try {
			Object[] result = (Object[]) entityManager.createNativeQuery(query)
					.setParameter("landApplicantId", landApplicantId).getSingleResult();

			Land_applicant land = new Land_applicant();
			land.setApplicantNo((String) result[0]);
			land.setTxtApplicantName((String) result[1]);
			land.setTxtFatherHusbandName((String) result[2]);
			land.setTxtMobileNo((String) result[3]);
			land.setTxtEmail((String) result[4]);
			land.setTxtDocumentNo((String) result[5]);
			land.setFileUploadDocument((String) result[6]);

			entityManager.close();
			return land;
		} catch (NoResultException e) {
			return null;
		}
	}

	// get data in prasant
	@Transactional
	public List<LandApplicantDTO> getLandApplicantDetailsUser(BigInteger createdBy, int pageNumber, int pageSize) {

		String query = "SELECT la.applicant_no, la.applicant_name, d.district_name, t.tehsil_name, m.mouza_name, k.khata_no, p.plot_no, la.created_on, md.doc_name "
				+ "FROM land_applicant la LEFT JOIN land_plot lp ON la.land_applicant_id = lp.land_applicant_id "
				+ "LEFT JOIN m_district d ON la.curr_district = d.district_id "
				+ "LEFT JOIN m_tehsil t ON la.curr_district = t.district_id AND la.curr_block = t.tehsil_id "
				+ "LEFT JOIN m_mouza m ON la.plot_mouza_id = m.mouza_id "
				+ "LEFT JOIN m_khata_no k ON la.plot_khata_no_id = k.khata_no_id "
				+ "LEFT JOIN m_plot_no p ON lp.plot_no_id = p.plot_no_id "
				+ "LEFT JOIN m_document_type md ON la.doc_type = md.doc_type_id "
				+ "WHERE la.created_by=:createdBy AND la.deleted_flag = '0'  " + "ORDER BY la.applicant_no "
				+ "LIMIT :pageSize OFFSET :offset ";

		int offset = (pageNumber - 1) * pageSize;

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = entityManager.createNativeQuery(query).setParameter("pageSize", pageSize)
				.setParameter("offset", offset).setParameter("createdBy", createdBy).getResultList();

		entityManager.close();
		return transformResultListRET(resultList);
	}

	private List<LandApplicantDTO> transformResultListRET(List<Object[]> resultList) {
		List<LandApplicantDTO> roleInfoList = new ArrayList<>();
		for (Object[] row : resultList) {
			String applicantNo = (String) row[0];
			String applicantName = (String) row[1];
			String districtName = (String) row[2];
			String tehsilName = (String) row[3];
			String mouzaName = (String) row[4];
			String khataNo = (String) row[5];
			String plotNo = (String) row[6];
			Date createdOn = (Date) row[7];
			String docName = (String) row[8];

			LandApplicantDTO roleInfo = new LandApplicantDTO();
			roleInfo.setApplicantNo(applicantNo);
			roleInfo.setApplicantName(applicantName);
			roleInfo.setDistrictName(districtName);
			roleInfo.setTehsilName(tehsilName);
			roleInfo.setMouzaName(mouzaName);
			roleInfo.setKhataNo(khataNo);
			roleInfo.setPlotNo(plotNo);
			roleInfo.setCreatedOn(createdOn);
			roleInfo.setDocName(docName);

			roleInfoList.add(roleInfo);

		}

		return roleInfoList;
	}

	@SuppressWarnings("unused")
	public LandAppResponseStructureDTO getCombinedDataForApplicant(BigInteger applicantId) {

		log.info(" :: getCombinedDataForApplicant() method Execution Are Start ..!!");

		String nativeQuery = "SELECT la.land_application_id, la.applicant_name, la.father_name, la.mobile_no, la.email_address, CAST((select count(*) from public.land_documents where land_application_id=la.land_application_id and deleted_flag='0' ) as smallint)as land_count, la.doc_ref_no, la.docs_path, (select state_name from public.m_state where CAST(state_id as varchar)=la.curr_state limit 1)as state_name , la.curr_district, la.curr_block, la.curr_gp, la.curr_village, la.curr_police , la.curr_post_office, la.curr_address_line_1, la.curr_address_line_2, la.curr_pin_code, la.pre_state, la.pre_district, la.pre_block, la.pre_gp, la.pre_village, la.pre_police , la.pre_post_office, la.per_address_line_1, la.per_address_line_2, la.pre_pin_code, la.save_status, la.application_no, la.deleted_flag, (select district_name from land_bank.district_master \r\n"
				+ "where district_code=la.district_code limit 1) as district_name, (select tahasil_name from land_bank.tahasil_master \r\n"
				+ "where tahasil_code=la.tehsil_code limit 1) as tahasil_name, (select village_name from land_bank.village_master where village_code=la.village_code limit 1) as village_name, la.khatian_code, laa.pending_at_role_id, ki.khata_no as land_khata,mdt.doc_name, la.district_code, la.tehsil_code, la.village_code \r\n"
				+ "FROM public.land_application la INNER JOIN public.land_application_approval laa ON la.land_application_id = laa.land_application_id \r\n"
				+ "INNER JOIN m_document_type mdt ON mdt.doc_type_id = la.doc_type \r\n"
				+ "INNER JOIN land_bank.khatian_information ki ON ki.khatian_code = la.khatian_code \r\n"
				+ "where la.land_application_id = :landapplicantid AND la.deleted_flag='0'";

		String nativeQuery1 = "SELECT CAST(ls.total_area as varchar) as total_area, CAST(ls.purchase_area as varchar ) as purchase_area, ls.plot_code, pi.plot_no, pi.kissam ,ki.khata_no, CAST((SELECT st_extent(ST_Transform(geom,3857)) FROM land_bank.sjta_plot_boundary \r\n"
				+ "where plot_code in (ls.plot_code) group by village_code limit 1)as varchar) as extend, apli.inspection_date, apli.tahasildar_inspection_date, apli.approve_status, apli.tahasil_status, apli.plot_land_inspection_id, (SELECT COUNT(*) FROM application.land_inspection_application WHERE plot_land_inspection_id = apli.plot_land_inspection_id AND land_application_id = :landapplicantid) as verifyCount \r\n"
				+ "FROM land_schedule as ls \r\n"
				+ "INNER JOIN land_bank.plot_information as pi ON ls.plot_code = pi.plot_code \r\n"
				+ "INNER JOIN land_bank.khatian_information as ki ON pi.khatian_code = ki.khatian_code \r\n"
				+ "LEFT JOIN application.plot_land_inspection as apli ON ls.plot_code = apli.plot_code "
				+ "WHERE ls.land_application_id = :landapplicantid AND ls.deleted_flag='0'";

		String nativeQuery2 = "SELECT m.doc_name, ld.docs_path ,m.doc_type_id, ld.document_no  "
				+ " FROM public.land_documents ld \r\n"
				+ "INNER JOIN public.m_document_type m ON ld.document_name = m.doc_type_id \r\n"
				+ "where ld.land_application_id = :landapplicantid " + " AND ld.deleted_flag='0' ";

		@SuppressWarnings("unchecked")
		List<Object[]> queryResults = entityManager.createNativeQuery(nativeQuery)
				.setParameter("landapplicantid", applicantId).getResultList();

		@SuppressWarnings("unchecked")
		List<Object[]> queryResults1 = entityManager.createNativeQuery(nativeQuery1)
				.setParameter("landapplicantid", applicantId).getResultList();

		@SuppressWarnings("unchecked")
		List<Object[]> queryResults2 = entityManager.createNativeQuery(nativeQuery2)
				.setParameter("landapplicantid", applicantId).getResultList();

		log.info(" :: queryResults Return  ..!!");

		LandApplicantViewDTO applicantDTO = mapToApplicantDTO(queryResults);
		List<LandPlotViewDTO> plotDTOList = mapToPlotDTO(queryResults1);
		List<LandAppViewDocumentDTO> documentDTOList = mapToDocumentDTO(queryResults2);

		List<String> landDetails = new ArrayList<>();
		List<LandPlotViewDTO> getLand = plotDTOList;
		for (LandPlotViewDTO plotId : getLand) {
			landDetails.add(plotId.getPlotCode());
		}
		StringBuilder queryBuilder = new StringBuilder(
				"SELECT village_code, (CAST(st_extent(ST_Transform(geom,3857)) as varchar)) as extent "
						+ "FROM land_bank.sjta_plot_boundary " + "WHERE plot_code IN (");

		for (String plotNoId : landDetails) {
			queryBuilder.append("'");
			queryBuilder.append(plotNoId);
			queryBuilder.append("', ");
		}

		if (!landDetails.isEmpty()) {
			queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
		}

		queryBuilder.append(") GROUP BY village_code;");
		String sqlQueryExtend = queryBuilder.toString();
		String villageCode = null;
		String villageCodeExtend = null;
		Object[] queryResultsExtend = null;
		try {
			queryResultsExtend = (Object[]) entityManager.createNativeQuery(sqlQueryExtend).getSingleResult();
		} catch (NoResultException e) {

		}

		if (queryResultsExtend != null) {
			LandAppResponseStructureDTO dtoPlotExt = new LandAppResponseStructureDTO();
			villageCode = (String) queryResultsExtend[0];
			villageCodeExtend = (String) queryResultsExtend[1];
			dtoPlotExt.setVillageCodeextend(villageCode);
			dtoPlotExt.setVillageMergeExtend(villageCodeExtend);
		}
		LandAppResponseStructureDTO response = new LandAppResponseStructureDTO(applicantDTO, plotDTOList,
				documentDTOList, villageCode, villageCodeExtend);

		entityManager.close();
		if (response != null) {
			log.info(" :: LandAppResponseStructureDTO Return  ..!!");
			return response;
		} else
			log.info(" :: LandAppResponseStructureDTO Return Null  ..!!");
		return null;
	}

	private LandApplicantViewDTO mapToApplicantDTO(List<Object[]> queryResults) {
		if (queryResults.isEmpty()) {
			log.info(" :: LandApplicantViewDTO Return Null  ..!!");
			return null;
		}
		log.info(" :: LandApplicantViewDTO Return Result And Add to a DTO  ..!!");
		Object[] row = queryResults.get(0);
		LandApplicantViewDTO applicantDTO = new LandApplicantViewDTO();
		applicantDTO.setLandApplicantId((BigInteger) row[0]);
		applicantDTO.setApplicantNo((String) row[29]);
		applicantDTO.setApplicantName((String) row[1]);
		applicantDTO.setFatherName((String) row[2]);
		applicantDTO.setMobileNo((String) row[3]);
		applicantDTO.setEmailAddress((String) row[4]);
		applicantDTO.setDocType((Short) row[5]);
		applicantDTO.setDocRefNo((String) row[6]);
		applicantDTO.setDocsPath((String) row[7]);
		applicantDTO.setCurrStateId((String) row[8]);
		applicantDTO.setCurrDistrictId((String) row[9]);
		applicantDTO.setCurrBlockId((String) row[10]);
		applicantDTO.setCurrGpId((String) row[11]);
		applicantDTO.setCurrVillageId((String) row[12]);
		applicantDTO.setCurrPoliceStation((String) row[13]);
		applicantDTO.setCurrPostOffice((String) row[14]);
		applicantDTO.setCurrStreetNo((String) row[15]);
		applicantDTO.setCurrHouseNo((String) row[16]);
		applicantDTO.setCurrPinCode((String) row[17]);
		applicantDTO.setPlotDistrictId((String) row[31]);
		applicantDTO.setPlotTehsilId((String) row[32]);
		applicantDTO.setPlotMouzaId((String) row[33]);
		applicantDTO.setPlotKhataNoId((String) row[34]);
		applicantDTO.setPlotKhataNo((String) row[36]);
		applicantDTO.setPendingRoleId((Short) row[35]);
		applicantDTO.setDocName((String) row[37]);
		applicantDTO.setPlotDistrict((String) row[38]);
		applicantDTO.setPlotTehsil((String) row[39]);
		applicantDTO.setPlotMouza((String) row[40]);

		log.info(" :: LandApplicantViewDTO Return Result And Add to a DTO and return the result ..!!");
		return applicantDTO;
	}

	private List<LandPlotViewDTO> mapToPlotDTO(List<Object[]> queryResults) {
		List<LandPlotViewDTO> plotDTOList = new ArrayList<>();

		log.info(" :: LandPlotViewDTO Return Result And Add to a DTO  ..!!");
		for (Object[] row : queryResults) {
			LandPlotViewDTO plotDTO = new LandPlotViewDTO();
			plotDTO.setPlotNoId((String) row[3]);
			plotDTO.setTotalArea((String) row[0]);
			plotDTO.setVarietiesId((String) row[4]);
			plotDTO.setPurchaseArea((String) row[1]);
			plotDTO.setPlotCode((String) row[2]);
			plotDTO.setKhataNo((String) row[5]);
			plotDTO.setCoInspectionDate((Date) row[7]);
			plotDTO.setTahasildarInspectionDate((Date) row[8]);
			plotDTO.setCoStatus((Short) row[9]);
			plotDTO.setTahasilStatus((Short) row[10]);
			plotDTO.setPlotLandInspectionId((Integer) row[11]);
			plotDTO.setIsVerified(row[12].toString());
			plotDTOList.add(plotDTO);
		}
		log.info(" :: LandPlotViewDTO Return Result And Add to a DTO also return   ..!!");
		return plotDTOList;
	}

	private List<LandAppViewDocumentDTO> mapToDocumentDTO(List<Object[]> queryResults) {
		List<LandAppViewDocumentDTO> documentDTOList = new ArrayList<>();
		log.info(" :: LandAppViewDocumentDTO Return Result And Add to a DTO    ..!!");

		for (Object[] row : queryResults) {
			LandAppViewDocumentDTO documentDTO = new LandAppViewDocumentDTO();
			documentDTO.setDocumentName((String) row[0]);
			documentDTO.setDocsPath((String) row[1]);
			documentDTO.setDocsId((Short) row[2]);
			if (row[3] != null) {
				documentDTO.setDocumentNumber((String) row[3]);
			}
			documentDTOList.add(documentDTO);
		}
		log.info(" :: LandAppViewDocumentDTO Return Result And Add to a DTO also return   ..!!");
		return documentDTOList;
	}

	@Transactional
	public Integer saveLandApplicantRecord(Land_applicant land) {
		log.info("ischecked: " + land.getIsChecked());

		String sqlQuery = "INSERT INTO public.land_application ( "
				+ " applicant_name, father_name, mobile_no, email_address, doc_type, doc_ref_no,"
				+ " docs_path, curr_state, curr_district, curr_block, curr_gp,"
				+ " curr_village, curr_police, curr_post_office, curr_address_line_1,"
				+ " curr_address_line_2, curr_pin_code, pre_state, pre_district, pre_block,"
				+ " pre_gp, pre_village, created_by, pre_police,"
				+ " pre_post_office, per_address_line_1, per_address_line_2, pre_pin_code,"
				+ " save_status, application_no , " + " app_status , is_curr_addr_same_per_addr " + "     ) VALUES ("
				+ " :applicantName, :fatherName, :mobileNo, :emailAddress, :docType, :docRefNo,"
				+ " :docsPath, :currStateId, :currDistrictId, :currBlockId, :currGpId,"
				+ " :currVillageId, :currPoliceStation, :currPostOffice, :currStreetNo,"
				+ " :currHouseNo, :currPinCode, :preStateId, :preDistrictId, :preBlockId,"
				+ " :preGpId, :preVillageId, :createdBy,  :prePoliceStation,"
				+ " :prePostOffice, :preStreetNo, :preHouseNo, :prePinCode,"
				+ " :saveStatus, :applicantNo ,:appStatus, :isChecked " + "      ) "
				+ " RETURNING land_application_id ";

		Query query = entityManager.createNativeQuery(sqlQuery)

				.setParameter("applicantName", land.getTxtApplicantName())
				.setParameter("fatherName", land.getTxtFatherHusbandName())
				.setParameter("mobileNo", land.getTxtMobileNo()).setParameter("emailAddress", land.getTxtEmail())
				.setParameter("docType", land.getSelDocumentType()).setParameter("docRefNo", land.getTxtDocumentNo())
				.setParameter("docsPath", land.getFileUploadDocument()).setParameter("currStateId", land.getSelState())
				.setParameter("currDistrictId", land.getSelDistrict())
				.setParameter("currBlockId", land.getSelBlockULB())
				.setParameter("currVillageId", land.getSelVillageLocalAreaName())
				.setParameter("currGpId", land.getSelGPWardNo()).setParameter("preStateId", land.getSelState17())
				.setParameter("preDistrictId", land.getSelDistrict18())
				.setParameter("preBlockId", land.getSelBlockULB19()).setParameter("preGpId", land.getSelGPWARDNumber())
				.setParameter("preVillageId", land.getSelVillageLocalAreaName21())
				.setParameter("createdBy", land.getIntCreatedBy())
				.setParameter("currPoliceStation", land.getTxtPoliceStation())
				.setParameter("currPostOffice", land.getTxtPostOffice())
				.setParameter("currStreetNo", land.getTxtHabitationStreetNoLandmark())
				.setParameter("currHouseNo", land.getTxtHouseNo()).setParameter("currPinCode", land.getTxtPinCode())
				.setParameter("prePoliceStation", land.getTxtPoliceStation22())
				.setParameter("prePostOffice", land.getTxtPostOffice23())
				.setParameter("preStreetNo", land.getTxtHabitationStreetNoLandmark24())
				.setParameter("preHouseNo", land.getTxtHouseNo25()).setParameter("prePinCode", land.getTxtPinCode26())
				.setParameter("saveStatus", land.getSaveStatus()).setParameter("applicantNo", land.getApplicantNo())
				.setParameter("appStatus", land.getAppStage())
				.setParameter("isChecked", land.getIsChecked().shortValue());

		BigInteger result = (BigInteger) query.getSingleResult();

		entityManager.close();
		return result.intValue();
	}

	@Transactional
	public Integer insertLandDocument(String docsPath, Short documentName, Integer createdBy, Integer landApplicantId,
			String documentNumber) {

		String sqlQuery = "INSERT INTO land_documents (docs_path, document_name, "
				+ "created_by, land_application_id, document_no ) " + "VALUES (?, ?, ?, ?, ?)";

		Integer status = entityManager.createNativeQuery(sqlQuery).setParameter(1, docsPath)
				.setParameter(2, documentName).setParameter(3, createdBy).setParameter(4, landApplicantId)
				.setParameter(5, documentNumber).executeUpdate();

		entityManager.close();
		return status;
	}

	@Transactional
	public Integer deleteLandDocument(Integer landApplicantId) {

		String queryString = "update public.land_documents set deleted_flag = '1' where land_application_id = :applicantId";
		Integer status = entityManager.createNativeQuery(queryString).setParameter("applicantId", landApplicantId)
				.executeUpdate();

		entityManager.close();
		return status;
	}

	// update the applicant record
	@Transactional
	public Land_applicant updateLandApplicant(Land_applicant dto) {
		Short shortValue = null;
		if (dto.getIsChecked() == null) {
			shortValue = 0;
		} else {
			shortValue = dto.getIsChecked();
		}
		String nativeQuery = "UPDATE public.land_application SET docs_path = :docsPath, updated_by = :updatedBy, save_status = :saveStatus, curr_block = :currBlockId, pre_block = :preBlockId, curr_district = :currDistrictId, pre_district = :preDistrictId, doc_type = :docType, pre_gp = :preGpId, curr_gp = :currGpId, curr_state = :currStateId, pre_state = :preStateId, curr_village = :currVillageId, pre_village = :preVillageId, applicant_name = :applicantName, email_address = :emailAddress, father_name = :fatherName, curr_address_line_1 = :currStreetNo, per_address_line_1 = :preStreetNo, curr_address_line_2 = :currHouseNo, per_address_line_2 = :preHouseNo, mobile_no = :mobileNo, curr_pin_code = :currPinCode, pre_pin_code = :prePinCode, curr_police = :currPoliceStation, pre_police = :prePoliceStation, curr_post_office = :currPostOffice, pre_post_office = :prePostOffice, is_curr_addr_same_per_addr = :isChecked WHERE land_application_id = :landApplicantId";
		entityManager.createNativeQuery(nativeQuery).setParameter("docsPath", dto.getFileUploadDocument())
				.setParameter("updatedBy", dto.getIntCreatedBy()).setParameter("saveStatus", dto.getSaveStatus())
				.setParameter("currBlockId", dto.getSelBlockULB()).setParameter("preBlockId", dto.getSelBlockULB19())
				.setParameter("currDistrictId", dto.getSelDistrict())
				.setParameter("preDistrictId", dto.getSelDistrict18()).setParameter("docType", dto.getSelDocumentType())
				.setParameter("preGpId", dto.getSelGPWARDNumber()).setParameter("currGpId", dto.getSelGPWardNo())
				.setParameter("currStateId", dto.getSelState()).setParameter("preStateId", dto.getSelState17())
				.setParameter("currVillageId", dto.getSelVillageLocalAreaName())
				.setParameter("preVillageId", dto.getSelVillageLocalAreaName21())
				.setParameter("applicantName", dto.getTxtApplicantName())
				.setParameter("emailAddress", dto.getTxtEmail())
				.setParameter("fatherName", dto.getTxtFatherHusbandName())
				.setParameter("currStreetNo", dto.getTxtHabitationStreetNoLandmark())
				.setParameter("preStreetNo", dto.getTxtHabitationStreetNoLandmark24())
				.setParameter("currHouseNo", dto.getTxtHouseNo()).setParameter("preHouseNo", dto.getTxtHouseNo25())
				.setParameter("mobileNo", dto.getTxtMobileNo()).setParameter("currPinCode", dto.getTxtPinCode())
				.setParameter("prePinCode", dto.getTxtPinCode26())
				.setParameter("currPoliceStation", dto.getTxtPoliceStation())
				.setParameter("prePoliceStation", dto.getTxtPoliceStation22())
				.setParameter("currPostOffice", dto.getTxtPostOffice())
				.setParameter("prePostOffice", dto.getTxtPostOffice23()).setParameter("isChecked", shortValue)
				.setParameter("landApplicantId", dto.getIntId()).executeUpdate();
		entityManager.close();
		return dto;
	}

	@Transactional
	public String getAppStatusForLandApplicationId(Integer landApplicationId) {
		String sqlQuery = "SELECT app_status FROM public.land_application WHERE land_application_id = :landApplicationId";
		Query query = entityManager.createNativeQuery(sqlQuery).setParameter("landApplicationId", landApplicationId);

		Object result = query.getSingleResult();

		if (result != null) {
			return result.toString();
		} else {
			return null;
		}
	}

	@Transactional
	public Integer updateAppStatusForLandApplicationId(Integer landApplicationId, Short newAppStatus) {
		String sqlQuery = "UPDATE public.land_application " + " SET app_status = :newAppStatus "
				+ " WHERE land_application_id = :landApplicationId";

		return entityManager.createNativeQuery(sqlQuery).setParameter("newAppStatus", newAppStatus)
				.setParameter("landApplicationId", landApplicationId).executeUpdate();
	}

	@Transactional
	public Integer getResubmitApproval(Integer landappid) {
		String updateLandApplicationApprovalQuery = "UPDATE public.land_application_approval SET pending_at_role_id = 3,"
				+ "application_status_id = 18, approval_action_id = 0, approval_level = 1 WHERE land_application_id = :landappid";
		return entityManager.createNativeQuery(updateLandApplicationApprovalQuery).setParameter("landappid", landappid)
				.executeUpdate();

	}

}