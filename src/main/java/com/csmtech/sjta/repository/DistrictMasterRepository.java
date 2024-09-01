package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.DistrictBoundary;
import com.csmtech.sjta.entity.DistrictMaster;

public interface DistrictMasterRepository extends JpaRepository<DistrictMaster, String> {

	@Query("From DistrictMaster where (:txtDistrictCode='0' or txtDistrictCode like CONCAT('%',:txtDistrictCode,'%')) ")
	List<DistrictMaster> findAll(Pageable pageRequest, String txtDistrictCode);

	@Query(value = "select count(*) from land_bank.district_master", nativeQuery = true)
	Integer countByAll();

	DistrictMaster findByTxtDistrictCode(String txtDistrictCode);

	@Query(value = "Select district_code,district_name,\r\n"
			+ "CAST(st_extent(ST_Transform(geom,3857))AS character varying) \r\n"
			+ "as extent from land_bank.district_boundary \r\n"
			+ "GROUP By district_code,district_name,geom", nativeQuery = true)
	List<DistrictBoundary> viewAll();

	@Query(value = "Select * from land_bank.district_master where state_code = 'OD' Order By district_name", nativeQuery = true)
	List<DistrictMaster> findAllOrderByTxtDistrictName();

}