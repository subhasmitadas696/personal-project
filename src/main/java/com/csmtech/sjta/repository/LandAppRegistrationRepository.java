package com.csmtech.sjta.repository;

/**
 * @Auth Rashmi Ranjan Jena
 */

import java.io.Serializable;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmtech.sjta.entity.LandAppRegistrationEntity;

public interface LandAppRegistrationRepository extends JpaRepository<LandAppRegistrationEntity, Serializable> {

	LandAppRegistrationEntity findBymobileno(String username);

	LandAppRegistrationEntity findByusername(String userName);

	LandAppRegistrationEntity findByMobilenoAndUserType(String mobileno, String userType);

	@Query(value = " select count(*) " + " from " + " user_details  " + " where  " + " mobile_no=?  "
			+ " AND user_block_status=false " + " AND status='0' ", nativeQuery = true)
	Integer findBymobilenoBlockOrNot(String username);

	@Query(value = "select count(*) " + " from " + " user_details  " + " where  " + " user_name=?  "
			+ " AND user_block_status=false " + " AND status='0' ", nativeQuery = true)
	Integer findByusernameBlockOrNot(String userName);

	@Query(value = "select count(*) " + " from " + " citizen_profile_details  " + " where  " + " user_name=?  "
			+ " AND block_status=false " + " AND status='0' ", nativeQuery = true)
	Integer findCitizenBlockOrNot(String userName);

	@Query(value = " INSERT INTO public.user_details_test (user_name,password, full_name, mobile_no, email_id, otp, user_type, created_by, created_on ) "
			+ " VALUES (    :username ,   :password ,   :fullname , :mobileno , :email  , :otp , :usertype  ,   "
			+ " (SELECT currval(pg_get_serial_sequence('public.user_details_test', 'user_id')) ), "
			+ " CURRENT_TIMESTAMP)", nativeQuery = true)
	public Integer saveUserData(@Param("username") String userName, @Param("password") String password,
			@Param("fullname") String fullName, @Param("mobileno") String mobileNo, @Param("email") String email,
			@Param("otp") String otp, @Param("usertype") String usertype);

	@Query(value = "SELECT user_id, user_name, password, full_name, mobile_no, email_id, otp, created_by, created_on, updated_by, updated_on, user_type, is_first_login, status, district_code, tahasil_code, login_validation , login_validation_time FROM USER_DETAILS WHERE user_name = CAST(:userName AS VARCHAR) AND status = '0' "
			+ "UNION "
			+ "SELECT citizen_profile_details_id as user_id, user_name, password, full_name, mobile_no, email_id, otp, created_by, created_on, updated_by, updated_on, user_type, is_first_login, status, null as district_code, null as tahasil_code,login_validation,login_validation_time FROM citizen_profile_details WHERE user_name = CAST(:userName AS VARCHAR) AND status = '0' ", nativeQuery = true)
	LandAppRegistrationEntity findByUsernameNative(@Param("userName") String userName);

	LandAppRegistrationEntity findByUsernameAndUserType(String userName, String userType);

	LandAppRegistrationEntity findByusernameOrMobileno(String userName, String username);

	@Query(value = "select u.user_id,u.user_name,u.password,u.full_name,u.mobile_no,u.email_id,u.otp,u.user_type,u.created_by,u.created_on,"
			+ "u.updated_by,u.updated_on,u.status,u.is_first_login,u.district_code,u.tahasil_code,u.login_validation_time,u.login_validation "
			+ "FROM public.user_details u " + "inner join public.user_role ur on u.user_id = ur.user_id \r\n"
			+ "where u.user_name =:userName and u.status = '0' and ur.role_id = 11", nativeQuery = true)
	LandAppRegistrationEntity findByUserNameDeletedOrNot(String userName);

	@Query(value = "select u.user_id,u.user_name,u.password,u.full_name,u.mobile_no,u.email_id,u.otp,u.user_type,u.created_by,u.created_on,"
			+ "u.updated_by,u.updated_on,u.status,u.is_first_login,u.district_code,u.tahasil_code,u.login_validation_time,u.login_validation "
			+ "FROM public.user_details u " + "inner join public.user_role ur on u.user_id = ur.user_id \r\n"
			+ "where u.user_name =:userName and u.status = '0' and ur.role_id = 14", nativeQuery = true)
	LandAppRegistrationEntity findByTahasilUserNameDeletedOrNot(String userName);

	@Query(value = "select u.user_id,u.user_name,u.password,u.full_name,u.mobile_no,u.email_id,u.otp,u.user_type,u.created_by,u.created_on,"
			+ "u.updated_by,u.updated_on,u.status,u.is_first_login,u.district_code,u.tahasil_code,u.login_validation_time,u.login_validation "
			+ "FROM public.user_details u " + "inner join public.user_role ur on u.user_id = ur.user_id \r\n"
			+ "where u.tahasil_code =:tahasilCode and u.status = '0' and ur.role_id = 12", nativeQuery = true)
	LandAppRegistrationEntity findByTahasilCode(String tahasilCode);

	@Transactional
	@Modifying
	@Query(value = "update public.citizen_profile_details set login_validation=:value where user_name=:userName ", nativeQuery = true)
	public Integer updateLoginFlagForCitizen(@Param("userName") String userName, @Param("value") Short value);

	@Transactional
	@Modifying
	@Query(value = "update public.citizen_profile_details set login_validation_time = CURRENT_TIMESTAMP where user_name=:userName ", nativeQuery = true)
	public Integer updateLoginFlagForCitizenDate(@Param("userName") String userName);

	@Transactional
	@Modifying
	@Query(value = "update public.citizen_profile_details set login_validation_time = null , login_validation=:validation where user_name=:userName ", nativeQuery = true)
	public Integer updateLoginFlagForCitizenDateForNull(@Param("userName") String userName,
			@Param("validation") Short validation);

	@Query(value = "update public.user_details set login_validation=:value where user_name=:userName ", nativeQuery = true)
	public Integer updateLoginFlagOfficer(@Param("userName") String userName, @Param("value") Short value);

	@Transactional
	@Modifying
	@Query(value = "update public.user_details set login_validation_time = CURRENT_TIMESTAMP where user_name=:userName ", nativeQuery = true)
	public Integer updateLoginFlagForOfficerDate(@Param("userName") String userName);

	@Transactional
	@Modifying
	@Query(value = "update public.user_details set login_validation=:value where user_name=:userName ", nativeQuery = true)
	public Integer updateLoginFlagForOfficer(@Param("userName") String userName, @Param("value") Short value);

	@Transactional
	@Modifying
	@Query(value = "update public.user_details set login_validation_time = null , login_validation=:validation where user_name=:userName ", nativeQuery = true)
	public Integer updateLoginFlagForOfficerDateForNull(@Param("userName") String userName,
			@Param("validation") Short validation);

}
