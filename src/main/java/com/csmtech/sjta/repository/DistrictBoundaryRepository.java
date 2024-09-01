package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.DistrictBoundary;

public interface DistrictBoundaryRepository extends JpaRepository<DistrictBoundary, Integer> {

	@Query(value = "SELECT\r\n"
			+ "    dm.district_code AS district_id,\r\n"
			+ "    dm.district_name AS district_name,\r\n"
			+ "    CAST(st_extent(ST_Transform(db.geom, 3857)) AS character varying) AS extent\r\n"
			+ "FROM\r\n"
			+ "    land_bank.district_master dm\r\n"
			+ "JOIN land_bank.tahasil_master ki ON ki.district_code = dm.district_code\r\n"
			+ "JOIN land_bank.village_master kii ON ki.tahasil_code = kii.tahasil_code\r\n"
			+ "JOIN land_bank.khatian_information kiii ON kii.village_code = kiii.village_code\r\n"
			+ "JOIN (\r\n"
			+ "    SELECT\r\n"
			+ "        district_code,\r\n"
			+ "        district_name,\r\n"
			+ "        geom\r\n"
			+ "    FROM\r\n"
			+ "        land_bank.district_boundary\r\n"
			+ "    GROUP BY\r\n"
			+ "        district_code,\r\n"
			+ "        district_name,\r\n"
			+ "        geom\r\n"
			+ ") db ON dm.district_code = db.district_code\r\n"
			+ "WHERE\r\n"
			+ "    dm.state_code = 'OD'\r\n"
			+ "GROUP BY\r\n"
			+ "    dm.district_code,\r\n"
			+ "    dm.district_name, db.geom\r\n"
			+ "ORDER BY\r\n"
			+ "    district_name;", nativeQuery = true)
	List<Object[]> viewAll();

}
