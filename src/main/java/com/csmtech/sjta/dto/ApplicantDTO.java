package com.csmtech.sjta.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantDTO {
	
	private String applicant_name;
    private String father_name;
    private String mobile_no;
    private String email_address;
    private BigInteger doc_type_id;
    private String doc_ref_no;
    private String docs_path;
    private BigInteger curr_state_id;
    private BigInteger curr_district_id;
    private BigInteger curr_block_id;
    private BigInteger curr_gp_id;
    private BigInteger curr_village_id;
    private String curr_police_station;
    private String curr_post_office;
    private String curr_street_no;
    private String curr_house_no;
    private String curr_pin_code;
    private BigInteger per_state_id;
    private BigInteger per_district_id;
    private BigInteger per_block_id;
    private BigInteger per_gp_id;
    private BigInteger per_village_id;
    private String per_police_station;
    private String per_post_office;
    private String per_street_no;
    private String per_house_no;
    private String per_pin_code;

}
