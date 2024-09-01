package com.csmtech.sjta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.csmtech.sjta.entity.QuarryApplicant;

public interface QuarryApplicantRepository extends JpaRepository<QuarryApplicant, Integer> {
	QuarryApplicant findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

	@Query("From QuarryApplicant where deleted_flag=:bitDeletedFlag")
	List<QuarryApplicant> findAllByBitDeletedFlag(Boolean bitDeletedFlag);
}