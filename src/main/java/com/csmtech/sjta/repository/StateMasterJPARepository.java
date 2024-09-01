package com.csmtech.sjta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.State_master;

public interface StateMasterJPARepository extends JpaRepository<State_master, String> {

	@Query("From State_master ")
	Page<State_master> findAll(Pageable pageRequest);

	@Query(value = "select count(*) from  land_bank.state_master", nativeQuery = true)
	Integer countAll();

	State_master findByTxtStateCode(String txtStateCode);
}