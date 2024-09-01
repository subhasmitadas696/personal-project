package com.csmtech.sjta.repository;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmtech.sjta.entity.Bidderregistrara;

public interface BidderregistraraRepository extends JpaRepository<Bidderregistrara, Serializable> {

	@Query("From Bidderregistrara where bitDeletedFlag='0' AND intId=:intId")
	Bidderregistrara getAllRecord(BigInteger intId);

	@Query(value = "select bidder_form_m_application_id, pan_number pan_card_document,  "
			+ "aadhar_number, uploaded_aadhar_card, uploded_resent_signature , "
			+ "uploded_resent_photo_bidder,curr_state, curr_district,curr_block, "
			+ "curr_gp,curr_village,curr_post_office,curr_address_line_1,curr_address_line_2, curr_pin_code, pre_state, pre_district , "
			+ "pre_block, pre_gp , pre_village , pre_post_office , "
			+ "per_address_line_1 , per_address_line_2 , pre_pin_code , tender_auction_id , "
			+ "bidder_form_m_application_number , curr_police_station ,pre_police_station , "
			+ "(Select full_name from user_details where user_id = user_id LIMIT 1) AS user_name ,"
			+ "deleted_flag,created_on,updated_on,updated_by,created_by,user_id ,pan_number, "
			+ "approval_status ,is_curr_addr_same_per_addr ,payment_status ,approval_remarks "
			+ "From application.bidder_form_m_application  where deleted_flag='0' AND created_by=:createdBy order by bidder_form_m_application_id desc ", nativeQuery = true)
	List<Bidderregistrara> getallDataById(Pageable pageRequest, BigInteger createdBy);

	@Query(value = " select count(*) From application.bidder_form_m_application  where deleted_flag='0' AND created_by=:createdBy ", nativeQuery = true)
	Integer countByBitDeletedFlag(BigInteger createdBy);

	@Query(value = " select count(*) From application.bidder_form_m_application  WHERE deleted_flag = '0'  AND payment_status='1' AND approval_status=:approvalStatus ", nativeQuery = true)
	Integer countByBitDeletedFlagFromMEvalucation(@Param("approvalStatus") String approvalStatus);
	
	@Query(value ="select * from application.bidder_form_m_application  where deleted_flag='0' AND bidder_form_m_application_id =:id ",nativeQuery = true)
	Bidderregistrara findByIntId(BigInteger id);
}