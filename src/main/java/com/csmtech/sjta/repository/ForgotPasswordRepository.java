package com.csmtech.sjta.repository;

/**
 * @author prasanta.sethi
 */

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmtech.sjta.entity.CitizenProfileEntity;
import com.csmtech.sjta.entity.LandAppRegistrationEntity;

public interface ForgotPasswordRepository extends JpaRepository<LandAppRegistrationEntity, Serializable> {

	@Query(value = "SELECT user_id, user_name, password, full_name, mobile_no, email_id, otp, created_by, created_on, updated_by, updated_on, user_type, is_first_login, status, district_code, tahasil_code, login_validation,login_validation_time FROM USER_DETAILS WHERE user_name = CAST(:mobileNo AS VARCHAR) "
			+ "UNION "
			+ "SELECT citizen_profile_details_id as user_id, user_name, password, full_name, mobile_no, email_id, otp, created_by, created_on, updated_by, updated_on, user_type, is_first_login, status, null as district_code, null as tahasil_code, login_validation,login_validation_time FROM citizen_profile_details WHERE user_name = CAST(:mobileNo AS VARCHAR)", nativeQuery = true)
	LandAppRegistrationEntity findByUsername(@Param("mobileNo") String mobileNo);

	@Query(value = "SELECT password FROM USER_DETAILS WHERE user_name = CAST(:mobileNo AS VARCHAR) " + "UNION "
			+ "SELECT password FROM citizen_profile_details WHERE user_name = CAST(:mobileNo AS VARCHAR)", nativeQuery = true)
	String passwordValidate(String mobileNo);

	@Query(value = "SELECT user_id, user_name, password, full_name, mobile_no, email_id, otp, created_by, created_on, updated_by, updated_on, user_type, is_first_login, status, district_code, tahasil_code, login_validation,login_validation_time FROM USER_DETAILS WHERE mobile_no=:mobileNo and status = '0'", nativeQuery = true)
	LandAppRegistrationEntity mobileOfficerUser(String mobileNo);

	@Query(value = "SELECT citizen_profile_details_id as user_id, user_name, password, full_name, mobile_no, email_id, otp, created_by, created_on, updated_by, updated_on, user_type, is_first_login, status, null as district_code, null as tahasil_code, login_validation,login_validation_time FROM citizen_profile_details WHERE mobile_no=:mobileNo and status = '0'", nativeQuery = true)
	LandAppRegistrationEntity mobileCitizenUser(String mobileNo);

//	@Query("SELECT la.password FROM LandAppRegistrationEntity la WHERE la.username = :mobileNo")
//	String passwordValidate(String mobileNo);

}
