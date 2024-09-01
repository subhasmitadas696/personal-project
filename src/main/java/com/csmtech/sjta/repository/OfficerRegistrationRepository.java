package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.RoleEntity;
import com.csmtech.sjta.entity.User;

public interface OfficerRegistrationRepository extends JpaRepository<RoleEntity, Short> {

	@Query(value = "select 	full_name,mobile_no,email_id,department,designation from user_details where user_type='O' and status='false' ", nativeQuery = true)
	List<User> viewOfficer();

}
