package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.Land_applicant;

public interface LandApplicantRepository extends JpaRepository<Land_applicant, Integer> {

	@Query("SELECT la FROM Land_applicant la WHERE la.bitDeletedFlag = '0'")
	List<Land_applicant> findAllNotDeleted();

	@Query("SELECT la FROM Land_applicant la WHERE la.txtMobileNo LIKE %?1% AND la.bitDeletedFlag = '0' ")
	List<Land_applicant> findByTxtMobileNoContainingAndBitDeletedFlagFalse(String mobileNo);

	@Query(value = "SELECT " + " la.land_applicant_id," + " la.applicant_no," + " la.applicant_name, "
			+ " la.doc_ref_no," + " la.docs_path," + " la.doc_name," + " lp.total_area," + "  lp.purchase_area,"
			+ " mb.block_name," + " md.district_name, " + " mdt.doc_name," + " mgp.gp_name," + " mkn.khata_no,"
			+ " mm.mouza_name," + " mpn.plot_no," + " ms.state_name," + " mt.tehsil_name "
			+ " FROM public.land_applicant la"
			+ " LEFT JOIN public.land_documents ld ON la.land_applicant_id = ld.land_applicant_id"
			+ " LEFT JOIN public.land_plot lp ON la.land_applicant_id = lp.land_applicant_id"
			+ " LEFT JOIN public.m_block mb ON la.curr_block_id = mb.block_id"
			+ " LEFT JOIN public.m_district md ON la.curr_district_id = md.district_id"
			+ " LEFT JOIN public.m_document_type mdt ON la.doc_type = mdt.doc_type_id"
			+ " LEFT JOIN public.m_gp mgp ON la.curr_gp_id = mgp.gp_id"
			+ " LEFT JOIN public.m_khata_no mkn ON la.plot_khata_no_id = mkn.khata_no_id"
			+ " LEFT JOIN public.m_mouza mm ON la.plot_mouza_id = mm.mouza_id"
			+ " LEFT JOIN public.m_plot_no mpn ON lp.plot_no_id = mpn.plot_no_id"
			+ " LEFT JOIN public.m_state ms ON la.curr_state_id = ms.state_id"
			+ " LEFT JOIN public.m_tehsil mt ON la.curr_district_id = mt.district_id AND la.curr_block_id = mt.tehsil_id WHERE la.land_applicant_id =:landApplicantId", nativeQuery = true)
	Land_applicant findByLandApplicantId(Integer landApplicantId);

	@Query(value = "SELECT \r\n" + " la.land_applicant_id,\r\n" + " la.applicant_no,\r\n" + " la.applicant_name,\r\n"
			+ " la.father_name,\r\n" + "    la.mobile_no,\r\n" + "    la.email_address,\r\n"
			+ " la.doc_ref_no,\r\n" + "    la.docs_path,\r\n" + "    la.curr_police_station,\r\n"
			+ " la.curr_post_office,\r\n" + "    la.curr_street_no,\r\n" + "    la.curr_house_no,\r\n"
			+ " la.curr_pin_code,   \r\n" + "    la.pre_police_station,\r\n" + "    la.pre_post_office,\r\n"
			+ " la.pre_street_no,\r\n" + "    la.pre_house_no,\r\n" + "    la.pre_pin_code,\r\n"
			+ " la.doc_name,\r\n" + "    mb.block_name,\r\n" + "    md.district_name,   \r\n"
			+ " mdt.doc_name,\r\n" + "    mgp.gp_name,\r\n" + "    mkn.khata_no,  \r\n" + "    mm.mouza_name,\r\n"
			+ " ms.state_name,  \r\n" + "    mt.tehsil_name,  \r\n" + " FROM public.land_applicant la\r\n"
			+ "LEFT JOIN public.m_block mb ON la.curr_block_id = mb.block_id\r\n"
			+ "LEFT JOIN public.m_district md ON la.curr_district_id = md.district_id\r\n"
			+ "LEFT JOIN public.m_document_type mdt ON la.doc_type = mdt.doc_type_id\r\n"
			+ "LEFT JOIN public.m_gp mgp ON la.curr_gp_id = mgp.gp_id\r\n"
			+ "LEFT JOIN public.m_khata_no mkn ON la.plot_khata_no_id = mkn.khata_no_id\r\n"
			+ "LEFT JOIN public.m_mouza mm ON la.plot_mouza_id = mm.mouza_id\r\n"
			+ "LEFT JOIN public.m_state ms ON la.curr_state_id = ms.state_id\r\n"
			+ "LEFT JOIN public.m_tehsil mt ON la.curr_district_id = mt.district_id AND la.curr_block_id = mt.tehsil_id\r\n"
			+ "where la.land_applicant_id =:intId AND la.deleted_flag='false' "
			+ "ORDER BY la.applicant_no;", nativeQuery = true)
	Land_applicant getOneDetails(Integer intId);

}