package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.PlotBoundary;

public interface PlotBoundaryRepository extends JpaRepository<PlotBoundary, Integer> {
	@Query(value = "select pi.plot_code,CAST(ST_extent(ST_Transform(pb.geom, 3857)) AS character varying) AS extent from\r\n"
			+ "land_bank.plot_information pi inner join land_bank.khatian_information k on pi.khatian_code = k.khatian_code inner join \r\n"
			+ "land_bank.sjta_plot_boundary pb on pi.plot_code = pb.plot_code \r\n"
			+ "where k.village_code = :villageCode and k.khata_no = :khataNo group by pi.plot_code ", nativeQuery = true)
	List<Object[]> viewAll(String villageCode, String khataNo);

}