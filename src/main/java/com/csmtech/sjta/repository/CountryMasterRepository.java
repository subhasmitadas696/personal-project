package com.csmtech.sjta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.CountryMaster;

public interface CountryMasterRepository extends JpaRepository<CountryMaster, String> {
	CountryMaster findByTxtCountryCode(String txtCountryCode);

	@Query("From CountryMaster")
	Page<CountryMaster> findAll(Pageable pageRequest);

	@Query(value = "select count(*) from land_bank.country_master", nativeQuery = true)
	Integer countAll();

}
