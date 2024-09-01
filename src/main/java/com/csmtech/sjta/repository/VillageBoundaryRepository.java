package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.DistrictBoundary;
import com.csmtech.sjta.entity.TahasilBoundary;
import com.csmtech.sjta.entity.VillageBoundary;

public interface VillageBoundaryRepository extends JpaRepository<VillageBoundary, Integer> {

	@Query(value = "SELECT\r\n"
			+ "    vm.village_code AS mouza_id,\r\n"
			+ "    vm.village_name AS mouza_name,\r\n"
			+ "    CAST(ST_extent(ST_Transform(vb.geom, 3857)) AS character varying) AS extent\r\n"
			+ "FROM\r\n"
			+ "    land_bank.village_master vm\r\n"
			+ "LEFT JOIN land_bank.village_boundary vb ON vm.village_code = vb.village_code\r\n"
			+ "JOIN land_bank.khatian_information kiii ON vm.village_code = kiii.village_code\r\n"
			+ "WHERE\r\n"
			+ "    vm.tahasil_code = :tahasilCode \r\n"
			+ "GROUP BY\r\n"
			+ "    vm.village_code,\r\n"
			+ "    vm.village_name,\r\n"
			+ "    vb.geom\r\n"
			+ "ORDER BY\r\n"
			+ "    vm.village_name; ", nativeQuery = true)
	List<Object[]> viewAll(String tahasilCode);

}