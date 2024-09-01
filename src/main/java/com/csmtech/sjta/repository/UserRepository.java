package com.csmtech.sjta.repository;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmtech.sjta.entity.User;

/*
 * @Auth  GuruPrasad
 */

public interface UserRepository extends JpaRepository<User, Serializable> {

	User findByUserName(String userName);

	User findBymobileNo(String mobileNo);

	User findByuserId(BigInteger userId);

	@Query(value = "select * from user_details where user_id=:userId and status='0' ", nativeQuery = true)
	User findByuserIdFalse(@Param("userId") BigInteger userId);

	@Query(value = "SELECT user_id, user_name, password, full_name, mobile_no, email_id, otp, created_by, created_on, updated_by, updated_on, user_type, is_first_login, status, department_id FROM USER_DETAILS WHERE user_name = CAST(:userName AS VARCHAR) "
			+ "UNION "
			+ "SELECT citizen_profile_details_id as user_id, user_name, password, full_name, mobile_no, email_id, otp, created_by, created_on, updated_by, updated_on, user_type, is_first_login, status, null as department_id FROM citizen_profile_details WHERE user_name = CAST(:userName AS VARCHAR)", nativeQuery = true)
	User findByuserNameFalse(@Param("userName") String userName);

	List<User> findByUserNameOrMobileNo(String userName, String mobileNo);

}
