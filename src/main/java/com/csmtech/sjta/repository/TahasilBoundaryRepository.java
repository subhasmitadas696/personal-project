package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.TahasilBoundary;

public interface TahasilBoundaryRepository extends JpaRepository<TahasilBoundary, Integer> {

	@Query(value = "SELECT\r\n"
			+ "    ki.tahasil_code AS tehsil_id,\r\n"
			+ "    ki.tahasil_name AS tehsil_name,\r\n"
			+ "    CAST(st_extent(ST_Transform(tb.geom, 3857)) AS character varying) AS extent\r\n"
			+ "FROM\r\n"
			+ "    land_bank.tahasil_master ki\r\n"
			+ "JOIN land_bank.village_master kii ON ki.tahasil_code = kii.tahasil_code\r\n"
			+ "JOIN land_bank.khatian_information kiii ON kii.village_code = kiii.village_code\r\n"
			+ "LEFT JOIN land_bank.tahasil_boundary tb ON ki.tahasil_code = tb.tahasil_code\r\n"
			+ "WHERE\r\n"
			+ "    ki.district_code = :districtCode  \r\n"
			+ "GROUP BY\r\n"
			+ "    ki.tahasil_code,\r\n"
			+ "    ki.tahasil_name,\r\n"
			+ "    tb.geom\r\n"
			+ "ORDER BY\r\n"
			+ "    tehsil_name; ", nativeQuery = true)
	List<Object[]> viewAll(String districtCode);

}
