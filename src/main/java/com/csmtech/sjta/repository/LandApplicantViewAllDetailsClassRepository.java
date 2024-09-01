package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.LandAppResponseStructureDTO;
import com.csmtech.sjta.dto.LandAppViewDocumentDTO;
import com.csmtech.sjta.dto.LandApplicantViewDTO;
import com.csmtech.sjta.dto.LandPlotViewDTO;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class LandApplicantViewAllDetailsClassRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entityManager;

	@Value("${file.path}")
	private String filePathloc;

	@SuppressWarnings("unused")
	public LandAppResponseStructureDTO getCombinedDataForApplicant(BigInteger applicantId) {

		log.info(" :: getCombinedDataForApplicant() method Execution Are Start ..!!");

		String nativeQuery = "select landapp.applicant_name , landapp. father_name , landapp. mobile_no , landapp. email_address , (select doc_name from m_document_type \r\n"
				+ "where doc_type_id= landapp.doc_type LIMIT 1) as doc_type,landapp.doc_ref_no, (select state_name from m_state \r\n"
				+ "where curr_state_id= landapp.curr_state_id LIMIT 1) as curr_state_id , (select district_name from m_district \r\n"
				+ "where curr_district_id= landapp.curr_district_id LIMIT 1) as curr_district_id , (select block_name from m_block \r\n"
				+ "where curr_block_id= landapp.curr_block_id LIMIT 1) as curr_block_id , (select gp_name from m_gp where curr_gp_id= landapp.curr_gp_id LIMIT 1) as curr_gp_id , (select village_name from m_village_master \r\n"
				+ "where curr_village_id= landapp.curr_village_id LIMIT 1) as curr_village_id , (select police_station_name from public.m_police_station \r\n"
				+ "where police_station_id= landapp.curr_police_station LIMIT 1) as curr_police_station , landapp.curr_post_office , landapp.curr_street_no , landapp.curr_house_no , landapp.curr_pin_code , (select state_name from m_state \r\n"
				+ "where curr_state_id= landapp.pre_state_id LIMIT 1) as pre_state_id , (select district_name from m_district \r\n"
				+ "where curr_district_id= landapp.pre_district_id LIMIT 1) as pre_district_id , (select block_name from m_block \r\n"
				+ "where curr_block_id= landapp.pre_block_id LIMIT 1) as pre_block_id , (select gp_name from m_gp where curr_gp_id= landapp.pre_gp_id LIMIT 1) as pre_gp_id , (select gp_name from m_gp \r\n"
				+ "where curr_gp_id= landapp.pre_village_id LIMIT 1) as pre_village_id , (select police_station_name from public.m_police_station \r\n"
				+ "where police_station_id= landapp.pre_police_station LIMIT 1) as pre_police_station , landapp.pre_post_office , landapp.pre_street_no , landapp.pre_house_no , landapp.pre_pin_code , landapp.land_applicant_id , (select plot_no from m_plot_no \r\n"
				+ "where plot_no_id= landplot.plot_no_id LIMIT 1) as plot_no_id , landplot.total_area , landplot.purchase_area , (select varieties_name from m_varieties \r\n"
				+ "where varieties_id= landplot.varieties_id LIMIT 1) as varieties_id , (select doc_name from m_document_type \r\n"
				+ "where document_name= landdocs.document_name LIMIT 1) as document_name , landdocs.docs_path \r\n"
				+ "FROM land_applicant landapp \r\n"
				+ "LEFT JOIN land_plot landplot ON(landplot.land_applicant_id=landapp.land_applicant_id) \r\n"
				+ "LEFT JOIN land_documents landdocs ON(landplot.land_applicant_id=landdocs.land_applicant_id) WHERE landapp.land_applicant_id= :landapplicantid";

		@SuppressWarnings("unchecked")
		List<Object[]> queryResults = entityManager.createNativeQuery(nativeQuery)
				.setParameter("landapplicantid", applicantId).getResultList();
		log.info(" :: queryResults Return  ..!!");

		LandApplicantViewDTO applicantDTO = mapToApplicantDTO(queryResults);
		List<LandPlotViewDTO> plotDTOList = mapToPlotDTO(queryResults);
		List<LandAppViewDocumentDTO> documentDTOList = mapToDocumentDTO(queryResults);

		LandAppResponseStructureDTO response = new LandAppResponseStructureDTO(applicantDTO, plotDTOList,
				documentDTOList);

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
		applicantDTO.setApplicantName((String) row[0]);
		applicantDTO.setFatherName((String) row[1]);
		applicantDTO.setMobileNo((String) row[2]);
		applicantDTO.setEmailAddress((String) row[3]);
		applicantDTO.setDocType((Short) row[4]);
		applicantDTO.setDocRefNo((String) row[5]);
		applicantDTO.setCurrStateId((String) row[6]);
		applicantDTO.setCurrDistrictId((String) row[7]);
		applicantDTO.setCurrBlockId((String) row[8]);
		applicantDTO.setCurrGpId((String) row[9]);
		applicantDTO.setCurrVillageId((String) row[10]);
		applicantDTO.setCurrPoliceStation((String) row[11]);
		applicantDTO.setCurrPostOffice((String) row[12]);
		applicantDTO.setCurrStreetNo((String) row[13]);
		applicantDTO.setCurrHouseNo((String) row[14]);
		applicantDTO.setCurrPinCode((String) row[15]);
		applicantDTO.setPreStateId((String) row[16]);
		applicantDTO.setPreDistrictId((String) row[17]);
		applicantDTO.setPreBlockId((String) row[18]);
		applicantDTO.setPreGpId((String) row[19]);
		applicantDTO.setPreVillageId((String) row[20]);
		applicantDTO.setPrePoliceStation((String) row[21]);
		applicantDTO.setPrePostOffice((String) row[22]);
		applicantDTO.setPreStreetNo((String) row[23]);
		applicantDTO.setPreHouseNo((String) row[24]);
		applicantDTO.setPrePinCode((String) row[25]);
		applicantDTO.setLandApplicantId((BigInteger) row[26]);

		log.info(" :: LandApplicantViewDTO Return Result And Add to a DTO and return the result ..!!");
		return applicantDTO;
	}

	private List<LandPlotViewDTO> mapToPlotDTO(List<Object[]> queryResults) {
		List<LandPlotViewDTO> plotDTOList = new ArrayList<>();

		log.info(" :: LandPlotViewDTO Return Result And Add to a DTO  ..!!");
		for (Object[] row : queryResults) {
			LandPlotViewDTO plotDTO = new LandPlotViewDTO();
			plotDTO.setPlotNoId((String) row[27]);
			plotDTO.setTotalArea((String) row[28]);
			plotDTO.setPurchaseArea((String) row[29]);
			plotDTO.setVarietiesId((String) row[30]);

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
			documentDTO.setDocumentName((String) row[31]);
			String docsPath = filePathloc + "/" + (String) row[32];
			documentDTO.setDocsPath(docsPath);

			documentDTOList.add(documentDTO);
		}
		log.info(" :: LandAppViewDocumentDTO Return Result And Add to a DTO also return   ..!!");
		entityManager.close();
		return documentDTOList;
	}
}
