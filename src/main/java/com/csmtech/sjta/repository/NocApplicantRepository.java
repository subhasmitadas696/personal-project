package com.csmtech.sjta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import com.csmtech.sjta.entity.Noc_applicant;

public interface NocApplicantRepository extends JpaRepository<Noc_applicant, Integer> {
	Noc_applicant findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

	@Query("From Noc_applicant where deleted_flag=:bitDeletedFlag")
	List<Noc_applicant> findAllByBitDeletedFlag(Boolean bitDeletedFlag);
	
	
}