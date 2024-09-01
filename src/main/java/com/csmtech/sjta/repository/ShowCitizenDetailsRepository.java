package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmtech.sjta.dto.CitizenInfoDTO;
import com.csmtech.sjta.entity.LandAppRegistrationEntity;

/**
 * @author prasanta.sethi
 */

public interface ShowCitizenDetailsRepository extends JpaRepository<LandAppRegistrationEntity, Long> {

	@Query(nativeQuery = true, value = "SELECT full_name, mobile_no, user_block_status FROM user_details "
			+ "WHERE mobile_no LIKE %:mobileNo%")
	List<CitizenInfoDTO> findByMobilenoAndStatusFalse(@Param("mobileNo") String mobileNo);

	@Query(value = "SELECT user_id,user_type,full_name, mobile_no, email_id,user_block_status " + "FROM user_details "
			+ "WHERE (LOWER(mobile_no) LIKE LOWER(CONCAT('%', :searchKeyword, '%')) OR "
			+ "LOWER(full_name) LIKE LOWER(CONCAT('%', :searchKeyword, '%')))", nativeQuery = true)
	List<Object[]> searchByMobileNoContainingIgnoreCaseOrFullNameContainingIgnoreCase(String searchKeyword);

}
