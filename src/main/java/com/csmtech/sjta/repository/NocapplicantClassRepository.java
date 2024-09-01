package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.ApplicantDTO;
import com.csmtech.sjta.dto.PlotDetailsSubDTO;
import com.csmtech.sjta.dto.savenocNocPlotDetaisRecxord;

@Repository
public class NocapplicantClassRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@Transactional
	public Integer saveNocApplicant(String applicantName, String fatherName, String mobileNo, String emailAddress,
			Integer docTypeId, String docRefNo, String docsPath, Integer currStateId, Integer currDistrictId,
			Integer currBlockId, Integer currGpId, Integer selCurrVillageLocalAreaName, String currPoliceStation,
			String currPostOffice, String currStreetNo, String currHouseNo, String currPinCode, Integer perStateId,
			Integer perDistrictId, Integer perBlockId, Integer perGpId, Integer selPerVillageLocalAreaName,
			String perPoliceStation, String perPostOffice, String perStreetNo, String perHouseNo, String perPinCode,
			Integer createdBy) {

		String insertQuery = "INSERT INTO public.noc_applicant "
				+ "(applicant_name, father_name, mobile_no, email_address,"
				+ " doc_type_id, doc_ref_no, docs_path, curr_state_id, curr_district_id, "
				+ "curr_block_id, curr_gp_id, curr_village_id, curr_police_station, curr_post_office,"
				+ " curr_street_no, curr_house_no, curr_pin_code, per_state_id, per_district_id, per_block_id, per_gp_id,"
				+ " per_village_id, per_police_station, per_post_office, per_street_no, per_house_no, per_pin_code, created_by ) "
				+ "VALUES (:applicantName, :fatherName, :mobileNo, :emailAddress,"
				+ " :docTypeId, :docRefNo, :docsPath, :currStateId, :currDistrictId, "
				+ ":currBlockId, :currGpId, :currVillageId, :currPoliceStation, :currPostOffice, :currStreetNo,"
				+ " :currHouseNo, :currPinCode, :perStateId, :perDistrictId, :perBlockId, :perGpId, "
				+ ":perVillageId, :perPoliceStation, :perPostOffice, :perStreetNo, :perHouseNo, :perPinCode, :createdBy )";

		return entity.createNativeQuery(insertQuery).setParameter("applicantName", applicantName)
				.setParameter("fatherName", fatherName).setParameter("mobileNo", mobileNo)
				.setParameter("emailAddress", emailAddress).setParameter("docTypeId", docTypeId)
				.setParameter("docRefNo", docRefNo).setParameter("docsPath", docsPath)
				.setParameter("currStateId", currStateId).setParameter("currDistrictId", currDistrictId)
				.setParameter("currBlockId", currBlockId).setParameter("currGpId", currGpId)
				.setParameter("currVillageId", selCurrVillageLocalAreaName)
				.setParameter("currPoliceStation", currPoliceStation).setParameter("currPostOffice", currPostOffice)
				.setParameter("currStreetNo", currStreetNo).setParameter("currHouseNo", currHouseNo)
				.setParameter("currPinCode", currPinCode).setParameter("perStateId", perStateId)
				.setParameter("perDistrictId", perDistrictId).setParameter("perBlockId", perBlockId)
				.setParameter("perGpId", perGpId).setParameter("perVillageId", selPerVillageLocalAreaName)
				.setParameter("perPoliceStation", perPoliceStation).setParameter("perPostOffice", perPostOffice)
				.setParameter("perStreetNo", perStreetNo).setParameter("perHouseNo", perHouseNo)
				.setParameter("perPinCode", perPinCode).setParameter("createdBy", createdBy).executeUpdate();
	}

	@Transactional
	public Integer saveNocApplicantUpdate(String updatedAppName, String updatedFatherName, String updatedMobileNo,
			String updatedEmailAddress, Integer updatedDocTypeId, String updatedDocRefNo, String updatedDocsPath,
			Integer updatedCurrStateId, Integer updatedCurrDistrictId, Integer updatedCurrBlockId,
			Integer updatedCurrGpId, Integer updatedCurrVillageId, String updatedCurrPoliceStation,
			String updatedCurrPostOffice, String updatedCurrStreetNo, String updatedCurrHouseNo,
			String updatedCurrPinCode, Integer updatedPerStateId, Integer updatedPerDistrictId,
			Integer updatedPerBlockId, Integer updatedPerGpId, Integer updatedPerVillageId,
			String updatedPerPoliceStation, String updatedPerPostOffice, String updatedPerStreetNo,
			String updatedPerHouseNo, String updatedPerPinCode, Integer updatedBy, Integer applicantIdToUpdate) {
		String updateQuery = "UPDATE public.noc_applicant SET applicant_name = :updatedAppName, \r\n"
				+ "father_name = :updatedFatherName, mobile_no = :updatedMobileNo, \r\n"
				+ "email_address = :updatedEmailAddress, doc_type_id = :updatedDocTypeId, \r\n"
				+ "doc_ref_no = :updatedDocRefNo,\r\n"
				+ "docs_path = :updatedDocsPath, curr_state_id = :updatedCurrStateId, \r\n"
				+ "curr_district_id = :updatedCurrDistrictId, curr_block_id = :updatedCurrBlockId, \r\n"
				+ "curr_gp_id = :updatedCurrGpId, curr_village_id = :updatedCurrVillageId, \r\n"
				+ "curr_police_station = :updatedCurrPoliceStation, curr_post_office = :updatedCurrPostOffice, \r\n"
				+ "curr_street_no = :updatedCurrStreetNo, curr_house_no = :updatedCurrHouseNo, \r\n"
				+ "curr_pin_code = :updatedCurrPinCode, per_state_id = :updatedPerStateId, \r\n"
				+ "per_district_id = :updatedPerDistrictId, per_block_id = :updatedPerBlockId, \r\n"
				+ "per_gp_id = :updatedPerGpId, per_village_id = :updatedPerVillageId, \r\n"
				+ "per_police_station = :updatedPerPoliceStation, per_post_office = :updatedPerPostOffice, per_street_no = :updatedPerStreetNo, per_house_no = :updatedPerHouseNo, \r\n"
				+ "per_pin_code = :updatedPerPinCode WHERE noc_applicant_id = :applicantIdToUpdate";

		return entity.createNativeQuery(updateQuery).setParameter("updatedAppName", updatedAppName)
				.setParameter("updatedFatherName", updatedFatherName).setParameter("updatedMobileNo", updatedMobileNo)
				.setParameter("updatedEmailAddress", updatedEmailAddress)
				.setParameter("updatedDocTypeId", updatedDocTypeId).setParameter("updatedDocRefNo", updatedDocRefNo)
				.setParameter("updatedDocsPath", updatedDocsPath).setParameter("updatedCurrStateId", updatedCurrStateId)
				.setParameter("updatedCurrDistrictId", updatedCurrDistrictId)
				.setParameter("updatedCurrBlockId", updatedCurrBlockId).setParameter("updatedCurrGpId", updatedCurrGpId)
				.setParameter("updatedCurrVillageId", updatedCurrVillageId)
				.setParameter("updatedCurrPoliceStation", updatedCurrPoliceStation)
				.setParameter("updatedCurrPostOffice", updatedCurrPostOffice)
				.setParameter("updatedCurrStreetNo", updatedCurrStreetNo)
				.setParameter("updatedCurrHouseNo", updatedCurrHouseNo)
				.setParameter("updatedCurrPinCode", updatedCurrPinCode)
				.setParameter("updatedPerStateId", updatedPerStateId)
				.setParameter("updatedPerDistrictId", updatedPerDistrictId)
				.setParameter("updatedPerBlockId", updatedPerBlockId).setParameter("updatedPerGpId", updatedPerGpId)
				.setParameter("updatedPerVillageId", updatedPerVillageId)
				.setParameter("updatedPerPoliceStation", updatedPerPoliceStation)
				.setParameter("updatedPerPostOffice", updatedPerPostOffice)
				.setParameter("updatedPerStreetNo", updatedPerStreetNo)
				.setParameter("updatedPerHouseNo", updatedPerHouseNo)
				.setParameter("updatedPerPinCode", updatedPerPinCode)
				.setParameter("applicantIdToUpdate", applicantIdToUpdate)

				.executeUpdate();
	}

	@Transactional
	public Integer saveNocPlot(savenocNocPlotDetaisRecxord nocPlot, String filePath) {

		return null;
	}

	@Transactional
	public Integer updateNocApplicantPlotDetails(savenocNocPlotDetaisRecxord dto) {
		String updateQuery = " UPDATE noc_applicant SET plot_district_id = :plotDistrictId, "
				+ " plot_tehsil_id = :plotTehsilId,plot_mouza_id = :plotMouzaId, "
				+ " plot_khata_no_id = :plotKhataNoId WHERE noc_applicant_id = :nocApplicantId";

		return entity.createNativeQuery(updateQuery).setParameter("plotDistrictId", dto.getSelDistrictName())
				.setParameter("plotTehsilId", dto.getSelTehsilName()).setParameter("plotMouzaId", dto.getSelMouza())
				.setParameter("plotKhataNoId", dto.getSelKhataNo())
				.setParameter("nocApplicantId", dto.getNocApplicantId()).executeUpdate();
	}

	@Transactional
	public Integer batchInsertNocPlots(savenocNocPlotDetaisRecxord nocPlot) {
		String insertQuery = "INSERT INTO noc_plot (plot_no_id , total_area, created_by , noc_applicant_id) "
				+ "VALUES (:plot_no_id, :total_area, :created_by, :noc_applicant_id)";

		int batchSize = 500;
		List<PlotDetailsSubDTO> plotDetailsList = nocPlot.getPlot_details();

		for (int i = 0; i < plotDetailsList.size(); i++) {
			PlotDetailsSubDTO plotDetails = plotDetailsList.get(i);
			entity.createNativeQuery(insertQuery).setParameter("plot_no_id", plotDetails.getPlot_id())
					.setParameter("total_area", plotDetails.getTotal_area())
					.setParameter("created_by", nocPlot.getCreatedBy())
					.setParameter("noc_applicant_id", nocPlot.getNocApplicantId()).executeUpdate();

			if (i % batchSize == 0) {
				entity.flush();
				entity.clear();
			}
		}
		return batchSize;
	}

	@Transactional
	public Integer saveNocDocument(String halPattaValue, String sabikPattaValue, String sabikHalComparValue,
			String setlementYadastValue, String registeredDeedValue, String fileDocumentaryProofofOccupancyifany,
			BigInteger nocAppId, BigInteger createdby) {

		String nativeQuery = "INSERT INTO public.noc_documents "
				+ "(hal_patta, sabik_patta, sabik_hal_compar, setlement_yadast, registered_deed,doc_proof, noc_applicant_id, created_by) "
				+ "VALUES (:halPatta, :sabikPatta, :sabikHalCompar, :setlementYadast, :registeredDeed,:needocs, :nocApplicantId, :createdBy)";

		Query query = entity.createNativeQuery(nativeQuery);

		query.setParameter("halPatta", halPattaValue);
		query.setParameter("sabikPatta", sabikPattaValue);
		query.setParameter("sabikHalCompar", sabikHalComparValue);
		query.setParameter("setlementYadast", setlementYadastValue);
		query.setParameter("registeredDeed", registeredDeedValue);
		query.setParameter("needocs", fileDocumentaryProofofOccupancyifany);
		query.setParameter("nocApplicantId", nocAppId);
		query.setParameter("createdBy", createdby);

		return query.executeUpdate();

	}

	@Transactional
	public ApplicantDTO getApplicantDetailsById(BigInteger id) {
		String nativeQuery = "SELECT applicant_name, father_name, mobile_no, email_address, doc_type_id, \r\n"
				+ "doc_ref_no, docs_path, curr_state_id, curr_district_id, curr_block_id, \r\n"
				+ "curr_gp_id, curr_village_id, curr_police_station, curr_post_office, \r\n"
				+ "curr_street_no, curr_house_no, curr_pin_code, per_state_id, per_district_id, \r\n"
				+ "per_block_id, per_gp_id, per_village_id, per_police_station, per_post_office, \r\n"
				+ "per_street_no, per_house_no, per_pin_code FROM noc_applicant WHERE noc_applicant_id = :id";

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = entity.createNativeQuery(nativeQuery).setParameter("id", id).getResultList();

		if (resultList.isEmpty()) {
			return null;
		}

		Object[] result = resultList.get(0);

		String applicantName = (String) result[0];
		String fatherName = (String) result[1];
		String mobileNo = (String) result[2];
		String emailAddress = (String) result[3];
		BigInteger docType = (BigInteger) result[4];
		String docRefNo = (String) result[5];
		String docsPath = (String) result[6];
		BigInteger currStateId = (BigInteger) result[7];
		BigInteger currDistrictId = (BigInteger) result[8];
		BigInteger currBlockId = (BigInteger) result[9];
		BigInteger currGpId = (BigInteger) result[10];
		BigInteger currVillageId = (BigInteger) result[11];
		String currPoliceStation = (String) result[12];
		String currPostOffice = (String) result[13];
		String currStreetNo = (String) result[14];
		String currHouseNo = (String) result[15];
		String currPinCode = (String) result[16];
		BigInteger perStateId = (BigInteger) result[17];
		BigInteger perDistrictId = (BigInteger) result[18];
		BigInteger perBlockId = (BigInteger) result[19];
		BigInteger perGpId = (BigInteger) result[20];
		BigInteger perVillageId = (BigInteger) result[21];
		String perPoliceStation = (String) result[22];
		String perPostOffice = (String) result[23];
		String perStreetNo = (String) result[24];
		String perHouseNo = (String) result[25];
		String perPinCode = (String) result[26];

		return new ApplicantDTO(applicantName, fatherName, mobileNo, emailAddress, docType, docRefNo, docsPath,
				currStateId, currDistrictId, currBlockId, currGpId, currVillageId, currPoliceStation, currPostOffice,
				currStreetNo, currHouseNo, currPinCode, perStateId, perDistrictId, perBlockId, perGpId, perVillageId,
				perPoliceStation, perPostOffice, perStreetNo, perHouseNo, perPinCode);
	}

	@Transactional
	public savenocNocPlotDetaisRecxord getApplicantPlotDetailsById(BigInteger applicantId) {
		String nativeQuery = "SELECT plot.plot_no_id, plot.total_area, m.plot_no, app.plot_district_id, app.plot_tehsil_id, app.plot_mouza_id, app.plot_khata_no_id \r\n"
				+ "FROM noc_plot plot \r\n"
				+ "JOIN noc_applicant app ON app.noc_applicant_id = plot.noc_applicant_id \r\n"
				+ "JOIN m_plot_no m ON m.plot_no_id = plot.plot_no_id \r\n"
				+ "WHERE plot.noc_applicant_id = :applicantId";

		@SuppressWarnings("unchecked")
		List<Object[]> results = entity.createNativeQuery(nativeQuery).setParameter("applicantId", applicantId)
				.getResultList();

		savenocNocPlotDetaisRecxord resultDTO = new savenocNocPlotDetaisRecxord();

		if (!results.isEmpty()) {
			resultDTO.setSelDistrictName((BigInteger) results.get(0)[3]);
			resultDTO.setSelTehsilName((BigInteger) results.get(0)[4]);
			resultDTO.setSelMouza((BigInteger) results.get(0)[5]);
			resultDTO.setSelKhataNo((BigInteger) results.get(0)[6]);

			List<PlotDetailsSubDTO> plotDetailsList = new ArrayList<>();
			for (Object[] row : results) {
				PlotDetailsSubDTO plotDetails = new PlotDetailsSubDTO();
				plotDetails.setPlot_id((BigInteger) row[0]);
				plotDetails.setTotal_area((String) row[1]);
				plotDetails.setPlot_name((String) row[2]);
				plotDetailsList.add(plotDetails);
			}
			resultDTO.setPlot_details(plotDetailsList);
		}

		return resultDTO;
	}

	public BigInteger getLastNocApplicantId() {
		String sqlQuery = "SELECT MAX(noc_applicant_id) AS last_noc_applicant_id FROM public.noc_applicant";
		Query query = entity.createNativeQuery(sqlQuery);

		BigInteger result = (BigInteger) query.getSingleResult();
		return result != null ? result : BigInteger.ZERO;
	}

	@Transactional
	public Integer batchUpdateDeleteNocPlots(savenocNocPlotDetaisRecxord nocPlot) {
		String insertQuery = "UPDATE public.noc_plot " + "           SET deleted_flag = '1' "
				+ "           WHERE plot_no_id = :potId";

		int batchSize = 50;
		List<PlotDetailsSubDTO> plotDetailsList = nocPlot.getPlot_details();

		for (int i = 0; i < plotDetailsList.size(); i++) {
			PlotDetailsSubDTO plotDetails = plotDetailsList.get(i);
			entity.createNativeQuery(insertQuery).setParameter("potId", plotDetails.getPlot_id()).executeUpdate();

			if (i % batchSize == 0) {
				entity.flush();
				entity.clear();
			}
		}
		return batchSize;
	}

}
