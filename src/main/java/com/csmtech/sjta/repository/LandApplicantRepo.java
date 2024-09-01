package com.csmtech.sjta.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.LandApplicant;

public interface LandApplicantRepo extends JpaRepository<LandApplicant, BigInteger> {

	@Query(value = "SELECT la.land_applicant_id, la.applicant_no, la.applicant_name, la.father_name, la.mobile_no, la.email_address, la.doc_ref_no, la.docs_path, la.curr_police_station, la.curr_post_office, la.curr_street_no, la.curr_house_no, la.curr_pin_code, la.pre_police_station, la.pre_post_office, la.pre_street_no, la.pre_house_no, la.pre_pin_code, la.doc_name, mb.block_name, md.district_name, mdt.doc_name, mgp.gp_name, mkn.khata_no, mm.mouza_name, ms.state_name, mt.tehsil_name \r\n"
			+ "FROM public.land_applicant la \r\n"
			+ "LEFT JOIN public.m_block mb ON la.curr_block_id = mb.block_id \r\n"
			+ "LEFT JOIN public.m_district md ON la.curr_district_id = md.district_id \r\n"
			+ "LEFT JOIN public.m_document_type mdt \r\n" + "ON la.doc_type = mdt.doc_type_id \r\n"
			+ "LEFT JOIN public.m_gp mgp ON la.curr_gp_id = mgp.gp_id \r\n"
			+ "LEFT JOIN public.m_khata_no mkn ON la.plot_khata_no_id = mkn.khata_no_id \r\n"
			+ "LEFT JOIN public.m_mouza mm ON la.plot_mouza_id = mm.mouza_id \r\n"
			+ "LEFT JOIN public.m_state ms ON la.curr_state_id = ms.state_id \r\n"
			+ "LEFT JOIN public.m_tehsil mt ON la.curr_district_id = mt.district_id AND la.curr_block_id = mt.tehsil_id where la.land_applicant_id =:landApplicantId AND la.deleted_flag='false' \r\n"
			+ "ORDER BY la.applicant_no;", nativeQuery = true)
	LandApplicant getDetails(BigInteger landApplicantId);

}
