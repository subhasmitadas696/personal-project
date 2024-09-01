/**
 * 
 */
package com.csmtech.sjta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.csmtech.sjta.entity.BlockUser;

public interface BlockUserRepository extends JpaRepository<BlockUser, Long> {
	@Transactional
	@Modifying
	@Query(value = "UPDATE user_details SET user_block_status = :userBlockStatus, block_remarks = :blockRemarks WHERE user_id = :userId", nativeQuery = true)
	void updateUserBlockStatusAndRemarks(Long userId, Boolean userBlockStatus, String blockRemarks);
	
	@Query(value = "SELECT user_id,user_block_status FROM user_details WHERE user_id = ?1", nativeQuery = true)
	Long checkUserExists(Long userId);
	
	@Query(value = "SELECT user_block_status FROM user_details WHERE user_id = ?1", nativeQuery = true)
	Boolean checkBlockStatus(Long userId);

	@Query(value = "SELECT citizen_profile_details_id, block_status FROM citizen_profile_details WHERE citizen_profile_details_id = ?1", nativeQuery = true)
	Long checkCitizenExists(Long userId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE citizen_profile_details SET block_status = :userBlockStatus, block_remarks = :blockRemarks WHERE citizen_profile_details_id = :userId", nativeQuery = true)
	void updateCitizenBlockStatusAndRemarks(Long userId, Boolean userBlockStatus, String blockRemarks);

}