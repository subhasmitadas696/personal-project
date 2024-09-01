package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.Grievance;

public interface GrievanceRepository extends JpaRepository<Grievance, Integer> {

	Grievance findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

	@Query("From Grievance where bitDeletedFlag=:bitDeletedFlag")
	List<Grievance> findAllByBitDeletedFlag(Boolean bitDeletedFlag, Pageable pageRequest);

	Integer countByBitDeletedFlag(Boolean bitDeletedFlag);

	Grievance findByIntId(Integer intId);

	Grievance findByGrievanceNumber(String grievanceNumber);

	List<Grievance> findByGrievanceStatus(Integer grievanceStatus);
}